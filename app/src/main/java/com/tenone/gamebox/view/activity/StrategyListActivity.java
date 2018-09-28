

package com.tenone.gamebox.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.view.StrategyListView;
import com.tenone.gamebox.presenter.StrategyListPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;


public class  StrategyListActivity extends BaseActivity implements
        StrategyListView {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBarView;
    @ViewInject(R.id.id_strategyList_refreshLayout)
    RefreshLayout refreshLayout;
    @ViewInject(R.id.id_strategyList_listView)
    ListView listView;
    private StrategyListPresenter presenter;
    private int platform = 1;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_strategy_list );
        ViewUtils.inject( this );
        Intent intent = getIntent();
        if (intent.hasExtra( "platform" )) {
            platform = intent.getIntExtra( "platform", 1 );
        }
        presenter = new StrategyListPresenter( this, this,platform );
        presenter.initView();
        presenter.setAdapter();
        presenter.initListener();
        presenter.requestHttp( HttpType.REFRESH );
    }

    @Override
    public TitleBarView getTitleBarView() {
        return titleBarView;
    }

    @Override
    public RefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    @Override
    public ListView getListView() {
        return listView;
    }
}
