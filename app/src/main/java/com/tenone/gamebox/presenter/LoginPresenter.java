

package com.tenone.gamebox.presenter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.LoginBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.LoginView;
import com.tenone.gamebox.view.activity.FindPwdActivity;
import com.tenone.gamebox.view.activity.RegisterActivity;
import com.tenone.gamebox.view.base.Constant;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.FileUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.JrttUtils;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TrackingUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginPresenter extends BasePresenter implements OnClickListener,
		HttpResultListener {

	private LoginBiz loginBiz;
	private LoginView loginView;
	private Context mContext;
	private String account;
	private String pwd;

	public LoginPresenter(Context cxt, LoginView view) {
		this.mContext = cxt;
		this.loginView = view;
		this.loginBiz = new LoginBiz();
	}

	
	public void initView() {
		getTitleBarView().setLeftImg( R.drawable.icon_xqf_b );
		getTitleBarView().setTitleText( "" );
		getTitleBarView().setRightText( mContext.getString( R.string.register ) );
		account = SpUtil.getAccount();
		pwd = SpUtil.getPwd();
		if (!TextUtils.isEmpty( account )) {
			getAccountView().setText( account );
			getAccountView().setSelection( account.length() );
		}
		if (!TextUtils.isEmpty( pwd )) {
			getAccountView().clearFocus();
			getPwdView().requestFocus();
			getPwdView().setText( pwd );
			getPwdView().setSelection( pwd.length() );
		}
	}

	
	public void initListener() {
		getTitleBarView().getLeftImg().setOnClickListener( this );
		getLoginView().setOnClickListener( this );
		getForgetView().setOnClickListener( this );
		getTitleBarView().getRightText().setOnClickListener( this );
	}

	public TitleBarView getTitleBarView() {
		return loginView.getTitleBarView();
	}

	public CustomizeEditText getAccountView() {
		return loginView.getAccountView();
	}

	public CustomizeEditText getPwdView() {
		return loginView.getPwdView();
	}

	public Button getLoginView() {
		return loginView.getLoginView();
	}

	public TextView getForgetView() {
		return loginView.getForgetView();
	}

	private boolean verification() {
		return loginBiz.verification( mContext, getAccountView(), getPwdView() );
	}

	
	public void requestHttp(int what, String userName, String passWord) {
		buildProgressDialog( mContext );
		getLoginView().setClickable( false );
		getLoginView().setFocusable( false );
		HttpManager.login( what, mContext, this, userName, passWord );

	}

	public void onActivityResult(int arg0, int arg1, Intent arg2) {
		switch (arg0) {
			case 2:
				if (arg1 == Activity.RESULT_OK) {
					((Activity) mContext).setResult( Activity.RESULT_OK );
					close( mContext );
				}
				break;
		}
	}

	AlertDialog alertDialog;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.id_titleBar_leftImg:
				close( mContext );
				break;
			case R.id.id_titleBar_rightText:
				openOtherActivityForResult( mContext, 2, new Intent(
						mContext, RegisterActivity.class ).setAction( "2" ) );
				break;
			case R.id.id_login_loginBt:
				if (verification()) {
					account = getAccountView().getText().toString();
					pwd = getPwdView().getText().toString();
					alertDialog = buildProgressDialog( mContext );
					requestHttp( HttpType.REFRESH, account, pwd );
				}
				break;
			case R.id.id_login_forgetTv:
				openOtherActivityForResult( mContext, 3, new Intent(
						mContext, FindPwdActivity.class ) );
				break;
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		cancelProgressDialog();
		getLoginView().setClickable( true );
		getLoginView().setFocusable( true );
		cancelProgressDialog( alertDialog );
		if (1 == resultItem.getIntValue( "status" )) {
			Message message = new Message();
			message.what = what;
			message.obj = resultItem;
			handler.sendMessage( message );
		} else {
			showToast( mContext, resultItem.getString( "msg" ) );
		}
	}

	@Override
	public void onError(int what, String error) {
		cancelProgressDialog();
		getLoginView().setClickable( true );
		getLoginView().setFocusable( true );
		cancelProgressDialog( alertDialog );
		showToast( mContext, error );
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ResultItem resultItem = (ResultItem) msg.obj;
			if (msg.what == HttpType.REFRESH) {
				if (resultItem != null) {
					ResultItem item = resultItem.getItem( "data" );
					if (BeanUtils.isEmpty( item )) {
						return;
					}
					Constant.setIsVip( item.getBooleanValue( "is_vip", 1 ) );
					final String uId = item.getString( "id" );
					SpUtil.setUserId( uId );
					final String account = item.getString( "username" );
					SpUtil.setAccount( account );
					final String pwd = getPwdView().getText().toString();
					SpUtil.setPwd( pwd );
					String header = item.getString( "icon_url" );
					SpUtil.setHeaderUrl( header );
					String coin = item.getString( "coin" );
					MyApplication.getInstance().setCoin( coin );
					String platform = item.getString( "platform_money" );
					MyApplication.getInstance().setPlatform( platform );
					String bonus = item.getString( "recom_bonus" );
					MyApplication.getInstance().setRecom_bonus( bonus );
					final String phone = item.getString( "mobile" );
					SpUtil.setPhone( phone );
					TrackingUtils.setLoginSuccessBusiness( uId );
					if (Build.VERSION.SDK_INT > 21 && ContextCompat.checkSelfPermission( mContext,
							Manifest.permission.READ_PHONE_STATE )
							!= PackageManager.PERMISSION_GRANTED) {
						ActivityCompat.requestPermissions( (Activity) mContext,
								new String[]{Manifest.permission.READ_PHONE_STATE,}, 3421 );
					} else {
						JrttUtils.jrttReportData( (BeanUtils.isMatchered( BeanUtils.PHONE_PATTERN, account )) ?
								JrttUtils.MOBILELOGIN : JrttUtils.ACCOUNTLOGIN );
					}
					new Thread() {
						public void run() {
							JSONObject object = new JSONObject();
							try {
								object.put( "userName", account );
								object.put( "pwd", pwd );
								object.put( "phone", phone );
								object.put( "uid", uId );
								FileUtils
										.saveAccountNew( mContext, object.toString() );
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}.start();
					String nickName = item.getString( "nick_name" );
					SpUtil.setNick( nickName );
					SpUtil.setExit( false );
					((Activity) mContext).setResult( Activity.RESULT_OK );
					ListenerManager.sendOnLoginStateChange( true );
					close( mContext );
				}
			}
		}
	};
}
