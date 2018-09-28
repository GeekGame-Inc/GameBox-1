package com.tenone.gamebox.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnTabLayoutTextToLeftRightListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.TradingRecordModel;
import com.tenone.gamebox.view.activity.TradingProductDetailsActivity;
import com.tenone.gamebox.view.adapter.TradingRecordAdapter;
import com.tenone.gamebox.view.base.BaseLazyFragment;
import com.tenone.gamebox.view.custom.MyRefreshListView;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class InTheSaleFragment extends BaseLazyFragment implements Runnable, SwipeRefreshLayout.OnRefreshListener, OnTabLayoutTextToLeftRightListener, HttpResultListener, RefreshLayout.OnLoadListener, TradingRecordAdapter.OnBtClickListener, AdapterView.OnItemClickListener {
    @ViewInject(R.id.id_record_refresh)
    RefreshLayout refreshLayout;
    @ViewInject(R.id.id_record_listview)
    MyRefreshListView listView;

    private List<TradingRecordModel> models = new ArrayList<TradingRecordModel>();
    private TradingRecordAdapter adapter;
    private Context context;
    private int status = 2, page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate( R.layout.fragment_trading_record, container, false );
        ViewUtils.inject( this, view );
        initView();
        return view;
    }

    private void initView() {
        new Thread( this ).start();
    }


    private void request(int what) {
        HttpManager.getProductByUser( what, context, this, status, page );
    }

    @Override
    public void onLazyLoad() {
        refreshLayout.setRefreshing( true );
        request( HttpType.REFRESH );
    }

    @Override
    public void onRefresh() {
        page = 1;
        request( HttpType.REFRESH );
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        refreshLayout.setRefreshing( false );
        refreshLayout.setLoading( false );
        if (1 == resultItem.getIntValue( "status" )) {
            if (what == HttpType.REFRESH) {
                models.clear();
            }
            ResultItem data = resultItem.getItem( "data" );
            List<ResultItem> list = data.getItems( "list" );
            if (!BeanUtils.isEmpty( list )) {
                setData( list );
            }
            adapter.notifyDataSetChanged();
        } else {
            ToastUtils.showToast( context, resultItem.getString( "msg" ) );
        }
    }

    @Override
    public void onError(int what, String error) {
        refreshLayout.setRefreshing( false );
        refreshLayout.setLoading( false );
        ToastUtils.showToast( context, error );
    }

    @Override
    public void onTabLayoutTextToLeftRight(int margin) {
        if (adapter != null) {
            adapter.setPadding( margin );
            adapter.setSetPadding( true );
        }
    }

    @Override
    public void onLoad() {
        page++;
        request( HttpType.LOADING );
    }


    private void setData(List<ResultItem> data) {
        for (ResultItem item : data) {
            TradingRecordModel model = new TradingRecordModel();
            model.setGameName( item.getString( "game_name" ) );
            model.setMoney( item.getString( "price" ) );
            model.setStatus( item.getIntValue( "status" ) );
            model.setTime( item.getString( "create_time" ) );
            model.setTitle( item.getString( "title" ) );
            model.setReason( item.getString( "off_reason" ) );
            model.setProductId( item.getString( "id" ) );
            models.add( model );
        }
    }

    @Override
    public void run() {
        adapter = new TradingRecordAdapter( context, models, 2 );
        listView.post( () -> {
            listView.setAdapter( adapter );
            listView.setOnItemClickListener( this );
            refreshLayout.setOnRefreshListener( this );
            refreshLayout.setOnLoadListener( this );
            ListenerManager.registerOnTabLayoutTextToLeftRightListener( this );
            adapter.setOnBtClickListener( this );
        } );
    }


    @Override
    public void onDestroy() {
        ListenerManager.unRegisterOnTabLayoutTextToLeftRightListener( this );
        super.onDestroy();
    }

    @Override
    public void onBtClick(String productId, int position) {
        buildProgressDialog();
        HttpManager.withdrawProduct( 25, context, new HttpResultListener() {
            @Override
            public void onSuccess(int what, ResultItem resultItem) {
                cancelProgressDialog();
                if (1 == resultItem.getIntValue( "status" )) {
                    ToastUtils.showToast( context, "下架成功" );
                    models.remove( position );
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showToast( context, resultItem.getString( "msg" ) );
                }
            }

            @Override
            public void onError(int what, String error) {
                cancelProgressDialog();
                ToastUtils.showToast( context, error );
            }
        }, productId );
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TradingRecordModel model = models.get( position );
        startActivity( new Intent( getActivity(), TradingProductDetailsActivity.class )
                .putExtra( "productId", model.getProductId() ) );
    }
}
