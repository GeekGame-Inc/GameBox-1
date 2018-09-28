package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.view.GameNewFragmentView;
import com.tenone.gamebox.presenter.GameNewFragmentPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;


public class GameNewActivity extends BaseActivity implements GameNewFragmentView {
    @ViewInject(R.id.id_new_refresh)
    RefreshLayout refreshLayout;
    @ViewInject(R.id.id_new_listView)
    ListView listView;
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBarView;

    private GameNewFragmentPresenter fragmentPresenter;
    private int platform = 1;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.fragment_game_new );
        ViewUtils.inject( this );
        platform = getIntent().getIntExtra( "platform", 1 );
        titleBarView.setTitleText( getString( R.string.new_game) );
        titleBarView.setLeftImg( R.drawable.icon_xqf_b );
        titleBarView.setOnClickListener( R.id.id_titleBar_leftImg, v -> {
            finish();
        } );
        fragmentPresenter = new GameNewFragmentPresenter( this, this, platform );
        fragmentPresenter.setAdapter();
        fragmentPresenter.initListener();
        fragmentPresenter.requestHttp( HttpType.REFRESH );
    }

    @Override
    public RefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    @Override
    public ListView getRecommendListView() {
        return listView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
