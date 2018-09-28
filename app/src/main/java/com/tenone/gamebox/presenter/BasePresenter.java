

package com.tenone.gamebox.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.ApkInstallListener;
import com.tenone.gamebox.mode.listener.ApkInstallListener.InstallListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnDataChangeListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.BadgeView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.custom.bga.BGABadgeView;
import com.tenone.gamebox.view.receiver.DeleteDownloadFileReceiver;
import com.tenone.gamebox.view.receiver.DeleteDownloadFileReceiver.DeleteDownloadFileListener;
import com.tenone.gamebox.view.receiver.DownActionReceiver;
import com.tenone.gamebox.view.receiver.DownReceiver;
import com.tenone.gamebox.view.receiver.DownReceiver.DownStatusChangeListener;
import com.tenone.gamebox.view.receiver.WarnReceiver;
import com.tenone.gamebox.view.service.DownloadService;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.PermissionUtils;

import okhttp3.FormBody;
import okhttp3.RequestBody;


public class BasePresenter implements OnKeyListener {
    public static final int CHANNELDOWNLOAD = 25;

    public void sendBroadcast(String action, GameModel gameModel, Context cxt) {
        Intent intent = new Intent();
        intent.setAction( action );
        intent.putExtra( "data", gameModel );
        if (Build.VERSION.SDK_INT >= 24) {
            cxt.sendBroadcast( intent,
                    "com.tenone.gamebox.broadcast.permission" );
        } else {
            cxt.sendBroadcast( intent );
        }
    }

    public void registerWarnReceiver(Context mContext, WarnReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction( "open_notification" );
        mContext.registerReceiver( receiver, filter );
    }

    public void registerDownloadActionReceiver(Context mContext,
                                               DownActionReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction( "download_action" );
        mContext.registerReceiver( receiver, filter );
    }

    public static void sendDownloadActionBroadcast(Context mContext) {
        Intent intent = new Intent();
        intent.setAction( "download_action" );
        if (Build.VERSION.SDK_INT >= 24) {
            mContext.sendBroadcast( intent,
                    "com.tenone.gamebox.broadcast.permission" );
        } else {
            mContext.sendBroadcast( intent );
        }
    }

    public void registerDownReceiver(Context mContext,
                                     DownStatusChangeListener listener, DownReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction( Configuration.loadFilter );
        filter.addAction( Configuration.pasueFilter );
        filter.addAction( Configuration.completedFilter );
        filter.addAction( Configuration.deleteFilter );
        mContext.registerReceiver( receiver, filter );
        receiver.setChangeListener( listener );
    }

    public void registerInstallReceiver(Context mContext,
                                        InstallListener listener, ApkInstallListener installListener) {
        IntentFilter intentFilter = new IntentFilter(
                Intent.ACTION_MEDIA_MOUNTED );
        intentFilter.addAction( Intent.ACTION_PACKAGE_ADDED );
        intentFilter.addAction( Intent.ACTION_PACKAGE_REMOVED );
        intentFilter.addAction( Intent.ACTION_PACKAGE_REPLACED );
        intentFilter.addDataScheme( "package" );
        mContext.registerReceiver( installListener, intentFilter );
        installListener.setInstallListener( listener );
    }

    public void registerDeleteDownloadFileReceiver(Context context,
                                                   DeleteDownloadFileReceiver receiver,
                                                   DeleteDownloadFileListener listener) {
        IntentFilter intentFilter = new IntentFilter( "delete_download_file" );
        context.registerReceiver( receiver, intentFilter );
        receiver.setDeleteDownloadFileListener( listener );
    }

    public void unRegisterInstallListener(Context mContext,
                                          ApkInstallListener installListener) {
        if (installListener != null) {
            mContext.unregisterReceiver( installListener );
        }
    }

    public void unRegisterReceiver(Context mContext, DownReceiver receiver) {
        if (receiver != null) {
            mContext.unregisterReceiver( receiver );
        }
    }

    public void unRegisterDeleteDownloadFileReceiver(Context context,
                                                     DeleteDownloadFileReceiver receiver) {
        if (receiver != null) {
            context.unregisterReceiver( receiver );
        }
    }

    public void openDownService(Context mContext, GameModel gameModel) {
        Intent intent = new Intent( mContext, DownloadService.class );
        intent.putExtra( "gameModel", gameModel );
        mContext.startService( intent );
    }

    public void openOtherActivity(Context mContext, Intent intent) {
        mContext.startActivity( intent );
        ((Activity) mContext).overridePendingTransition( R.anim.fade_in,
                R.anim.fade_out );
    }

    public void openOtherActivityForResult(Context cxt, int requestCode,
                                           Intent intent) {
        ((Activity) cxt).startActivityForResult( intent, requestCode );
        ((Activity) cxt).overridePendingTransition( R.anim.fade_in,
                R.anim.fade_out );
    }

