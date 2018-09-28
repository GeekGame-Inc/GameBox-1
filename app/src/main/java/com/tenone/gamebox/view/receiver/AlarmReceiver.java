
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
import android.text.TextUtils;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.OpenServiceNotificationMode;
import com.tenone.gamebox.view.utils.DatabaseUtils;
import com.tenone.gamebox.view.utils.SpUtil;

public class AlarmReceiver extends BroadcastReceiver {

    private DatabaseUtils databaseUtils;

    @Override
    public void onReceive(Context context, Intent intent) {
        databaseUtils = DatabaseUtils.getInstanse( context );
        if (intent != null && intent.hasExtra( "serviceId" )) {
            String id = intent.getExtras().getString( "serviceId" );
            if (!TextUtils.isEmpty( id )) {
                String gameId = intent.getExtras().getString( "gameId" );
                OpenServiceNotificationMode notificationMode = databaseUtils
                        .getWarn( id, gameId );
							String content = "\u6e38\u620f\u300a" + notificationMode.getGameName() + "\u300b"
									+ notificationMode.getServiceId() + context.getResources().getString( R.string.open_notify_text );
                String channelId = "185" + gameId + notificationMode.getServiceId();
                String channelName = "185" + gameId + notificationMode.getServiceId() + "\u5f00\u670d\u901a\u77e5";
                databaseUtils.deleteWarn( notificationMode );
                databaseUtils.insertAlready( notificationMode );
                Intent mIntent = new Intent();
                mIntent.setAction( "open_notification" );
                context.sendBroadcast( mIntent );
                if (SpUtil.getNotification()) {
                    Intent updateIntent = new Intent();
                    ComponentName localComponentName = new ComponentName( "com.tenone.gamebox",
                            "com.tenone.gamebox.view.activity.MyMessageActivity" );
                    updateIntent.setComponent( localComponentName );
                    updateIntent.setAction( "notification" );
                    @SuppressLint("WrongConstant")
                    PendingIntent updatePendingIntent = PendingIntent.getActivity( context, 0,
                            updateIntent, Intent.FLAG_ACTIVITY_NEW_TASK );
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
                            .setContentTitle( context.getResources().getString( R.string.open_notify ) )
                            .setContentText( content )
                            .setWhen( System.currentTimeMillis() )
                            .setSmallIcon( R.drawable.icon_luncher )
                            .setChannelId( channelId )
                            .setContentIntent( updatePendingIntent ).build();
                    String str = notificationMode.getServiceId();
                    if (!TextUtils.isEmpty( str )) {
                        manager.notify( Integer.valueOf( str ).intValue(), notification );
                    }
                }
            }
        }
    }
}
