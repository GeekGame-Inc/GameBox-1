package com.tenone.gamebox.view.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;


public class ChenColorUtils {

	@SuppressWarnings("deprecation")
	public static int getNewColorByStartEndColor(Context context,
			float fraction, int startValue, int endValue) {
		return evaluate(fraction, context.getResources().getColor(startValue),
				context.getResources().getColor(endValue));
	}

	public static int evaluate(float fraction, int startValue, int endValue) {
		int startA = (startValue >> 24) & 0xff;
		int startR = (startValue >> 16) & 0xff;
		int startG = (startValue >> 8) & 0xff;
		int startB = startValue & 0xff;

		int endA = (endValue >> 24) & 0xff;
		int endR = (endValue >> 16) & 0xff;
		int endG = (endValue >> 8) & 0xff;
		int endB = endValue & 0xff;

		return ((startA + (int) (fraction * (endA - startA))) << 24)
				| ((startR + (int) (fraction * (endR - startR))) << 16)
				| ((startG + (int) (fraction * (endG - startG))) << 8)
				| ((startB + (int) (fraction * (endB - startB))));
	}

	public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
		final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
		DrawableCompat.setTintList(wrappedDrawable, colors);
		return wrappedDrawable;
	}
}
