package com.tenone.gamebox.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.CommonViewHolder;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.PayModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.VipModel;
import com.tenone.gamebox.view.adapter.CommonAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.TrackingUtils;

import java.util.ArrayList;
import java.util.List;

public class OpeningVipActvity extends BaseActivity implements
		HttpResultListener {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_vip_textview1)
	TextView textView;
	@ViewInject(R.id.id_vip_gridView)
	GridView gridView;
	@ViewInject(R.id.id_vip_listview)
	ListView listView;
	@ViewInject(R.id.id_vip_price)
	TextView priceTv;
	@ViewInject(R.id.id_vip_oldprice)
	TextView oldPriceTv;
	@ViewInject(R.id.id_vip_open)
	Button button;
	private List<PayModel> payStyle = new ArrayList<PayModel>();
	private List<VipModel> vipModels = new ArrayList<VipModel>();
	private CommonAdapter<VipModel> monthAdapter;
	private CommonAdapter<PayModel> payStyleAdapter;
	private int currentMonth = 0, currentPayStyle = 0, currentPosition = 0;
	private String[] payStyleArray = new String[]{"\u652f\u4ed8\u5b9d\u652f\u4ed8", "\u652f\u4ed8\u5b9d\u626b\u7801\u652f\u4ed8", "\u5fae\u4fe1\u652f\u4ed8", "\u5fae\u4fe1\u626b\u7801\u652f\u4ed8"};
	private int leftId[] = {R.drawable.ic_alipay, R.drawable.ic_alipay,
			R.drawable.ic_wecat, R.drawable.ic_wecat};

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		setContentView( R.layout.activity_opening_vip );
		ViewUtils.inject( this );
		initPayStyle();
		initView();
	}

	private void initView() {
		titleBarView.setTitleText( "\u8d85\u7ea7\u4f1a\u5458" );
		titleBarView.setLeftImg( R.drawable.icon_xqf_b );
		monthAdapter = new CommonAdapter<VipModel>( this, vipModels,
				R.layout.item_vip_month ) {
			@Override
			public void convert(final CommonViewHolder holder, VipModel t) {
				TextView view = holder.getView( R.id.id_vip_month );
				view.setText( t.getMonth() + "\u4e2a\u6708" );
				view.setSelected( currentMonth == holder.getPosition() );
			}
		};
		gridView.setAdapter( monthAdapter );
		gridView.setOnItemClickListener( (parent, view, position, id) -> {
			if (vipModels.size() > 0) {
				currentPosition = position;
				currentMonth = position;
				monthAdapter.notifyDataSetChanged();
				initTextView( vipModels.get( position ) );
			}
		} );
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
						(currentPayStyle == holder.getPosition() ? rightDrawable : null), null );
			}
		};
		listView.setAdapter( payStyleAdapter );
		listView.setOnItemClickListener( (parent, view, position, id) -> {
			if (payStyle.size() > 0) {
				currentPayStyle = position;
				payStyleAdapter.notifyDataSetChanged();
				for (VipModel model : vipModels) {
					PayModel payModel = payStyle.get( currentPayStyle );
					model.setPayStyle( payModel.getPayStyle() );
				}
			}
		} );
		HttpManager.getVipOption( HttpType.REFRESH, this, this );
		button.setSelected( MyApplication.getInstance().isVip() );
	}

	@OnClick({R.id.id_titleBar_leftImg, R.id.id_vip_open})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.id_titleBar_leftImg:
				finish();
				break;
			case R.id.id_vip_open:
				if (MyApplication.getInstance().isVip()) {
					ToastCustom.makeText( this, "\u60a8\u5df2\u662fvip,\u4e0d\u7528\u91cd\u590d\u5f00\u901a",
							ToastCustom.LENGTH_SHORT ).show();
					return;
				}
				HttpManager.payReady( HttpType.LOADING, this, this );
				break;
		}
	}

	public void initPayStyle() {
		int[] array = {11, 1, 4, 3, 6};
		for (int i = 0; i < payStyleArray.length; i++) {
			PayModel model = new PayModel();
			model.setlId( leftId[i] );
			model.setName( payStyleArray[i] );
			model.setPayStyle( array[i] );
			payStyle.add( model );
		}
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		if (1 == resultItem.getIntValue( "status" )) {
			switch (what) {
				case HttpType.REFRESH:
					setData( resultItem );
					break;
				case HttpType.LOADING:
					startPay();
					break;
				case 28:
					TrackingUtils.setEvent( TrackingUtils.OPENVIPEVENT, TrackingUtils.getUserInfoMap() );
					ToastCustom.makeText( this, "\u53d1\u8d77\u6210\u529f", ToastCustom.LENGTH_SHORT )
							.show();
					ResultItem item = resultItem.getItem( "data" );
					String orderID = item.getString( "orderID" );
					String url = item.getString( "url" );
					String paymentType = (currentPayStyle == 0 || currentPayStyle == 1) ? "alipay" :
							(currentPayStyle == 2 || currentPayStyle == 3) ? "weixinpay" : "tencentpay";
					float currencyAmount = Float.valueOf( vipModels.get( currentPosition ).getMoney() ).floatValue();
					Intent intent = new Intent();
					intent.setClass( this, PayActivity.class );
					intent.putExtra( "url", url );
					intent.putExtra( "orderID", orderID );
					intent.putExtra( "paymentType", paymentType );
					intent.putExtra( "currencyAmount", currencyAmount );
					startActivity( intent );
					break;
			}
		} else {
			ToastCustom.makeText( this, resultItem.getString( "msg" ),
					ToastCustom.LENGTH_SHORT ).show( Gravity.BOTTOM );
		}
	}

	@Override
	public void onError(int what, String error) {
		ToastCustom.makeText( this, error, ToastCustom.LENGTH_SHORT ).show(
				Gravity.BOTTOM );
	}

	private void setData(ResultItem resultItem) {
		List<ResultItem> items = resultItem.getItems( "data" );
		if (items != null) {
			for (int i = 0; i < items.size(); i++) {
				ResultItem item = items.get( i );
				VipModel model = new VipModel();
				model.setMoney( item.getString( "money" ) );
				model.setMonth( item.getString( "month" ) );
				model.setProductID( item.getString( "productID" ) );
				model.setPtb( item.getString( "ptb" ) );
				model.setOldPrice( item.getString( "costMoney" ) );
				model.setVipCoin( item.getString( "coin" ) );
				vipModels.add( model );
			}
			if (vipModels.size() > 0) {
				VipModel model = vipModels.get( 0 );
				model.setPayStyle( payStyle.get( 0 ).getPayStyle() );
				initTextView( model );
			}
			monthAdapter.notifyDataSetChanged();
		}
	}

	@SuppressLint("StringFormatMatches")
	private void initTextView(VipModel model) {
		textView.setText( getResources().getString( R.string.vip_text,
				model.getPtb(), model.getVipCoin() ) );
		priceTv.setText( model.getMoney() + "\u5143" );
		Paint paint = oldPriceTv.getPaint();
		paint.setFlags( Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG );
		paint.setAntiAlias( true );
		oldPriceTv.setText( "\u539f\u4ef7:" + model.getOldPrice() + "\u5143" );
	}

	private void startPay() {
		HttpManager.startPay( 28, this, this, vipModels.get( currentMonth ) );
	}
}
