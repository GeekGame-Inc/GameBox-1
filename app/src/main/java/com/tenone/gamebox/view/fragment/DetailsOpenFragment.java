/** 
 * Project Name:GameBox 
 * File Name:DetailsOpenFragment.java 
 * Package Name:com.tenone.gamebox.view.fragment 
 * Date:2017-4-10����3:51:52 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.view.DetailsOpenFragmentView;
import com.tenone.gamebox.presenter.DetailsOpenFragmentPresenter;
import com.tenone.gamebox.view.base.BaseLazyFragment;

public class DetailsOpenFragment extends BaseLazyFragment implements
		DetailsOpenFragmentView {
	View view;
	@ViewInject(R.id.id_gameDetailsOpen_refresh)
    SwipeRefreshLayout refreshLayout;
	@ViewInject(R.id.id_gameDetailsOpen_listView)
    RecyclerView listView;

	DetailsOpenFragmentPresenter presenter;
	GameModel gameModel;
	String gameId;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		if (bundle != null && bundle.containsKey("gameModel")) {
			gameModel = (GameModel) bundle.get("gameModel");
		}
		gameId = bundle.getString("id");
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_details_open, container,
					false);
		}
		ViewUtils.inject(this, view);
		presenter = new DetailsOpenFragmentPresenter(this, getActivity());
		presenter.setAdapter();
		presenter.initListener();
		return view;
	}

	@Override
	public SwipeRefreshLayout getRefreshLayout() {
		return refreshLayout;
	}

	@Override
	public RecyclerView getListView() {
		return listView;
	}

	@Override
	public GameModel getGameModel() {
		return gameModel;
	}

	@Override
	public String getGameId() {
		return gameId;
	}

    @Override
    public void onLazyLoad() {
        refreshLayout.setRefreshing( true );
        presenter.requestHttp(HttpType.REFRESH);
    }
}
