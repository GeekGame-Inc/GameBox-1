/**
 * Project Name:GameBox
 * File Name:SystemBarUtils.java
 * Package Name:com.tenone.gamebox.view.utils
 * Date:2017-3-17����2:53:34
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.gyf.barlibrary.ImmersionBar;
import com.tenone.gamebox.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SystemBarUtils {
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier( "status_bar_height", "dimen", "android" );
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize( resourceId );
        }
        return result;
    }

    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName( "android.view.MiuiWindowManager$LayoutParams" );
                Field field = layoutParams.getField( "EXTRA_FLAG_STATUS_BAR_DARK_MODE" );
                darkModeFlag = field.getInt( layoutParams );
                Method extraFlagField = clazz.getMethod( "setExtraFlags", int.class, int.class );
                if (dark) {
                    extraFlagField.invoke( window, darkModeFlag, darkModeFlag );
                } else {
                    extraFlagField.invoke( window, 0, darkModeFlag );
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField( "MEIZU_FLAG_DARK_STATUS_BAR_ICON" );
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField( "meizuFlags" );
                darkFlag.setAccessible( true );
                meizuFlags.setAccessible( true );
                int bit = darkFlag.getInt( null );
                int value = meizuFlags.getInt( lp );
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt( lp, value );
                window.setAttributes( lp );
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    public static void compat(Activity activity, int statusBarColor) {
        ImmersionBar immersionBar = ImmersionBar.with( activity );
        if (!ImmersionBar.isSupportStatusBarDarkFont()) {
            immersionBar.statusBarColor( statusBarColor == R.color.white ? R.color.line0 : statusBarColor )
                    .statusBarDarkFont( false );
        } else {
            immersionBar.statusBarColor( statusBarColor )
                    .statusBarDarkFont( statusBarColor == R.color.white );
        }
        immersionBar
                .fitsSystemWindows( true )
                .keyboardEnable( true )
                .init();
    }

    public static void compat(Activity activity, int statusBarColor, boolean isDark) {
        ImmersionBar.with( activity )
                .statusBarColor( statusBarColor )
                .statusBarDarkFont( isDark )
                .fitsSystemWindows( true )
                .keyboardEnable( true )
                .init();
    }
}
