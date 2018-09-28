package com.tenone.gamebox.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

import java.util.ArrayList;
import java.util.List;

public class SignInView extends View {
	// ������ɫ(ѡ�С�δѡ��)
	private static final int DEF_PADDING = 10;
	private static final int DEF_BALLMAGIN = 20;
	private static final int TEXT_MARGIN_TOP = 10;

	private List<String> datas = new ArrayList<String>();
	private List<String> items = new ArrayList<String>();
	private int current = -1;
	private int viewPadding = 0;
	private int viewWidth;
	private int viewHeight;
	private int signInBallRadio;
	// С���y����
	private int circleY = 0;
	// ���ֵ�y����
	private int descY = 0;
	// С��֮���ľ���
	private int ballMagin = 20;

	private RectF signInBgRectF;
	// С���λ�ü���
	private List<Point> circlePoints = new ArrayList<Point>();
	// ���ֵ�λ�ü���
	private List<Point> decPoints = new ArrayList<Point>();
	// ������λ�ü���
	private List<Point> linePoints = new ArrayList<Point>();

	private Paint circlePaint;
	private Paint linePaint;
	private Paint descPaint;

	public SignInView(Context context) {
		this(context, null);
	}

	public SignInView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SignInView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@SuppressWarnings("deprecation")
	private void init() {
		int textSize1 = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 12, getResources()
						.getDisplayMetrics());
		int textSize2 = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 14, getResources()
						.getDisplayMetrics());
		circlePaint = creatPaint(getResources().getColor(R.color.blue_40),
				textSize1, Paint.Style.STROKE, 3);
		linePaint = creatPaint(getResources().getColor(R.color.blue_40),
				textSize1, Paint.Style.STROKE, 3);
		descPaint = creatPaint(getResources().getColor(R.color.blue_40),
				textSize2, Paint.Style.STROKE, 0);
	}

	public void setDatas(List<String> desc, List<String> list) {
		this.datas = desc;
		this.items = list;
		calculateCirclePoints();
		invalidate();
	}

	public void setCurrent(int c) {
		this.current = c;
		if (current > datas.size()) {
			current = datas.size();
		}
		invalidate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		viewPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, DEF_PADDING, getResources()
						.getDisplayMetrics());
		ballMagin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, DEF_BALLMAGIN, getResources()
						.getDisplayMetrics());
		int textMarginTop = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, TEXT_MARGIN_TOP, getResources()
						.getDisplayMetrics());
		// �ڻص��л�ȡview�Ŀ�Ⱥ͸߶�,������С��İ뾶
		viewWidth = w;
		viewHeight = h;
		signInBallRadio = DisplayMetricsUtils.getScreenWidth(getContext()) / 24;

		signInBgRectF = new RectF(viewPadding, viewPadding, viewWidth
				- viewPadding, viewHeight - viewPadding);
		circleY = (int) signInBgRectF.centerY() - textMarginTop;
		descY = (int) (signInBgRectF.top + signInBallRadio + textMarginTop + ballMagin);
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	// ����ÿ��С���λ��
	private void calculateCirclePoints() {
		if (null != datas) {
			int screenWidth = DisplayMetricsUtils.getScreenWidth(getContext());
			int vWidth = signInBallRadio * 2 * datas.size() + ballMagin * 2
					* datas.size() + 2 * viewPadding;
			int w = (screenWidth - vWidth) / 2;
			// ��ȡ����Դ�зֿ��ķ���,����һ��7��,�м��С���ǲ�����6��,������size-1
			int intervalSize = datas.size() - 1;
			for (int i = 0; i < datas.size(); i++) {
				Point circleP = new Point(w + viewPadding
						+ (ballMagin + signInBallRadio) * ((i + 1) * 2 - 1),
						circleY);
				Point descP = new Point(w + viewPadding
						+ (ballMagin + signInBallRadio) * ((i + 1) * 2 - 1),
						descY);
				Point lineP = new Point(
						circleP.x + signInBallRadio + ballMagin, circleY);
				if (i < intervalSize) {
					linePoints.add(lineP);
				}
				circlePoints.add(circleP);
				decPoints.add(descP);
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (datas != null && datas.size() > 0) {
			drawSignDesc(canvas);
			drawSignLine(canvas);
			drawSignCircle(canvas);
			drawSignCircleText(canvas);
		}
		super.onDraw(canvas);
	}

	// ����ǩ����Ϣ����
	@SuppressWarnings("deprecation")
	private void drawSignDesc(Canvas canvas) {
		for (int i = 0; i < datas.size(); i++) {
			Point p = decPoints.get(i);
			descPaint
					.setColor(getResources().getColor(R.color.defultTextColor));
			descPaint.setTextAlign(Paint.Align.CENTER);
			canvas.drawText(datas.get(i), p.x, p.y, descPaint);
		}
	}

	// ��������
	private void drawSignLine(Canvas canvas) {
		for (int i = 0; i < linePoints.size(); i++) {
			Point p = linePoints.get(i);
			canvas.drawLine(p.x - ballMagin, p.y, p.x + ballMagin, p.y,
					linePaint);
		}
	}

	// ����ԲȦ
	private void drawSignCircle(Canvas canvas) {
		for (int i = 0; i < datas.size(); i++) {
			Point p = circlePoints.get(i);
			if (current > -1) {
				if (i <= current) {
					circlePaint.setStyle(Paint.Style.FILL);
				} else {
					circlePaint.setStyle(Paint.Style.STROKE);

				}
			}
			canvas.drawCircle(p.x, p.y, signInBallRadio, circlePaint);
		}
	}

	// ����Բ���ĵ�����
	@SuppressWarnings("deprecation")
	private void drawSignCircleText(Canvas canvas) {
		for (int i = 0; i < items.size(); i++) {
			Point p = circlePoints.get(i);
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(R.color.blue_40));
			if (current > -1) {
				if (i <= current) {
					paint.setColor(getResources().getColor(R.color.white));
				} else {
					paint.setColor(getResources().getColor(R.color.blue_40));
				}
			}
			int textSize1 = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_SP, 10, getResources()
							.getDisplayMetrics());
			paint.setTextSize(textSize1);
			paint.setStyle(Paint.Style.FILL);
			paint.setTextAlign(Paint.Align.CENTER);
			Paint.FontMetrics fontMetrics = paint.getFontMetrics();
			int top = (int) fontMetrics.top;// Ϊ���ߵ������ϱ߿�ľ���,����ͼ�е�top
			int bottom = (int) fontMetrics.bottom;// Ϊ���ߵ������±߿�ľ���,����ͼ�е�bottom
			int len = (int) paint.measureText(items.get(i));
			Rect rect = new Rect(p.x - (len / 2), p.y - (-top + bottom) / 2
					- signInBallRadio, p.x + (len / 2), p.y + (-top + bottom)
					/ 2 + signInBallRadio);// ��һ������
			int baseLineY = rect.centerY() - top / 2 - bottom / 2;// �����м���y����㹫ʽ
			canvas.drawText(items.get(i), p.x, baseLineY, paint);
		}
	}

	private Paint creatPaint(int paintColor, int textSize, Paint.Style style,
			int lineWidth) {
		Paint paint = new Paint();
		paint.setColor(paintColor);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(lineWidth);
		paint.setDither(true);
		paint.setTextSize(textSize);
		paint.setStyle(style);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
		return paint;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}
}
