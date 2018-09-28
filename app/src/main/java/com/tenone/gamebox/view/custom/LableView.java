package com.tenone.gamebox.view.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

public class LableView extends LinearLayout {

	private String[] text;
	private int[] drawables = {R.drawable.shape_lable_1, R.drawable.shape_lable_2,
			R.drawable.shape_lable_3};
	private int maxSize = 3;
	private int dp3, dp5;

	public LableView(Context context) {
		this( context, null );
	}

	public LableView(Context context, @Nullable AttributeSet attrs) {
		this( context, attrs, 0 );
	}

	public LableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super( context, attrs, defStyleAttr );
		init();
	}

	private void init() {
		dp3 = DisplayMetricsUtils.dipTopx( getContext(), 3 );
		dp5 = DisplayMetricsUtils.dipTopx( getContext(), 5 );
		setOrientation( HORIZONTAL );
		setGravity( Gravity.CENTER_VERTICAL );
	}


	public void addLable(String[] lables) {
		removeAllViews();
		if (lables != null) {
			for (int i = 0; i < (lables.length > 3 ? 3 : lables.length); i++) {
				String lable = lables[i];
				TextView textView = new TextView( getContext() );
				textView.setGravity( Gravity.CENTER );
				textView.setText( lable );
				textView.setTextSize( TypedValue.COMPLEX_UNIT_SP, 10 );
				textView.setTextColor( getResources().getColor( R.color.white ) );
				textView.setPadding( dp5, 1, dp5, 1 );
				textView.setSingleLine();
				textView.setEllipsize( TextUtils.TruncateAt.MIDDLE );
				addView( textView );
				setTextBackground( textView, i % 3 );
				LayoutParams params = (LayoutParams) textView
						.getLayoutParams();
				params.setMargins( 0, 0, dp3, 0 );
				textView.setLayoutParams( params );
			}
		}
		invalidate();
	}

	private void setTextBackground(TextView textView, int index) {
		textView.setBackground( getResources().getDrawable(
				drawables[index] ) );
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure( widthMeasureSpec, heightMeasureSpec );
	}
}
