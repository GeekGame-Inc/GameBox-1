package com.tenone.gamebox.view.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.tenone.gamebox.view.base.BaseActivity;

/**
 * Created by Eddy on 2018/1/15.
 */

public class WindowBackgroundUtils {

    private static float alpha = 1.0f;
    private static Handler mHandler;
    private static Context context;

    public WindowBackgroundUtils(Context context,Handler mHandler,float alpha) {
        WindowBackgroundUtils.alpha = alpha;
        WindowBackgroundUtils.mHandler = mHandler;
        WindowBackgroundUtils.context = context;
    }

    private static WindowBackgroundUtils instance;

    public static WindowBackgroundUtils getInstance(Context context,Handler mHandler,float alpha) {
        return new WindowBackgroundUtils(context,mHandler,alpha);
    }

    public void toDark() {
        new Thread( () -> {
						while (alpha > 0.5f) {
								try {
										Thread.sleep( 4 );
								} catch (InterruptedException e) {
										e.printStackTrace();
								}
								Message msg = mHandler.obtainMessage();
								msg.what = 1;
								alpha -= 0.01f;
								msg.obj = alpha;
								mHandler.sendMessage( msg );
						}
				} ).start();
    }

    public void toLight() {
        new Thread( () -> {
						while (alpha < 1f) {
								try {
										Thread.sleep( 4 );
								} catch (InterruptedException e) {
										e.printStackTrace();
								}
								Message msg = mHandler.obtainMessage();
								msg.what = 1;
								alpha += 0.01f;
								msg.obj = alpha;
								mHandler.sendMessage( msg );
						}
				} ).start();
    }

    public void backgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = ((BaseActivity) context).getWindow()
                .getAttributes();
        lp.alpha = alpha; // 0.0-1.0
        ((BaseActivity) context).getWindow().setAttributes( lp );
        ((BaseActivity) context).getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DIM_BEHIND );
    }
}
