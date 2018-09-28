package com.tenone.gamebox.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.view.GameClassifyView;
import com.tenone.gamebox.presenter.GameClassifyPresenter;
import com.tenone.gamebox.view.base.BaseFragment;
import com.tenone.gamebox.view.custom.MyExpandableListView;
import com.tenone.gamebox.view.custom.RefreshLayout;

/**
 * 游戏分类 ClassName: GameClassifyFragment <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017-3-9 下午4:37:22 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 */
public class GameClassifyFragment extends BaseFragment implements
		GameClassifyView {
	@ViewInject(R.id.id_classify_expandableView)
	MyExpandableListView expandableListView;
	@ViewInject(R.id.id_classify_refreshLayout)
	RefreshLayout refreshLayout;
	View view;
	GameClassifyPresenter classifyPresenter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_game_classify, container,
					false);
		}
		ViewUtils.inject(this, view);
		classifyPresenter = new GameClassifyPresenter(this, getActivity());
		classifyPresenter.initView();
		classifyPresenter.setAdapter();
		classifyPresenter.initListener();
		classifyPresenter.requestHttp(HttpType.REFRESH);
		return view;
	}

	@Override
	public MyExpandableListView getExpandableListView() {
		return expandableListView;
	}

	@Override
	public RefreshLayout getRefreshLayout() {
		return refreshLayout;
	}
}
