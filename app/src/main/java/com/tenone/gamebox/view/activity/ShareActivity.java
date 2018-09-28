package com.tenone.gamebox.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.share.SharePopupWindow;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.SpUtil;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class ShareActivity extends AppCompatActivity implements
        PlatformActionListener, HttpResultListener {
    @ViewInject(R.id.id_share_coin)
    TextView coinTv;
    @ViewInject(R.id.id_share_persion)
    TextView persionTv;
    @ViewInject(R.id.id_share_rule)
    TextView ruleTv;
    @ViewInject(R.id.id_share_toolbar)
    Toolbar toolbar;

    private SharePopupWindow sharePopupWindow;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_share );
        ViewUtils.inject( this );
        ImmersionBar.with( this ).titleBar( toolbar ).statusBarDarkFont( true ).init();
        initView();
    }

    private void initView() {
        toolbar.setTitle( "" );
        toolbar.setContentInsetsAbsolute( 0, 0 );
        setSupportActionBar( toolbar );
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled( true );
        }
        toolbar.setNavigationOnClickListener( v -> {
            finish();
        } );
        HttpManager.friendRecomInfo( 1, this, this );
    }

    @OnClick({R.id.id_share_top, R.id.id_share_button})
    public void onClick(View v) {
        if (!BeanUtils.isLogin()) {
            startActivityForResult( new Intent( this, LoginActivity.class ), 2006 );
            return;
        }
        switch (v.getId()) {
            case R.id.id_share_button:
                String url = MyApplication.getHttpUrl().getFrendRecom() + "?c="
                        + MyApplication.getConfigModle().getChannelID() + "&u="
                        + SpUtil.getUserId();
                showPopuwindow( url );
                break;
            case R.id.id_share_top:
                startActivity( new Intent( this, TopActivity.class ) );
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == 2006 && resultCode == RESULT_OK) {
            HttpManager.friendRecomInfo( 1, this, this );
        }
    }

    private void showPopuwindow(String url) {
        if (sharePopupWindow == null) {
            sharePopupWindow = new SharePopupWindow( this, url );
        }
        sharePopupWindow.showAtLocation( toolbar, Gravity.BOTTOM, 0, 0 );
        sharePopupWindow.setPlatformActionListener( this );
    }

    @Override
    public void onCancel(Platform arg0, int arg1) {
        ToastCustom.makeText( this, "����ȡ��", ToastCustom.LENGTH_SHORT ).show();
    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
        ToastCustom.makeText( this, "����ɹ�", ToastCustom.LENGTH_SHORT ).show();
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        ToastCustom.makeText( this, "�������", ToastCustom.LENGTH_SHORT ).show();
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        if (1 == resultItem.getIntValue( "status" )) {
            ResultItem item = resultItem.getItem( "data" );
            if (item != null) {
                setView( item );
            }
        } else {
            ToastCustom.makeText( this, resultItem.getString( "msg" ),
                    ToastCustom.LENGTH_SHORT ).show();
        }
    }

    private void setView(ResultItem item) {
        coinTv.setText( item.getString( "recom_bonus" ) );
        persionTv.setText( item.getString( "recom_counts" ) );
        String a = item.getString( "one_get_coin" );
        String b = item.getString( "recom_top" );
        String c = item.getString( "one_register_coin" );
        ruleTv.setText( Html.fromHtml( getResources().getString( R.string.share_rules, c, a, b ) ) );
    }

    @Override
    public void onError(int what, String error) {
        ToastCustom.makeText( this, error, ToastCustom.LENGTH_SHORT ).show();
    }

    @Override
    protected void onDestroy() {
        ImmersionBar.with( this ).destroy(); //������ø÷�������ֹ�ڴ�й©
        super.onDestroy();
    }
}
