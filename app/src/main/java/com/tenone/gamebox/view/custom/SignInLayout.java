package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.tenone.gamebox.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("DrawAllocation")
public class SignInLayout extends LinearLayout {
	// �����б�
	private List<View> views = new ArrayList<View>();
	private int count = 0;
	private Context context;

	public SignInLayout(Context context) {
		this(context, null);
	}

	// xml����SignInLayout����������캯��
	public SignInLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	// new SignInLayout����������캯��
	public SignInLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		 setDividerDrawable(null);
		 setOrientation(LinearLayout.HORIZONTAL);
		init();
	}

	private void init() {
		count = this.getChildCount();
		// һ���ж��ٸ����ȡ�������list
		for (int i = 0; i < count; i++) {
			View view = this.getChildAt(i);
			views.add(view);
		}
	}
	@SuppressWarnings("deprecation")
	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(context.getResources().getColor(R.color.rebateColor));
		// �����ж��������path�������������һ����
		for (int i = 0; i < count - 1; i++) {
			View v = views.get(i);
			System.out.println("--------------------i = " + i);
			// һ�������ĳ���
			int onePiece = (this.getMeasuredWidth()
					- (this.getPaddingRight() + this.getPaddingLeft()) - v
					.getMeasuredWidth() * count)
					/ (count - 1);
			canvas.drawLine(v.getX() + (v.getMeasuredWidth() / 2), v.getY(),
					v.getX() + (v.getMeasuredWidth() / 2) + onePiece, v.getY(),
					paint);
		}
		super.onDraw(canvas);
	}
}
