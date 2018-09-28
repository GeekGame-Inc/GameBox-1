

package com.tenone.gamebox.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.LoginView;
import com.tenone.gamebox.presenter.LoginPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.JrttUtils;


public class LoginActivity extends BaseActivity implements LoginView {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBar;
	@ViewInject(R.id.id_login_account)
	CustomizeEditText accountET;
	@ViewInject(R.id.id_login_pwd)
	CustomizeEditText pwdET;
	@ViewInject(R.id.id_login_loginBt)
	Button loginBT;
	@ViewInject(R.id.id_login_forgetTv)
	TextView forgetTv;
	@ViewInject(R.id.id_title_underline)
	View view;

	private LoginPresenter presenter;
	public static LoginActivity instance;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		setContentView( R.layout.activity_login );
		ViewUtils.inject( this );
		instance = this;
		view.setVisibility( View.GONE );
		presenter = new LoginPresenter( this, this );
		presenter.initView();
		presenter.initListener();
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
	public Button getLoginView() {
		return loginBT;
	}

	@Override
	public TextView getForgetView() {
		return forgetTv;
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		presenter.onActivityResult( arg0, arg1, arg2 );
		super.onActivityResult( arg0, arg1, arg2 );
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult( requestCode, permissions, grantResults );
		if (requestCode == 3421) {
			JrttUtils.jrttReportData( (BeanUtils.isMatchered( BeanUtils.PHONE_PATTERN, accountET.getText().toString() )) ?
					JrttUtils.MOBILELOGIN : JrttUtils.ACCOUNTLOGIN );
		}
	}
}
