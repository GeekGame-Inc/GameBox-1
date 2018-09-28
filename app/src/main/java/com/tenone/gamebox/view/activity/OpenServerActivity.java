package com.tenone.gamebox.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.TodayOpenFragmentView;
import com.tenone.gamebox.presenter.TodayOpenFragmentPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;

public class OpenServerActivity extends BaseActivity implements
        TodayOpenFragmentView {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBarView;

    @ViewInject(R.id.id_todayOpen_refresh)
    RefreshLayout refreshLayout;
    @ViewInject(R.id.id_todayOpen_listView)
    ListView listView;
    @ViewInject(R.id.id_openServer_todayTv)
    TextView todayTv;
    @ViewInject(R.id.id_openServer_jjTv)
    TextView jjTv;
    @ViewInject(R.id.id_openServer_ykTv)
    TextView ykTv;

    private TodayOpenFragmentPresenter presenter;
    private int platform = 1;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_open_server_tab );
        ViewUtils.inject( this );
        titleBarView.setTitleText(getString( R.string.openServerTab ) );
        titleBarView.setLeftImg( R.drawable.icon_back_grey );
        titleBarView.setOnClickListener( R.id.id_titleBar_leftImg, v -> finish() );
        Intent intent = getIntent();
        if (intent.hasExtra( "platform" )) {
            platform = intent.getIntExtra( "platform", 1 );
        }
        presenter = new TodayOpenFragmentPresenter( this, this, platform );
        presenter.initView();
        presenter.request();
    }

    @Override
    public RefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    @Override
    public ListView getListView() {
        return listView;
    }

    @Override
    public TextView getJRTv() {
        return todayTv;
    }

    @Override
    public TextView getJJTv() {
        return jjTv;
    }

    @Override
    public TextView getYKTv() {
        return ykTv;
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
