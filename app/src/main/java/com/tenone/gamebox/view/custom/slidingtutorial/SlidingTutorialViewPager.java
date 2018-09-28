package com.tenone.gamebox.view.custom.slidingtutorial;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SlidingTutorialViewPager extends ViewPager {
    public SlidingTutorialViewPager(Context context) {
        super(context);
    }

    public SlidingTutorialViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (getAdapter() == null || getAdapter().getCount() == 0) {
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (getAdapter() == null || getAdapter().getCount() == 0) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        try {
            return super.getChildDrawingOrder(childCount, i);
        } catch (IndexOutOfBoundsException ignored) {
        }

        return 0;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (NullPointerException ignored) {
        }
    }
}
