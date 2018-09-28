package com.tenone.gamebox.mode.mode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

public class CommonViewHolder {
	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	private Context mContext;
	private int mLayoutId;

	public CommonViewHolder(Context context, ViewGroup parent, int layoutId,
			int position) {
		mContext = context;
		mLayoutId = layoutId;
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		mConvertView.setTag(this);
	}

	public static CommonViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {
			return new CommonViewHolder(context, parent, layoutId, position);
	}

	public int getPosition() {
		return mPosition;
	}

	public int getLayoutId() {
		return mLayoutId;
	}

	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {
		return mConvertView;
	}

	public CommonViewHolder setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}

	public CommonViewHolder setImageResource(int viewId, int resId) {
		ImageView view = getView(viewId);
		view.setImageResource(resId);
		return this;
	}

	public CommonViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
		ImageView view = getView(viewId);
		view.setImageBitmap(bitmap);
		return this;
	}

	public CommonViewHolder setImageDrawable(int viewId, Drawable drawable) {
		ImageView view = getView(viewId);
		view.setImageDrawable(drawable);
		return this;
	}

	public CommonViewHolder setBackgroundColor(int viewId, int color) {
		View view = getView(viewId);
		view.setBackgroundColor(color);
		return this;
	}

	@SuppressLint("NewApi")
	public CommonViewHolder setBackgroundDra(int viewId, Drawable backgroundDra) {
		View view = getView(viewId);
		view.setBackground(backgroundDra);
		return this;
	}

	public CommonViewHolder setBackgroundRes(int viewId, int backgroundRes) {
		View view = getView(viewId);
		view.setBackgroundResource(backgroundRes);
		return this;
	}

	public CommonViewHolder setTextColor(int viewId, int textColor) {
		TextView view = getView(viewId);
		view.setTextColor(textColor);
		return this;
	}

	@SuppressWarnings("deprecation")
	public CommonViewHolder setTextColorRes(int viewId, int textColorRes) {
		TextView view = getView(viewId);
		view.setTextColor(mContext.getResources().getColor(textColorRes));
		return this;
	}

	@SuppressLint("NewApi")
	public CommonViewHolder setAlpha(int viewId, float value) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getView(viewId).setAlpha(value);
		} else {
			AlphaAnimation alpha = new AlphaAnimation(value, value);
			alpha.setDuration(0);
			alpha.setFillAfter(true);
			getView(viewId).startAnimation(alpha);
		}
		return this;
	}

	public CommonViewHolder setVisible(int viewId, boolean visible) {
		View view = getView(viewId);
		view.setVisibility(visible ? View.VISIBLE : View.GONE);
		return this;
	}

	public CommonViewHolder linkify(int viewId) {
		TextView view = getView(viewId);
		Linkify.addLinks(view, Linkify.ALL);
		return this;
	}

	public CommonViewHolder setTypeface(Typeface typeface, int... viewIds) {
		for (int viewId : viewIds) {
			TextView view = getView(viewId);
			view.setTypeface(typeface);
			view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
		}
		return this;
	}

	public CommonViewHolder setFlags(int flage, boolean isAntiAlias,
			int... viewIds) {
		for (int viewId : viewIds) {
			TextView view = getView(viewId);
			view.getPaint().setFlags(flage);
			view.getPaint().setAntiAlias(isAntiAlias);
		}
		return this;
	}

	public CommonViewHolder setProgress(int viewId, int progress) {
		ProgressBar view = getView(viewId);
		view.setProgress(progress);
		return this;
	}

	public CommonViewHolder setProgress(int viewId, int progress, int max) {
		ProgressBar view = getView(viewId);
		view.setMax(max);
		view.setProgress(progress);
		return this;
	}

	public CommonViewHolder setMax(int viewId, int max) {
		ProgressBar view = getView(viewId);
		view.setMax(max);
		return this;
	}

	public CommonViewHolder setRating(int viewId, float rating) {
		RatingBar view = getView(viewId);
		view.setRating(rating);
		return this;
	}

	public CommonViewHolder setRating(int viewId, float rating, int max) {
		RatingBar view = getView(viewId);
		view.setMax(max);
		view.setRating(rating);
		return this;
	}

	public CommonViewHolder setTag(int viewId, Object tag) {
		View view = getView(viewId);
		view.setTag(tag);
		return this;
	}

	public CommonViewHolder setTag(int viewId, int key, Object tag) {
		View view = getView(viewId);
		view.setTag(key, tag);
		return this;
	}

	public CommonViewHolder setChecked(int viewId, boolean checked) {
		Checkable view = getView(viewId);
		view.setChecked(checked);

		return this;
	}

	public CommonViewHolder setOnClickListener(int viewId,
			View.OnClickListener listener) {
		View view = getView(viewId);
		view.setOnClickListener(listener);
		return this;
	}

	public CommonViewHolder setOnTouchListener(int viewId,
			View.OnTouchListener listener) {
		View view = getView(viewId);
		view.setOnTouchListener(listener);
		return this;
	}

	public CommonViewHolder setOnLongClickListener(int viewId,
			View.OnLongClickListener listener) {
		View view = getView(viewId);
		view.setOnLongClickListener(listener);
		return this;
	}

	public CommonViewHolder setOnCheckedChangeListener(int viewId,
			CompoundButton.OnCheckedChangeListener listener) {
		CheckBox view = getView(viewId);
		view.setFocusable(true);
		view.setClickable(true);
		view.setOnCheckedChangeListener(listener);
		return this;
	}
}
