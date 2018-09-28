/** 
 * Project Name:GameBox 
 * File Name:GameView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-3����2:35:51 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.view.custom.CustomerUnderlinePageIndicator;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;

import java.util.List;

/**
 * ClassName:GameView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-3 ����2:35:51 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface GameView {
	RelativeLayout getSearchView();

	<T> List<T> getViews(Context cxt);

	TabPageIndicator getTabPageIndicator();

	ViewPager getViewPager();

	CustomerUnderlinePageIndicator getCustomerUnderlinePageIndicator();

	TextView getDownBadgeTv();

	TextView getMessageBadgeTv();
}
