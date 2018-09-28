package com.tenone.gamebox.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.DrivingView;
import com.tenone.gamebox.presenter.DrivingPresenter;
import com.tenone.gamebox.view.activity.LoginActivity;
import com.tenone.gamebox.view.activity.PublishDynamicsActivity;
import com.tenone.gamebox.view.base.BaseLazyFragment;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.BeanUtils;

public class DrivingFragment extends BaseLazyFragment implements DrivingView {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBar;
    @ViewInject(R.id.id_driving_tabLayout)
    TabLayout tabLayout;
    @ViewInject(R.id.id_driving_viewPager)
    ViewPager viewPager;
    @ViewInject(R.id.id_driving_point)
    TextView pointTv;

    private DrivingPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.activity_driving, container, false );
        ViewUtils.inject( this, view );
        return view;
    }

    @OnClick({R.id.id_driving_publishDynamics})
    public void onClick(View view) {
        if (view.getId() == R.id.id_driving_publishDynamics) {
            if (!BeanUtils.isLogin()) {
                startActivity( new Intent( getActivity(), LoginActivity.class ) );
                return;
            }
            startActivity( new Intent( getActivity(), PublishDynamicsActivity.class ) );
        }
    }

    @Override
    public TitleBarView titleBar() {
        return titleBar;
    }

    @Override
    public TabLayout getTabLayout() {
        return tabLayout;
    }

    @Override
    public ViewPager viewPager() {
        return viewPager;
    }

    @Override
    public TextView pointTv() {
        return pointTv;
    }

    @Override
    public void onResume() {
        if (presenter != null) {
            presenter.onResume();
        }
        super.onResume();
    }

    @Override
    public void onLazyLoad() {
        presenter = new DrivingPresenter( getActivity(), this );
        presenter.initView();
        presenter.setAdapter( getChildFragmentManager() );
    }
}
