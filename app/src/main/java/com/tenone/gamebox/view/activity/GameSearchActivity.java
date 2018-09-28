package com.tenone.gamebox.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.view.GameSearchView;
import com.tenone.gamebox.presenter.GameSearchPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.MyGridView;
import com.tenone.gamebox.view.custom.MyListView;
import com.tenone.gamebox.view.custom.SearchTitleBarView;

@SuppressLint("ResourceAsColor")
public class GameSearchActivity extends BaseActivity implements GameSearchView {
	@ViewInject(R.id.id_searchBar)
	SearchTitleBarView searchTitleBarView;
	@ViewInject(R.id.id_gameSearch_gridViewTop)
	MyGridView topGridView;
	@ViewInject(R.id.id_gameSearch_gridViewBottom)
	MyGridView bottomGridView;
	@ViewInject(R.id.id_gameSearch_listView)
	MyListView listView;

	private GameSearchPresenter presenter;
	private int platform = 1;
	private String topGameName;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		setContentView( R.layout.activity_game_search );
		ViewUtils.inject( this );
		Intent intent = getIntent();
		if (intent.hasExtra( "platform" )) {
			platform = intent.getIntExtra( "platform", 1 );
		}
		if (intent.hasExtra( "topGame" )) {
			topGameName = intent.getStringExtra( "topGame" );
		}
		presenter = new GameSearchPresenter( this, this, platform,topGameName );
		presenter.initView();
		presenter.setAdapter();
		presenter.initListener();
		presenter.requestList( HttpType.REFRESH );
	}

	@Override
	public SearchTitleBarView getSearchTitleBarView() {
		return searchTitleBarView;
	}

	@Override
	public MyGridView getTopGridView() {
		return topGridView;
	}

	@Override
	public MyGridView getBottomGridView() {
		return bottomGridView;
	}

	@Override
	public MyListView getListView() {
		return listView;
	}
}
