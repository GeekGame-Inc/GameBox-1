package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gyf.barlibrary.ImmersionBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.adapter.TradingCustomerAdapter;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class TradingCustomerActivity extends AppCompatActivity implements HttpResultListener, SwipeRefreshLayout.OnRefreshListener, TradingCustomerAdapter.OnQQClickListener {
    @ViewInject(R.id.id_trading_customer_refresh)
    SwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.id_trading_customer_recycler)
    RecyclerView recyclerView;
    @ViewInject(R.id.id_trading_customer_toolbar)
    Toolbar toolbar;

    private List<String> qqArray = new ArrayList<String>();
    private TradingCustomerAdapter adapter;
    private ImmersionBar immersionBar;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.actvitity_trading_customer );
        ViewUtils.inject( this );
        immersionBar = ImmersionBar.with( this );
        immersionBar.titleBar( toolbar ).statusBarDarkFont( true ).init();
        initTitle();
        initView();

    }

    private void initTitle() {
        toolbar.setTitle( "" );
        toolbar.setContentInsetsAbsolute( 0, 0 );
        setSupportActionBar( toolbar );
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled( true );
        }
        toolbar.setNavigationOnClickListener( v -> {
            finish();
        } );
    }

    private void initView() {
        LinearLayoutManager manager = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false );
        recyclerView.setLayoutManager( manager );
        adapter = new TradingCustomerAdapter( this, qqArray );
        adapter.setOnQQClickListener( this );
        recyclerView.setAdapter( adapter );
        refreshLayout.setRefreshing( true );
        HttpManager.customer( HttpType.REFRESH, this, this );
        refreshLayout.setOnRefreshListener( this );
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        refreshLayout.setRefreshing( false );
        if (1 == resultItem.getIntValue( "status" )) {
            List<ResultItem> data = resultItem.getItems( "data" );
            setData( data );
        } else {
            ToastUtils.showToast( this, resultItem.getString( "msg" ) );
        }
    }

    private void setData(List<ResultItem> data) {
        qqArray.clear();
        if (!BeanUtils.isEmpty( data )) {
            for (int i = 0; i < data.size(); i++) {
                String qq = String.valueOf( data.get( i ) );
                qqArray.add( qq );
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(int what, String error) {
        refreshLayout.setRefreshing( false );
        ToastUtils.showToast( this, error );
    }

    @Override
    public void onRefresh() {
        HttpManager.customer( HttpType.REFRESH, this, this );
    }

    @Override
    public void onQQClick(String qq) {
        BeanUtils.startQQ( this, qq, "" );
    }

    @Override
    protected void onDestroy() {
        if (immersionBar != null) {
            immersionBar.destroy();
        }
        super.onDestroy();
    }
}
