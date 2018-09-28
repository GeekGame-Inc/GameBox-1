package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.PlatformCoinBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.PlatformCoinListener;
import com.tenone.gamebox.mode.mode.CommonViewHolder;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.PlatformCoinModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.PlatformCoinDetailView;
import com.tenone.gamebox.view.adapter.CommonAdapter;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpManager;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("InflateParams")
public class PlatformCoinPresenter extends BasePresenter implements
		PlatformCoinListener, OnClickListener, OnRefreshListener,
		OnLoadListener, HttpResultListener {
	private Context mContext;
	private PlatformCoinDetailView detailView;
	private PlatformCoinBiz biz;
	private CommonAdapter<PlatformCoinModel> mAdapter;
	private List<PlatformCoinModel> models = new ArrayList<PlatformCoinModel>();
	private int page = 1, who;
	private boolean type = false;

	public PlatformCoinPresenter(Context cxt, PlatformCoinDetailView v, boolean t) {
		this.mContext = cxt;
		this.detailView = v;
		this.type = t;
		this.biz = new PlatformCoinBiz();
	}

	public void initView() {
		showCustomToast(mContext, "\u53ea\u80fd\u67e5\u770b\u6700\u8fd17\u5929\u7684\u8bb0\u5f55\u54e6\uff01", Gravity.CENTER);
		getToastTv().setVisibility(type ? View.VISIBLE : View.GONE);
		initTitle();
		initAdapter();
		initListener();
		requestHttp(HttpType.REFRESH);
	}

	private void initTitle() {
		getTitleBarView().setLeftImg(R.drawable.icon_xqf_b);
		getTitleBarView().setTitleText(type ? "\u91d1\u5e01\u660e\u7ec6" : "\u5e73\u53f0\u5e01\u660e\u7ec6" );
	}

	private void initAdapter() {
		mAdapter = new CommonAdapter<PlatformCoinModel>(mContext, models,
				R.layout.item_platform_coin_detail) {

			@Override
			public void convert(CommonViewHolder holder, PlatformCoinModel t) {
				holder.setText(R.id.id_item_platform_coin_time, t.getTime());
				holder.setText(R.id.id_item_platform_coin_type, t.getType());
				holder.setText(R.id.id_item_platform_coin_change, t.getNum());
				holder.setText(R.id.id_item_platform_coin_counts, t.getCounts());
				holder.setTextColorRes(R.id.id_item_platform_coin_counts,
						R.color.appColor);
			}
		};
		int pandding = DisplayMetricsUtils.dipTopx(mContext, 10);
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.item_platform_coin_detail, null);
		view.setPadding(0, pandding, 0, pandding);
		getListView().addHeaderView(view);
		getListView().setAdapter(mAdapter);
		getListView().setEmptyView(getEmptyView());
	}

	private void initListener() {
		getTitleBarView().getLeftImg().setOnClickListener(this);
		getRefreshLayout().setOnRefreshListener(this);
		getRefreshLayout().setOnLoadListener(this);
	}

	public void requestHttp(int what) {
		HttpManager.platformCoinDetails(what, mContext, this, page, type);
	}

	private TitleBarView getTitleBarView() {
		return detailView.getTitleBarView();
	}

	private RefreshLayout getRefreshLayout() {
		return detailView.getRefreshLayout();
	}

	private ListView getListView() {
		return detailView.getListView();
	}

	private ImageView getEmptyView() {
		return detailView.getEmptyView();
	}

	private TextView getToastTv() {
		return detailView.getToastTv();
	}

	public void constructModels(ResultItem resultItem) {
		biz.constructModels(resultItem, this, type);
	}

	@Override
	public void onPlatformCoinConstruct(List<PlatformCoinModel> list) {
		if (HttpType.REFRESH == who) {
			models.clear();
		}
		if (list != null) {
			models.addAll(list);
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		if (R.id.id_titleBar_leftImg == v.getId())
			close(mContext);
	}

	@Override
	public void onRefresh() {
		page = 1;
		requestHttp(HttpType.REFRESH);
	}

	@Override
	public void onLoad() {
		page += 1;
		requestHttp(HttpType.LOADING);
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		who = what;
		getRefreshLayout().setRefreshing(false);
		getRefreshLayout().setLoading(false);
		if (1 == resultItem.getIntValue("status")) {
			ResultItem item = resultItem.getItem("data");
			try {
				constructModels(item);
			} catch (NullPointerException e) {
				page = page > 1 ? (page - 1) : 1;
				showToast(mContext, "\u6ca1\u6709\u6570\u636e\u4e86" );
			}
		} else {
			page = page > 1 ? (page - 1) : 1;
			showToast(mContext, resultItem.getString("msg"));
		}
	}

	@Override
	public void onError(int what, String error) {
		getRefreshLayout().setRefreshing(false);
		getRefreshLayout().setLoading(false);
		showToast(mContext, error);
	}
}
