/** 
 * Project Name:GameBox 
 * File Name:WebView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-4-13����4:43:26 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.content.Intent;

import com.tenone.gamebox.view.custom.ProgressWebView;
import com.tenone.gamebox.view.custom.TitleBarView;
public interface WebActivityView {
	TitleBarView getTitleBarView();

	ProgressWebView getWebView();

	Intent getIntent();
}
