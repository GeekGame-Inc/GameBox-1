package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OnRemoveItemListViewDeleteClickListener;
import com.tenone.gamebox.view.utils.MyLog;

public class RemoveItemListView extends ListView implements AbsListView.OnScrollListener {
    //��һ�εĴ�����
    private int mLastX, mLastY;
    //��ǰ������item��λ��
    private int mPosition = -1;

    //item��Ӧ�Ĳ���
    private View mItemLayout;
    //ɾ����ť
    private TextView mDelete;

    //��󻬶�����(��ɾ����ť�Ŀ��)
    private int mMaxLength;
    //�Ƿ��ڴ�ֱ�����б�
    private boolean isDragging;
    //item���ڷ������ָ�ƶ�
    private boolean isItemMoving;

    //item�Ƿ�ʼ�Զ�����
    private boolean isStartScroll;
    //ɾ����ť״̬   0���ر� 1����Ҫ�ر� 2����Ҫ�� 3����
    private int mDeleteBtnState;

    //�����ָ�ڻ��������е��ٶ�
    private VelocityTracker mVelocityTracker;
    private Scroller mScroller;
    private OnRemoveItemListViewDeleteClickListener mListener;

    public RemoveItemListView(Context context) {
        this( context, null, 0 );
    }

    public RemoveItemListView(Context context, AttributeSet attrs) {
        this( context, attrs, 0 );
    }

    public RemoveItemListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        mScroller = new Scroller( context, new LinearInterpolator() );
        mVelocityTracker = VelocityTracker.obtain();
        setOnScrollListener( this );
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        MyLog.d( "onTouchEvent" );
        mVelocityTracker.addMovement( e );
        int x = (int) e.getX();
        int y = (int) e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mDeleteBtnState == 0) {
                    mPosition = pointToPosition( x, y );
                    if (mPosition > -1) {
                        mItemLayout = getChildAt( mPosition );
                        if (mItemLayout == null) {
                            return false;
                        }
                        mDelete = mItemLayout.findViewById( R.id.item_delete );
                        mMaxLength = mDelete.getWidth();
                        mDelete.setOnClickListener( v -> {
                            if (mListener != null) {
                                mListener.onRemoveItemListViewDeleteClick( mPosition );
                            }
                            mItemLayout.scrollTo( 0, 0 );
                            mDeleteBtnState = 0;
                        } );
                    } else if (mDeleteBtnState == 3) {
                        mScroller.startScroll( mItemLayout.getScrollX(), 0, -mMaxLength, 0, 200 );
                        invalidate();
                        mDeleteBtnState = 0;
                        return false;
                    } else {
                        return false;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mItemLayout != null) {
                    int dx = mLastX - x;
                    int dy = mLastY - y;
                    int scrollX = mItemLayout.getScrollX();
                    if (Math.abs( dx ) > Math.abs( dy )) {//��߽���
                        isItemMoving = true;
                        if (scrollX + dx <= 0) {
                            mItemLayout.scrollTo( 0, 0 );
                            return true;
                        } else if (scrollX + dx >= mMaxLength) {//�ұ߽���
                            mItemLayout.scrollTo( mMaxLength, 0 );
                            return true;
                        }
                        mItemLayout.scrollBy( dx, 0 );//item������ָ����
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mItemLayout != null) {
                    if (!isItemMoving && !isDragging && mListener != null) {
                        mListener.onRemoveItemListViewItemClick( mItemLayout, mPosition );
                    }
                    isItemMoving = false;
                    mVelocityTracker.computeCurrentVelocity( 1000 );//������ָ�������ٶ�
                    float xVelocity = mVelocityTracker.getXVelocity();//ˮƽ�����ٶȣ�����Ϊ����
                    float yVelocity = mVelocityTracker.getYVelocity();//��ֱ�����ٶ�
                    int deltaX = 0;
                    int upScrollX = mItemLayout.getScrollX();
                    if (Math.abs( xVelocity ) > 100 && Math.abs( xVelocity ) > Math.abs( yVelocity )) {
                        if (xVelocity <= -100) {//���ٶȴ���100����ɾ����ť��ʾ
                            deltaX = mMaxLength - upScrollX;
                            mDeleteBtnState = 2;
                        } else if (xVelocity > 100) {//�һ��ٶȴ���100����ɾ����ť����
                            deltaX = -upScrollX;
                            mDeleteBtnState = 1;
                        }
                    } else {
                        if (upScrollX >= mMaxLength / 2) {//item���󻬶��������ɾ����ť��ȵ�һ�룬����ʾɾ����ť
                            deltaX = mMaxLength - upScrollX;
                            mDeleteBtnState = 2;
                        } else if (upScrollX < mMaxLength / 2) {//��������
                            deltaX = -upScrollX;
                            mDeleteBtnState = 1;
                        }
                    }
                    //item�Զ�������ָ��λ��
                    mScroller.startScroll( upScrollX, 0, deltaX, 0, 200 );
                    isStartScroll = true;
                    invalidate();
                    mVelocityTracker.clear();
                }
                break;
        }
        mLastX = x;
        mLastY = y;
        return super.onTouchEvent( e );
    }

    public void setmListener(OnRemoveItemListViewDeleteClickListener mListener) {
        this.mListener = mListener;
    }

    @SuppressLint("InflateParams")
    @Override
    public void setAdapter(ListAdapter adapter) {
        View mListViewFooter = LayoutInflater.from( getContext() ).inflate(
                R.layout.layout_footer, null, false );
        mListViewFooter.setVisibility( View.GONE );
        addFooterView( mListViewFooter );
        super.setAdapter( adapter );
        removeFooterView( mListViewFooter );
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mItemLayout.scrollTo( mScroller.getCurrX(), mScroller.getCurrY() );
            invalidate();
        } else if (isStartScroll) {
            isStartScroll = false;
            if (mDeleteBtnState == 1) {
                mDeleteBtnState = 0;
            }

            if (mDeleteBtnState == 2) {
                mDeleteBtnState = 3;
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.recycle();
        super.onDetachedFromWindow();
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        isDragging = scrollState == SCROLL_STATE_TOUCH_SCROLL;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }
}
