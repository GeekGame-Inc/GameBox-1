package com.tenone.gamebox.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.sy.h5.SYH5SDK;
import com.sy.sdk.utls.ToastUtls;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.AutoInstallApkThread;
import com.tenone.gamebox.mode.listener.ApkInstallListener;
import com.tenone.gamebox.mode.listener.AppBarStateChangeListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.presenter.AppStatisticsManager;
import com.tenone.gamebox.share.SharePopupWindow;
import com.tenone.gamebox.view.adapter.ManagementAdapter;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.CustomQBadgeView;
import com.tenone.gamebox.view.custom.DownloadProgressBar;
import com.tenone.gamebox.view.custom.IconTopTextBottomView;
import com.tenone.gamebox.view.custom.LableView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.fragment.DetailsCommentFragment;
import com.tenone.gamebox.view.fragment.DetailsGiftFragment;
import com.tenone.gamebox.view.fragment.DetailsOpenFragment;
import com.tenone.gamebox.view.fragment.GameDetailStrategyFragment;
import com.tenone.gamebox.view.fragment.GameDetailsFragment;
import com.tenone.gamebox.view.receiver.DownReceiver;
import com.tenone.gamebox.view.service.DownloadService;
import com.tenone.gamebox.view.utils.ApkUtils;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DatabaseUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.PermissionUtils;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TimeUtils;
import com.tenone.gamebox.view.utils.TrackingUtils;
import com.tenone.gamebox.view.utils.WindowUtils;
import com.tenone.gamebox.view.utils.database.GameDownloadTab;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class NewGameDetailsActivity extends AppCompatActivity implements HttpResultListener, DownReceiver.DownStatusChangeListener,
		ApkInstallListener.InstallListener, PlatformActionListener {
	@ViewInject(R.id.id_game_new_appBar)
	AppBarLayout appBarLayout;
	@ViewInject(R.id.id_game_new_collapsing)
	CollapsingToolbarLayout collapsingToolbarLayout;
	@ViewInject(R.id.id_new_game_toolbar)
	Toolbar toolbar;
	@ViewInject(R.id.id_new_game_tabLayout)
	TabLayout tabLayout;
	@ViewInject(R.id.id_new_game_viewpager)
	ViewPager viewPager;
	@ViewInject(R.id.id_new_game_qq)
	TextView qqTv;
	@ViewInject(R.id.id_new_game_icon)
	ImageView iconIv;
	@ViewInject(R.id.id_new_game_name)
	TextView nameTv;
	@ViewInject(R.id.id_new_game_label)
	LableView lableView;
	@ViewInject(R.id.id_new_game_size)
	TextView sizeTv;
	@ViewInject(R.id.id_new_game_times)
	TextView timesTv;
	@ViewInject(R.id.id_new_game_disLayout)
	LinearLayout disLayout;
	@ViewInject(R.id.id_new_game_dis)
	TextView disTv;
	@ViewInject(R.id.id_new_game_title)
	TextView titleTv;
	@ViewInject(R.id.id_new_game_share)
	ImageView shareIv;
	@ViewInject(R.id.id_new_game_down)
	DownloadProgressBar downloadProgressBar;
	@ViewInject(R.id.id_new_game_sc)
	TextView scTv;
	@ViewInject(R.id.id_new_game_labelView)
	View badgeView;
	@ViewInject(R.id.id_new_game_labelView2)
	View badgeView2;
	@ViewInject(R.id.id_new_game_labelView3)
	View badgeView3;
	@ViewInject(R.id.id_new_game_timeLayout)
	LinearLayout timeLayout;
	@ViewInject(R.id.id_new_game_time)
	TextView timeTv;
	@ViewInject(R.id.id_new_game_start)
	TextView startGameTv;
	@ViewInject(R.id.id_new_game_promptLayout)
	LinearLayout promptLayout;
	@ViewInject(R.id.id_new_game_trading)
	IconTopTextBottomView tradingView;
	@ViewInject(R.id.id_new_game_topLayout)
	RelativeLayout topLayout;
	@ViewInject(R.id.id_new_game_topTv)
	TextView topTv;
	@ViewInject(R.id.id_new_game_header)
	LinearLayout linearLayout;
	@ViewInject(R.id.id_new_game_operate)
	ImageView operateIv;
	@ViewInject(R.id.id_new_game_player)
	TextView playerTv;
	@ViewInject(R.id.id_new_game_questionNums)
	TextView questionNumTv;

	private ManagementAdapter mAdapter;
	private List<String> title = new ArrayList<String>();
	private GameDetailsFragment detailsFragment;
	private DetailsCommentFragment detailsCommentFragment;
	private DetailsGiftFragment detailsGiftFragment;
	private DetailsOpenFragment detailsOpenFragment;
	private GameDetailStrategyFragment gameDetailStrategyFragment;
	private List<Fragment> fragments;
	private GameModel gameModel;
	private String gameId, qqUrl;
	private Context mContext;
	private ApkInstallListener installListener;
	private DownReceiver receiver;
	private SharePopupWindow sharePopupWindow;
	private AlertDialog progressDialog;
	private CustomQBadgeView commentBadge, strategyBadge, giftBadge;
	private int newGame = 0, badgeLeftMargin = 0, platform;
	private String newGameTime = "0";
	private boolean isH5 = false;
	private String gameAppId = "", gameAppKey = "", gameH5Url = "";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		mContext = this;
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_new_game_details );
		ViewUtils.inject( this );
		ImmersionBar.with( this ).titleBar( toolbar ).statusBarDarkFont( true ).init();
		initTitle();
		gameId = getIntent().getExtras().getString( "id" );
		buildProgressDialog();
		requestHttp();
		initListener();
	}

