package com.tenone.gamebox.mode.biz;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.able.RegisterAble;
import com.tenone.gamebox.mode.mode.RegisterMode;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.ToastUtils;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("HandlerLeak")
@SuppressWarnings("deprecation")
public class RegisterBiz implements RegisterAble {
    private boolean isStop;
    private int timeDsec;
    private Context mContext;
    private TextView button;

    @Override
    public boolean verification(Context cxt, CustomizeEditText account,
                                CustomizeEditText password, CustomizeEditText phone,
                                CustomizeEditText code, String codeText, String type) {
        if ("1".equals( type )) {
            if (TextUtils.isEmpty( account.getText().toString() )) {
                ToastUtils.showToast( cxt, R.string.inputAccountHint );
                return false;
            }
        } else {
            if (TextUtils.isEmpty( phone.getText().toString() )) {
                ToastUtils.showToast( cxt, R.string.inputPhoneHint );
                return false;
            }

            if (!BeanUtils.isMatchered( BeanUtils.PHONE_PATTERN, phone.getText()
                    .toString() )) {
                ToastUtils.showToast( cxt, R.string.phoneError );
                return false;
            }

            if (TextUtils.isEmpty( code.getText().toString() )) {
                ToastUtils.showToast( cxt, R.string.inputCodeHint );
                return false;
            }
        }

        if (TextUtils.isEmpty( password.getText().toString() )) {
            ToastUtils.showToast( cxt, R.string.inputPwdHint );
            return false;
        }

        if (password.getText().toString().length() < 6) {
            ToastUtils.showToast( cxt, R.string.pwdNotEnough );
            return false;
        }

        if (password.getText().toString().length() > 16) {
            ToastUtils.showToast( cxt, R.string.pwdEnoughed );
            return false;
        }

        return true;
    }

    @Override
    public RegisterMode getRegisterMode(CustomizeEditText account,
                                        CustomizeEditText password, CustomizeEditText phone,
                                        CustomizeEditText code) {
        RegisterMode mode = new RegisterMode();
        mode.setAccount( account.getText().toString() );
        mode.setPassword( password.getText().toString() );
        mode.setPhone( phone.getText().toString() );
        return mode;
    }

    @Override
    public void changeButton(Context cxt, TextView button, CustomizeEditText phone) {
        if (TextUtils.isEmpty( phone.getText().toString() )) {
            ToastUtils.showToast( mContext, R.string.inputPhoneHint );
            return;
        } else {
            if (!BeanUtils.isMatchered( BeanUtils.PHONE_PATTERN, phone.getText()
                    .toString() )) {
                ToastUtils.showToast( mContext, R.string.phoneError );
                return;
            }
        }
        this.mContext = cxt;
        this.button = button;
        button.setTextColor( mContext.getResources().getColor( R.color.gray_9a ) );
        button.setText( mContext.getResources().getString( R.string.sent ) );
        timeDsec = 60;
        button.setEnabled( false );
        mHandle.removeMessages( 1 );
        isStop = false;
        startTime();
    }

    public void reduceTime() {
        timeDsec = timeDsec - 1;
    }

    private void startTime() {
        mHandle.sendEmptyMessageDelayed( 1, 1000 );
    }

    private void updateClock() {
        if (timeDsec == 0) {
            button.setTextColor( mContext.getResources().getColor( R.color.blue_40 ) );
            button.setEnabled( true );
            button.setText( R.string.getCode );
            isStop = true;
        } else {
            button.setText( "(" + timeDsec + ")" + R.string.get_again  );
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (!isStop) {
                        reduceTime();
                        updateClock();
                    }
                    mHandle.sendEmptyMessageDelayed( 1, 1000 );
                    break;
            }
        }
    };
}
