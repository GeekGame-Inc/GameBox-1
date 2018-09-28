package com.tenone.gamebox.view.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.ReservedAlarmModel;
import com.tenone.gamebox.view.receiver.ReservedReceiver;
import com.tenone.gamebox.view.utils.MyLog;
import com.tenone.gamebox.view.utils.database.RealmUtils;

import java.util.List;

import io.realm.Realm;

public class ReservedService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra( "gameModel" )) {
            GameModel gameModel = (GameModel) intent.getExtras().get( "gameModel" );
            new MyThread( gameModel.getTime(), gameModel.getGameId() ).start();
        } else {
            List<ReservedAlarmModel> models = RealmUtils.queryReservedAlarmAll();
            for (ReservedAlarmModel model : models) {
                MyLog.d( "gameId is  " + model.getGameId() );
                new MyThread( model.getTime(), model.getGameId() ).start();
            }
        }
        return START_STICKY;
    }

    private class MyThread extends Thread {
        private String time;
        private int gameId;

        public MyThread(String time, int gameId) {
            this.time = time;
            this.gameId = gameId;
        }


        @Override
        public void run() {
            setAlarm( time, gameId );
        }

        private void setAlarm(String t, int gameId) {
            AlarmManager manager = (AlarmManager) getSystemService( ALARM_SERVICE );
            Intent i = new Intent( ReservedService.this, ReservedReceiver.class );
            i.putExtra( "gameId", gameId );
            PendingIntent pi = PendingIntent.getBroadcast( ReservedService.this, Integer
                    .valueOf( t ).intValue(), i, 0 );
            long time = Long.valueOf( t ) * 1000;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                manager.setExact( AlarmManager.RTC_WAKEUP, time, pi );
            } else {
                manager.set( AlarmManager.RTC_WAKEUP, time, pi );
            }
        }
    }


    @Override
    public void onDestroy() {
        Realm.getDefaultInstance().close();
        super.onDestroy();
    }
}