    public void close(Context mContext) {
        ((Activity) mContext).finish();
    }

    public void showToast(Context mContext, String text) {
        ToastCustom.makeText( mContext, text, ToastCustom.LENGTH_SHORT ).show();
    }

    public void showCustomToast(Context context, String text) {
        ToastCustom.makeText( context, text, ToastCustom.LENGTH_SHORT ).show();
    }

    public void showCustomToast(Context context, String text, int position) {
        ToastCustom.makeText( context, text, ToastCustom.LENGTH_SHORT ).show(
                position );
    }

    public void unDeleteBroadcastReceiver(Context context,
                                          DeleteBroadcastReceiver receiver) {
        context.unregisterReceiver( receiver );
    }

    public void registerDeleteBroadcastReceiver(
            DeleteBroadcastReceiver receiver, Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction( "delete_warn" );
        context.registerReceiver( receiver, intentFilter );
    }

    public class DeleteBroadcastReceiver extends BroadcastReceiver {
        OnDataChangeListener listener;

        public DeleteBroadcastReceiver(OnDataChangeListener l) {
            this.listener = l;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (listener != null) {
                listener.onDataChange();
            }
        }
    }

    public static boolean is185() {
        return "185".equals( MyApplication.getConfigModle().getChannelID() );
    }


    public boolean isLogin() {
        return BeanUtils.isLogin();
    }

    public static void requestChannelDownload(int what, GameModel model,
                                              Context cxt, HttpResultListener listener) {
        String url = MyApplication.getHttpUrl().getChannelDownload();
        RequestBody requestBody = new FormBody.Builder().add( "system", "1" )
                .add( "tag", model.getGameTag() )
                .add( "channel", MyApplication.getConfigModle().getChannelID() )
                .build();
        HttpUtils.postHttp( cxt, what, url, requestBody, listener );
    }

    public BadgeView showBadgeView(Context context, int position, String text,
                                   View view) {
        BadgeView badgeView = new BadgeView( context, view );
        badgeView.setBadgePosition( position );
        badgeView.setText( text );
        return badgeView;
    }

    public BGABadgeView showBGABadgeView(Context context, int position,
                                         String text, View view) {
        BGABadgeView badgeView = new BGABadgeView( context, view );
        badgeView.setBadgePosition( position );
        badgeView.setText( text );
        return badgeView;
    }

    public BGABadgeView showMessageBadgeView(Context context, View view,
                                             String text) {
        BGABadgeView badgeView1 = showBGABadgeView( context,
                BadgeView.POSITION_CENTER, text, view );
        if (TextUtils.isEmpty( text )) {
            badgeView1.setWidth( DisplayMetricsUtils.dipTopx( context, 10 ) );
            badgeView1.setHeight( DisplayMetricsUtils.dipTopx( context, 10 ) );
        } else {
            badgeView1.setTextSize( TypedValue.COMPLEX_UNIT_SP, 12 );
        }
        return badgeView1;
    }

    public BGABadgeView showDownBadgeView(Context context, View view,
                                          String text) {
        BGABadgeView badgeView = showBGABadgeView( context,
                BGABadgeView.POSITION_CENTER, text, view );
        badgeView.setTextSize( TypedValue.COMPLEX_UNIT_SP, 12 );
        badgeView.show();
        return badgeView;
    }

    public BGABadgeView showDownBadgeView(Context context, View view,
                                          String text, int position) {
        BGABadgeView badgeView = showBGABadgeView( context,
                position, text, view );
        badgeView.setTextSize( TypedValue.COMPLEX_UNIT_SP, 12 );
        badgeView.show();
        return badgeView;
    }

    public boolean retuestStoragePermission(Activity activity) {
        try {
            if (PermissionUtils.checkSelfPermission( activity,
                    PermissionUtils.PERMISSION_WRITE_EXTERNAL_STORAGE )) {
                PermissionUtils.requestPermission( activity,
                        PermissionUtils.PERMISSION_WRITE_EXTERNAL_STORAGE, 1 );
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private AlertDialog progressDialog;

    public AlertDialog buildProgressDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = new AlertDialog.Builder( context, R.style.loadingStyle ).show();
        }
        if (!((Activity) context).isFinishing()) {
            progressDialog.setContentView( R.layout.loading_progress );
            progressDialog.setCancelable( false );
            TextView msg = progressDialog
                    .findViewById( R.id.id_tv_loadingmsg );
            msg.setText( context.getResources().getString( R.string.loading )+"..." );
            progressDialog.show();
            progressDialog.setOnKeyListener( this );
        }
        return progressDialog;
    }

    public void cancelProgressDialog(AlertDialog progressDialog) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void cancelProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            cancelProgressDialog( progressDialog );
            return true;
        }
        return false;
    }
}
