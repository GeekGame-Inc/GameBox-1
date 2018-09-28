package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.view.GameGiftView;
import com.tenone.gamebox.presenter.GameGiftPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.RefreshLayout;

public class GiftActivity extends BaseActivity implements GameGiftView {

	@ViewInject(R.id.id_serachBar)
	View serachBar;

	@ViewInject(R.id.id_gift_refreshLayout)
	RefreshLayout refreshLayout;

	@ViewInject(R.id.id_gift_listView)
	ListView listView;
	
	
	@ViewInject(R.id.id_main_downBadge)
	TextView downBadgeTv;
	
	@ViewInject(R.id.id_main_messageBadge)
	TextView messageBadgeTv;

	GameGiftPresenter gameGiftPresenter;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gift);
		ViewUtils.inject(this);
		gameGiftPresenter = new GameGiftPresenter(this, this);
		gameGiftPresenter.setAdapter();
		gameGiftPresenter.initListener();
		gameGiftPresenter.initView();
		gameGiftPresenter.requestList(HttpType.REFRESH);
	}

	@Override
	public View getSerachBar() {
		return serachBar;
	}

	@Override
	public RefreshLayout getRefreshLayout() {
		return refreshLayout;
	}

	@Override
	public ListView getRecommendListView() {
		return listView;
	}

	@Override
	public TextView getDownBadgeTv() {
		return downBadgeTv;
	}

	@Override
	public TextView getMessageBadgeTv() {
		return messageBadgeTv;
	}
}
