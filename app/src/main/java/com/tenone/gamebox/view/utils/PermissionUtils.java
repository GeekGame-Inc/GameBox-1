/**
 * Project Name:GameBox
 * File Name:PermissionUtils.java
 * Package Name:com.tenone.gamebox.view.utils
 * Date:2017-4-26����2:22:46
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;


@TargetApi(23)
public class PermissionUtils {
    public static final int PERMISSIONSREQUEST = 1;
    private static final String TAG = PermissionUtils.class.getSimpleName();
    public static final int CODE_RECORD_AUDIO = 0;
    public static final int CODE_GET_ACCOUNTS = 1;
    public static final int CODE_READ_PHONE_STATE = 2;
    public static final int CODE_CALL_PHONE = 3;
    public static final int CODE_CAMERA = 4;
    public static final int CODE_ACCESS_FINE_LOCATION = 5;
    public static final int CODE_ACCESS_COARSE_LOCATION = 6;
    public static final int CODE_READ_EXTERNAL_STORAGE = 7;
    public static final int CODE_WRITE_EXTERNAL_STORAGE = 8;
    public static final int CODE_MULTI_PERMISSION = 100;
    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String PERMISSION_INTERNET = Manifest.permission.INTERNET;
    public static final String PERMISSION_WIFI = Manifest.permission.ACCESS_WIFI_STATE;
    public static final String PERMISSION_BLUETOOTH = Manifest.permission.BLUETOOTH;
    public static final String PERMISSION_RECEIVE_BOOT_COMPLETED = Manifest.permission.RECEIVE_BOOT_COMPLETED;

    public static final String PERMISSION_RESTART_PACKAGES = Manifest.permission.RESTART_PACKAGES;

    public static final String PERMISSION_ACCESS_NETWORK_STATE = Manifest.permission.ACCESS_NETWORK_STATE;

    public static final String PERMISSION_INSTALL_SHORTCUT = Manifest.permission.INSTALL_SHORTCUT;
    public static final String PERMISSION_UNMOUNT_FILESYSTEMS = Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS;
    public static final String PERMISSION_GROUP_STORAGE = Manifest.permission_group.STORAGE;
    public static final String PERMISSION_GROUP_PHONE = Manifest.permission_group.PHONE;
    public static final String PERMISSION_GROUP_SMS = Manifest.permission_group.SMS;
    public static final String PERMISSION_GROUP_SENSORS = Manifest.permission_group.SENSORS;

    @SuppressLint("InlinedApi")
    public static final String PERMISSION_GROUP_MICROPHONE = Manifest.permission_group.MICROPHONE;
    public static final String PERMISSION_GROUP_LOCATION = Manifest.permission_group.LOCATION;

    public static final String PERMISSION_GROUP_CONTACTS = Manifest.permission_group.CONTACTS;

    public static final String PERMISSION_GROUP_CAMERA = Manifest.permission_group.CAMERA;

    public static final String PERMISSION_GROUP_CALENDAR = Manifest.permission_group.CALENDAR;

    public static Map<String, String> hintMap = new HashMap<String, String>();

    public static boolean checkSelfPermission(Activity activity,
                                              String permission) {
        return (ContextCompat.checkSelfPermission( activity, permission ) == PackageManager.PERMISSION_DENIED);
    }

    private static final String[] requestPermissions = {
            PERMISSION_READ_PHONE_STATE, PERMISSION_READ_EXTERNAL_STORAGE,
            PERMISSION_WRITE_EXTERNAL_STORAGE,
            PERMISSION_CAMERA, PERMISSION_INTERNET,
            PERMISSION_WIFI, PERMISSION_BLUETOOTH,
            PERMISSION_RECEIVE_BOOT_COMPLETED, PERMISSION_RESTART_PACKAGES,
            PERMISSION_ACCESS_NETWORK_STATE, PERMISSION_INSTALL_SHORTCUT,
            PERMISSION_UNMOUNT_FILESYSTEMS};


    public static void requestPermission(final Activity activity,
                                         String permission, final int requestCode) {
        if (activity == null) {
            return;
        }
        String[] permissions = new String[1];
        permissions[0] = permission;
        if (!EasyPermissions.hasPermissions( activity, permissions )) {
            EasyPermissions.requestPermissions( activity, requestCode, permissions );
        }
    }

    public static void requestPermissions(final Activity activity, String[] permissions, final int requestCode) {
        if (!EasyPermissions.hasPermissions( activity, permissions )) {
            EasyPermissions.requestPermissions( activity, requestCode, permissions );
        }
    }


    public static void requestMultiPermissions(final Activity activity, int requestCode) {
        if (!EasyPermissions.hasPermissions( activity, requestPermissions )) {
            EasyPermissions.requestPermissions( activity, requestCode, requestPermissions );
        }
    }

    private static void openSettingActivity(final Activity activity, String message) {
        Intent intent = new Intent();
        intent.setAction( Settings.ACTION_APPLICATION_DETAILS_SETTINGS );
        Log.d( TAG, "getPackageName(): " + activity.getPackageName() );
        Uri uri = Uri.fromParts( "package", activity.getPackageName(), null );
        intent.setData( uri );
        activity.startActivity( intent );
    }

}
