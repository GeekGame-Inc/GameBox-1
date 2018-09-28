/**
 * Project Name:GameBox
 * File Name:PrivilegePresenter.java
 * Package Name:com.tenone.gamebox.presenter
 * Date:2017-4-13����3:40:31
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.PrivilegeBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.PrivilegeMode;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.PrivilegeView;
import com.tenone.gamebox.view.activity.WebActivity;
import com.tenone.gamebox.view.adapter.PrivilegeAdapter;
import com.tenone.gamebox.view.custom.MyRefreshListView;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.HttpManager;

import java.util.ArrayList;
import java.util.List;

public class PrivilegePresenter extends BasePresenter implements
        OnItemClickListener, OnClickListener, HttpResultListener, SwipeRefreshLayout.OnRefreshListener {
    private PrivilegeBiz privilegeBiz;
    private PrivilegeView privilegeView;
    private Context mContext;
    private PrivilegeAdapter mAdapter;
    private List<PrivilegeMode> items = new ArrayList<PrivilegeMode>();
    private int platform = 1;
    private AlertDialog alertDialog;

    public PrivilegePresenter(Context cxt, PrivilegeView view, int platform) {
        this.mContext = cxt;
        this.platform = platform;
        this.privilegeBiz = new PrivilegeBiz();
        this.privilegeView = view;
    }

    public void initView() {
        alertDialog = buildProgressDialog( mContext );
        getTitleBarView().setTitleText( R.string.activity );
        getTitleBarView().setLeftImg( R.drawable.icon_xqf_b );
    }

    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new PrivilegeAdapter( items, mContext );
        }
        getListView().setAdapter( mAdapter );
    }

    public void initListener() {
        getListView().setOnItemClickListener( this );
        getTitleBarView().getLeftImg().setOnClickListener( this );
        getRefreshLayout().setOnRefreshListener( this );
    }

    public void requestHttp(int what) {
        HttpManager.exclusiveList( what, mContext, this, platform);
    }

    private TitleBarView getTitleBarView() {
        return privilegeView.getTitleBarView();
    }

    private MyRefreshListView getListView() {
        return privilegeView.getListView();
    }

    private RefreshLayout getRefreshLayout() {
        return privilegeView.getRefreshLayout();
    }

    private List<PrivilegeMode> getPrivilegeModes(List<ResultItem> resultItems) {
        return privilegeBiz.getPrivilegeModes( resultItems );
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        PrivilegeMode mode = items.get( position );
        // ��һ����ҳ
        String url = mode.getDetailsUrl();
        String title = "�����";
        mContext.startActivity( new Intent( mContext, WebActivity.class )
                .putExtra( "url", url ).putExtra( "title", title ) );
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_titleBar_leftImg) {
            close( mContext );
        }
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        cancelProgressDialog( alertDialog );
        getRefreshLayout().setRefreshing( false );
        getRefreshLayout().setLoading( false );
        if (1 == resultItem.getIntValue( "status" )) {
            List<ResultItem> list = resultItem.getItems( "data" );
            if (what == HttpType.REFRESH) {
                items.clear();
            }
            if (list != null) {
                items.addAll( getPrivilegeModes( list ) );
            }
            mAdapter.notifyDataSetChanged();
        } else {
            showToast( mContext, resultItem.getString( "msg" ) );
        }
    }

    @Override
    public void onError(int what, String error) {
        cancelProgressDialog( alertDialog );
        getRefreshLayout().setRefreshing( false );
        getRefreshLayout().setLoading( false );
        showToast( mContext, error );
    }

    @Override
    public void onRefresh() {
        requestHttp( HttpType.REFRESH );
    }

}
