
package com.tenone.gamebox.mode.listener;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.service.ScanService;
import com.tenone.gamebox.view.utils.DatabaseUtils;
import com.tenone.gamebox.view.utils.FileUtils;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TelephoneUtils;
import com.tenone.gamebox.view.utils.database.GameDownloadTab;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnDeleteDownloadFileListener;

import java.io.File;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class ApkInstallListener extends BroadcastReceiver implements
        OnDeleteDownloadFileListener, HttpResultListener {
    String TAG = "ApkInstallListener";
    DatabaseUtils databaseUtils;

    public static void sendDownloadActionBroadcast(Context mContext) {
        Intent intent = new Intent();
        intent.setAction( "download_action" );
        mContext.sendBroadcast( intent );
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (databaseUtils == null) {
            databaseUtils = DatabaseUtils.getInstanse( context );
        }

        if (intent.getAction().equals( "android.intent.action.PACKAGE_ADDED" )) {
            sendDownloadActionBroadcast( context );
            String packageName = intent.getDataString();
            String str = packageName.replace( "package:", "" );
            ContentValues values = new ContentValues();
            values.put( GameDownloadTab.DOWNSTATUS, GameStatus.INSTALLCOMPLETED );
            String selection = GameDownloadTab.PACKGENAME + " = ?";
            databaseUtils.updateDownload( values, selection,
                    new String[]{str} );
            String sql = "update " + GameDownloadTab.TABLE_NAME + " set "
                    + GameDownloadTab.DOWNSTATUS + " = " + GameStatus.INSTALLCOMPLETED + " where "
                    + GameDownloadTab.PACKGENAME + " = " + str;
            databaseUtils.updateDownload( sql );
            if (listener != null) {
                listener.installed( str, GameStatus.INSTALLCOMPLETED );
            }
            String where = GameDownloadTab.PACKGENAME + "=?";
            String[] values1 = {str};
            GameModel gameModel = DatabaseUtils.getInstanse( context )
                    .getGameModel( where, values1 );
            requestInstall( 1, context, str, gameModel.getVersionsName(),
                    gameModel.getGameId() + "" );
            if (SpUtil.getAutoClear()) {
                if (!TextUtils.isEmpty( gameModel.getUrl() )) {
                    FileDownloader.delete( gameModel.getUrl(), true, this );
                }
            }
        }
        if (intent.getAction().equals( "android.intent.action.PACKAGE_REMOVED" )) {
            String packageName = intent.getDataString();
            String str = packageName.replace( "package:", "" );
            ContentValues values = new ContentValues();
            values.put( GameDownloadTab.DOWNSTATUS, GameStatus.UNINSTALLING );
            String selection = GameDownloadTab.PACKGENAME + " = ?";
            databaseUtils.updateDownload( values, selection,
                    new String[]{str} );
            String where = GameDownloadTab.PACKGENAME + "=?";
            String[] values1 = {str};
            GameModel gameModel = DatabaseUtils.getInstanse( context )
                    .getGameModel( where, values1 );
            requestUnInstall( 2, context, str );
            if (SpUtil.getAutoClear()) {
                if (!TextUtils.isEmpty( gameModel.getUrl() )) {
                    FileDownloader.delete( gameModel.getUrl(), false, this );
                }
            }
            if (listener != null) {
                listener.unInstall( str, GameStatus.UNLOAD );
            }
        }
        Intent scanIntent = new Intent( context, ScanService.class );
        context.startService( scanIntent );
    }

    class MyThread extends Thread {
        String apkName;
        String path;

        public MyThread(String name) {
            this.apkName = name;
            this.path = Configuration.getDownloadpath() + "/" + apkName;
        }

        @Override
        public void run() {
            File file = new File( path );
            FileUtils.delFile( file );
            super.run();
        }
    }

    InstallListener listener;

    public void setInstallListener(InstallListener l) {
        this.listener = l;
    }

    public interface InstallListener {

        void installed(String packgeName, int status);

        void unInstall(String packgeName, int status);
    }

    @Override
    public void onDeleteDownloadFileFailed(DownloadFileInfo arg0,
                                           DeleteDownloadFileFailReason arg1) {
    }

    @Override
    public void onDeleteDownloadFilePrepared(DownloadFileInfo arg0) {
    }

    @Override
    public void onDeleteDownloadFileSuccess(DownloadFileInfo arg0) {
    }

    private void requestInstall(int what, Context context, String pack, String version,
                                String gid) {
        String url = MyApplication.getHttpUrl().getGameInstall();
        RequestBody requestBody = new FormBody.Builder().add( "system", "1" )
                .add( "code", TelephoneUtils.getImei( context ) )
                .add( "channel", MyApplication.getConfigModle().getChannelID() )
                .add( "pack", pack ).add( "version", version ).add( "gid", gid )
                .add( "uid", SpUtil.getUserId() ).build();
        HttpUtils.postHttp( context, what, url, requestBody, this );
    }

    private void requestUnInstall(int what, Context context, String pack) {
        String url = MyApplication.getHttpUrl().getGameUninstall();
        RequestBody requestBody = new FormBody.Builder().add( "system", "1" )
                .add( "code", TelephoneUtils.getImei( context ) )
                .add( "channel", MyApplication.getConfigModle().getChannelID() )
                .add( "pack", pack ).build();
        HttpUtils.postHttp( context, what, url, requestBody, this );
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        if (what == 1) {
        } else {
        }
    }

    @Override
    public void onError(int what, String error) {
    }
}
