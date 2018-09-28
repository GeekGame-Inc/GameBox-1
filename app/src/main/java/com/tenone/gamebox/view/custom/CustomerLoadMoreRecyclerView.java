package com.tenone.gamebox.view.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.tenone.gamebox.mode.listener.OnLoadMoreListener;

public class CustomerLoadMoreRecyclerView extends RecyclerView {
    private OnLoadMoreListener mLoadMoreListener;


    public CustomerLoadMoreRecyclerView(Context context) {
        super( context );
    }

    public CustomerLoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );
    }

    public CustomerLoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super( context, attrs, defStyle );
    }

    public void setmLoadMoreListener(OnLoadMoreListener mLoadMoreListener) {
        this.mLoadMoreListener = mLoadMoreListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged( l, t, oldl, oldt );
        int lastVisiblePosition = 0;
        LayoutManager mLayoutManager = getLayoutManager();
        if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisiblePosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisiblePosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            //��ΪStaggeredGridLayoutManager�������Կ��ܵ��������ʾ��item���ڶ������������ȡ������һ������
            //�õ�����������ȡ��������positionֵ�����Ǹ����������ʾ��positionֵ��
            int[] lastPositions = new int[((StaggeredGridLayoutManager) mLayoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions( lastPositions );
            lastVisiblePosition = findMax( lastPositions );
        }
        if (mLayoutManager.getChildCount() > 0             //����ǰ��ʾ��item����>0
                && lastVisiblePosition >= mLayoutManager.getItemCount() - 1           //����ǰ��Ļ���һ��������λ��>=����item������
                && mLayoutManager.getItemCount() > mLayoutManager.getChildCount()) { // ����ǰ��Item�����ڿɼ�Item��
            if (mLoadMoreListener != null) {
                mLoadMoreListener.onLoadMore();
            }
        }
    }
    private int findMax(int[] into) {
        int max = into[0];
        for (int value : into) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
