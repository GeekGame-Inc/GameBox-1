package com.tenone.gamebox.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.ApkUtils;
import com.tenone.gamebox.view.utils.ChenColorUtils;
import com.tenone.gamebox.view.utils.HttpManager;

@SuppressLint("ResourceAsColor")
public class CallCenterActivity extends BaseActivity implements
        HttpResultListener {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBarView;
    @ViewInject(R.id.id_call_center_boxQQ)
    TextView boxTv;
    @ViewInject(R.id.id_call_center_gameQQ)
    TextView gameTv;
    @ViewInject(R.id.id_call_center_playerQQ)
    TextView playerTv;
    @ViewInject(R.id.id_call_center_rebateQQ)
    TextView rebateTv;
    @ViewInject(R.id.textView1)
    TextView iconTv;

    private String bGroupQQ, shouyouQQ, sGroupQQ, fanliQQ;
    private String bGroupQQKey, shouyouQQKey, sGroupQQKey, fanliQQKey;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_call_center );
        ViewUtils.inject( this );
        initView();
    }

    private void initView() {
        initTitle();
        HttpManager.customerService( HttpType.REFRESH, getApplicationContext(),
                this );
    }

    private void initTitle() {
        Drawable leftDrawable = getResources().getDrawable(
                R.drawable.icon_back_grey );
        titleBarView.setLeftImg( ChenColorUtils.tintDrawable( leftDrawable,
                ColorStateList.valueOf( getResources().getColor( R.color.gray_69 ) ) ) );
			titleBarView.setTitleText( "\u5ba2\u670d\u4e2d\u5fc3" );

        Drawable rightDrawable = getResources().getDrawable(
                R.drawable.icon_kefu_center );
        rightDrawable.setBounds( 0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight() );
        iconTv.setCompoundDrawables( ChenColorUtils.tintDrawable( rightDrawable,
                ColorStateList.valueOf( getResources().getColor( R.color.blue_40 ) ) ), null, null, null );
    }

    @OnClick({R.id.id_call_center_boxQQBt, R.id.id_call_center_gameQQBt,
            R.id.id_call_center_playerQQBt, R.id.id_call_center_rebateQQBt,
            R.id.id_titleBar_leftImg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_call_center_boxQQBt:
                startQQGroup( bGroupQQKey );
                break;
            case R.id.id_call_center_gameQQBt:
                startQQ( shouyouQQ, shouyouQQKey );
                break;
            case R.id.id_call_center_playerQQBt:
                startQQGroup( sGroupQQKey );
                break;
            case R.id.id_call_center_rebateQQBt:
                startQQ( fanliQQ, fanliQQKey );
                break;
            case R.id.id_titleBar_leftImg:
                finish();
                break;
        }
    }

    private void startQQ(String qq, String key) {
        if (ApkUtils.checkAppExist( this, "com.tencent.mobileqq" )) {
            try {
                String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;
                startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse( url ) ) );
            } catch (Exception e) {
							Toast.makeText( this, "\u8bf7\u68c0\u67e5\u662f\u5426\u5b89\u88c5\u4e86QQ\u5ba2\u6237\u7aef", Toast.LENGTH_SHORT )
									.show();
            }
        } else {
            if (!TextUtils.isEmpty( key )) {
                Intent intent = new Intent();
                intent.setData( Uri.parse( key ) );
                intent.setAction( Intent.ACTION_VIEW );
                startActivity( intent );
            } else {
							Toast.makeText( this, "\u8bf7\u68c0\u67e5\u662f\u5426\u5b89\u88c5\u4e86QQ\u5ba2\u6237\u7aef", Toast.LENGTH_SHORT ).show();
            }
        }
    }

    private void startQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData( Uri
                .parse( "mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D"
                        + key ) );
        try {
            startActivity( intent );
        } catch (Exception e) {
					Toast.makeText( this, "\u8bf7\u68c0\u67e5\u662f\u5426\u5b89\u88c5\u4e86QQ\u5ba2\u6237\u7aef", Toast.LENGTH_SHORT ).show();
        }
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        if (1 == resultItem.getIntValue( "status" )) {
            ResultItem item = resultItem.getItem( "data" );
            if (item != null) {
                initData( item );
            }
        }
    }

    private void initData(ResultItem item) {
        ResultItem fanli = item.getItem( "fanli_qq" );
        fanliQQ = fanli.getString( "number" );
        fanliQQKey = fanli.getString( "link" );
        ResultItem shouyou = item.getItem( "shouyou_qq" );
        shouyouQQ = shouyou.getString( "number" );
        shouyouQQKey = shouyou.getString( "link" );
        ResultItem sGroup = item.getItem( "shouyou_group" );
        sGroupQQ = sGroup.getString( "number" );
        sGroupQQKey = sGroup.getString( "link" );
        ResultItem bGroup = item.getItem( "box_group" );
        bGroupQQ = bGroup.getString( "number" );
        bGroupQQKey = bGroup.getString( "link" );

        boxTv.setText( bGroupQQ );
        gameTv.setText( shouyouQQ );
        playerTv.setText( sGroupQQ );
        rebateTv.setText( fanliQQ );
    }

    @Override
    public void onError(int what, String error) {
    }
}
