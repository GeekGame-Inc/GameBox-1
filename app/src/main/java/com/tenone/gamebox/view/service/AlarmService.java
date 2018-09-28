package com.tenone.gamebox.view.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;

import com.tenone.gamebox.mode.mode.OpenServiceNotificationMode;
import com.tenone.gamebox.view.receiver.AlarmReceiver;
import com.tenone.gamebox.view.utils.DatabaseUtils;

import java.util.List;

@SuppressLint("NewApi")
public class AlarmService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra( "OpenServiceNotificationMode" )) {
            OpenServiceNotificationMode mode = (OpenServiceNotificationMode) intent
                    .getExtras().get( "OpenServiceNotificationMode" );
            AlarmManager manager = (AlarmManager) getSystemService( ALARM_SERVICE );
            Intent i = new Intent( this, AlarmReceiver.class );
            i.putExtra( "serviceId", mode.getServiceId() );
            i.putExtra( "gameId", mode.getGameId() );
            PendingIntent pi = PendingIntent.getBroadcast( this, Integer
                    .valueOf( mode.getServiceId() ).intValue(), i, 0 );
            if (TextUtils.equals( "insert",intent.getAction() )) {
                long time = Long.valueOf( mode.getTime() ) * 1000;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    manager.setExact( AlarmManager.RTC_WAKEUP, time, pi );
                } else {
                    manager.set( AlarmManager.RTC_WAKEUP, time, pi );
                }
            } else if (TextUtils.equals( "delete",intent.getAction() )) {
                manager.cancel( pi );
            }
        } else {
            List<OpenServiceNotificationMode> modes = DatabaseUtils
                    .getInstanse( getApplicationContext() ).getWarns();
            for (OpenServiceNotificationMode openServiceNotificationMode : modes) {
                AlarmManager manager = (AlarmManager) getSystemService( ALARM_SERVICE );
                Intent i = new Intent( this, AlarmReceiver.class );
                i.putExtra( "serviceId",
                        openServiceNotificationMode.getServiceId() );
                i.putExtra( "gameId", openServiceNotificationMode.getGameId() );
                PendingIntent pi = PendingIntent.getBroadcast( this, Integer
                        .valueOf( openServiceNotificationMode.getServiceId() )
                        .intValue(), i, 0 );
                long time = Long.valueOf( openServiceNotificationMode.getTime() ) * 1000;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    manager.setExact( AlarmManager.RTC_WAKEUP, time, pi );
                } else {
                    manager.set( AlarmManager.RTC_WAKEUP, time, pi );
                }
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
