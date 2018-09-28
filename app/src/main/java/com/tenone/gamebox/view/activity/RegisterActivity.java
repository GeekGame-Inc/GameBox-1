/**
 * Project Name:GameBox
 * File Name:RegisterActivity.java
 * Package Name:com.tenone.gamebox.view.activity
 * Date:2017-3-15����5:23:12
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.RegisterView;
import com.tenone.gamebox.presenter.RegisterPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.JrttUtils;

/**
 * ע�� ClassName:RegisterActivity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-15 ����5:23:12 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */

public class RegisterActivity extends BaseActivity implements RegisterView {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBar;
	@ViewInject(R.id.id_register_account)
	CustomizeEditText accountET;
	@ViewInject(R.id.id_register_pwd)
	CustomizeEditText pwdET;
	@ViewInject(R.id.id_register_phone)
	CustomizeEditText phoneET;
	@ViewInject(R.id.id_register_code)
	CustomizeEditText codeET;
	@ViewInject(R.id.id_register_sendCodeBt)
	TextView sendCodeBt;
	@ViewInject(R.id.id_register_registerBt)
	Button registerBt;

	@ViewInject(R.id.id_register_layout2)
	LinearLayout layout2;
	@ViewInject(R.id.id_register_layout1)
	LinearLayout layout1;
	@ViewInject(R.id.id_register_mobilePwd)
	CustomizeEditText mobilePwd;
	@ViewInject(R.id.id_register_agreementCheck)
	CheckBox checkBox;
	private RegisterPresenter presenter;
	private String type = "";

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		setContentView( R.layout.activity_register );
		ViewUtils.inject( this );
		type = getIntent().getAction();
		layout2.setVisibility( "1".equals( type ) ? View.GONE : View.VISIBLE );
		layout1.setVisibility( "2".equals( type ) ? View.GONE : View.VISIBLE );
		presenter = new RegisterPresenter( this, this, type );
		presenter.initView();
		presenter.initListener();
	}

	@OnClick({R.id.id_register_agreement})
	public void onClick(View view) {
		if (R.id.id_register_agreement == view.getId()) {
			startActivity( new Intent( this, WebActivity.class )
					.putExtra( "title", "�û�Э��" )
					.putExtra( "url", MyApplication.getHttpUrl().getAgreement() ) );
		}
	}

	@Override
	public TitleBarView getTitleBarView() {
		return titleBar;
	}

	@Override
	public CustomizeEditText getAccountView() {
		return accountET;
	}

	@Override
	public CustomizeEditText getPwdView() {
		return pwdET;
	}

	@Override
	public CustomizeEditText getPhoneView() {
		return phoneET;
	}

	@Override
	public CustomizeEditText getCodeView() {
		return codeET;
	}

	@Override
	public TextView getSendCodeView() {
		return sendCodeBt;
	}

	@Override
	public Button getRegisterView() {
		return registerBt;
	}

	@Override
	public CheckBox getCheckBox() {
		return checkBox;
	}

	@Override
	public LinearLayout getLayout1() {
		return layout1;
	}

	@Override
	public LinearLayout getLayout2() {
		return layout2;
	}

	@Override
	public CustomizeEditText getPwdView2() {
		return mobilePwd;
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult( arg0, arg1, arg2 );
		if (RESULT_OK == arg1) {
			setResult( RESULT_OK );
			finish();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult( requestCode, permissions, grantResults );
		if (requestCode == 4567) {
			JrttUtils.jrttReportData( (!"1".equals( type )) ? JrttUtils.ACCOUNTREGISTER : JrttUtils.MOBILEREGISTER );
		}
	}
}
