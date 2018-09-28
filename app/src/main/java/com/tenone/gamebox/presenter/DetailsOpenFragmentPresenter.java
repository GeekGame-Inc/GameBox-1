package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.DetailsOpenFragmentBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OpenServiceWarnListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.OpenServerMode;
import com.tenone.gamebox.mode.mode.OpenServiceNotificationMode;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.DetailsOpenFragmentView;
import com.tenone.gamebox.view.activity.LoginActivity;
import com.tenone.gamebox.view.adapter.DetailsOpenAdapter;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.SpacesItemDecoration;
import com.tenone.gamebox.view.service.AlarmService;
import com.tenone.gamebox.view.utils.DatabaseUtils;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class DetailsOpenFragmentPresenter extends BasePresenter implements
        OpenServiceWarnListener, HttpResultListener, OnRefreshListener {
    DetailsOpenFragmentView fragmentView;
    DetailsOpenFragmentBiz fragmentBiz;
    Context mContext;
    DetailsOpenAdapter mAdapter;
    List<OpenServerMode> items = new ArrayList<OpenServerMode>();

    public DetailsOpenFragmentPresenter(DetailsOpenFragmentView v, Context cxt) {
        this.fragmentView = v;
        this.mContext = cxt;
        this.fragmentBiz = new DetailsOpenFragmentBiz();
    }

    public void setAdapter() {
        mAdapter = new DetailsOpenAdapter( items, mContext );
        getListView().setLayoutManager( new LinearLayoutManager( mContext ) );
        getListView().addItemDecoration(  new SpacesItemDecoration( mContext, LinearLayoutManager.VERTICAL,
                1, mContext.getResources().getColor( R.color.divider ) ) );
        getListView().setAdapter( mAdapter );
    }

    public void initListener() {
        mAdapter.setOpenServiceWarnListener( this );
        getRefreshLayout().setOnRefreshListener( this );
    }

    public void requestHttp(int what) {
        String url = MyApplication.getHttpUrl().getGameOpenServer();
        RequestBody requestBody = new FormBody.Builder()
                .add( "gid", getGameId() ).build();
        HttpUtils.postHttp( mContext, what, url, requestBody, this );
    }

    public String getGameId() {
        return fragmentView.getGameId();
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return fragmentView.getRefreshLayout();
    }

    public RecyclerView getListView() {
        return fragmentView.getListView();
    }

    public List<OpenServerMode> getModes(List<ResultItem> resultItems) {
        return fragmentBiz.getModes( getGameModel(), resultItems, mContext );
    }

    public GameModel getGameModel() {
        return fragmentView.getGameModel();
    }

    public OpenServiceNotificationMode getNotificationMode(OpenServerMode mode) {
        return fragmentBiz.getNotificationMode( mode, getGameModel() );
    }

    public void openAlarm(OpenServiceNotificationMode mode, String flags) {
        Intent intent = new Intent( mContext, AlarmService.class );
        intent.putExtra( "OpenServiceNotificationMode", mode );
        intent.setAction( flags );
        mContext.startService( intent );
    }

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
            DatabaseUtils.getInstanse( mContext ).insertWarn( notificationMode );
        } else {
            flag = "delete";
            mode.setNotification( false );
            DatabaseUtils.getInstanse( mContext ).deleteWarn( notificationMode );
        }
        openAlarm( notificationMode, flag );
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        getRefreshLayout().setRefreshing( false );
        if ("0".equals( resultItem.getString( "status" ) )) {
            Message message = new Message();
            message.what = what;
            message.obj = resultItem;
            handler.sendMessage( message );
        } else {
            showToast( mContext, resultItem.getString( "msg" ) );
        }
    }

    @Override
    public void onError(int what, String error) {
        getRefreshLayout().setRefreshing( false );
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ResultItem resultItem = (ResultItem) msg.obj;
            switch (msg.what) {
                case HttpType.REFRESH:
                    if (resultItem != null) {
                        List<ResultItem> array = resultItem.getItems( "data" );
                        items.clear();
                        items.addAll( getModes( array ) );
                    }
                    break;
            }
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onRefresh() {
        requestHttp( HttpType.REFRESH );
    }
}
