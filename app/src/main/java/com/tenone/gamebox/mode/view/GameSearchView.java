/** 
 * Project Name:GameBox 
 * File Name:GameSearchView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-28обнГ4:39:39 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import com.tenone.gamebox.view.custom.MyGridView;
import com.tenone.gamebox.view.custom.MyListView;
import com.tenone.gamebox.view.custom.SearchTitleBarView;

/**
 * ClassName:GameSearchView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-28 обнГ4:39:39 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface GameSearchView {

	SearchTitleBarView getSearchTitleBarView();

	MyGridView getTopGridView();

	MyGridView getBottomGridView();

	MyListView getListView();
}
