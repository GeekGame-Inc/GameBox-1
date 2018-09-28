package com.tenone.gamebox.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.GameTransferView;
import com.tenone.gamebox.presenter.GameTransferPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.CustomerUnderlinePageIndicator;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;

@SuppressLint("ResourceAsColor")
public class GameTransferActivity extends BaseActivity implements GameTransferView {
	@ViewInject(R.id.id_transfer_tabPageIndicator)
	TabPageIndicator tabPageIndicator;
	@ViewInject(R.id.id_transfer_underlineIndicator)
	CustomerUnderlinePageIndicator indicator;
	@ViewInject(R.id.id_transfer_viewPager)
	ViewPager viewPager;
	@ViewInject(R.id.id_title_bar)
	TitleBarView barView;
	
	GameTransferPresenter presenter;
	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_game_transfer);
		ViewUtils.inject(this);
		presenter = new GameTransferPresenter(this, this);
		presenter.initView();
	}
	
	@Override
	public TabPageIndicator tabPageIndicator() {
		return tabPageIndicator;
	}
	
	@Override
	public CustomerUnderlinePageIndicator indicator() {
		return indicator;
	}
	@Override
	public ViewPager viewPager() {
		return viewPager;
	}
	@Override
	public TitleBarView titleBarView() {
		return barView;
	}
}
