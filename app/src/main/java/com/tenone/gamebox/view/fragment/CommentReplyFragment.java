package com.tenone.gamebox.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnCommentItemClickListener;
import com.tenone.gamebox.mode.mode.CommentMode;
import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.mode.mode.DynamicCommentModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.activity.PublishGameCommentActivity;
import com.tenone.gamebox.view.adapter.CommentReplyAdapter;
import com.tenone.gamebox.view.base.BaseFragment;
import com.tenone.gamebox.view.custom.SpacesItemDecoration;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DataStateChangeCheck;
import com.tenone.gamebox.view.utils.HttpManager;

import java.util.ArrayList;
import java.util.List;

public class CommentReplyFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, HttpResultListener, DataStateChangeCheck.LoadDataListener, OnCommentItemClickListener {
    @ViewInject(R.id.id_comment_reply_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.id_comment_reply_listview)
    RecyclerView recyclerView;

    private CommentReplyAdapter adapter;
    private ArrayList<CommentMode> models = new ArrayList<CommentMode>();
    private DataStateChangeCheck changeCheck;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_comment_reply, container, false );
        ViewUtils.inject( this, view );
        initView();
        return view;
    }


    private void initView() {
        adapter = new CommentReplyAdapter( models, getActivity() );
        adapter.setOnCommentItemClickListener( this );
        LinearLayoutManager manager = new LinearLayoutManager( getActivity() );
        manager.setAutoMeasureEnabled( true );
        recyclerView.setLayoutManager( manager );
        recyclerView.addItemDecoration( new SpacesItemDecoration( getActivity(), LinearLayoutManager.VERTICAL, 1, getResources().getColor( R.color.divider ) ) );
        recyclerView.setAdapter( adapter );
        swipeRefreshLayout.setOnRefreshListener( this );
        HttpManager.replayComment( 0, getActivity(), this, page );
        changeCheck = new DataStateChangeCheck( recyclerView );
        recyclerView.addOnScrollListener( changeCheck );
        changeCheck.setLoadDataListener( this );
    }


    @Override
    public void onRefresh() {
        page = 1;
        HttpManager.replayComment( 0, getActivity(), this, page );
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        swipeRefreshLayout.setRefreshing( false );
        if (1 == resultItem.getIntValue( "status" )) {
            if (0 == what) {
                models.clear();
            }
            ResultItem item = resultItem.getItem( "data" );
            if (!BeanUtils.isEmpty( item )) {
                List<ResultItem> items = item.getItems( "list" );
                if (!BeanUtils.isEmpty( items )) {
                    setData( items );
                }
            }
        } else {
            ToastCustom.makeText( getActivity(), resultItem.getString( "msg" ), ToastCustom.LENGTH_SHORT ).show();
        }
    }

    @Override
    public void onError(int what, String error) {
        ToastCustom.makeText( getActivity(), error, ToastCustom.LENGTH_SHORT ).show();
        swipeRefreshLayout.setRefreshing( false );
    }

    public void setData(List<ResultItem> data) {
        for (ResultItem item : data) {
            CommentMode mode = new CommentMode();
            mode.setReplyId( item.getString( "uid" ) );
            mode.setCommentUserId( item.getString( "to_uid" ) );
            mode.setComment( item.getString( "content" ) );
            mode.setIsFake( item.getIntValue( "is_fake" ) );
            mode.setCommentTime( item.getString( "create_time" ) );
            mode.setDynamicsId( item.getString( "dynamics_id" ) );
            mode.setCommentType( item.getString( "comment_type" ) );
            mode.setreplyNick( item.getString( "nick_name" ) );
            models.add( mode );
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadNextPage(View view) {
        page++;
        HttpManager.replayComment( 1, getActivity(), this, page );
    }

    @Override
    public void onRefreshPage(View view) {
    }

    @Override
    public void onCommentItemClick(CommentMode mode) {
        DynamicCommentModel dynamicCommentModel = new DynamicCommentModel();
        DriverModel driverModel = new DriverModel();
        driverModel.setNick( mode.getreplyNick() );
        driverModel.setDriverId( mode.getReplyId() );
        dynamicCommentModel.setIsFake( mode.getIsFake() );
        dynamicCommentModel.setDriverModel( driverModel );
        startActivity( new Intent( getActivity(), PublishGameCommentActivity.class )
                .putExtra( "gameId", mode.getDynamicsId() )
                .putExtra( "model", dynamicCommentModel )
                .setAction( mode.getCommentType() ) );
    }
}
