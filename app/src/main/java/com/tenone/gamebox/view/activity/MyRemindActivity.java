/** 
 * Project Name:GameBox 
 * File Name:MyRemindActivity.java 
 * Package Name:com.tenone.gamebox.view.activity 
 * Date:2017-3-29上午11:41:08 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.MyRemindView;
import com.tenone.gamebox.presenter.MyRemindPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * 我的提醒 ClassName:MyRemindActivity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-29 上午11:41:08 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class MyRemindActivity extends BaseActivity implements MyRemindView {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_myRemind_listView)
	ListView listView;

	MyRemindPresenter presenter;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_myremind);
		ViewUtils.inject(this);
		presenter = new MyRemindPresenter(this, this);
		presenter.initView();
		presenter.setAdapter();
		presenter.initListener();
	}

	@Override
	public TitleBarView getTitleBarView() {
		return titleBarView;
	}

	@Override
	public ListView getListView() {
		return listView;
	}
}
