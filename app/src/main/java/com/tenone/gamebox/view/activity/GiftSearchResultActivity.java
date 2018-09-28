/** 
 * Project Name:GameBox 
 * File Name:GiftSearchResultActivity.java 
 * Package Name:com.tenone.gamebox.view.activity 
 * Date:2017-3-28����2:12:51 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.view.GiftSearchResultView;
import com.tenone.gamebox.presenter.GiftSearchResultPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.SearchTitleBarView;

public class GiftSearchResultActivity extends BaseActivity implements
		GiftSearchResultView {
	@ViewInject(R.id.id_giftSearchResult_refresh)
	RefreshLayout refreshLayout;
	@ViewInject(R.id.id_giftSearchResult_listView)
	ListView listView;
	@ViewInject(R.id.id_searchBar)
	SearchTitleBarView searchTitleBarView;

	GiftSearchResultPresenter giftSearchResultPresenter;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_gift_search_result);
		ViewUtils.inject(this);
		giftSearchResultPresenter = new GiftSearchResultPresenter(this, this);
		giftSearchResultPresenter.initView();
		giftSearchResultPresenter.setAdapter();
		giftSearchResultPresenter.initListener();
		giftSearchResultPresenter.requestList(HttpType.REFRESH);
	}

	@Override
	public SearchTitleBarView getSearchTitleBarView() {
		return searchTitleBarView;
	}

	@Override
	public RefreshLayout getRefreshLayout() {
		return refreshLayout;
	}

	@Override
	public ListView getListView() {
		return listView;
	}
}
