package com.tenone.gamebox.view.service;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.DeleteDialogConfrimListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.presenter.BasePresenter;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.receiver.NetworkChangeRexeiver;
import com.tenone.gamebox.view.utils.DialogUtils;
import com.tenone.gamebox.view.utils.FileUtils;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.database.GameDownloadTab;
import com.tenone.gamebox.view.utils.download.DownloadManager;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.DownloadStatusConfiguration;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnDeleteDownloadFileListener;
import org.wlf.filedownloader.listener.OnFileDownloadStatusListener;

import java.io.File;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class NoRequestHttpDownloadService extends Service implements
        NetworkChangeRexeiver.OnNetworkChangeListener, DeleteDialogConfrimListener {

    String TAG = "NoRequestHttpDownloadService";
    DownloadManager manager;

    Context context;

    NetworkChangeRexeiver changeRexeiver;

    boolean isWifi = true;

    GameModel data;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (changeRexeiver == null) {
            changeRexeiver = new NetworkChangeRexeiver();
        }
        registerReceiver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();
        if (manager == null) {
            manager = DownloadManager.getInstanse( getApplicationContext() );
        }
        if (intent != null && intent.hasExtra( "gameModel" )) {
            data = (GameModel) intent.getExtras().get( "gameModel" );
            if (data.getStatus() == GameStatus.LOADING) {
                if (!TextUtils.isEmpty( data.getUrl() )) {
                    startDispose( data );
                }
            } else {
                startDispose( data );
            }
        }
        return super.onStartCommand( intent, flags, startId );
    }

    private void startDispose(GameModel gameModel) {
        if (!TextUtils.isEmpty( gameModel.getUrl() )) {
            DownGameThread thread = manager.getNewThreads().get( gameModel.getUrl() );
            if (thread != null) {
                thread.setGameModel( gameModel );
                thread.run();
            } else {
                thread = new DownGameThread( gameModel );
                thread.setName( gameModel.getUrl() );
                manager.addNewThreads( thread );
                thread.start();
            }
        } else {
            Message message = new Message();
					message.obj = "\u4e0b\u8f7d\u5730\u5740\u4e3a\u7a7a";
            handler.sendMessage( message );
        }
    }

    public void registerReceiver() {
        changeRexeiver.setOnNetworkChangeListener( this );
        IntentFilter filter = new IntentFilter();
        filter.addAction( "android.net.conn.CONNECTIVITY_CHANGE" );
        filter.setPriority( 1000 );
        registerReceiver( changeRexeiver, filter );
    }

    public class DownGameThread extends Thread implements
            OnFileDownloadStatusListener, OnDeleteDownloadFileListener,
            HttpResultListener {
        GameModel gameModel;

        public DownGameThread(GameModel model) {
            this.gameModel = model;
        }

        public void setGameModel(GameModel model) {
            this.gameModel = model;
        }

        @Override
        public void run() {
            switch (gameModel.getStatus()) {
                case GameStatus.LOADING:
                    startDown();
                    break;
                case GameStatus.PAUSEING:
                    pauseDown();
                    break;
                case GameStatus.UNLOAD:
                    deleteDown();
                    break;
                case GameStatus.INSTALLCOMPLETED:
                    sendBroadcast( Configuration.completedFilter, gameModel );
                    break;
                case GameStatus.UPDATE:
                    startDown();
                    break;
            }
            sendDownloadActionBroadcast( context );
            super.run();
        }

        public void sendDownloadActionBroadcast(Context mContext) {
            Intent intent = new Intent();
            intent.setAction( "download_action" );
            if (Build.VERSION.SDK_INT >= 24) {
                mContext.sendBroadcast( intent,
                        "com.tenone.gamebox.broadcast.permission" );
            } else {
                mContext.sendBroadcast( intent );
            }
        }

        private void startDown() {
            manager.addDownload( gameModel );
            DownloadStatusConfiguration.Builder builder = new DownloadStatusConfiguration.Builder();
            builder.addListenUrl( gameModel.getUrl() );
            FileDownloader
                    .registerDownloadStatusListener( this, builder.build() );
            FileDownloader.start( gameModel.getUrl() );
        }

        private void pauseDown() {
            DownloadStatusConfiguration.Builder builder = new DownloadStatusConfiguration.Builder();
            builder.addListenUrl( gameModel.getUrl() );
            FileDownloader
                    .registerDownloadStatusListener( this, builder.build() );
            FileDownloader.pause( gameModel.getUrl() );
        }

        private void deleteDown() {
            FileDownloader.delete( gameModel.getUrl(), true, this );
        }

        @Override
        public void onFileDownloadStatusCompleted(DownloadFileInfo arg0) {
            String filePath = Configuration.downloadpath + "/" + gameModel.getApkName();
            try {
                long apkSize = FileUtils.getFileSize( new File( filePath ) );
                long fileSize = arg0.getFileSizeLong();
                if (apkSize != fileSize) {
                    deleteDown();
                    gameModel.setStatus( GameStatus.UNLOAD );
                    manager.updateDownload( gameModel, GameDownloadTab.DOWNSTATUS,
                            gameModel.getStatus() + "" );
                    DialogUtils.showConfirmDialog( context, dialog -> {
                                startDown();
                                dialog.dismiss();
                            }, getString( R.string.download_error_message ),
                            getString( R.string.cancle_download ), getString( R.string.try_download ) );
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                deleteDown();
                gameModel.setStatus( GameStatus.UNLOAD );
                manager.updateDownload( gameModel, GameDownloadTab.DOWNSTATUS,
                        gameModel.getStatus() + "" );
                DialogUtils.showConfirmDialog( context, new DeleteDialogConfrimListener() {
                            @Override
                            public void onConfrimClick(AlertDialog dialog) {
                                startDown();
                                dialog.dismiss();
                            }
                        }, getString( R.string.download_error_message ),
                        getString( R.string.cancle_download ), getString( R.string.try_download ) );
                return;
            }
            File file = new File( filePath );
            file.renameTo( new File( "Cts" + filePath ) );
            gameModel.setStatus( GameStatus.COMPLETED );
            manager.updateDownload( gameModel, GameDownloadTab.DOWNSTATUS,
                    gameModel.getStatus() + "" );
            sendBroadcast( Configuration.completedFilter, gameModel );
            sendBroadcast( Configuration.installFilter, gameModel );
            requestTimes( gameModel.getGameId() + "", context );
            sendDownloadActionBroadcast( context );
            sendNotification( gameModel.getName(), gameModel.getGameId() + "",
                    arg0.getFileName() );
            Intent intent = new Intent();
            intent.putExtra( "model", gameModel );
        }

        @Override
        public void onFileDownloadStatusDownloading(DownloadFileInfo arg0,
                                                    float arg1, long arg2) {
            float fileSize = arg0.getFileSizeLong();
            float downSize = arg0.getDownloadedSizeLong();
            float progress = (downSize / fileSize) * 100;
            gameModel.setProgress( (int) progress );
            manager.updateDownloadProgress( gameModel );
            sendBroadcast( Configuration.loadFilter, gameModel );
        }

        @Override
        public void onFileDownloadStatusFailed(String arg0,
                                               DownloadFileInfo arg1, FileDownloadStatusFailReason failReason) {
            String reason = "";
            String failType = failReason.getType();
            if (FileDownloadStatusFailReason.TYPE_URL_ILLEGAL.equals( failType )) {
                reason = getApplicationContext().getResources().getString( R.string.download_pat_error );
            } else if (FileDownloadStatusFailReason.TYPE_STORAGE_SPACE_IS_FULL
                    .equals( failType )) {
                reason = getApplicationContext().getResources().getString( R.string.no_memory );
            } else if (FileDownloadStatusFailReason.TYPE_NETWORK_DENIED
                    .equals( failType )) {
                reason = getApplicationContext().getResources().getString( R.string.noNetwork );
            } else if (FileDownloadStatusFailReason.TYPE_NETWORK_TIMEOUT
                    .equals( failType )) {
                reason = getApplicationContext().getResources().getString( R.string.timeout );
            } else {
                reason = getApplicationContext().getResources().getString( R.string.download_error );
            }
            deleteDown();
            DownloadManager.getInstanse( getApplicationContext() )
                    .removeDownload( gameModel );
            showError( reason );
            sendBroadcast( Configuration.deleteFilter, gameModel );
            sendDownloadActionBroadcast( context );
        }

        public void requestTimes(String gameId, Context cxt) {
            String url = MyApplication.getHttpUrl().getDownloadingTimes();
            RequestBody requestBody = new FormBody.Builder().add( "gid", gameId )
                    .build();
            HttpUtils.postHttp( cxt, 8, url, requestBody, this );
        }

        public void showError(String reason) {
            Message message = new Message();
            message.obj = reason;
            handler.sendMessage( message );
            gameModel.setStatus( GameStatus.UNLOAD );
            gameModel.setProgress( 0 );
            manager.updateDownloadProgress( gameModel );
            sendBroadcast( Configuration.loadFilter, gameModel );
        }

        @Override
        public void onFileDownloadStatusPaused(DownloadFileInfo arg0) {
            gameModel.setStatus( GameStatus.PAUSEING );
            manager.updateDownload( gameModel, GameDownloadTab.DOWNSTATUS,
                    gameModel.getStatus() + "" );
            sendBroadcast( Configuration.pasueFilter, gameModel );
        }

        @Override
        public void onFileDownloadStatusPrepared(DownloadFileInfo arg0) {
            gameModel.setApkName( arg0.getFileName() );
            manager.updateDownload( gameModel, GameDownloadTab.APKNAME,
                    gameModel.getApkName() );
        }

        @Override
        public void onFileDownloadStatusPreparing(DownloadFileInfo arg0) {
        }

        @Override
        public void onFileDownloadStatusWaiting(DownloadFileInfo arg0) {
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
            gameModel.setStatus( GameStatus.UNLOAD );
            gameModel.setProgress( 0 );
            manager.removeDownload( gameModel );
            BasePresenter.sendDownloadActionBroadcast( getApplicationContext() );
            sendBroadcast( Configuration.deleteFilter, gameModel );
        }

        @Override
        public void onSuccess(int what, ResultItem resultItem) {
            Log.i( "requestTimes", resultItem.getString( "msg" ) );
        }

        @Override
        public void onError(int what, String error) {
            Log.i( "requestTimes", error );
        }
    }

    private void sendBroadcast(String action, GameModel gameModel) {
        Intent intent = new Intent();
        intent.setAction( action );
        intent.putExtra( "data", gameModel );
        if (Build.VERSION.SDK_INT >= 24) {
            context.sendBroadcast( intent,
                    "com.tenone.gamebox.broadcast.permission" );
        } else {
            context.sendBroadcast( intent );
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Toast.makeText( getApplicationContext(), msg.obj.toString(),
                    Toast.LENGTH_SHORT ).show();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver( changeRexeiver );
    }

    @Override
    public void networkChange(boolean isAvailable, boolean isWifi) {
        this.isWifi = isWifi;
        if (isAvailable && SpUtil.getWifi() && !isWifi) {
            if (manager != null) {
                for (Map.Entry<String, DownloadService.DownThread> entry : manager.getThreads()
                        .entrySet()) {
                    FileDownloader.pause( entry.getKey() );
                }
            }
        }
    }

    @Override
    public void onConfrimClick(AlertDialog dialog) {
        startDispose( data );
    }

    @SuppressWarnings("static-access")
    private void sendNotification(String gameName, String gameId, String apkName) {
        if (SpUtil.getNotification()) {
            Intent updateIntent = new Intent( Intent.ACTION_VIEW );
            String str = Configuration.getDownloadpath() + "/" + apkName;
            if (TextUtils.isEmpty( str )) {
                return;
            }
            File file = new File( str );
            if (Build.VERSION.SDK_INT >= 24) {
                Uri apkUri = FileProvider.getUriForFile( context,
                        "com.tenone.gamebox.fileprovider", file );
                updateIntent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION );
                updateIntent.setDataAndType( apkUri,
                        "application/vnd.android.package-archive" );
            } else {
                updateIntent.setDataAndType( Uri.fromFile( file ),
                        "application/vnd.android.package-archive" );
            }
            PendingIntent updatePendingIntent = PendingIntent.getActivity(
                    context, 0, updateIntent, Intent.FILL_IN_ACTION );
            NotificationManager manager = (NotificationManager) context
                    .getSystemService( Context.NOTIFICATION_SERVICE );
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    context );
            builder.setSound( null );
            if (SpUtil.getSound() && SpUtil.getShake()) {
                builder.setDefaults( Notification.DEFAULT_ALL );
            } else if (true == SpUtil.getSound() && false == SpUtil.getShake()) {
                builder.setVibrate( null );
                builder.setDefaults( Notification.DEFAULT_SOUND );
            } else if (false == SpUtil.getSound() && true == SpUtil.getShake()) {
                builder.setSound( null );
                builder.setDefaults( Notification.DEFAULT_VIBRATE );
            } else if (false == SpUtil.getSound() && false == SpUtil.getShake()) {
                builder.setSound( null );
                builder.setVibrate( null );
            }
            Notification notification = builder
                    .setContentTitle( gameName + getResources().getString( R.string.dowanloadend ) )
                    .setContentText( gameName + getResources().getString( R.string.click_instanll ) )
                    .setWhen( System.currentTimeMillis() )
                    .setSmallIcon( R.drawable.icon_logo )
                    .setContentIntent( updatePendingIntent ).build();
            notification.flags |= notification.FLAG_AUTO_CANCEL;
            if (!TextUtils.isEmpty( gameId )) {
                manager.notify( Integer.valueOf( gameId ).intValue(), notification );
            }
        }
    }

}
