package com.tenone.gamebox.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnDriverItemClickListener;
import com.tenone.gamebox.mode.listener.OnFansChangeListener;
import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.activity.UserInfoActivity;
import com.tenone.gamebox.view.adapter.MineFansFragmentAdapter;
import com.tenone.gamebox.view.base.BaseLazyFragment;
import com.tenone.gamebox.view.custom.EmptyRecyclerView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;

import java.util.ArrayList;
import java.util.List;


public class MyAttentionFragment extends BaseLazyFragment implements HttpResultListener, SwipeRefreshLayout.OnRefreshListener, OnDriverItemClickListener, OnFansChangeListener {
    @ViewInject(R.id.id_fragment_fans_attention_refresh)
    SwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.id_fragment_fans_attention_recycler)
    EmptyRecyclerView recyclerView;
    @ViewInject(R.id.id_empty_root)
    View emptyView;
    private String uid;

    private List<DriverModel> models = new ArrayList<DriverModel>();
    private MineFansFragmentAdapter adapter;
    private int page = 1;
    private String totalNum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_fans_attention, container, false );
        ViewUtils.inject( this, view );
        Bundle bundle = getArguments();
        uid = bundle.getString( "uid" );
        initView();
        return view;
    }

    private void initView() {
        adapter = new MineFansFragmentAdapter( getActivity(), models );
        adapter.setUserId( uid );
        adapter.setOnDriverItemClickListener( this );
        LinearLayoutManager manager = new LinearLayoutManager( getActivity() );
        manager.setOrientation( LinearLayoutManager.VERTICAL );
        recyclerView.setLayoutManager( manager );
        recyclerView.setAdapter( adapter );
        refreshLayout.setOnRefreshListener( this );
        ListenerManager.registerOnFansChangeListeners( this );
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        refreshLayout.setRefreshing( false );
        if (what == HttpType.REFRESH) {
            models.clear();
        }
        if (1 == resultItem.getIntValue( "status" )) {
            ResultItem item = resultItem.getItem( "data" );
            if (!BeanUtils.isEmpty( item )) {
                setData( item );
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
        HttpManager.followList( HttpType.REFRESH, getActivity(), this, uid, page, 1 );
    }

    private void setData(ResultItem item) {
        List<ResultItem> items = item.getItems( "list" );
        if (!BeanUtils.isEmpty( items )) {
            emptyView.setVisibility( View.GONE );
            for (ResultItem r : items) {
                DriverModel model = new DriverModel();
                model.setNick( r.getString( "nickname" ) );
                model.setDriverId( r.getString( "uid" ) );
                model.setHeader( r.getString( "icon_url" ) );
                model.setFansNum( r.getString( "fans_counts" ) );
                model.setAttention( r.getIntValue( "follow_status" ) );
                model.setVip( r.getBooleanValue( "vip", 1 ) );
                model.setSex( r.getString( "sex" ) );
                models.add( model );
            }
            adapter.notifyDataSetChanged();
        } else {
            emptyView.setVisibility( View.VISIBLE );
        }
    }

    @Override
    public void onDriverHeaderClick(DriverModel model) {
        startActivity( new Intent( getActivity(), UserInfoActivity.class ).putExtra( "uid", model.getDriverId() ) );
    }

    @Override
    public void onAttentionClick(final DriverModel model) {
        HttpManager.followOrCancel( 20036, getActivity(),
                new HttpResultListener() {
                    @Override
                    public void onSuccess(int what, ResultItem resultItem) {
											if (1 == resultItem.getIntValue( "status" )) {
												ToastCustom.makeText( getActivity(),
														"\u64cd\u4f5c\u6210\u529f", ToastCustom.LENGTH_SHORT ).show();
											} else {
												ToastCustom.makeText( getActivity(),
														"\u64cd\u4f5c\u5931\u8d25", ToastCustom.LENGTH_SHORT ).show();
											}
                    }

                    @Override
                    public void onError(int what, String error) {
                        ToastCustom.makeText( getActivity(), error, ToastCustom.LENGTH_SHORT ).show();
                    }
                }, model.getDriverId(), (model.isAttention() == 0) ? 1 : 2 );
    }

    @Override
    public void onFansChange() {
        refreshLayout.setRefreshing( true );
        page = 1;
        HttpManager.followList( HttpType.REFRESH, getActivity(), this, uid, page, 1 );
    }

    @Override
    public void onLazyLoad() {
        refreshLayout.setRefreshing( true );
        HttpManager.followList( HttpType.REFRESH, getActivity(), this, uid, page, 1 );
    }
}
