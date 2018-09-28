package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.AppBarStateChangeListener;
import com.tenone.gamebox.view.base.BaseActivity;

public class QuestionUserInfoActivity extends BaseActivity {
	@ViewInject(R.id.id_question_user_info_appBar)
	AppBarLayout appBarLayout;
	@ViewInject(R.id.id_question_user_info_collapsing)
	CollapsingToolbarLayout collapsingToolbarLayout;
	@ViewInject(R.id.id_question_user_info_toolbar)
	Toolbar toolbar;
	@ViewInject(R.id.id_question_user_info_tabLayout)
	TabLayout tabLayout;
	@ViewInject(R.id.id_question_user_info_viewpager)
	ViewPager viewPager;
	@ViewInject(R.id.id_question_user_info_header)
	ImageView headerIv;
	@ViewInject(R.id.id_question_user_info_nick)
	TextView nickTv;
	@ViewInject(R.id.id_question_user_info_intro)
	TextView introTv;
	@ViewInject(R.id.id_question_user_info_labelView)
	View badgeView;
	@ViewInject(R.id.id_question_user_info_labelView2)
	View badgeView2;
	@ViewInject(R.id.id_question_user_info_title)
	TextView titleTv;
	@ViewInject(R.id.id_question_user_info_notice)
	ImageView noticeIv;

	private String uid;
	private String title = "";

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		uid = getIntent().getStringExtra( "uid" );
		setContentView( R.layout.activity_question_info );
		ViewUtils.inject( this );
		ImmersionBar.with( this ).titleBar( toolbar ).statusBarDarkFont( true ).init();
		initTitle();
		initView();
	}

	private void initTitle() {
		collapsingToolbarLayout.setTitle( "" );
		toolbar.setTitle( "" );
		toolbar.setContentInsetsAbsolute( 0, 0 );
		setSupportActionBar( toolbar );
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled( true );
		}
		toolbar.setNavigationOnClickListener( v -> {
			finish();
		} );
		noticeIv.setOnClickListener( v -> {

		} );

		appBarLayout.addOnOffsetChangedListener( new AppBarStateChangeListener() {
			@Override
			public void onStateChanged(AppBarLayout appBarLayout, AppBarStateChangeListener.State state) {
				if (state == State.EXPANDED) {
					titleTv.setText( "" );
					noticeIv.setSelected( false );
					toolbar.setSelected( false );
				} else if (state == State.COLLAPSED) {
					titleTv.setText( title );
					noticeIv.setSelected( true );
					toolbar.setSelected( true );
				} else {
					titleTv.setText( title );
					noticeIv.setSelected( true );
					toolbar.setSelected( true );
				}
			}
		} );
		tabLayout.setupWithViewPager( viewPager );
	}

	private void initView() {


	}
}
