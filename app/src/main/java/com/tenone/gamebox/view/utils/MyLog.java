package com.tenone.gamebox.view.utils;

import android.util.Log;

public class MyLog {

    private static final String TAG = "185GameBox";

    public static int v(String msg) {
        return Log.v( TAG, msg );
    }
    public static int i(String msg) {
        return Log.i( TAG, msg );
    }

    public static int e(String msg) {
        return Log.e( TAG, msg );
    }

    public static int d(String msg) {
        return Log.d( TAG, msg );
    }

    public static int v(String tag,String msg) {
        return Log.v( tag, msg );
    }
    public static int i(String tag,String msg) {
        return Log.i( tag, msg );
    }

    public static int e(String tag,String msg) {
        return Log.e( tag, msg );
    }

    public static int d(String tag,String msg) {
        return Log.d( tag, msg );
    }
}
