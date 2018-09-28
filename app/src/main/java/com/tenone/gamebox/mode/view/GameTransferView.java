package com.tenone.gamebox.mode.view;

import android.support.v4.view.ViewPager;

import com.tenone.gamebox.view.custom.CustomerUnderlinePageIndicator;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;

public interface GameTransferView {

	TabPageIndicator tabPageIndicator();

	CustomerUnderlinePageIndicator indicator();

	ViewPager viewPager();

	TitleBarView titleBarView();
}
