/** 
 * Project Name:GameBox 
 * File Name:StrategySearchResultView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-30ионГ10:38:34 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.content.Intent;
import android.widget.ListView;

import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.SearchTitleBarView;

/**
 * ClassName:StrategySearchResultView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-30 ионГ10:38:34 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface StrategySearchResultView {
	SearchTitleBarView getSearchTitleBarView();

	RefreshLayout getRefreshLayout();

	ListView getListView();

	Intent getIntent();
}
