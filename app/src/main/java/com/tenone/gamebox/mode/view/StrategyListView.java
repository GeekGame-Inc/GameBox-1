/** 
 * Project Name:GameBox 
 * File Name:StrategyListView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-4-13обнГ6:14:55 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.content.Intent;
import android.widget.ListView;

import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * ClassName:StrategyListView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-13 обнГ6:14:55 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface StrategyListView {

	TitleBarView getTitleBarView();

	RefreshLayout getRefreshLayout();

	ListView getListView();

	Intent getIntent();
}
