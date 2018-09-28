/** 
 * Project Name:GameBox 
 * File Name:GameGiftView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-10下午4:20:54 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
*/  
  
package com.tenone.gamebox.mode.view;  

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tenone.gamebox.view.custom.RefreshLayout;

/** 
 * ClassName:GameGiftView <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017-3-10 下午4:20:54 <br/> 
 * @author   John Lie 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */
public interface GameGiftView {
	/* 获取搜索框 */
	View getSerachBar();

	/* 获取刷新控件 */
	RefreshLayout getRefreshLayout();

	/* 获取列表 */
	ListView getRecommendListView();

	/* 获取Intent */
	Intent getIntent();
	
	
	TextView getDownBadgeTv();

	TextView getMessageBadgeTv();
}
  