/** 
 * Project Name:GameBox 
 * File Name:BrowseImageView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-4-26ионГ10:48:23 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.content.Intent;
import android.support.v4.view.ViewPager;

/**
 * ClassName:BrowseImageView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-26 ионГ10:48:23 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface BrowseImageView {
	
	ViewPager getViewPager();

	Intent getIntent();
}
