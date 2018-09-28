/**
 * Project Name:GameBox
 * File Name:FindPwdPresenter.java
 * Package Name:com.tenone.gamebox.presenter
 * Date:2017-4-27����1:34:20
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.FindPwdBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.FindPwdView;
import com.tenone.gamebox.view.activity.ModificationPwdActivity;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.SpUtil;

/**
 * ClassName:FindPwdPresenter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-27 ����1:34:20 <br/>
 *
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class FindPwdPresenter extends BasePresenter implements OnClickListener,
        HttpResultListener {
    FindPwdBiz findPwdBiz;
    FindPwdView findPwdView;
    Context mContext;
    String code;
    AlertDialog alertDialog;

    public FindPwdPresenter(FindPwdView v, Context cxt) {
        this.findPwdView = v;
        this.mContext = cxt;
        this.findPwdBiz = new FindPwdBiz();
    }

    public void initView() {
        getTitleBarView().setLeftImg( R.drawable.icon_back_grey );
        getTitleBarView().setTitleText( R.string.findPwd );
    }

    public void initListener() {
        getTitleBarView().getLeftImg().setOnClickListener( this );
        getSendCodeView().setOnClickListener( this );
        getNextView().setOnClickListener( this );
    }

    /* ���� */
    public TitleBarView getTitleBarView() {
        return findPwdView.getTitleBarView();
    }

    /* �ֻ��� */
    public CustomizeEditText getPhoneView() {
        return findPwdView.getPhoneView();
    }

    /* ��֤�� */
    public CustomizeEditText getCodeView() {
        return findPwdView.getCodeView();
    }

    /* ������֤�� */
    public TextView getSendCodeView() {
        return findPwdView.getSendCodeView();
    }

    /* ע�� */
    public Button getNextView() {
        return findPwdView.getNextView();
    }

    /* ��֤�Ƿ���Ի�ȡ��֤�� */
    public boolean verification() {
        return findPwdBiz.verification( mContext, getPhoneView() );
    }

    /* �Ƿ������һ�� */
    public boolean verificationNext() {
        return findPwdBiz.verificationNext( mContext, getPhoneView(),
                getCodeView() );
    }

    /* �ı�button״̬ */
    public void changeButton() {
        findPwdBiz.changeButton( mContext, getSendCodeView(), getPhoneView() );
    }

    /**
     * ��ȡ��֤�� getCode:(������һ�仰�����������������). <br/>
     *
     * @author John Lie
     * @param what
     * @param phone
     * @since JDK 1.6
     */
    public void getCode(int what, String phone) {
        HttpManager.getCode( what, mContext, this, phone, 2 );
    }

    /**
     * ������֤��ӿ� checkCode:(������һ�仰�����������������). <br/>
     *
     * @author John Lie
     * @param what
     * @param phone
     * @param code
     * @since JDK 1.6
     */
    public void checkCode(int what, String phone, String code) {
        HttpManager.check_smscode( what, mContext, this, phone, code );
    }

    public void onActivityResult(int arg0, int arg1, Intent arg2) {
        if (arg0 == 32 && arg1 == Activity.RESULT_OK) {
            close( mContext );
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_titleBar_leftImg:
                close( mContext );
                break;
            case R.id.id_findPwd_sendCodeBt:
                if (verification()) {
                    alertDialog = buildProgressDialog( mContext );
                    getCode( HttpType.REFRESH, getPhoneView().getText().toString() );
                    changeButton();
                }
                break;
            case R.id.id_findPwd_nextBt:
                if (verificationNext()) {
                    code = getCodeView().getText().toString();
                    if (TextUtils.isEmpty( code )) {
                        showToast( mContext, "��������ȷ����֤��" );
                        break;
                    }
                    alertDialog = buildProgressDialog( mContext );
                    checkCode( HttpType.LOADING,
                            getPhoneView().getText().toString(), code );
                }
                break;
        }
    }

    @Override
    public void onSuccess(final int what, final ResultItem resultItem) {
        cancelProgressDialog( alertDialog );
        if ("1".equals( resultItem.getString( "status" ) )) {
            new Thread() {
                public void run() {
                    switch (what) {
                        case HttpType.REFRESH:
                            if (resultItem.getItem( "data" ) != null) {
                                code = String.valueOf( resultItem.getItem( "data" ) );
                            }
                            break;
                        case HttpType.LOADING:
                            if (resultItem.getItem( "data" ) != null) {
                                String uid = resultItem.getItem( "data" ).getString(
                                        "id" );
                                String token = resultItem.getItem( "data" )
                                        .getString( "token" );
                                SpUtil.setUserId( uid );
                                Message message = new Message();
                                message.obj = token;
                                handler.sendMessage( message );
                            }
                            break;
                    }
                }
            }.start();
        } else {
            showToast( mContext, resultItem.getString( "msg" ) );
        }
    }

    @Override
    public void onError(int what, String error) {
        cancelProgressDialog( alertDialog );
        showToast( mContext, error );
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ((Activity) mContext).startActivityForResult( new Intent(
                    mContext, ModificationPwdActivity.class ).setAction( "find" )
                    .putExtra( "token", (String) msg.obj ), 32 );
        }
    };

}
