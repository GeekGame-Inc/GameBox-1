package com.tenone.gamebox.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.DrivingBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.DrivingView;
import com.tenone.gamebox.view.activity.ThroughDynamicsActivity;
import com.tenone.gamebox.view.adapter.ManagementAdapter;
import com.tenone.gamebox.view.custom.BadgeView;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;

import java.util.List;


public class DrivingPresenter extends BasePresenter implements TabPageIndicator.OnPageSelectedListener {
	private Context mContext;
	private DrivingView view;
	private DrivingBiz biz;

	ManagementAdapter mAdapter;
	private BadgeView badgeView;
	private static int messageType = 1;

	private static DrivingPresenter instance;

	public static DrivingPresenter getInstance() {
		return instance;
	}

	public DrivingPresenter(Context mContext, DrivingView view) {
		this.mContext = mContext;
		this.view = view;
		this.biz = new DrivingBiz();
		instance = this;
	}

	public void initView() {
		initTitle();
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pointTv().getLayoutParams();
		params.rightMargin = DisplayMetricsUtils.dipTopx( mContext, 7 );
		params.topMargin = DisplayMetricsUtils.dipTopx( mContext, 2 );
		pointTv().setLayoutParams( params );
	}

	
	public void setAdapter(FragmentManager manager) {
		new Thread() {
			@Override
			public void run() {
				if (mAdapter == null) {
					mAdapter = new ManagementAdapter( manager );
				}
				mAdapter.setArray( getFragments() );
				mAdapter.setmTitleList( getFragmentTitles() );
				viewPager().post( () -> {
					viewPager().setAdapter( mAdapter );
					viewPager().setCurrentItem( 0 );
					tabLayout().setupWithViewPager( viewPager() );
					BeanUtils.reflex( tabLayout(), () -> {
					} );
				} );
				super.run();
			}
		}.start();
	}

	private void initTitle() {
		titleBar().setTitleText( R.string.driving );
		titleBar().setRightText( "\u7a7f\u8d8a" );
		titleBar().setOnClickListener( R.id.id_titleBar_rightText,
				v -> openOtherActivity( mContext, new Intent( mContext, ThroughDynamicsActivity.class ) ) );
	}

	private TitleBarView titleBar() {
		return view.titleBar();
	}

	private TabLayout tabLayout() {
		return view.getTabLayout();
	}

	private ViewPager viewPager() {
		return view.viewPager();
	}

	private TextView pointTv() {
		return view.pointTv();
	}

	private List<Fragment> getFragments() {
		return biz.getFragments();
	}

	private List<String> getFragmentTitles() {
		return biz.getFragmentTitles( mContext, R.array.driving_titles );
	}

	private void addBadgView(String text) {
		if (badgeView == null) {
			badgeView = new BadgeView( mContext, pointTv() );
			badgeView.setHeight( DisplayMetricsUtils.dipTopx( mContext, 15 ) );
			badgeView.setTextSize( TypedValue.COMPLEX_UNIT_SP, 10 );
			badgeView.setTextColor( ContextCompat.getColor( mContext, R.color.white ) );
			badgeView.setGravity( Gravity.TOP | Gravity.RIGHT );
			if (!badgeView.isShown())
				badgeView.show();
		}
		badgeView.setText( text );
		if (!badgeView.isShown()) {
			badgeView.show();
		}
	}

	private void hideBadgView() {
		if (badgeView != null && badgeView.isShown()) {
			badgeView.hide();
		}
	}

	@Override
	public void onPageSelected(int arg0) {
		if (arg0 == 4) {
			hideBadgView();
		}
		ListenerManager.sendregisterOnDynamicTabChangeListener();
	}

	public void onResume() {
		if (BeanUtils.isLogin()) {
			getMessageNum();
		}
	}

	private void getMessageNum() {
		HttpManager.userNewUp( HttpType.REFRESH, mContext, new HttpResultListener() {
			@Override
			public void onSuccess(int what, ResultItem resultItem) {
				if (1 == resultItem.getIntValue( "status" )) {
					ResultItem item = resultItem.getItem( "data" );
					String count = item.getString( "count" );
					if (!TextUtils.isEmpty( count ) && !"0".equals( count )) {
						addBadgView( count );
					} else {
						hideBadgView();
					}
					messageType = item.getIntValue( "type" );
				}
			}

			@Override
			public void onError(int what, String error) {
			}
		} );
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		DrivingPresenter.messageType = messageType;
	}
}
