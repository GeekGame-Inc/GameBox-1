package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.SpUtil;


public class UserIntroActivity extends BaseActivity implements TextWatcher {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_userIntro_edit)
	CustomizeEditText editText;
	@ViewInject(R.id.id_userIntro_num)
	TextView numTv;
	@ViewInject(R.id.id_userIntro_text)
	TextView textView;

	private String intro;
	private String uid;
	private boolean isUs = false;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		intro = getIntent().getExtras().getString( "intro" );
		uid = getIntent().getExtras().getString( "uid" );
		isUs = SpUtil.getUserId().equals( uid );
		super.onCreate( arg0 );
		setContentView( R.layout.activity_user_intro );
		ViewUtils.inject( this );
		initTitle();
		initView();
	}


	private void initTitle() {
		titleBarView.setTitleText( isUs ? "\u7f16\u8f91\u7b80\u4ecb" : "\u53f8\u673a\u7b80\u4ecb" );
		titleBarView.setLeftImg( R.drawable.icon_back_grey );
		if (isUs) {
			titleBarView.setRightText( "\u4fdd\u5b58" );
			titleBarView.setOnClickListener( R.id.id_titleBar_rightText, v -> {
				intro = editText.getText().toString();
				if (TextUtils.isEmpty( intro )) {
					showToast( "\u8bf7\u8f93\u5165\u4f60\u7684\u4e2a\u4eba\u7b80\u4ecb" );
					return;
				}
				setResult( RESULT_OK, getIntent().putExtra( "intro", intro ) );
				finish();
			} );
		}
	}

	@OnClick({R.id.id_titleBar_leftImg})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.id_titleBar_leftImg:
				finish();
				break;
		}
	}

	private void initView() {
		if (!TextUtils.isEmpty( intro )) {
			editText.setText( intro );
			editText.setSelection( intro.length() );
			numTv.setText( intro.length() + "/30" );
		}
		if (!isUs) {
			numTv.setVisibility( View.GONE );
			textView.setVisibility( View.GONE );
			editText.setFocusable( false );
			editText.setHint( "\u6682\u65f6\u6ca1\u6709\u7b80\u4ecb" );
			editText.setClickable( false );
		}
		editText.addTextChangedListener( this );
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {

		if (!TextUtils.isEmpty( s )) {
			numTv.setText( s.length() + "/30" );
		}
	}
}
