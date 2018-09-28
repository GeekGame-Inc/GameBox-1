package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

public class IconTopTextBottomView extends RelativeLayout {
	private int defIconId;
	private String defText;
	private Drawable badgeDrawable;
	private int defTextColor, badgeNum;
	private int iconId, textColor;
	private TextView badgeTv, textTv;
	private float textSize;
	private ImageView imageView;
	private int dp6, dp2;
	private int imageWidth, badgeTvWidth, textTvWidth;

	public IconTopTextBottomView(Context context) {
		this( context, null );
	}

	public IconTopTextBottomView(Context context, @Nullable AttributeSet attrs) {
		this( context, attrs, 0 );
	}

	public IconTopTextBottomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super( context, attrs, defStyleAttr );
		dp6 = DisplayMetricsUtils.dipTopx( context, 6 );
		dp2 = DisplayMetricsUtils.dipTopx( context, 2 );
		init( context, attrs );
	}

	@SuppressLint("ResourceType")
	private void init(Context context, AttributeSet attrs) {
		TypedArray mTypedArray = context.obtainStyledAttributes( attrs, R.styleable.IconTopTextBottomViewStyleable );
		defIconId = mTypedArray.getResourceId( R.styleable.IconTopTextBottomViewStyleable_def_icon, R.drawable.d_icon_zhjy_an );
		iconId = mTypedArray.getResourceId( R.styleable.IconTopTextBottomViewStyleable_icon, R.drawable.d_icon_zhjy );
		textColor = mTypedArray.getColor( R.styleable.IconTopTextBottomViewStyleable_other_color, ContextCompat.getColor( context, R.color.blue_40 ) );
		defTextColor = mTypedArray.getColor( R.styleable.IconTopTextBottomViewStyleable_def_color,
				ContextCompat.getColor( context, R.color.defultTextColor ) );
		defText = mTypedArray.getString( R.styleable.IconTopTextBottomViewStyleable_tv_text );
		badgeNum = mTypedArray.getInteger( R.styleable.IconTopTextBottomViewStyleable_badge_num, 0 );
		textSize = mTypedArray.getDimensionPixelSize( R.styleable.IconTopTextBottomViewStyleable_text_size, 14 );
		badgeDrawable = ContextCompat.getDrawable( context, R.drawable.shape_gray_f2 );
		initBadgeTv( context );
		initTextTv( context );
		initImage( context );
		this.addView( imageView );
		this.addView( textTv );
		this.addView( badgeTv );

		LayoutParams params = (LayoutParams) imageView.getLayoutParams();
		params.addRule( RelativeLayout.CENTER_HORIZONTAL );
		imageView.setLayoutParams( params );
		imageView.setId( 1 );

		LayoutParams textParams = (LayoutParams) textTv.getLayoutParams();
		textParams.addRule( RelativeLayout.CENTER_HORIZONTAL );
		textParams.addRule( RelativeLayout.BELOW, imageView.getId() );
		textParams.topMargin = dp6;
		textTv.setLayoutParams( textParams );
		textTv.setGravity( Gravity.CENTER );
		textTv.setId( 2 );

		LayoutParams badgeParams = (LayoutParams) badgeTv.getLayoutParams();
		badgeParams.addRule( RelativeLayout.ALIGN_TOP, imageView.getId() );
		badgeParams.addRule( RelativeLayout.RIGHT_OF, imageView.getId() );
		badgeParams.leftMargin = dp2;
		badgeTv.setLayoutParams( badgeParams );
		badgeTv.setId( 3 );
		requestLayout();
	}

	private void initImage(Context context) {
		imageView = new ImageView( context );
		imageView.setBackground( ContextCompat.getDrawable( context, defIconId ) );
	}

	private void initTextTv(Context context) {
		textTv = new TextView( context );
		textTv.setTextColor( defTextColor );
		textTv.setGravity( Gravity.CENTER );
		textTv.setTextSize( 15 );
		textTv.setSingleLine();
		textTv.setText( defText );
	}

	private void initBadgeTv(Context context) {
		badgeTv = new TextView( context );
		badgeTv.setPadding( dp6, dp2, dp6, dp2 );
		badgeTv.setBackground( badgeDrawable );
		badgeTv.setTextColor( ContextCompat.getColor( context, R.color.gray_69 ) );
		badgeTv.setTextSize( 10 );
		badgeTv.setGravity( Gravity.CENTER );
		badgeTv.setSingleLine();
		badgeTv.setText( String.valueOf( badgeNum ) );
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure( widthMeasureSpec, heightMeasureSpec );
		imageWidth = imageView.getMeasuredWidth() > imageWidth ? imageView.getMeasuredWidth() : imageWidth;
		textTvWidth = textTv.getMeasuredWidth() > textTvWidth ? textTv.getMeasuredWidth() : textTvWidth;
		badgeTvWidth = badgeTv.getMeasuredWidth() > badgeTvWidth ? badgeTv.getMeasuredWidth() : badgeTvWidth;
		int width = calculateWidth();
		int height = imageView.getMeasuredHeight() + textTv.getMeasuredHeight() + dp6;
		setMeasuredDimension( width > 0 ? width : MeasureSpec.getSize( widthMeasureSpec ), height > 0 ? height : MeasureSpec.getSize( heightMeasureSpec ) );
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout( changed, l, t, r, b );
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged( w, h, oldw, oldh );
	}

	private int calculateWidth() {
		int a = (badgeTvWidth + dp2 - (textTvWidth - imageWidth) / 2);
		int width = (a > 0 ? a : 0) + dp6 * 2 + textTvWidth;
		return width;
	}

	public void setBadgeNum(int num) {
		this.badgeNum = num;
		badgeTv.setText( String.valueOf( badgeNum ) );
		if (0 != badgeNum) {
			imageView.setBackground( ContextCompat.getDrawable( getContext(), iconId ) );
			textTv.setTextColor( textColor );
		}
		requestLayout();
	}
}