package com.tenone.gamebox.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tenone.gamebox.mode.biz.GameClassifyRecyclerBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameClassify;
import com.tenone.gamebox.mode.mode.GameClassifyModel;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.GameClassifyRecyclerView;
import com.tenone.gamebox.view.activity.GameClassifyTab;
import com.tenone.gamebox.view.activity.NewGameDetailsActivity;
import com.tenone.gamebox.view.adapter.RecycleViewAdapter;
import com.tenone.gamebox.view.adapter.RecycleViewAdapter.OnGridItemClickListener;
import com.tenone.gamebox.view.adapter.RecycleViewAdapter.OnHeaderItemClickListener;
import com.tenone.gamebox.view.adapter.RecycleViewAdapter.OnMoreClickListener;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class GameClassifyRecyclerPresenter extends BasePresenter implements
		HttpResultListener, OnRefreshListener, OnHeaderItemClickListener,
		OnGridItemClickListener, OnMoreClickListener {
	private GameClassifyRecyclerBiz classifyRecyclerBiz;
	private GameClassifyRecyclerView classifyRecyclerView;
	private Context mContext;
	private RecycleViewAdapter recycleViewAdapter;
	private int page = 1;
	private List<GameClassify> items = new ArrayList<GameClassify>();
	private List<GameClassifyModel> classifyModels = new ArrayList<GameClassifyModel>();
	private int platform = 1;
	private String topGameName;

	public GameClassifyRecyclerPresenter(GameClassifyRecyclerView v,
																			 Context context, int platform, String topGameName) {
		this.classifyRecyclerView = v;
		this.platform = platform;
		this.classifyRecyclerBiz = new GameClassifyRecyclerBiz();
		this.mContext = context;
		this.topGameName = topGameName;
	}

	public void initView() {
		getRefreshLayout().setRefreshing( true );
		initRecyclerView();
	}

	public void initRecyclerView() {
		LinearLayoutManager manager = new LinearLayoutManager( mContext );
		manager.setAutoMeasureEnabled( true );
		getRecyclerView().setLayoutManager( manager );
	}

	public void setAdapter() {
		recycleViewAdapter = new RecycleViewAdapter( classifyModels, mContext, items, topGameName, platform );
		getRecyclerView().setAdapter( recycleViewAdapter );
	}

	public void initListener() {
		getRefreshLayout().setOnRefreshListener( this );
		recycleViewAdapter.setOnHeaderItemClickListener( this );
		recycleViewAdapter.setOnGridItemClickListener( this );
		recycleViewAdapter.setOnMoreClickListener( this );
	}


	public void requestHttp(int what) {
		String url = MyApplication.getHttpUrl().getGameClass();
		RequestBody requestBody = new FormBody.Builder()
				.add( "channel", MyApplication.getConfigModle().getChannelID() )
				.add( "page", page + "" )
				.add( "platform", platform + "" )
				.add( "system", "1" ).build();
		HttpUtils.postHttp( mContext, what, url, requestBody, this );
	}

	public List<GameClassify> getGameClassifys(List<ResultItem> resultItems) {
		return classifyRecyclerBiz.constructArray( resultItems );
	}

	public SwipeRefreshLayout getRefreshLayout() {
		return classifyRecyclerView.getRefreshLayout();
	}

	public List<GameClassifyModel> cinstructRecommendArray(
			List<ResultItem> resultItems) {
		return classifyRecyclerBiz.cinstructRecommendArray( resultItems );
	}

	public RecyclerView getRecyclerView() {
		return classifyRecyclerView.getRecyclerView();
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		getRefreshLayout().setRefreshing( false );
		if ("0".equals( resultItem.getString( "status" ) )) {
			ResultItem data = resultItem.getItem( "data" );
			if (data != null) {
				if (what == HttpType.REFRESH) {
					items.clear();
					classifyModels.clear();
				}
				items.addAll( getGameClassifys( data.getItems( "class" ) ) );
				classifyModels.addAll( cinstructRecommendArray( data.getItems( "classData" ) ) );
			}
			recycleViewAdapter.notifyDataSetChanged();
		} else {
			showToast( mContext, resultItem.getString( "msg" ) );
		}
	}

	@Override
	public void onError(int what, String error) {
		getRefreshLayout().setRefreshing( false );
		showToast( mContext, error );
	}

	@Override
	public void onRefresh() {
		page = 1;
		requestHttp( HttpType.REFRESH );
	}

	@Override
	public void onHeaderItemClick(GameClassify classify) {
		String prefix = platform == 1 ? AppStatisticsManager.BT_PREFIX :
				platform == 2 ? AppStatisticsManager.DISCOUNT_PREFIX :
						AppStatisticsManager.H5_PREFIX;
		String action = prefix + AppStatisticsManager.CLASSIFY_PREFIX + classify.getClassifyName();
		AppStatisticsManager.addStatistics( action );
		openOtherActivity( mContext, new Intent( mContext, GameClassifyTab.class )
				.putExtra( "id", classify.getClassifyId() + "" )
				.putExtra( "name", classify.getClassifyName() )
				.putExtra( "platform", platform ) );
	}

	@Override
	public void onGridItemClick(GameModel model) {
		openOtherActivity( mContext, new Intent( mContext, NewGameDetailsActivity.class )
				.putExtra( "id", model.getGameId() + "" ) );
	}

	@Override
	public void onMoreClick(GameClassifyModel classifyModel) {
		openOtherActivity( mContext, new Intent( mContext, GameClassifyTab.class )
				.putExtra( "id", classifyModel.getClassId() )
				.putExtra( "name", classifyModel.getClassName() )
				.putExtra( "platform", platform ) );
	}


}
