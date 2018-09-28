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
 * �Զ���tabview �ײ���ť ClassName: TabButtonView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(��ѡ). <br/>
 * date: 2017-3-2 ����5:16:01 <br/>
 *
 * @author John Lie
 * @since JDK 1.6
 */
@SuppressLint({"NewApi", "InflateParams"})
public class TabButtonView extends RadioButton {
    Context mContext;
    TypedArray typedArray;// ������
    int defultIcon = -1;// Ĭ��ͼ��ͼƬ
    int checkedIcon = -1;// ѡ��ͼ��ͼƬ
    Drawable top = null, top1 = null;// ����״̬��drawabletop ͼƬ��

    public TabButtonView(Context context, AttributeSet attrs, int defStyleAttr,
                         int defStyleRes) {
        super( context, attrs, defStyleAttr, defStyleRes );
        this.mContext = context;
        /* ��ȡ�Զ��������� */
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
     * initView:(��ʼ��). <br/>
     *
     * @author John Lie
     * @since JDK 1.6
     */
    @SuppressWarnings("deprecation")
    private void initView() {
        try {
            int num = typedArray.getIndexCount();// ��ȡ�ж��ٸ�����
            /* ���������� */
            for (int i = 0; i < num; i++) {
                /* ��ȡһ������ */
                int attr = typedArray.getIndex( i );
                switch (attr) {
                    case R.styleable.TabButtonAttr_text:
                        setText( typedArray.getString( i ) );// ��������
                        break;
                    case R.styleable.TabButtonAttr_defultIcon:
                        top1 = mContext.getResources().getDrawable(
                                typedArray.getResourceId( i, 0 ) );// ��ȡĬ��ͼƬ����
                        break;
                    case R.styleable.TabButtonAttr_checkedIcon:
                        top = mContext.getResources().getDrawable(
                                typedArray.getResourceId( i, 0 ) );// ��ȡѡ��ͼƬ����
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
            result = DisplayMetricsUtils.dipTopx( getContext(), 45 );//�����Լ�����Ҫ����
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
