package com.tenone.gamebox.mode.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by Eddy on 2018/1/26.
 */

public abstract class EndLessOnScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager mLinearLayoutManager;
    private int totalItemCount;

    private int previousTotal = 0;

    private int visibleItemCount;

    private int firstVisibleItem;

    private boolean loading = true;

    public EndLessOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled( recyclerView, dx, dy );
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        Log.i( "onScrolled","visibleItemCount is "+visibleItemCount +" totalItemCount is "+totalItemCount +" firstVisibleItem is "+firstVisibleItem + " loading is " + loading);
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem) {
            onLoadMore();
            loading = true;
        }else {
            loading = false;
        }
    }

    public abstract void onLoadMore();
}
