/** 
 * Project Name:GameBox 
 * File Name:GameClassifyPresenter.java 
 * Package Name:com.tenone.gamebox.presenter 
 * Date:2017-3-9����4:27:06 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.GameClassifyBiz;
import com.tenone.gamebox.mode.listener.GameItemClickListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameClassify;
import com.tenone.gamebox.mode.mode.GameClassifyModel;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.GameClassifyView;
import com.tenone.gamebox.view.activity.GameClassifyTab;
import com.tenone.gamebox.view.activity.GameDetailsActivity;
import com.tenone.gamebox.view.adapter.ClassifyExpandableAdapter;
import com.tenone.gamebox.view.adapter.GameClassifyFragmentAdapter;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.MyExpandableListView;
import com.tenone.gamebox.view.custom.MyGridView;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.cache.ACache;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * ClassName:GameClassifyPresenter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-9 ����4:27:06 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class GameClassifyPresenter extends BasePresenter implements
		OnItemClickListener, HttpResultListener, GameItemClickListener,
		OnRefreshListener, OnLoadListener {
	GameClassifyView classifyView;
	GameClassifyBiz clssifyBiz;
	Context mContext;
	GameClassifyFragmentAdapter mAdapter;
	ClassifyExpandableAdapter expandableAdapter;
	int page = 1;
	List<GameClassify> items = new ArrayList<GameClassify>();

	List<GameClassifyModel> classifyModels = new ArrayList<GameClassifyModel>();
	View headerView;
	ACache cache;

	public GameClassifyPresenter(GameClassifyView view, Context cxt) {
		this.classifyView = view;
		this.mContext = cxt;
		this.clssifyBiz = new GameClassifyBiz();
		cache = ACache.get(mContext);
		getCache();
	}

	@SuppressLint("InflateParams")
	public void initView() {
		headerView = LayoutInflater.from(mContext).inflate(
				R.layout.layout_classify_header, null);
	}

	public void getCache() {
		ResultItem resultItem = (ResultItem) cache.getAsObject("gameClassify");
		if (resultItem != null) {
			items.clear();
			items.addAll(getGameClassifys(resultItem.getItems("class")));
			classifyModels.clear();
			classifyModels.addAll(cinstructRecommendArray(resultItem
					.getItems("classData")));
		}
	}

	/**
	 * ��ʼ�������� setAdapter:(������һ�仰�����������������). <br/>
	 * 
	 * @author John Lie
	 * @since JDK 1.6
	 */
	public void setAdapter() {
		if (expandableAdapter == null) {
			expandableAdapter = new ClassifyExpandableAdapter(classifyModels,
					mContext);
		}
		getExpandableListView().addHeaderView(headerView);
		getExpandableListView().setAdapter(expandableAdapter);
		MyGridView gridView = headerView
				.findViewById(R.id.id_classify_gridView);
		if (mAdapter == null) {
			mAdapter = new GameClassifyFragmentAdapter(mContext, items);
		}
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(this);
		for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
			getExpandableListView().expandGroup(i);
		}
	}

	/**
	 * ��ʼ�������� initListener:(������һ�仰�����������������). <br/>
	 * 
	 * @author John Lie
	 * @since JDK 1.6
	 */
	public void initListener() {
		expandableAdapter.setGridItemClickListener(this);
		getExpandableListView().setOnGroupClickListener(
				new OnGroupClickListener() {
					@Override
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						openOtherActivity(
								mContext,
								new Intent(mContext, GameClassifyTab.class)
										.putExtra(
												"id",
												classifyModels.get(
														groupPosition)
														.getClassId())
										.putExtra(
												"name",
												classifyModels.get(
														groupPosition)
														.getClassName()));
						return true;
					}
				});

		getRefreshLayout().setOnRefreshListener(this);
		getRefreshLayout().setOnLoadListener(this);
	}

	/**
	 * ��ȡ���� requestHttp:(������һ�仰�����������������). <br/>
	 * 
	 * @author John Lie
	 * @param what
	 * @since JDK 1.6
	 */
	public void requestHttp(int what) {
		String url = MyApplication.getHttpUrl().getGameClass();
		RequestBody requestBody = new FormBody.Builder()
				.add("channel", MyApplication.getConfigModle().getChannelID())
				.add("page", page + "").add("system", "1").build();
		HttpUtils.postHttp(mContext, what, url, requestBody, this);
	}

	/**
	 * ��ȡ����Դ getGameClassifys:(������һ�仰�����������������). <br/>
	 * 
	 * @author John Lie
	 * @return
	 * @since JDK 1.6
	 */
	public List<GameClassify> getGameClassifys(List<ResultItem> resultItems) {
		return clssifyBiz.constructArray(resultItems);
	}

	public MyExpandableListView getExpandableListView() {
		return classifyView.getExpandableListView();
	}

	public RefreshLayout getRefreshLayout() {
		return classifyView.getRefreshLayout();
	}

	public List<GameClassifyModel> cinstructRecommendArray(
			List<ResultItem> resultItems) {
		return clssifyBiz.cinstructRecommendArray(resultItems);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		openOtherActivity(mContext, new Intent(mContext, GameClassifyTab.class)
				.putExtra("id", items.get(position).getClassifyId() + "")
				.putExtra("name", items.get(position).getClassifyName()));
	}

	@Override
	public void gameItemClick(GameModel gameMode) {
		openOtherActivity(mContext, new Intent(mContext,
				GameDetailsActivity.class).putExtra("id", gameMode.getGameId()
				+ ""));
	}

	@Override
	public void onLoad() {
		page++;
		requestHttp(HttpType.LOADING);
	}

	@Override
	public void onRefresh() {
		page = 1;
		requestHttp(HttpType.REFRESH);
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		getRefreshLayout().setRefreshing(false);
		getRefreshLayout().setLoading(false);
		if ("0".equals(resultItem.getString("status"))) {
			Message message = new Message();
			message.what = what;
			message.obj = resultItem;
			handler.sendMessage(message);
		} else {
			showToast(mContext, resultItem.getString("msg"));
		}
	}

	@Override
	public void onError(int what, String error) {
		getRefreshLayout().setRefreshing(false);
		getRefreshLayout().setLoading(false);
		showToast(mContext, error);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			ResultItem resultItem = ((ResultItem) msg.obj).getItem("data");
			switch (msg.what) {
			case HttpType.REFRESH:
				cache.put("gameClassify", resultItem);
				items.clear();
				items.addAll(getGameClassifys(resultItem.getItems("class")));
				classifyModels.clear();
				classifyModels.addAll(cinstructRecommendArray(resultItem
						.getItems("classData")));
				mAdapter.notifyDataSetChanged();
				expandableAdapter.notifyDataSetChanged();
				break;
			case HttpType.LOADING:
				classifyModels.addAll(cinstructRecommendArray(resultItem
						.getItems("classData")));
				expandableAdapter.notifyDataSetChanged();
				break;
			}
			for (int i = 0; i < expandableAdapter.getGroupCount(); i++) {
				getExpandableListView().expandGroup(i);
			}
		}
	};

}
