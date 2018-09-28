package com.tenone.gamebox.view.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tenone.gamebox.mode.mode.GamePackMode;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.utils.ApkUtils;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScanService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        List<PackageInfo> packageInfos = ApkUtils.getAllApps( getApplicationContext() );
        List<GamePackMode> allPackModes = DatabaseUtils.getInstanse( getApplicationContext() ).gamePackModes();
        List<GamePackMode> installedPackModes = new ArrayList<GamePackMode>();
        if (!BeanUtils.isEmpty( packageInfos ) && !BeanUtils.isEmpty( allPackModes )) {
            for (GamePackMode packMode : allPackModes) {
                String packName = packMode.getPackName();
                for (PackageInfo info : packageInfos) {
                    if (info.packageName.equals( packName )) {
                        installedPackModes.add( packMode );
                        break;
                    }
                }
            }
        }
        if (!BeanUtils.isEmpty( installedPackModes )) {
            Random random = new Random();
            int index = random.nextInt( installedPackModes.size() );
            GamePackMode packMode = installedPackModes.get( index );
            MyApplication.setTopGameId( packMode.getGameId());
            List<String> ids = new ArrayList<String>();
            for (GamePackMode mode : installedPackModes) {
                ids.add( mode.getGameId() + "" );
            }
            MyApplication.setInstalledGameIds( ids );
        }
        return super.onStartCommand( intent, flags, startId );
    }
}
