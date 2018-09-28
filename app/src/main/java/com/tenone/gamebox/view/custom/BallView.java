/** 
 * Project Name:GameBox 
 * File Name:BallView.java 
 * Package Name:com.tenone.gamebox.view.custom 
 * Date:2017-5-4����5:39:14 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

import java.util.Random;

/**
 * ClassName:BallView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-5-4 ����5:39:14 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class BallView extends View {
	private Paint paint; // ���廭��
	private float cx = 50; // Բ��Ĭ��X����
	private float cy = 50; // Բ��Ĭ��Y����
	private int radius = 20;
	// ������ɫ����
	private int colorArray[] = { Color.BLACK, Color.BLACK, Color.GREEN,
			Color.YELLOW, Color.RED };
	private int paintColor = colorArray[0]; // ���廭��Ĭ����ɫ
	Context mContext;

	int screenW, screenH;

	public BallView(Context context) {
		super(context);
		this.mContext = context;
		// ��ʼ������
		initPaint();
		screenW = DisplayMetricsUtils.getScreenWidth(mContext);
		screenH = DisplayMetricsUtils.getScreenHeight(mContext);
	}

	private void initPaint() {
		paint = new Paint();
		// �����������
		paint.setAntiAlias(true);
		// ���û�����ɫ
		paint.setColor(paintColor);
	}

	// ��дonDraw����ʵ�ֻ�ͼ����
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// ����Ļ����Ϊ��ɫ
		canvas.drawColor(Color.WHITE);
		// ����Բ������
		revise();
		// ������û�����ɫ
		setPaintRandomColor();
		// ����СԲ��ΪС��
		canvas.drawCircle(cx, cy, radius, paint);
	}

	// Ϊ�������������ɫ
	private void setPaintRandomColor() {
		Random rand = new Random();
		int randomIndex = rand.nextInt(colorArray.length);
		paint.setColor(colorArray[randomIndex]);
	}

	// ����Բ������
	private void revise() {
		if (cx <= radius) {
			cx = radius;
		} else if (cx >= (screenW - radius)) {
			cx = screenW - radius;
		}
		if (cy <= radius) {
			cy = radius;
		} else if (cy >= (screenH - radius)) {
			cy = screenH - radius;
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// ����
			cx = (int) event.getX();
			cy = (int) event.getY();
			// ֪ͨ�ػ�
			postInvalidate(); // �÷��������onDraw���������»�ͼ
			break;
		case MotionEvent.ACTION_MOVE:
			// �ƶ�
			cx = (int) event.getX();
			cy = (int) event.getY();
			// ֪ͨ�ػ�
			postInvalidate();
			break;
		case MotionEvent.ACTION_UP:
			// ̧��
			cx = (int) event.getX();
			cy = (int) event.getY();
			// ֪ͨ�ػ�
			postInvalidate();
			break;
		}

		/*
		 * ��ע1���˴�һ��Ҫ��return super.onTouchEvent(event)�޸�Ϊreturn true��ԭ���ǣ�
		 * 1�������onTouchEvent(event)��������û�����κδ������Ƿ�����false��
		 * 2)һ������false���ڸ÷�������Ҳ�����յ�MotionEvent
		 * .ACTION_MOVE��MotionEvent.ACTION_UP�¼���
		 */
		// return super.onTouchEvent(event);
		return true;
	}
}
