package com.tenone.gamebox.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.TitleBarView;

public class TradingNoticeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBarView;
    @ViewInject(R.id.id_trading_notice_root)
    SwipeRefreshLayout rootView;

    private String url;
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_trading_notice );
        ViewUtils.inject( this );
        url = MyApplication.getHttpUrl().getTradeNotesH5();
        initTitle();
        initView();
    }

    private void initTitle() {
        titleBarView.setTitleText( "\u4ea4\u6613\u987b\u77e5" );
        titleBarView.setLeftImg( R.drawable.icon_xqf_b );
        titleBarView.setOnClickListener( R.id.id_titleBar_leftImg, v -> finish() );
    }

    @Override
    public void onRefresh() {
        webView.loadUrl( url );
    }

    private void initView() {
        rootView.setRefreshing( true );
        rootView.setOnRefreshListener( this );
        webView = new WebView( this );
        rootView.addView( webView );
        ViewGroup.LayoutParams params = webView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        webView.setLayoutParams( params );
        webView.getSettings().setBuiltInZoomControls( false );
        webView.getSettings().setDisplayZoomControls( false );
        webView.getSettings().setCacheMode( WebSettings.LOAD_NO_CACHE );
        webView.setHorizontalScrollBarEnabled( false );
        webView.setVerticalScrollBarEnabled( false );
        webView.getSettings().setJavaScriptEnabled( true );
        
        webView.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN );
        webView.getSettings().setUseWideViewPort( true );
        webView.getSettings().setLoadWithOverviewMode( true );
        webView.loadUrl( url );
        webView.setWebViewClient( new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl( url );
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished( view, url );
                rootView.setRefreshing( false );
            }
        } );
        webView.setDownloadListener( (url, userAgent, contentDisposition, mimetype, contentLength) -> {
            if (url != null && url.startsWith( "http://" ))
                rootView.setRefreshing( false );
            startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse( url ) ) );
        } );
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            rootView.removeView( webView );
            webView.removeAllViews();
            webView.destroy();
        }
        super.onDestroy();
    }
}
