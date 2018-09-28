/** 
 * Project Name:GameBox 
 * File Name:GiftSearchView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-28ионГ9:50:40 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.content.Intent;
import android.widget.ListView;

import com.tenone.gamebox.view.custom.SearchTitleBarView;

/**
 * ClassName:GiftSearchView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-28 ионГ9:50:40 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface GiftSearchView {

	SearchTitleBarView getSearchTitleBarView();

	ListView getListView();

	Intent getIntent();
}
