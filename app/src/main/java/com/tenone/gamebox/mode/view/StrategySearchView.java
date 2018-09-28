/** 
 * Project Name:GameBox 
 * File Name:StrategySearchView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-29ÏÂÎç6:29:45 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.content.Intent;
import android.widget.ListView;

import com.tenone.gamebox.view.custom.SearchTitleBarView;

/**
 * ¹¥ÂÔËÑË÷ ClassName:StrategySearchView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-29 ÏÂÎç6:29:45 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface StrategySearchView {

	SearchTitleBarView getSearchTitleBarView();

	ListView getListView();

	Intent getIntent();
}
