/**
 * Project Name:GameBox
 * File Name:GameSearchResultActivity.java
 * Package Name:com.tenone.gamebox.view.activity
 * Date:2017-4-25����5:15:38
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.view.GameSearchResultView;
import com.tenone.gamebox.presenter.GameSearchResultPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.SearchTitleBarView;

/**
 * ��Ϸ�������ҳ ClassName:GameSearchResultActivity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-25 ����5:15:38 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */
@SuppressLint("ResourceAsColor")
public class GameSearchResultActivity extends BaseActivity implements
        GameSearchResultView {
    @ViewInject(R.id.id_searchBar)
    SearchTitleBarView searchTitleBarView;
    @ViewInject(R.id.id_gameSearchResult_refresh)
    RefreshLayout refreshLayout;
    @ViewInject(R.id.id_gameSearchResult_listView)
    ListView listView;

    private GameSearchResultPresenter presenter;
    private int platform = 1;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_search_result_game );
        ViewUtils.inject( this );
        Intent intent = getIntent();
        if (intent.hasExtra( "platform" )) {
            platform = intent.getIntExtra( "platform", 1 );
        }
        presenter = new GameSearchResultPresenter( this, this, platform );
        presenter.initView();
        presenter.setAdapter();
        presenter.initListener();
        presenter.requestList( HttpType.REFRESH );
    }

    @Override
    public SearchTitleBarView getSearchTitleBarView() {
        return searchTitleBarView;
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
