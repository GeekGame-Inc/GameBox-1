package com.tenone.gamebox.view.custom.xbanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressLint("ClickableViewAccessibility")
@SuppressWarnings( "deprecation" )
public class XBannerViewPager extends ViewPager {

	private boolean mIsAllowUserScroll = true;
	private AutoPlayDelegate mAutoPlayDelegate;

	public XBannerViewPager(Context context) {
		super(context);
	}

	public XBannerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScrollDuration(int duration) {
		try {
			Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
			scrollerField.setAccessible(true);
			scrollerField
					.set(this, new XBannerScroller(getContext(), duration));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setBannerCurrentItemInternal(int position) {
		Class<ViewPager> viewpagerClass = ViewPager.class;
		try {
			Method setCurrentItemInternalMethod = viewpagerClass
					.getDeclaredMethod("setCurrentItemInternal", int.class,
							boolean.class, boolean.class);
			setCurrentItemInternalMethod.setAccessible(true);
			setCurrentItemInternalMethod.invoke(this, position, true, true);
			ViewCompat.postInvalidateOnAnimation(this);
		} catch (Exception e) {
		}
	}

	public void setIsAllowUserScroll(boolean iSallowUserScroll) {
		mIsAllowUserScroll = iSallowUserScroll;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mIsAllowUserScroll) {
			return super.onInterceptTouchEvent(ev);
		} else {
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mIsAllowUserScroll) {
			if (mAutoPlayDelegate != null
					&& (ev.getAction() == MotionEvent.ACTION_CANCEL || ev
							.getAction() == MotionEvent.ACTION_UP)) {
				mAutoPlayDelegate
						.handleAutoPlayActionUpOrCancel(getXVelocity());
				return false;
			} else {
				return super.onTouchEvent(ev);
			}
		} else {
			return false;
		}
	}
	@SuppressWarnings( "deprecation" )
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

	public void setAutoPlayDelegate(AutoPlayDelegate autoPlayDelegate) {
		mAutoPlayDelegate = autoPlayDelegate;
	}

	public interface AutoPlayDelegate {
		void handleAutoPlayActionUpOrCancel(float xVelocity);
	}
}