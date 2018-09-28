/** 
 * Project Name:GameBox 
 * File Name:GameClassifyTabView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-9下午6:27:30 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.content.Intent;
import android.widget.ListView;

import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * 分类列表 ClassName:GameClassifyTabView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-9 下午6:27:30 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface GameClassifyTabView {
	/* 获取标题 */
	TitleBarView getTitleBarView();

	/* 获取刷新控件 */
	RefreshLayout getRefreshLayout();

	/* 获取列表 */
	ListView getRecommendListView();

	/* 获取Intent */
	Intent getIntent();
}
