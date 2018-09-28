package com.tenone.gamebox.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnStrategyItemClickListener;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.StrategyMode;
import com.tenone.gamebox.view.activity.WebActivity;
import com.tenone.gamebox.view.adapter.StrategyAdapter;
import com.tenone.gamebox.view.base.BaseFragment;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DataStateChangeCheck;
import com.tenone.gamebox.view.utils.HttpManager;

import java.util.ArrayList;
import java.util.List;

public class StrategyFragment extends BaseFragment implements HttpResultListener, SwipeRefreshLayout.OnRefreshListener, OnStrategyItemClickListener, DataStateChangeCheck.LoadDataListener {
    View view;
    @ViewInject(R.id.id_strategy_refresh)
    SwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.id_strategy_listView)
    RecyclerView listView;

    private StrategyAdapter adapter;
    List<StrategyMode> items = new ArrayList<StrategyMode>();
    private int page = 1;
    private DataStateChangeCheck dataStateChangeCheck;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate( R.layout.layout_strategy, container,
                    false );
        }
        ViewUtils.inject( this, view );
        initView();
        HttpManager.articleList( 0, getActivity(), this, 2, page );
        return view;
    }

    private void initView() {
        dataStateChangeCheck = new DataStateChangeCheck( listView );
        refreshLayout.setOnRefreshListener( this );
        refreshLayout.setRefreshing( true );
        adapter = new StrategyAdapter( items, getActivity() );
        adapter.setOnStrategyItemClickListener( this );
        LinearLayoutManager manager = new LinearLayoutManager( getActivity(), LinearLayoutManager.VERTICAL, false );
        listView.setLayoutManager( manager );
        listView.setAdapter( adapter );
        listView.addItemDecoration( new DividerItemDecoration( getActivity(), LinearLayoutManager.VERTICAL ) );
        listView.addOnScrollListener( dataStateChangeCheck );
        dataStateChangeCheck.setLoadDataListener( this );
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        refreshLayout.setRefreshing( false );
        if (0 == resultItem.getIntValue( "status" )) {
            ResultItem data = resultItem.getItem( "data" );
            List<ResultItem> resultItems = data.getItems( "list" );
            if (what == 0) {
                items.clear();
            }
            if (!BeanUtils.isEmpty( resultItems )) {
                setData( resultItems );
            }
        }
    }

    @Override
    public void onError(int what, String error) {
        refreshLayout.setRefreshing( false );
    }

    @Override
    public void onRefresh() {
        page = 1;
        HttpManager.articleList( 0, getActivity(), this, 2, page );
    }

    @Override
    public void onStrategtItemClick(StrategyMode mode) {
        startActivity( new Intent( getActivity(), WebActivity.class )
                .putExtra( "title", "��������" )
                .putExtra( "url", mode.getUrl() ) );
    }

    public void setData(List<ResultItem> data) {
        for (ResultItem item : data) {
            StrategyMode mode = new StrategyMode();
            mode.setGameName( item.getString( "gamename" ) );
            mode.setStrategyId( item.getIntValue( "tid" ) );
            mode.setStrategyName( item.getString( "title" ) );
            mode.setStrategyImgUrl( "http://www.185sy.com" + item.getString( "logo" ) );
            mode.setWriter( item.getString( "author" ) );
            mode.setTime( item.getString( "release_time" ) );
            mode.setUrl( item.getString( "info_url" ) );
            items.add( mode );
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadNextPage(View view) {
        page++;
        HttpManager.articleList( 1, getActivity(), this, 2, page );
    }

    @Override
    public void onRefreshPage(View view) {

    }
}
