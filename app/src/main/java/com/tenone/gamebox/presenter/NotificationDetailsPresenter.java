package com.tenone.gamebox.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.NotificationDetailsBiz;
import com.tenone.gamebox.mode.mode.OpenServiceNotificationMode;
import com.tenone.gamebox.mode.view.NotificationDetailsView;
import com.tenone.gamebox.view.activity.NewGameDetailsActivity;
import com.tenone.gamebox.view.adapter.NotificationTabAdapter;
import com.tenone.gamebox.view.custom.swipemenulistview.SwipeMenu;
import com.tenone.gamebox.view.custom.swipemenulistview.SwipeMenuCreator;
import com.tenone.gamebox.view.custom.swipemenulistview.SwipeMenuItem;
import com.tenone.gamebox.view.custom.swipemenulistview.SwipeMenuListView;
import com.tenone.gamebox.view.custom.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

import java.util.ArrayList;
import java.util.List;

public class NotificationDetailsPresenter extends BasePresenter implements
        OnMenuItemClickListener, OnItemClickListener {
    NotificationDetailsView detailsView;
    NotificationDetailsBiz detailsBiz;
    Context mContext;

    NotificationTabAdapter mAdapter;
    List<OpenServiceNotificationMode> items = new ArrayList<OpenServiceNotificationMode>();

    public NotificationDetailsPresenter(NotificationDetailsView v, Context cxt) {
        this.detailsView = v;
        this.mContext = cxt;
        this.detailsBiz = new NotificationDetailsBiz();
    }

    public void setAdapter() {
        items.clear();
        items.addAll( getContent() );
        mAdapter = new NotificationTabAdapter( mContext, items );
        getListView().setAdapter( mAdapter );
    }

    public void initView() {
        getListView().setMenuCreator( initSwipeMenuCreator() );
    }

    public void initListener() {
        getListView().setOnMenuItemClickListener( this );
        getListView().setOnItemClickListener( this );
    }

    public SwipeMenuListView getListView() {
        return detailsView.getListView();
    }


    public List<OpenServiceNotificationMode> getContent() {
        return detailsBiz.getContent( mContext );
    }

    public void deleteItem(OpenServiceNotificationMode notificationMode) {
        detailsBiz.deleteItem( mContext, notificationMode );
    }

    public SwipeMenuCreator initSwipeMenuCreator() {
        SwipeMenuCreator creator = menu -> {
            SwipeMenuItem deleteItem = new SwipeMenuItem( mContext );
            deleteItem.setBackground( new ColorDrawable( Color.rgb( 0xF9,
                    0x3F, 0x25 ) ) );
            deleteItem.setWidth( DisplayMetricsUtils.dipTopx( mContext, 90 ) );
            deleteItem.setIcon( R.drawable.ic_delete );
            menu.addMenuItem( deleteItem );
        };
        return creator;
    }


    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        deleteItem( items.get( position ) );
        items.remove( position );
        mAdapter.notifyDataSetChanged();
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        openOtherActivity( mContext, new Intent( mContext, NewGameDetailsActivity.class )
                .putExtra( "id", items.get( position ).getGameId() ) );
    }
}
