package com.tenone.gamebox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jzxiang.pickerview.data.Type;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnTimePickerSeleteListener;
import com.tenone.gamebox.mode.listener.OnWheeledListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ProductModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.adapter.ImageGridAdapter;
import com.tenone.gamebox.view.adapter.PublishDynamicGridAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.base.Constant;
import com.tenone.gamebox.view.custom.MyGridView;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.pickers.CustomPicker;
import com.tenone.gamebox.view.custom.pickers.TimePickerDialog;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.FileUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.TimeUtils;
import com.tenone.gamebox.view.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class TradingSellActivity extends BaseActivity implements OnTimePickerSeleteListener, OnWheeledListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, PublishDynamicGridAdapter.OnDeleteClickListener, HttpResultListener {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_trading_sell_gameName)
	TextView gameNameTv;
	@ViewInject(R.id.id_trading_sell_account)
	TextView accountTv;
	@ViewInject(R.id.id_trading_sell_platform)
	TextView platformTv;
	@ViewInject(R.id.id_trading_sell_describe)
	EditText describeTv;
	@ViewInject(R.id.id_trading_sell_time)
	TextView timeTv;
	@ViewInject(R.id.id_trading_sell_server)
	EditText serverEt;
	@ViewInject(R.id.id_trading_sell_price)
	EditText priceEt;
	@ViewInject(R.id.id_trading_sell_title)
	EditText titleEt;
	@ViewInject(R.id.id_trading_sell_imgs)
	MyGridView gridView;
	@ViewInject(R.id.id_trading_sell_editImg)
	TextView editImgTv;
	@ViewInject(R.id.id_trading_sell_imgs1)
	MyGridView gridView1;
	@ViewInject(R.id.id_trading_sell_editImg1)
	TextView editImgTv1;


	private String gameName, account, title, desc, time, price, server, productId;
	private int gameId, platform = 0;
	private TimePickerDialog timePickerDialog;
	private CustomPicker picker;
	private String[] platformArray;

	//��Ϸ��ͼ
	private PublishDynamicGridAdapter adapter;
	private ImageGridAdapter adapter2;
	private ArrayList<String> paths = new ArrayList<String>();
	private ArrayList<String> paths2 = new ArrayList<String>();
	private static final int STARTALBUM = 110;
	//��ֵ��ͼ
	private PublishDynamicGridAdapter adapter3;
	private ImageGridAdapter adapter4;
	private ArrayList<String> paths3 = new ArrayList<String>();
	private ArrayList<String> paths4 = new ArrayList<String>();

	private static final int STARTALBUM2 = 120;

	private static final int MAX_COUNT = 7, MIN_COUNT = 2;

	private ProductModel productModel;
	private MaterialDialog.Builder buidler;
	private Context context;
	private int dp50;
	private int scWidth;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		context = this;
		productModel = new ProductModel();
		Intent intent = getIntent();
		if (intent.hasExtra( "productId" )) {
			productId = intent.getStringExtra( "productId" );
			productModel.setProductId( productId );
		} else {
			gameName = intent.getStringExtra( "gameName" );
			account = intent.getStringExtra( "account" );
			gameId = intent.getIntExtra( "gameId", 0 );
			List<String> array = intent.getStringArrayListExtra( "platforms" );
			if (array != null) {
				platformArray = new String[array.size()];
				for (int i = 0; i < array.size(); i++) {
					platformArray[i] = getString( TextUtils.equals( "1", array.get( i ) ) ? R.string.android :
							TextUtils.equals( "2", array.get( i ) ) ? R.string.ios : R.string.double_end );
				}
			}
		}
		super.onCreate( arg0 );
		setContentView( R.layout.activity_trading_sell );
		ViewUtils.inject( this );
		initTitle();
		if (TextUtils.isEmpty( productId )) {
			initView();
		} else {
			initView2();
		}
	}


	private void initTitle() {
		titleBarView.setLeftImg( R.drawable.icon_xqf_b );
		titleBarView.setTitleText( "\u6211\u8981\u5356\u53f7" );
		titleBarView.setOnClickListener( R.id.id_titleBar_leftImg, v -> finish() );
	}

	private void initView() {
		gameNameTv.setText( gameName );
		accountTv.setText( account );
		priceEt.setHint( getResources().getString( R.string.min_price ) + Constant.getProductMinPrice() + "\u5143" );
		initAdapter1();
		initAdapter3();
		productModel.setAppid( String.valueOf( gameId ) );
		productModel.setSdkUserName( account );
		if (platformArray != null && platformArray.length == 1) {
			platformTv.setText( platformArray[0] );
			platform = TextUtils.equals( getString( R.string.android ), platformArray[0] ) ? 1 :
					TextUtils.equals( getString( R.string.ios ), platformArray[0] ) ? 2 : 3;
			platformTv.setClickable( false );
		}
	}

	private void initAdapter1() {
		adapter = new PublishDynamicGridAdapter( this, paths );
		adapter.setMaxSize( 7 );
		gridView.setAdapter( adapter );
		gridView.setOnItemClickListener( this );
		gridView.setOnItemLongClickListener( this );
		adapter.setOnDeleteClickListener( this );
	}


	private void initAdapter3() {
		adapter3 = new PublishDynamicGridAdapter( this, paths3 );
		adapter3.setMaxSize( 7 );
		gridView1.setAdapter( adapter3 );
		gridView1.setOnItemClickListener( (parent, view, position, id) -> {
			if (position == (adapter3.getCount() - 1) && paths3.size() < MAX_COUNT) {
				choicePhotoWrapper( paths3, STARTALBUM2 );
			}
		} );
		gridView1.setOnItemLongClickListener( (parent, view, position, id) -> {
			adapter3.setLongClickPosition( position );
			adapter3.notifyDataSetChanged();
			return false;
		} );
		adapter3.setOnDeleteClickListener( position -> {
			paths3.remove( position );
			adapter3.notifyDataSetChanged();
		} );
	}

	private void initView2() {
		dp50 = DisplayMetricsUtils.dipTopx( context, 50 );
		scWidth = DisplayMetricsUtils.getScreenWidth( context );
		editImgTv.setVisibility( View.VISIBLE );
		editImgTv.setOnClickListener( v -> {
			editImgTv.setVisibility( View.GONE );
			initAdapter1();
		} );
		editImgTv1.setVisibility( View.VISIBLE );
		editImgTv1.setOnClickListener( v -> {
			editImgTv1.setVisibility( View.GONE );
			initAdapter3();
		} );
		buildProgressDialog( this );
		HttpManager.productInfo( 0, this, new HttpResultListener() {
			@Override
			public void onSuccess(int what, ResultItem resultItem) {
				cancelProgressDialog();
				if (1 == resultItem.getIntValue( "status" )) {
					ResultItem data = resultItem.getItem( "data" );
					productModel.setTitle( data.getString( "title" ) );
					productModel.setServer( data.getString( "server_name" ) );
					productModel.setSdkUserName( data.getString( "account" ) );
					productModel.setDesc( data.getString( "desc" ) );
					productModel.setPrice( data.getString( "price" ) );
					productModel.setPlatform( data.getIntValue( "system" ) );
					productModel.setGameName( data.getString( "game_name" ) );
					String t = data.getString( "end_time" );
					if (!TextUtils.isEmpty( t )) {
						long t1 = Long.valueOf( t ).longValue() * 1000;
						productModel.setEndTime( TimeUtils.formatData( t1, "yyyy-MM-dd" ) );
					}
					List<ResultItem> list = data.getItems( "imgs" );
					if (!BeanUtils.isEmpty( list )) {
						for (int i = 0; i < list.size(); i++) {
							String text = String.valueOf( list.get( i ) );
							paths2.add( text );
						}
					}
					productModel.setImgs( paths2 );
					List<ResultItem> array = data.getItems( "trade_imgs" );
					if (!BeanUtils.isEmpty( array )) {
						for (int i = 0; i < array.size(); i++) {
							String text = String.valueOf( array.get( i ) );
							paths4.add( text );
						}
					}
					productModel.setTradeImgs( paths4 );
					List<ResultItem> system = data.getItems( "system_enabled" );
					if (system != null) {
						platformArray = new String[system.size()];
						for (int i = 0; i < system.size(); i++) {
							String text = String.valueOf( system.get( i ) );
							platformArray[i] = getString( "1".equals( text ) ? R.string.android : "2".equals( text ) ? R.string.ios : R.string.double_end );
						}
					}
					platformTv.setText( getString( 1 == productModel.getPlatform() ? R.string.android :
							2 == productModel.getPlatform() ? R.string.ios : R.string.double_end ) );
					platform = productModel.getPlatform();
					if (platformArray.length == 1) {
						platformTv.setClickable( false );
					}
					setData();
				} else {
					ToastUtils.showToast( context, resultItem.getString( "msg" ) );
				}
			}

			@Override
			public void onError(int what, String error) {
				cancelProgressDialog();
				ToastUtils.showToast( context, error );
			}
		}, productId, 1 );
	}

	private void setData() {
		gameName = productModel.getGameName();
		account = productModel.getSdkUserName();
		title = productModel.getTitle();
		desc = productModel.getDesc();
		time = productModel.getEndTime();
		price = productModel.getPrice();
		server = productModel.getServer();
		platform = productModel.getPlatform();
		gameNameTv.setText( gameName );
		accountTv.setText( account );
		titleEt.setText( title );
		describeTv.setText( desc );
		describeTv.setSelection( desc.length() );
		priceEt.setText( price );
		serverEt.setText( server );
		platformTv.setText( getString( platform == 1 ? R.string.android : R.string.ios ) );
		timeTv.setText( time );
		serverEt.setSelection( server.length() );
		titleEt.setSelection( title.length() );
		priceEt.setSelection( price.length() );
		initAdapter2();
		initAdapter4();
	}

	private void initAdapter2() {
		if (adapter2 == null) {
			adapter2 = new ImageGridAdapter( this, paths2 );
		}
		adapter2.setImgHeight( (scWidth - dp50) / 4 );
		gridView.setAdapter( adapter2 );
		gridView.setOnItemClickListener( (parent, view, position, id) -> startActivity(
				new Intent( context, BrowseImageActivity.class )
						.putStringArrayListExtra( "urls", paths2 )
						.putExtra( "url", paths2.get( position ) ) ) );
		adapter2.notifyDataSetChanged();
	}

	private void initAdapter4() {
		if (adapter4 == null) {
			adapter4 = new ImageGridAdapter( this, paths4 );
		}
		adapter4.setImgHeight( (scWidth - dp50) / 4 );
		gridView1.setAdapter( adapter4 );
		gridView1.setOnItemClickListener( (parent, view, position, id) -> startActivity(
				new Intent( context, BrowseImageActivity.class )
						.putStringArrayListExtra( "urls", paths4 )
						.putExtra( "url", paths2.get( position ) ) ) );
		adapter4.notifyDataSetChanged();
	}


	@OnClick({R.id.id_trading_sell_platform, R.id.id_trading_sell_time, R.id.id_trading_sell_confirm})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.id_trading_sell_platform:
				if (platformArray != null && platformArray.length > 1) {
					showPlatformPicker();
				}
				break;
			case R.id.id_trading_sell_time:
				showTimePicker();
				break;
			case R.id.id_trading_sell_confirm:
				if (checkSell()) {
					if (buidler == null) {
						initBuilder();
					}
					buidler.show();
				}
				break;
		}
	}

	private void initBuilder() {
		buidler = new MaterialDialog.Builder( context )
				.title( "\u6211\u8981\u5356\u53f7" )
				.negativeText( "\u8003\u8651\u4e00\u4e0b" )
				.positiveText( "\u786e\u8ba4\u51fa\u552e" )
				.titleGravity( GravityEnum.CENTER )
				.content( "\u5546\u54c1\u8fdb\u5165\"\u5ba1\u6838\u4e2d\"\u9700\u8981\u5ba2\u670d\u5ba1\u6838\u901a\u8fc7\u624d\u80fd\u5c55\u793a,\u9884\u8ba1\u9700\u89811-3\u5929\u3002" +
						"\u5982\u679c\u5ba1\u6838\u5931\u8d25,\u5c06\u544a\u77e5\u60a8\u539f\u56e0,\u60a8\u53ef\u4ee5\u5728\u6211\u7684\u51fa\u552e\u8ba2\u5355\u754c\u9762\u67e5\u770b\u8fdb\u5ea6\u3002" )
				.buttonsGravity( GravityEnum.CENTER )
				.positiveColor( getResources().getColor( R.color.blue_40 ) )
				.negativeColor( getResources().getColor( R.color.blue_40 ) )
				.titleColor( getResources().getColor( R.color.gray_69 ) )
				.contentColor( getResources().getColor( R.color.gray_9a ) )
				.callback( new MaterialDialog.ButtonCallback() {
					@Override
					public void onPositive(MaterialDialog dialog) {
						dialog.dismiss();
						uploadProduct();
					}
				} );
	}

	@Override
	protected void onDestroy() {
		cancelProgressDialog();
		super.onDestroy();
	}

	private void uploadProduct() {
		buildProgressDialog( context );
		if (TextUtils.isEmpty( productId )) {
			HttpManager.sellProduct( HttpType.REFRESH,
					context, TradingSellActivity.this, productModel );
		} else {
			HttpManager.applyOnsale( HttpType.REFRESH,
					context, TradingSellActivity.this, productModel );
		}
	}

	private boolean checkSell() {
		server = serverEt.getText().toString();
		if (TextUtils.isEmpty( server )) {
			ToastUtils.showToast( this, "\u672a\u586b\u6e38\u620f\u533a\u670d" );
			return false;
		}
		productModel.setServer( server );
		if (platform <= 0) {
			ToastUtils.showToast( this, "\u672a\u9009\u6e38\u620f\u5e73\u53f0" );
			return false;
		}
		productModel.setSystem( platform );
		price = priceEt.getText().toString();
		if (TextUtils.isEmpty( price )) {
			ToastUtils.showToast( this, "\u672a\u586b\u51fa\u552e\u4ef7\u683c" );
			return false;
		}
		try {
			double p = Double.valueOf( price ).doubleValue();
			if (p < Constant.getProductMinPrice()) {
				ToastUtils.showToast( this, "\u51fa\u552e\u4ef7\u683c\u4e0d\u80fd\u4f4e\u4e8e" + Constant.getProductMinPrice() + "\u5143" );
				return false;
			}
		} catch (NumberFormatException e) {
			ToastUtils.showToast( this, "\u8bf7\u586b\u5199\u6b63\u786e\u7684\u4ef7\u683c" );
			return false;
		}
		productModel.setPrice( price );
		title = titleEt.getText().toString();
		if (TextUtils.isEmpty( title )) {
			ToastUtils.showToast( this, "\u672a\u586b\u5546\u54c1\u6807\u9898" );
			return false;
		}
		productModel.setTitle( title );

		if (TextUtils.isEmpty( time )) {
			ToastUtils.showToast( this, "\u672a\u9009\u62e9\u5546\u54c1\u8fc7\u671f\u65f6\u95f4" );
			return false;
		}
		productModel.setEndTime( time );

		desc = describeTv.getText().toString();
		if (TextUtils.isEmpty( desc )) {
			ToastUtils.showToast( this, "\u672a\u586b\u5546\u54c1\u63cf\u8ff0" );
			return false;
		}
		productModel.setDesc( desc );

		if (!TextUtils.isEmpty( productId )) {
			if (paths2.isEmpty() && (paths.isEmpty() || paths.size() < MIN_COUNT)) {
				ToastUtils.showToast( this, "\u81f3\u5c11\u4e0a\u4f20" + MIN_COUNT + "\u5f20\u6e38\u620f\u622a\u56fe" );
				return false;
			}
		} else {
			if ((paths.isEmpty() || paths.size() < MIN_COUNT)) {
				ToastUtils.showToast( this, "\u81f3\u5c11\u4e0a\u4f20" + MIN_COUNT + "\u5f20\u6e38\u620f\u622a\u56fe" );
				return false;
			}
		}
		productModel.setImgs( paths );
		if (!TextUtils.isEmpty( productId )) {
			if (paths4.isEmpty() && (paths3.isEmpty() || paths3.size() < MIN_COUNT)) {
				ToastUtils.showToast( this, "\u81f3\u5c11\u4e0a\u4f20" + MIN_COUNT + "\u5f20\u6e38\u620f\u622a\u56fe" );
				return false;
			}
		} else {
			if ((paths3.isEmpty() || paths3.size() < MIN_COUNT)) {
				ToastUtils.showToast( this, "\u81f3\u5c11\u4e0a\u4f20" + MIN_COUNT + "\u5f20\u6e38\u620f\u622a\u56fe" );
				return false;
			}
		}
		productModel.setTradeImgs( paths3 );
		return true;
	}

	public void showTimePicker() {
		if (timePickerDialog == null) {
			timePickerDialog = new TimePickerDialog.Builder()
					.setCancelStringId( getString( R.string.cancle ) )
					.setSureStringId( getString( R.string.confirm ) )
					.setTitleStringId( "\u9009\u62e9\u7ed3\u675f\u65f6\u95f4" )
					.setCyclic( true )
					.setThemeColor( getResources().getColor( R.color.blue_40 ) )
					.setWheelItemTextSelectorColorId( getResources().getColor( R.color.blue_40 ) )
					.setWheelItemTextNormalColorId( getResources().getColor( R.color.gray_9a ) )
					.setType( Type.YEAR_MONTH_DAY )
					.setWheelItemTextSize( 12 )
					.setMinMillseconds( System.currentTimeMillis() )
					.build();
			timePickerDialog.setListener( this );
		}
		timePickerDialog.show( getSupportFragmentManager().beginTransaction(), "time" );
	}


	private void showPlatformPicker() {
		if (picker == null) {
			picker = new CustomPicker( this, platformArray, "\u9009\u62e9\u5e73\u53f0" );
			picker.setOnWheeledListener( this );
			picker.setGravity( Gravity.CENTER );//居中
		}
		picker.setOffset( 1 );
		picker.show();
	}

	@Override
	public void onTimePickerSelete(String year, String month, String day) {
		time = year + "-" + month + "-" + day;
		timeTv.setText( time );
	}

	@Override
	public void onWheeled(String text) {
		platform = platformArray[0].equals( text ) ? 1 : 2;
		platformTv.setText( text );
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult( requestCode, resultCode, data );
		if (requestCode == STARTALBUM && resultCode == RESULT_OK) {
			paths.clear();
			paths.addAll( BGAPhotoPickerActivity.getSelectedImages( data ) );
			adapter.notifyDataSetChanged();
		}
		if (requestCode == STARTALBUM2 && resultCode == RESULT_OK) {
			paths3.clear();
			paths3.addAll( BGAPhotoPickerActivity.getSelectedImages( data ) );
			adapter3.notifyDataSetChanged();
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position == (adapter.getCount() - 1) && paths.size() < MAX_COUNT) {
			choicePhotoWrapper( paths, STARTALBUM );
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		adapter.setLongClickPosition( position );
		adapter.notifyDataSetChanged();
		return false;
	}

	@Override
	public void onDeleteClick(int position) {
		paths.remove( position );
		adapter.notifyDataSetChanged();
	}

	private void choicePhotoWrapper(ArrayList<String> array, int requestCode) {
		File takePhotoDir = FileUtils.getCacheDirectory( this );
		startActivityForResult( BGAPhotoPickerActivity.newIntent( this, takePhotoDir, MAX_COUNT,
				array, false ), requestCode );
	}


	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		cancelProgressDialog();
		if (1 == resultItem.getIntValue( "status" )) {
			ToastUtils.showToast( this, "\u4e0a\u4f20\u6210\u529f" );
			ToastUtils.showToast( this, "\u4ea4\u6613\u6210\u529f\u540e\u6240\u5f97\u6536\u5165\u6263\u53bb\u670d\u52a1\u8d39\u540e\u5c06\u4f1a\u81ea\u52a8\u8f6c\u5165\u60a8\u7684\u652f\u4ed8\u5b9d\uff0c\u6211\u4eec\u4e5f\u4f1a\u7ed9\u60a8\u53d1\u9001\u77ed\u4fe1\uff0c\u5982\u679c\u957f\u65f6\u95f4\u672a\u6536\u5230\u8bf7\u53ca\u65f6\u8054\u7cfb\u5ba2\u670d\u8fdb\u884c\u6c9f\u901a" );
			if (productModel != null && productModel.getImgs() != null && !productModel.getImgs().isEmpty()) {
				if (!paths.isEmpty()) {
					new DeleteFileThread( productModel.getImgs() ).start();
				}
				if (!paths3.isEmpty()) {
					new DeleteFileThread( productModel.getTradeImgs() ).start();
				}
			}
			setResult( RESULT_OK );
			finish();
		} else {
			ToastUtils.showToast( this, resultItem.getString( "msg" ) );
		}
	}

	@Override
	public void onError(int what, String error) {
		cancelProgressDialog();
		ToastUtils.showToast( this, error );
	}

	private class DeleteFileThread extends Thread {
		List<String> array;

		public DeleteFileThread(List<String> array) {
			this.array = array;
		}

		@Override
		public void run() {
			for (String path : array) {
				File file = new File( Configuration.cachepath, new File( path ).getName() );
				FileUtils.delFile( file );
			}
			super.run();
		}
	}
}
