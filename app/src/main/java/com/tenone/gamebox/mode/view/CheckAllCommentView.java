/** 
 * Project Name:GameBox 
 * File Name:CheckAllCommentView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-5-2обнГ3:25:03 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.content.Intent;
import android.widget.ListView;

import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * ClassName:CheckAllCommentView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-5-2 обнГ3:25:03 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface CheckAllCommentView {

	TitleBarView getTitleBarView();

	RefreshLayout getreRefreshLayout();

	ListView getListView();

	Intent getIntent();
}
