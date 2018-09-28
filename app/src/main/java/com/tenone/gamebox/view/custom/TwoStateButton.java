package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.tenone.gamebox.R;

@SuppressLint("NewApi")
public class TwoStateButton extends AppCompatButton {
    private int state = 0;

    private Drawable defultDrawable, otherDrawable;
    private String defultText, otherText;
    private TypedArray typedArray;
    private int defultColor = Color.BLACK, otherColor = Color.WHITE;

    public TwoStateButton(Context context) {
        this( context, null );
    }

    public TwoStateButton(Context context, AttributeSet attrs) {
        this( context, attrs, 0 );
    }

    public TwoStateButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        typedArray = context.getTheme().obtainStyledAttributes( attrs,
                R.styleable.TwoStateButtonAttr, 0, 0 );
        init();
    }


    private void init() {
        try {
            int num = typedArray.getIndexCount();
            for (int i = 0; i < num; i++) {
                int attr = typedArray.getIndex( i );
                switch (attr) {
                    case R.styleable.TwoStateButtonAttr_defultDrawable_TwoStateButton:
                        defultDrawable = typedArray.getDrawable( i );
                        break;
                    case R.styleable.TwoStateButtonAttr_otherDrawable_TwoStateButton:
                        otherDrawable = typedArray.getDrawable( i );
                        break;
                    case R.styleable.TwoStateButtonAttr_defultText_TwoStateButton:
                        defultText = typedArray.getString( i );
                        break;
                    case R.styleable.TwoStateButtonAttr_otherText_TwoStateButton:
                        otherText = typedArray.getString( i );
                        break;
                    case R.styleable.TwoStateButtonAttr_defultColor_TwoStateButton:
                        defultColor = typedArray.getColor( i, Color.BLACK );
                        break;
                    case R.styleable.TwoStateButtonAttr_otherColor_TwoStateButton:
                        otherColor = typedArray.getColor( i, Color.WHITE );
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw( canvas );
        switch (state) {
            case 0:
                setBackground( defultDrawable );
                setText( defultText );
                setTextColor( defultColor );
                break;
            case 1:
                setBackground( otherDrawable );
                setText( otherText );
                setTextColor( otherColor );
                break;
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        invalidate();
    }
}
