/** 
 * Project Name:GameBox 
 * File Name:PublishCommentView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-23обнГ1:37:01 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.content.Intent;
import android.widget.ImageView;

import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * ClassName:PublishCommentView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-23 обнГ1:37:01 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface PublishCommentView {
	
	Intent getIntent();

	TitleBarView getTitleBarView();

	CustomizeEditText getEditText();

	ImageView getAlbumImgView();

	ImageView getImgView();

	ImageView getCollectImgView();
}
