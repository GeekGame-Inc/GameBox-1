package com.tenone.gamebox.view.custom;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.tenone.gamebox.view.custom.slidingtutorial.Renderer;

public class RhombusRenderer implements Renderer {
	private static final float ANGLE_45 = 45f;

	public static RhombusRenderer create() {
		return new RhombusRenderer();
	}

	private RhombusRenderer() {
	}

	@Override
	public void draw(@NonNull Canvas canvas, @NonNull RectF elementBounds, @NonNull Paint paint, boolean isActive) {
		canvas.save();
		canvas.rotate(ANGLE_45, elementBounds.centerX(), elementBounds.centerY());
		canvas.drawRect(elementBounds, paint);
		canvas.restore();
	}
}
