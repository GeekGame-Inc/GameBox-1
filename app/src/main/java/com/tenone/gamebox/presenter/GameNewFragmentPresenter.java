

package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.GameNewFragmentBiz;
import com.tenone.gamebox.mode.listener.GameNewFragmentListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.GameNewFragmentView;
import com.tenone.gamebox.view.activity.ClosedBetaTestSubscribeActivity;
import com.tenone.gamebox.view.activity.NewGameDetailsActivity;
import com.tenone.gamebox.view.adapter.JPGameAdapter;
import com.tenone.gamebox.view.adapter.OldBtGameAdapter;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("HandlerLeak")
public class GameNewFragmentPresenter extends BasePresenter implements
		OnRefreshListener, OnLoadListener,
		HttpResultListener, GameNewFragmentListener, AdapterView.OnItemClickListener {
	private GameNewFragmentView fragmentView;
	private GameNewFragmentBiz fragmentBiz;
	private Context mContext;
	private OldBtGameAdapter mAdapter;
	private int platform, page = 1;


	private List<GameModel> items = new ArrayList<GameModel>();
	private List<GameModel> ncModels = new ArrayList<GameModel>();
	private List<GameModel> yyModels = new ArrayList<GameModel>();

	private RelativeLayout ncLayout, yyLayout;
	private TextView ncTv, yyTv;
	private LinearLayout headerGaoupLayout;
	private RecyclerView ncRecyclerView, yyRecyclerView;
	private JPGameAdapter ncAdapter, yyAdapter;


	public GameNewFragmentPresenter(GameNewFragmentView view, Context cxt, int platform) {
		this.fragmentView = view;
		this.mContext = cxt;
		this.platform = platform;
		this.fragmentBiz = new GameNewFragmentBiz();
	}


	public void setAdapter() {
		if (mAdapter == null) {
			mAdapter = new OldBtGameAdapter( items, mContext, platform );
		}
		if (platform == 1) {
			getListView().addHeaderView( initHeaderView() );
		}
		getListView().setAdapter( mAdapter );
		getListView().setOnItemClickListener( this );
	}

	private View initHeaderView() {
		View view = LayoutInflater.from( mContext ).inflate( R.layout.layout_game_new_header, null, false );
		findView( view );
		initNC();
		initYY();
		return view;
	}

	private void initYY() {
		LinearLayoutManager manager2 = new LinearLayoutManager( mContext, LinearLayoutManager.HORIZONTAL, false );
		yyRecyclerView.setLayoutManager( manager2 );
		yyAdapter = new JPGameAdapter( yyModels, mContext );
		yyAdapter.setOnRecyclerViewItemClickListener( gameModel -> {
			openOtherActivity( mContext, new Intent( mContext, NewGameDetailsActivity.class )
					.putExtra( "id", String.valueOf( gameModel.getGameId() ) ) );
		} );
		yyRecyclerView.setAdapter( yyAdapter );
		yyTv.setOnClickListener( v -> {
			openOtherActivity( mContext, new Intent( mContext, ClosedBetaTestSubscribeActivity.class )
					.setAction( "yy" ) );
		} );
	}

	private void initNC() {
		LinearLayoutManager manager1 = new LinearLayoutManager( mContext, LinearLayoutManager.HORIZONTAL, false );
		ncRecyclerView.setLayoutManager( manager1 );
		ncAdapter = new JPGameAdapter( ncModels, mContext );
		ncAdapter.setOnRecyclerViewItemClickListener( gameModel ->
				openOtherActivity( mContext, new Intent( mContext, NewGameDetailsActivity.class )
						.putExtra( "id", String.valueOf( gameModel.getGameId() ) ) )
		);
		ncRecyclerView.setAdapter( ncAdapter );
		ncTv.setOnClickListener( v ->
				openOtherActivity( mContext, new Intent( mContext, ClosedBetaTestSubscribeActivity.class )
						.setAction( "nc" ) )
		);
	}

	private void findView(View view) {
		ncLayout = view.findViewById( R.id.id_game_new_more_ncLayout );
		yyLayout = view.findViewById( R.id.id_game_new_more_yyLayout );
		ncTv = view.findViewById( R.id.id_game_new_more_ncgame );
		yyTv = view.findViewById( R.id.id_game_new_more_yygame );
		ncRecyclerView = view.findViewById( R.id.id_game_new_nclist );
		yyRecyclerView = view.findViewById( R.id.id_game_new_yylist );
		headerGaoupLayout = view.findViewById( R.id.id_game_new_headerGameLayout );
	}

	public void initListener() {
		getRefreshLayout().setOnRefreshListener( this );
		getRefreshLayout().setOnLoadListener( this );
	}

	public void requestHttp(int what) {
		if (platform == 2) {
			HttpManager.getNewGame( mContext, what, this, page, platform );
		} else {
			HttpManager.newGetNewGame( mContext, what, this, page, platform );
		}
	}

	public ListView getListView() {
		return fragmentView.getRecommendListView();
	}

	public RefreshLayout getRefreshLayout() {
		return fragmentView.getRefreshLayout();
	}

	public void getGameRecommends(List<ResultItem> resultItem) {
		fragmentBiz.constructArray( resultItem, mContext, this );
	}

	@Override
	public void onLoad() {
		page++;
		requestHttp( HttpType.LOADING );
	}

	@Override
	public void onRefresh() {
		page = 1;
		requestHttp( HttpType.REFRESH );
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		getRefreshLayout().setRefreshing( false );
		getRefreshLayout().setLoading( false );
		if (0 == resultItem.getIntValue( "status" )) {
			this.what = what;
			if (platform == 1) {
				ResultItem data = resultItem.getItem( "data" );
				if (!BeanUtils.isEmpty( data )) {
					setBtNewGames( data );
				}
			} else {
				List<ResultItem> datas = resultItem.getItems( "data" );
				getGameRecommends( datas );
			}
		} else {
			showToast( mContext, resultItem.getString( "msg" ) );
		}
	}

	private void setBtNewGames(ResultItem data) {
		if (what == HttpType.REFRESH) {
			boolean isVisibility1 = true, isVisibility2 = true;
			List<ResultItem> closedBeta = data.getItems( "closedBeta" );
			if (!BeanUtils.isEmpty( closedBeta )) {
				setClosedBeta( closedBeta );
			} else {
				ncLayout.setVisibility( View.GONE );
				isVisibility1 = false;
			}

			List<ResultItem> reservation = data.getItems( "reservation" );
			if (!BeanUtils.isEmpty( reservation )) {
				setReservation( reservation );
			} else {
				yyLayout.setVisibility( View.GONE );
				isVisibility2 = false;
			}
			headerGaoupLayout.setVisibility( isVisibility1 || isVisibility2 ? View.VISIBLE : View.GONE );
		}
		List<ResultItem> list = data.getItems( "list" );
		if (!BeanUtils.isEmpty( list )) {
			getGameRecommends( list );
		}
	}

	private void setReservation(List<ResultItem> reservation) {
		yyModels.clear();
		for (ResultItem data : reservation) {
			GameModel model = new GameModel();
			model.setGameId( data.getIntValue( "id" ) );
			model.setName( data.getString( "gamename" ) );
			model.setImgUrl( MyApplication.getHttpUrl().getBaseUrl() + data.getString( "logo" ) );
			yyModels.add( model );
		}
		yyAdapter.notifyDataSetChanged();
	}

	private void setClosedBeta(List<ResultItem> closedBeta) {
		ncModels.clear();
		for (ResultItem data : closedBeta) {
			GameModel model = new GameModel();
			model.setGameId( data.getIntValue( "id" ) );
			model.setName( data.getString( "gamename" ) );
			model.setImgUrl( MyApplication.getHttpUrl().getBaseUrl() + data.getString( "logo" ) );
			ncModels.add( model );
		}
		ncAdapter.notifyDataSetChanged();
	}

	@Override
	public void onError(int what, String error) {
		getRefreshLayout().setRefreshing( false );
		getRefreshLayout().setLoading( false );
		showToast( mContext, error );
	}

	private int what;

	@Override
	public void updateUI(List<GameModel> gameModels) {
		if (HttpType.REFRESH == what) {
			items.clear();
		}
		items.addAll( gameModels );
		mAdapter.notifyDataSetChanged();
		if (items.size() > 1) {
			if (MyApplication.getTopGameId() == 0)
				MyApplication.setTopGameId( items.get( 1 ).getGameId() );
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		GameModel model = null;
		if (platform == 1) {
			if (position > 0) {
				model = items.get( position - 1 );
			}
		} else {
			model = items.get( position );
		}
		if (!BeanUtils.isEmpty( model )) {
			openOtherActivity( mContext, new Intent( mContext, NewGameDetailsActivity.class )
					.putExtra( "id", String.valueOf( model.getGameId() ) ) );
		}
	}
}
