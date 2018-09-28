/** 
 * Project Name:GameBox 
 * File Name:BeAboutToOpenFragment.java 
 * Package Name:com.tenone.gamebox.view.fragment 
 * Date:2017-3-27上午10:58:21 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.view.BeAboutToOpenView;
import com.tenone.gamebox.presenter.BeAboutToOpenPresenter;
import com.tenone.gamebox.view.base.BaseFragment;
import com.tenone.gamebox.view.custom.RefreshLayout;

/**
 * 即将开服 ClassName:BeAboutToOpenFragment <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-27 上午10:58:21 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class BeAboutToOpenFragment extends BaseFragment implements
		BeAboutToOpenView {
	View view;
	@ViewInject(R.id.id_todayOpen_refresh)
	RefreshLayout refreshLayout;
	@ViewInject(R.id.id_todayOpen_listView)
	ListView listView;

	BeAboutToOpenPresenter presenter;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_today_open, container,
					false);
		}
		ViewUtils.inject(this, view);
		presenter = new BeAboutToOpenPresenter(this, getActivity());
		presenter.setAdapter();
		presenter.initListener();
		presenter.requestHttp(HttpType.REFRESH);
		return view;
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
	public void onDestroy() {
		presenter.onDestroy();
		super.onDestroy();
	}
}
