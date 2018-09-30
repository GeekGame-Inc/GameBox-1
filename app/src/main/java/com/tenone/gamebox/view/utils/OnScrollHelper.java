package com.tenone.gamebox.view.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.tenone.gamebox.mode.listener.OnScrollStateListener;
import com.tenone.gamebox.mode.mode.OnScrollState;
import com.tenone.gamebox.view.custom.RefreshLayout;

import java.util.Vector;

public class OnScrollHelper {


	private static OnScrollHelper instance;
	private boolean changed = false;

	public OnScrollHelper() {
		listeners = new Vector<OnScrollStateListener>();
	}

	public static OnScrollHelper getInstance() {
		if (instance == null) {
			synchronized (OnScrollHelper.class) {
				if (instance == null) {
					instance = new OnScrollHelper();
				}
			}
		}
		return instance;
	}

	private Vector<OnScrollStateListener> listeners;


	public synchronized void registerOnScrollWatcher(OnScrollStateListener listener) {
		if (listener == null) {
			throw new NullPointerException();
		}
		if (!listeners.contains( listener )) {
			listeners.addElement( listener );
		}
	}

	public synchronized void deleteObserver(OnScrollStateListener listener) {
		listeners.removeElement( listener );
	}


	@SuppressWarnings("deprecation")
	public void onScrollStateUpdate(Object object) {
		if (object instanceof ListView) {
			((ListView) object).setOnScrollListener( new AbsListView.OnScrollListener() {
				private boolean fromTouch;

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
						fromTouch = true;
					}
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
					resultAbsListViewOnScroll( view, firstVisibleItem, visibleItemCount, totalItemCount, fromTouch );
				}
			} );
		}
		if (object instanceof RefreshLayout) {
			((RefreshLayout) object).setOnScrollListener( new RefreshLayout.OnScrollListener() {
				private boolean fromTouch;

				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
						fromTouch = true;
					}
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
					resultAbsListViewOnScroll( view, firstVisibleItem, visibleItemCount, totalItemCount, fromTouch );
				}
			} );
		}
		if (object instanceof RecyclerView) {
			((RecyclerView) object).addOnScrollListener( new RecyclerView.OnScrollListener() {
				private boolean fromTouch;

				public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
					fromTouch = newState == RecyclerView.SCROLL_STATE_DRAGGING;
				}

				public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
					if (!fromTouch) {
						return;
					}
					if (dy >= 0) {
						update( OnScrollState.SCROLL_DOWN );
					} else {
						update( OnScrollState.SCROLL_UP );
					}
				}
			} );
		}
	}

	private int lastScrollY, mTouchY, lastVisibleItem;

	private void resultAbsListViewOnScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, boolean fromTouch) {
		if (!fromTouch) {
			return;
		}
		final int tempY = getFirstViewScrollY( view );
		if (lastScrollY == tempY) {
			return;
		}
		lastScrollY = tempY;
		View childAt = view.getChildAt( 0 );
		OnScrollState scrollState = OnScrollState.SCROLL_NO;
		int[] location = new int[2];
		childAt.getLocationOnScreen( location );
		if (firstVisibleItem != lastVisibleItem) {
			if (firstVisibleItem > lastVisibleItem) {
				//	("向上滑动");
				scrollState = OnScrollState.SCROLL_DOWN;
			} else if (firstVisibleItem < lastVisibleItem) {
				//	("向下滑动");
				scrollState = OnScrollState.SCROLL_UP;
			}
			lastVisibleItem = firstVisibleItem;
			mTouchY = location[1];
		} else {
			if (mTouchY > location[1]) {
				//	("->向上滑动");
				scrollState = OnScrollState.SCROLL_DOWN;
			} else if (mTouchY < location[1]) {
				//		("->向下滑动");
				scrollState = OnScrollState.SCROLL_UP;
			} else {
				//	("->未滑动");
				scrollState = OnScrollState.SCROLL_NO;
			}
			mTouchY = location[1];
		}
		update( scrollState );
	}

	public synchronized boolean hasChanged() {
		return changed;
	}

	protected synchronized void setChanged() {
		changed = true;
	}

	protected synchronized void clearChanged() {
		changed = false;
	}

	public void notifyObservers(OnScrollState state) {
		Object[] arrLocal;
		synchronized (this) {
			if (!hasChanged())
				return;
			arrLocal = listeners.toArray();
			clearChanged();
		}
		for (int i = arrLocal.length - 1; i >= 0; i--)
			((OnScrollStateListener) arrLocal[i]).onScrollState( state );
	}


	private void update(OnScrollState state) {
		setChanged();
		notifyObservers( state );
	}

	private int getFirstViewScrollY(AbsListView view) {
		View c = view.getChildAt( 0 );//第一个可见的view
		if (c == null) {
			return 0;
		}
		int top = c.getTop() + view.getPaddingTop();
		return -top;
	}
}
