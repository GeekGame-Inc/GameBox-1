/**
 * Project Name:GameBox
 * File Name:RegisterPresenter.java
 * Package Name:com.tenone.gamebox.presenter
 * Date:2017-3-15����5:45:44
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.presenter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.RegisterBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.StatisticActionEnum;
import com.tenone.gamebox.mode.view.RegisterView;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.FileUtils;
import com.tenone.gamebox.view.utils.GDTActionUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.JrttUtils;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.ToastUtils;
import com.tenone.gamebox.view.utils.TrackingUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ClassName:RegisterPresenter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-15 ����5:45:44 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */
public class RegisterPresenter extends BasePresenter implements
		OnClickListener, HttpResultListener, CompoundButton.OnCheckedChangeListener {
	RegisterView registerView;
	RegisterBiz registerBiz;
	Context mContext;
	boolean isChecked = true;
	// ��֤��
	String code = "", type = "";

	public static final int GETCODE = 5;
	public static final int REGISTER = 9;

	public RegisterPresenter(RegisterView view, Context cxt, String type) {
		this.registerView = view;
		this.mContext = cxt;
		this.registerBiz = new RegisterBiz();
		this.type = type;
	}

	/**
	 * ��ʼ������ initView:(������һ�仰�����������������). <br/>
	 *
	 * @author John Lie
	 * @since JDK 1.6
	 */
	public void initView() {
		getTitleBarView().setLeftImg( R.drawable.icon_back_grey );
		getTitleBarView().setRightText( "1".equals( type ) ? "�ֻ���ע��" : "�û���ע��" );
		getTitleBarView().setTitleText( "1".equals( type ) ? "�û���ע��" : "�ֻ���ע��" );
	}

	/**
	 * ע����� initListener:(������һ�仰�����������������). <br/>
	 *
	 * @author John Lie
	 * @since JDK 1.6
	 */
	public void initListener() {
		getSendCodeView().setOnClickListener( this );
		getRegisterView().setOnClickListener( this );
		getTitleBarView().getLeftImg().setOnClickListener( this );
		getTitleBarView().getRightText().setOnClickListener( this );
		getCheckBox().setOnCheckedChangeListener( this );
	}

	/* ���� */
	TitleBarView getTitleBarView() {
		return registerView.getTitleBarView();
	}

	/* �˺� */
	public CustomizeEditText getAccountView() {
		return registerView.getAccountView();
	}

	/* ���� */
	public CustomizeEditText getPwdView() {
		return registerView.getPwdView();
	}

	/* �ֻ��� */
	public CustomizeEditText getPhoneView() {
		return registerView.getPhoneView();
	}

	/* ��֤�� */
	public CustomizeEditText getCodeView() {
		return registerView.getCodeView();
	}

	/* ������֤�� */
	public TextView getSendCodeView() {
		return registerView.getSendCodeView();
	}

	/* ע�� */
	public Button getRegisterView() {
		return registerView.getRegisterView();
	}

	public CustomizeEditText getPwdView2() {
		return registerView.getPwdView2();
	}

	private CheckBox getCheckBox() {
		return registerView.getCheckBox();
	}

	/* ��֤ */
	public boolean verification() {
		return registerBiz.verification( mContext, getAccountView(),
				"1".equals( type ) ? getPwdView() : getPwdView2(),
				getPhoneView(), getCodeView(), code, type );
	}

	/* �ı�button״̬ */
	public void changeButton() {
		registerBiz.changeButton( mContext, getSendCodeView(), getPhoneView() );
	}

	/**
	 * ע�� requestHttp:(������һ�仰�����������������). <br/>
	 *
	 * @param what
	 * @author John Lie
	 * @since JDK 1.6
	 */
	public void register(int what, String userName, String phone, String pwd,
											 String type, String code) {
		getRegisterView().setFocusable( false );
		getRegisterView().setClickable( false );
		buildProgressDialog( mContext );
		HttpManager.register( what, mContext, this, userName, pwd, code, phone,
				type );
	}

	/**
	 * ��ȡ��֤�� getCode:(������һ�仰�����������������). <br/>
	 *
	 * @param what
	 * @param phone
	 * @author John Lie
	 * @since JDK 1.6
	 */
	public void getCode(int what, String phone) {
		HttpManager.getCode( what, mContext, this, phone, 1 );
	}

	private String pwd;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.id_titleBar_rightText:
				type = type.equals( "1" ) ? "2" : "1";
				registerView.getLayout2().setVisibility( "1".equals( type ) ? View.GONE : View.VISIBLE );
				registerView.getLayout1().setVisibility( "2".equals( type ) ? View.GONE : View.VISIBLE );
				initView();
              /*  openOtherActivityForResult( mContext, 2,
                        new Intent( mContext, RegisterActivity.class )
                                .setAction( "1".equals( type ) ? "2" : "1" ) );*/
                /*List<Activity> activities = new ArrayList<>();
                activities.addAll( BeanUtils.getActivitiesByApplication( ((Activity) mContext).getApplication() ) );
                for (Activity activity : activities) {
                    Log.i( "AllActivitys", "activity name is " + activity.getClass().getName() );
                }*/
				break;
			case R.id.id_titleBar_leftImg:
				close( mContext );
				break;
			case R.id.id_register_registerBt:// ע��
				boolean verification = verification();
				if (verification) {
					String name = getAccountView().getText().toString();
					String phone = getPhoneView().getText().toString();
					if (!"1".equals( type )) {
						code = getCodeView().getText().toString();
						if (TextUtils.isEmpty( code )) {
							showToast( mContext, "��������֤��!!!" );
							return;
						}
					}
					pwd = "1".equals( type ) ? getPwdView().getText().toString()
							: getPwdView2().getText().toString();
					if (isChecked) {
						register( REGISTER, name, phone, pwd, type, code );
					} else {
						showToast( mContext, "���Ķ���ͬ���û�Э��" );
					}
				}
				break;
			case R.id.id_register_sendCodeBt:// ��֤��
				if (TextUtils.isEmpty( getPhoneView().getText().toString() )) {
					ToastUtils.showToast( mContext, R.string.inputPhoneHint );
				} else {
					if (!BeanUtils.isMatchered( BeanUtils.PHONE_PATTERN,
							getPhoneView().getText().toString() )) {
						ToastUtils.showToast( mContext, R.string.phoneError );
					} else {
						getCode( GETCODE, getPhoneView().getText().toString() );
					}
				}
				break;
		}
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		cancelProgressDialog();
		if ("1".equals( resultItem.getString( "status" ) )) {
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
		getRegisterView().setFocusable( true );
		getRegisterView().setClickable( true );
		cancelProgressDialog();
		showToast( mContext, error );
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ResultItem resultItem = (ResultItem) msg.obj;
			switch (msg.what) {
				case GETCODE:
					if (resultItem != null) {
						changeButton();
					} else {
						showToast( mContext, "��֤��Ϊ��" );
					}
					break;
				case REGISTER:
					AppStatisticsManager.addStatistics( StatisticActionEnum.REGISTRATION_COMPLETE, true );
					GDTActionUtils.reportData( mContext, 1 );
					if (Build.VERSION.SDK_INT > 21 && ContextCompat.checkSelfPermission( mContext,
							Manifest.permission.READ_PHONE_STATE )
							!= PackageManager.PERMISSION_GRANTED) {
						ActivityCompat.requestPermissions( (Activity) mContext,
								new String[]{Manifest.permission.READ_PHONE_STATE,}, 4567 );
					} else {
						JrttUtils.jrttReportData( ("1".equals( type )) ? JrttUtils.ACCOUNTREGISTER : JrttUtils.MOBILEREGISTER );
					}
					getRegisterView().setFocusable( true );
					getRegisterView().setClickable( true );
					if (resultItem != null) {
						ResultItem item = resultItem.getItem( "data" );
						final String uid = item.getString( "id" );
						final String name = item.getString( "username" );
						final String phone = item.getString( "mobile" );
						String avatar = item.getString( "icon_url" );
						String nickName = item.getString( "nick_name" );
						TrackingUtils.setRegisterWithAccountID( uid );
						new Thread() {
							public void run() {
								JSONObject object = new JSONObject();
								try {
									object.put( "userName", name );
									object.put( "pwd", pwd );
									object.put( "phone", phone );
									object.put( "uid", uid );
									FileUtils
											.saveAccountNew( mContext, object.toString() );
								} catch (JSONException e) {
									e.printStackTrace();
								}
								HttpManager.login( 80, mContext, null, name, pwd );
							}
						}.start();
						SpUtil.setPhone( phone );
						SpUtil.setAccount( name );
						SpUtil.setNick( nickName );
						SpUtil.setUserId( uid );
						SpUtil.setPwd( "1".equals( type ) ? getPwdView().getText()
								.toString() : getPwdView2().getText().toString() );
						SpUtil.setExit( false );
						if (!TextUtils.isEmpty( avatar )) {
							SpUtil.setHeaderUrl( avatar );
						}
						((Activity) mContext).setResult( Activity.RESULT_OK );
						close( mContext );
					}
					break;
			}
		}
	};

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		this.isChecked = isChecked;
	}
}
