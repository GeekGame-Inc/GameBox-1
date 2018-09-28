package com.tenone.gamebox.view.fragment;

import android.view.View;

import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.custom.xbanner.XBanner;

public class DiscountGameFragment extends BaseIndexGameFragment {

	@Override
	public void initView() {
		super.initView();
		super.initView( 2 );
	}

	@Override
	protected void initJP(View view) {
		super.initJP( view );
		super.initJP( 2);
	}
	@Override
	protected void initRM(View view) {
		super.initRM( view );
		super.initRM( 2 );
	}

	@Override
	protected void initZX(View view) {
		super.initZX( view );
		super.initZX( 2 );
	}


	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		super.onSuccess( what, resultItem );
		onSuccess( what, resultItem, "dis_game" );
	}

	@Override
	public void onLoad() {
		super.onLoad();
		super.onLoad( 2 );
	}

	@Override
	public void onRefresh() {
		super.onRefresh();
		super.onRefresh( 2 );
	}

	@Override
	public void onLazyLoad() {
		super.onLazyLoad();
		super.onLazyLoad( "dis_game", 2 );
	}

	@Override
	public void onItemClick(XBanner banner, int position) {
		super.onItemClick( banner, position );
		super.onItemClick( 2, position + 1 );
	}
}
