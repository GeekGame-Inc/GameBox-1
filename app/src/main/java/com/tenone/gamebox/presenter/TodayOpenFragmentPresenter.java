

package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tenone.gamebox.mode.biz.TodayOpenFragmentBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnDataChangeListener;
import com.tenone.gamebox.mode.listener.OpenItemClickListener;
import com.tenone.gamebox.mode.listener.OpenServiceWarnListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.OpenServerMode;
import com.tenone.gamebox.mode.mode.OpenServiceNotificationMode;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.TodayOpenFragmentView;
import com.tenone.gamebox.view.activity.LoginActivity;
import com.tenone.gamebox.view.activity.NewGameDetailsActivity;
import com.tenone.gamebox.view.adapter.TodayOpenFragmentAdapter;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.service.AlarmService;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DatabaseUtils;
import com.tenone.gamebox.view.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;


public class TodayOpenFragmentPresenter extends BasePresenter implements
        HttpResultListener, OnRefreshListener, OnLoadListener,
        OpenServiceWarnListener, OpenItemClickListener, OnDataChangeListener {
    private TodayOpenFragmentView fragmentView;
    private TodayOpenFragmentBiz fragmentBiz;
    private Context mContext;
    private TodayOpenFragmentAdapter mAdapter;
    private int page = 1, currentType = 1, platform = 1;
    private List<OpenServerMode> modes = new ArrayList<OpenServerMode>();
    private DeleteBroadcastReceiver broadcastReceiver;

    public TodayOpenFragmentPresenter(TodayOpenFragmentView v, Context cxt, int platform) {
        this.fragmentView = v;
        this.platform = platform;
        this.mContext = cxt;
        this.fragmentBiz = new TodayOpenFragmentBiz();
        getJRTv().setSelected( true );
    }

    public void initView() {
        setAdapter();
        initListener();
    }

    public void request() {
        requestHttp( HttpType.REFRESH );
    }

    private void setAdapter() {
        mAdapter = new TodayOpenFragmentAdapter( modes, mContext );
        getListView().setAdapter( mAdapter );
    }

    private void initListener() {
        getRefreshLayout().setOnRefreshListener( this );
        getRefreshLayout().setOnLoadListener( this );
        mAdapter.setOpenServiceWarnListener( this );
        mAdapter.setOpenItemClickListener( this );
        broadcastReceiver = new DeleteBroadcastReceiver( this );
        registerDeleteBroadcastReceiver( broadcastReceiver, mContext );
        getJRTv().setOnClickListener( v -> setTextViewSelected( 1 ) );
        getJJTv().setOnClickListener( v -> setTextViewSelected( 2 ) );
        getYKTv().setOnClickListener( v -> setTextViewSelected( 3 ) );
    }

    private void setTextViewSelected(int currentType) {
        getJRTv().setSelected( 1 == currentType );
        getJJTv().setSelected( 2 == currentType );
        getYKTv().setSelected( 3 == currentType );
        page = 1;
        this.currentType = currentType;
        getRefreshLayout().setRefreshing( true );
        requestHttp( HttpType.REFRESH );
    }

    
    private void requestHttp(int what) {
        String url = MyApplication.getHttpUrl().getOpenServer();
        RequestBody requestBody = new FormBody.Builder()
                .add( "channel", MyApplication.getConfigModle().getChannelID() )
                .add( "page", page + "" )
                .add( "system", "1" )
                .add( "type", currentType + "" )
                .add( "platform", platform + "" )
                .build();
        HttpUtils.postHttp( mContext, what, url, requestBody, this );
    }

    private RefreshLayout getRefreshLayout() {
        return fragmentView.getRefreshLayout();
    }

    private TextView getJRTv() {
        return fragmentView.getJRTv();
    }

    private TextView getJJTv() {
        return fragmentView.getJJTv();
    }

    private TextView getYKTv() {
        return fragmentView.getYKTv();
    }

    private ListView getListView() {
        return fragmentView.getListView();
    }

    private List<OpenServerMode> getModes(List<ResultItem> resultItems) {
        return fragmentBiz.getModes( resultItems, mContext );
    }

    private OpenServiceNotificationMode getNotificationMode(OpenServerMode mode) {
        return fragmentBiz.getNotificationMode( mode );
    }

    private void openAlarm(OpenServiceNotificationMode mode, String flags) {
        Intent intent = new Intent( mContext, AlarmService.class );
        intent.putExtra( "OpenServiceNotificationMode", mode );
        intent.setAction( flags );
        mContext.startService( intent );
    }

    private void comparisonSql(List<OpenServerMode> modes) {
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
            showToast( mContext, resultItem.getString( "msg" ) );
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
    public void onOpenServiceClick(OpenServerMode mode) {
        if (!BeanUtils.isLogin()) {
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
    public void onOpenItemClick(OpenServerMode mode) {
        openOtherActivity( mContext, new Intent( mContext, NewGameDetailsActivity.class )
                .putExtra( "id", mode.getGameId() ) );
    }

    @Override
    public void onDataChange() {
        comparisonSql( modes );
        mAdapter.notifyDataSetChanged();
    }
}
