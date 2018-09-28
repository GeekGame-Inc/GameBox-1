/** 
 * Project Name:GameBox 
 * File Name:CommentFragmentView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-22обнГ3:13:17 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.widget.ListView;

import com.tenone.gamebox.view.custom.RefreshLayout;

/**
 * ClassName:CommentFragmentView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-22 обнГ3:13:17 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface CommentFragmentView {

	RefreshLayout getRefreshLayout();

	ListView getListView();
}
