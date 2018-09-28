/** 
 * Project Name:GameBox 
 * File Name:TwoStateButton.java 
 * Package Name:com.tenone.gamebox.view.custom 
 * Date:2017-3-10����3:16:46 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

import com.tenone.gamebox.R;

@SuppressLint("NewApi")
public class ThreeStateButton extends Button {
	/* ״̬0,1 */
	private int state = 0;

	private Drawable defultDrawable, otherDrawable, threeDrawable;
	private String defultText, otherText, threeText;
	private TypedArray typedArray;
	private int defultColor = Color.BLACK, otherColor = Color.WHITE,
			threeColor = Color.WHITE;

	public ThreeStateButton(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		typedArray = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.ThreeStateButtonAttr, defStyleRes, 0);
		init();
	}

	public ThreeStateButton(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr, 0);
		typedArray = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.ThreeStateButtonAttr, 0, 0);
		init();
	}

	public ThreeStateButton(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		typedArray = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.ThreeStateButtonAttr, 0, 0);
		init();
	}

	public ThreeStateButton(Context context) {
		super(context, null);
		typedArray = context.getTheme().obtainStyledAttributes(null,
				R.styleable.ThreeStateButtonAttr, 0, 0);
		init();
	}

	private void init() {
		try {
			int num = typedArray.getIndexCount();
			for (int i = 0; i < num; i++) {
				int attr = typedArray.getIndex(i);
				switch (attr) {
				case R.styleable.ThreeStateButtonAttr_defultDrawable_ThreeStateButton:
					defultDrawable = typedArray.getDrawable(i);
					break;
				case R.styleable.ThreeStateButtonAttr_otherDrawable_ThreeStateButton:
					otherDrawable = typedArray.getDrawable(i);
					break;
				case R.styleable.ThreeStateButtonAttr_defultText_ThreeStateButton:
					defultText = typedArray.getString(i);
					break;
				case R.styleable.ThreeStateButtonAttr_otherText_ThreeStateButton:
					otherText = typedArray.getString(i);
					break;
				case R.styleable.ThreeStateButtonAttr_defultColor_ThreeStateButton:
					defultColor = typedArray.getColor(i, Color.BLACK);
					break;
				case R.styleable.ThreeStateButtonAttr_otherColor_ThreeStateButton:
					otherColor = typedArray.getColor(i, Color.WHITE);
					break;
				case R.styleable.ThreeStateButtonAttr_threeDrawable_ThreeStateButton:
					threeDrawable = typedArray.getDrawable(i);
					break;
				case R.styleable.ThreeStateButtonAttr_threeColor_ThreeStateButton:
					threeColor = typedArray.getColor(i, Color.WHITE);
					break;
				case R.styleable.ThreeStateButtonAttr_threeText_ThreeStateButton:
					threeText = typedArray.getString(i);
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
			setBackground(defultDrawable);
			setText(defultText);
			setTextColor(defultColor);
			break;
		case 1:
			setBackground(otherDrawable);
			setText(otherText);
			setTextColor(otherColor);
			break;
		case 2:
			setBackground(threeDrawable);
			setText(threeText);
			setTextColor(threeColor);
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
