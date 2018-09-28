package com.tenone.gamebox.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnRemoveItemListViewDeleteClickListener;
import com.tenone.gamebox.mode.listener.OnTabLayoutTextToLeftRightListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.TradingRecordModel;
import com.tenone.gamebox.view.activity.TradingSellActivity;
import com.tenone.gamebox.view.adapter.InTheWareHouseAdapter;
import com.tenone.gamebox.view.base.BaseLazyFragment;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RemoveItemListView;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.MyLog;
import com.tenone.gamebox.view.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class InTheWarehouseFragment extends BaseLazyFragment implements Runnable, SwipeRefreshLayout.OnRefreshListener, OnTabLayoutTextToLeftRightListener, HttpResultListener, RefreshLayout.OnLoadListener, InTheWareHouseAdapter.OnBtClickListener, OnRemoveItemListViewDeleteClickListener {
    @ViewInject(R.id.id_record_refresh)
    RefreshLayout refreshLayout;
    @ViewInject(R.id.id_record_listview)
    RemoveItemListView listView;

    private List<TradingRecordModel> models = new ArrayList<TradingRecordModel>();
    private InTheWareHouseAdapter adapter;
    private Context context;
    private int status = 5, page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        View view = inflater.inflate( R.layout.fragment_inthewarehouse, container, false );
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
            model.setSaleEnabled( item.getBooleanValue( "onsale_enabled", 1 ) );
            models.add( model );
        }
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

    @Override
    public void run() {
        adapter = new InTheWareHouseAdapter( context, models );
        listView.post( () -> {
            listView.setAdapter( adapter );
            refreshLayout.setOnRefreshListener( this );
            refreshLayout.setOnLoadListener( this );
            ListenerManager.registerOnTabLayoutTextToLeftRightListener( this );
            listView.setmListener( this );
            adapter.setOnBtClickListener( this );
        } );
    }

    @Override
    public void onDestroy() {
        ListenerManager.unRegisterOnTabLayoutTextToLeftRightListener( this );
        super.onDestroy();
    }


    @Override
    public void onBtClick(String productId) {
        startActivityForResult( new Intent( context, TradingSellActivity.class )
                .putExtra( "productId", productId ), 16 );
    }

    @Override
    public void onRemoveItemListViewItemClick(View view, int position) {

    }

    @Override
    public void onRemoveItemListViewDeleteClick(int position) {
        TradingRecordModel model = models.get( position );
        HttpManager.deleteProduct( 4, context, new HttpResultListener() {
            @Override
            public void onSuccess(int what, ResultItem resultItem) {
                if (1 == resultItem.getIntValue( "status" )) {
                    ToastUtils.showToast( context, "\u5220\u9664\u6210\u529f" );
                    models.remove( model );
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showToast( context, resultItem.getString( "msg" ) );
                }
            }

            @Override
            public void onError(int what, String error) {
                ToastUtils.showToast( context, error );
            }
        }, model.getProductId() );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        MyLog.d( "onActivityResult" );
        if (requestCode == 16 && resultCode == Activity.RESULT_OK) {
            refreshLayout.setRefreshing( true );
            request( HttpType.REFRESH );
        }
        super.onActivityResult( requestCode, resultCode, data );
    }
}
