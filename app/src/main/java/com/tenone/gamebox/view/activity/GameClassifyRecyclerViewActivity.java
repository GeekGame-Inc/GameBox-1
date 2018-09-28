package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.view.GameClassifyRecyclerView;
import com.tenone.gamebox.presenter.GameClassifyRecyclerPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.TitleBarView;
public class GameClassifyRecyclerViewActivity extends BaseActivity implements
		GameClassifyRecyclerView {
	@ViewInject(R.id.id_classify_recyclerView)
	RecyclerView recyclerView;
	@ViewInject(R.id.id_classify_refreshLayout)
	SwipeRefreshLayout refreshLayout;
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;

	private GameClassifyRecyclerPresenter classifyPresenter;
	private int platform = 1;
	private String topGameName;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		setContentView( R.layout.fragment_classify_recycle );
		ViewUtils.inject( this );
		titleBarView.setTitleText( "\u5206\u7c7b" );
		titleBarView.setLeftImg( R.drawable.icon_xqf_b );
		titleBarView.setOnClickListener( R.id.id_titleBar_leftImg, v -> {
			finish();
		} );
		platform = getIntent().getIntExtra( "platform", 1 );
		topGameName = getIntent().getStringExtra( "topGame" );
		classifyPresenter = new GameClassifyRecyclerPresenter( this, this, platform, topGameName );
		classifyPresenter.initView();
		classifyPresenter.setAdapter();
		classifyPresenter.initListener();
		classifyPresenter.requestHttp( HttpType.REFRESH );
	}

	@Override
	public SwipeRefreshLayout getRefreshLayout() {
		return refreshLayout;
	}

	@Override
	public RecyclerView getRecyclerView() {
		return recyclerView;
	}
}
