package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.view.MyGiftView;
import com.tenone.gamebox.presenter.MyGiftPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.RecommendListView;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;

public class MyGiftActivity extends BaseActivity implements MyGiftView {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_Mygift_refreshLayout)
	RefreshLayout refreshLayout;
	@ViewInject(R.id.id_Mygift_listView)
	RecommendListView listView;
	MyGiftPresenter giftPresenter;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_mygift);
		ViewUtils.inject(this);
		giftPresenter = new MyGiftPresenter(this, this);
		giftPresenter.initView();
		giftPresenter.setAdapter();
		giftPresenter.initListener();
		giftPresenter.requestList(HttpType.REFRESH);
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
	public RecommendListView getListView() {
		return listView;
	}

}
