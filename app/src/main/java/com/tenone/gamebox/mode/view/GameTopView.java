/** 
 * Project Name:GameBox 
 * File Name:GameTopView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-10上午11:26:39 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * 游戏排行榜 ClassName:GameTopView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-10 上午11:26:39 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface GameTopView {
	/* 获取搜索框 */
	View getSerachBar();

	/* 获取标题 */
	TitleBarView getTitleBarView();

	/* 获取刷新控件 */
	RefreshLayout getRefreshLayout();

	/* 获取列表 */
	ListView getListView();

	/* 获取Intent */
	Intent getIntent();
	
	TextView getDownBadgeTv();

	TextView getMessageBadgeTv();
}
