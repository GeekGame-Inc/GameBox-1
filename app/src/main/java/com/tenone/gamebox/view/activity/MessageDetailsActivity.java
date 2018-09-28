package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.TimeUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

public class MessageDetailsActivity extends BaseActivity implements HttpResultListener {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBarView;
    @ViewInject(R.id.id_message_details_tv)
    TextView textView;
    @ViewInject(R.id.id_message_details_title)
    TextView titleTv;
    @ViewInject(R.id.id_message_details_time)
    TextView timeTv;
    @ViewInject(R.id.id_message_details_iv)
    ImageView imageView;
    @ViewInject(R.id.id_message_details_bt)
    Button button;

    private int messageId;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_message_details );
        ViewUtils.inject( this );
        initTitle();
        messageId = getIntent().getExtras().getInt( "id" );
        HttpManager.messageInfo( HttpType.REFRESH, this, this, messageId );
    }

    private void initView(ResultItem resultItem) {
        if (null != resultItem) {
            textView.setText( resultItem.getString( "desc" ) );
            titleTv.setText( resultItem.getString( "title" ) );
            String time = resultItem.getString( "create_time" );
            if (!TextUtils.isEmpty( time )) {
                try {
                    timeTv.setText( TimeUtils.formatDateSec( Long.valueOf( time ).longValue() * 1000 ) );
                } catch (NumberFormatException e) {
                    timeTv.setText( "" );
                }
            }
            button.setVisibility( (0 != resultItem.getIntValue( "attach_type" )) ? View.VISIBLE : View.GONE );
            button.setBackground( getResources().getDrawable( (1 == resultItem.getIntValue( "is_get" ) ?
                    R.drawable.radiu_gray : R.drawable.shape_rebate) ) );
					button.setText( (1 == resultItem.getIntValue( "is_get" ) ? "\u5df2\u9886\u53d6" : "\u70b9\u51fb\u9886\u53d6") );
            if (!TextUtils.isEmpty( resultItem.getString( "image" ) )) {
                ImageLoadUtils.loadMessageImg( this, resultItem.getString( "image" ), R.drawable.ic_img_failure, imageView );
            }
            url = resultItem.getString( "api_url" );
        }
    }

    private void initTitle() {
        titleBarView.setLeftImg( R.drawable.icon_back_grey );
        titleBarView.setTitleText( R.string.message_details );
        titleBarView.setOnClickListener( R.id.id_titleBar_leftImg, v -> finish() );
    }

    @OnClick({R.id.id_message_details_bt})
    public void onClick(View view) {
        if (!TextUtils.isEmpty( url )) {
            HttpManager.getBonus( HttpType.LOADING, this, this, messageId, url );
        }
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        if (1 == resultItem.getIntValue( "status" )) {
            switch (what) {
                case HttpType.REFRESH:
                    ResultItem item = resultItem.getItem( "data" );
                    initView( item );
                    break;
                case HttpType.LOADING:
									ToastCustom.makeText( this, "\u9886\u53d6\u6210\u529f", ToastCustom.LENGTH_SHORT ).show();
									button.setText( "\u5df2\u9886\u53d6" );
                    button.setBackground( getResources().getDrawable( R.drawable.radiu_gray ) );
                    break;
            }

        } else {
            ToastCustom.makeText( this, resultItem.getString( "msg" ), ToastCustom.LENGTH_SHORT ).show();
        }
    }

    @Override
    public void onError(int what, String error) {
        ToastCustom.makeText( this, error, ToastCustom.LENGTH_SHORT ).show();
    }

}
