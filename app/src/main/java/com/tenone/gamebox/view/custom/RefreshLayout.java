package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

@SuppressLint("ClickableViewAccessibility")
public class RefreshLayout extends SwipeRefreshLayout implements
        AbsListView.OnScrollListener {

    private int mTouchSlop;
    private ListView mListView;

    private OnLoadListener mOnLoadListener;

    private View mListViewFooter;

    private int mYDown;
    private int mLastY;
    private boolean isLoading = false;

    public RefreshLayout(Context context) {
        this( context, null );
    }

    @SuppressLint("InflateParams")
    public RefreshLayout(Context context, AttributeSet attrs) {
        super( context, attrs );
        mTouchSlop = ViewConfiguration.get( context ).getScaledTouchSlop();
        mListViewFooter = LayoutInflater.from( context ).inflate(
                R.layout.layout_footer, null, false );
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout( changed, left, top, right, bottom );
        if (mListView == null) {
            getListView();
        }
    }

    private void getListView() {
        int childs = getChildCount();
        if (childs > 0) {
            View childView = getChildAt( 0 );
            if (childView instanceof ListView) {
                mListView = (ListView) childView;
                mListView.setOnScrollListener( this );
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mListView != null && mListView.getAdapter() != null && mListView.getAdapter().getCount() > 5) {
            final int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mYDown = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mLastY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    if ((Math.abs( mLastY - mYDown ) >= DisplayMetricsUtils.dipTopx( getContext(), 20 ))
                            && canLoad() && !isRefreshing()) {
                        if (mListView != null) {
                            loadData();
                        }
                    }
                    break;
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (isLoading) {
            return true;
        }
        return super.onTouchEvent( arg0 );
    }

    private boolean canLoad() {
        return isBottom() && !isLoading && isPullUp();
    }

    private boolean isBottom() {
        if (mListView != null && mListView.getAdapter() != null) {
            return mListView.getLastVisiblePosition() == (mListView
                    .getAdapter().getCount() - 1);
        }
        return false;
    }

    private boolean isPullUp() {
        return (mYDown - mLastY) >= (mTouchSlop + 40);
    }

    private void loadData() {
        if (mOnLoadListener != null) {
            setLoading( true );
            mOnLoadListener.onLoad();
        }
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        if (isLoading) {
            mListView.addFooterView( mListViewFooter, null, false );
        } else {
            if (mListView != null && mListViewFooter != null) {
                mListView.removeFooterView( mListViewFooter );
            }
            mYDown = 0;
            mLastY = 0;
        }
    }

    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (listener != null) {
            listener.onScrollStateChanged( view, scrollState );
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (canLoad() && !isRefreshing()) {
            loadData();
        }
        if (listener != null) {
            listener.onScroll( view, firstVisibleItem, visibleItemCount,
                    totalItemCount );
        }
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }


    OnScrollListener listener;


    public interface OnLoadListener {
        void onLoad();
    }

    public interface OnScrollListener {

        void onScrollStateChanged(AbsListView view, int scrollState);

        void onScroll(AbsListView view, int firstVisibleItem,
											int visibleItemCount, int totalItemCount);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isRefreshing()) {
                setRefreshing( false );
            }
            if (isLoading) {
                setLoading( false );
            }
            return true;
        }
        return super.onKeyDown( keyCode, event );
    }
}
