/** 
 * Project Name:GameBox 
 * File Name:DetailsGiftFragment.java 
 * Package Name:com.tenone.gamebox.view.fragment 
 * Date:2017-4-10����3:51:21 
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
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.view.DetailsGiftFragmentView;
import com.tenone.gamebox.presenter.DetailsGiftFragmentPresenter;
import com.tenone.gamebox.view.base.BaseLazyFragment;

public class DetailsGiftFragment extends BaseLazyFragment implements
		DetailsGiftFragmentView {
	@ViewInject(R.id.id_gameDetailsGift_listView)
    RecyclerView listView;
    @ViewInject( R.id.id_gameDetailsGift_refreshLayout )
	SwipeRefreshLayout refreshLayout;
	View view;

	DetailsGiftFragmentPresenter presenter;
	String gameId;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_details_gift, container,
					false);
		}
		ViewUtils.inject(this, view);
		Bundle bundle = getArguments();
		gameId = bundle.getString("id");
		presenter = new DetailsGiftFragmentPresenter(this, getActivity());
		presenter.setAdapter();
		return view;
	}

	@Override
	public RecyclerView getListView() {
		return listView;
	}

	@Override
	public String getGameId() {
		return gameId;
	}

    @Override
    public SwipeRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    @Override
    public void onLazyLoad() {
        presenter.requestList(HttpType.REFRESH);
    }
}
