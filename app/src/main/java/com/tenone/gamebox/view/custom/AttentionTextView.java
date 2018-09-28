package com.tenone.gamebox.view.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.ChenColorUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;



public class AttentionTextView extends AppCompatTextView {
    private Drawable drawable1, drawable2, drawable3;

    public AttentionTextView(Context context) {
        this( context, null );
    }

    public AttentionTextView(Context context, AttributeSet attrs) {
        this( context, attrs, 0 );
    }

    public AttentionTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        init();
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected( selected );
        setText( selected ? "\u53d6\u6d88\u5173\u6ce8" : "\u52a0\u5173\u6ce8" );
        setTextColor( getContext().getResources().getColor( selected ? R.color.gray_9a : R.color.blue_40 ) );
        setCompoundDrawables( null, selected ? drawable2 : drawable1, null, null );
        invalidate();
    }

    private void init() {
        drawable1 = ChenColorUtils.tintDrawable( getContext().getResources().getDrawable( R.drawable.icon_dynamic_yiguanzhu ), ColorStateList.valueOf( getResources().getColor( R.color.blue_40 ) ) );
        drawable2 = ChenColorUtils.tintDrawable( getContext().getResources().getDrawable( R.drawable.icon_dynamic_quxiaoguanzhu ), ColorStateList.valueOf( getResources().getColor( R.color.gray_9a ) ) );
        drawable3 = getContext().getResources().getDrawable( R.drawable.icon_xianghuguangzhu );
        setCompoundDrawablePadding( DisplayMetricsUtils.dipTopx( getContext(), 2 ) );
        
        drawable1.setBounds( 0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight() );
        drawable2.setBounds( 0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight() );
        drawable3.setBounds( 0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight() );
        setCompoundDrawables( null, drawable1, null, null );
    }

    public void setStatus(int status) {
        Drawable drawable = null;
        String text = "";
        int colorId = 0;
        switch (status) {
            case 0:
                drawable = drawable1;
                text = "\u52a0\u5173\u6ce8";
                colorId = R.color.blue_40;
                break;
            case 1:
                drawable = drawable2;
                text = "\u53d6\u6d88\u5173\u6ce8";
                colorId = R.color.gray_9a;
                break;
            case 2:
                drawable = drawable3;
                colorId = R.color.gray_9a;
                break;
        }
        setText( text );
        setTextColor( getContext().getResources().getColor( colorId ) );
        setCompoundDrawables( null, drawable, null, null );
        invalidate();
    }


}
