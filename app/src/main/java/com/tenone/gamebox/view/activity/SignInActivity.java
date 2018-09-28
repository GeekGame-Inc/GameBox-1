package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.SignInSignInView;
import com.tenone.gamebox.presenter.SignInPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.SignInView;
import com.tenone.gamebox.view.custom.TitleBarView;

public class SignInActivity extends BaseActivity implements SignInSignInView {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_signin_button)
	Button button;
	@ViewInject(R.id.id_signin_signNum)
	TextView textView1;
	@ViewInject(R.id.id_signin_ereyDayTv)
	TextView textView2;
	@ViewInject(R.id.id_signin_addTv)
	TextView textView3;
	@ViewInject(R.id.id_signin_signView)
	SignInView signInView;
	private SignInPresenter presenter;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_signin);
		ViewUtils.inject(this);
		presenter = new SignInPresenter(this, this);
		presenter.intView();
	}

	@Override
	public TitleBarView titleBarView() {
		return titleBarView;
	}

	@Override
	public Button button() {
		return button;
	}

	@Override
	public TextView textview1() {
		return textView1;
	}

	@Override
	public TextView textview2() {
		return textView2;
	}

	@Override
	public TextView textview3() {
		return textView3;
	}

	@Override
	public SignInView signInView() {
		return signInView;
	}
}
