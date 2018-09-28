/**
 * Project Name:GameBox
 * File Name:GuidanceActivity.java
 * Package Name:com.tenone.gamebox.view.activity
 * Date:2017-5-9����5:52:36
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.view.adapter.GuideAdapter;
import com.tenone.gamebox.view.custom.slidingtutorial.IndicatorOptions;
import com.tenone.gamebox.view.custom.slidingtutorial.TutorialPageIndicator;
import com.tenone.gamebox.view.utils.JrttUtils;
import com.tenone.gamebox.view.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * ����ҳ ClassName:GuidanceActivity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-5-9 ����5:52:36 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */

public class GuidanceActivity extends AppCompatActivity implements OnClickListener, ViewPager.OnPageChangeListener {
	Context context;
	@ViewInject(R.id.viewPager)
	ViewPager viewPager;
	@ViewInject(R.id.indicator)
	TutorialPageIndicator indicator;
	@ViewInject(R.id.tvSkip)
	TextView skipTv;

	private GuideAdapter adapter;
	private List<View> array = new ArrayList<View>();
	private Bundle dataBundle;
	private int[] rIds = {R.drawable.icon_guidance1, R.drawable.icon_guidance2, R.drawable.icon_guidance3};

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		SpUtil.setShowGuide( true );
		getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN );
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		dataBundle = getIntent().getBundleExtra( "bundle" );
		setContentView( R.layout.st_fragment_presentation );
		ViewUtils.inject( this );
		context = this;
		initArray();
		IndicatorOptions.Builder builder = IndicatorOptions.newBuilder( getApplicationContext() );
		builder.setElementSize( 20 );
		indicator.initWith( builder.build(), 3 );
		adapter = new GuideAdapter( array, rIds, this );
		viewPager.setAdapter( adapter );
		viewPager.setCurrentItem( 0 );
		viewPager.setOffscreenPageLimit( 3 );
		viewPager.setOnPageChangeListener( this );
	}

	private void initArray() {
		for (int i = 0; i < 3; i++) {
			ImageView imageView = new ImageView( context );
			imageView.setScaleType( ImageView.ScaleType.FIT_XY );
			array.add( imageView );
		}
	}

	@OnClick({R.id.tvSkip})
	public void onClick(View v) {
		if (v.getId() == R.id.tvSkip) {
			startMain();
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		indicator.onPageScrolled( position % 3,
				positionOffset, true );
	}

	@Override
	public void onPageSelected(int position) {
		if (position == 2) {
			skipTv.setVisibility( View.VISIBLE );
		} else {
			skipTv.setVisibility( View.GONE );
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	private void startMain() {
		Uri uri = getIntent().getData();
		Intent intent = new Intent( this, MainActivity.class );
		if (dataBundle != null) {
			intent.putExtra( "bundle", dataBundle );
		}
		if (uri != null) {
			String dynamicId = uri.getQueryParameter( "dynamicId" );
			if (!TextUtils.isEmpty( dynamicId )) {
				intent.putExtra( "dynamicId", dynamicId );
			}
		}
		startActivity( intent );
		finish();
		overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		return true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (array != null) {
			for (View view : array) {
				view.setBackground( null );
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		JrttUtils.onResume( this );
	}

	@Override
	protected void onPause() {
		super.onPause();
		JrttUtils.onPause( this );
	}
}
