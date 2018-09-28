/** 
 * Project Name:GameBox 
 * File Name:GameHotFragmentView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-9下午2:25:32 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.widget.ListView;

import com.tenone.gamebox.view.custom.RefreshLayout;

/**
 * ClassName:GameHotFragmentView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-9 下午2:25:32 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface GameHotFragmentView {
	
	/* 刷新控件 */
	RefreshLayout getRefreshLayout();

	/* listview */
	ListView getRecommendListView();
}
