
package com.tenone.gamebox.mode.biz;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.utils.ApkUtils;

import java.util.List;

public class AutoInstallApkThread extends AsyncTask<Void, Void, Boolean> {
    Context mContext;
    String apkName;

    public void start() {
        this.execute();
    }

    public AutoInstallApkThread(Context ac, String apkName) {
        this.mContext = ac;
        this.apkName = apkName;
    }

    @Override
    protected synchronized Boolean doInBackground(Void... voids) {
        List<String> list = MyApplication.installingPacks;
        return !list.contains( apkName );
    }

    @Override
    protected void onPostExecute(Boolean isInstall) {
        super.onPostExecute( isInstall );
        Log.i( "AutoInstallApkThread", "auto install " + apkName );
        MyApplication.getInstance().setInstallingPacks( apkName );
        ApkUtils.installApp( apkName, mContext );
    }
}
