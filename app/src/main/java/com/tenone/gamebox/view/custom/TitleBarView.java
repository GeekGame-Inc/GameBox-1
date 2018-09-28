package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;

@SuppressLint({ "InflateParams", "NewApi" })
public class TitleBarView extends RelativeLayout {
	private Context mContext;
	private View view;
	private ImageView leftImg, rightImg;
	private TextView leftText, rightText, titleText, leftTextTwo;

	public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		this.mContext = context;
		initView(context);
	}

	public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr, 0);
		this.mContext = context;
		initView(context);
	}

	public TitleBarView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		this.mContext = context;
		initView(context);
	}

	public TitleBarView(Context context) {
		super(context, null);
		this.mContext = context;
		initView(context);
	}

	private void initView(Context context) {
		try {
			view = LayoutInflater.from(mContext)
					.inflate(R.layout.layout_title_bar, null)
					.findViewById(R.id.id_titleBar_layout);
			leftImg = view.findViewById(R.id.id_titleBar_leftImg);
			rightImg = view.findViewById(R.id.id_titleBar_rightImg);
			leftText = view.findViewById(R.id.id_titleBar_leftText);
			rightText = view
					.findViewById(R.id.id_titleBar_rightText);
			titleText = view.findViewById(R.id.id_titleBar_title);
			leftTextTwo = view
					.findViewById(R.id.id_titleBar_leftTextTwo);
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null) {
				parent.removeAllViews();
			}
			addView(view);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public TextView getLeftTextTwo() {
		return leftTextTwo;
	}

	public void setLeftTextTwo(TextView leftTextTwo) {
		this.leftTextTwo = leftTextTwo;
	}

	public ImageView getLeftImg() {
		return leftImg;
	}

	public void setLeftImg(ImageView leftImg) {
		this.leftImg = leftImg;
	}

	public ImageView getRightImg() {
		return rightImg;
	}

	public void setRightImg(ImageView rightImg) {
		this.rightImg = rightImg;
	}

	public TextView getLeftText() {
		return leftText;
	}

	public void setLeftText(TextView leftText) {
		this.leftText = leftText;
	}

	public TextView getRightText() {
		return rightText;
	}

	public void setRightText(TextView rightText) {
		this.rightText = rightText;
	}

	public TextView getTitleText() {
		return titleText;
	}

	public void setTitleText(TextView titleText) {
		this.titleText = titleText;
	}

	@SuppressWarnings("deprecation")
	public void setLeftImg(int rId) {
		leftImg.setImageDrawable(ContextCompat.getDrawable( mContext,rId ));
	}

	public void setLeftImg(Drawable drawable) {
		leftImg.setImageDrawable(drawable);
	}

	public void setLeftText(int rId) {
		leftText.setText(mContext.getResources().getString(rId));
	}

	public void setLeftText(String text) {
		leftText.setText(text);
	}

	public void setLeftTwoText(int rId) {
		leftTextTwo.setText(mContext.getResources().getString(rId));
	}

	public void setLeftTwoText(String text) {
		leftTextTwo.setText(text);
	}

	public void setTitleText(int rId) {
		titleText.setText(mContext.getResources().getString(rId));
	}

	public void setTitleText(String text) {
		titleText.setText(text);
	}

	public void setRigthImg(int rId) {
		rightImg.setImageDrawable( ContextCompat.getDrawable( mContext,rId ));
	}

	public void setRightImg(Drawable drawable) {
		rightImg.setImageDrawable(drawable);
	}

	public void setRightText(int rId) {
		rightText.setText(mContext.getResources().getString(rId));
	}

	public void setRightText(String text) {
		rightText.setText(text);
	}

	public View getView(int rId) {
		View v = view.findViewById(rId);
		return v;
	}

	public void setOnClickListener(int rId, OnClickListener listener) {
		getView(rId).setOnClickListener(listener);
	}
}
