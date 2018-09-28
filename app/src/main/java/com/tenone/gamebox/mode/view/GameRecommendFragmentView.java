/** 
 * Project Name:GameBox 
 * File Name:GameRecommendFragmentView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-8下午5:30:48 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import com.tenone.gamebox.view.custom.RecommendListView;
import com.tenone.gamebox.view.custom.RefreshLayout;

/**
 * ClassName:GameRecommendFragmentView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-8 下午5:30:48 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface GameRecommendFragmentView {

	/* 获取刷新控件 */
	RefreshLayout getRefreshLayout();

	/* 获取listView */
	RecommendListView getRecommendListViewView();
	
	boolean ismVisible();
}
