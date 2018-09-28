/** 
 * Project Name:GameBox 
 * File Name:UpdateFragment.java 
 * Package Name:com.tenone.gamebox.view.fragment 
 * Date:2017-3-20下午5:57:20 
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
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.view.UpdateFragmentView;
import com.tenone.gamebox.presenter.UpdateFragmentPresenter;
import com.tenone.gamebox.view.base.BaseFragment;

/**
 * 更新 ClassName:UpdateFragment <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-20 下午5:57:20 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class UpdateFragment extends BaseFragment implements UpdateFragmentView {
	@ViewInject(R.id.id_updateFragment_listview)
	ListView listView;
	@ViewInject(R.id.id_updateFragment_allUpdate)
	TextView allUpdate;

	View view;
	UpdateFragmentPresenter presenter;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_update, container, false);
		}
		ViewUtils.inject(this, view);
		presenter = new UpdateFragmentPresenter(getActivity(), this);
		presenter.setAdapter();
		presenter.initListener();
		presenter.requestHttp(HttpType.REFRESH);
		return view;
	}

	@Override
	public ListView getListView() {
		return listView;
	}

	@Override
	public TextView getAllUpdateView() {
		return allUpdate;
	}
	
	@Override
	public void onDestroy() {
		presenter.destory();
		super.onDestroy();
	}
}
