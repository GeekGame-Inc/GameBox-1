/** 
 * Project Name:GameBox 
 * File Name:DetailsOpenFragmentView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-4-12����1:47:36 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.tenone.gamebox.mode.mode.GameModel;

/**
 * ClassName:DetailsOpenFragmentView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-12 ����1:47:36 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface DetailsOpenFragmentView {

	String getGameId();

	SwipeRefreshLayout getRefreshLayout();

    RecyclerView getListView();

	GameModel getGameModel();
}
