package com.tenone.gamebox.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sy.sdk.utls.ToastUtls;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnStrategyItemClickListener;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.StrategyMode;
import com.tenone.gamebox.share.SharePopupWindow;
import com.tenone.gamebox.view.activity.LoginActivity;
import com.tenone.gamebox.view.activity.WebActivity;
import com.tenone.gamebox.view.adapter.StrategyAdapter;
import com.tenone.gamebox.view.base.BaseLazyFragment;
import com.tenone.gamebox.view.custom.SpacesItemDecoration;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DataStateChangeCheck;
import com.tenone.gamebox.view.utils.HttpManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class GameDetailStrategyFragment extends BaseLazyFragment implements HttpResultListener, SwipeRefreshLayout.OnRefreshListener, OnStrategyItemClickListener, DataStateChangeCheck.LoadDataListener, StrategyAdapter.OperationListener, PlatformActionListener {
	View view;
	@ViewInject(R.id.id_strategy_refresh)
	SwipeRefreshLayout refreshLayout;
	@ViewInject(R.id.id_strategy_listView)
	RecyclerView listView;

	private StrategyAdapter adapter;
	List<StrategyMode> items = new ArrayList<StrategyMode>();
	private int page = 1;
	private String gameId;
	private DataStateChangeCheck dataStateChangeCheck;
	private SharePopupWindow sharePopupWindow;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate( R.layout.layout_strategy, container,
					false );
		}
		ViewUtils.inject( this, view );
		initView();
		Bundle bundle = getArguments();
		gameId = bundle.getString( "id" );
		page = 1;
		return view;
	}

	private void initView() {
		dataStateChangeCheck = new DataStateChangeCheck( listView );
		refreshLayout.setOnRefreshListener( this );
		adapter = new StrategyAdapter( items, getActivity() );
		adapter.setListener( this );
		adapter.setOnStrategyItemClickListener( this );
		LinearLayoutManager manager = new LinearLayoutManager( getActivity(), LinearLayoutManager.VERTICAL, false );
		listView.setLayoutManager( manager );
		listView.setAdapter( adapter );
		listView.addItemDecoration( new SpacesItemDecoration( getActivity(), LinearLayoutManager.VERTICAL,
				1, getResources().getColor( R.color.divider ) ) );
		listView.addOnScrollListener( dataStateChangeCheck );
		dataStateChangeCheck.setLoadDataListener( this );
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		refreshLayout.setRefreshing( false );
		if (0 == resultItem.getIntValue( "status" )) {
			ResultItem data = resultItem.getItem( "data" );
			if (!BeanUtils.isEmpty( data )) {
				List<ResultItem> resultItems = data.getItems( "list" );
				if (what == 0) {
					items.clear();
				}
				if (!BeanUtils.isEmpty( resultItems )) {
					setData( resultItems );
				}
			}
		}
	}

	@Override
	public void onError(int what, String error) {
		refreshLayout.setRefreshing( false );
	}

	@Override
	public void onRefresh() {
		page = 1;
		HttpManager.articleListByGame( 0, getActivity(), this, 1, page, gameId );
	}

	@Override
	public void onStrategtItemClick(StrategyMode mode) {
		startActivity( new Intent( getActivity(), WebActivity.class )
				.putExtra( "title", "\u653b\u7565\u8be6\u60c5" )
				.putExtra( "url", mode.getUrl() ) );
	}

	public void setData(List<ResultItem> data) {
		for (ResultItem item : data) {
			StrategyMode mode = new StrategyMode();
			mode.setArticleId( item.getString( "article_id" ) );
			mode.setGameName( item.getString( "gamename" ) );
			mode.setStrategyId( item.getIntValue( "tid" ) );
			mode.setStrategyName( item.getString( "title" ) );
			mode.setStrategyImgUrl( "http://www.185sy.com" + item.getString( "pic" ) );
			mode.setWriter( item.getString( "author" ) );
			mode.setTime( item.getString( "release_time" ) );
			mode.setUrl( item.getString( "info_url" ) );
			mode.setViewCounts( item.getIntValue( "view_counts" ) );
			mode.setLikes( item.getIntValue( "likes" ) );
			mode.setDisLikes( item.getIntValue( "dislikes" ) );
			mode.setShareCounts( item.getIntValue( "shares" ) );
			mode.setTop( item.getBooleanValue( "is_top", 1 ) );
			mode.setLikeType( item.getIntValue( "like_type" ) );
			items.add( mode );
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onLoadNextPage(View view) {
		page++;
		HttpManager.articleListByGame( 1, getActivity(), this, 1, page, gameId );
	}

	@Override
	public void onRefreshPage(View view) {

	}

	@Override
	public void onLazyLoad() {
		refreshLayout.setRefreshing( true );
		HttpManager.articleListByGame( 0, getActivity(), this, 1, page, gameId );
	}

	@Override
	public void onPraiseClick(StrategyMode model) {
		if (!BeanUtils.isLogin()) {
			startActivity( new Intent( getActivity(), LoginActivity.class ) );
			return;
		}
		if (model.getLikeType() != 1 && model.getLikeType() != 0) {
			HttpManager.articleLike( 12, getActivity(), new HttpResultListener() {
				@Override
				public void onSuccess(int what, ResultItem resultItem) {
					if (1 == resultItem.getIntValue( "status" )) {
						model.setLikes( model.getLikes() + 1 );
						model.setLikeType( 1 );
						adapter.notifyDataSetChanged();
					}
					ToastUtls.getInstance().showToast( getActivity(), resultItem.getString( "msg" ) );
				}

				@Override
				public void onError(int what, String error) {
				}
			}, model.getArticleId(), 1 );
		} else {
			ToastUtls.getInstance().showToast( getActivity(), "\u60a8\u5df2\u7ecf\u8d5e/\u8e29\u8fc7\u8be5\u653b\u7565" );
		}
	}

	@Override
	public void onStepOnClick(StrategyMode model) {
		if (model.getLikeType() != 1 && model.getLikeType() != 0) {
			HttpManager.articleLike( 12, getActivity(), new HttpResultListener() {
				@Override
				public void onSuccess(int what, ResultItem resultItem) {
					if (1 == resultItem.getIntValue( "status" )) {
						model.setDisLikes( model.getDisLikes() + 1 );
						model.setLikeType( 0 );
						adapter.notifyDataSetChanged();
					}
					ToastUtls.getInstance().showToast( getActivity(), resultItem.getString( "msg" ) );
				}

				@Override
				public void onError(int what, String error) {
				}
			}, model.getArticleId(), 0 );
		} else {
			ToastUtls.getInstance().showToast( getActivity(), "\u60a8\u5df2\u7ecf\u8d5e/\u8e29\u8fc7\u8be5\u653b\u7565" );
		}
	}

	private StrategyMode currentStrategyMode;

	@Override
	public void onShareClick(StrategyMode model) {
		currentStrategyMode = model;
		if (sharePopupWindow != null && sharePopupWindow.isShowing()) {
			sharePopupWindow.dismiss();
		}
		sharePopupWindow = null;
		sharePopupWindow = new SharePopupWindow( getActivity(), model );
		sharePopupWindow.setPlatformActionListener( this );
		sharePopupWindow.showAtLocation( refreshLayout, Gravity.BOTTOM, 0, 0 );
	}

	@Override
	public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
		HttpManager.shareArticle( 15, getActivity(), new HttpResultListener() {
			@Override
			public void onSuccess(int what, ResultItem resultItem) {
				if (1 == resultItem.getIntValue( "status" )) {
					currentStrategyMode.setShareCounts( currentStrategyMode.getShareCounts() + 1 );
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onError(int what, String error) {
				Log.i( "share_article", error );
			}
		}, currentStrategyMode.getArticleId() );
	}

	@Override
	public void onError(Platform platform, int i, Throwable throwable) {
		ToastCustom.makeText( getActivity(), "\u5206\u4eab\u51fa\u9519", ToastCustom.LENGTH_SHORT ).show();
	}

	@Override
	public void onCancel(Platform platform, int i) {
		ToastCustom.makeText( getActivity(), "\u5206\u4eab\u53d6\u6d88", ToastCustom.LENGTH_SHORT ).show();
	}
}
