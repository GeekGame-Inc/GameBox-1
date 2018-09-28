package com.tenone.gamebox.view.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.custom.loadingindicator.AVLoadingIndicatorView;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

public class LoadMoreView extends LinearLayout {
	TextView textView;
	AVLoadingIndicatorView avLoadingIndicatorView;

	public LoadMoreView(@NonNull Context context) {
		this( context, null );
	}

	public LoadMoreView(@NonNull Context context, @Nullable AttributeSet attrs) {
		this( context, attrs, 0 );
	}

	public LoadMoreView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super( context, attrs, defStyleAttr );
		init( context );
	}

	private void init(Context context) {
		setBackgroundColor( ContextCompat.getColor( context, R.color.white ) );
		setOrientation( HORIZONTAL );
		setGravity( Gravity.CENTER );
		int dp7 = DisplayMetricsUtils.dipTopx( context, 5 );
		setPadding( 5, 5, 5, 5 );
		textView = new TextView( context );
		textView.setTextSize( TypedValue.COMPLEX_UNIT_SP, 13 );
		avLoadingIndicatorView = new AVLoadingIndicatorView( context );
		avLoadingIndicatorView.setIndicatorColor( ContextCompat.getColor( context, R.color.black ) );
		avLoadingIndicatorView.setmMaxHeight( DisplayMetricsUtils.dipTopx( context, 50 ) );
		avLoadingIndicatorView.setmMaxWidth( DisplayMetricsUtils.dipTopx( context, 50 ) );
		avLoadingIndicatorView.setmMinHeight( DisplayMetricsUtils.dipTopx( context, 35 ) );
		avLoadingIndicatorView.setmMinWidth( DisplayMetricsUtils.dipTopx( context, 35 ) );
		avLoadingIndicatorView.setIndicator( "BallPulseIndicator" );
		addView( textView );
		addView( avLoadingIndicatorView );
		LinearLayout.LayoutParams params = (LayoutParams) avLoadingIndicatorView.getLayoutParams();
		params.leftMargin = dp7;
		avLoadingIndicatorView.setLayoutParams( params );
	}

	public void failure() {
		textView.setText( getContext().getString( R.string.no_data ) + "..." );
	}

	public void loading() {
		textView.setText( getContext().getString( R.string.loading ) );
	}


	public void complete() {
		textView.setText( getContext().getString( R.string.loaded ) + "..." );
	}
}
