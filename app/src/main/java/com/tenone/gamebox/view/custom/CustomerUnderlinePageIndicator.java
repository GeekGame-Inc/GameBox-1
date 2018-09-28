package com.tenone.gamebox.view.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.custom.viewpagerindicator.UnderlinePageIndicator;

/**
 * �Զ���UnderlinePageIndicator
 * ClassName: UnderlinePageIndicatorEx <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(��ѡ). <br/>
 * date: 2017-3-6 ����9:35:28 <br/>
 *
 * @author John Lie
 * @since JDK 1.6
 */
public class CustomerUnderlinePageIndicator extends UnderlinePageIndicator {

    public CustomerUnderlinePageIndicator(Context context) {
        super( context, null );
    }

    public CustomerUnderlinePageIndicator(Context context, AttributeSet attrs) {
        super( context, attrs, R.attr.vpiUnderlinePageIndicatorStyle );

    }

    public CustomerUnderlinePageIndicator(Context context, AttributeSet attrs, int defStyle) {
        super( context, attrs, defStyle );
    }


    @Override
    public void setViewPager(ViewPager viewPager) {
        if (mViewPager == viewPager) {
            return;
        }
        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException( "ViewPager does not have adapter instance." );
        }
        mViewPager = viewPager;
        invalidate();
        post( () -> {
            if (mFades) {
                post( mFadeRunnable );
            }
        } );
    }
}
