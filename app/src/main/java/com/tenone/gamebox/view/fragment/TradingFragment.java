package com.tenone.gamebox.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnLoadMoreListener;
import com.tenone.gamebox.mode.listener.OnRecyclerViewItemClickListener;
import com.tenone.gamebox.mode.listener.OnTradingLoginStatusListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.TradingModel;
import com.tenone.gamebox.view.activity.SelectAccountActivity;
import com.tenone.gamebox.view.activity.TradingCustomerActivity;
import com.tenone.gamebox.view.activity.TradingLoginActivity;
import com.tenone.gamebox.view.activity.TradingNoticeActivity;
import com.tenone.gamebox.view.activity.TradingProductDetailsActivity;
import com.tenone.gamebox.view.activity.TradingRecordActivity;
import com.tenone.gamebox.view.activity.TradingSearchActivity;
import com.tenone.gamebox.view.adapter.TradingAdapter;
import com.tenone.gamebox.view.base.BaseLazyFragment;
import com.tenone.gamebox.view.custom.CustomerLoadMoreRecyclerView;
import com.tenone.gamebox.view.custom.SpacesItemDecoration;
import com.tenone.gamebox.view.custom.dialog.TradingNotesDialog;
import com.tenone.gamebox.view.custom.popupwindow.TradingConditionsWindow;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.CharSequenceUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.OnScrollHelper;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


public class TradingFragment extends BaseLazyFragment implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, HttpResultListener, OnRecyclerViewItemClickListener<TradingModel>, OnTradingLoginStatusListener {
	@ViewInject(R.id.id_trading_refresh)
	SwipeRefreshLayout refreshLayout;
	@ViewInject(R.id.id_trading_list)
	CustomerLoadMoreRecyclerView listView;
	@ViewInject(R.id.id_trading_appBarLayout)
	AppBarLayout appBarLayout;
	@ViewInject(R.id.id_trading_collapsingToolbarLayout)
	CollapsingToolbarLayout collapsingToolbarLayout;
	@ViewInject(R.id.id_trading_toolbar)
	Toolbar toolbar;
	@ViewInject(R.id.id_trading_conditions)
	TextView conditionsTv;
	@ViewInject(R.id.id_trading_platform)
	TextView platformTv;
	@ViewInject(R.id.id_trading_conditionsLayout)
	RelativeLayout conditionsLayout;
	@ViewInject(R.id.id_trading_game)
	TextView gameNameTv;
	@ViewInject(R.id.id_trading_account)
	TextView accountTv;
	@ViewInject(R.id.id_trading_clear)
	ImageView clearImg;

