package com.tenone.gamebox.view.utils;

import android.content.Context;
import android.util.Log;

import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.ResultItem;


public class GDTActionUtils {
    private static boolean isStartGDTAction = false;

    public static boolean isIsStartGDTAction() {
        return isStartGDTAction;
    }

    public static void setIsStartGDTAction(boolean isStartGDTAction) {
        GDTActionUtils.isStartGDTAction = isStartGDTAction;
    }

    public static void reportData(Context context, int type) {
        Log.i( "COdeTimeTemp", "reportData time is " + System.currentTimeMillis() );
        if (isIsStartGDTAction()) {
            HttpManager.reportData( context, 0, new HttpResultListener() {
							@Override
							public void onSuccess(int what, ResultItem resultItem) {
								SpUtil.setGdtIsFirst( false );
							}

							@Override
							public void onError(int what, String error) {

							}
						}, type );
        }
    }
}
