package com.tenone.gamebox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.FileUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class TradingRegisterActivity extends BaseActivity implements HttpResultListener {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBarView;
    @ViewInject(R.id.id_trading_register_accountEt)
    EditText accountEt;
    @ViewInject(R.id.id_trading_register_pwdEt)
    EditText pwdEt;
    @ViewInject(R.id.id_trading_register_codeEt)
    EditText codeEt;
    @ViewInject(R.id.id_trading_register_codeTv)
    TextView codeTv;

    private Context context;
    private Timer timer;
    private TimerTask timerTask;
    private int time = 60;
    private String account, pwd, code;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        context = this;
        setContentView( R.layout.activity_trading_register );
        ViewUtils.inject( this );
        initView();
    }

    private void initView() {
        codeTv.setSelected( true );
        titleBarView.setLeftImg( R.drawable.icon_xqf_b );
        titleBarView.setTitleText( "交易注册" );
    }

    @OnClick({R.id.id_trading_register_codeTv, R.id.id_trading_register_agreement
            , R.id.id_trading_register_register, R.id.id_titleBar_leftImg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_titleBar_leftImg:
                finish();
                break;
            case R.id.id_trading_register_codeTv:
                account = accountEt.getText().toString();
                if (TextUtils.isEmpty( account )) {
                    ToastUtils.showToast( this, "请输入手机号码" );
                    return;
                }
                if (!BeanUtils.isMatchered( BeanUtils.PHONE_PATTERN, account )) {
                    ToastUtils.showToast( this, "请输入正确的手机号码" );
                    return;
                }
                HttpManager.sendMessage( this, 22, this, account, 1 );
                startCountdown();
                break;
            case R.id.id_trading_register_agreement:
                startActivity( new Intent( this, WebActivity.class )
                        .putExtra( "title", "用户协议" )
                        .putExtra( "url", MyApplication.getHttpUrl().getAgreement() ) );
                break;
            case R.id.id_trading_register_register:
                account = accountEt.getText().toString();
                code = codeEt.getText().toString();
                pwd = pwdEt.getText().toString();
                if (checkRegister()) {
                    buildProgressDialog();
                    HttpManager.tradingRegister( HttpType.REFRESH, this, this, code, account, pwd );
                }
                break;
        }
    }

    private boolean checkRegister() {
        if (TextUtils.isEmpty( account )) {
            ToastUtils.showToast( this, "请输入手机号" );
            return false;
        }
        if (!BeanUtils.isMatchered( BeanUtils.PHONE_PATTERN, account )) {
            ToastUtils.showToast( this, "请输入正确的手机号码" );
            return false;
        }
        if (TextUtils.isEmpty( code )) {
            ToastUtils.showToast( this, "请输入验证码" );
            return false;
        }
        if (TextUtils.isEmpty( pwd ) || pwd.length() < 6) {
            ToastUtils.showToast( this, "请输入不少于6位的密码" );
            return false;
        }
        if (TextUtils.isEmpty( pwd ) || pwd.length() < 6) {
            ToastUtils.showToast( this, "请输入不少于6位的密码" );
            return false;
        }
        return true;
    }

    private void startCountdown() {
        codeTv.setClickable( false );
        codeTv.setTextColor( getResources().getColor( R.color.gray_9a ) );
        codeTv.setText( getResources().getString( R.string.sent ) );
        if (timer == null) {
            timer = new Timer();
        }
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread( () -> {
                        if (time < 1) {
                            codeTv.setClickable( true );
                            codeTv.setTextColor( getResources().getColor( R.color.blue_40 ) );
                            codeTv.setText( "重新获取" );
                            time = 60;
                            cancleTimer();
                        } else {
                            codeTv.setText( time + "s后重新获取" );
                        }
                    } );
                    time--;
                }
            };
        }
        timer.schedule( timerTask, 1000, 1000 );
    }

    private void cancleTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onDestroy() {
        cancleTimer();
        super.onDestroy();
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        cancelProgressDialog();
        if (1 == resultItem.getIntValue( "status" )) {
            switch (what) {
                case 22:
                    ToastUtils.showToast( this, "验证码发送成功,请注意查收" );
                    break;
                case HttpType.REFRESH:
                    ToastUtils.showToast( this, "注册成功" );
                    registerResult( resultItem );
                    break;
            }
        } else {
            ToastUtils.showToast( this, resultItem.getString( "msg" ) );
        }
    }

    private void registerResult(ResultItem resultItem) {
        new Thread() {
            @Override
            public void run() {
                ResultItem data = resultItem.getItem( "data" );
                String tradingMobile = data.getString( "mobile" );
                SpUtil.setTradingUid( data.getString( "uid" ) );
                SpUtil.setTradingUserName( data.getString( "username" ) );
                SpUtil.setTradingMobile(tradingMobile );
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
