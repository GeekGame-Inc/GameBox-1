/** 
 * Project Name:GameBox 
 * File Name:GiftSearchResultView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-28下午2:27:34 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.content.Intent;
import android.widget.ListView;

import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.SearchTitleBarView;

/**
 * 礼包搜索结果 ClassName:GiftSearchResultView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-28 下午2:27:34 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface GiftSearchResultView {
	
	SearchTitleBarView getSearchTitleBarView();

	RefreshLayout getRefreshLayout();

	ListView getListView();

	Intent getIntent();
}
