package com.tenone.gamebox.view.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.utils.FileUtils;
import com.tenone.gamebox.view.utils.HttpUtils;

@SuppressLint("HandlerLeak")
public class DownSplashService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra( "url" )) {
            downUrl = intent.getStringExtra( "url" );
            if (!TextUtils.isEmpty( downUrl )) {
                HttpUtils.downFile( getApplicationContext(), downUrl, handler );
            }
        }
        return super.onStartCommand( intent, flags, startId );
    }

    String downUrl = "";

    Handler handler = new Handler() {
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    byte[] bytes = (byte[]) msg.obj;
                    new SaveBitmapThread( bytes ).start();
                    break;
                case 0:
                    break;
            }
        }
    };

    private class SaveBitmapThread extends Thread {
        byte[] bytes;

        public SaveBitmapThread(byte[] bytes) {
            this.bytes = bytes;
        }

        @Override
        public void run() {
            String newUrl = Configuration.SPLASHIMGPATH + "/";
            Bitmap bitmap = BitmapFactory.decodeByteArray( bytes, 0,
                    bytes.length );
            FileUtils.saveBitmap( newUrl, bitmap,
                    Configuration.SPLASHIMGNAME );
            super.run();
        }
    }
}
