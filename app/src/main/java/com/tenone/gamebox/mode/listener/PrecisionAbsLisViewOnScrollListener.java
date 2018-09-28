package com.tenone.gamebox.mode.listener;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AbsListView;

import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

public abstract class PrecisionAbsLisViewOnScrollListener implements RefreshLayout.OnScrollListener {

	private Context context;
	private int searchLayoutHeight;

	public PrecisionAbsLisViewOnScrollListener(Context context, int searchLayoutHeight) {
		this.context = context;
		this.searchLayoutHeight = searchLayoutHeight;
	}
	@SuppressWarnings( "unchecked" )
	private SparseArray<ItemRecod> recordSp = new SparseArray( 0 );
	private int mCurrentfirstVisibleItem = 0;
	boolean isSendGoneMessage = false, isSendVisibleMessage = false;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		mCurrentfirstVisibleItem = firstVisibleItem;
		View firstView = view.getChildAt( 0 );
		if (null != firstView) {
			ItemRecod itemRecord = recordSp.get( firstVisibleItem );
			if (null == itemRecord) {
				itemRecord = new ItemRecod();
			}
			//item�߶�
			itemRecord.height = firstView.getHeight();
			//����λ�þඥ������(��ֵ)
			itemRecord.top = firstView.getTop();
			recordSp.append( firstVisibleItem, itemRecord );
		}

		int scrollY = getScrollY();
		int dp5 = DisplayMetricsUtils.dipTopx( context, 5 );
		int critical = searchLayoutHeight + dp5;
		if (scrollY > critical) {
			if (!isSendVisibleMessage) {
				onVisibility( View.VISIBLE );
				isSendVisibleMessage = true;
				isSendGoneMessage = false;
			}
		} else {
			if (!isSendGoneMessage) {
				onVisibility( View.GONE );
				isSendGoneMessage = true;
				isSendVisibleMessage = false;
			}
		}
		Log.d( "BTGame", getScrollY() + " search layout height is " + searchLayoutHeight + "  dp5 is " + dp5 );
	}

	//��ȡ������ȷֵ
	private int getScrollY() {
		int height = 0;
		for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
			ItemRecod itemRecod = recordSp.get( i );
			if (itemRecod != null) {
				height += itemRecod.height;
			}
		}
		ItemRecod itemRecod = recordSp.get( mCurrentfirstVisibleItem );
		if (null == itemRecod) {
			itemRecod = new ItemRecod();
		}
		return height - itemRecod.top;
	}

	private class ItemRecod {
		int height = 0;
		int top = 0;
	}

	public abstract void onVisibility(int visibility);
}
