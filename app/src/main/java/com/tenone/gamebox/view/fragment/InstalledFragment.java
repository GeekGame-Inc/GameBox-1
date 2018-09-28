/** 
 * Project Name:GameBox 
 * File Name:InstalledFragment.java 
 * Package Name:com.tenone.gamebox.view.fragment 
 * Date:2017-3-20下午4:33:54 
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
import com.tenone.gamebox.mode.view.InstalledFragmentView;
import com.tenone.gamebox.presenter.InstalledFragmentPresenter;
import com.tenone.gamebox.view.base.BaseFragment;

/**
 * 已经装应用 ClassName:InstalledFragment <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-20 下午4:33:54 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class InstalledFragment extends BaseFragment implements
		InstalledFragmentView {
	@ViewInject(R.id.id_installed_listview)
	ListView listView;

	View view;
	InstalledFragmentPresenter presenter;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_instatlled, container,
					false);
		}
		ViewUtils.inject(this, view);
		presenter = new InstalledFragmentPresenter(getActivity(), this);
		presenter.setAdapter();
		presenter.initListener();
		return view;
	}

	@Override
	public ListView getListView() {
		return listView;
	}
}
