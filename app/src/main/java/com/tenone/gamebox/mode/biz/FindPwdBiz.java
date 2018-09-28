
package com.tenone.gamebox.mode.biz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.able.FindPwdAble;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.ToastUtils;


@SuppressWarnings("deprecation")
public class FindPwdBiz implements FindPwdAble {
    private boolean isStop;
    private int timeDsec;
    private Context mContext;
    private TextView button;

    @Override
    public boolean verification(Context cxt, CustomizeEditText phone) {
        if (TextUtils.isEmpty( phone.getText().toString() )) {
            ToastUtils.showToast( cxt,R.string.inputPhoneHint );
            return false;
        }
        return true;
    }

    @Override
    public boolean verificationNext(Context context, CustomizeEditText phone,
                                    CustomizeEditText code) {
        if (TextUtils.isEmpty( phone.getText().toString() )) {
            ToastUtils.showToast( context,R.string.inputPhoneHint );
            return false;
        }

        if (TextUtils.isEmpty( code.getText().toString() )) {
            ToastUtils.showToast( context,R.string.inputCodeHint );
            return false;
        }
        return true;
    }

    @Override
    public void changeButton(Context cxt, TextView button, CustomizeEditText phone) {
        if (TextUtils.isEmpty( phone.getText().toString() )) {
            ToastUtils.showToast( cxt,R.string.inputPhoneHint );
            return;
        } else {
            if (!BeanUtils.isMatchered( BeanUtils.PHONE_PATTERN, phone.getText()
                    .toString() )) {
                ToastUtils.showToast( cxt,R.string.phoneError );
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
            button.setText( "(" + timeDsec + ")" + mContext.getString( R.string.get_again) );
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
