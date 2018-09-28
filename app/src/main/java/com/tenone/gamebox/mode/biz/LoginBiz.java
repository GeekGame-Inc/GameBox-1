
package com.tenone.gamebox.mode.biz;

import android.content.Context;
import android.text.TextUtils;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.able.LoginAble;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.utils.ToastUtils;

public class LoginBiz implements LoginAble {

    @Override
    public boolean verification(Context cxt, CustomizeEditText account,
                                CustomizeEditText pwd) {
        if (TextUtils.isEmpty( account.getText().toString() )) {
            ToastUtils.showToast( cxt, R.string.inputAccountHint );
            return false;
        }

        if (TextUtils.isEmpty( pwd.getText().toString() )) {
            ToastUtils.showToast( cxt, R.string.inputPwdHint );
            return false;
        }

        if (pwd.getText().toString().length() < 6) {
            ToastUtils.showToast( cxt, R.string.pwdNotEnough );
            return false;
        }

        return true;
    }
}
