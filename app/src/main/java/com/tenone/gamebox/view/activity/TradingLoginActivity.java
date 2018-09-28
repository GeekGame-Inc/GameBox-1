package com.tenone.gamebox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.FileUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class TradingLoginActivity extends BaseActivity implements HttpResultListener {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBarView;
    @ViewInject(R.id.id_trading_login_accountEt)
    EditText accountEt;
    @ViewInject(R.id.id_trading_login_pwdEt)
    EditText pwdEt;

    private String account, pwd;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        context = this;
        setContentView( R.layout.activity_trading_login );
        ViewUtils.inject( this );
        initView();
    }

    private void initView() {
        titleBarView.setTitleText( getString( R.string.login_trading ) );
        titleBarView.setLeftImg( R.drawable.icon_xqf_b );
        titleBarView.setRightText( getString( R.string.register_trading) );
        account = SpUtil.getTradingMobile();
        pwd = SpUtil.getTradingPwd();
        if (!TextUtils.isEmpty( account )) {
            accountEt.setText( account );
            accountEt.setSelection( account.length() );
        }
        if (!TextUtils.isEmpty( pwd )) {
            pwdEt.setText( pwd );
            pwdEt.setSelection( pwd.length() );
        }
    }

    @OnClick({R.id.id_trading_login_forgetPwd, R.id.id_trading_login_login
            , R.id.id_titleBar_leftImg, R.id.id_titleBar_rightText})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_trading_login_forgetPwd:
                startActivityForResult( new Intent( this, TradingForgetPwdActivity.class ), 1028 );
                break;
            case R.id.id_trading_login_login:
                if (checkLogin()) {
                    buildProgressDialog();
                    HttpManager.tradingLogin( HttpType.REFRESH, this, this, pwd, account );
                }
                break;
            case R.id.id_titleBar_leftImg:
                finish();
                break;
            case R.id.id_titleBar_rightText:
                startActivityForResult( new Intent( this, TradingRegisterActivity.class ), 1025 );
                break;
        }
    }

    private boolean checkLogin() {
        account = accountEt.getText().toString();
        pwd = pwdEt.getText().toString();
        if (TextUtils.isEmpty( account )) {
            ToastUtils.showToast( this, getString( R.string.please_input_mobile ) );
            return false;
        }
        if (!BeanUtils.isMatchered( BeanUtils.PHONE_PATTERN, account )) {
            ToastUtils.showToast( this, getString( R.string.please_input_right_mobile ) );
            return false;
        }
        if (TextUtils.isEmpty( pwd ) || pwd.length() < 6) {
            ToastUtils.showToast( this, "\u8bf7\u8f93\u5165\u4e0d\u5c11\u4e8e6\u4f4d\u7684\u5bc6\u7801" );
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1025:
                    finish();
                    break;
                case 1028:
                    buildProgressDialog();
                    pwd = SpUtil.getTradingPwd();
                    account = SpUtil.getTradingMobile();
                    HttpManager.tradingLogin( HttpType.REFRESH, TradingLoginActivity.this,
                            TradingLoginActivity.this, pwd, account );
                    break;
            }
        }
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        cancelProgressDialog();
        if (1 == resultItem.getIntValue( "status" )) {
            ToastUtils.showToast( this, getString( R.string.login_success) );
            loginResult( resultItem );
        } else {
            ToastUtils.showToast( this, resultItem.getString( "msg" ) );
        }
    }

    @Override
    public void onError(int what, String error) {
        cancelProgressDialog();
        ToastUtils.showToast( this, error );
    }

    private void loginResult(ResultItem resultItem) {
        new Thread() {
            @Override
            public void run() {
                ResultItem data = resultItem.getItem( "data" );
                String tradingMobile = data.getString( "mobile" );
                SpUtil.setTradingUid( data.getString( "uid" ) );
                SpUtil.setTradingUserName( data.getString( "username" ) );
                SpUtil.setTradingMobile( tradingMobile );
                SpUtil.setTradingIsExit( false );
                JSONObject object = new JSONObject();
                try {
                    object.put( "userName", SpUtil.getAccount() );
                    object.put( "pwd", SpUtil.getPwd() );
                    object.put( "phone", SpUtil.getPhone() );
                    object.put( "uid", SpUtil.getUserId() );
                    object.put( "tradingMobile", tradingMobile );
                    object.put( "tradingPassword", pwd );
                    FileUtils
                            .saveAccountNew( context, object.toString() );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                runOnUiThread( () -> {
                    ListenerManager.sendOnTradingLoginStatusListener( BeanUtils.tradingIsLogin() );
                    finish();
                } );
                super.run();
            }
        }.start();
    }
}
