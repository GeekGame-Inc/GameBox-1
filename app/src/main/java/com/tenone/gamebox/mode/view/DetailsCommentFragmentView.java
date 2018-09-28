/** 
 * Project Name:GameBox 
 * File Name:DetailsCommentFragmentView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-4-12����11:29:08 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

/**
 * ClassName:DetailsCommentFragmentView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-12 ����11:29:08 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface DetailsCommentFragmentView {

    RecyclerView getListView();

	String getGameId();

    SwipeRefreshLayout getRefreshLayout();

    ImageView getImageView();
}
