/** 
 * Project Name:GameBox 
 * File Name:TwoStateTextView.java 
 * Package Name:com.tenone.gamebox.view.custom 
 * Date:2017-3-14ионГ11:05:26 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * ClassName:TwoStateTextView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-14 ионГ11:05:26 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
@SuppressLint("NewApi")
public class TwoStateTextView extends TextView {

	int state;

	public TwoStateTextView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public TwoStateTextView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr, 0);
	}

	public TwoStateTextView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public TwoStateTextView(Context context) {
		super(context, null);
	}

}
