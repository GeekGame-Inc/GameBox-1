/** 
 * Project Name:GameBox 
 * File Name:SwitchButton.java 
 * Package Name:com.tenone.gamebox.view.custom 
 * Date:2017-3-23����4:12:13 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.tenone.gamebox.R;

/**
 * ClassName:SwitchButton <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-23 ����4:12:13 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
@SuppressLint({ "ClickableViewAccessibility", "DrawAllocation" })
public class SwitchButton extends View implements OnClickListener {

	private Bitmap mSwitchBottom, mSwitchThumb, mSwitchFrame, mSwitchMask;
	private float mCurrentX = 0;
	private boolean mSwitchOn = true;// ����Ĭ���ǿ��ŵ�
	private int mMoveLength;// ����ƶ�����
	private float mLastX = 0;// ��һ�ΰ��µ���Ч����

	private Rect mDest = null;// ���Ƶ�Ŀ�������С
	private Rect mSrc = null;// ��ȡԴͼƬ�Ĵ�С
	private int mDeltX = 0;// �ƶ���ƫ����
	private Paint mPaint = null;
	private OnChangeListener mListener = null;
	private boolean mFlag = false;

	public SwitchButton(Context context) {
		super(context);
		init(context);
	}

	public SwitchButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * ��ʼ�������Դ
	 */
	public void init(Context context) {
		try {
			mSwitchBottom = BitmapFactory.decodeResource(
					context.getResources(), R.drawable.ic_switch_bottom);
			mSwitchThumb = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.ic_switch_btn_pressed);
			mSwitchFrame = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.ic_switch_frame);
			mSwitchMask = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.ic_switch_mask);

			setOnClickListener(this);
			setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return false;
				}
			});
			mMoveLength = mSwitchBottom.getWidth() - mSwitchFrame.getWidth();
			mDest = new Rect(0, 0, mSwitchFrame.getWidth(),
					mSwitchFrame.getHeight());
			mSrc = new Rect();
			mPaint = new Paint();
			mPaint.setAntiAlias(true);
			mPaint.setAlpha(255);
			mPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		} catch (NullPointerException e) {
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		try {
			setMeasuredDimension(mSwitchFrame.getWidth(), mSwitchFrame.getHeight());
		} catch (NullPointerException e) {
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (mDeltX > 0 || mDeltX == 0 && mSwitchOn) {
			if (mSrc != null) {
				mSrc.set(mMoveLength - mDeltX, 0, mSwitchBottom.getWidth()
						- mDeltX, mSwitchFrame.getHeight());
			}
		} else if (mDeltX < 0 || mDeltX == 0 && !mSwitchOn) {
			if (mSrc != null) {
				mSrc.set(-mDeltX, 0, mSwitchFrame.getWidth() - mDeltX,
						mSwitchFrame.getHeight());
			}
		}

		int count = canvas.saveLayer(new RectF(mDest), null,
				Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
						| Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
						| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
						| Canvas.CLIP_TO_LAYER_SAVE_FLAG);

		canvas.drawBitmap(mSwitchBottom, mSrc, mDest, null);
		canvas.drawBitmap(mSwitchThumb, mSrc, mDest, null);
		canvas.drawBitmap(mSwitchFrame, 0, 0, null);
		canvas.drawBitmap(mSwitchMask, 0, 0, mPaint);
		canvas.restoreToCount(count);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastX = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			mCurrentX = event.getX();
			mDeltX = (int) (mCurrentX - mLastX);
			// ������ؿ������󻬶������߿��ع������һ�������ʱ���ǲ���Ҫ����ģ�
			if ((mSwitchOn && mDeltX < 0) || (!mSwitchOn && mDeltX > 0)) {
				mFlag = true;
				mDeltX = 0;
			}

			if (Math.abs(mDeltX) > mMoveLength) {
				mDeltX = mDeltX > 0 ? mMoveLength : -mMoveLength;
			}
			invalidate();
			return true;
		case MotionEvent.ACTION_UP:
			if (Math.abs(mDeltX) > 0 && Math.abs(mDeltX) < mMoveLength / 2) {
				mDeltX = 0;
				invalidate();
				return true;
			} else if (Math.abs(mDeltX) > mMoveLength / 2
					&& Math.abs(mDeltX) <= mMoveLength) {
				mDeltX = mDeltX > 0 ? mMoveLength : -mMoveLength;
				mSwitchOn = !mSwitchOn;
				if (mListener != null) {
					mListener.onChange(this, mSwitchOn);
				}
				invalidate();
				mDeltX = 0;
				return true;
			} else if (mDeltX == 0 && mFlag) {
				// ��ʱ��õ����ǲ���Ҫ���д���ģ���Ϊ�Ѿ�move����
				mDeltX = 0;
				mFlag = false;
				return true;
			}
			return super.onTouchEvent(event);
		default:
			break;
		}
		invalidate();
		return super.onTouchEvent(event);
	}

	public void setOnChangeListener(OnChangeListener listener) {
		mListener = listener;
	}

	public interface OnChangeListener {
		void onChange(SwitchButton sb, boolean state);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		mDeltX = mSwitchOn ? mMoveLength : -mMoveLength;
		mSwitchOn = !mSwitchOn;
		if (mListener != null) {
			mListener.onChange(this, mSwitchOn);
		}
		invalidate();
		mDeltX = 0;
	}
}
