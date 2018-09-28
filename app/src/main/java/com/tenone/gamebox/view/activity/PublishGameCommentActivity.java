/** 
 * Project Name:GameBox 
 * File Name:PublishGameComment.java 
 * Package Name:com.tenone.gamebox.view.activity 
 * Date:2017-5-2����1:55:29 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.PublishGameCommentView;
import com.tenone.gamebox.presenter.PublishGameCommentPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;

public class PublishGameCommentActivity extends BaseActivity implements
		PublishGameCommentView {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_publishGame_editText)
	CustomizeEditText customizeEditText;
	PublishGameCommentPresenter presenter;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_publish_comment_game);
		ViewUtils.inject(this);
		presenter = new PublishGameCommentPresenter(this, this);
		presenter.initView();
		presenter.initListener();
	}

	@Override
	public TitleBarView getTitleBarView() {
		return titleBarView;
	}

	@Override
	public CustomizeEditText getEditText() {
		return customizeEditText;
	}
}
