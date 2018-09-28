/** 
 * Project Name:GameBox 
 * File Name:NoScrollViewPager.java 
 * Package Name:com.tenone.gamebox.view.custom 
 * Date:2017-3-20ÉÏÎç10:22:21 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ½ûÖ¹×óÓÒ»¬¶¯µÄViewPager ClassName:NoScrollViewPager <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-20 ÉÏÎç10:22:21 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
@SuppressLint("ClickableViewAccessibility")
public class NoScrollViewPager extends ViewPager {

	private boolean transition = false;

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (!transition)
			return false;
		else
			return super.onInterceptTouchEvent(arg0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (!transition)
			return false;
		else
			return super.onTouchEvent(arg0);
	}

	public boolean isTransition() {
		return transition;
	}

	public void setTransition(boolean transition) {
		this.transition = transition;
	}
}
