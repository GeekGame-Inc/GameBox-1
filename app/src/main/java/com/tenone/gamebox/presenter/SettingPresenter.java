

package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.SettingBiz;
import com.tenone.gamebox.mode.listener.ClearFilesListener;
import com.tenone.gamebox.mode.listener.DeleteDialogConfrimListener;
import com.tenone.gamebox.mode.listener.FileSizeCalculateListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.UpdateListener;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.SettingView;
import com.tenone.gamebox.view.activity.BindMobileActivity;
import com.tenone.gamebox.view.activity.LoginActivity;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.dialog.UpdateDetectionDialog.DetectionBuilder;
import com.tenone.gamebox.view.custom.dialog.UpdateDialog.UpdateBuidler;
import com.tenone.gamebox.view.service.UpdateService;
import com.tenone.gamebox.view.utils.ApkUtils;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.CharSequenceUtils;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.SpUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import okhttp3.FormBody;
import okhttp3.RequestBody;


public class SettingPresenter extends BasePresenter implements OnClickListener,
		OnCheckedChangeListener, FileSizeCalculateListener, ClearFilesListener,
		DeleteDialogConfrimListener, HttpResultListener, UpdateListener {
	SettingBiz settingBiz;
	SettingView settingView;
	Context mContext;
	DetectionBuilder detectionBuilder;
	UpdateBuidler updateBuilder;
	AlertDialog alertDialog;

	public SettingPresenter(SettingView v, Context cxt) {
		this.mContext = cxt;
		this.settingBiz = new SettingBiz();
		this.settingView = v;
	}

	@SuppressLint("NewApi")
	public void initView() {
		getTitleBarView().setLeftImg( R.drawable.icon_xqf_b );
		getTitleBarView().setTitleText( R.string.setting );

		getWifiSwitch().setChecked( isWifi() );
		getAutoInstallSwitch().setChecked( isAutoInstall() );
		getNotificationSwitch().setChecked( isNotification() );
		isShowVoice( isNotification() );
		getVoiceSwitch().setChecked( isVoice() );
		getShakeSwitch().setChecked( isShake() );
		getVersionsView().setText( versions() );
		isShowClear( isAutoClear() );
		cacheSize();
		downloadSize();
		getButton().setSelected( BeanUtils.isLogin() );
		getButton().setText( BeanUtils.isLogin() ? R.string.exitLogin : R.string.login );
		mobileTv().setText(
				BeanUtils.isLogin() ? (isBindMobile() ? CharSequenceUtils
						.getVisibilyPhone( SpUtil.getPhone() ) : "������") : "ȥ��¼" );
	}

	public void initListener() {
		getTitleBarView().getLeftImg().setOnClickListener( this );
		getClearDownloadView().setOnClickListener( this );
		getClearCacheView().setOnClickListener( this );
		getAutoInstallSwitch().setOnCheckedChangeListener( this );
		getWifiSwitch().setOnCheckedChangeListener( this );
		getNotificationSwitch().setOnCheckedChangeListener( this );
		getVoiceSwitch().setOnCheckedChangeListener( this );
		getShakeSwitch().setOnCheckedChangeListener( this );
		getAutoClearSwitch().setOnCheckedChangeListener( this );
		getDetectionView().setOnClickListener( this );
		getButton().setOnClickListener( this );
		ListenerManager.registerUpdateListener( this );
		bindMobileLayout().setOnClickListener( this );
	}

	private TitleBarView getTitleBarView() {
		return settingView.getTitleBarView();
	}

	private Switch getWifiSwitch() {
		return settingView.getWifiSwitch();
	}

	private Switch getAutoInstallSwitch() {
		return settingView.getAutoInstallSwitch();
	}

	private Switch getNotificationSwitch() {
		return settingView.getNotificationSwitch();
	}

	private Switch getVoiceSwitch() {
		return settingView.getVoiceSwitch();
	}

	private Switch getShakeSwitch() {
		return settingView.getShakeSwitch();
	}

	private Switch getAutoClearSwitch() {
		return settingView.getAutoClearSwitch();
	}

	private RelativeLayout getClearDownloadView() {
		return settingView.getClearDownloadView();
	}

	private RelativeLayout getClearCacheView() {
		return settingView.getClearCacheView();
	}

	private RelativeLayout getDetectionView() {
		return settingView.getDetectionView();
	}

	private TextView getDownloadSizeView() {
		return settingView.getDownloadSizeView();
	}

	private TextView getCacheView() {
		return settingView.getCacheSizeView();
	}

	private TextView getVersionsView() {
		return settingView.getVersionsView();
	}

	private Button getButton() {
		return settingView.getButton();
	}

	private RelativeLayout getVoiceLayout() {
		return settingView.getVoiceLayout();
	}

	private RelativeLayout getShakeLayout() {
		return settingView.getShakeLayout();
	}

	private TextView mobileTv() {
		return settingView.mobileTv();
	}

	private RelativeLayout bindMobileLayout() {
		return settingView.bindMobileLayout();
	}


	private boolean isWifi() {
		return settingBiz.isWifi();
	}


	private boolean isAutoInstall() {
		return settingBiz.isAutoInstall();
	}


	private boolean isAutoClear() {
		return settingBiz.isAutoClear();
	}


	private boolean isNotification() {
		return settingBiz.isNotification();
	}


	private boolean isVoice() {
		return settingBiz.isVoice();
	}


	private boolean isShake() {
		return settingBiz.isShake();
	}


	private void downloadSize() {
		settingBiz.downloadSize( Configuration.getDownloadpath(), this );
	}


	private void cacheSize() {
		settingBiz.cacheSize( Configuration.getCachepath(), this );
	}


	private String versions() {
		return settingBiz.versions( mContext );
	}

	private void setStatus(boolean b, String key) {
		settingBiz.setStatus( b, key );
	}


	private void clearDown() {
		settingBiz.clearDownload( Configuration.getDownloadpath(), this );
	}


	private void clearCache() {
		settingBiz.clearCache( Configuration.getCachepath(), this );
	}

	private boolean isBindMobile() {
		return settingBiz.isBindMobile();
	}


	private void showDetectionDialog() {
		if (detectionBuilder == null) {
			detectionBuilder = new DetectionBuilder( mContext );
		}
		detectionBuilder.setMessage( R.string.checkForUpdatesIng );
		detectionBuilder.showDialog();
	}

	private void requestHttp() {
		String url = MyApplication.getHttpUrl().getCheckApp().replace( " ", "" );
		RequestBody requestBody = new FormBody.Builder().add( "system", "1" )
				.add( "channel", MyApplication.getConfigModle().getChannelID() )
				.add( "version", getVersion() ).build();
		HttpUtils.postHttp( mContext, 2, url, requestBody, this );
	}


	public String getVersion() {
		try {
			PackageManager manager = mContext.getPackageManager();
			PackageInfo info = manager.getPackageInfo(
					mContext.getPackageName(), 0 );
			String version = info.versionCode + "";
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}


	private void detectionSucceed() {
		if (detectionBuilder == null) {
			detectionBuilder = new DetectionBuilder( mContext );
		}
		detectionBuilder.setMessage( R.string.newVersion );
		detectionBuilder.showDialog();
	}

	private void showDialog() {
		if (updateBuilder == null) {
			updateBuilder = new UpdateBuidler( mContext );
		}
		updateBuilder.setMessage( "\u68c0\u6d4b\u5230\u6709\u65b0\u7248\u672c\uff0c\u662f\u5426\u7acb\u5373\u66f4\u65b0?" );
		updateBuilder.setConfirmText( "\u7acb\u5373\u66f4\u65b0" );
		updateBuilder.setCancleText( "\u6682\u4e0d\u66f4\u65b0" );
		updateBuilder.setConfrimListener( this );
		updateBuilder.show();
	}


	public void exitLogin() {
		settingBiz.exitLogin();
		ListenerManager.sendOnLoginStateChange( false );
		getButton().setSelected( false );
		getButton().setText( R.string.login );
		mobileTv().setText( "\u53bb\u767b\u5f55" );
		SpUtil.setExit( true );
	}

	public void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg0 == 25 && arg1 == Activity.RESULT_OK) {
			getButton().setSelected( BeanUtils.isLogin() );
			getButton().setText( R.string.exitLogin );
		}
		mobileTv().setText(
				BeanUtils.isLogin() ? (isBindMobile() ? CharSequenceUtils
						.getVisibilyPhone( SpUtil.getPhone() ) : "\u7acb\u5373\u7ed1\u5b9a") : "\u53bb\u767b\u5f55" );
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.id_titleBar_leftImg:
				close( mContext );
				break;
			case R.id.id_setting_clearDownload:

				clearDown();
				break;
			case R.id.id_setting_detection:

				showDetectionDialog();
				requestHttp();
				alertDialog = buildProgressDialog( mContext );
				break;
			case R.id.id_setting_clearCache:

				clearCache();
				break;
			case R.id.id_setting_button:

				if (BeanUtils.isLogin()) {
					exitLogin();
				} else {
					openOtherActivityForResult( mContext, 25, new Intent( mContext,
							LoginActivity.class ) );
				}
				break;
			case R.id.id_setting_bindMobile:
				if (!BeanUtils.isLogin()) {
					openOtherActivityForResult( mContext, 25, new Intent( mContext,
							LoginActivity.class ) );
					return;
				}
				Intent intent = new Intent();
				intent.setClass( mContext, BindMobileActivity.class );
				intent.setAction( isBindMobile() ? "unbind" : "bind" );
				openOtherActivityForResult( mContext, 80, intent );
				break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		String key = null;
		switch (buttonView.getId()) {
			case R.id.id_setting_isAutoInstall:
				key = SpUtil.isAutoInstallKey;
				break;
			case R.id.id_setting_isWifi:
				key = SpUtil.isWifiKey;
				break;
			case R.id.id_setting_isNotification:
				key = SpUtil.isNotificationKey;
				break;
			case R.id.id_setting_voice:
				key = SpUtil.isSoundKey;
				break;
			case R.id.id_setting_shake:
				key = SpUtil.isShakeKey;
				break;
			case R.id.id_setting_isAutoClear:
				key = SpUtil.isAutoClearKey;
				break;
		}
		if (key.equals( SpUtil.isNotificationKey )) {
			isShowVoice( isChecked );
		}
		if (key.equals( SpUtil.isAutoClearKey )) {
			isShowClear( isChecked );
		}
		statusMap.put( key, isChecked );
	}


	private void isShowVoice(boolean isChecked) {
		getVoiceLayout().setVisibility( isChecked ? View.VISIBLE : View.GONE );
		getShakeLayout().setVisibility( isChecked ? View.VISIBLE : View.GONE );
		getVoiceSwitch().setChecked( isChecked );
		getShakeSwitch().setChecked( isChecked );
	}


	private void isShowClear(boolean isChecked) {
		getClearDownloadView().setVisibility(
				isChecked ? View.GONE : View.VISIBLE );
		getAutoClearSwitch().setChecked( isChecked );
	}


	Map<String, Boolean> statusMap = new HashMap<String, Boolean>();


	public void saveStatus() {
		Iterator<Entry<String, Boolean>> it = statusMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Boolean> entry = it.next();
			setStatus( entry.getValue(), entry.getKey() );
		}
	}

	public void onDestroy() {
		ListenerManager.unRegisterUpdateListener( this );
	}

	@Override
	public void calculateResult(String path, String size) {
		if (path.equals( Configuration.getDownloadpath() )) {
			getDownloadSizeView().setText( size );
		}

		if (path.equals( Configuration.getCachepath() )) {
			getCacheView().setText( size );
		}
	}

	@Override
	public void clearResult(String path, boolean result) {
		String text = "";
		if (path.equals( Configuration.getCachepath() )) {
			cacheSize();
			text = "\u7f13\u5b58\u6e05\u9664\u6210\u529f";
		}
		if (path.equals( Configuration.getDownloadpath() )) {
			downloadSize();
			text = "\u4e0b\u8f7d\u76ee\u5f55\u6e05\u9664\u6210\u529f";
		}
		showToast( mContext, text );
	}

	private AlertDialog updateDialog;

	@Override
	public void onConfrimClick(AlertDialog dialog) {
		updateDialog = dialog;
		Intent service = new Intent( mContext, UpdateService.class );
		service.putExtra( "path", path );
		mContext.startService( service );
		updateBuilder.getMessageTv().setText( "\u6b63\u5728\u4e0b\u8f7d:" + 0 + "%" );
		updateBuilder.getProgressBar().setVisibility( View.VISIBLE );
		updateBuilder.getProgressBar().setProgress( 0 );
		updateBuilder.getConfirmBt().setVisibility( View.GONE );
		updateBuilder.getCancleBt().setVisibility( View.GONE );
	}

	private String path;

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		cancelProgressDialog( alertDialog );
		detectionBuilder.dismiss();
		if ("0".equals( resultItem.getString( "status" ) )) {
			path = resultItem.getString( "data" );
			if (TextUtils.isEmpty( path )) {
				detectionSucceed();
			} else {
				showDialog();
			}
		} else {
			showToast( mContext, resultItem.getString( "msg" ) );
		}
	}

	@Override
	public void onError(int what, String error) {
		cancelProgressDialog( alertDialog );
		detectionBuilder.dismiss();
		showToast( mContext, error );
	}

	@Override
	public void update(int progerss, String apkName) {
		if (progerss > 0 && progerss < 100) {
			if (updateBuilder != null) {
				updateBuilder.getMessageTv().setText( "\u6b63\u5728\u4e0b\u8f7d:" + progerss + "%" );
				updateBuilder.getProgressBar().setProgress( progerss );
			}
		} else if (progerss == 100) {
			updateBuilder.getMessageTv().setText( "\u4e0b\u8f7d\u5b8c\u6210,\u6b63\u5728\u5b89\u88c5" );
			updateBuilder.getProgressBar().setProgress( progerss );
			ApkUtils.installApp( apkName, mContext );
			if (updateDialog != null) {
				updateDialog.dismiss();
			}
		}
	}
}
