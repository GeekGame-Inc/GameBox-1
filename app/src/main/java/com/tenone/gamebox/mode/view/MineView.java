/** 
 * Project Name:GameBox 
 * File Name:MineView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-14����10:11:56 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.view.custom.CustomerScrollView;

/**
 * import android.widget.GridView;
 * 
 * 
 * ClassName:MineView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-14 ����10:11:56 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface MineView {

	ListView getGridView();

	ImageView getHeaderView();
	
	ImageView getHeaderView2();
	
	RelativeLayout getHeaderLayout();

	TextView getNickView();

	TextView shareEarningsTv();

	TextView goldTv();

	TextView platformTv();
	
	ImageView settingImg();
	
	ImageView settingImg2();
	
	TextView vipImg();
	TextView vipTv();
	CustomerScrollView customerScrollView();
}
