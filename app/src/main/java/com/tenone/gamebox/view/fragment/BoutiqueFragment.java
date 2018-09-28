package com.tenone.gamebox.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.john.superadapter.OnItemClickListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnLoadMoreListener;
import com.tenone.gamebox.mode.mode.BoutiqueGameModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.activity.NewGameDetailsActivity;
import com.tenone.gamebox.view.adapter.BoutiqueAdapter;
import com.tenone.gamebox.view.base.BaseLazyFragment;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.LoadMoreRecyclerView;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class BoutiqueFragment extends BaseLazyFragment implements HttpResultListener, OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
	@ViewInject(R.id.id_fragment_boutique_refresh)
	SwipeRefreshLayout refreshLayout;
	@ViewInject(R.id.id_fragment_boutique_list)
	LoadMoreRecyclerView recyclerView;
	@ViewInject(R.id.id_fragment_boutique_search)
	ImageView searchIv;

	private BoutiqueAdapter adapter;
	private List<BoutiqueGameModel> models = new ArrayList<>();
	private int page = 1;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate( R.layout.fragment_boutique, container, false );
		ViewUtils.inject( this, view );
		initView();
		return view;
	}

	private void initView() {
		adapter = new BoutiqueAdapter( getActivity(), models, R.layout.item_boutique );
		adapter.setOnItemClickListener( this );
		refreshLayout.setOnRefreshListener( this );
		recyclerView.setLayoutManager( new LinearLayoutManager( getActivity() ) );
		recyclerView.setAdapter( adapter );
		recyclerView.initLoadMore( this );
		recyclerView.setLoadMoreEnabled( true );
		searchIv.setOnClickListener( v->{} );
	}

	private void request(int what) {
		HttpManager.boutiqueGame( getActivity(), what, this, page );
	}

	private void setData(List<ResultItem> data) {
		for (ResultItem item : data) {
			BoutiqueGameModel model = new BoutiqueGameModel();
			model.setGameId( item.getIntValue( "id" ) );
			model.setGameIcon( MyApplication.getHttpUrl().getBaseUrl() + item.getString( "logo" ) );
			model.setGameIntro( item.getString( "finetopstr" ) );
			model.setGameName( item.getString( "gamename" ) );
			model.setGameType( item.getString( "types" ) );
			model.setGameSize( item.getString( "size" ) );
			model.setGameCommentCounts( item.getIntValue( "comment_count" ) );
			String ads = MyApplication.getHttpUrl().getBaseUrl() + item.getString( "spread_pic" );
			model.setGameAdsImg( ads );
			String[] array = new String[3];
			for (int i = 0; i < 3; i++) {
				array[i] = MyApplication.getHttpUrl().getBaseUrl() + item.getString( "pic" + (i + 1) );
			}
			model.setGameImgs( array );
			models.add( model );
		}
	}

	@Override
	public void onLazyLoad() {
		refreshLayout.setRefreshing( true );
		request( HttpType.REFRESH );
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		refreshLayout.setRefreshing( false );
		recyclerView.loadMoreComplete();
		if (0 == resultItem.getIntValue( "status" )) {
			if (what == HttpType.REFRESH) {
				models.clear();
			}
			List<ResultItem> data = resultItem.getItems( "data" );
			if (BeanUtils.isEmpty( data )) {
				recyclerView.setLoadMoreEnabled( false );
			} else {
				setData( data );
			}
			adapter.notifyDataSetChanged();
		} else {
			showToast( resultItem.getString( "msg" ) );
			recyclerView.setLoadMoreEnabled( false );
		}
	}


	@Override
	public void onError(int what, String error) {
		recyclerView.loadMoreComplete();
		recyclerView.setLoadMoreEnabled( false );
		ToastUtils.showToast( getActivity(), error );
	}

	@Override
	public void onItemClick(View itemView, int viewType, int position) {
		startActivity( new Intent( getActivity(), NewGameDetailsActivity.class )
				.putExtra( "id", models.get( position ).getGameId() + "" ) );
	}

	@Override
	public void onRefresh() {
		recyclerView.setLoadMoreEnabled( true );
		page = 1;
		request( HttpType.REFRESH );
	}

	@Override
	public void onLoadMore() {
		page += 1;
		request( HttpType.LOADING );
	}
}
