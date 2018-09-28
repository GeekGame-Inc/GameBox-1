/**
 * Project Name:GameBox
 * File Name:MyRemindPresenter.java
 * Package Name:com.tenone.gamebox.presenter
 * Date:2017-3-29����1:33:43
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.MyRemindBiz;
import com.tenone.gamebox.mode.listener.DeleteDialogConfrimListener;
import com.tenone.gamebox.mode.listener.OnClearListener;
import com.tenone.gamebox.mode.listener.OpenItemClickListener;
import com.tenone.gamebox.mode.listener.OpenServiceWarnListener;
import com.tenone.gamebox.mode.mode.OpenServerMode;
import com.tenone.gamebox.mode.view.MyRemindView;
import com.tenone.gamebox.view.activity.GameDetailsActivity;
import com.tenone.gamebox.view.activity.GameDetailsNewActivity;
import com.tenone.gamebox.view.adapter.MyRemindAapter;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.dialog.DeleteDialog.Buidler;

import java.util.ArrayList;
import java.util.List;

public class MyRemindPresenter extends BasePresenter implements
        OnClickListener, DeleteDialogConfrimListener, OpenItemClickListener,
        OpenServiceWarnListener, OnClearListener {
    MyRemindView remindView;
    MyRemindBiz remindBiz;
    Context mContext;
    MyRemindAapter mAdapter;

    Buidler builder;

    List<OpenServerMode> modes = new ArrayList<OpenServerMode>();

    public MyRemindPresenter(MyRemindView v, Context cxt) {
        this.mContext = cxt;
        this.remindBiz = new MyRemindBiz( cxt );
        this.remindView = v;
        modes.clear();
        modes.addAll( getModes() );
    }

    public void initView() {
        getTitleBarView().setLeftImg( R.drawable.icon_back_grey );
        getTitleBarView().setTitleText( R.string.myRemind );
        getTitleBarView().setRigthImg( R.drawable.ic_delete_wr );
    }

    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new MyRemindAapter( modes, mContext );
        }
        getListView().setAdapter( mAdapter );
    }

    public void initListener() {
        getTitleBarView().getLeftImg().setOnClickListener( this );
        getTitleBarView().getRightImg().setOnClickListener( this );
        mAdapter.setOpenItemClickListener( this );
        mAdapter.setOpenServiceWarnListener( this );
    }

    public TitleBarView getTitleBarView() {
        return remindView.getTitleBarView();
    }

    public ListView getListView() {
        return remindView.getListView();
    }

    public List<OpenServerMode> getModes() {
        return remindBiz.getModes( mContext );
    }

    public void clearAllNotification(List<OpenServerMode> modes,
                                     OnClearListener listener, int id) {
        remindBiz.clearAllNotification( modes, listener, id );
    }

    public void clearOneNotification(OnClearListener listener, int id,
                                     int gameId) {
        remindBiz.clearOneNotification( listener, id, gameId );
    }

    private void showDialog() {
        if (builder == null) {
            builder = new Buidler( mContext );
        }
        builder.setMessage( R.string.clearAllRemind );
        builder.setConfrimListener( this );
        builder.createDialog();
    }

    public void sendBroadcast() {
        mContext.sendBroadcast( new Intent().setAction( "delete_warn" ) );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_titleBar_leftImg:
                close( mContext );
                break;
            case R.id.id_titleBar_rightImg:
                // ɾ��
                showDialog();
                break;
        }
    }

    @Override
    public void onConfrimClick(AlertDialog dialog) {
        dialog.dismiss();
        clearAllNotification( modes, this, 0 );
    }

    @Override
    public void onOpenServiceClick(OpenServerMode mode) {
        if (!mode.isOpen() && mode.isNotification()) {
            String id = mode.getGameId();
            int gameId = 0;
            if (!TextUtils.isEmpty( id )) {
                gameId = Integer.valueOf( mode.getGameId() ).intValue();
            }
            clearOneNotification( this, mode.getServiceId(), gameId );
        }
    }

    @Override
    public void onOpenItemClick(OpenServerMode mode) {
        openOtherActivity( mContext,
                new Intent( mContext, Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ? GameDetailsActivity.class : GameDetailsNewActivity.class ).putExtra( "id",
                        mode.getGameId() + "" ) );
    }

    @Override
    public void onClear(boolean succ, int id) {
        switch (id) {
            case 0:
                if (succ) {
                    modes.clear();
                } else {
                    showToast( mContext, "ɾ��ʧ��" );
                }
                break;
            default:
                if (succ) {
                    for (int i = 0; i < modes.size(); i++) {
                        OpenServerMode mode = modes.get( i );
                        if (mode.getServiceId() == id) {
                            modes.remove( i );
                            break;
                        }
                    }
                } else {
                    showToast( mContext, "ȡ��ʧ��" );
                }
                break;
        }
        mAdapter.notifyDataSetChanged();
    }
}
