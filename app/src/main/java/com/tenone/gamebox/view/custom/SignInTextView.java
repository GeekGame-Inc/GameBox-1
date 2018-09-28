package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import com.tenone.gamebox.R;

@SuppressLint("DrawAllocation")
public class SignInTextView extends TextView {

	private Paint mPaint;

	public SignInTextView(Context context) {
		this(context, null);
	}

	// xml����SignInTextView����������캯��
	public SignInTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	// new SignInTextView����������캯��
	public SignInTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * ��ʼ������
	 */
	public void init() {
		mPaint = new Paint();
	}

	/**
	 * ����onDraw���Ʊ߿�
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// ����һ��RectF�������޶�����Բ���ķ�Χ
		RectF rectf = new RectF();
		// ���û��ʵ���ɫ
		mPaint.setColor(getResources().getColor(R.color.rebateColor));
		// ���û��ʵ���ʽ������
		mPaint.setStyle(isSelected() ? Paint.Style.FILL : Paint.Style.STROKE);
		// ���ÿ����
		mPaint.setAntiAlias(true);
		// ���û���һ���뾶��Ȼ��Ƚϳ��Ϳ�������ֵ��ȷ�������εĳ���ȷ���뾶
		int r = getMeasuredWidth() > getMeasuredHeight() ? getMeasuredWidth()
				: getMeasuredHeight();
		// ������õ�padding��һ�����Ƴ���������Բ�Ρ����Ƶ�ʱ����padding
		// Log.i("�߽�",
		// "���"+getMeasuredWidth()+"�߶�"+getMeasuredHeight()+"getPaddingLeft()"+getPaddingLeft()+"getPaddingTop"+getPaddingTop()+"getPaddingRight(): "+getPaddingRight()+"getPaddingBottom()"+getPaddingBottom());
		// ��padding��Ϊ0��ʱ�򣬻��Ƴ����ľ���RectF�޶����������һ��������
		rectf.set(getPaddingLeft(), getPaddingTop(), r - getPaddingRight(), r
				- getPaddingBottom());
		// ����Բ��
		canvas.drawArc(rectf, 0, 360, false, mPaint);
	}
}
