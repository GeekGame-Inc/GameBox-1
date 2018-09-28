package com.tenone.gamebox.view.utils;

import android.content.Context;

import com.tenone.gamebox.view.custom.ToastCustom;

public class ToastUtils {

    public static void showToast(Context context, String text) {
        ToastCustom.makeText( context, text, ToastCustom.LENGTH_SHORT ).show();
    }

    public static void showToast(Context context, int rId) {
        ToastCustom.makeText( context, context.getResources().getString( rId ), ToastCustom.LENGTH_SHORT ).show();
    }
}
