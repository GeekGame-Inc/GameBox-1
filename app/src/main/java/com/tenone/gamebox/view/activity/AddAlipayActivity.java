package com.tenone.gamebox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
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

public class AddAlipayActivity extends BaseActivity implements HttpResultListener {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBarView;
    @ViewInject(R.id.id_add_alipay_et)
    EditText editText;
    @ViewInject(R.id.id_add_alipay_real_name)
    EditText realNameEt;


    private String alipay, realName;
    private MaterialDialog.Builder buidler;
    private Context context;

    private boolean isEdit;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        context = this;
        isEdit = !TextUtils.equals( "add", getIntent().getAction() );
        super.onCreate( arg0 );
        setContentView( R.layout.activity_add_alipay );
        ViewUtils.inject( this );
        initTitle();
    }

    private void initTitle() {
        titleBarView.setLeftImg( R.drawable.icon_xqf_b );
        titleBarView.setTitleText( isEdit ? getString( R.string.change_alipay_account) : getString( R.string.bind_alipay_account)  );
        titleBarView.setOnClickListener( R.id.id_titleBar_leftImg, v -> finish() );
    }


    @OnClick({R.id.id_add_alipay_confirm})
    public void onClick(View view) {
        if (view.getId() == R.id.id_add_alipay_confirm) {
            realName = realNameEt.getText().toString();
            if (TextUtils.isEmpty( realName )) {
                ToastUtils.showToast( this, getString( R.string.please_input_real_name) );
                return;
            }
            alipay = editText.getText().toString();
            if (TextUtils.isEmpty( alipay )) {
                ToastUtils.showToast( this, getString( R.string.please_input_alipay_account) );
                return;
            }
            showDialog();
        }
    }

    private void showDialog() {
        if (buidler == null) {
            buidler = new MaterialDialog.Builder( context )
                    .title( R.string.bind_alipy )
                    .negativeText( R.string.check_once )
                    .positiveText( R.string.confirm_no_error )
                    .titleGravity( GravityEnum.CENTER )
                    .content( R.string.alipay_account_hint )
                    .buttonsGravity( GravityEnum.CENTER )
                    .positiveColor( getResources().getColor( R.color.blue_40 ) )
                    .negativeColor( getResources().getColor( R.color.blue_40 ) )
                    .titleColor( getResources().getColor( R.color.gray_69 ) )
                    .contentColor( getResources().getColor( R.color.gray_9a ) )
                    .callback( new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            buildProgressDialog();
                            HttpManager.editUser( HttpType.REFRESH, context, AddAlipayActivity.this, alipay, realName );
                        }
                    } );
        }
        buidler.show();
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        cancelProgressDialog();
        if (1 == resultItem.getIntValue( "status" )) {
            ToastUtils.showToast( this, getString( R.string.bind_success) );
            String a = resultItem.getString( "alipay_acount" );
            Intent intent = getIntent();
            intent.putExtra( "alipay", a );
            setResult( RESULT_OK, intent );
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
