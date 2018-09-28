package com.tenone.gamebox.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.view.GameHotFragmentView;
import com.tenone.gamebox.presenter.GameHotFragmentPresenter;
import com.tenone.gamebox.view.base.BaseFragment;
import com.tenone.gamebox.view.custom.RefreshLayout;

public class GameHotFragment extends BaseFragment implements
		GameHotFragmentView {
	View view;
	@ViewInject(R.id.id_hot_refresh)
	RefreshLayout refreshLayout;
	@ViewInject(R.id.id_hot_listView)
	ListView listView;

	GameHotFragmentPresenter fragmentPresenter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_game_hot, container,
					false);
		}
		ViewUtils.inject(this, view);
		fragmentPresenter = new GameHotFragmentPresenter(this, getActivity());
		fragmentPresenter.setAdapter();
		fragmentPresenter.initListener();
		fragmentPresenter.requestHttp(HttpType.REFRESH);
		return view;
	}
	
	
	@Override
	public RefreshLayout getRefreshLayout() {
		return refreshLayout;
	}

	@Override
	public ListView getRecommendListView() {
		return listView;
	}
}
