/** 
 * Project Name:GameBox 
 * File Name:ManagementView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-17下午4:06:18 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import com.tenone.gamebox.view.custom.CustomerUnderlinePageIndicator;
import com.tenone.gamebox.view.custom.NoScrollViewPager;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;

/**
 * 应用管理 ClassName:ManagementView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-17 下午4:06:18 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface ManagementView {

	TitleBarView getTitleBarView();

	TabPageIndicator getTabPageIndicator();

	CustomerUnderlinePageIndicator getCustomerUnderlinePageIndicator();

	NoScrollViewPager getViewPager();
}
