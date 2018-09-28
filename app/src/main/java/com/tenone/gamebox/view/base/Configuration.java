/**
 * Project Name:GameBox
 * File Name:Configuration.java
 * Package Name:com.tenone.gamebox.view
 * Date:2017-3-24����10:11:17
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.base;

import android.content.Context;

import com.tenone.gamebox.view.utils.FileUtils;
import com.tenone.gamebox.view.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
    public static String downloadpath;
    public static String cachepath;
    /* ���س�ʱʱ�� */
    public static int downloadTimeOut;
    /* ��������ʱʱ�� */
    public static int requestTimeOut;

    /* ������Ϸ������ */
    public static List<String> allGameNames = new ArrayList<String>();

    public static String loadFilter = "gamebox_down_loading";

    public static String pasueFilter = "gamebox_down_pasue";

    public static String completedFilter = "gamebox_down_completed";

    public static String deleteFilter = "gamebox_down_delete";

    public static String installFilter = "install";
    // ����ҳͼƬ����
    public static String SPLASHIMGNAME = "splash";
    // ����ҳͼƬ��ַ
    public static String SPLASHIMGPATH = "";

    public static void initConfiguration(Context context) {
        SPLASHIMGPATH = FileUtils.getCacheDirectory( context, true )
                .getAbsolutePath();
        SPLASHIMGNAME = "185".equals( MyApplication.getConfigModle().getChannelID() ) ?
                "splash_185" : "splash_" + MyApplication.getConfigModle().getChannelID();
        cachepath = FileUtils.getCacheDirectory( context ).getAbsolutePath();
        downloadpath = FileUtils.getIndividualCacheDirectory( context,
                "185BoxDownload" ).getAbsolutePath();
        downloadTimeOut = 250000;
        requestTimeOut = 10000;
        new Thread() {
            @Override
            public void run() {
                allGameNames.addAll( SpUtil.getAllGameNames() );
                super.run();
            }
        }.start();
    }

    public static List<String> getAllGameNames() {
        return allGameNames;
    }

    public static void setAllGameNames(List<String> allGameNames) {
        Configuration.allGameNames = allGameNames;
    }

    public static String getDownloadpath() {
        return downloadpath;
    }

    public static void setDownloadpath(String downloadpath) {
        Configuration.downloadpath = downloadpath;
    }

    public static String getCachepath() {
        return cachepath;
    }

    public static void setCachepath(String cachepath) {
        Configuration.cachepath = cachepath;
    }

    public static int getDownloadTimeOut() {
        return downloadTimeOut;
    }

    public static void setDownloadTimeOut(int downloadTimeOut) {
        Configuration.downloadTimeOut = downloadTimeOut;
    }

    public static int getRequestTimeOut() {
        return requestTimeOut;
    }

    public static void setRequestTimeOut(int requestTimeOut) {
        Configuration.requestTimeOut = requestTimeOut;
    }

}
