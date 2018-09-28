/**
 * Project Name:GameBox
 * File Name:GameDetailsFragmentView.java
 * Package Name:com.tenone.gamebox.mode.view
 * Date:2017-4-10����5:52:01
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.mode.view;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.custom.MoreTextView;
import com.tenone.gamebox.view.custom.MyGridView;
import com.tenone.gamebox.view.custom.MyListView;

/**
 * ClassName:GameDetailsFragmentView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-10 ����5:52:01 <br/>
 *
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface GameDetailsFragmentView {

    RecyclerView getImgsView();

    MyGridView getGridView();

    MoreTextView getMoreTextView();

    ResultItem getResultItem();

    TextView getPublishBt();

    TextView getCheckTv();

    MyListView getCommentListView();

    String getGameId();

    MoreTextView getFeatureMoreTextView();

    MoreTextView getRebateMoreTextView();

    MoreTextView getVipMoreTextView();

    RelativeLayout getRebateLayout();

    LinearLayout getVipLayout();

    LinearLayout getGifLayout();

    ImageView getGifImageView();

    ImageView getGifPalyImageView();

    NestedScrollView getNestedScrollView();

}
