/** 
 * Project Name:GameBox 
 * File Name:LoginView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-17ÉÏÎç11:44:43 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.widget.Button;
import android.widget.TextView;

import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * µÇÂ¼ ClassName:LoginView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-17 ÉÏÎç11:44:43 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface LoginView {

	TitleBarView getTitleBarView();

	CustomizeEditText getAccountView();

	CustomizeEditText getPwdView();

	Button getLoginView();

	TextView getForgetView();
}