	private List<TradingModel> models = new ArrayList<TradingModel>();
	private TradingAdapter adapter;
	private boolean isRefresh, isLoading;
	private List<String> conditions = new ArrayList<String>();
	private List<String> platform = new ArrayList<String>();
	private TradingConditionsWindow conditionsWindow, platformWindow;
	private boolean isConditionsShowing = false, isPlatformShowing = false;
	private String gameName = "";
	private int order = 1, orderType = 1, system = 0, page = 1;
	private TradingNotesDialog.TradingNotesBuilder builder;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate( R.layout.fragment_trading, container, false );
		ViewUtils.inject( this, view );
		return view;
	}

	private void initView() {
		appBarLayout.addOnOffsetChangedListener( (appBarLayout, verticalOffset) -> {
		} );
		toolbar.setTitle( "" );
		toolbar.setContentInsetsAbsolute( 0, 0 );
		collapsingToolbarLayout.setTitle( "" );
		adapter = new TradingAdapter( getActivity(), models );
		adapter.setOnRecyclerViewItemClickListener( this );
		LinearLayoutManager manager = new LinearLayoutManager( getActivity(), LinearLayoutManager.VERTICAL, false );
		listView.setLayoutManager( manager );
		listView.addItemDecoration( new SpacesItemDecoration( getActivity(), LinearLayoutManager.HORIZONTAL, 1,
				ContextCompat.getColor( getContext(), R.color.divider) ) );
		listView.setAdapter( adapter );
		listView.setmLoadMoreListener( this );
		refreshLayout.setOnRefreshListener( this );
		initList();
		if (BeanUtils.tradingIsLogin()) {
			accountTv.setText( CharSequenceUtils.getVisibilyPhone( SpUtil.getTradingMobile() ) );
		}
		ListenerManager.registerOnTradingLoginStatusListener( this );
		OnScrollHelper.getInstance().onScrollStateUpdate( listView);
	}

	private void initList() {
		conditions.add( getString( R.string.latest_release ) );
		conditions.add( getString( R.string.lowest_price ) );
		conditions.add( getString( R.string.highest_price ) );
		platform.add( getString( R.string.unlimited ) );
		platform.add( getString( R.string.android ) );
		platform.add( getString( R.string.ios ) );
		platform.add( getString( R.string.double_end ) );
	}

	@OnClick({R.id.id_trading_account, R.id.id_trading_search, R.id.id_trading_conditions, R.id.id_trading_platform,
			R.id.id_trading_xzImg, R.id.id_trading_mhImg, R.id.id_trading_jlImg, R.id.id_trading_kfImg,
			R.id.id_trading_xzTv, R.id.id_trading_mhTv, R.id.id_trading_jlTv, R.id.id_trading_kfTv,
			R.id.id_trading_clear})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.id_trading_account:
				if (BeanUtils.tradingIsLogin()) {
					startActivity( new Intent( getActivity(), SelectAccountActivity.class )
							.setAction( "mine" ) );
				} else {
					startActivity( new Intent( getActivity(), TradingLoginActivity.class ) );
				}
				break;
			case R.id.id_trading_search:
				startActivityForResult( new Intent( getActivity(), TradingSearchActivity.class ), Activity.RESULT_FIRST_USER );
				break;
			case R.id.id_trading_conditions:
				showConditionsWindow();
				break;
			case R.id.id_trading_platform:
				showPlatformWindow();
				break;
			case R.id.id_trading_xzImg:
			case R.id.id_trading_xzTv:
				startActivity( new Intent( getActivity(), TradingNoticeActivity.class ) );
				break;
			case R.id.id_trading_jlImg:
			case R.id.id_trading_jlTv:
				if (BeanUtils.tradingIsLogin()) {
					startActivity( new Intent( getActivity(), TradingRecordActivity.class ) );
				} else {
					startActivity( new Intent( getActivity(), TradingLoginActivity.class ) );
				}
				break;
			case R.id.id_trading_mhImg:
			case R.id.id_trading_mhTv:
				if (BeanUtils.tradingIsLogin()) {
					if (builder == null) {
						builder = new TradingNotesDialog.TradingNotesBuilder( getActivity() );
						builder.setType( 0 );
						builder.setOnAgreeClickListener( () -> {
							startActivity( new Intent( getActivity(), SelectAccountActivity.class )
									.setAction( "sell" ) );
							builder.dismiss();
						} );
					}
					builder.showDialog();
				} else {
					startActivity( new Intent( getActivity(), TradingLoginActivity.class ) );
				}
				break;
			case R.id.id_trading_kfImg:
			case R.id.id_trading_kfTv:
				startActivity( new Intent( getActivity(), TradingCustomerActivity.class ) );
				break;
			case R.id.id_trading_clear:
				gameName = "";
				gameNameTv.setText( getString( R.string.all ) );
				clearImg.setVisibility( View.GONE );
				refreshData();
				break;
		}
	}

	private void showPlatformWindow() {
		if (isPlatformShowing) {
			platformWindow.dismiss();
			platformTv.setSelected( false );
			isPlatformShowing = false;
		} else {
			resetConditionsWindow();
			initPlatformWindow();
		}
	}

	private void initPlatformWindow() {
		if (platformWindow == null) {
			platformWindow = new TradingConditionsWindow( getActivity(), platform, 1 );
			platformWindow.setOnPlatformCallback( platform -> {
				isPlatformShowing = false;
				platformTv.setSelected( false );
				system = this.platform.indexOf( platform );
				platformTv.setText( platform );
				gameName = "";
				gameNameTv.setText( getString( R.string.all ) );
				clearImg.setVisibility( View.GONE );
				refreshData();
			} );
		}
		platformWindow.showAsDropDown( conditionsLayout, 0, 1 );
		platformWindow.setOnDismissListener( () -> {
			platformTv.setSelected( false );
			isPlatformShowing = false;
		} );
		platformTv.setSelected( true );
		isPlatformShowing = true;
	}

	private void resetPlatformWindow() {
		if (platformWindow != null && platformWindow.isShowing()) {
			platformWindow.dismiss();
			platformTv.setSelected( false );
		}
		isPlatformShowing = false;
	}

	private void showConditionsWindow() {
		if (isConditionsShowing) {
			conditionsWindow.dismiss();
			isConditionsShowing = false;
			conditionsTv.setSelected( false );
		} else {
			resetPlatformWindow();
			initConditionsWindow();
		}
	}

	private void initConditionsWindow() {
		if (conditionsWindow == null) {
			conditionsWindow = new TradingConditionsWindow( getActivity(), conditions, 0 );
			conditionsWindow.setOnTradingConditionsCallback( conditions -> {
				order = (this.conditions.indexOf( conditions ) == 0 ? 1 : 2);
				orderType = (this.conditions.indexOf( conditions ) == 0 ? 1 : this.conditions.indexOf( conditions ) == 1 ? 2 : 1);
				conditionsTv.setSelected( false );
				conditionsTv.setText( conditions );
				isConditionsShowing = false;
				refreshData();
			} );
		}
		conditionsWindow.showAsDropDown( conditionsLayout, 0, 1 );
		conditionsWindow.setOnDismissListener( () -> {
			conditionsTv.setSelected( false );
			isConditionsShowing = false;
		} );
		conditionsTv.setSelected( true );
		isConditionsShowing = true;
	}

	private void resetConditionsWindow() {
		if (conditionsWindow != null && conditionsWindow.isShowing()) {
			conditionsWindow.dismiss();
			conditionsTv.setSelected( false );
		}
		isConditionsShowing = false;
	}

	@Override
	public void onLazyLoad() {
		initView();
		refreshData();
	}

	private void refreshData() {
		isRefresh = true;
		refreshLayout.setRefreshing( true );
		HttpManager.productList( HttpType.REFRESH, getActivity(), this, gameName, order, orderType, page, system );
	}

	@Override
	public void onRefresh() {
		if (isLoading) {
			isRefresh = false;
			refreshLayout.setRefreshing( false );
			return;
		}
		isRefresh = true;
		refreshLayout.setRefreshing( true );
		page = 1;
		HttpManager.productList( HttpType.REFRESH, getActivity(), this, gameName, order, orderType, page, system );
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult( requestCode, resultCode, data );
		if (data != null && data.hasExtra( "gameName" )) {
			gameName = data.getStringExtra( "gameName" );
			clearImg.setVisibility( View.VISIBLE );
			gameNameTv.setText( gameName );
			system = 0;
			platformTv.setText( this.platform.get( 0 ) );
			refreshData();
		}
	}

	@Override
	public void onLoadMore() {
		if (isRefresh) {
			isLoading = false;
			return;
		}
		adapter.setLoading( true );
		adapter.notifyDataSetChanged();
		page++;
		HttpManager.productList( HttpType.LOADING, getActivity(), this, gameName, order, orderType, page, system );
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		refreshLayout.setRefreshing( false );
		adapter.setLoading( false );
		if (1 == resultItem.getIntValue( "status" )) {
			if (what == HttpType.REFRESH) {
				models.clear();
			}
			ResultItem data = resultItem.getItem( "data" );
			List<ResultItem> list = data.getItems( "list" );
			if (!BeanUtils.isEmpty( list )) {
				setData( list );
			}
			adapter.notifyDataSetChanged();
		} else {
			ToastUtils.showToast( getActivity(), resultItem.getString( "msg" ) );
		}
	}

	private void setData(List<ResultItem> list) {
		for (ResultItem data : list) {
			TradingModel model = new TradingModel();
			model.setServer( data.getString( "server_name" ) );
			model.setTitle( data.getString( "title" ) );
			model.setId( data.getString( "id" ) );
			model.setPlatform( data.getIntValue( "system" ) );
			model.setPrice( data.getString( "price" ) );
			model.setStartTime( data.getString( "publish_time" ) );
			model.setImgUrl( data.getString( "imgs" ) );
			model.setGameName( data.getString( "game_name" ) );
			models.add( model );
		}
	}

	@Override
	public void onError(int what, String error) {
		refreshLayout.setRefreshing( false );
		adapter.setLoading( false );
		ToastUtils.showToast( getActivity(), error );
	}

	@Override
	public void onRecyclerViewItemClick(TradingModel tradingModel) {
		startActivity( new Intent( getActivity(), TradingProductDetailsActivity.class )
				.putExtra( "productId", tradingModel.getId() ) );
	}

	@Override
	public void onTradingLogin(boolean isLogin) {
		if (isLogin) {
			accountTv.setText( CharSequenceUtils.getVisibilyPhone( SpUtil.getTradingMobile() ) );
		} else {
			accountTv.setText( R.string.login_trading );
		}
	}

	@Override
	public void onDestroy() {
		ListenerManager.unRegisterOnTradingLoginStatusListener( this );
		super.onDestroy();
	}
}
