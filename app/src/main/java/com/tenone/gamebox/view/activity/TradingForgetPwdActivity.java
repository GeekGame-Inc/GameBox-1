package com.tenone.gamebox.view.activity;

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
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;

public class TradingForgetPwdActivity extends BaseActivity implements HttpResultListener {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBarView;
    @ViewInject(R.id.id_trading_forget_accountEt)
    EditText accountEt;
    @ViewInject(R.id.id_trading_forget_pwdEt)
    EditText pwdEt;
    @ViewInject(R.id.id_trading_forget_codeEt)
    EditText codeEt;
    @ViewInject(R.id.id_trading_forget_codeTv)
    TextView codeTv;

    private Timer timer;
    private TimerTask timerTask;
    private int time = 60;
    private String account, pwd, code;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_trading_forget );
        ViewUtils.inject( this );
        initView();
    }

    private void initView() {
        codeTv.setSelected( true );
        titleBarView.setLeftImg( R.drawable.icon_xqf_b );
        titleBarView.setTitleText( "�һ�����" );
    }


    @OnClick({R.id.id_trading_forget_codeTv, R.id.id_trading_forget_confirm, R.id.id_titleBar_leftImg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_titleBar_leftImg:
                finish();
                break;
            case R.id.id_trading_forget_codeTv:
                account = accountEt.getText().toString();
                if (TextUtils.isEmpty( account )) {
                    ToastUtils.showToast( this, "�������ֻ�����" );
                    return;
                }
                if (!BeanUtils.isMatchered( BeanUtils.PHONE_PATTERN, account )) {
                    ToastUtils.showToast( this, "��������ȷ���ֻ�����" );
                    return;
                }
                HttpManager.sendMessage( this, 22, this, account, 2 );
                startCountdown();
                break;
            case R.id.id_trading_forget_confirm:
                account = accountEt.getText().toString();
                code = codeEt.getText().toString();
                pwd = pwdEt.getText().toString();
                if (checkConfirm()) {
                    buildProgressDialog();
                    HttpManager.tradingForgetPwd( HttpType.REFRESH, this, this, pwd, account, code );
                }
                break;
        }
    }

    private boolean checkConfirm() {
        if (TextUtils.isEmpty( account )) {
            ToastUtils.showToast( this, "�������ֻ���" );
            return false;
        }
        if (!BeanUtils.isMatchered( BeanUtils.PHONE_PATTERN, account )) {
            ToastUtils.showToast( this, "��������ȷ���ֻ�����" );
            return false;
        }
        if (TextUtils.isEmpty( code )) {
            ToastUtils.showToast( this, "��������֤��" );
            return false;
        }
        if (TextUtils.isEmpty( pwd ) || pwd.length() < 6) {
            ToastUtils.showToast( this, "�����벻����6λ������" );
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
                            codeTv.setText( "���»�ȡ" );
                            time = 60;
                            cancleTimer();
                        } else {
                            codeTv.setText( time + "s�����»�ȡ" );
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
                    ToastUtils.showToast( this, "��֤�뷢�ͳɹ�,��ע�����" );
                    break;
                case HttpType.REFRESH:
                    ToastUtils.showToast( this, "�޸ĳɹ�" );
                    registerResult();
                    break;
            }
        } else {
            ToastUtils.showToast( this, resultItem.getString( "msg" ) );
        }
    }

    private void registerResult() {
        new Thread() {
            @Override
            public void run() {
                SpUtil.setTradingPwd( pwd );
                SpUtil.setTradingMobile( account );
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
