package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.RadioButton;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

/**
 * 自定义tabview 底部按钮 ClassName: TabButtonView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017-3-2 下午5:16:01 <br/>
 *
 * @author John Lie
 * @since JDK 1.6
 */
@SuppressLint({"NewApi", "InflateParams"})
public class TabButtonView extends RadioButton {
    Context mContext;
    TypedArray typedArray;// 属性组
    int defultIcon = -1;// 默认图标图片
    int checkedIcon = -1;// 选中图标图片
    Drawable top = null, top1 = null;// 两种状态的drawabletop 图片；

    public TabButtonView(Context context, AttributeSet attrs, int defStyleAttr,
                         int defStyleRes) {
        super( context, attrs, defStyleAttr, defStyleRes );
        this.mContext = context;
        /* 获取自定义属性组 */
        typedArray = mContext.getTheme().obtainStyledAttributes( attrs,
                R.styleable.TabButtonAttr, defStyleRes, 0 );
        initView();
    }

    public TabButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr, 0 );
        this.mContext = context;
        typedArray = mContext.getTheme().obtainStyledAttributes( attrs,
                R.styleable.TabButtonAttr, 0, 0 );
        initView();
    }

    public TabButtonView(Context context, AttributeSet attrs) {
        super( context, attrs, android.R.attr.radioButtonStyle );
        this.mContext = context;
        typedArray = mContext.getTheme().obtainStyledAttributes( attrs,
                R.styleable.TabButtonAttr, 0, 0 );
        initView();
    }

    public TabButtonView(Context context) {
        super( context, null );
        this.mContext = context;
        typedArray = mContext.getTheme().obtainStyledAttributes( null,
                R.styleable.TabButtonAttr, 0, 0 );
        initView();
    }

    public void setCheckedIcon(int defultIcon, int checkedIcon) {
        top1 = getResources().getDrawable( defultIcon );
        top = getResources().getDrawable( checkedIcon );
        invalidate();
    }

    /**
     * initView:(初始化). <br/>
     *
     * @author John Lie
     * @since JDK 1.6
     */
    @SuppressWarnings("deprecation")
    private void initView() {
        try {
            int num = typedArray.getIndexCount();// 获取有多少个属性
            /* 遍历属性组 */
            for (int i = 0; i < num; i++) {
                /* 获取一个属性 */
                int attr = typedArray.getIndex( i );
                switch (attr) {
                    case R.styleable.TabButtonAttr_text:
                        setText( typedArray.getString( i ) );// 设置文字
                        break;
                    case R.styleable.TabButtonAttr_defultIcon:
                        top1 = mContext.getResources().getDrawable(
                                typedArray.getResourceId( i, 0 ) );// 获取默认图片属性
                        break;
                    case R.styleable.TabButtonAttr_checkedIcon:
                        top = mContext.getResources().getDrawable(
                                typedArray.getResourceId( i, 0 ) );// 获取选中图片属性
                        break;
                }
            }
            top1.setBounds( 0, 0, top1.getMinimumWidth(), top1.getMinimumHeight() );
            top.setBounds( 0, 0, top.getMinimumWidth(), top.getMinimumHeight() );

        } catch (Exception e) {
            e.printStackTrace();
        }
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension( measureWidth( widthMeasureSpec ),
                measureHeight( heightMeasureSpec ) );
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode( measureSpec );
        int size = MeasureSpec.getSize( measureSpec );
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = DisplayMetricsUtils.dipTopx( getContext(), 45 );
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min( result, size );
            }
        }
        return result;

    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode( measureSpec );
        int size = MeasureSpec.getSize( measureSpec );
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = DisplayMetricsUtils.dipTopx( getContext(), 45 );//根据自己的需要更改
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min( result, size );
            }
        }
        return result;

    }

    @Override
    public void toggle() {
        if (!isChecked()) {
            super.toggle();
        }
    }


    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent( event );
        event.setClassName( TabButtonView.class.getName() );
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo( info );
        info.setClassName( TabButtonView.class.getName() );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isChecked()) {
            setCompoundDrawablesWithIntrinsicBounds( null, top, null, null );
        } else {
            setCompoundDrawablesWithIntrinsicBounds( null, top1, null, null );
        }
        super.onDraw( canvas );
    }

}
