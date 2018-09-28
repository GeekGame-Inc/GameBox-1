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
import com.tenone.gamebox.view.utils.ToastUtils;

public class TradingAddAccountActivity extends BaseActivity implements HttpResultListener {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBarView;
    @ViewInject(R.id.id_trading_add_accountEt)
    EditText accountEt;
    @ViewInject(R.id.id_trading_add_pwdEt)
    EditText pwdEt;

    private String account, pwd;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_trading_add );
        ViewUtils.inject( this );
        initTitle();
    }

    private void initTitle() {
        titleBarView.setLeftImg( R.drawable.icon_xqf_b );
        titleBarView.setTitleText( "\u6dfb\u52a0\u5173\u8054\u8d26\u53f7" );
        titleBarView.setOnClickListener( R.id.id_titleBar_leftImg, v -> finish() );
    }

    @OnClick({R.id.id_trading_add_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_trading_add_add:
                if (checkBind()) {
                    buildProgressDialog();
                    HttpManager.bindSdkUser( HttpType.REFRESH, this, this, account, pwd );
                }
                break;
        }
    }

    private boolean checkBind() {
        account = accountEt.getText().toString();
        pwd = pwdEt.getText().toString();
        if (TextUtils.isEmpty( account )) {
            ToastUtils.showToast( this, "\u8bf7\u8f93\u5165\u5c06\u8981\u5173\u8054\u7684\u8d26\u53f7" );
            return false;
        }
        if (TextUtils.isEmpty( pwd )) {
            ToastUtils.showToast( this, "\u8bf7\u8f93\u5165\u5c06\u8981\u5173\u8054\u7684\u8d26\u53f7\u5bc6\u7801" );
            return false;
        }
        return true;
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        cancelProgressDialog();
        if (1 == resultItem.getIntValue( "status" )) {
            ToastUtils.showToast( this, "\u6dfb\u52a0\u6210\u529f" );
            setResult( RESULT_OK );
            finish();
        } else {
            ToastUtils.showToast( this, resultItem.getString( "msg" ) );
        }
    }

    @Override
    public void onError(int what, String error) {
        cancelProgressDialog();
        ToastUtils.showToast( this, error );
    }
}
