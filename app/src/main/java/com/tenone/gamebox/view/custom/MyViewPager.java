/** 
 * Project Name:GameBox 
 * File Name:MyViewPager.java 
 * Package Name:com.tenone.gamebox.view.custom 
 * Date:2017-4-11����11:47:05 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import java.lang.reflect.Field;

@SuppressWarnings( "deprecation" )
public class MyViewPager extends ViewPager {
	private static final int VEL_THRESHOLD = 400;
	private int mPageScrollPosition;
	private float mPageScrollPositionOffset;

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyViewPager(Context context) {
		super(context);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if ((ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP)) {
			if (mPageScrollPosition < getCurrentItem()) {
				if (getXVelocity() > VEL_THRESHOLD
						|| (mPageScrollPositionOffset < 0.7f && getXVelocity() > -VEL_THRESHOLD)) {
					setCurrentItem(mPageScrollPosition);
				} else {
					setCurrentItem(mPageScrollPosition + 1);
				}
			} else {
				if (getXVelocity() < -VEL_THRESHOLD
						|| (mPageScrollPositionOffset > 0.3f && getXVelocity() < VEL_THRESHOLD)) {
					setCurrentItem(mPageScrollPosition + 1);
				} else {
					setCurrentItem(mPageScrollPosition);
				}
			}
			return false;
		} else {
			return super.onTouchEvent(ev);
		}
	}

	private float getXVelocity() {
		float xVelocity = 0;
		Class<ViewPager> viewpagerClass = ViewPager.class;
		try {
			Field velocityTrackerField = viewpagerClass
					.getDeclaredField("mVelocityTracker");
			velocityTrackerField.setAccessible(true);
			VelocityTracker velocityTracker = (VelocityTracker) velocityTrackerField
					.get(this);

			Field activePointerIdField = viewpagerClass
					.getDeclaredField("mActivePointerId");
			activePointerIdField.setAccessible(true);

			Field maximumVelocityField = viewpagerClass
					.getDeclaredField("mMaximumVelocity");
			maximumVelocityField.setAccessible(true);
			int maximumVelocity = maximumVelocityField.getInt(this);

			velocityTracker.computeCurrentVelocity(1000, maximumVelocity);
			xVelocity = VelocityTrackerCompat.getXVelocity(velocityTracker,
					activePointerIdField.getInt(this));
		} catch (Exception e) {
		}
		return xVelocity;
	}

	@Override
	@CallSuper
	protected void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		mPageScrollPosition = position;
		mPageScrollPositionOffset = positionOffset;
		super.onPageScrolled(position, positionOffset, positionOffsetPixels);
	}
}
