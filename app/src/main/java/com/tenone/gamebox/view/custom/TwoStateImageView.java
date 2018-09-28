/** 
 * Project Name:GameBox 
 * File Name:TwoStateImageView.java 
 * Package Name:com.tenone.gamebox.view.custom 
 * Date:2017-4-10����3:26:42 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.tenone.gamebox.R;

/**
 * ClassName:TwoStateImageView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-10 ����3:26:42 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
@SuppressLint("NewApi")
public class TwoStateImageView extends ImageView {
	/* ״̬0,1 */
	private int state = 0;
	private Drawable defultDrawable, otherDrawable;
	
	private TypedArray typedArray;

	public TwoStateImageView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		typedArray = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.TwoStateImageViewAttr, defStyleRes, 0);
		init();
	}

	public TwoStateImageView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr, 0);
		typedArray = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.TwoStateImageViewAttr, 0, 0);
		init();
	}

	public TwoStateImageView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		typedArray = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.TwoStateImageViewAttr, 0, 0);
		init();
	}

	public TwoStateImageView(Context context) {
		super(context, null);
		typedArray = context.getTheme().obtainStyledAttributes(null,
				R.styleable.TwoStateImageViewAttr, 0, 0);
		init();
	}
	
	private void init() {
		try {
			int num = typedArray.getIndexCount();
			for (int i = 0; i < num; i++) {
				int attr = typedArray.getIndex(i);
				switch (attr) {
				case R.styleable.TwoStateImageViewAttr_defultDrawable_TwoStateImage:
					defultDrawable = typedArray.getDrawable(i);
					break;
				case R.styleable.TwoStateImageViewAttr_otherDrawable_TwoStateImage:
					otherDrawable = typedArray.getDrawable(i);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		typedArray.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		switch (state) {
		case 0:
			BitmapDrawable bitmapDrawable = (BitmapDrawable) defultDrawable;
			setImageBitmap(bitmapDrawable.getBitmap());
			bitmapDrawable = null;
			break;
		case 1:
			BitmapDrawable drawable = (BitmapDrawable) otherDrawable;
			setImageBitmap(drawable.getBitmap());
			drawable = null;
			break;
		}
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
}
