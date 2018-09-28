package com.tenone.gamebox.view.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.RebateView;
import com.tenone.gamebox.presenter.RebatePresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.CustomerUnderlinePageIndicator;
import com.tenone.gamebox.view.custom.MarqueeTextView;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;


@SuppressLint("ResourceAsColor")
public class RebateActivity extends BaseActivity implements RebateView {
	@ViewInject(R.id.id_newtTitle_leftImg)
	ImageView leftImg;
	@ViewInject(R.id.id_newtTitle_title)
	TextView titleView;
	@ViewInject(R.id.id_newtTitle_rightImg)
	ImageView rightImg;

	@ViewInject(R.id.id_rebate_tabPageIndicator)
	TabPageIndicator tabPageIndicator;
	@ViewInject(R.id.id_rebate_underlineIndicator)
	CustomerUnderlinePageIndicator indicator;
	@ViewInject(R.id.id_rebate_viewPager)
	ViewPager viewPager;

	@ViewInject(R.id.id_rebate_marqueeTextView)
	MarqueeTextView marqueeTextView;

	private RebatePresenter presenter;

	private String sdkUid;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		setContentView( R.layout.activity_rebate );
		Uri uri = getIntent().getData();
		if (uri != null) {
			String userId = uri.getQueryParameter( "userId" );
			if (!TextUtils.isEmpty( userId )) {
				sdkUid = userId;
			}
		}
		ViewUtils.inject( this );
		initView();
	}

	private void initView() {
		presenter = new RebatePresenter( this, this,sdkUid );
		presenter.initView();
	}

	@Override
	public ImageView leftImg() {
		return leftImg;
	}

	@Override
	public ImageView rightImg() {
		return rightImg;
	}

	@Override
	public TextView titleTv() {
		return titleView;
	}

	@Override
	public TabPageIndicator tabPageIndicator() {
		return tabPageIndicator;
	}

	@Override
	public CustomerUnderlinePageIndicator underlinePageIndicator() {
		return indicator;
	}

	@Override
	public ViewPager viewPager() {
		return viewPager;
	}

	@Override
	protected void onDestroy() {
		marqueeTextView.releaseResources();
		super.onDestroy();
	}

	@Override
	public MarqueeTextView marqueeTextView() {
		return marqueeTextView;
	}
}
