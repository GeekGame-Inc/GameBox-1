/** 
 * Project Name:GameBox 
 * File Name:TailorImageView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-22ионГ10:26:21 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.widget.ImageView;

import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.tailor.ClipImageLayout;

/**
 * ClassName:TailorImageView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-22 ионГ10:26:21 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface TailorImageView {

	ClipImageLayout getClipImageLayout();

	TitleBarView getTitleBarView();
	ImageView getImageView();
}
