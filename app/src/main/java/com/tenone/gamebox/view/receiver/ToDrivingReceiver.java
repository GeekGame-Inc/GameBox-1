package com.tenone.gamebox.view.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ToDrivingReceiver extends BroadcastReceiver {

    private ToDrivingListener toDrivingListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && "ToDriving".equals( intent.getAction() ) && toDrivingListener != null) {
            toDrivingListener.OnToDriving();
        }
    }

    public void setToDrivingListener(ToDrivingListener toDrivingListener) {
        this.toDrivingListener = toDrivingListener;
    }

    public interface ToDrivingListener {
        void OnToDriving();
    }

}
