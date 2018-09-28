package com.tenone.gamebox.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.PlatformCoinBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.PlatformCoinListener;
import com.tenone.gamebox.mode.mode.CommonViewHolder;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.PlatformCoinModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.adapter.CommonAdapter;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpManager;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("InflateParams")
public class PlatformFragment extends Fragment implements OnRefreshListener,
		OnLoadListener, HttpResultListener, PlatformCoinListener {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_platform_coin_refresh)
	RefreshLayout refreshLayout;
	@ViewInject(R.id.id_platform_coin_listview)
	ListView listView;
	@ViewInject(R.id.id_platform_coin_emptyView)
	ImageView emptyView;
	@ViewInject(R.id.id_platform_coin_toast)
	TextView toastTv;
	private Context mContext;
	private CommonAdapter<PlatformCoinModel> mAdapter;
	private List<PlatformCoinModel> models = new ArrayList<PlatformCoinModel>();
	private int page = 1,who =0;
	private PlatformCoinBiz biz;
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_gold_details, container,
				false);
		ViewUtils.inject(this, view);
		biz = new PlatformCoinBiz();
		toastTv.setVisibility(View.GONE);
		mContext = getActivity();
		initAdapter();
		requestHttp(HttpType.REFRESH);
		refreshLayout.setOnRefreshListener(this);
		refreshLayout.setOnLoadListener(this);
		return view;
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
		listView.addHeaderView(view);
		listView.setAdapter(mAdapter);
		listView.setEmptyView(emptyView);
	}

	/**
	 * ��ȡ���� requestHttp:(������һ�仰�����������������). <br/>
	 * 
	 * @author John Lie
	 * @param what
	 * @since JDK 1.6
	 */
	public void requestHttp(int what) {
		HttpManager.platformCoinDetails(what, mContext, this, page, false);
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
		refreshLayout.setRefreshing(false);
		refreshLayout.setLoading(false);
		if (1 == resultItem.getIntValue("status")) {
			ResultItem item = resultItem.getItem("data");
			try {
				constructModels(item);
			} catch (NullPointerException e) {
				page = page > 1 ? (page - 1) : 1;
				showToast(mContext, "û��������");
			}
		} else {
			page = page > 1 ? (page - 1) : 1;
			showToast(mContext, resultItem.getString("msg"));
		}
	}

	@Override
	public void onError(int what, String error) {
		refreshLayout.setRefreshing(false);
		refreshLayout.setLoading(false);
	}
	
	public void showToast(Context mContext, String text) {
		ToastCustom.makeText(mContext, text, ToastCustom.LENGTH_SHORT).show();
	}
	
	public void constructModels(ResultItem resultItem) {
		biz.constructModels(resultItem, this, false);
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
}