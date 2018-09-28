package com.tenone.gamebox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ProductModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.MoreTextView;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.dialog.TradingNotesDialog;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.TimeUtils;
import com.tenone.gamebox.view.utils.ToastUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;
import com.thoughtworks.xstream.mapper.Mapper;

import java.util.ArrayList;
import java.util.List;

public class TradingProductDetailsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, HttpResultListener {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_product_details_icon)
	ImageView iconIv;
	@ViewInject(R.id.id_product_details_name)
	TextView nameTv;
	@ViewInject(R.id.id_product_details_size)
	TextView sizeTv;
	@ViewInject(R.id.id_product_details_server)
	TextView serverTv;
	@ViewInject(R.id.id_product_details_platform)
	TextView platformTv;
	@ViewInject(R.id.id_product_details_price)
	TextView priceTv;
	@ViewInject(R.id.id_product_details_pay)
	TextView payTv;
	@ViewInject(R.id.id_product_details_desc)
	MoreTextView descTv;
	@ViewInject(R.id.id_product_details_imgs)
	LinearLayout imgLv;
	@ViewInject(R.id.id_product_details_refresh)
	SwipeRefreshLayout refreshLayout;
	@ViewInject(R.id.id_product_details_down)
	ImageView downloadIv;

	private ArrayList<String> imgs = new ArrayList<>();
	private String productId = "", gameId = "";
	private int dp10;
	private Context context;
	private ProductModel productModel;
	private TradingNotesDialog.TradingNotesBuilder builder;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		context = this;
		super.onCreate( arg0 );
		productId = getIntent().getStringExtra( "productId" );
		productModel = new ProductModel();
		productModel.setProductId( productId );
		setContentView( R.layout.activity_product_details );
		ViewUtils.inject( this );
		initTitle();
		initView();
	}

	private void initTitle() {
		titleBarView.setLeftImg( R.drawable.icon_xqf_b );
		titleBarView.setTitleText( "\u5546\u54c1\u8be6\u60c5" );
		titleBarView.setOnClickListener( R.id.id_titleBar_leftImg, v -> finish() );
	}

	private void initView() {
		refreshLayout.setRefreshing( true );
		refreshLayout.setOnRefreshListener( this );
		request();
	}

	private void request() {
		HttpManager.productInfo( HttpType.REFRESH, this, this, productId, 0 );
	}

	@OnClick({R.id.id_product_details_buy})
	public void onClick(View view) {
		if (view.getId() == R.id.id_product_details_buy) {
			if (BeanUtils.tradingIsLogin()) {
				if (builder == null) {
					builder = new TradingNotesDialog.TradingNotesBuilder( context );
					builder.setType( 1 );
					builder.setOnAgreeClickListener( () -> {
						startActivity( new Intent( this, TradingPayActivity.class )
								.putExtra( "productModel", productModel ) );
						builder.dismiss();
					} );
				}
				builder.showDialog();
			} else {
				startActivity( new Intent( this, TradingLoginActivity.class ) );
			}
		}
	}

	@Override
	public void onRefresh() {
		request();
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		refreshLayout.setRefreshing( false );
		if (1 == resultItem.getIntValue( "status" )) {
			ResultItem data = resultItem.getItem( "data" );
			setData( data );
		} else {
			ToastUtils.showToast( this, resultItem.getString( "msg" ) );
		}
	}

	private void setData(ResultItem data) {
		dp10 = DisplayMetricsUtils.dipTopx( this, 10 );
		nameTv.setText( data.getString( "game_name" ) );
		productModel.setGameName( data.getString( "game_name" ) );
		sizeTv.setText( data.getString( "size" ) + "M" );
		productModel.setSize( data.getString( "size" ) );
		String time = data.getString( "account_cretime" );
		try {
			long t = Long.valueOf( time ) * 1000;
			time = TimeUtils.formatDateDay( t );
		} catch (NumberFormatException e) {
		}
		payTv.setText( "\u6b64\u8d26\u53f7\u521b\u5efa\u4e8e" + time
				+ "\uff0c\u5f53\u524d\u6e38\u620f\u5df2\u5145\u503c" + data.getString( "pay_money" ) + "\u5143" );
		gameId = data.getString( "box_gameid" );
		if (!TextUtils.isEmpty( gameId )) {
			int id = Integer.valueOf( gameId ).intValue();
			if (id > 0) {
				downloadIv.setVisibility( View.VISIBLE );
				downloadIv.setOnClickListener( v -> {
					startActivity( new Intent( this, NewGameDetailsActivity.class )
							.putExtra( "id", gameId ) );
				} );
			}
		}
		int system = data.getIntValue( "system" );
		platformTv.setText( system == 1 ? getString( R.string.android ) : system == 2 ? "ios" : getString( R.string.double_end ) );
		productModel.setPlatform( system );
		descTv.setText( data.getString( "desc" ) );
		ImageLoadUtils.loadNormalImg( iconIv, this, data.getString( "game_logo" ) );
		productModel.setIcon( data.getString( "game_logo" ) );
		serverTv.setText( data.getString( "server_name" ) );
		productModel.setServer( data.getString( "server_name" ) );
		productModel.setTitle( data.getString( "title" ) );
		priceTv.setText( data.getString( "price" ) );
		productModel.setPrice( data.getString( "price" ) );
		List<ResultItem> list = data.getItems( "imgs" );
		imgs.clear();
		imgLv.removeAllViews();
		addImageView( list );
	}

	private void addImageView(List<ResultItem> list) {
		new AsyncTask<List<ResultItem>, Mapper.Null, List<ImageView>>() {
			@Override
			protected List<ImageView> doInBackground(List<ResultItem>... lists) {
				List<ResultItem> list = lists[0];
				if (!BeanUtils.isEmpty( list )) {
					for (int i = 0; i < list.size(); i++) {
						String url = String.valueOf( list.get( i ) );
						imgs.add( url );
					}
				}
				productModel.setImgs( imgs );
				List<ImageView> imageViews = new ArrayList<>();
				for (int i = 0; i < imgs.size(); i++) {
					ImageView imageView = new ImageView( context );
					imageView.setPadding( dp10, dp10, dp10, dp10 );
					imageViews.add( imageView );
				}
				return imageViews;
			}

			@Override
			protected void onPostExecute(List<ImageView> imageViews) {
				super.onPostExecute( imageViews );
				for (int j = 0; j < imageViews.size(); j++) {
					ImageView imageView = imageViews.get( j );
					String url = imgs.get( j );
					ImageLoadUtils.loadIntoUseFitWidth( context, url,
							R.drawable.ic_banner_failure, R.drawable.icon_banner_loading, imageView );
					imgLv.addView( imageView );
					imageView.setOnClickListener( v -> {
						startActivity( new Intent( context, BrowseImageActivity.class )
								.putStringArrayListExtra( "urls", imgs )
								.putExtra( "url", url ) );
					} );
				}
			}
		}.execute( list );
	}

	@Override
	public void onError(int what, String error) {
		refreshLayout.setRefreshing( false );
		ToastUtils.showToast( this, error );
	}
}
