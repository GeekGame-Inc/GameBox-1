package com.tenone.gamebox.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.AboutView;
import com.tenone.gamebox.presenter.AboutPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.TitleBarView;
@SuppressLint("ResourceAsColor")
public class AboutActivity extends BaseActivity implements AboutView {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBarView;
    @ViewInject(R.id.id_about_http)
    TextView webTv;
    @ViewInject(R.id.id_about_webo)
    TextView weboTv;
    @ViewInject(R.id.id_about_version)
    TextView versionTv;
    @ViewInject(R.id.id_about_weChat)
    TextView weChatTv;
    @ViewInject(R.id.id_about_appName)
    TextView appNameTv;


    private AboutPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_about );
        ViewUtils.inject( this );
        presenter = new AboutPresenter( this, this );
        presenter.initView();
        presenter.initListener();
        appNameTv.setText( getResources().getString( R.string.app_name ) );
    }

    @Override
    public TitleBarView getTitleBarView() {
        return titleBarView;
    }

    @Override
    public TextView getVersionTextView() {
        return versionTv;
    }

    @Override
    public TextView getWebTextView() {
        return webTv;
    }

    @Override
    public TextView getWeboTextView() {
        return weboTv;
    }

    @Override
    public TextView getWeChatTextView() {
        return weChatTv;
    }
}
