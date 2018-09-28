/** 
 * Project Name:GameBox 
 * File Name:GameRecommendFragmentView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-8����5:30:48 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import com.tenone.gamebox.view.custom.RecommendListView;
import com.tenone.gamebox.view.custom.RefreshLayout;

/**
 * ClassName:GameRecommendFragmentView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-8 ����5:30:48 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface GameRecommendFragmentView {

	/* ��ȡˢ�¿ؼ� */
	RefreshLayout getRefreshLayout();

	/* ��ȡlistView */
	RecommendListView getRecommendListViewView();
	
	boolean ismVisible();
}
