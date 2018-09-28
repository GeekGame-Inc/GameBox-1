package com.tenone.gamebox.view.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tenone.gamebox.view.utils.MyLog;

public class UpAndNoticeReceiver extends BroadcastReceiver {

    private OnUpAndNoticeListener onUpAndNoticeListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && onUpAndNoticeListener != null) {
            if (intent.hasExtra( "updateUrl" )) {
                String updateUrl = intent.getStringExtra( "updateUrl" );
                MyLog.d( "updateUrl is " + updateUrl );
                onUpAndNoticeListener.onUpCallback( updateUrl );
            }

            if (intent.hasExtra( "title" ) && intent.hasExtra( "content" )) {
                String title = intent.getStringExtra( "title" );
                String content = intent.getStringExtra( "content" );
                onUpAndNoticeListener.onNoticeCallback( title, content );
                MyLog.d( "title is " + title + " content is " + content );
            }

        }
    }

    public void setOnUpAndNoticeListener(OnUpAndNoticeListener onUpAndNoticeListener) {
        this.onUpAndNoticeListener = onUpAndNoticeListener;
    }

    public interface OnUpAndNoticeListener {

        void onUpCallback(String upUrl);

        void onNoticeCallback(String title, String content);
    }

}
