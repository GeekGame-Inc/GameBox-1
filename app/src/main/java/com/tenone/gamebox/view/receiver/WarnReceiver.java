package com.tenone.gamebox.view.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
public class WarnReceiver extends BroadcastReceiver {
    WarnNotifiacticonListener warnNotifiacticonListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (warnNotifiacticonListener != null) {
            warnNotifiacticonListener.notificaticonFun( TextUtils.equals( "open_notification", intent.getAction() ) );
        }
    }

    public void setWarnNotifiacticonListener(
            WarnNotifiacticonListener warnNotifiacticonListener) {
        this.warnNotifiacticonListener = warnNotifiacticonListener;
    }


    public interface WarnNotifiacticonListener {
        void notificaticonFun(boolean b);
    }

}
