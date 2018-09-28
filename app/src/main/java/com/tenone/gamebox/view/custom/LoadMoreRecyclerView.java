package com.tenone.gamebox.view.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.john.superadapter.SuperAdapter;
import com.tenone.gamebox.mode.listener.OnLoadMoreListener;

public class LoadMoreRecyclerView extends RecyclerView {
	private final int STATE_LOADING = 0x01;
	private final int STATE_FAILURE = 0x02;
	private final int STATE_COMPLETE = 0x03;
	private boolean isLoading = false;

	// 滑到底部里最后一个个数的阀值
	private static final int VISIBLE_THRESHOLD = 1;

	private int state;
	private boolean loadMoreEnabled = true;
	private OnLoadMoreListener listener;
	private SuperAdapter superAdapter;
	private LinearLayoutManager layoutManager;
	private View emptyView;

	public LoadMoreRecyclerView(Context context) {
		this( context, null );
	}

	public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
		this( context, attrs, 0 );
	}

	public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
		super( context, attrs, defStyle );
		((SimpleItemAnimator) getItemAnimator()).setSupportsChangeAnimations( false );
	}

	public void initLoadMore(@NonNull OnLoadMoreListener listener) {
		this.listener = listener;
		if (getAdapter() instanceof SuperAdapter) {
			superAdapter = (SuperAdapter) getAdapter();
		}
		layoutManager = (LinearLayoutManager) getLayoutManager();
		addOnScrollListener( new OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged( recyclerView, newState );
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled( recyclerView, dx, dy );
				if (canLoadMore() && dy >= 0) {
					int totalItemCount = layoutManager.getItemCount();
					int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
					if (lastVisibleItem + VISIBLE_THRESHOLD >= totalItemCount) {
						if (isLoading) {
							return;
						}
						Log.i( "LoadMoreRecyclerView", "onLoadMore  lastVisibleItem is " + lastVisibleItem + " totalItemCount is " + totalItemCount );
						onLoadMore( recyclerView );
					}
				}
			}
		} );
	}

	public void setLoadMoreEnabled(boolean enabled) {
		this.loadMoreEnabled = enabled;
	}

	private boolean canLoadMore() {
		return state != STATE_LOADING && loadMoreEnabled;
	}

	private void onLoadMore(RecyclerView recyclerView) {
		isLoading = true;
		state = STATE_LOADING;
		recyclerView.post( () -> {
			if (state == STATE_LOADING) {
				if (superAdapter != null) {
					LoadMoreView loadMoreView = new LoadMoreView( getContext() );
					loadMoreView.loading();
					superAdapter.addFooterView( loadMoreView );
				}
			}
		} );
		if (listener != null) {
			listener.onLoadMore();
		}
	}

	public void loadMoreFailed() {
		state = STATE_FAILURE;
		if (superAdapter != null) {
			superAdapter.removeFooterView();
		}
		isLoading = false;
	}

	public void loadMoreComplete() {
		state = STATE_COMPLETE;
		if (superAdapter != null) {
			superAdapter.removeFooterView();
		}
		isLoading = false;
	}

	public void setEmptyView(View emptyView) {
		this.emptyView = emptyView;
	}

	@Override
	public void setAdapter(Adapter adapter) {
		super.setAdapter( adapter );
		if (adapter != null) {
			adapter.registerAdapterDataObserver( adapterDataObserver );
		}
		adapterDataObserver.onChanged();
	}

	private AdapterDataObserver adapterDataObserver = new AdapterDataObserver() {
		@Override
		public void onChanged() {
			Adapter<?> adapter = getAdapter();
			if (adapter != null && emptyView != null) {
				if (adapter.getItemCount() == 0) {
					emptyView.setVisibility( VISIBLE );
					LoadMoreRecyclerView.this.setVisibility( GONE );
				} else {
					emptyView.setVisibility( GONE );
					LoadMoreRecyclerView.this.setVisibility( VISIBLE );
				}
			}
		}
	};
}
