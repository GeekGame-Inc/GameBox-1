package com.tenone.gamebox.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.GoldCoinCenterBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.GoldCoinCenterView;
import com.tenone.gamebox.view.activity.ExchangePlatformActivity;
import com.tenone.gamebox.view.activity.PlatformCoinDetailActivity;
import com.tenone.gamebox.view.activity.WebActivity;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TrackingUtils;

import java.util.HashMap;
import java.util.Map;

public class GoldCoinCenterPresenter extends BasePresenter implements
		OnClickListener, HttpResultListener {
	private GoldCoinCenterBiz centerBiz;
	private GoldCoinCenterView centerView;
	private Context context;

	public GoldCoinCenterPresenter(GoldCoinCenterView v, Context c) {
		this.context = c;
		this.centerView = v;
		this.centerBiz = new GoldCoinCenterBiz();
	}

	public void initView() {
		titleBarView().setTitleText( "\u91d1\u5e01\u4e2d\u5fc3" );
		titleBarView().setLeftImg( R.drawable.icon_xqf_b );
		titleBarView().setRightText( "\u91d1\u5e01\u660e\u7ec6" );
		titleBarView().setOnClickListener( R.id.id_titleBar_leftImg, this );
		titleBarView().setOnClickListener( R.id.id_titleBar_rightText, this );
		luckyBt().setOnClickListener( this );
		changeBt().setOnClickListener( this );
		request();
	}

	public void onRestart() {
		request();
	}

	private void request() {
		HttpManager.myCoin( HttpType.REFRESH, context, this );
	}

	private void setView(ResultItem result) {
		banlanceTv().setText( centerBiz.getBanlance( result ) );
		todayTv().setText( centerBiz.getToday( result ) );
		tomonthTv().setText( centerBiz.getToMonth( result ) );
	}

	private TitleBarView titleBarView() {
		return centerView.titleBarView();
	}

	private TextView banlanceTv() {
		return centerView.banlanceTv();
	}

	private TextView todayTv() {
		return centerView.todayTv();
	}

	private TextView tomonthTv() {
		return centerView.tomonthTv();
	}

	private Button luckyBt() {
		return centerView.luckyBt();
	}

	private Button changeBt() {
		return centerView.changeBt();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.id_titleBar_leftImg:
				close( context );
				break;
			case R.id.id_titleBar_rightText:
				openOtherActivity( context, new Intent( context,
						PlatformCoinDetailActivity.class ).setAction( "gold" ) );
				break;
			case R.id.id_gold_nowLucky:
				Intent intent = new Intent();
				intent.putExtra( "title", "\u91d1\u5e01\u62bd\u5956" );
				String url = MyApplication.getHttpUrl().getLuckyUrl() + "&uid="
						+ SpUtil.getUserId();
				intent.putExtra( "url", url );
				intent.setClass( context, WebActivity.class );
				openOtherActivity( context, intent );
				Map<String, Object> map = new HashMap<String, Object>();
				map.put( TrackingUtils.USERNAMEKEY, SpUtil.getAccount() );
				map.put( TrackingUtils.NICKNAMEKEY, SpUtil.getNick() );
				map.put( TrackingUtils.MOBILEKEY, SpUtil.getPhone() );
				TrackingUtils.setEvent( TrackingUtils.LUCKYDRAWEVENT, map );
				break;
			case R.id.id_gold_nowChange:
				openOtherActivityForResult( context, 268, new Intent( context,
						ExchangePlatformActivity.class ) );
				break;
		}
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		if (1 == resultItem.getIntValue( "status" )) {
			ResultItem item = resultItem.getItem( "data" );
			if (item != null) {
				setView( item );
			}
		} else {
			ToastCustom.makeText( context, resultItem.getString( "msg" ),
					ToastCustom.LENGTH_SHORT ).show();
		}
	}

	@Override
	public void onError(int what, String error) {
		ToastCustom.makeText( context, error, ToastCustom.LENGTH_SHORT ).show();
	}
}
