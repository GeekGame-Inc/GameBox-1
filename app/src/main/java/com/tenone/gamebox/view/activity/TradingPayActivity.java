package com.tenone.gamebox.view.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.PayResultCallback;
import com.tenone.gamebox.mode.mode.CommonViewHolder;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.PayModel;
import com.tenone.gamebox.mode.mode.ProductModel;
import com.tenone.gamebox.mode.mode.PurchaseModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.adapter.CommonAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.MyListView;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.JrttUtils;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.MyLog;
import com.tenone.gamebox.view.utils.PayUtils;
import com.tenone.gamebox.view.utils.ToastUtils;
import com.tenone.gamebox.view.utils.TradingPayTask;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class TradingPayActivity extends BaseActivity implements HttpResultListener, PayResultCallback {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_trading_pay_icon)
	ImageView iconIv;
	@ViewInject(R.id.id_trading_pay_title)
	TextView titleTv;
	@ViewInject(R.id.id_trading_pay_platform)
	TextView platformTv;
	@ViewInject(R.id.id_trading_pay_server)
	TextView serverTv;
	@ViewInject(R.id.id_trading_pay_name)
	TextView nameTv;
	@ViewInject(R.id.id_trading_pay_price)
	TextView priceTv;


	@ViewInject(R.id.id_trading_pay_listview)
	MyListView payLv;


	private String[] payStyleArray = {"\u652f\u4ed8\u5b9d\u652f\u4ed8", "\u5fae\u4fe1\u652f\u4ed8"};
	private int leftId[] = {R.drawable.ic_alipay, R.drawable.ic_wecat};
	private List<PayModel> payStyle = new ArrayList<PayModel>();
	private CommonAdapter<PayModel> payStyleAdapter;
	private int currentPosition = 0;
	private ProductModel productModel;
	private String text = "", orderId = "";
	private Context context;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		context = this;
		productModel = (ProductModel) getIntent().getSerializableExtra( "productModel" );
		setContentView( R.layout.activity_trading_pay );
		ViewUtils.inject( this );
		initTitle();
		initPayStyle();
		initView();
	}

	private void initTitle() {
		titleBarView.setLeftImg( R.drawable.icon_xqf_b );
		titleBarView.setTitleText( "\u8d2d\u4e70\u5546\u54c1" );
		titleBarView.setOnClickListener( R.id.id_titleBar_leftImg, v -> finish() );
	}

	private void initView() {
		payStyleAdapter = new CommonAdapter<PayModel>( this, payStyle,
				R.layout.item_vip_pay ) {
			@SuppressWarnings("deprecation")
			@Override
			public void convert(CommonViewHolder holder, PayModel t) {
				TextView view = holder.getView( R.id.id_vip_paystyle );
				view.setText( t.getName() );
				Drawable lefrDrawable = getResources().getDrawable( t.getlId() );
				Drawable rightDrawable = getResources().getDrawable(
						R.drawable.icon_checked );
				lefrDrawable.setBounds( 0, 0, lefrDrawable.getMinimumWidth(),
						lefrDrawable.getMinimumHeight() );
				rightDrawable.setBounds( 0, 0, rightDrawable.getMinimumWidth(),
						rightDrawable.getMinimumHeight() );
				view.setCompoundDrawables( lefrDrawable, null,
						(currentPosition == holder.getPosition() ? rightDrawable : null), null );
			}
		};
		payLv.setAdapter( payStyleAdapter );
		payLv.setOnItemClickListener( (parent, view, position, id) -> {
			if (payStyle.size() > 0) {
				currentPosition = position;
				payStyleAdapter.notifyDataSetChanged();
			}
		} );
		String str = productModel.getImgs().get( 0 );
		ImageLoadUtils.loadNormalImg( iconIv, this, str );
		titleTv.setText( productModel.getTitle() );
		serverTv.setText( productModel.getServer() );
		nameTv.setText( productModel.getGameName() );
		platformTv.setText( getString( productModel.getPlatform() == 1 ? R.string.android :
				(productModel.getPlatform() == 2 ? R.string.ios : R.string.double_end) ) );
		priceTv.setText( productModel.getPrice() );
		ListenerManager.registerPayResultCallback( this );
	}

	public void initPayStyle() {
		for (int i = 0; i < payStyleArray.length; i++) {
			PayModel model = new PayModel();
			model.setlId( leftId[i] );
			model.setName( payStyleArray[i] );
			model.setPayStyle( (i + 1) );
			payStyle.add( model );
		}
	}

	@OnClick({R.id.id_trading_pay})
	public void onClick(View view) {
		if (view.getId() == R.id.id_trading_pay) {
			buildProgressDialog();
			HttpManager.tradingStartPayment( HttpType.REFRESH, this, this,
					productModel.getProductId(), (currentPosition + 1) );
		}
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		cancelProgressDialog();
		if (1 == resultItem.getIntValue( "status" )) {
			ResultItem data = resultItem.getItem( "data" );
			orderId = data.getString( "id" );
			if (currentPosition == 0) {
				text = data.getString( "token" );
				new TradingPayTask( TradingPayActivity.this, context ).execute( text );
			} else {
				ResultItem token = data.getItem( "token" );
				if (!BeanUtils.isEmpty( token )) {
					PayUtils.startWXPay( token );
				}
			}
		} else {
			ToastUtils.showToast( this, resultItem.getString( "msg" ) );
		}
	}

	@Override
	public void onError(int what, String error) {
		cancelProgressDialog();
		ToastUtils.showToast( this, error );
	}

	@Override
	public void onPaySuccess() {
		ToastUtils.showToast( this, "\u4ea4\u6613\u6210\u529f\u540e\u8d26\u53f7\u5bc6\u7801\u5c06\u4f1a\u4ee5\u77ed\u4fe1\u7684\u5f62\u5f0f\u53d1\u9001\u7ed9\u60a8\uff0c\u8bf7\u6ce8\u610f\u67e5\u6536\uff0c\u5982\u679c\u957f\u65f6\u95f4\u672a\u6536\u5230\u8bf7\u53ca\u65f6\u8054\u7cfb\u5ba2\u670d\u8fdb\u884c\u6c9f\u901a" );
		JrttUtils.jrttReportPay( new PurchaseModel( "game_account_trading", "game_account", orderId,
				1, currentPosition == 0 ? "alipay" : "weixinpay", "CNY",
				true, Float.valueOf( productModel.getPrice() ).intValue() ) );
		setResult( RESULT_OK );
		finish();
	}

	@Override
	public void onPayFail() {
		MyLog.d( "onPayFail" );
		JrttUtils.jrttReportPay( new PurchaseModel( "game_account_trading", "game_account", orderId,
				1, currentPosition == 0 ? "alipay" : "weixinpay", "CNY",
				false, Float.valueOf( productModel.getPrice() ).intValue() ) );
	}

	@Override
	public void onPayCancle() {
		MyLog.d( "onPayCancle" );
		JrttUtils.jrttReportPay( new PurchaseModel( "game_account_trading", "game_account", orderId,
				1, currentPosition == 0 ? "alipay" : "weixinpay", "CNY", false, Float.valueOf( productModel.getPrice() ).intValue() ) );
		HttpManager.tradingCancelPayment( 55, this, new HttpResultListener() {
			@Override
			public void onSuccess(int what, ResultItem resultItem) {
				MyLog.d( resultItem.getString( "msg" ) );
			}

			@Override
			public void onError(int what, String error) {
				MyLog.d( error );
			}
		}, orderId );
	}

	@Override
	protected void onDestroy() {
		ListenerManager.unRegisterPayResultCallback( this );
		super.onDestroy();
	}
}
