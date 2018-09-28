/** 
 * Project Name:GameBox 
 * File Name:SettingActivity.java 
 * Package Name:com.tenone.gamebox.view.activity 
 * Date:2017-3-14…œŒÁ11:40:55 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.SettingView;
import com.tenone.gamebox.presenter.SettingPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * …Ë÷√ ClassName:SettingActivity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-14 …œŒÁ11:40:55 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class SettingActivity extends BaseActivity implements SettingView {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_setting_button)
	Button button;
	@ViewInject(R.id.id_setting_cacheSize)
	TextView cacheSizeTv;
	@ViewInject(R.id.id_setting_clearCache)
	RelativeLayout clearCache;
	@ViewInject(R.id.id_setting_clearDownload)
	RelativeLayout clearDownload;
	@ViewInject(R.id.id_setting_currentVersions)
	TextView versionsTv;
	@ViewInject(R.id.id_setting_detection)
	RelativeLayout detection;
	@ViewInject(R.id.id_setting_isWifi)
	Switch isWifi;
	@ViewInject(R.id.id_setting_isAutoInstall)
	Switch isAutoInstall;
	@ViewInject(R.id.id_setting_isNotification)
	Switch notification;
	@ViewInject(R.id.id_setting_voice)
	Switch voice;
	@ViewInject(R.id.id_setting_shake)
	Switch shake;
	@ViewInject(R.id.id_setting_isAutoClear)
	Switch isAutoClear;

	@ViewInject(R.id.id_setting_downloadDirectorySize)
	TextView downloadDirectorySize;
	@ViewInject(R.id.id_setting_voiceLayout)
	RelativeLayout voiceLayout;
	@ViewInject(R.id.id_setting_shakeLayout)
	RelativeLayout shakeLayout;

	@ViewInject(R.id.id_setting_mobile)
	TextView mobileTv;
	@ViewInject(R.id.id_setting_bindMobile)
	RelativeLayout bindMobileLayout;

	SettingPresenter presenter;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_setting);
		ViewUtils.inject(this);
		presenter = new SettingPresenter(this, this);
		presenter.initView();
		presenter.initListener();
	}

	@Override
	public TitleBarView getTitleBarView() {
		return titleBarView;
	}

	@Override
	public Switch getWifiSwitch() {
		return isWifi;
	}

	@Override
	public Switch getAutoInstallSwitch() {
		return isAutoInstall;
	}

	@Override
	public Switch getNotificationSwitch() {
		return notification;
	}

	@Override
	public Switch getVoiceSwitch() {
		return voice;
	}

	@Override
	public Switch getShakeSwitch() {
		return shake;
	}

	@Override
	public RelativeLayout getClearDownloadView() {
		return clearDownload;
	}

	@Override
	public RelativeLayout getClearCacheView() {
		return clearCache;
	}

	@Override
	public RelativeLayout getDetectionView() {
		return detection;
	}

	@Override
	public TextView getDownloadSizeView() {
		return downloadDirectorySize;
	}

	@Override
	public TextView getCacheSizeView() {
		return cacheSizeTv;
	}

	@Override
	public TextView getVersionsView() {
		return versionsTv;
	}

	@Override
	public Button getButton() {
		return button;
	}

	@Override
	protected void onPause() {
		presenter.saveStatus();
		super.onPause();
	}

	@Override
	public RelativeLayout getVoiceLayout() {
		return voiceLayout;
	}

	@Override
	public RelativeLayout getShakeLayout() {
		return shakeLayout;
	}

	@Override
	public Switch getAutoClearSwitch() {
		return isAutoClear;
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		presenter.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	protected void onDestroy() {
		presenter.onDestroy();
		super.onDestroy();
	}

	@Override
	public TextView mobileTv() {
		return mobileTv;
	}

	@Override
	public RelativeLayout bindMobileLayout() {
		return bindMobileLayout;
	}
}
