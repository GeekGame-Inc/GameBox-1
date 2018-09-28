/** 
 * Project Name:GameBox 
 * File Name:AttentionView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-24обнГ2:54:34 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.widget.ListView;

import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * ClassName:AttentionView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-24 обнГ2:54:34 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface AttentionView {

	TitleBarView getTitleBarView();

	RefreshLayout getRefreshLayout();

	ListView getListView();
}
