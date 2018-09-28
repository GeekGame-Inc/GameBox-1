package com.tenone.gamebox.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.view.GameClassifyTabView;
import com.tenone.gamebox.presenter.GameClassifyTabPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;

public class GameClassifyTab extends BaseActivity implements
        GameClassifyTabView {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBar;
    @ViewInject(R.id.id_classifyTab_refreshLayout)
    RefreshLayout refreshLayout;
    @ViewInject(R.id.id_classifyTab_listView)
    ListView listView;

    private GameClassifyTabPresenter classifyTabPresenter;
    private int platform = 1;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_game_classify );
        ViewUtils.inject( this );
        Intent intent = getIntent();
        if (intent.hasExtra( "platform" )) {
            platform = intent.getIntExtra( "platform", 1 );
        }
        classifyTabPresenter = new GameClassifyTabPresenter( this, this ,platform);
        classifyTabPresenter.init();
        classifyTabPresenter.setAdapter();
        classifyTabPresenter.initListener();
        classifyTabPresenter.requestHttp( HttpType.REFRESH );
    }

    @Override
    public TitleBarView getTitleBarView() {
        return titleBar;
    }

    @Override
    public RefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    @Override
    public ListView getRecommendListView() {
        return listView;
    }
}
