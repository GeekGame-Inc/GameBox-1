package com.tenone.gamebox.mode.view;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;

public interface PlatformCoinDetailView {
	TitleBarView getTitleBarView();

	RefreshLayout getRefreshLayout();

	ListView getListView();

	ImageView getEmptyView();

	TextView getToastTv();
}
