package com.tenone.gamebox.view.activity;

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
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.ToastUtils;


public class TradingResetPwdActivity extends BaseActivity implements HttpResultListener {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBarView;
    @ViewInject(R.id.id_trading_reset_pwdEt)
    EditText pwdEt;
    @ViewInject(R.id.id_trading_reset_newEt)
    EditText newEt;

    private String pwd, newPwd;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_trading_reset_pwd );
        ViewUtils.inject( this );
        initView();
    }

    private void initView() {
        titleBarView.setLeftImg( R.drawable.icon_xqf_b );
        titleBarView.setTitleText( "\u4fee\u6539\u5bc6\u7801" );
    }

    @OnClick({R.id.id_trading_reset_confirm, R.id.id_titleBar_leftImg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_titleBar_leftImg:
                finish();
                break;
            case R.id.id_trading_reset_confirm:
                newPwd = newEt.getText().toString();
                pwd = pwdEt.getText().toString();
                if (checkConfirm()) {
                    buildProgressDialog();
                    HttpManager.tradingResetPwd( HttpType.REFRESH, this, this, pwd, newPwd );
                }
                break;
        }
    }

    private boolean checkConfirm() {
        if (TextUtils.isEmpty( pwd )) {
            ToastUtils.showToast( this, "\u8bf7\u8f93\u5165\u65e7\u5bc6\u7801" );
            return false;
        }
        if (!pwd.equals( SpUtil.getTradingPwd() )) {
            ToastUtils.showToast( this, "\u8bf7\u8f93\u5165\u6b63\u786e\u7684\u65e7\u5bc6\u7801" );
            return false;
        }
        if (TextUtils.isEmpty( newPwd ) || newPwd.length() < 6) {
            ToastUtils.showToast( this, "\u8bf7\u8f93\u5165\u4e0d\u5c11\u4e8e6\u4f4d\u7684\u65b0\u5bc6\u7801" );
            return false;
        }
        return true;
    }


    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        cancelProgressDialog();
        if (1 == resultItem.getIntValue( "status" )) {
            ToastUtils.showToast( this, "\u4fee\u6539\u6210\u529f" );
            registerResult();
        } else {
            ToastUtils.showToast( this, resultItem.getString( "msg" ) );
        }
    }

    private void registerResult() {
        new Thread() {
            @Override
            public void run() {
                SpUtil.setTradingPwd( newPwd );
                runOnUiThread( () -> {
                    setResult( RESULT_OK );
                    finish();
                } );
                super.run();
            }
        }.start();
    }

    @Override
    public void onError(int what, String error) {
        cancelProgressDialog();
        ToastUtils.showToast( this, error );
    }
}
