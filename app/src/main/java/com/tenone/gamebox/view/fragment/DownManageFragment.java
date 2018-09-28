

package com.tenone.gamebox.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.DownManageFragmentView;
import com.tenone.gamebox.presenter.DownManageFragmentPresenter;
import com.tenone.gamebox.view.base.BaseFragment;


public class DownManageFragment extends BaseFragment implements
		DownManageFragmentView {
	@ViewInject(R.id.id_downManage_listview)
	ListView listView;
	@ViewInject(R.id.id_downManage_deleteBt)
	Button deleteBt;
	
	View view;
	DownManageFragmentPresenter presenter;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_down_manage, container,
					false);
		}
		ViewUtils.inject(this, view);
		presenter = new DownManageFragmentPresenter(this, getActivity());
		presenter.setAdapter();
		presenter.initListener();
		return view;
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

	@Override
	public Button getButton() {
		return deleteBt;
	}
}
