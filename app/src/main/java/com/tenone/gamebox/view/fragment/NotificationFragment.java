/** 
 * Project Name:GameBox 
 * File Name:NotificationFragment.java 
 * Package Name:com.tenone.gamebox.view.fragment 
 * Date:2017-3-22ÏÂÎç2:59:52 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.NotificationFragmentView;
import com.tenone.gamebox.presenter.NotificationFragmentPresenter;
import com.tenone.gamebox.view.base.BaseFragment;

public class NotificationFragment extends BaseFragment implements
		NotificationFragmentView {
	@ViewInject(R.id.id_notification_openServicer)
	TextView openSerVicerTv;

	@ViewInject(R.id.id_notification_systemNotification)
	TextView notificationTv;
	View view;

	NotificationFragmentPresenter presenter;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_notification, container,
					false);
		}
		ViewUtils.inject(this, view);
		presenter = new NotificationFragmentPresenter(this, getActivity());
		presenter.initListener();
		return view;
	}

	@Override
	public TextView getOpenServiceTv() {
		return openSerVicerTv;
	}

	@Override
	public TextView getSystemNotificationTv() {
		return notificationTv;
	}
}
