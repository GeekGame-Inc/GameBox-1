package com.tenone.gamebox.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.OpenServerTabView;
import com.tenone.gamebox.presenter.OpenServerTabPresenter;
import com.tenone.gamebox.view.base.BaseLazyFragment;

public class OpenServerFragment extends BaseLazyFragment implements OpenServerTabView {
    @ViewInject(R.id.id_open_server_viewpager)
    ViewPager viewPager;
    @ViewInject(R.id.id_open_server_tabLayout)
    TabLayout tabLayout;

    private Context context;

    private OpenServerTabPresenter presenter;

    @Override
    public void onLazyLoad() {
        presenter = new OpenServerTabPresenter( this, context );
        presenter.initView();
        presenter.setAdapter( getChildFragmentManager() );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_open_server, container, false );
        ViewUtils.inject( this, view );
        context = getActivity();
        return view;
    }


    @Override
    public TabLayout getTabLayout() {
        return tabLayout;
    }

    @Override
    public ViewPager getViewPager() {
        return viewPager;
    }
}
