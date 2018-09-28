package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.view.AttentionView;
import com.tenone.gamebox.presenter.AttentionPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;

public class MyAttentionActivity extends BaseActivity implements AttentionView {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_attention_refresh)
	RefreshLayout refreshLayout;
	@ViewInject(R.id.id_attention_listView)
	ListView listView;

	AttentionPresenter presenter;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_attention);
		ViewUtils.inject(this);
		presenter = new AttentionPresenter(this, this);
		presenter.initView();
		presenter.setAdapter();
		presenter.initListener();
		presenter.requestHttp(HttpType.REFRESH);
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
	protected void onDestroy() {
		presenter.destory();
		super.onDestroy();
	}
}
