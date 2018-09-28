package com.tenone.gamebox.mode.view;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.tenone.gamebox.view.custom.TitleBarView;

public interface DrivingView {

    TitleBarView titleBar();

    TabLayout getTabLayout();

    ViewPager viewPager();

    TextView pointTv();
}
