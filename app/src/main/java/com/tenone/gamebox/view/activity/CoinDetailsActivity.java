package com.tenone.gamebox.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.MyMessageView;
import com.tenone.gamebox.presenter.CoinDetailsPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.CustomerUnderlinePageIndicator;
import com.tenone.gamebox.view.custom.NoScrollViewPager;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;


@SuppressLint("ResourceAsColor")
public class CoinDetailsActivity extends BaseActivity implements MyMessageView {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_coin_details_tabPageIndicator)
	TabPageIndicator tabPageIndicator;
	@ViewInject(R.id.id_coin_details_underlineIndicator)
	CustomerUnderlinePageIndicator indicator;
	@ViewInject(R.id.id_coin_details_viewPager)
	NoScrollViewPager viewPager;
	CoinDetailsPresenter presenter;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		setContentView( R.layout.activity_coindetails );
		ViewUtils.inject( this );
		presenter = new CoinDetailsPresenter( this, this );
		presenter.initTitle();
		presenter.setAdapter();
		presenter.initTabView();
		presenter.initListener();
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
	public CustomerUnderlinePageIndicator getIndicator() {
		return indicator;
	}

	@Override
	public NoScrollViewPager getViewPager() {
		return viewPager;
	}
}
