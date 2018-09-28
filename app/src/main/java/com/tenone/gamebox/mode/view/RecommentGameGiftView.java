/** 
 * Project Name:GameBox 
 * File Name:RecommentGameGiftView.java 
 * Package Name:com.tenone.gamebox.mode.view 
 * Date:2017-3-27下午1:28:19 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.view;

import android.widget.LinearLayout;
import android.widget.ListView;

import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * 推荐里游戏礼包 ClassName:RecommentGameGiftView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-27 下午1:28:19 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface RecommentGameGiftView {

	TitleBarView getTitleBarView();

	RefreshLayout getRefreshLayout();

	ListView getListView();

	LinearLayout getLinearLayout();
}
