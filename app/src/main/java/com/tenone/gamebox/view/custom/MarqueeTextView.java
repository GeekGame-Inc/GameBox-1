package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.MarqueeTextViewClickListener;

import java.util.List;

/**
 * Created by Eddy on 2017/11/1.
 */

public class MarqueeTextView extends LinearLayout {
    private Context mContext;
    private ViewFlipper viewFlipper;
    private View marqueeTextView;
    private List<String> textArrays;
    private MarqueeTextViewClickListener marqueeTextViewClickListener;

    public MarqueeTextView(Context context) {
        super( context );
        mContext = context;
        initBasicView();
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super( context, attrs );
        mContext = context;
        initBasicView();
    }

    public void setTextArraysAndClickListener(List<String> text,
                                              MarqueeTextViewClickListener callback) {
        this.textArrays = text;
        this.marqueeTextViewClickListener = callback;
        initMarqueeTextView( textArrays, marqueeTextViewClickListener );
    }

    @SuppressLint("InflateParams")
    public void initBasicView() {
        try {
            marqueeTextView = LayoutInflater.from( mContext ).inflate(
                    R.layout.marquee_textview_layout, null );
            LayoutParams layoutParams = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
            addView( marqueeTextView, layoutParams );
            viewFlipper = marqueeTextView
                    .findViewById( R.id.viewFlipper );
            viewFlipper.setInAnimation( AnimationUtils.loadAnimation( mContext,
                    R.anim.slide_in_bottom ) );
            viewFlipper.setOutAnimation( AnimationUtils.loadAnimation( mContext,
                    R.anim.slide_out_top ) );
            viewFlipper.startFlipping();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public void initMarqueeTextView(List<String> textArrays,
                                    MarqueeTextViewClickListener marqueeTextViewClickListener) {
        if (textArrays.size() == 0) {
            return;
        }
        int i = 0;
        viewFlipper.removeAllViews();
        while (i < textArrays.size()) {
            TextView textView = new TextView( mContext );
            textView.setText( textArrays.get( i ) );
            textView.setMaxLines( 1 );
            textView.setGravity( Gravity.CENTER );
            textView.setEllipsize( TextUtils.TruncateAt.END );
            textView.setTextColor( mContext.getResources().getColor(
                    R.color.gray_9a ) );
            textView.setTextSize( TypedValue.COMPLEX_UNIT_SP, 13 );
            textView.setOnClickListener( marqueeTextViewClickListener );
            LayoutParams lp = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
            viewFlipper.addView( textView, lp );
            i++;
        }
    }

    public void releaseResources() {
        if (marqueeTextView != null) {
            if (viewFlipper != null) {
                viewFlipper.stopFlipping();
                viewFlipper.removeAllViews();
                viewFlipper = null;
            }
            marqueeTextView = null;
        }
    }
}
