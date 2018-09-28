package com.tenone.gamebox.view.receiver;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.ReservedAlarmModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.MyLog;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.database.RealmUtils;

public class ReservedReceiver extends BroadcastReceiver implements HttpResultListener {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.hasExtra( "gameId" )) {
            int gameId = intent.getIntExtra( "gameId", 0 );
            ReservedAlarmModel model = RealmUtils.queryReservedAlarm( gameId );
            if (model != null) {
							String content = "\u6e38\u620f\u300a" + model.getGameName() + "\u300b\u5df2\u7ecf\u4e0a\u7ebf\u5566!!!\u5feb\u6765\u73a9\u513f\u5427!!!\u4e0d\u8981\u9519\u8fc7\u54df!!!";
                if (SpUtil.getNotification()) {
                    String channelId = "185" + gameId;
                    String channelName = "185" + gameId + "ԤԼ֪ͨ";
                    Intent updateIntent = new Intent();
                    ComponentName localComponentName = new ComponentName( "com.tenone.gamebox",
                            "com.tenone.gamebox.view.activity.NewGameDetailsActivity" );
                    updateIntent.setComponent( localComponentName );
                    updateIntent.putExtra( "id", String.valueOf( model.getGameId() ) );
                    @SuppressLint("WrongConstant")
                    PendingIntent updatePendingIntent = PendingIntent.getActivity( context, 0, updateIntent,
                            Intent.FLAG_ACTIVITY_NEW_TASK );
                    NotificationManager manager = (NotificationManager) context
                            .getSystemService( Context.NOTIFICATION_SERVICE );
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel( channelId,
                                channelName, NotificationManager.IMPORTANCE_HIGH );
                        if (manager != null) {
                            manager.createNotificationChannel( channel );
                        }
                    }
                    NotificationCompat.Builder builder = new NotificationCompat.Builder( context );
                    builder.setSound( null );
                    if (SpUtil.getSound() && SpUtil.getShake()) {
                        builder.setDefaults( Notification.DEFAULT_ALL );
                    } else if (SpUtil.getSound() && !SpUtil.getShake()) {
                        builder.setVibrate( null );
                        builder.setDefaults( Notification.DEFAULT_SOUND );
                    } else if (!SpUtil.getSound() && SpUtil.getShake()) {
                        builder.setSound( null );
                        builder.setDefaults( Notification.DEFAULT_VIBRATE );
                    } else if (!SpUtil.getSound() && !SpUtil.getShake()) {
                        builder.setSound( null );
                        builder.setVibrate( null );
                    }
                    Notification notification = builder
                            .setContentTitle( "\u4e0a\u7ebf\u901a\u77e5" )
                            .setContentText( content )
                            .setWhen( System.currentTimeMillis() )
                            .setSmallIcon( R.drawable.icon_luncher )
                            .setChannelId( channelId )
                            .setContentIntent( updatePendingIntent ).build();
                    manager.notify( model.getGameId(), notification );
                }
                RealmUtils.deleteReservedAlarm( model );
                HttpManager.reserveSuccess( context, 10, this, gameId );
            }
        }
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        MyLog.d( "reserved called success" );
    }

    @Override
    public void onError(int what, String error) {
        MyLog.d( "reserved called error" );
    }
}
