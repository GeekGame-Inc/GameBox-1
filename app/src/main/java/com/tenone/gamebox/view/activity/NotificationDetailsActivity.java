package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.NotificationDetailsView;
import com.tenone.gamebox.presenter.NotificationDetailsPresenter;
import com.tenone.gamebox.view.base.BaseFragment;
import com.tenone.gamebox.view.custom.swipemenulistview.SwipeMenuListView;

public class NotificationDetailsActivity extends BaseFragment implements
        NotificationDetailsView {
    @ViewInject(R.id.id_notification_listView)
    SwipeMenuListView listView;
    NotificationDetailsPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.activity_notification_details, container, false );
        ViewUtils.inject( this, view );
        presenter = new NotificationDetailsPresenter( this, getActivity() );
        presenter.setAdapter();
        presenter.initView();
        presenter.initListener();
        return view;
    }

    @Override
    public SwipeMenuListView getListView() {
        return listView;
    }
}
