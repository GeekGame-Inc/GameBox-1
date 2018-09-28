package com.tenone.gamebox.mode.biz;

import android.app.Activity;
import android.os.Looper;
import android.text.TextUtils;

import com.tenone.gamebox.view.utils.PermissionUtils;

public class RequestPermissionThread extends Thread {
    private Activity activity;
    private String permission;
    private int requestCode = 0;
    private String[] permissions;

    public RequestPermissionThread(Activity activity) {
        this.activity = activity;
    }

    public RequestPermissionThread(Activity activity, int requestCode) {
        this.activity = activity;
        this.requestCode = requestCode;
    }

    public RequestPermissionThread(Activity activity, String permission,
                                   int requestCode) {
        this.activity = activity;
        this.permission = permission;
        this.requestCode = requestCode;
    }

    public RequestPermissionThread(Activity activity, String[] permissions,
                                   int requestCode) {
        this.activity = activity;
        this.permissions = permissions;
        this.requestCode = requestCode;
    }

    public static void requestAllPermissions(Activity activity) {
        new RequestPermissionThread( activity ).start();
    }

    public static void requestAllPermissions(Activity activity, int requestCode) {
        new RequestPermissionThread( activity, requestCode ).start();
    }

    public static void requestPermission(Activity activity, String permission,
                                         int requestCode) {
        new RequestPermissionThread( activity, permission, requestCode ).start();
    }

    public static void requestPermissions(Activity activity,
                                          String[] permissions, int requestCode) {
        new RequestPermissionThread( activity, permissions, requestCode ).start();
    }

    @Override
    public void run() {
        Looper.prepare();
        if (TextUtils.isEmpty( permission )) {
            if (null != permissions && null != permissions && permissions.length > 1) {
                PermissionUtils.requestPermissions( activity, permissions,
                        requestCode );
            } else {
                PermissionUtils.requestMultiPermissions( activity, requestCode );
            }
        } else {
            PermissionUtils.requestPermission( activity, permission, requestCode );
        }
        Looper.loop();
    }
}
