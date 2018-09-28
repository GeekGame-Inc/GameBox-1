/**
 * Project Name:GameBox
 * File Name:AddDownloadWindow.java
 * Package Name:com.tenone.gamebox.view.custom.popupwindow
 * Date:2017-4-6����4:01:44
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.custom.popupwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ClassName:AddDownloadWindow <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-6 ����4:01:44 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */
@SuppressWarnings("deprecation")
@SuppressLint({"InflateParams", "HandlerLeak"})
public class AddDownloadWindow extends PopupWindow {
    private Context mContext;
    private String message;
    private View view;
    private long time;

    private Timer timer;
    private TimerTask timerTask;

    public AddDownloadWindow(Context cxt, String message, long time) {
        this.mContext = cxt;
        this.message = message;
        this.time = time;
        // ���õ�����ɵ��
        setOutsideTouchable( true );
        setBackgroundDrawable( new BitmapDrawable() );
        int width = DisplayMetricsUtils.getScreenWidth( mContext )
                - DisplayMetricsUtils.dipTopx( mContext, 53 );
        setWidth( width );
        setHeight( LayoutParams.WRAP_CONTENT );
        setAnimationStyle( R.style.PopupAnimation );
        setBackgroundDrawable( getBackground() );
        view = LayoutInflater.from( mContext ).inflate(
                R.layout.dialog_detection_update, null );
        setContentView( view );
        initView();
    }

    private void initView() {
        TextView messageTextView = view
                .findViewById( R.id.id_detection_message );
        messageTextView.setText( message );
    }

    public void showCenter(View v) {
        showAtLocation( v, Gravity.CENTER, 0, 0 );
        if (time > 0) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    view.post( () -> {
                        dismiss();
                    } );
                }
            };
            timer.schedule( timerTask, time, time );
        }
    }

    @Override
    public void dismiss() {
        cancleTimer();
        super.dismiss();
    }

    private void cancleTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
