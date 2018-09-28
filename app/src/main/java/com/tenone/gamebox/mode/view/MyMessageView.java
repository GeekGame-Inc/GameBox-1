/** 
 * Project Name:GameBox 
 * File Name:MyMessageView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-22����2:31:59 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.content.Intent;

import com.tenone.gamebox.view.custom.CustomerUnderlinePageIndicator;
import com.tenone.gamebox.view.custom.NoScrollViewPager;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;

/**
 * ClassName:MyMessageView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-22 ����2:31:59 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface MyMessageView {
	
	TitleBarView getTitleBarView();

	TabPageIndicator getTabPageIndicator();

	CustomerUnderlinePageIndicator getIndicator();

    NoScrollViewPager getViewPager();
	
	Intent getIntent();
}
