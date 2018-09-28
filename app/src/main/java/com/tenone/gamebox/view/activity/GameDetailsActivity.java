package com.tenone.gamebox.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.GameDetailsView;
import com.tenone.gamebox.presenter.GameDetailsPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.CustomerUnderlinePageIndicator;
import com.tenone.gamebox.view.custom.DownProgress;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.TwoStateImageView;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;
@SuppressWarnings( "deprecation" )
@SuppressLint("ResourceAsColor")
public class GameDetailsActivity extends BaseActivity implements
		GameDetailsView {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_gameDetails_img)
	ImageView imageView;
	@ViewInject(R.id.id_gameDetails_gameName)
	TextView nameTv;
	@ViewInject(R.id.id_gameDetails_ratingBar)
	RatingBar ratingBar;
	@ViewInject(R.id.id_gameDetails_size)
	TextView sizeTv;
	@ViewInject(R.id.id_gameDetails_label)
	LinearLayout label;
	@ViewInject(R.id.id_gameDetails_tabPageIndicator)
	TabPageIndicator tabPageIndicator;
	@ViewInject(R.id.id_gameDetails_underlineIndicator)
	CustomerUnderlinePageIndicator pageIndicator;
	@ViewInject(R.id.id_gameDetails_viewPager)
	ViewPager viewPager;
	@ViewInject(R.id.id_gameDetails_collect)
	TwoStateImageView collectBt;
	@ViewInject(R.id.id_gameDetails_share)
	TwoStateImageView shareBt;
	@ViewInject(R.id.id_gameDetails_dwon)
	DownProgress progress;
	@ViewInject(R.id.id_gameDetails_qq)
	LinearLayout qqLayout;

	private GameDetailsPresenter presenter;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_game_details);
		ViewUtils.inject(this);
		presenter = new GameDetailsPresenter(this, this);
		presenter.initListener();
	}

	@Override
	public TitleBarView getTitleBarView() {
		return titleBarView;
	}

	@Override
	public ImageView getImageView() {
		return imageView;
	}

	@Override
	public TextView getNameTv() {
		return nameTv;
	}

	@Override
	public RatingBar getRatingBar() {
		return ratingBar;
	}

	@Override
	public TextView getSizeTv() {
		return sizeTv;
	}

	@Override
	public LinearLayout getLabelLayout() {
		return label;
	}

	@Override
	public TabPageIndicator getTabPageIndicator() {
		return tabPageIndicator;
	}

	@Override
	public CustomerUnderlinePageIndicator getPageIndicator() {
		return pageIndicator;
	}

	@Override
	public ViewPager getViewPager() {
		return viewPager;
	}

	@Override
	public TwoStateImageView getCollectBt() {
		return collectBt;
	}

	@Override
	public TwoStateImageView getShareBt() {
		return shareBt;
	}

	@Override
	public DownProgress getProgress() {
		return progress;
	}

	@Override
	public Intent getIntent() {
		return super.getIntent();
	}

	@Override
	protected void onDestroy() {
		presenter.onDestroy();
		super.onDestroy();
	}
	@Override
	public LinearLayout getQQLayout() {
		return qqLayout;
	}
}
