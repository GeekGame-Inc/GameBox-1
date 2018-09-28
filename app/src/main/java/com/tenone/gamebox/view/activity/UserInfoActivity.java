package com.tenone.gamebox.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.AppBarStateChangeListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.mode.mode.DynamicModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.share.SharePopupWindow;
import com.tenone.gamebox.view.adapter.ManagementAdapter;
import com.tenone.gamebox.view.custom.CircleImageView;
import com.tenone.gamebox.view.custom.CustomQBadgeView;
import com.tenone.gamebox.view.custom.CustomerRatingBar;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.fragment.MineDynamicFragment;
import com.tenone.gamebox.view.fragment.MineFansFragment;
import com.tenone.gamebox.view.fragment.MyAttentionFragment;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;


public class UserInfoActivity extends AppCompatActivity implements HttpResultListener, MineDynamicFragment.OnShareCallback, PlatformActionListener {
	@ViewInject(R.id.id_userInfo_appbar)
	AppBarLayout appBarLayout;
	@ViewInject(R.id.id_userInfo_header)
	CircleImageView headerIv;
	@ViewInject(R.id.id_userInfo_nick)
	TextView nickTv;
	@ViewInject(R.id.id_userInfo_sex)
	ImageView sexIv;
	@ViewInject(R.id.id_userInfo_vip)
	ImageView vipIv;
	@ViewInject(R.id.id_userInfo_ratingBar)
	CustomerRatingBar ratingBar;
	@ViewInject(R.id.id_userInfo_intro)
	TextView introTv;
	@ViewInject(R.id.id_userInfo_toolbar)
	Toolbar toolbar;
	@ViewInject(R.id.id_userInfo_rightTv)
	TextView rightTv;
	@ViewInject(R.id.id_userInfo_tabLayout)
	TabLayout tabLayout;
	@ViewInject(R.id.id_userInfo_viewpager)
	ViewPager viewPager;
	@ViewInject(R.id.id_userInfo_attention)
	TextView attentionTextView;
	@ViewInject(R.id.id_userInfo_title)
	TextView titleTv;
	@ViewInject(R.id.id_userInfo_labelView1)
	View view1;
	@ViewInject(R.id.id_userInfo_labelView2)
	View view2;
	@ViewInject(R.id.id_userInfo_labelView3)
	View view3;

