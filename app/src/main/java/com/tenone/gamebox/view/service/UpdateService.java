/**
 * Project Name:GameBox
 * File Name:UpdateService.java
 * Package Name:com.tenone.gamebox.view.service
 * Date:2017-5-9����6:50:10
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.tenone.gamebox.view.utils.SpUtil;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.DownloadStatusConfiguration;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnFileDownloadStatusListener;

public class UpdateService extends Service implements
        OnFileDownloadStatusListener {
    Intent receiverIntent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra( "path" )) {
            String path = intent.getExtras().getString( "path" );
            Log.i( "MainActivity", "path is " + path );
            DownloadStatusConfiguration.Builder builder = new DownloadStatusConfiguration.Builder();
            builder.addListenUrl( path );
            FileDownloader
                    .registerDownloadStatusListener( this, builder.build() );
            FileDownloader.start( path );
            receiverIntent = new Intent();
            receiverIntent.setAction( "app_update" );
            receiverIntent.setComponent( new ComponentName( "com.tenone.gamebox", "com.tenone.gamebox.view.receiver.UpdateReceiver" ) );
        }
        return super.onStartCommand( intent, flags, startId );
    }

    @Override
    public void onFileDownloadStatusCompleted(DownloadFileInfo arg0) {
        Log.i( "MainActivity", "onFileDownloadStatusCompleted " );
        SpUtil.setIsUpdateing( false );
        receiverIntent.putExtra( "progerss", 100 );
        receiverIntent.putExtra( "apkName", arg0.getFileName() );
        sendBroadcast( receiverIntent );
    }

    @Override
    public void onFileDownloadStatusDownloading(DownloadFileInfo arg0,
                                                float arg1, long arg2) {
        times++;
        float fileSize = arg0.getFileSizeLong();
        float downSize = arg0.getDownloadedSizeLong();
        float progress = (downSize / fileSize) * 100;
        if (times == 1) {
            SpUtil.setIsUpdateing( true );
        }
        receiverIntent.putExtra( "progerss", (int) progress );
        String apkName = arg0.getFileName();
        receiverIntent.putExtra( "apkName", apkName );

        sendBroadcast( receiverIntent );
        Log.i( "MainActivity", "onFileDownloadStatusDownloading progerss is " + progress );
    }

    int times = 0;

    @Override
    public void onFileDownloadStatusFailed(String arg0, DownloadFileInfo arg1,
                                           FileDownloadStatusFailReason arg2) {
        Log.i( "MainActivity", "onFileDownloadStatusFailed " );
    }

    @Override
    public void onFileDownloadStatusPaused(DownloadFileInfo arg0) {
        Log.i( "MainActivity", "onFileDownloadStatusPaused " );
    }

    @Override
    public void onFileDownloadStatusPrepared(DownloadFileInfo arg0) {
        Log.i( "MainActivity", "onFileDownloadStatusPrepared " );
    }

    @Override
    public void onFileDownloadStatusPreparing(DownloadFileInfo arg0) {
        Log.i( "MainActivity", "onFileDownloadStatusPreparing " );
    }

    @Override
    public void onFileDownloadStatusWaiting(DownloadFileInfo arg0) {
        Log.i( "MainActivity", "onFileDownloadStatusWaiting " );
    }
}
