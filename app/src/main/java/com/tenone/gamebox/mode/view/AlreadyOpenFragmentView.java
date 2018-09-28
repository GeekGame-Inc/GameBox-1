/** 
 * Project Name:GameBox 
 * File Name:AlreadyOpenFragmentView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-29ионГ11:29:03 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.widget.ListView;

import com.tenone.gamebox.view.custom.RefreshLayout;

/**
 * ClassName:AlreadyOpenFragmentView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-29 ионГ11:29:03 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface AlreadyOpenFragmentView {
	RefreshLayout getRefreshLayout();

	ListView getListView();
}
