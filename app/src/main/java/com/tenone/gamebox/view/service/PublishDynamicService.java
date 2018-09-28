package com.tenone.gamebox.view.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnDataRefreshListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.BitmapUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.TrackingUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eddy on 2018/3/9.
 */

public class PublishDynamicService extends Service {

    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            if (!TextUtils.isEmpty( content )) {
                HttpManager.publishDynamics( HttpType.REFRESH, PublishDynamicService.this, new HttpResultListener() {
                    @Override
                    public void onSuccess(int what, ResultItem resultItem) {
                        TrackingUtils.setEvent( TrackingUtils.PUBLISGDYNAMIC, TrackingUtils.getUserInfoMap() );
                        if (1 == resultItem.getIntValue( "status" )) {
                            ListenerManager.sendOnDataRefreshListener( OnDataRefreshListener.MIN );
													ToastCustom.makeText( PublishDynamicService.this,
															"\u52a8\u6001\u63d0\u4ea4\u6210\u529f,\u8bf7\u7b49\u5f85\u5ba1\u6838", ToastCustom.LENGTH_SHORT ).show();
                            stopSelf();
                        } else {
													ToastCustom.makeText( PublishDynamicService.this,
															"\u52a8\u6001\u63d0\u4ea4\u5931\u8d25,\u8bf7\u91cd\u65b0\u63d0\u4ea4", ToastCustom.LENGTH_SHORT ).show();
                        }
                    }

                    @Override
                    public void onError(int what, String error) {
                        ToastCustom.makeText( PublishDynamicService.this,
                                error, ToastCustom.LENGTH_SHORT ).show();
                        stopSelf();
                    }
                }, content, files );
            }
            super.dispatchMessage( msg );
        }
    };

    String content = "";
    List<String> paths = new ArrayList<String>();
    File[] files;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra( "content" )) {
            content = intent.getExtras().getString( "content" );
        }
        if (intent != null && intent.hasExtra( "list" )) {
            paths = intent.getStringArrayListExtra( "list" );
            if (!BeanUtils.isEmpty( paths )) {
                files = new File[paths.size()];
                new FileThread().start();
            } else {
                handler.sendEmptyMessage( 0 );
            }
        }
        return super.onStartCommand( intent, flags, startId );
    }


    private void setFile() {
        for (int i = 0; i < paths.size(); i++) {
            String path = paths.get( i );
            if (path.contains( ".gif" ) || path.contains( ".GIF" )) {
                files[i] = new File( path );
            } else {
                files[i] = BitmapUtils.compressBmpToFile( BitmapUtils.fileToBitmap( path ),
                        Configuration.cachepath, "dynamicImage" + i );
            }
        }
    }

    private class FileThread extends Thread {
        @Override
        public void run() {
            setFile();
            handler.sendEmptyMessage( 0 );
            super.run();
        }
    }
}
