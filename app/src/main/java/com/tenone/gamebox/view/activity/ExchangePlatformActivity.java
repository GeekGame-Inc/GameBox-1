package com.tenone.gamebox.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TrackingUtils;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("ResourceAsColor")
public class ExchangePlatformActivity extends BaseActivity implements
		HttpResultListener {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_exchange_goldNum)
	TextView goldNumTv;
	@ViewInject(R.id.id_exchange_platformCoinNum)
	TextView platformCoinNumTv;
	@ViewInject(R.id.id_exchange_instruction)
	TextView instructionTv;
	@ViewInject(R.id.id_exchange_edit)
	EditText editText;
	private int coinNum = 0, count = 0, ratio = 0, can = 0;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		setContentView( R.layout.activity_exchange_platform );
		ViewUtils.inject( this );
		request();
		initView();
	}

	private void initView() {
		titleBarView.setLeftImg( R.drawable.icon_xqf_b );
		titleBarView.setTitleText( "\u5e73\u53f0\u5e01\u5151\u6362" );
	}

	private void request() {
		HttpManager.getCoinInfo( HttpType.REFRESH, this, this );
	}

	private void exchange(String counts) {
		HttpManager.exchange( HttpType.LOADING, this, this, counts );
	}

	private void setView(ResultItem item) {
		coinNum = item.getIntValue( "coin" );
		ratio = item.getIntValue( "platform_coin_ratio" );
		count = item.getIntValue( "platform_start_count" );
		can = (coinNum / ratio) > (count - 1) ? (coinNum / ratio) : 0;
		goldNumTv.setText( "\u6211\u7684\u91d1\u5e01 : " + coinNum );
		instructionTv.setText( ratio + "\u91d1\u5e01=1\u5e73\u53f0\u5e01,\u5c0f\u4e8e" + count + "\u5e73\u53f0\u5e01\u4e0d\u80fd\u5151\u6362" );
		platformCoinNumTv.setText( "\u53ef\u5151\u6362\u5e73\u53f0\u5e01: " + can );
	}

	@OnClick({R.id.id_exchange_button, R.id.id_titleBar_leftImg})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.id_exchange_button:
				String counts = editText.getText().toString();
				if (TextUtils.isEmpty( counts )) {
					ToastCustom.makeText( this, "\u8bf7\u8f93\u5165\u8981\u5151\u6362\u7684\u5e73\u53f0\u5e01\u6570\u91cf",
							ToastCustom.LENGTH_SHORT ).show();
					return;
				} else {
					int a = Integer.valueOf( counts );
					if (a < 10) {
						ToastCustom.makeText( this, "\u5bf9\u4e0d\u8d77,\u4e0d\u8db310\u5e73\u53f0\u5e01\u4e0d\u80fd\u5151\u6362,\u8bf7\u91cd\u65b0\u8f93\u5165",
								ToastCustom.LENGTH_SHORT ).show();
						return;
					}
					if (a > can) {
						ToastCustom.makeText( this, "\u5bf9\u4e0d\u8d77,\u60a8\u7684\u91d1\u5e01\u4e0d\u8db3,\u8bf7\u91cd\u65b0\u8f93\u5165",
								ToastCustom.LENGTH_SHORT ).show();
						return;
					}
				}
				exchange( counts );
				break;
			case R.id.id_titleBar_leftImg:
				finish();
				break;
		}
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		if (1 == resultItem.getIntValue( "status" )) {
			switch (what) {
				case HttpType.REFRESH:
					ResultItem item = resultItem.getItem( "data" );
					if (item != null) {
						setView( item );
					}
					break;
				case HttpType.LOADING:
					request();
					ToastCustom.makeText( this, "\u5151\u6362\u6210\u529f", ToastCustom.LENGTH_SHORT )
							.show();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put( TrackingUtils.USERNAMEKEY, SpUtil.getAccount() );
					map.put( TrackingUtils.NICKNAMEKEY, SpUtil.getNick() );
					map.put( TrackingUtils.MOBILEKEY, SpUtil.getPhone() );
					TrackingUtils.setEvent( TrackingUtils.EXCHANGEEVENT, map );
					break;
			}
		} else {
			ToastCustom.makeText( this, resultItem.getString( "msg" ),
					ToastCustom.LENGTH_SHORT ).show();
		}
	}

	@Override
	public void onError(int what, String error) {
		ToastCustom.makeText( this, error, ToastCustom.LENGTH_SHORT ).show();
	}
}
