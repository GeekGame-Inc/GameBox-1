/** 
 * Project Name:GameBox 
 * File Name:SettingView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-14����5:01:10 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * ClassName:SettingView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-14 ����5:01:10 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface SettingView {

	TitleBarView getTitleBarView();

	Switch getWifiSwitch();

	Switch getAutoInstallSwitch();

	Switch getNotificationSwitch();

	Switch getVoiceSwitch();

	Switch getShakeSwitch();
	
	Switch getAutoClearSwitch();
	
	RelativeLayout getClearDownloadView();

	RelativeLayout getClearCacheView();

	RelativeLayout getDetectionView();

	TextView getDownloadSizeView();

	TextView getCacheSizeView();

	TextView getVersionsView();

	Button getButton();

	RelativeLayout getVoiceLayout();

	RelativeLayout getShakeLayout();
	
	TextView mobileTv();
	
	RelativeLayout bindMobileLayout();
}
