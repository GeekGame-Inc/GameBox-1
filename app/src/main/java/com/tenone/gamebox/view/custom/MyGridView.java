/** 
 * Project Name:GameBox 
 * File Name:MyGridView.java 
 * Package Name:com.tenone.gamebox.view.custom 
 * Date:2017-3-28обнГ4:18:48 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * ClassName:MyGridView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-28 обнГ4:18:48 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class MyGridView extends GridView {
	public MyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyGridView(Context context) {
		super(context);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
