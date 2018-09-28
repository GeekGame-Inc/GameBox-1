package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.ManagementView;
import com.tenone.gamebox.presenter.ManagementPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.CustomerUnderlinePageIndicator;
import com.tenone.gamebox.view.custom.NoScrollViewPager;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;

public class ManagementActivity extends BaseActivity implements ManagementView {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_management_tabPageIndicator)
	TabPageIndicator tabPageIndicator;
	@ViewInject(R.id.id_management_underlineIndicator)
	CustomerUnderlinePageIndicator underlineIndicator;
	@ViewInject(R.id.id_management_viewPager)
	NoScrollViewPager viewPager;

	private ManagementPresenter presenter;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_management);
		ViewUtils.inject(this);
		presenter = new ManagementPresenter(this, this);
		presenter.initTitle();
		presenter.initListener();
		presenter.setAdapter(this);
		presenter.initTabView();
	}

	@Override
	public TitleBarView getTitleBarView() {
		return titleBarView;
	}

	@Override
	public TabPageIndicator getTabPageIndicator() {
		return tabPageIndicator;
	}

	@Override
	public CustomerUnderlinePageIndicator getCustomerUnderlinePageIndicator() {
		return underlineIndicator;
	}

	@Override
	public NoScrollViewPager getViewPager() {
		return viewPager;
	}
	@Override
	protected void onDestroy() {
		presenter.onDestroy();
		super.onDestroy();
	}
}
