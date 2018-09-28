package com.tenone.gamebox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ExpandableListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.RebateHelp;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.adapter.RebateHelpAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.ChenColorUtils;
import com.tenone.gamebox.view.utils.HttpManager;

import java.util.ArrayList;
import java.util.List;

public class RebateHelpActivity extends BaseActivity implements
		HttpResultListener {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_rebate_help_expandable)
	ExpandableListView expandableListView;

	private RebateHelpAdapter adapter;
	private Context mContext;
	private List<RebateHelp> rebateHelps = new ArrayList<RebateHelp>();
	private String[] titles = new String[]{"\u8fd4\u5229\u987b\u77e5", "\u8f6c\u6e38\u987b\u77e5", "\u6392\u884c\u5956\u52b1\u987b\u77e5"};
	private int what = 0;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		setContentView( R.layout.activity_rebate_help );
		ViewUtils.inject( this );
		mContext = this;
		what = getIntent().getIntExtra( "what", 0 );
		initView();
	}

	private void initTile() {
		titleBarView.setTitleText( titles[what] );
		titleBarView.setLeftImg( R.drawable.icon_xqf_b );
		Drawable rightDrawable = mContext.getResources().getDrawable(
				R.drawable.icon_kefu_title );
		titleBarView.setRightImg( ChenColorUtils.tintDrawable( rightDrawable,
				ColorStateList.valueOf( mContext.getResources().getColor( R.color.gray_69 ) ) ) );
		switch (what) {
			case 0:
				rebateKnow();
				break;
			case 1:
				changeGameKnow();
				break;
			case 2:
				rankNotice();
				break;
		}
	}

	private void initView() {
		initTile();
		adapter = new RebateHelpAdapter( mContext, rebateHelps );
		expandableListView.setAdapter( adapter );
	}

	@OnClick({R.id.id_titleBar_leftImg, R.id.id_titleBar_rightImg})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.id_titleBar_leftImg:
				finish();
				break;
			case R.id.id_titleBar_rightImg:
				startActivity( new Intent( mContext, CallCenterActivity.class ) );
				break;
		}
	}

	private void rebateKnow() {
		HttpManager.rebateKnow( HttpType.REFRESH, mContext, this );
	}

	private void changeGameKnow() {
		HttpManager.changeGameKnow( HttpType.LOADING, mContext, this );
	}

	private void rankNotice() {
		HttpManager.rankNotice( 2, mContext, this );
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		if (1 == resultItem.getIntValue( "status" )) {
			List<ResultItem> items = resultItem.getItems( "data" );
			setData( items );
		} else {
			ToastCustom.makeText( mContext, resultItem.getString( "msg" ),
					ToastCustom.LENGTH_SHORT ).show();
		}
	}

	@Override
	public void onError(int what, String error) {
		ToastCustom.makeText( mContext, error, ToastCustom.LENGTH_SHORT ).show();
	}

	private void setData(List<ResultItem> items) {
		if (items != null) {
			for (int i = 0; i < items.size(); i++) {
				ResultItem item = items.get( i );
				RebateHelp help = new RebateHelp();
				help.setTitle( item.getString( "title" ) );
				String list = item.getString( "content" );
				help.setArrays( list );
				rebateHelps.add( help );
			}
			adapter.notifyDataSetChanged();
		}
	}
}
