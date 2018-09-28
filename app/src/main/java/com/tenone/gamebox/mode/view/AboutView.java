/** 
 * Project Name:GameBox 
 * File Name:AboutView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-24����5:28:31 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.widget.TextView;

import com.tenone.gamebox.view.custom.TitleBarView;

public interface AboutView {
	
	TitleBarView getTitleBarView();

	TextView getVersionTextView();

	TextView getWebTextView();

	TextView getWeboTextView();

	TextView getWeChatTextView();
}
