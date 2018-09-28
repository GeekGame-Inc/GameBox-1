/**
 * Project Name:GameBox
 * File Name:DisOpenFragment.java
 * Package Name:com.tenone.gamebox.view.fragment
 * Date:2017-3-27����10:58:21
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.TodayOpenFragmentView;
import com.tenone.gamebox.presenter.TodayOpenFragmentPresenter;
import com.tenone.gamebox.view.base.BaseLazyFragment;
import com.tenone.gamebox.view.custom.RefreshLayout;

/**
 * �������� ClassName:DisOpenFragment <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-27 ����10:58:21 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */
public class DisOpenFragment extends BaseLazyFragment implements
        TodayOpenFragmentView {

    private View view;
    @ViewInject(R.id.id_todayOpen_refresh)
    RefreshLayout refreshLayout;
    @ViewInject(R.id.id_todayOpen_listView)
    ListView listView;
    @ViewInject(R.id.id_openServer_todayTv)
    TextView todayTv;
    @ViewInject(R.id.id_openServer_jjTv)
    TextView jjTv;
    @ViewInject(R.id.id_openServer_ykTv)
    TextView ykTv;

    private TodayOpenFragmentPresenter presenter;

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate( R.layout.fragment_today_open, container,
                    false );
        }
        ViewUtils.inject( this, view );
        presenter = new TodayOpenFragmentPresenter( this, getActivity(), 2 );
        presenter.initView();
        return view;
    }

    @Override
    public RefreshLayout getRefreshLayout() {
        return refreshLayout;
    }

    @Override
    public ListView getListView() {
        return listView;
    }

    @Override
    public TextView getJRTv() {
        return todayTv;
    }

    @Override
    public TextView getJJTv() {
        return jjTv;
    }

    @Override
    public TextView getYKTv() {
        return ykTv;
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLazyLoad() {
        presenter.request();
    }
}
