/**
 * Project Name:GameBox
 * File Name:BeAboutToOpenPresenter.java
 * Package Name:com.tenone.gamebox.presenter
 * Date:2017-3-29����11:01:30
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

import com.tenone.gamebox.mode.biz.BeAboutToOpenBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnDataChangeListener;
import com.tenone.gamebox.mode.listener.OpenItemClickListener;
import com.tenone.gamebox.mode.listener.OpenServiceWarnListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.OpenServerMode;
import com.tenone.gamebox.mode.mode.OpenServiceNotificationMode;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.BeAboutToOpenView;
import com.tenone.gamebox.view.activity.GameDetailsActivity;
import com.tenone.gamebox.view.activity.GameDetailsNewActivity;
import com.tenone.gamebox.view.activity.LoginActivity;
import com.tenone.gamebox.view.adapter.BeAboutToOpenAdapter;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.service.AlarmService;
import com.tenone.gamebox.view.utils.DatabaseUtils;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * ClassName:BeAboutToOpenPresenter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-29 ����11:01:30 <br/>
 *
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class BeAboutToOpenPresenter extends BasePresenter implements
        HttpResultListener, OnRefreshListener, OnLoadListener,
        OpenItemClickListener, OpenServiceWarnListener, OnDataChangeListener {
    BeAboutToOpenView fragmentView;
    BeAboutToOpenBiz fragmentBiz;
    Context mContext;
    BeAboutToOpenAdapter mAdapter;
    int page = 1;
    List<OpenServerMode> modes = new ArrayList<OpenServerMode>();
    DeleteBroadcastReceiver broadcastReceiver;

    public BeAboutToOpenPresenter(BeAboutToOpenView v, Context cxt) {
        this.fragmentView = v;
        this.mContext = cxt;
        this.fragmentBiz = new BeAboutToOpenBiz();
    }

    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new BeAboutToOpenAdapter( modes, mContext );
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
                .add( "page", page + "" ).add( "system", "1" ).add( "type", 2 + "" )
                .build();
        HttpUtils.postHttp( mContext, what, url, requestBody, this );
    }

    public void initListener() {
        getRefreshLayout().setOnRefreshListener( this );
        getRefreshLayout().setOnLoadListener( this );
        mAdapter.setOpenItemClickListener( this );
        mAdapter.setOpenServiceWarnListener( this );
        broadcastReceiver = new DeleteBroadcastReceiver( this );
        registerDeleteBroadcastReceiver( broadcastReceiver, mContext );
    }

    public RefreshLayout getRefreshLayout() {
        return fragmentView.getRefreshLayout();
    }

    public ListView getListView() {
        return fragmentView.getListView();
    }

    public List<OpenServerMode> getModes(List<ResultItem> resultItems) {
        return fragmentBiz.getModes( resultItems, mContext );
    }

    public OpenServiceNotificationMode getNotificationMode(OpenServerMode mode) {
        return fragmentBiz.getNotificationMode( mode );
    }

    public void openAlarm(OpenServiceNotificationMode mode, String flags) {
        Intent intent = new Intent( mContext, AlarmService.class );
        intent.putExtra( "OpenServiceNotificationMode", mode );
        intent.setAction( flags );
        mContext.startService( intent );
    }

    public void comparisonSql(List<OpenServerMode> modes) {
        fragmentBiz.comparisonSql( mContext, modes );
    }

    public void onDestroy() {
        unDeleteBroadcastReceiver( mContext, broadcastReceiver );
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
    public void onOpenServiceClick(OpenServerMode mode) {
        if ("0".equals( SpUtil.getUserId() )) {
            openOtherActivity( mContext, new Intent( mContext,
                    LoginActivity.class ) );
            return;
        }
        String flag = "";
        OpenServiceNotificationMode notificationMode = getNotificationMode( mode );
        if (!mode.isNotification()) {
            mode.setNotification( true );
            flag = "insert";
            // ���뵽���ݿ�
            DatabaseUtils.getInstanse( mContext ).insertWarn( notificationMode );
        } else {
            flag = "delete";
            // �����ݿ�ɾ��
            mode.setNotification( false );
            DatabaseUtils.getInstanse( mContext ).deleteWarn( notificationMode );
        }
        openAlarm( notificationMode, flag );
        mAdapter.notifyDataSetChanged();
    }

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

    @Override
    public void onDataChange() {
        comparisonSql( modes );
        mAdapter.notifyDataSetChanged();
    }
}
