/**
 * Project Name:GameBox
 * File Name:AlreadyOpenFragmentPresenter.java
 * Package Name:com.tenone.gamebox.presenter
 * Date:2017-3-29����11:32:54
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.ListView;

import com.tenone.gamebox.mode.biz.AlreadyOpenFragmentBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OpenItemClickListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.OpenServerMode;
import com.tenone.gamebox.mode.mode.OpenServiceNotificationMode;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.AlreadyOpenFragmentView;
import com.tenone.gamebox.view.activity.GameDetailsActivity;
import com.tenone.gamebox.view.activity.GameDetailsNewActivity;
import com.tenone.gamebox.view.adapter.AlreadyOpenFragmentAdapter;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * ClassName:AlreadyOpenFragmentPresenter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-29 ����11:32:54 <br/>
 *
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class AlreadyOpenFragmentPresenter extends BasePresenter implements
        HttpResultListener, OnRefreshListener, OnLoadListener,
        OpenItemClickListener {
    AlreadyOpenFragmentView fragmentView;
    AlreadyOpenFragmentBiz fragmentBiz;
    Context mContext;
    AlreadyOpenFragmentAdapter mAdapter;
    List<OpenServerMode> modes = new ArrayList<OpenServerMode>();
    int page = 1;

    public AlreadyOpenFragmentPresenter(AlreadyOpenFragmentView v, Context cxt) {
        this.fragmentView = v;
        this.mContext = cxt;
        this.fragmentBiz = new AlreadyOpenFragmentBiz();
    }

    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new AlreadyOpenFragmentAdapter( modes, mContext );
        }
        getListView().setAdapter( mAdapter );
    }

    /**
     * ��ȡ���� requestHttp:(������һ�仰�����������������). <br/>
     *
     * @author John Lie
     * @param what
     * @since JDK 1.6
     */
    public void requestHttp(int what) {
        String url = MyApplication.getHttpUrl().getOpenServer();
        RequestBody requestBody = new FormBody.Builder()
                .add( "channel", MyApplication.getConfigModle().getChannelID() )
                .add( "page", page + "" ).add( "system", "1" ).add( "type", 3 + "" )
                .build();
        HttpUtils.postHttp( mContext, what, url, requestBody, this );
    }

    public void initListener() {
        getRefreshLayout().setOnRefreshListener( this );
        getRefreshLayout().setOnLoadListener( this );
        mAdapter.setOpenItemClickListener( this );
    }

    public RefreshLayout getRefreshLayout() {
        return fragmentView.getRefreshLayout();
    }

    public ListView getListView() {
        return fragmentView.getListView();
    }

    public List<OpenServerMode> getModes(List<ResultItem> resultItems) {
        return fragmentBiz.getModes( resultItems );
    }

    public OpenServiceNotificationMode getNotificationMode(OpenServerMode mode) {
        return fragmentBiz.getNotificationMode( mode );
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        getRefreshLayout().setRefreshing( false );
        getRefreshLayout().setLoading( false );
        if ("0".equals( resultItem.getString( "status" ) )) {
            Message message = new Message();
            message.what = what;
            message.obj = resultItem;
            handler.sendMessage( message );
        } else {
            showToast( mContext, resultItem.getString( "" ) );
        }
    }

    @Override
    public void onError(int what, String error) {
        getRefreshLayout().setRefreshing( false );
        getRefreshLayout().setLoading( false );
        showToast( mContext, error );
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ResultItem resultItem = (ResultItem) msg.obj;
            List<ResultItem> items = resultItem.getItems( "data" );
            switch (msg.what) {
                case HttpType.REFRESH:
                    if (items != null) {
                        modes.clear();
                        modes.addAll( getModes( items ) );
                    }
                    break;
                case HttpType.LOADING:
                    if (items != null) {
                        modes.addAll( getModes( items ) );
                    }
                    break;
            }
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onOpenItemClick(OpenServerMode mode) {
        openOtherActivity( mContext, new Intent( mContext,
                Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ? GameDetailsActivity.class : GameDetailsNewActivity.class ).putExtra( "id", mode.getGameId() ) );
    }

    @Override
    public void onLoad() {
        page++;
        requestHttp( HttpType.LOADING );
    }

    @Override
    public void onRefresh() {
        page = 1;
        requestHttp( HttpType.REFRESH );
    }
}
