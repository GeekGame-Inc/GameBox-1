package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;

public class AccountDescribeActivity extends BaseActivity implements TextWatcher {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBarView;
    @ViewInject(R.id.id_account_describe_edit)
    CustomizeEditText editText;
    @ViewInject(R.id.id_account_describe_num)
    TextView numTv;

    private String describe;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_account_describe );
        ViewUtils.inject( this );
        initTitle();
        initView();
    }


    private void initTitle() {
        titleBarView.setTitleText( "�˺�����" );
        titleBarView.setLeftImg( R.drawable.icon_back_grey );
        titleBarView.setRightText( "����" );
        titleBarView.setOnClickListener( R.id.id_titleBar_rightText, v -> {
            describe = editText.getText().toString();
            if (TextUtils.isEmpty( describe )) {
                showToast( "�������˺ŵ�����" );
                return;
            }
            setResult( RESULT_OK, getIntent().putExtra( "describe", describe ) );
            finish();
        } );
        titleBarView.setOnClickListener( R.id.id_titleBar_leftImg, v -> finish() );
    }

    private void initView() {
        editText.addTextChangedListener( this );
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty( s )) {
            numTv.setText( s.length() + "/200" );
        }
    }
}
