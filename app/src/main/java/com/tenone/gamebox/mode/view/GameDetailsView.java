/** 
 * Project Name:GameBox 
 * File Name:GameDetailsView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-30下午3:04:27 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tenone.gamebox.view.custom.CustomerUnderlinePageIndicator;
import com.tenone.gamebox.view.custom.DownProgress;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.TwoStateImageView;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;

/**
 * 游戏详情 ClassName:GameDetailsView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-30 下午3:04:27 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface GameDetailsView {

	TitleBarView getTitleBarView();

	ImageView getImageView();

	TextView getNameTv();

	RatingBar getRatingBar();

	TextView getSizeTv();

	LinearLayout getLabelLayout();

	TabPageIndicator getTabPageIndicator();

	CustomerUnderlinePageIndicator getPageIndicator();

	ViewPager getViewPager();

	Intent getIntent();

	TwoStateImageView getCollectBt();

	TwoStateImageView getShareBt();

	DownProgress getProgress();
	
	LinearLayout getQQLayout();
}