/*	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent( intent );
		setIntent( intent );
		initTitle();
		gameId = getIntent().getExtras().getString( "id" );
		buildProgressDialog();
		requestHttp();
	}*/

	private void initListener() {
		new Thread() {
			@Override
			public void run() {
				receiver = new DownReceiver();
				registerDownReceiver( mContext, NewGameDetailsActivity.this, receiver );
				installListener = new ApkInstallListener();
				registerInstallReceiver( mContext, NewGameDetailsActivity.this, installListener );
				super.run();
			}
		}.start();
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
		shareIv.setOnClickListener( v -> {
			if (!BeanUtils.isLogin()) {
				startActivityForResult( new Intent( this, LoginActivity.class ), 2006 );
				return;
			}
			String url = MyApplication.getHttpUrl().getFrendRecom() + "?c="
					+ MyApplication.getConfigModle().getChannelID() + "&u="
					+ SpUtil.getUserId();
			showPopuwindow( url );
		} );
		appBarLayout.addOnOffsetChangedListener( new AppBarStateChangeListener() {
			@Override
			public void onStateChanged(AppBarLayout appBarLayout, State state) {
				if (state == State.EXPANDED) {
					titleTv.setText( "" );
					shareIv.setSelected( false );
					toolbar.setSelected( false );
				} else if (state == State.COLLAPSED) {
					titleTv.setText( gameModel != null ? gameModel.getName() : "\u6e38\u620f\u8be6\u60c5" );
					shareIv.setSelected( true );
					toolbar.setSelected( true );
				} else {
					titleTv.setText( gameModel != null ? gameModel.getName() : "\u6e38\u620f\u8be6\u60c5" );
					shareIv.setSelected( true );
					toolbar.setSelected( true );
				}
			}
		} );
		tabLayout.setupWithViewPager( viewPager );
		gameModel = new GameModel();
	}

	private void requestHttp() {
		String url = MyApplication.getHttpUrl().getGameInfo();
		RequestBody requestBody = new FormBody.Builder()
				.add( "channel", MyApplication.getConfigModle().getChannelID() )
				.add( "system", "1" )
				.add( "gid", gameId )
				.add( "username", SpUtil.getAccount() ).build();
		HttpUtils.postHttp( mContext, 0, url, requestBody, this );
	}

	private void collect(int what, int type) {
		buildProgressDialog();
		String url = MyApplication.getHttpUrl().getGameCollect();
		RequestBody requestBody = new FormBody.Builder().add( "type", type + "" )
				.add( "gid", gameId ).add( "username", SpUtil.getAccount() )
				.build();
		HttpUtils.postHttp( mContext, what, url, requestBody, new HttpResultListener() {
			@Override
			public void onSuccess(int what, ResultItem resultItem) {
				cancelProgressDialog();
				if ("0".equals( resultItem.getString( "status" ) )) {
					gameModel.setCollectde( !gameModel.isCollectde() );
					scTv.setSelected( gameModel.isCollectde() );
					String str = gameModel.isCollectde() ?
							getString( R.string.collect_success ) : getString( R.string.cancle_success );
					showToast( str );
					scTv.setText( gameModel.isCollectde() ? getString( R.string.cancle ) : getString( R.string.collect ) );
				} else {
					showToast( resultItem.getString( "msg" ) );
				}
			}

			@Override
			public void onError(int what, String error) {
				cancelProgressDialog();
				showToast( error );
			}
		} );
	}

	private void initFragments(ResultItem resultItem) {
		initView();
		new Thread() {
			@Override
			public void run() {
				detailsFragment = new GameDetailsFragment();
				detailsFragment.setAction( getIntent().getAction() );
				detailsCommentFragment = new DetailsCommentFragment();
				detailsGiftFragment = new DetailsGiftFragment();
				detailsOpenFragment = new DetailsOpenFragment();
				gameDetailStrategyFragment = new GameDetailStrategyFragment();
				Bundle bundle = new Bundle();
				bundle.putString( "id", gameId );
				detailsCommentFragment.setArguments( bundle );
				detailsGiftFragment.setArguments( bundle );
				gameDetailStrategyFragment.setArguments( bundle );
				bundle.putSerializable( "gameModel", gameModel );
				detailsOpenFragment.setArguments( bundle );
				bundle.putSerializable( "resultItem", resultItem );
				detailsFragment.setArguments( bundle );
				if (fragments == null) {
					fragments = new ArrayList<Fragment>();
					fragments.add( detailsFragment );
					fragments.add( detailsCommentFragment );
					fragments.add( detailsGiftFragment );
					fragments.add( detailsOpenFragment );
					fragments.add( gameDetailStrategyFragment );
				}
				setTitles();
				mAdapter = new ManagementAdapter( getSupportFragmentManager() );
				mAdapter.setmTitleList( title );
				mAdapter.setArray( fragments );
				runOnUiThread( () -> {
					initViewPager();
				} );
				super.run();
			}
		}.start();
	}

	private void initView() {
		ImageLoadUtils.loadNormalImg( iconIv, mContext, gameModel.getImgUrl() );
		nameTv.setText( gameModel.getName() );
		lableView.addLable( gameModel.getLabelArray() );
		sizeTv.setText( gameModel.getSize() + "M" );
		timesTv.setText( gameModel.getTimes() + "\u4e0b\u8f7d" );
		String dis = gameModel.getDis();
		if (TextUtils.isEmpty( dis ) || "0".equals( dis )) {
			disLayout.setVisibility( View.GONE );
		} else {
			disTv.setText( dis + "\u6298" );
		}
		if (!TextUtils.isEmpty( qqUrl )) {
			qqTv.setVisibility( View.VISIBLE );
			qqTv.setOnClickListener( v -> {
				Intent intent = new Intent();
				intent.setData( Uri.parse( qqUrl ) );
				intent.setAction( Intent.ACTION_VIEW );
				mContext.startActivity( intent );
			} );
		}
		if (newGame != 0) {
			timeLayout.setVisibility( View.VISIBLE );
			String text = newGame == 1 ? "\u516c\u6d4b\u65f6\u95f4:" : "\u4e0a\u7ebf\u65f6\u95f4:";
			String text2 = "";
			if (!TextUtils.isEmpty( newGameTime )) {
				long t = Long.valueOf( newGameTime ).longValue() * 1000;
				if (t > 0) {
					text2 = TimeUtils.formatDateMin( t );
				} else {
					text2 = "\u656c\u8bf7\u671f\u5f85";
				}
			}
			timeTv.setText( text + text2 );
		}
		new Thread() {
			@Override
			public void run() {
				Spanned playerTxt = getPlayerTxt( gameModel.getPlayers() );
				Spanned questionTxt = getQuestionTxt( gameModel.getQuestions(), gameModel.getAnswers() );
				runOnUiThread( () -> {
					playerTv.setText( playerTxt );
					questionNumTv.setText( questionTxt );
				} );
				super.run();
			}
		}.start();
	}

	private Spanned getPlayerTxt(int player) {
		String text = getString( R.string.player_txt, player + "" );
		return Html.fromHtml( text );
	}

	private Spanned getQuestionTxt(int question, int answer) {
		String text = getString( R.string.question_txt, question + "", answer + "" );
		return Html.fromHtml( text );
	}

	int cycleIndex = 0;

	private void initViewPager() {
		viewPager.setAdapter( mAdapter );
		viewPager.setOffscreenPageLimit( 1 );
		viewPager.setCurrentItem( ("mine".equals( getIntent().getAction() )) ? 1 : 0 );
		BeanUtils.reflex( tabLayout, () ->
				BeanUtils.getTabLayoutTextViewToLeft( tabLayout, 5, (width, rightWidth, isEnd) -> {
					cycleIndex++;
					badgeLeftMargin += width;
					switch (cycleIndex) {
						case 2:
							runOnUiThread( () -> {
								RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) badgeView.getLayoutParams();
								params.leftMargin = (badgeLeftMargin - rightWidth);
								badgeView.setLayoutParams( params );
							} );
							break;
						case 3:
							runOnUiThread( () -> {
								RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) badgeView2.getLayoutParams();
								params.leftMargin = (badgeLeftMargin - rightWidth);
								badgeView2.setLayoutParams( params );
							} );
							break;
						case 5:
							runOnUiThread( () -> {
								RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) badgeView3.getLayoutParams();
								params.leftMargin = badgeLeftMargin;
								badgeView3.setLayoutParams( params );
							} );
							break;
					}
				} )
		);
	}


	private void showMsgBadge(String text) {
		if (commentBadge == null) {
			commentBadge = new CustomQBadgeView( this );
			commentBadge.bindTarget( badgeView )
					.setBadgeBackground( getResources().getDrawable( R.drawable.shape_gray_f2 ) )
					.setBadgeTextColor( getResources().getColor( R.color.gray_69 ) )
					.setBadgeGravity( Gravity.START | Gravity.TOP )
					.setGravityOffset( 0, 2, true )
					.setBadgeTextSize( 10, true );
		}
		commentBadge.setBadgeText( text );
	}

	private void showStrategyBadge(String text) {
		if (strategyBadge == null) {
			strategyBadge = new CustomQBadgeView( this );
			strategyBadge.bindTarget( badgeView3 )
					.setBadgeBackground( getResources().getDrawable( R.drawable.shape_gray_f2 ) )
					.setBadgeTextColor( getResources().getColor( R.color.gray_69 ) )
					.setBadgeGravity( Gravity.START | Gravity.TOP )
					.setGravityOffset( 0, 2, true )
					.setBadgeTextSize( 10, true );
		}
		strategyBadge.setBadgeText( text );
	}

	private void showGiftBadge(String text) {
		if (giftBadge == null) {
			giftBadge = new CustomQBadgeView( this );
			giftBadge.bindTarget( badgeView2 )
					.setBadgeBackground( getResources().getDrawable( R.drawable.shape_gray_f2 ) )
					.setBadgeTextColor( getResources().getColor( R.color.gray_69 ) )
					.setBadgeGravity( Gravity.START | Gravity.TOP )
					.setGravityOffset( 0, 2, true )
					.setBadgeTextSize( 10, true );
		}
		giftBadge.setBadgeText( text );
	}

	private void setTitles() {
		String[] array = getResources().getStringArray( R.array.game_details_titles );
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				String string = array[i];
				title.add( string );
			}
		}
	}

	protected void registerDownReceiver(Context mContext,
																			DownReceiver.DownStatusChangeListener listener, DownReceiver receiver) {
		IntentFilter filter = new IntentFilter();
		filter.addAction( com.tenone.gamebox.view.base.Configuration.loadFilter );
		filter.addAction( com.tenone.gamebox.view.base.Configuration.pasueFilter );
		filter.addAction( com.tenone.gamebox.view.base.Configuration.completedFilter );
		filter.addAction( com.tenone.gamebox.view.base.Configuration.deleteFilter );
		mContext.registerReceiver( receiver, filter );
		receiver.setChangeListener( listener );
	}

	protected void registerInstallReceiver(Context mContext,
																				 ApkInstallListener.InstallListener listener, ApkInstallListener installListener) {
		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_MEDIA_MOUNTED );
		intentFilter.addAction( Intent.ACTION_PACKAGE_ADDED );
		intentFilter.addAction( Intent.ACTION_PACKAGE_REMOVED );
		intentFilter.addAction( Intent.ACTION_PACKAGE_REPLACED );
		intentFilter.addDataScheme( "package" );
		mContext.registerReceiver( installListener, intentFilter );
		installListener.setInstallListener( listener );
	}

	protected void sendBroadcast(String action, GameModel gameModel, Context cxt) {
		Intent intent = new Intent();
		intent.setAction( action );
		intent.putExtra( "data", gameModel );
		if (Build.VERSION.SDK_INT >= 24) {
			cxt.sendBroadcast( intent,
					"com.tenone.gamebox.broadcast.permission" );
		} else {
			cxt.sendBroadcast( intent );
		}
	}

	protected boolean requestStoragePermission(Activity activity) {
		try {
			if (PermissionUtils.checkSelfPermission( activity,
					PermissionUtils.PERMISSION_WRITE_EXTERNAL_STORAGE )) {
				PermissionUtils.requestPermission( activity,
						PermissionUtils.PERMISSION_WRITE_EXTERNAL_STORAGE, 1 );
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	protected void openDownService(Context mContext, GameModel gameModel) {
		Intent intent = new Intent( mContext, DownloadService.class );
		intent.putExtra( "gameModel", gameModel );
		mContext.startService( intent );
	}

	protected void sendDownloadActionBroadcast(Context mContext) {
		Intent intent = new Intent();
		intent.setAction( "download_action" );
		if (Build.VERSION.SDK_INT >= 24) {
			mContext.sendBroadcast( intent,
					"com.tenone.gamebox.broadcast.permission" );
		} else {
			mContext.sendBroadcast( intent );
		}
	}

	protected void showToast(String text) {
		ToastCustom.makeText( this, text, ToastCustom.LENGTH_SHORT ).show();
	}


	private void showPopuwindow(String url) {
		if (sharePopupWindow == null) {
			sharePopupWindow = new SharePopupWindow( this, url );
		}
		sharePopupWindow.showAtLocation( tabLayout, Gravity.BOTTOM, 0, 0 );
		sharePopupWindow.setPlatformActionListener( this );
	}

	protected void buildProgressDialog() {
		try {
			if (progressDialog == null) {
				progressDialog = new AlertDialog.Builder( this, R.style.loadingStyle ).show();
			}
			progressDialog.setContentView( R.layout.loading_progress );
			progressDialog.setCancelable( true );
			progressDialog.setCanceledOnTouchOutside( false );
			TextView msg = progressDialog
					.findViewById( R.id.id_tv_loadingmsg );
			msg.setText( getResources().getString( R.string.loading ) + "..." );
			progressDialog.show();
		} catch (Exception e) {
		}
	}

	protected void cancelProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		ImmersionBar.with( this ).destroy();
		if (installListener != null) {
			unregisterReceiver( installListener );
		}
		if (receiver != null) {
			unregisterReceiver( receiver );
		}
		super.onDestroy();
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		if (0 == resultItem.getIntValue( "status" )) {
			switch (what) {
				case HttpType.REFRESH:
					ResultItem data = resultItem.getItem( "data" );
					setGameModel( data );
					initFragments( data );
					break;
			}
		} else {
			ToastCustom.makeText( mContext, resultItem.getString( "msg" ), ToastCustom.LENGTH_SHORT ).show();
		}
	}

	@Override
	public void onError(int what, String error) {
		cancelProgressDialog();
		ToastCustom.makeText( mContext, error, ToastCustom.LENGTH_SHORT ).show();
	}

	private void setGameModel(ResultItem resultItem) {
		ResultItem item = resultItem.getItem( "gameinfo" );
		platform = item.getIntValue( "platform" );
		isH5 = 3 == platform;
		startGameTv.setVisibility( isH5 ? View.VISIBLE : View.GONE );
		downloadProgressBar.setVisibility( isH5 ? View.GONE : View.VISIBLE );
		gameAppId = item.getString( "appid" );
		gameAppKey = item.getString( "app_clientkey" );
		gameH5Url = item.getString( "h5_url" );
		newGame = item.getIntValue( "newgame" );
		newGameTime = item.getString( "newgame_time" );
		qqUrl = item.getString( "qq_group" );
		Log.i( "Box", "qqUrl is " + qqUrl );
		gameModel.setGameTag( item.getString( "tag" ) );
		gameModel.setName( item.getString( "gamename" ) );
		int operate = item.getIntValue( "operate" );
		gameModel.setOperate( operate );
		operateIv.setVisibility( operate > 0 ? View.VISIBLE : View.GONE );
		operateIv.setSelected( 2 == operate );
		linearLayout.setSelected( 2 == operate );
		String id = item.getString( "id" );
		if (!TextUtils.isEmpty( id )) {
			gameModel.setGameId( Integer.valueOf( id ) );
		}
		gameModel.setDis( item.getString( "discount" ) );
		gameModel.setImgUrl( MyApplication.getHttpUrl().getBaseUrl()
				+ item.getString( "logo" ) );
		gameModel.setSize( item.getString( "size" ) );
		gameModel.setUrl( item.getString( "android_url" ) );
		gameModel.setVersionsName( item.getString( "version" ) );
		gameModel.setPackgeName( item.getString( "android_pack" ) );
		String grade = item.getString( "score" );
		float g = 0;
		if (!TextUtils.isEmpty( grade )) {
			g = Float.valueOf( grade ).floatValue();
		}
		gameModel.setGrade( g );
		gameModel.setCollectde( "1".equals( item.getString( "collect" ) ) );
		String downTimes = item.getString( "download" );
		if (!TextUtils.isEmpty( downTimes )) {
			int download = Integer.valueOf( downTimes ).intValue();
			if (download > 10000) {
				download = download / 10000;
				gameModel.setTimes( download + "\u4e07+" );
			} else {
				gameModel.setTimes( download + "" );
			}
		} else {
			gameModel.setTimes( "0" );
		}
		String str = item.getString( "types" );
		if (!TextUtils.isEmpty( str )) {
			String[] array = str.split( " " );
			gameModel.setLabelArray( array );
		}
		String where = GameDownloadTab.GAMEID + "=? AND "
				+ GameDownloadTab.GAMENAME + "=?";
		GameModel model = DatabaseUtils.getInstanse( mContext ).getGameModel(
				where,
				new String[]{(gameModel.getGameId() + ""),
						gameModel.getName()} );
		gameModel.setApkName( model.getApkName() );
		gameModel.setStatus( model.getStatus() );
		gameModel.setProgress( model.getProgress() );
		ApkUtils.inspectApk( mContext, gameModel );
		scTv.setSelected( gameModel.isCollectde() );
		scTv.setSelected( gameModel.isCollectde() );
		scTv.setText( gameModel.isCollectde() ? getString( R.string.cancle ) : getString( R.string.collect ) );
		if (item.getIntValue( "trade_open" ) == 1) {
			int products = item.getIntValue( "products" );
			tradingView.setBadgeNum( products );
			tradingView.setOnClickListener( v -> {
				if (products == 0) {
					ToastUtls.getInstance().showToast( mContext, "\u76ee\u524d\u65e0\u4eba\u5356\u6b64\u6e38\u620f\u8d26\u53f7" );
				} else if (!BeanUtils.isEmpty( gameModel )) {
					startActivity( new Intent( mContext, TradingActivity.class )
							.putExtra( "gameName", gameModel.getName() ) );
				}
			} );
		} else {
			tradingView.setVisibility( View.GONE );
		}
		int top = item.getIntValue( "top" );
		if (top > 0) {
			topLayout.setVisibility( View.VISIBLE );
			topTv.setText( "top" + top );
		}
		int commentCount = item.getIntValue( "comment_counts" );
		showMsgBadge( commentCount + "" );
		int giftCount = item.getIntValue( "pack_counts" );
		showGiftBadge( giftCount + "" );
		int articleCounts = item.getIntValue( "article_counts" );
		showStrategyBadge( articleCounts + "" );
		downloadProgressBar.reset();
		downloadProgressBar.setStae( gameModel.getStatus() );
		gameModel.setPlayers( item.getIntValue( "player" ) );
		gameModel.setQuestions( item.getIntValue( "question" ) );
		gameModel.setAnswers( item.getIntValue( "answer" ) );
		cancelProgressDialog();
	}


	@OnClick({R.id.id_new_game_down, R.id.id_new_game_sc, R.id.id_new_game_pl, R.id.id_new_game_start
			, R.id.id_new_game_promptClose, R.id.id_new_game_topLayout, R.id.id_new_game_consult})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.id_new_game_down:
				if (gameModel != null) {
					String prefix = 1 == platform ? AppStatisticsManager.BT_PREFIX :
							2 == platform ? AppStatisticsManager.DISCOUNT_PREFIX :
									AppStatisticsManager.H5_PREFIX;
					String action = prefix + AppStatisticsManager.DOWNLOAD_PREFIX + gameModel.getName();
					AppStatisticsManager.addStatistics( action );
					downloadGame();
				}
				break;
			case R.id.id_new_game_sc:
				if (gameModel != null) {
					if (!BeanUtils.isLogin()) {
						startActivity( new Intent( mContext,
								LoginActivity.class ) );
						break;
					}
					collect( 12, gameModel.isCollectde() ? 2 : 1 );
				}
				break;
			case R.id.id_new_game_pl:
				toComment();
				break;
			case R.id.id_new_game_start:
				startGame();
				break;
			case R.id.id_new_game_promptClose:
				promptLayout.setVisibility( View.GONE );
				break;
			case R.id.id_new_game_topLayout:
				startActivity( new Intent( mContext, GameTopActivity.class )
						.putExtra( "platform", platform ) );
				break;
			case R.id.id_new_game_consult:
				startActivity( new Intent( mContext, QuestionsAndAnswerActivity.class )
						.putExtra( "gameModel", gameModel ) );
				break;
		}
	}

	private void startGame() {
		if (!BeanUtils.isLogin()) {
			startActivity( new Intent( mContext, LoginActivity.class ) );
			return;
		}
		if (!TextUtils.isEmpty( gameAppId )) {
			buildProgressDialog();
			HttpManager.sdkLogin( 1, mContext, new HttpResultListener() {
				@Override
				public void onSuccess(int what, ResultItem resultItem) {
					cancelProgressDialog();
					if (1 == resultItem.getIntValue( "status" )) {
						ResultItem item = resultItem.getItem( "data" );
						if (item != null) {
							Bundle bundle = new Bundle();
							bundle.putString( "userId", item.getString( "id" ) );
							bundle.putString( "userName", item.getString( "username" ) );
							bundle.putString( "token", item.getString( "token" ) );
							bundle.putString( "gameUrl", gameH5Url );
							Log.i( "185SYBox", "h5 url is " + gameH5Url );
							bundle.putString( "appKey", gameAppKey );
							bundle.putString( "appId", gameAppId );
							bundle.putInt( "platformCoin", item.getIntValue( "platform_money" ) );
							bundle.putBoolean( "questionContractEnabled",
									item.getBooleanValue( "question_contract_enabled", 1 ) );
							SYH5SDK.getInstance( mContext ).startH5Activity( bundle );
						}
					} else {
						ToastUtls.getInstance().showToast( mContext, resultItem.getString( "msg" ) );
					}
				}

				@Override
				public void onError(int what, String error) {
					cancelProgressDialog();
					ToastUtls.getInstance().showToast( mContext, error );
				}
			}, SpUtil.getAccount(), SpUtil.getPwd(), gameAppId, gameAppKey );
		}
	}

	private void toComment() {
		if (!BeanUtils.isLogin()) {
			startActivity( new Intent( NewGameDetailsActivity.this, LoginActivity.class ) );
			return;
		}
		HttpManager.userLoginApp( 0, this, new HttpResultListener() {
			@Override
			public void onSuccess(int what, ResultItem resultItem) {
				cancelProgressDialog();
				if (1 == resultItem.getIntValue( "status" )) {
					startActivity(
							new Intent( NewGameDetailsActivity.this, PublishGameCommentActivity.class )
									.putExtra( "gameId", gameId ) );
				} else {
					showToast( resultItem.getString( "msg" ) );
				}
			}

			@Override
			public void onError(int what, String error) {
				cancelProgressDialog();
				showToast( error );
			}
		}, gameId );
	}

	private void downloadGame() {
		if (gameModel != null) {
			int status = gameModel.getStatus();
			switch (status) {
				case GameStatus.UNLOAD:
					startDownloadGame();
					break;
				case GameStatus.LOADING:
					gameModel.setStatus( GameStatus.PAUSEING );
					openDownService( mContext, gameModel );
					break;
				case GameStatus.PAUSEING:
					gameModel.setStatus( GameStatus.LOADING );
					openDownService( mContext, gameModel );
					break;
				case GameStatus.COMPLETED:
					String apkName = gameModel.getApkName();
					ApkUtils.installApp( apkName, mContext );
					break;
				case GameStatus.INSTALLING:
					showToast( getString( R.string.instaling_txt ) );
					break;
				case GameStatus.INSTALLCOMPLETED:
					ApkUtils.doStartApplicationWithPackageName(
							gameModel.getPackgeName(), mContext );
					break;
				case GameStatus.DELETE:
					gameModel.setStatus( GameStatus.LOADING );
					openDownService( mContext, gameModel );
				case GameStatus.UNINSTALLING:
					ApkUtils.installApp( gameModel.getApkName(), mContext );
					break;
			}
			return;
		}
	}

	private void startDownloadGame() {
		if (requestStoragePermission( this )) {
			if (!TextUtils.isEmpty( gameModel.getUrl() )) {
				gameModel.setStatus( GameStatus.LOADING );
				openDownService( mContext, gameModel );
				WindowUtils.showAddDownloadWindow( mContext,
						tabLayout, 1500, getString( R.string.already_add_downlaod_list ) );
				sendDownloadActionBroadcast( mContext );
				Map<String, Object> map = new HashMap<String, Object>();
				map.put( TrackingUtils.USERNAMEKEY, SpUtil.getAccount() );
				map.put( TrackingUtils.NICKNAMEKEY, SpUtil.getNick() );
				map.put( TrackingUtils.MOBILEKEY, SpUtil.getPhone() );
				map.put( TrackingUtils.GAMENAME, gameModel.getName() );
				TrackingUtils.setEvent( TrackingUtils.DOWNLOADEVENT, map );
			} else {
				showToast( mContext.getResources().getString( R.string.no_download_path ) );
			}
		}
	}

	@Override
	public void onDownStatusChange(GameModel model) {
		if (model != null && gameModel != null) {
			if (model.getGameId() == gameModel.getGameId()) {
				gameModel.setProgress( model.getProgress() );
				gameModel.setStatus( model.getStatus() );
				gameModel.setUrl( model.getUrl() );
				switch (model.getStatus()) {
					case GameStatus.UNLOAD:
						model.setProgress( 0 );
						break;
					case GameStatus.COMPLETED:
						if (SpUtil.getAutoInstall()) {
							new AutoInstallApkThread( mContext, model.getApkName() ).start();
						} else {
							model.setStatus( GameStatus.UNINSTALLING );
						}
						break;
				}
				gameModel.setType( 1 );
				downloadProgressBar.reset();
				downloadProgressBar.setStae( model.getStatus() );
				downloadProgressBar.setProgress( model.getProgress() );
			}
		}
	}

	@Override
	public void installed(String packgeName, int status) {
		if (gameModel != null) {
			if (packgeName.equals( gameModel.getPackgeName() )) {
				gameModel.setStatus( status );
				sendBroadcast( Configuration.completedFilter, gameModel,
						mContext );
			}
		}
	}

	@Override
	public void unInstall(String packgeName, int status) {
		if (packgeName.equals( gameModel.getPackgeName() )) {
			gameModel.setStatus( status );
			sendBroadcast( Configuration.deleteFilter, gameModel, mContext );
		}
	}

	@Override
	public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
		showToast( getString( R.string.share_success ) );
	}

	@Override
	public void onError(Platform platform, int i, Throwable throwable) {
		showToast( getString( R.string.share_error ) );
	}

	@Override
	public void onCancel(Platform platform, int i) {
		showToast( getString( R.string.share_cancle ) );
	}

}
