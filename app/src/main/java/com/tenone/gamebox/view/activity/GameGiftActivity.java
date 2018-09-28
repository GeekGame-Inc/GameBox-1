/** 
 * Project Name:GameBox 
 * File Name:GameGift.java 
 * Package Name:com.tenone.gamebox.view.activity 
 * Date:2017-3-27上午11:31:30 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.view.RecommentGameGiftView;
import com.tenone.gamebox.presenter.RecommentGameGiftPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * 推荐里的游戏礼包 ClassName:GameGift <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-27 上午11:31:30 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class GameGiftActivity extends BaseActivity implements
		RecommentGameGiftView {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;

	@ViewInject(R.id.id_gameGift_refresh)
	RefreshLayout refreshLayout;

	@ViewInject(R.id.id_gameGift_listView)
	ListView listView;

	@ViewInject(R.id.id_gameGift_search)
	LinearLayout searchLayout;

	RecommentGameGiftPresenter presenter;

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_game_gift);
		ViewUtils.inject(this);
		presenter = new RecommentGameGiftPresenter(this, this);
		presenter.initView();
		presenter.setAdapter();
		presenter.initListener();
		presenter.requestBanner(presenter.BANNER);
		presenter.requestHttp(HttpType.REFRESH);
	}

	@Override
	public TitleBarView getTitleBarView() {
		return titleBarView;
	}

	@Override
	public RefreshLayout getRefreshLayout() {
		return refreshLayout;
	}

	@Override
	public ListView getListView() {
		return listView;
	}

	@Override
	public LinearLayout getLinearLayout() {
		return searchLayout;
	}
}
