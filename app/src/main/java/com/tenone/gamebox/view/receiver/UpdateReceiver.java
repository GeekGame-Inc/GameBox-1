package com.tenone.gamebox.view.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tenone.gamebox.view.utils.ListenerManager;
public class UpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.hasExtra( "progerss" )) {
            int progerss = intent.getExtras().getInt( "progerss" );
            String apkName = intent.getExtras().getString( "apkName" );
            Log.i( "MainActivity", "listener != null" );
            ListenerManager.sendUpdateListener( progerss, apkName );
        }
    }
}
