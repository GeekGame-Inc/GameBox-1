package com.tenone.gamebox.view.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.ApkInstallListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.view.custom.FloatingDragger;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.custom.bga.BGABadgeView;
import com.tenone.gamebox.view.receiver.DownActionReceiver;
import com.tenone.gamebox.view.receiver.DownReceiver;
import com.tenone.gamebox.view.service.DownloadService;
import com.tenone.gamebox.view.utils.PermissionUtils;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.SystemBarUtils;


@SuppressLint({"NewApi", "ResourceAsColor"})
public abstract class BaseActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
		}
		SystemBarUtils.compat( this, R.color.white );
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView( new FloatingDragger( this, layoutResID ).getView() );
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ImmersionBar.with( this ).destroy();
	}

	protected Global global = Global.getInstance();

	protected void startActivity(String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction( action );
		if (bundle != null) {
			intent.putExtras( bundle );
		}
		startActivity( intent );
	}

	protected void startActivity(Class<?> cls) {
		startActivity( cls, null );
	}

	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass( this, cls );
		if (bundle != null) {
			intent.putExtra( "bundle", bundle );
		}
		startActivity( intent );
	}

	protected void showShortToast(String text) {
		Toast.makeText( this, text, Toast.LENGTH_SHORT ).show();
	}

	protected void showShortToast(int resId) {
		Toast.makeText( this, resId, Toast.LENGTH_SHORT ).show();
	}

	@TargetApi(19)
	public void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		Log.i( "setTranslucentStatus", "flags is " + winParams.flags );
		win.setAttributes( winParams );
	}

	private AlertDialog progressDialog;

	protected void buildProgressDialog() {
		try {
			if (progressDialog == null) {
				progressDialog = new AlertDialog.Builder( this, R.style.loadingStyle ).show();
			}
			progressDialog.setContentView( R.layout.loading_progress );
			progressDialog.setCancelable( true );
			progressDialog.setCanceledOnTouchOutside( false );
			TextView msg = progressDialog
					.findViewById( R.id.id_tv_loadingmsg );
			msg.setText( getResources().getString( R.string.loading ) + "..." );
			progressDialog.show();
		} catch (Exception e) {
		}
	}

	protected void buildProgressDialog(Context context) {
		try {
			if (progressDialog == null) {
				progressDialog = new AlertDialog.Builder( context, R.style.loadingStyle ).show();
			}
			progressDialog.setContentView( R.layout.loading_progress );
			progressDialog.setCancelable( true );
			progressDialog.setCanceledOnTouchOutside( false );
			TextView msg = progressDialog
					.findViewById( R.id.id_tv_loadingmsg );
			msg.setText( getResources().getString( R.string.loading ) + "..." );
			progressDialog.show();
		} catch (Exception e) {
		}
	}

	protected void buildProgressDialog(String text) {
		try {
			if (progressDialog == null) {
				progressDialog = new AlertDialog.Builder( this, R.style.loadingStyle ).show();
			}
			progressDialog.setContentView( R.layout.loading_progress );
			progressDialog.setCancelable( true );
			progressDialog.setCanceledOnTouchOutside( false );
			TextView msg = progressDialog
					.findViewById( R.id.id_tv_loadingmsg );
			msg.setText( text + "..." );
			progressDialog.show();
		} catch (Exception e) {
		}
	}

	protected void cancelProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	protected boolean is185() {
		return "185".equals( MyApplication.getConfigModle().getChannelID() );
	}

	protected boolean isLogin() {
		return !"0".equals( SpUtil.getUserId() ) && !TextUtils.isEmpty( SpUtil.getUserId() );
	}

	protected void showToast(String text) {
		ToastCustom.makeText( this, text, ToastCustom.LENGTH_SHORT ).show();
	}

	protected boolean retuestStoragePermission(Activity activity) {
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

	protected void openDownService(Context mContext, GameModel gameModel) {
		Intent intent = new Intent( mContext, DownloadService.class );
		intent.putExtra( "gameModel", gameModel );
		mContext.startService( intent );
	}

	protected void sendDownloadActionBroadcast(Context mContext) {
		Intent intent = new Intent();
		intent.setAction( "download_action" );
		if (Build.VERSION.SDK_INT >= 24) {
			mContext.sendBroadcast( intent,
					"com.tenone.gamebox.broadcast.permission" );
		} else {
			mContext.sendBroadcast( intent );
		}
	}

	protected void registerDownReceiver(Context mContext,
																			DownReceiver.DownStatusChangeListener listener, DownReceiver receiver) {
		IntentFilter filter = new IntentFilter();
		filter.addAction( com.tenone.gamebox.view.base.Configuration.loadFilter );
		filter.addAction( com.tenone.gamebox.view.base.Configuration.pasueFilter );
		filter.addAction( com.tenone.gamebox.view.base.Configuration.completedFilter );
		filter.addAction( com.tenone.gamebox.view.base.Configuration.deleteFilter );
		mContext.registerReceiver( receiver, filter );
		receiver.setChangeListener( listener );
	}

	protected void registerInstallReceiver(Context mContext,
																				 ApkInstallListener.InstallListener listener, ApkInstallListener installListener) {
		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_MEDIA_MOUNTED );
		intentFilter.addAction( Intent.ACTION_PACKAGE_ADDED );
		intentFilter.addAction( Intent.ACTION_PACKAGE_REMOVED );
		intentFilter.addAction( Intent.ACTION_PACKAGE_REPLACED );
		intentFilter.addDataScheme( "package" );
		mContext.registerReceiver( installListener, intentFilter );
		installListener.setInstallListener( listener );
	}

	protected void sendBroadcast(String action, GameModel gameModel, Context cxt) {
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

	public BGABadgeView showDownBadgeView(Context context, View view, String text) {
		BGABadgeView badgeView = showBGABadgeView( context,
				BGABadgeView.POSITION_CENTER, text, view );
		badgeView.setTextSize( TypedValue.COMPLEX_UNIT_SP, 12 );
		badgeView.show();
		return badgeView;
	}


	public BGABadgeView showDownBadgeView(Context context, View view, String text, int position) {
		BGABadgeView badgeView = showBGABadgeView( context,
				position, text, view );
		badgeView.setTextSize( TypedValue.COMPLEX_UNIT_SP, 10 );
		badgeView.show();
		return badgeView;
	}


	public BGABadgeView showBGABadgeView(Context context, int position, String text, View view) {
		BGABadgeView badgeView = new BGABadgeView( context, view );
		badgeView.setBadgePosition( position );
		badgeView.setText( text );
		return badgeView;
	}

	public void registerDownloadActionReceiver(Context mContext, DownActionReceiver receiver) {
		IntentFilter filter = new IntentFilter();
		filter.addAction( "download_action" );
		mContext.registerReceiver( receiver, filter );
	}
}
