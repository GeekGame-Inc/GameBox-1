package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OnTabLayoutTextToLeftRightListener;
import com.tenone.gamebox.view.adapter.ManagementAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.NoScrollViewPager;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.fragment.HaveToBuyFragment;
import com.tenone.gamebox.view.fragment.HaveToSellFragment;
import com.tenone.gamebox.view.fragment.InTheReviewFragment;
import com.tenone.gamebox.view.fragment.InTheSaleFragment;
import com.tenone.gamebox.view.fragment.InTheWarehouseFragment;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.ListenerManager;

import java.util.ArrayList;
import java.util.List;

public class TradingRecordActivity extends BaseActivity implements Runnable, OnTabLayoutTextToLeftRightListener {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_trading_record_tabLayout)
	TabLayout tabLayout;
	@ViewInject(R.id.id_trading_record_viewpager)
	NoScrollViewPager viewPager;

	private List<String> tabTexts = new ArrayList<String>();
	private ManagementAdapter mAdapter;
	private List<Fragment> fragments = new ArrayList<Fragment>();

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		setContentView( R.layout.activity_trading_record );
		ViewUtils.inject( this );
		initTitle();
		initView();
	}

	private void initTitle() {
		titleBarView.setTitleText( "\u4ea4\u6613\u8bb0\u5f55" );
		titleBarView.setLeftImg( R.drawable.icon_xqf_b );
		titleBarView.setOnClickListener( R.id.id_titleBar_leftImg, v -> finish() );
	}

	private void initView() {
		tabLayout.setupWithViewPager( viewPager );
		new Thread( this ).start();
	}

	private void setTitles() {
		String[] array = getResources().getStringArray( R.array.trading_record_titles );
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				String string = array[i];
				tabTexts.add( string );
			}
		}
	}

	@Override
	public void run() {
		fragments.add( new HaveToBuyFragment() );
		fragments.add( new InTheReviewFragment() );
		fragments.add( new InTheSaleFragment() );
		fragments.add( new HaveToSellFragment() );
		fragments.add( new InTheWarehouseFragment() );
		setTitles();
		mAdapter = new ManagementAdapter( getSupportFragmentManager() );
		mAdapter.setArray( fragments );
		mAdapter.setmTitleList( tabTexts );
		runOnUiThread( () -> {
			viewPager.setAdapter( mAdapter );
			viewPager.setCurrentItem( 0 );
			viewPager.setOffscreenPageLimit( 5 );
			BeanUtils.reflex( tabLayout, () -> BeanUtils.getTabLayoutTextViewToLeftRightMargin( tabLayout, TradingRecordActivity.this ) );
		} );
	}

	@Override
	public void onTabLayoutTextToLeftRight(int margin) {
		ListenerManager.sendOnTabLayoutTextToLeftRightListener( margin );
	}
}
