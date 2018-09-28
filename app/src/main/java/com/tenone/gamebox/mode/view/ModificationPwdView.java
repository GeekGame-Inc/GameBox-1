/** 
 * Project Name:GameBox 
 * File Name:ModificationPwdView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-24обнГ4:20:08 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.content.Intent;
import android.widget.Button;

import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * ClassName:ModificationPwdView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-24 обнГ4:20:08 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface ModificationPwdView {

	TitleBarView getTitleBarView();

	CustomizeEditText getOldPwdEditText();

	CustomizeEditText getNewPwdEditText();

	CustomizeEditText getConfirmPwdEditText();

	Button getButton();

	Intent getIntent();

}
