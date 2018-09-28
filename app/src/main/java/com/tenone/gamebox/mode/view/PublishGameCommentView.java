/** 
 * Project Name:GameBox 
 * File Name:PublishGameCommentView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-5-2����2:14:54 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.content.Intent;

import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * ClassName:PublishGameCommentView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-5-2 ����2:14:54 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface PublishGameCommentView {
	Intent getIntent();
	TitleBarView getTitleBarView();
	CustomizeEditText getEditText();

}
