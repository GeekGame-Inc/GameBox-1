package com.tenone.gamebox.mode.view;

import android.widget.Button;
import android.widget.TextView;

import com.tenone.gamebox.view.custom.SignInView;
import com.tenone.gamebox.view.custom.TitleBarView;

public interface SignInSignInView {
	TitleBarView titleBarView();

	Button button();

	TextView textview1();

	TextView textview2();

	TextView textview3();

	SignInView signInView();
}
