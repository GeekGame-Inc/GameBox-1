package com.tenone.gamebox.mode.view;

import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.view.custom.CustomerUnderlinePageIndicator;
import com.tenone.gamebox.view.custom.MarqueeTextView;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;

public interface RebateView {
	
	ImageView leftImg();

	ImageView rightImg();

	TextView titleTv();

	TabPageIndicator tabPageIndicator();

	CustomerUnderlinePageIndicator underlinePageIndicator();

	ViewPager viewPager();
	
	MarqueeTextView marqueeTextView();
}
