package com.tenone.gamebox.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.SellAccountModel;
import com.tenone.gamebox.view.adapter.SellAccountAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.CharSequenceUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectAccountActivity extends BaseActivity implements HttpResultListener, SwipeRefreshLayout.OnRefreshListener, SellAccountAdapter.OnStatusClickListener {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_select_refresh)
	SwipeRefreshLayout refreshLayout;
	@ViewInject(R.id.id_select_list)
	ExpandableListView expandableListView;
	@ViewInject(R.id.id_select_bottom)
	LinearLayout bottomLayout;

	private SellAccountAdapter accountAdapter;
	private List<SellAccountModel> models = new ArrayList<SellAccountModel>();
	private TextView alipayTv;
	private boolean isHavaAlipay = false;
	private MaterialDialog.Builder buidler;
	private SellAccountModel currentModel;
	private String alipay;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		setContentView( R.layout.activity_select_account );
		ViewUtils.inject( this );
		initTitle();
		initView();
	}

	private void initTitle() {
		titleBarView.setLeftImg( R.drawable.icon_xqf_b );
		titleBarView.setTitleText( "sell".equals( getIntent().getAction() ) ? getString( R.string.select_account ) :
				getString( R.string.mine_account ) );
		titleBarView.setRightText( getString( R.string.add ) );
		titleBarView.setOnClickListener( R.id.id_titleBar_leftImg, v -> finish() );
		titleBarView.setOnClickListener( R.id.id_titleBar_rightText, v -> {
			startActivityForResult( new Intent( this, TradingAddAccountActivity.class ), 2008 );
		} );
		bottomLayout.setVisibility( "sell".equals( getIntent().getAction() ) ? View.GONE : View.VISIBLE );
	}

	private void initView() {
		accountAdapter = new SellAccountAdapter( this, models );
		accountAdapter.setOnStatusClickListener( this );
		expandableListView.setAdapter( accountAdapter );
		expandableListView.addHeaderView( initHeaderView() );
		expandableListView.addFooterView( initFootView() );
		expandableListView.setOnGroupClickListener( (parent, v, groupPosition, id) -> false );
		expandableListView.setOnChildClickListener( (parent, v, groupPosition, childPosition, id) -> {
			if (!accountAdapter.isCustomerChildSelectable( groupPosition )) {
				ToastUtils.showToast( this, "\u8d26\u53f7\u6b63\u5728\u4ea4\u6613\u4e2d\uff0c\u5df2\u88ab\u51bb\u7ed3" );
				return false;
			}
			if (!isHavaAlipay) {
				ToastUtils.showToast( this, "\u7528\u6237\u9700\u8981\u7ed1\u5b9a\u652f\u4ed8\u5b9d\u8d26\u53f7\u624d\u80fd\u8fdb\u884c\u4ea4\u6613" );
				return false;
			}
			SellAccountModel model = models.get( groupPosition );
			if (model.getStatus() == 1) {
				return true;
			}
			GameModel gameModel = model.getGameModels().get( childPosition );
			startActivityForResult( new Intent( this, TradingSellActivity.class )
					.putExtra( "gameId", gameModel.getGameId() )
					.putExtra( "gameName", gameModel.getName() )
					.putExtra( "account", model.getAccount() )
					.putExtra( "platforms", gameModel.getPlatforms() ), 2008 );
			return false;
		} );
		refreshLayout.setRefreshing( true );
		refreshLayout.setOnRefreshListener( this );
		refresh();
	}

	private View initHeaderView() {
		View view = LayoutInflater.from( this ).inflate( R.layout.layout_select_account_header, null, false );
		alipayTv = view.findViewById( R.id.id_trading_alipay );
		alipayTv.setOnClickListener( v -> {
			if (!isHavaAlipay) {
				startActivityForResult( new Intent( this, AddAlipayActivity.class )
						.setAction( "add" ), 2006 );
			} else {
				startActivityForResult( new Intent( this, AddAlipayActivity.class )
						.setAction( "edit" ), 2006 );
			}
		} );
		return view;
	}

	private View initFootView() {
		View view = LayoutInflater.from( this ).inflate( R.layout.layout_select_account_foot, null, false );
		return view;
	}

	@OnClick({R.id.id_select_reset, R.id.id_select_exit})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.id_select_reset:
				startActivity( new Intent( this, TradingResetPwdActivity.class ) );
				break;
			case R.id.id_select_exit:
				SpUtil.setTradingUid( "-1" );
				SpUtil.setTradingIsExit( true );
				ListenerManager.sendOnTradingLoginStatusListener( BeanUtils.tradingIsLogin() );
				startActivity( new Intent( this, TradingLoginActivity.class ) );
				finish();
				break;
		}
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		refreshLayout.setRefreshing( false );
		models.clear();
		if (1 == resultItem.getIntValue( "status" )) {
			ResultItem data = resultItem.getItem( "data" );
			List<ResultItem> items = data.getItems( "list" );
			if (!BeanUtils.isEmpty( items )) {
				initData( items );
			}
			alipay = data.getString( "alipay_acount" );
			isHavaAlipay = !TextUtils.isEmpty( alipay );
			if (!isHavaAlipay) {
				alipayTv.setText( "\u8fd8\u672a\u5173\u8054\u652f\u4ed8\u5b9d\u8d26\u53f7\uff0c\u70b9\u51fb\u53bb\u5173\u8054" );
			} else {
				alipayTv.setText( "\u5df2\u5173\u8054\u652f\u4ed8\u5b9d\u8d26\u53f7:" + CharSequenceUtils.getVisibilyText( alipay ) );
			}
		} else {
			ToastUtils.showToast( this, resultItem.getString( "msg" ) );
		}
		accountAdapter.notifyDataSetInvalidated();
	}

	private void expandGroup() {
		for (int i = 0; i < accountAdapter.getGroupCount(); i++) {
			expandableListView.expandGroup( i, true );
		}
		if (!expandableListView.isStackFromBottom()) {
			expandableListView.setStackFromBottom( true );
		}
		expandableListView.setStackFromBottom( false );
	}

	@Override
	public void onError(int what, String error) {
		refreshLayout.setRefreshing( false );
		ToastUtils.showToast( this, error );
	}

	private void initData(List<ResultItem> items) {
		for (ResultItem data : items) {
			SellAccountModel model = new SellAccountModel();
			model.setAccount( data.getString( "sdk_username" ) );
			model.setId( data.getString( "sdk_uid" ) );
			model.setStatus( data.getIntValue( "selling" ) );
			List<ResultItem> list = data.getItems( "game_list" );
			List<GameModel> gameModels = new ArrayList<GameModel>();
			if (!BeanUtils.isEmpty( list )) {
				for (ResultItem game : list) {
					GameModel gameModel = new GameModel();
					gameModel.setName( game.getString( "game_name" ) );
					gameModel.setGameId( game.getIntValue( "appid" ) );
					gameModel.setImgUrl( game.getString( "logo" ) );
					List<ResultItem> array = game.getItems( "system" );
					if (!BeanUtils.isEmpty( array )) {
						ArrayList<String> strs = new ArrayList<String>();
						for (int i = 0; i < array.size(); i++) {
							strs.add( String.valueOf( array.get( i ) ) );
						}
						gameModel.setPlatforms( strs );
					}
					gameModels.add( gameModel );
				}
			}
			model.setGameModels( gameModels );
			models.add( model );
		}
		expandGroup();
	}

	@Override
	public void onRefresh() {
		refresh();
	}

	private void refresh() {
		HttpManager.sdkUserList( HttpType.REFRESH, this, this );
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult( requestCode, resultCode, data );
		if (requestCode == 2006 && resultCode == RESULT_OK) {
			alipayTv.setText( "\u5df2\u5173\u8054\u652f\u4ed8\u5b9d\u8d26\u53f7:" +
					CharSequenceUtils.getVisibilyText( data.getStringExtra( "alipay" ) ) );
			return;
		}
		if (requestCode == 2008 && resultCode == RESULT_OK) {
			refresh();
		}
	}

	@Override
	public void onStatusClick(View view, SellAccountModel model) {
		currentModel = model;
		if (model.getStatus() == 0) {
			if (buidler == null) {
				buidler = new MaterialDialog.Builder( SelectAccountActivity.this )
						.title( "\u53d6\u6d88\u5173\u8054" )
						.negativeText( "\u8003\u8651\u4e00\u4e0b" )
						.positiveText( "\u786e\u8ba4\u53d6\u6d88" )
						.titleGravity( GravityEnum.CENTER )
						.content( "\u53d6\u6d88\u5173\u8054\u5c06\u5220\u9664\u6b64\u8d26\u53f7\u4e0b\u7684\u6240\u6709\u51fa\u552e\u4e2d\u7684\u6e38\u620f\u8d26\u53f7\uff0c\u662f\u5426\u53d6\u6d88\u5173\u8054?" )
						.buttonsGravity( GravityEnum.CENTER )
						.positiveColor( getResources().getColor( R.color.blue_40 ) )
						.negativeColor( getResources().getColor( R.color.blue_40 ) )
						.titleColor( getResources().getColor( R.color.gray_69 ) )
						.contentColor( getResources().getColor( R.color.gray_9a ) )
						.callback( new MaterialDialog.ButtonCallback() {
							@Override
							public void onPositive(MaterialDialog dialog) {
								unBindSdkUser();
							}
						} );
			}
			buidler.show();
		}
	}

	private void unBindSdkUser() {
		buildProgressDialog();
		HttpManager.unBindSdkUser( 10, SelectAccountActivity.this, new HttpResultListener() {
			@Override
			public void onSuccess(int what, ResultItem resultItem) {
				cancelProgressDialog();
				if (1 == resultItem.getIntValue( "status" )) {
					ToastUtils.showToast( SelectAccountActivity.this, "\u53d6\u6d88\u6210\u529f" );
					models.remove( currentModel );
					currentModel = null;
					accountAdapter.notifyDataSetInvalidated();
				} else {
					ToastUtils.showToast( SelectAccountActivity.this, resultItem.getString( "msg" ) );
				}
			}

			@Override
			public void onError(int what, String error) {
				cancelProgressDialog();
				ToastUtils.showToast( SelectAccountActivity.this, error );
			}
		}, currentModel.getAccount() );
	}
}
