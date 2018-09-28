/** 
 * Project Name:GameBox 
 * File Name:GameNewFragmentView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-9����3:46:45 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
*/  
  
package com.tenone.gamebox.mode.view;

import android.widget.ListView;

import com.tenone.gamebox.view.custom.RefreshLayout;

/** 
 * ClassName:GameNewFragmentView <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017-3-9 ����3:46:45 <br/> 
 * @author   John Lie 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */
public interface GameNewFragmentView {
	/* ˢ�¿ؼ� */
	RefreshLayout getRefreshLayout();

	/* listview */
	ListView getRecommendListView();
}
  