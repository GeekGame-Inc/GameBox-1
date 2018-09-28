/**
 * Project Name:GameBox
 * File Name:DownProgress.java
 * Package Name:com.tenone.gamebox.view.custom
 * Date:2017-3-8下午3:06:38
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.GameStatus;

/**
 * ClassName:DownProgress <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-8 下午3:06:38 <br/>
 *
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class DownProgress extends ProgressBar {
    private Paint mPaint;// 画笔
    private Context context;
    private int defultPaintColor;
    float defultSize = 14;
    TypedArray typedArray;
    private int[] paintColor = {
            getResources().getColor( R.color.blue_40 ),
            getResources().getColor( R.color.defultTextColor ),
            getResources().getColor( R.color.white )};
    private String text;

    /**
     * game state 0(no download),1(downloading),2(pause),3(download completed),4(installing),5(install completed),8(no install)
     */
    private int status = -1;

    public DownProgress(Context context, AttributeSet attrs, int defStyleAttr,
                        int defStyleRes) {
        super( context, attrs, defStyleAttr, defStyleRes );
        this.context = context;
        defultPaintColor = getResources().getColor( R.color.defultTextColor );
        typedArray = context.getTheme().obtainStyledAttributes( attrs,
                R.styleable.DownProgressStyle, defStyleRes, 0 );
        initPaint();
    }

    public DownProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        this( context, attrs, defStyleAttr, 0 );
    }

    public DownProgress(Context context, AttributeSet attrs) {
        this( context, attrs, 0 );
    }

    public DownProgress(Context context) {
        this( context, null );
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias( true );
        try {
            int num = typedArray.getIndexCount();
            for (int i = 0; i < num; i++) {
                int attr = typedArray.getIndex( i );
                switch (attr) {
                    case R.styleable.DownProgressStyle_textSize_DownProgressStyle:
                        defultSize = typedArray.getDimension( i, TypedValue
                                .applyDimension( TypedValue.COMPLEX_UNIT_SP, 14,
                                        getResources().getDisplayMetrics() ) );
                        break;
                }
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        mPaint.setColor( defultPaintColor );
        // float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
        // 14,
        // getResources().getDisplayMetrics());
        mPaint.setTextSize( defultSize );
        typedArray.recycle();
    }

    public void setTextProgress(int status, int progress) {
        try {
            mPaint.setColor( defultPaintColor );
            switch (status) {
                case GameStatus.UNLOAD:
                    text = context.getResources().getString( R.string.download );
                    mPaint.setColor( paintColor[0] );
                    break;
                case GameStatus.LOADING:
                    text = (int) ((progress * 1.0f / getMax()) * 100) + "%";
                    break;
                case GameStatus.PAUSEING:
                    text = context.getResources().getString( R.string.go_on );
                    mPaint.setColor( paintColor[0] );
                    break;
                case GameStatus.COMPLETED:
                    text = context.getResources().getString( R.string.install );
                    mPaint.setColor( paintColor[2] );
                    break;
                case GameStatus.INSTALLING:
                    text = context.getResources().getString( R.string.installing );
                    mPaint.setColor( paintColor[2] );
                    break;
                case GameStatus.INSTALLCOMPLETED:
                    text = context.getResources().getString( R.string.open );
                    mPaint.setColor( paintColor[2] );
                    break;
                case GameStatus.UNINSTALLING:
                    text = context.getResources().getString( R.string.install );
                    mPaint.setColor( paintColor[2] );
                    break;
                case GameStatus.UPDATE:
                    text = context.getResources().getString( R.string.update );
                    mPaint.setColor( paintColor[0] );
                    break;
                default:
                    text = (int) ((progress * 1.0f / getMax()) * 100) + "%";
                    break;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        invalidate();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw( canvas );
        Rect rect = new Rect();
        try {
            drawProgressText( canvas, rect );
            drawColorProgressText( canvas, rect );
        } catch (NullPointerException e) {
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        if (status == GameStatus.PAUSEING) {
            setProgress( 0 );
        }
        invalidate();
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress( progress );
        setTextProgress( status, progress );
    }

    private void drawProgressText(Canvas canvas, Rect rect) {
        mPaint.getTextBounds( text, 0, text.length(), rect );
        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText( text, x, y, mPaint );
    }

    private void drawColorProgressText(Canvas canvas, Rect rect) {
        int tWidth = rect.width();
        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        float progressWidth = (getProgress() * 1f / 100f) * getMeasuredWidth();
        if (progressWidth > x) {
            mPaint.setColor( Color.WHITE );
            canvas.save( Canvas.ALL_SAVE_FLAG );
            float right = Math.min( progressWidth, x + tWidth );
            canvas.clipRect( x, 0, right, getMeasuredHeight() );
            canvas.drawText( text, x, y, mPaint );
            canvas.restore();
        }
    }
}
