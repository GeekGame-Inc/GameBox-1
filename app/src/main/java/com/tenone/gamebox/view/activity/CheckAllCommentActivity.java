/** 
 * Project Name:GameBox 
 * File Name:CheckAllCommentActivity.java 
 * Package Name:com.tenone.gamebox.view.activity 
 * Date:2017-5-2����3:15:24 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.CheckAllCommentView;
import com.tenone.gamebox.presenter.CheckAllCommentPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;

@SuppressLint("ResourceAsColor") public class CheckAllCommentActivity extends BaseActivity implements
		CheckAllCommentView {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_allComment_refresh)
	RefreshLayout refreshLayout;
	@ViewInject(R.id.id_allComment_listView)
	ListView listView;

	CheckAllCommentPresenter presenter;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_check_all_comment);
		ViewUtils.inject(this);
		presenter = new CheckAllCommentPresenter(this, this);
		presenter.initView();
		presenter.initListener();
		presenter.setAdapter();
	}

	@Override
	public TitleBarView getTitleBarView() {
		return titleBarView;
	}

	@Override
	public RefreshLayout getreRefreshLayout() {
		return refreshLayout;
	}

	@Override
	public ListView getListView() {
		return listView;
	}
}
