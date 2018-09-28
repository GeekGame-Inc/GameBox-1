/**
 * Project Name:GameBox
 * File Name:FindPwdActivity.java
 * Package Name:com.tenone.gamebox.view.activity
 * Date:2017-4-27����11:23:13
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.FindPwdView;
import com.tenone.gamebox.presenter.FindPwdPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * �һ����� ClassName:FindPwdActivity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-27 ����11:23:13 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */
@SuppressLint("ResourceAsColor")
public class FindPwdActivity extends BaseActivity implements FindPwdView {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBarView;
    @ViewInject(R.id.id_findPwd_account)
    CustomizeEditText phoneEditText;
    @ViewInject(R.id.id_findPwd_code)
    CustomizeEditText codeEditText;
    @ViewInject(R.id.id_findPwd_sendCodeBt)
    TextView sendButton;
    @ViewInject(R.id.id_findPwd_nextBt)
    Button nextButton;
    @ViewInject(R.id.id_title_underline)
    View underline;
    FindPwdPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_find_pwd );
        ViewUtils.inject( this );
        underline.setVisibility( View.GONE );
        presenter = new FindPwdPresenter( this, this );
        presenter.initView();
        presenter.initListener();
    }

    @Override
    public TitleBarView getTitleBarView() {
        return titleBarView;
    }

    @Override
    public CustomizeEditText getPhoneView() {
        return phoneEditText;
    }

    @Override
    public CustomizeEditText getCodeView() {
        return codeEditText;
    }

    @Override
    public TextView getSendCodeView() {
        return sendButton;
    }

    @Override
    public Button getNextView() {
        return nextButton;
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult( arg0, arg1, arg2 );
        presenter.onActivityResult( arg0, arg1, arg2 );
    }
}
