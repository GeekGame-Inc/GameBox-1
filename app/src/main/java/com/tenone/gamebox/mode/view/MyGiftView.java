/** 
 * Project Name:GameBox 
 * File Name:MyGiftView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-22обнГ1:32:19 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import com.tenone.gamebox.view.custom.RecommendListView;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * ClassName:MyGiftView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-22 обнГ1:32:19 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface MyGiftView {

	TitleBarView getTitleBarView();

	RefreshLayout getRefreshLayout();

	RecommendListView getListView();
}
