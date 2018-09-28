package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.PlatformCoinDetailView;
import com.tenone.gamebox.presenter.PlatformCoinPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;

public class PlatformCoinDetailActivity extends BaseActivity implements
		PlatformCoinDetailView {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_platform_coin_refresh)
	RefreshLayout refreshLayout;
	@ViewInject(R.id.id_platform_coin_listview)
	ListView listView;
	@ViewInject(R.id.id_platform_coin_emptyView)
	ImageView emptyView;
	@ViewInject(R.id.id_platform_coin_toast)
	TextView toastTv;

	private PlatformCoinPresenter coinPresenter;
	private boolean type = false;
	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_platform_coin_details);
		ViewUtils.inject(this);
		type = getIntent().getAction().equals("gold");
		coinPresenter = new PlatformCoinPresenter(this, this, type);
		coinPresenter.initView();
	}

	@Override
	public TitleBarView getTitleBarView() {
		return titleBarView;
	}

	@Override
	public RefreshLayout getRefreshLayout() {
		return refreshLayout;
	}

	@Override
	public ListView getListView() {
		return listView;
	}

	@Override
	public ImageView getEmptyView() {
		return emptyView;
	}

	@Override
	public TextView getToastTv() {
		return toastTv;
	}
}
