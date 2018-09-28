/** 
 * Project Name:GameBox 
 * File Name:StrategyView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-29обнГ3:04:14 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.widget.LinearLayout;
import android.widget.ListView;

import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * ClassName:StrategyView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-29 обнГ3:04:14 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface StrategyView {
	
	TitleBarView getTitleBarView();

	CustomizeEditText getCustomizeEditText();

	LinearLayout getLinearLayout();

	RefreshLayout getRefreshLayout();

	ListView getListView();
}
