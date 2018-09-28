package com.tenone.gamebox.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.PurchaseModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.JrttUtils;
import com.tenone.gamebox.view.utils.TrackingUtils;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint({"SetJavaScriptEnabled", "HandlerLeak"})
public class PayActivity extends BaseActivity {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_pay_rootView)
	LinearLayout rootView;

	private String url, orderID, paymentType;
	private float currencyAmount = 0;
	private WebView webView;
	private Context context;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		setContentView( R.layout.activity_pay );
		context = this;
		ViewUtils.inject( this );
		url = getIntent().getExtras().getString( "url" );
		orderID = getIntent().getExtras().getString( "orderID" );
		paymentType = getIntent().getExtras().getString( "paymentType" );
		currencyAmount = getIntent().getExtras().getFloat( "currencyAmount" );
		initView();
	}

	private void initView() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT );
		webView = new WebView( getApplicationContext() );
		webView.setLayoutParams( params );
		rootView.addView( webView );
		titleBarView.setTitleText( "\u652f\u4ed8" );
		titleBarView.setLeftImg( R.drawable.icon_back_grey );
		webView.getSettings().setAllowFileAccess( true ); 
		webView.getSettings().setBuiltInZoomControls( true );
		webView.getSettings().setDisplayZoomControls( false );
		webView.getSettings().setDefaultTextEncodingName( "utf-8" );
		webView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );
		webView.getSettings().setLoadsImagesAutomatically( true ); 
		webView.setHorizontalScrollBarEnabled( false );
		webView.setVerticalScrollBarEnabled( false );
		webView.getSettings().setJavaScriptEnabled( true );
		
		
		
		webView.getSettings().setLayoutAlgorithm( LayoutAlgorithm.SINGLE_COLUMN );
		webView.getSettings().setUseWideViewPort( true );
		webView.getSettings().setLoadWithOverviewMode( true );
		webView.loadUrl( url );
		new QueryThread( orderID ).start();
		webView.setWebViewClient( new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith( "http:" ) || url.startsWith( "https:" )) {
					view.loadUrl( url );
					return true;
				}
				try {
					Intent intent = new Intent( Intent.ACTION_VIEW, Uri
							.parse( url ) );
					startActivity( intent );
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		} );
	}

	@OnClick({R.id.id_titleBar_leftImg})
	public void onClick(View view) {
		if (view.getId() == R.id.id_titleBar_leftImg) {
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		webView.loadDataWithBaseURL( null, "", "text/html", "utf-8", null );
		webView.clearHistory();
		((ViewGroup) webView.getParent()).removeView( webView );
		webView.destroy();
		webView = null;
		super.onDestroy();
	}

	
	private class QueryThread extends Thread implements HttpResultListener {
		String orderID;
		Timer timer;
		TimerTask task;

		public QueryThread(String id) {
			this.orderID = id;
		}

		@Override
		public void run() {
			timer = new Timer();
			task = new TimerTask() {
				@Override
				public void run() {
					HttpManager.payQuery( HttpType.REFRESH,
							getApplicationContext(), QueryThread.this, orderID );
				}
			};
			timer.schedule( task, 3000, 5000 );
			super.run();
		}

		@Override
		public void onSuccess(int what, ResultItem resultItem) {
			if (1 == resultItem.getIntValue( "status" )) {
				ResultItem item = resultItem.getItem( "data" );
				String url = item.getString( "url" );
				if (item.getIntValue( "order_status" ) == 1
						|| item.getIntValue( "order_status" ) == 2) {
					cancleTimer();
					loadWeb( url );
				}
			} else {
				JrttUtils.jrttReportPay( new PurchaseModel( "open_vip", "vip", orderID,
						1, paymentType, "CNY", false, Float.valueOf( currencyAmount ).intValue() ) );
			}
		}

		private void loadWeb(String url) {
			Message message = new Message();
			message.obj = url;
			handler.sendMessage( message );
		}

		private void cancleTimer() {
			if (timer != null) {
				timer.cancel();
			}

			if (task != null) {
				task.cancel();
			}
			timer = null;
			task = null;
		}

		@Override
		public void onError(int what, String error) {
			ToastCustom.makeText( context, error, ToastCustom.LENGTH_SHORT )
					.show( Gravity.BOTTOM );
		}
	}

	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			if (webView != null) {
				webView.loadUrl( (String) msg.obj );
				TrackingUtils.setPayment( orderID, paymentType, "CNY", currencyAmount );
				JrttUtils.jrttReportPay( new PurchaseModel( "open_vip", "vip", orderID,
						1, paymentType, "CNY", true, Float.valueOf( currencyAmount ).intValue() ) );
			}
			super.dispatchMessage( msg );
		}
	};
}
