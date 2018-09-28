

package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.WebBiz;
import com.tenone.gamebox.mode.view.WebActivityView;
import com.tenone.gamebox.view.activity.MyPrizesActivity;
import com.tenone.gamebox.view.custom.ProgressWebView;
import com.tenone.gamebox.view.custom.TitleBarView;


@SuppressLint("SetJavaScriptEnabled")
public class WebPresenter extends BasePresenter implements OnClickListener {
    WebBiz webBiz;
    WebActivityView activityView;
    Context mContext;
    boolean isLucky = false;

    public WebPresenter(Context cxt, WebActivityView view) {
        this.mContext = cxt;
        this.activityView = view;
        this.webBiz = new WebBiz();
    }

    public void initView() {
        isLucky = getIntent().getStringExtra( "url" ).contains( "luckydraw" );
        getTitleBarView().setTitleText( getTitle() );
        getTitleBarView().setLeftImg( R.drawable.icon_xqf_b );
        if (isLucky) {
            getTitleBarView().setRightText( "\u6211\u7684\u5956\u54c1" );
            getTitleBarView().getRightText().setOnClickListener( this );
        }
    }

    public void initWeb() {
        getWebView().getSettings().setBuiltInZoomControls( !isLucky );
        getWebView().getSettings().setDisplayZoomControls( false );
        getWebView().getSettings().setCacheMode( WebSettings.LOAD_NO_CACHE );
        getWebView().setHorizontalScrollBarEnabled( false );
        getWebView().setVerticalScrollBarEnabled( false );
        getWebView().getSettings().setJavaScriptEnabled( true );
        
        getWebView().getSettings().setLayoutAlgorithm(
                LayoutAlgorithm.SINGLE_COLUMN );
        getWebView().getSettings().setUseWideViewPort( true );
        getWebView().getSettings().setLoadWithOverviewMode( true );
        getWebView().loadUrl( getUrl() );
        getWebView().setWebViewClient( new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl( url );
                return true;
            }
        } );
        getWebView().setDownloadListener( (url, userAgent, contentDisposition, mimetype, contentLength) -> {
            if (url != null && url.startsWith( "http://" ))
                mContext.startActivity( new Intent( Intent.ACTION_VIEW, Uri
                        .parse( url ) ) );
        } );
    }

    public void initListener() {
        getTitleBarView().getLeftImg().setOnClickListener( this );
    }

    public TitleBarView getTitleBarView() {
        return activityView.getTitleBarView();
    }

    public ProgressWebView getWebView() {
        return activityView.getWebView();
    }

    public Intent getIntent() {
        return activityView.getIntent();
    }

    public String getUrl() {
        return webBiz.getUrl( getIntent() );
    }

    public String getTitle() {
        return webBiz.getTitle( getIntent() );
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_titleBar_leftImg) {
            close( mContext );
        }

        if (v.getId() == R.id.id_titleBar_rightText) {
            openOtherActivity( mContext, new Intent( mContext,
                    MyPrizesActivity.class ) );
        }
    }

}