	private ManagementAdapter adapter;
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
	private ArrayList<String> titles = new ArrayList<String>();
	private String uid;
	private boolean isMySelf = false;
	private DriverModel model;
	private CustomQBadgeView badgeView1, badgeView2, badgeView3;
	private int badgeLeftMargin, beforeRight;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		Intent intent = getIntent();
		if (intent.hasExtra( "uid" )) {
			uid = intent.getExtras().getString( "uid" );
		}
		isMySelf = (!TextUtils.isEmpty( uid )) && uid.equals( SpUtil.getUserId() );
		super.onCreate( arg0 );
		setContentView( R.layout.activity_userinfo );
		ViewUtils.inject( this );
		ImmersionBar.with( this ).titleBar( toolbar ).statusBarDarkFont( true ).init();
		initTitle();
		HttpManager.userDesc( HttpType.REFRESH, this, this, uid );
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		if (1 == resultItem.getIntValue( "status" )) {
			ResultItem item = resultItem.getItem( "data" );
			new Thread() {
				@Override
				public void run() {
					setModel( item );
					runOnUiThread( () -> {
						setData( what );
					} );
					super.run();
				}
			}.start();
		} else {
			ToastCustom.makeText( this, resultItem.getString( "msg" ), ToastCustom.LENGTH_SHORT ).show();
		}
	}

	private void setModel(ResultItem item) {
		model = new DriverModel();
		model.setDriverId( uid );
		model.setAttentionNum( item.getString( "follow_counts" ) );
		model.setDriverNum( item.getString( "drive_counts" ) );
		model.setFansNum( item.getString( "fan_counts" ) );
		model.setNick( item.getString( "nick_name" ) );
		model.setSex( item.getString( "sex" ) );
		model.setVip( item.getBooleanValue( "vip", 1 ) );
		model.setHeader( item.getString( "icon_url" ) );
		model.setRegTime( item.getString( "reg_time" ) );
		model.setBirth( item.getString( "birth" ) );
		model.setQq( item.getString( "qq" ) );
		model.setAddress( item.getString( "address" ) );
		model.setIntro( item.getString( "desc" ) );
		model.setUserName( item.getString( "username" ) );
		model.setScore( item.getString( "driver_level" ) );
		model.setEmail( item.getString( "email" ) );
		model.setAttention( item.getIntValue( "is_follow" ) );
	}

	@Override
	public void onError(int what, String error) {
		ToastCustom.makeText( this, error, ToastCustom.LENGTH_SHORT ).show();
	}

	private void initTitle() {
		toolbar.setTitle( "" );
		toolbar.setContentInsetsAbsolute( 0, 0 );
		setSupportActionBar( toolbar );
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled( true );
		}
		toolbar.setNavigationOnClickListener( v -> finish() );
		if (isMySelf) {
			rightTv.setText( getString( R.string.edit ) );
		} else {
			rightTv.setText( getString( R.string.more ) );
		}
		rightTv.setOnClickListener( v -> startActivityForResult( new Intent( UserInfoActivity.this,
				UserMessageActivity.class ).putExtra( "model", model ), 5888 ) );
		appBarLayout.addOnOffsetChangedListener( new AppBarStateChangeListener() {
			@Override
			public void onStateChanged(AppBarLayout appBarLayout, State state) {
				if (state == State.EXPANDED) {
					titleTv.setText( "" );
					rightTv.setTextColor( getResources().getColor( R.color.white ) );
					toolbar.setSelected( false );
				} else if (state == State.COLLAPSED) {
					titleTv.setText( "\u53f8\u673a\u4fe1\u606f" );
					rightTv.setTextColor( getResources().getColor( R.color.gray_69 ) );
					toolbar.setSelected( true );
				} else {
					titleTv.setText( "\u53f8\u673a\u4fe1\u606f" );
					rightTv.setTextColor( getResources().getColor( R.color.gray_69 ) );
					toolbar.setSelected( true );
				}
			}
		} );
	}

	private void setData(int what) {
		if (!BeanUtils.isEmpty( model )) {
			if (HttpType.REFRESH == what) {
				initView();
			}
			ImageLoadUtils.loadNormalImg( headerIv, this, model.getHeader() );
			nickTv.setText( getString( R.string.nick )+": " + model.getNick() );
			ratingBar.setClickable( false );
			String ra = model.getScore();
			if (!TextUtils.isEmpty( ra )) {
				try {
					float f = Float.valueOf( ra ).floatValue();
					ratingBar.setStar( f );
				} catch (NumberFormatException e) {
				}
			} else {
				ratingBar.setStar( 0 );
			}
			if (!TextUtils.isEmpty( model.getIntro() )) {
				introTv.setText( "\u7b80\u4ecb:" + model.getIntro() );
			} else {
				introTv.setVisibility( View.GONE );
			}
			vipIv.setVisibility( model.isVip() ? View.VISIBLE : View.GONE );
			if ("\u672a\u8bbe\u7f6e".equals( model.getSex() )) {
				sexIv.setVisibility( View.GONE );
			} else {
				sexIv.setSelected( !getString( R.string.man ).equals( model.getSex() ) );
			}
			attentionTextView.setText( model.isAttention() == 1 ? "\u53d6\u6d88\u5173\u6ce8" : "\u52a0\u5173\u6ce8" );
		}
	}


	private void initView() {
		new Thread() {
			@Override
			public void run() {
				titles.add( "\u5f00\u8f66\u6570" );
				titles.add( "\u7c89\u4e1d" );
				titles.add( "\u5173\u6ce8" );
				Bundle bundle = new Bundle();
				bundle.putString( "uid", uid );
				MineDynamicFragment mineDynamicFragment = new MineDynamicFragment();
				mineDynamicFragment.setArguments( bundle );
				MineFansFragment mineFansFragment = new MineFansFragment();
				mineFansFragment.setArguments( bundle );
				MyAttentionFragment myAttentionFragment = new MyAttentionFragment();
				myAttentionFragment.setArguments( bundle );
				fragments.add( mineDynamicFragment );
				fragments.add( mineFansFragment );
				fragments.add( myAttentionFragment );
				adapter = new ManagementAdapter( getSupportFragmentManager() );
				adapter.setmTitleList( titles );
				adapter.setArray( fragments );
				mineDynamicFragment.setOnShareCallback( UserInfoActivity.this );
				runOnUiThread( () -> {
					if (!isMySelf) {
						attentionTextView.setVisibility( View.VISIBLE );
						attentionTextView.setOnClickListener( v -> HttpManager.followOrCancel( 2155, UserInfoActivity.this,
								new HttpResultListener() {
									@Override
									public void onSuccess(int what, ResultItem resultItem) {
										if (1 == resultItem.getIntValue( "status" )) {
											ToastCustom.makeText( UserInfoActivity.this,
													"\u64cd\u4f5c\u6210\u529f", ToastCustom.LENGTH_SHORT ).show();
											model.setAttention( model.isAttention() == 1 ? 0 : 1 );
											attentionTextView.setText( model.isAttention() == 1 ? "\u53d6\u6d88\u5173\u6ce8" : "\u52a0\u5173\u6ce8" );
											ListenerManager.sendOnFansChangeListener();
										}
									}

									@Override
									public void onError(int what, String error) {
									}
								}, model.getDriverId(), model.isAttention() == 1 ? 2 : 1 ) );
					}
					viewPager.setAdapter( adapter );
					viewPager.setCurrentItem( 0 );
					viewPager.setOffscreenPageLimit( 3 );
					tabLayout.setupWithViewPager( viewPager );
					BeanUtils.reflex( UserInfoActivity.this, tabLayout, 3 );
					BeanUtils.getTabLayoutTextViewToLeft2( tabLayout, 3, (width, right, position) -> {
						badgeLeftMargin = badgeLeftMargin + width + beforeRight;
						beforeRight = right;
						switch ((position + 1)) {
							case 1:
								runOnUiThread( () -> {
									RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view1.getLayoutParams();
									params.leftMargin = badgeLeftMargin
											+ DisplayMetricsUtils.dipTopx( UserInfoActivity.this, 10 );
									view1.setLayoutParams( params );
								} );
								break;
							case 2:
								runOnUiThread( () -> {
									RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view2.getLayoutParams();
									params.leftMargin = badgeLeftMargin
											+ DisplayMetricsUtils.dipTopx( UserInfoActivity.this, 10 );
									view2.setLayoutParams( params );
								} );
								break;
							case 3:
								runOnUiThread( () -> {
									RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view3.getLayoutParams();
									params.leftMargin = badgeLeftMargin
											+ DisplayMetricsUtils.dipTopx( UserInfoActivity.this, 10 );
									view3.setLayoutParams( params );
								} );
								break;
						}
					} );
					String num1 = model.getDriverNum(), num2 = model.getFansNum(), num3 = model.getAttentionNum();
					if (!TextUtils.isEmpty( num1 ))
						showMsgBadge1( num1 );
					if (!TextUtils.isEmpty( num2 ))
						showMsgBadge2( num2 );
					if (!TextUtils.isEmpty( num3 ))
						showMsgBadge3( num3 );
				} );

				super.run();
			}
		}.start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult( requestCode, resultCode, data );
		if (5888 == requestCode && resultCode == RESULT_OK) {
			HttpManager.userDesc( HttpType.LOADING, this, this, uid );
		}
	}

	private SharePopupWindow sharePopupWindow;

	private String currentDynamicId;

	@Override
	public void onShare(DynamicModel model) {
		currentDynamicId = model.getDynamicModelId();
		sharePopupWindow = null;
		sharePopupWindow = new SharePopupWindow( this, model );
		sharePopupWindow.setPlatformActionListener( this );
		sharePopupWindow.showAtLocation( rightTv, Gravity.BOTTOM, 0, 0 );
	}

	@Override
	public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
		HttpManager.shareDynamics( 15, this, new HttpResultListener() {
			@Override
			public void onSuccess(int what, ResultItem resultItem) {
				Log.i( "share_dynamics", resultItem.getString( "msg" ) );
			}

			@Override
			public void onError(int what, String error) {
				Log.i( "share_dynamics", error );
			}
		}, currentDynamicId );
	}

	@Override
	public void onError(Platform platform, int i, Throwable throwable) {
	}

	@Override
	public void onCancel(Platform platform, int i) {
	}

	private void showMsgBadge1(String text) {
		if (badgeView1 == null) {
			badgeView1 = new CustomQBadgeView( this );
		}
		badgeView1.bindTarget( view1 )
				.setBadgeText( text )
				.setBadgeBackground( getResources().getDrawable( R.drawable.shape_gray_f2 ) )
				.setBadgeTextColor( getResources().getColor( R.color.gray_69 ) )
				.setBadgeGravity( Gravity.START | Gravity.TOP )
				.setGravityOffset( 0, 2, true )
				.setBadgeTextSize( 10, true );
	}

	private void showMsgBadge2(String text) {
		if (badgeView2 == null) {
			badgeView2 = new CustomQBadgeView( this );
		}
		badgeView2.bindTarget( view2 )
				.setBadgeText( text )
				.setBadgeBackground( getResources().getDrawable( R.drawable.shape_gray_f2 ) )
				.setBadgeTextColor( getResources().getColor( R.color.gray_69 ) )
				.setBadgeGravity( Gravity.START | Gravity.TOP )
				.setGravityOffset( 0, 2, true )
				.setBadgeTextSize( 10, true );
	}

	private void showMsgBadge3(String text) {
		if (badgeView3 == null) {
			badgeView3 = new CustomQBadgeView( this );
		}
		badgeView3.bindTarget( view3 )
				.setBadgeText( text )
				.setBadgeBackground( getResources().getDrawable( R.drawable.shape_gray_f2 ) )
				.setBadgeTextColor( getResources().getColor( R.color.gray_69 ) )
				.setBadgeGravity( Gravity.START | Gravity.TOP )
				.setGravityOffset( 0, 2, true )
				.setBadgeTextSize( 10, true );
	}
}
