package com.tenone.gamebox.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.tenone.gamebox.mode.listener.OnRemoveItemRecyclerViewItemClickListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.MessageModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.activity.MessageDetailsActivity;
import com.tenone.gamebox.view.adapter.MessageListAdapter;
import com.tenone.gamebox.view.base.BaseFragment;
import com.tenone.gamebox.view.custom.RemoveItemRecyclerView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.HttpManager;

import java.util.ArrayList;
import java.util.List;

public class MessageListFragment extends BaseFragment implements OnRemoveItemRecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener, HttpResultListener {
    @ViewInject(R.id.id_message_list_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @ViewInject(R.id.id_message_list_listview)
    RemoveItemRecyclerView recyclerView;

    private MessageListAdapter adapter;
    private ArrayList<MessageModel> models = new ArrayList<MessageModel>();
    private int currentPosition = -1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.activity_message_list, container, false );
        ViewUtils.inject( this, view );
        initView();
        return view;
    }


    private void initView() {
        adapter = new MessageListAdapter( models, getActivity() );
        LinearLayoutManager manager = new LinearLayoutManager( getActivity() );
        manager.setAutoMeasureEnabled( true );
        recyclerView.setLayoutManager( manager );
        recyclerView.setAdapter( adapter );
        recyclerView.setOnRemoveItemRecyclerViewItemClickListener( this );
        swipeRefreshLayout.setOnRefreshListener( this );
        HttpManager.getMessageList( HttpType.REFRESH, getActivity(), this );
    }


    @Override
    public void onRemoveItemRecyclerViewItemClick(View view, int position) {
        models.get( position ).setRead( true );
        adapter.notifyItemChanged( position );
        startActivity( new Intent( getActivity(), MessageDetailsActivity.class )
                .putExtra( "id", models.get( position ).getId() ) );
    }

    @Override
    public void onRemoveItemRecyclerViewDeleteClick(int position) {
        currentPosition = position;
        HttpManager.deleteMessage( HttpType.LOADING, getActivity(), this, models.get( position ).getId() );
    }

    @Override
    public void onRefresh() {
        HttpManager.getMessageList( HttpType.REFRESH, getActivity(), this );
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        swipeRefreshLayout.setRefreshing( false );
        if (1 == resultItem.getIntValue( "status" )) {
            switch (what) {
                case HttpType.REFRESH:
                    List<ResultItem> items = resultItem.getItems( "data" );
                    if (null != items) {
                        new MyThread( items ).start();
                    }
                    break;
                case HttpType.LOADING:
									ToastCustom.makeText( getActivity(), "\u5220\u9664\u6210\u529f", ToastCustom.LENGTH_SHORT ).show();
                    models.remove( currentPosition );
                    adapter.notifyDataSetChanged();
                    break;
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

    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage( msg );
        }
    };

    private class MyThread extends Thread {

        private List<ResultItem> resultItems;

        public MyThread(List<ResultItem> list) {
            this.resultItems = list;
        }

        @Override
        public void run() {
            models.clear();
            for (ResultItem item : resultItems) {
                MessageModel model = new MessageModel();
                model.setAction( item.getIntValue( "action" ) );
                model.setContent( item.getString( "desc" ) );
                model.setRead( 1 == item.getIntValue( "is_read" ) );
                model.setId( item.getIntValue( "id" ) );
                model.setTitle( item.getString( "title" ) );
                model.setTime( item.getString( "create_time" ) );
                models.add( model );
            }

            handler.post( () -> adapter.notifyDataSetChanged() );
            super.run();
        }
    }

}
