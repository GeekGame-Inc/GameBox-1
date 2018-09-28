package com.tenone.gamebox.view.fragment;

import android.view.View;

import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.custom.xbanner.XBanner;

public class H5Fragment extends BaseIndexGameFragment {
	@Override
	public void initView() {
		super.initView();
		super.initView( 3 );
	}

	@Override
	protected void initJP(View view) {
		super.initJP( view );
		super.initJP( 3 );
	}

	@Override
	protected void initRM(View view) {
		super.initRM( view );
		super.initRM( 3 );
	}

	@Override
	protected void initZX(View view) {
		super.initZX( view );
		super.initZX( 3 );
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		super.onSuccess( what, resultItem );
		super.onSuccess( what, resultItem, "h5_game" );
	}

	@Override
	public void onLoad() {
		super.onLoad();
		super.onLoad( 3 );
	}

	@Override
	public void onRefresh() {
		super.onRefresh();
		super.onRefresh( 3 );
	}

	@Override
	public void onLazyLoad() {
		super.onLazyLoad();
		super.onLazyLoad( "h5_game", 3 );
	}


	@Override
	public void onItemClick(XBanner banner, int position) {
		super.onItemClick( banner, position );
		super.onItemClick( 3, position + 1 );
	}
}
