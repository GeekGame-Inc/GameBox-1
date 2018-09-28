package com.tenone.gamebox.view.custom.tailor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

/**
 * http://blog.csdn.net/lmj623565791/article/details/39761281
 * 
 * @author zhy
 * 
 */
public class ClipImageLayout extends RelativeLayout {

	private ZoomImageView mZoomImageView;
	private ClipImageBorderView mClipImageView;
	private int mImageHorizontalPadding = 20;
	private int mLayoutHorizontalPadding = 80;

	public ClipImageLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ClipImageLayout(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		mZoomImageView = new ZoomImageView(context);
		mZoomImageView.setScaleType(ScaleType.MATRIX);
		mClipImageView = new ClipImageBorderView(context);
		LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);
		mImageHorizontalPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mImageHorizontalPadding,
				getResources().getDisplayMetrics());
		mLayoutHorizontalPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mLayoutHorizontalPadding,
				getResources().getDisplayMetrics());
		mZoomImageView.setHorizontalPadding(mImageHorizontalPadding);
		mZoomImageView.setLayoutHorizontalPadding(mLayoutHorizontalPadding);
		mClipImageView.setHorizontalPadding(mLayoutHorizontalPadding);
	}

	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mImageHorizontalPadding = mHorizontalPadding;
	}

	public Bitmap clip() {
		return mZoomImageView.clip();
	}

	public void setImageDrawable(Drawable imageDrawable) {
		mZoomImageView.setImageDrawable(imageDrawable);
	}
}
