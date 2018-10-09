package com.tenone.gamebox.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.StatisticActionEnum;
import com.tenone.gamebox.presenter.AppStatisticsManager;
import com.tenone.gamebox.view.activity.LoginActivity;
import com.tenone.gamebox.view.activity.ManagementActivity;
import com.tenone.gamebox.view.activity.MyMessageActivity;
import com.tenone.gamebox.view.activity.TaskCenterActivity;
import com.tenone.gamebox.view.adapter.GameViewPagerAdapter;
import com.tenone.gamebox.view.base.BaseLazyFragment;
import com.tenone.gamebox.view.base.Constant;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.CustomQBadgeView;
import com.tenone.gamebox.view.receiver.WarnReceiver;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DatabaseUtils;
import com.tenone.gamebox.view.utils.HttpManager;

import java.util.ArrayList;
import java.util.List;

public class NewGameFragment extends BaseLazyFragment implements WarnReceiver.WarnNotifiacticonListener {
	@ViewInject(R.id.id_new_game_viewPager)
	ViewPager viewPager;
	@ViewInject(R.id.id_new_game_tabLayout)
	TabLayout tabLayout;
	@ViewInject(R.id.id_new_game_iv1)
	ImageView msgBadgeIv;
	@ViewInject(R.id.id_new_game_iv2)
	ImageView downBadgeIv;
	@ViewInject(R.id.id_new_game_task)
	ImageView taskIv;
	private GameViewPagerAdapter<Fragment> mAdapter;
	private Context mContext;
	private WarnReceiver receiver;
	private boolean toMessageList = false;
	private List<Fragment> fragments = new ArrayList<Fragment>();
	private BtGameFragment btGameFragment;
	private BoutiqueFragment boutiqueFragment;
	private DiscountGameFragment discountGameFragment;
	private CommitmentFragment commitmentFragment;
	private H5Fragment h5Fragment;
	private CustomQBadgeView msgBadge, downBadge;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate( R.layout.activity_new_game, container, false );
		ViewUtils.inject( this, view );
		mContext = getActivity();
		initView();
		return view;
	}

	private void initView() {
		new Thread() {
			@Override
			public void run() {
				getTitles();
				initFragment();
				setAdapter();
				super.run();
			}
		}.start();
	}


	private void setAdapter() {
		if (mAdapter == null) {
			mAdapter = new GameViewPagerAdapter<Fragment>( getChildFragmentManager() );
		}
		mAdapter.setArray( fragments );
		mAdapter.setmTitleList( getTitles() );
		getActivity().runOnUiThread( () -> {
			viewPager.setAdapter( mAdapter );
			viewPager.setCurrentItem( 0 );
			viewPager.setOffscreenPageLimit( 4 );
			initIndicator();
		} );
	}

	private List<String> getTitles() {
		List<String> titles = new ArrayList<String>();
		String[] array = getResources().getStringArray( R.array.fragment_new_game_title );
		if (array != null) {
			for (String string : array) {
				titles.add( string );
			}
		}
		if (!Constant.isIsShowDiscount()) {
			titles.remove( "\u6298\u6263" );
		}
		if (!Constant.isIsShowBoutique()) {
			titles.remove( "\u7cbe\u54c1" );
		}
		return titles;
	}


	private void initIndicator() {
		tabLayout.setupWithViewPager( viewPager );
		BeanUtils.setIndicatorWidth( tabLayout );
	}


	private void setFlickerAnimation(ImageView imageView) {
		final Animation animation = new AlphaAnimation( 1, 0.5f );
		animation.setDuration( 900 );
		animation.setInterpolator( new LinearInterpolator() );
		animation.setRepeatCount( Animation.INFINITE );
		animation.setRepeatMode( Animation.REVERSE );
		imageView.setAnimation( animation );
	}

	@Override
	public void onPause() {
		super.onPause();
		taskIv.clearAnimation();
	}

	private void initFragment() {
		fragments.clear();
		btGameFragment = new BtGameFragment();
		fragments.add( btGameFragment );
		if (Constant.isIsShowBoutique()) {
			boutiqueFragment = new BoutiqueFragment();
			fragments.add( boutiqueFragment );
		}
		if (Constant.isIsShowDiscount()) {
			discountGameFragment = new DiscountGameFragment();
			fragments.add( discountGameFragment );
		}
		h5Fragment = new H5Fragment();
		fragments.add( h5Fragment );
		commitmentFragment = new CommitmentFragment();
		fragments.add( commitmentFragment );
		receiver = new WarnReceiver();
		registerWarnReceiver( mContext, receiver );
		receiver.setWarnNotifiacticonListener( this );
	}

	@OnClick({R.id.id_new_game_message, R.id.id_new_game_down, R.id.id_new_game_task})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.id_new_game_down:
				startActivity( new Intent( getActivity(), ManagementActivity.class ) );
				break;
			case R.id.id_new_game_message:
				if (BeanUtils.isLogin()) {
					startActivity( new Intent( getActivity(), MyMessageActivity.class )
							.putExtra( "tag", toMessageList ? 1 : 0 ) );
				} else {
					startActivity( new Intent( getActivity(), LoginActivity.class ) );
				}
				break;
			case R.id.id_new_game_task:
				AppStatisticsManager.addStatistics( StatisticActionEnum.INDEX_EARN_COINS );
				if (BeanUtils.isLogin()) {
					startActivity( new Intent( getActivity(), TaskCenterActivity.class ) );
				} else {
					startActivity( new Intent( getActivity(), LoginActivity.class ) );
				}
				break;
		}
	}

	@Override
	public void onLazyLoad() {
		if (BeanUtils.isLogin()) {
			HttpManager.getUnreadCounts( 0, mContext, new HttpResultListener() {
				@Override
				public void onSuccess(int what, ResultItem resultItem) {
					if (1 == resultItem.getIntValue( "status" )) {
						String text = resultItem.getString( "data" );
						if (!TextUtils.isEmpty( text ) && !"0".equals( text )) {
							toMessageList = true;
							showMsgBadge( text );
						} else {
							toMessageList = false;
							hideMsgBadge();
						}
					} else {
						toMessageList = false;
						hideMsgBadge();
					}
				}

				@Override
				public void onError(int what, String error) {
				}
			} );
		}
	}

	private void showMsgBadge(String text) {
		if (msgBadge == null) {
			msgBadge = new CustomQBadgeView( getActivity() );
		}
		msgBadge.bindTarget( msgBadgeIv )
				.setBadgeText( text )
				.setBadgeBackgroundColor( getResources().getColor( R.color.blue_40 ) )
				.setBadgeTextColor( getResources().getColor( R.color.white ) )
				.setBadgeGravity( Gravity.END | Gravity.TOP )
				.setBadgeTextSize( 10, true );
	}

	private void hideMsgBadge() {
		if (msgBadge != null) {
			msgBadge.hide( true );
		}
	}

	private void showDownBadge(String text) {
		if (downBadge == null) {
			downBadge = new CustomQBadgeView( getActivity() );
		}
		downBadge.bindTarget( downBadgeIv )
				.setBadgeText( text )
				.setBadgeBackgroundColor( ContextCompat.getColor( getActivity(), R.color.blue_40 ) )
				.setBadgeTextColor( getResources().getColor( R.color.white ) )
				.setBadgeGravity( Gravity.END | Gravity.TOP )
				.setBadgeTextSize( 7, true );
	}

	boolean isInitDownBadge = false;

	@Override
	public void onResume() {
		super.onResume();
		if (!isInitDownBadge) {
			initDownBadge();
		}
		setFlickerAnimation( taskIv );
	}

	private void initDownBadge() {
		isInitDownBadge = true;
		/* ɨ�����ض��� */
		List<GameModel> gameModels = DatabaseUtils.getInstanse( mContext )
				.getDownloadList();
		int a = 0;
		for (GameModel mode : gameModels) {
			if (mode.getStatus() == GameStatus.LOADING
					|| mode.getStatus() == GameStatus.PAUSEING
					|| mode.getStatus() == GameStatus.COMPLETED) {
				a++;
			}
		}
		if (a > 0) {
			showDownBadge( a + "" );
		} else {
			if (downBadge != null) {
				downBadge.hide( true );
			}
		}
		isInitDownBadge = false;
	}

	@Override
	public void notificaticonFun(boolean b) {
		if (b) {
			showMsgBadge( "" );
		} else {
			hideMsgBadge();
		}
	}


	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver( receiver );
		super.onDestroy();
	}
}
