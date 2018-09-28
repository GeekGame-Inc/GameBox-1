package com.tenone.gamebox.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("ResourceAsColor")
public class BindMobileActivity extends BaseActivity implements
		HttpResultListener {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_bind_getCode)
	TextView getCodeTv;
	@ViewInject(R.id.id_bind_mobileEt)
	EditText mobileEt;
	@ViewInject(R.id.id_bind_code)
	EditText codeEt;
	@ViewInject(R.id.id_bind_bind)
	Button button;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
				case GETGODE:
					if (time > 1) {
						time--;
						getCodeTv.setText("(" + time + "s)\u91cd\u65b0\u83b7\u53d6" );
					}
					break;
				case CANCLE:
					cancleTimer();
					time = 60;
					getCodeTv.setClickable( true );
					getCodeTv.setSelected( true );
					getCodeTv.setText( getString( R.string.get_again ) );
					break;
			}
			super.dispatchMessage( msg );
		}
	};

	private static final int GETGODE = 0;
	private static final int CANCLE = 1;
	private static final int BINDORUNBIND = 2;
	private int time = 60;
	private Timer timer;
	private TimerTask task;
	private String mobile;

	private boolean isBind = true;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		setContentView( R.layout.activity_mobile_bind );
		ViewUtils.inject( this );
		isBind = "bind"
				.equals( TextUtils.isEmpty( getIntent().getAction() ) ? "null"
						: getIntent().getAction() );
		getCodeTv.setSelected( true );
		initTitle();
		initView();
	}

	private void initTitle() {
		titleBarView.setLeftImg( R.drawable.icon_back_grey );
		titleBarView.setTitleText(isBind ? "\u7ed1\u5b9a\u624b\u673a" : "\u89e3\u7ed1\u624b\u673a\u53f7" );
	}

	private void initView() {
		mobileEt.setHint( "\u8bf7\u8f93\u5165\u8981" + (isBind ? "\u7ed1\u5b9a" : "\u89e3\u7ed1\u7684") + "\u7684\u624b\u673a\u53f7" );
		button.setText( "\u7acb\u5373" + (isBind ? "\u7ed1\u5b9a" : "\u89e3\u7ed1"));
	}

	@OnClick({R.id.id_bind_bind, R.id.id_titleBar_leftImg,
			R.id.id_bind_getCode})
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.id_bind_bind:
				mobile = mobileEt.getText().toString();
				if (TextUtils.isEmpty( mobile )) {
					ToastUtils.showToast( this, "\u8bf7\u8f93\u5165" + (isBind ? "\u7ed1\u5b9a" : "\u89e3\u7ed1") + "\u7684\u624b\u673a\u53f7" );
					return;
				}
				String code = codeEt.getText().toString();
				if (TextUtils.isEmpty( code )) {
					ToastUtils.showToast( this, "\u8bf7\u8f93\u5165\u9a8c\u8bc1\u7801" );
					return;
				}
				bindOrUnbingMobile( mobile, code );
				break;
			case R.id.id_titleBar_leftImg:
				finish();
				break;
			case R.id.id_bind_getCode:
				mobile = mobileEt.getText().toString();
				if (TextUtils.isEmpty( mobile )) {
					ToastUtils.showToast( this, "\u8bf7\u8f93\u5165" + (isBind ? "\u7ed1\u5b9a" : "\u89e3\u7ed1") + "\u7684\u624b\u673a\u53f7" );
					return;
				}
				getCode( mobile );
				timeFun();
				break;
		}
	}

	private void getCode(String mobile) {
		HttpManager.getCode( GETGODE, this, this, mobile, isBind ? 3 : 4 );
	}

	private void bindOrUnbingMobile(String mobile, String code) {
		HttpManager.bindOrUnbingMobile( BINDORUNBIND, this, this, mobile, code,
				isBind );
	}

	private void timeFun() {
		if (60 == time) {
			getCodeTv.setClickable( false );
			getCodeTv.setSelected( false );
			timer = new Timer();
			task = new TimerTask() {
				@Override
				public void run() {
					if (time > 1) {
						handler.sendEmptyMessage( GETGODE );
					} else {
						handler.sendEmptyMessage( CANCLE );
					}
				}
			};
			timer.schedule( task, 0, 1000 );
		}
	}

	private void cancleTimer() {
		if (task != null) {
			task.cancel();
		}
		if (timer != null) {
			timer.cancel();
		}
		task = null;
		timer = null;
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		if (1 == resultItem.getIntValue( "status" )) {
			switch (what) {
				case GETGODE:
					ToastCustom.makeText(this, "\u9a8c\u8bc1\u7801\u5df2\u53d1\u9001,\u8bf7\u6ce8\u610f\u67e5\u6536",
							ToastCustom.LENGTH_SHORT).show();
					break;
				case BINDORUNBIND:
					ToastCustom.makeText(this,
							"\u624b\u673a\u53f7" + (isBind ? "\u7ed1\u5b9a" : "\u89e3\u7ed1") + "\u6210\u529f",
							ToastCustom.LENGTH_SHORT).show();
					SpUtil.setPhone( isBind ? mobile : "" );
					setResult( RESULT_OK );
					finish();
					break;
			}
		} else {
			ToastCustom.makeText( this, resultItem.getString( "msg" ),
					ToastCustom.LENGTH_SHORT ).show();
		}
	}

	@Override
	public void onError(int what, String error) {
		ToastCustom.makeText( this, error, ToastCustom.LENGTH_SHORT ).show();
	}
}
