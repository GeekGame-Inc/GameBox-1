package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.GoldCoinCenterView;
import com.tenone.gamebox.presenter.GoldCoinCenterPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.TitleBarView;

public class GoldCoinCenterActivity extends BaseActivity implements
		GoldCoinCenterView {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_gold_balance)
	TextView banlanceTv;
	@ViewInject(R.id.id_gold_today)
	TextView todayTv;
	@ViewInject(R.id.id_gold_tomonth)
	TextView tomonthTv;
	@ViewInject(R.id.id_gold_nowLucky)
	Button luckyBt;
	@ViewInject(R.id.id_gold_nowChange)
	Button changeBt;

	private GoldCoinCenterPresenter presenter;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_gold_coin_center);
		ViewUtils.inject(this);
		presenter = new GoldCoinCenterPresenter(this, this);
		presenter.initView();
	}

	@Override
	protected void onRestart() {
		presenter.onRestart();
		super.onRestart();
	}
	
	@Override
	public TitleBarView titleBarView() {
		return titleBarView;
	}

	@Override
	public TextView banlanceTv() {
		return banlanceTv;
	}

	@Override
	public TextView todayTv() {
		return todayTv;
	}

	@Override
	public TextView tomonthTv() {
		return tomonthTv;
	}

	@Override
	public Button luckyBt() {
		return luckyBt;
	}

	@Override
	public Button changeBt() {
		return changeBt;
	}
}
