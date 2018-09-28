

package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.WebActivityView;
import com.tenone.gamebox.presenter.WebPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.ProgressWebView;
import com.tenone.gamebox.view.custom.TitleBarView;


public class WebActivity extends BaseActivity implements WebActivityView {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_webView)
	ProgressWebView webView;

	private WebPresenter presenter;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_web);
		ViewUtils.inject(this);
		presenter = new WebPresenter(this, this);
		presenter.initView();
		presenter.initWeb();
		presenter.initListener();
	}

	@Override
	public TitleBarView getTitleBarView() {
		return titleBarView;
	}

	@Override
	public ProgressWebView getWebView() {
		return webView;
	}

	@Override
	protected void onDestroy() {
		webView.loadUrl("http://blank");
		webView.destroy();
		webView = null;
		super.onDestroy();
	}
}
