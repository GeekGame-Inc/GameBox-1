/** 
 * Project Name:GameBox 
 * File Name:AboutPresenter.java 
 * Package Name:com.tenone.gamebox.presenter 
 * Date:2017-3-24����5:33:02 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.presenter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.AboutBiz;
import com.tenone.gamebox.mode.view.AboutView;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.TitleBarView;

public class AboutPresenter extends BasePresenter implements OnClickListener {
	AboutBiz aboutBiz;
	AboutView aboutView;
	Context mContext;

	public AboutPresenter(AboutView v, Context cxt) {
		this.aboutBiz = new AboutBiz();
		this.aboutView = v;
		this.mContext = cxt;
	}

	public void initView() {
		getTitleBarView().setTitleText(R.string.aboutUs);
		getTitleBarView().setLeftImg(R.drawable.icon_back_grey);
		getVersionTextView().setText(getVersion());
		getWeboTextView().setText(getWebo());
		getWebTextView().setText(getWeb());
		getWeChatTextView().setText(getWeChat());
	}

	public void initListener() {
		getTitleBarView().getLeftImg().setOnClickListener(this);
	}

	public TitleBarView getTitleBarView() {
		return aboutView.getTitleBarView();
	}

	public TextView getVersionTextView() {
		return aboutView.getVersionTextView();
	}

	public TextView getWebTextView() {
		return aboutView.getWebTextView();
	}

	public TextView getWeboTextView() {
		return aboutView.getWeboTextView();
	}

	public TextView getWeChatTextView() {
		return aboutView.getWeChatTextView();
	}

	public String getVersion() {
		return aboutBiz.getVersion(mContext);
	}

	public String getWeb() {
		return aboutBiz.getWeb();
	}

	public String getWebo() {
		return aboutBiz.getWebo();
	}

	public String getWeChat() {
		return aboutBiz.getWeChat();
	}

	private void close() {
		((BaseActivity) mContext).finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_titleBar_leftImg:
			close();
			break;
		}
	}
}
