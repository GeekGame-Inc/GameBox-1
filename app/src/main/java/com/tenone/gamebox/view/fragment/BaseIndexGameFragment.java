package com.tenone.gamebox.view.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.CacheDataThread;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.PrecisionAbsLisViewOnScrollListener;
import com.tenone.gamebox.mode.mode.BannerModel;
import com.tenone.gamebox.mode.mode.GameHeaderModel;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.JP_ZX_RM_Model;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.StatisticActionEnum;
import com.tenone.gamebox.presenter.AppStatisticsManager;
import com.tenone.gamebox.view.activity.GameClassifyRecyclerViewActivity;
import com.tenone.gamebox.view.activity.GameNewActivity;
import com.tenone.gamebox.view.activity.GameTopActivity;
import com.tenone.gamebox.view.activity.NewGameDetailsActivity;
import com.tenone.gamebox.view.activity.OpenServerActivity;
import com.tenone.gamebox.view.activity.StrategyListActivity;
import com.tenone.gamebox.view.activity.WebActivity;
import com.tenone.gamebox.view.adapter.BtGameAdapter;
import com.tenone.gamebox.view.adapter.JPGameAdapter;
import com.tenone.gamebox.view.base.BaseLazyFragment;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.MyListView;
import com.tenone.gamebox.view.custom.MyRefreshListView;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.xbanner.XBanner;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.OnScrollHelper;
import com.tenone.gamebox.view.utils.cache.ACache;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class BaseIndexGameFragment extends BaseLazyFragment implements HttpResultListener, XBanner.OnItemClickListener, XBanner.XBannerAdapter, RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
	protected static final String TAG = "BaseIndexGameFragment";

	RefreshLayout refreshLayout;
	MyRefreshListView recyclerView;
	ImageView searchIv;
	protected LinearLayout jpLayout, zxLayout, rmLayout;
	protected List<GameModel> items = new ArrayList<GameModel>();
	protected List<GameModel> zxItems = new ArrayList<GameModel>();
	protected List<GameModel> rmItems = new ArrayList<GameModel>();
	protected List<GameModel> jpItems = new ArrayList<GameModel>();
	protected GameHeaderModel headerModel;

	protected List<BannerModel> bannerArray = new ArrayList<BannerModel>();
	protected BtGameAdapter adapter, zxAdapter, rmAdapter;
	protected JPGameAdapter jpAdapter;
	protected XBanner xBanner;
	protected MyListView rmListView, zxListView;
	protected ImageView jpggImg, rmggImg, zxggImg;
	protected RecyclerView jpListView;
	protected int page = 1;
	protected ACache cache;
	protected RelativeLayout searchLayout;
	protected String topGameName;
	protected TextView searchTextView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate( R.layout.fragment_bt_game, container, false );
		refreshLayout = view.findViewById( R.id.id_fragment_bt_dis_refresh );
		recyclerView = view.findViewById( R.id.id_fragment_bt_dis_listview );
		searchIv = view.findViewById( R.id.id_fragment_bt_dis_search );
		cache = ACache.get( getActivity() );
		initView();
		return view;
	}

	@Override
	public void onLazyLoad() {
		refreshLayout.setRefreshing( true );
	}

	protected void onLazyLoad(String key, int platform) {
		getCache( key );
		HttpManager.newGameIndex( getActivity(), HttpType.REFRESH, this, page, platform );
	}

	@Override
	public void onLoad() {
		page += 1;
	}

	@Override
	public void onRefresh() {
		page = 1;
	}

	protected void onLoad(int platform) {
		HttpManager.newGameIndex( getActivity(), HttpType.LOADING, this, page, platform );
	}

	protected void onRefresh(int platform) {
		HttpManager.newGameIndex( getActivity(), HttpType.REFRESH, this, page, platform );
	}

	@Override
	public void onItemClick(XBanner banner, int position) {

		if (bannerArray.get( position ).getType() == 1) {
			startActivity( new Intent( getActivity(), NewGameDetailsActivity.class )
					.putExtra( "id", String.valueOf( bannerArray.get( position ).getGameId() ) ) );
		} else {
			String url = bannerArray.get( position ).getUrl();
			String title = (bannerArray.get( position ).getType() == 2) ? getString( R.string.detail_of_the_strategy )
					: getString( R.string.detail_of_the_activity );
			startActivity( new Intent( getActivity(), WebActivity.class )
					.putExtra( "url", url ).putExtra( "title", title ) );
		}
	}

	protected void onItemClick(int platform, int position) {
		String prefix = 1 == platform ? AppStatisticsManager.BT_PREFIX :
				2 == platform ? AppStatisticsManager.DISCOUNT_PREFIX :
						AppStatisticsManager.H5_PREFIX;
		String action = prefix + AppStatisticsManager.ADVERTISING_PREFIX + position;
		AppStatisticsManager.addStatistics( action );
	}

	@Override
	public void loadBanner(XBanner banner, Object model, View view, int position) {
		BannerModel bannerModel = (BannerModel) model;
		String str = bannerModel.getImageUrl();
		ImageView imageView = (ImageView) view;
		imageView.setScaleType( ImageView.ScaleType.FIT_XY );
		ImageLoadUtils.loadNormalImg( imageView, getActivity(), str );
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		refreshLayout.setRefreshing( false );
		refreshLayout.setLoading( false );
	}

	protected void onSuccess(int what, ResultItem resultItem, String key) {
		if (0 == resultItem.getIntValue( "status" )) {
			ResultItem data = resultItem.getItem( "data" );
			if (!BeanUtils.isEmpty( data )) {
				if (what == HttpType.REFRESH) {
					new CacheDataThread( getActivity(), data, key ).start();
					items.clear();
					if (headerModel != null) {
						headerModel.clear();
					}
					List<ResultItem> banner = data.getItems( "banner" );
					ResultItem finetop = data.getItem( "finetop" );
					ResultItem newtop = data.getItem( "newtop" );
					ResultItem weektop = data.getItem( "weektop" );
					setHeaderData( banner, finetop, newtop, weektop );
				}
				List<ResultItem> gameList = data.getItems( "gamelist" );
				if (!BeanUtils.isEmpty( gameList )) {
					setGameListData( gameList );
				}
				topGameName = data.getString( "topgame" );
				searchTextView.setText( topGameName );
			}
		}
	}

	@Override
	public void onError(int what, String error) {
		refreshLayout.setRefreshing( false );
		refreshLayout.setLoading( false );
		showToast( error );
	}

	protected void initView() {
		refreshLayout.setOnLoadListener( this );
		refreshLayout.setOnRefreshListener( this );
	}

	protected void initView(int platform) {
		adapter = new BtGameAdapter( items, getActivity(), platform );
		recyclerView.setAdapter( adapter );
		recyclerView.addHeaderView( initHeaderView( platform ) );
		searchIv.setOnClickListener( v -> startActivity( new Intent( getActivity(), GameClassifyRecyclerViewActivity.class )
				.putExtra( "platform", platform )
				.putExtra( "topGame", topGameName ) ) );
		recyclerView.setOnItemClickListener( (parent, view, position, id) -> {
			if (position > 0) {
				GameModel model = items.get( position - 1 );
				addGameStatistics( platform, 1, model );
				startActivity( new Intent( getActivity(), NewGameDetailsActivity.class )
						.putExtra( "id", String.valueOf( model.getGameId() ) ) );
			}
		} );
		refreshLayout.setOnScrollListener( new PrecisionAbsLisViewOnScrollListener( getActivity(), searchLayout.getMeasuredHeight() ) {
			@Override
			public void onVisibility(int visibility) {
				searchIv.setVisibility( visibility );
			}
		} );
		OnScrollHelper.getInstance().onScrollStateUpdate( refreshLayout);
	}

	private View initHeaderView(int platform) {
		View view = LayoutInflater.from( getActivity() ).inflate(
				R.layout.layout_new_game_header, null );
		jpLayout = (LinearLayout) findView( view, R.id.id_game_header_jp );
		zxLayout = (LinearLayout) findView( view, R.id.id_game_header_zx );
		rmLayout = (LinearLayout) findView( view, R.id.id_game_header_rm );
		searchLayout = (RelativeLayout) findView( view, R.id.id_new_search_layout );
		searchTextView = (TextView) findView( view, R.id.id_new_search_text );
		findView( view, R.id.id_game_header_tv1 ).setOnClickListener( v -> {
			addButtonStatistics( platform, 0 );
			startActivity( new Intent( getActivity(), GameNewActivity.class )
					.putExtra( "platform", platform ) );
		} );
		findView( view, R.id.id_game_header_tv2 ).setOnClickListener( v -> {
			addButtonStatistics( platform, 1 );
			startActivity( new Intent( getActivity(), GameTopActivity.class )
					.putExtra( "platform", platform ) );
		} );
		findView( view, R.id.id_game_header_tv3 ).setSelected( true );
		TextView textView = (TextView) findView( view, R.id.id_game_header_tv3 );
		Drawable drawable = ContextCompat.getDrawable( getActivity(),
				platform == 1 ? R.drawable.icon_manv : platform == 2 ?
						R.drawable.icon_cdzk : R.drawable.icon_zuanjinbi );
		drawable.setBounds( 0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight() );
		textView.setCompoundDrawables( null, drawable, null, null );
		textView.setText( platform == 1 ? "\u9001" + getString( R.string.fullV ) : platform == 2 ? getString( R.string.ultralow_discount ) : getString( R.string.make_coin ) );
		textView.setOnClickListener( v -> {
			addButtonStatistics( platform, 2 );
			startActivity( new Intent( getActivity(), StrategyListActivity.class )
					.putExtra( "platform", platform ) );
		} );
		findView( view, R.id.id_game_header_tv4 ).setOnClickListener( v -> {
			addButtonStatistics( platform, 3 );
			startActivity( new Intent( getActivity(), OpenServerActivity.class )
					.putExtra( "platform", platform ) );
		} );
		searchLayout.setOnClickListener( v -> startActivity( new Intent( getActivity(), GameClassifyRecyclerViewActivity.class )
				.putExtra( "platform", platform )
				.putExtra( "topGame", topGameName ) ) );
		findView( view, R.id.id_game_header_classify ).setOnClickListener( v -> {
			addButtonStatistics( platform, 4 );
			startActivity( new Intent( getActivity(), GameClassifyRecyclerViewActivity.class )
					.putExtra( "platform", platform )
					.putExtra( "topGame", topGameName ) );
		} );
		initBanner( view );
		initJP( view );
		initZX( view );
		initRM( view );
		return view;
	}

	protected void initRM(View view) {
		rmListView = (MyListView) findView( view, R.id.id_game_header_rmListView );
		rmggImg = (ImageView) findView( view, R.id.id_game_header_rmggImg );
		ViewGroup.LayoutParams params = rmggImg.getLayoutParams();
		params.width = DisplayMetricsUtils.getScreenWidth( getActivity() ) - DisplayMetricsUtils.dipTopx( getActivity(), 8 );
		params.height = DisplayMetricsUtils.dipTopx( getActivity(), 125 );
		rmggImg.setLayoutParams( params );
	}

	protected void initRM(int platform) {
		rmAdapter = new BtGameAdapter( rmItems, getActivity(), platform );
		rmListView.setAdapter( rmAdapter );
		rmListView.setOnItemClickListener( (parent, view1, position, id) -> {
			GameModel model = rmItems.get( position );
			addGameStatistics( platform, 1, model );
			startActivity( new Intent( getActivity(), NewGameDetailsActivity.class )
					.putExtra( "id", String.valueOf( model.getGameId() ) ) );
		} );
	}

	protected void initZX(View view) {
		zxListView = (MyListView) findView( view, R.id.id_game_header_zxListView );
		zxggImg = (ImageView) findView( view, R.id.id_game_header_zxggImg );
		ViewGroup.LayoutParams params = zxggImg.getLayoutParams();
		params.width = DisplayMetricsUtils.getScreenWidth( getActivity() ) - DisplayMetricsUtils.dipTopx( getActivity(), 8 );
		params.height = DisplayMetricsUtils.dipTopx( getActivity(), 125 );
		zxggImg.setLayoutParams( params );
	}

	protected void initZX(int platform) {
		zxAdapter = new BtGameAdapter( zxItems, getActivity(), platform );
		zxListView.setAdapter( zxAdapter );
		zxListView.setOnItemClickListener( (parent, view1, position, id) -> {
			GameModel model = zxItems.get( position );
			addGameStatistics( platform, 1, model );
			startActivity( new Intent( getActivity(), NewGameDetailsActivity.class )
					.putExtra( "id", String.valueOf( model.getGameId() ) ) );
		} );
	}

	protected void initJP(View view) {
		jpListView = (RecyclerView) findView( view, R.id.id_game_header_jpListView );
		jpggImg = (ImageView) findView( view, R.id.id_game_header_jpggImg );
		LinearLayoutManager layoutManager = new LinearLayoutManager( getActivity(), LinearLayoutManager.HORIZONTAL,
				false );
		jpAdapter = new JPGameAdapter( jpItems, getActivity() );
		jpListView.setLayoutManager( layoutManager );
		jpListView.setAdapter( jpAdapter );
		ViewGroup.LayoutParams params = jpggImg.getLayoutParams();
		params.width = DisplayMetricsUtils.getScreenWidth( getActivity() ) - DisplayMetricsUtils.dipTopx( getActivity(), 8 );
		params.height = DisplayMetricsUtils.dipTopx( getActivity(), 125 );
		jpggImg.setLayoutParams( params );
	}

	protected void initJP(int platform) {
		jpAdapter.setOnRecyclerViewItemClickListener( gameMode -> {
			addGameStatistics( platform, 0, gameMode );
			startActivity( new Intent( getActivity(), NewGameDetailsActivity.class )
					.putExtra( "id", String.valueOf( gameMode.getGameId() ) ) );
		} );
	}

	private void initBanner(View view) {
		xBanner = (XBanner) findView( view, R.id.id_banner );
		ViewGroup.LayoutParams params = xBanner.getLayoutParams();
		params.width = DisplayMetricsUtils.getScreenWidth( getActivity() ) - DisplayMetricsUtils.dipTopx( getActivity(), 8 );
		params.height = params.width * 273 / 734;
		xBanner.setLayoutParams( params );
		xBanner.setOnItemClickListener( this );
	}

	protected void setBannerData() {
		if (xBanner != null) {
			xBanner.setData( bannerArray, null );
			xBanner.setmAdapter( this );
		}
	}

	protected void getCache(String key) {
		ResultItem data = (ResultItem) cache.getAsObject( key );
		if (!BeanUtils.isEmpty( data )) {
			items.clear();
			if (headerModel != null) {
				headerModel.clear();
			}
			List<ResultItem> banner = data.getItems( "banner" );
			ResultItem finetop = data.getItem( "finetop" );
			ResultItem newtop = data.getItem( "newtop" );
			ResultItem weektop = data.getItem( "weektop" );
			setHeaderData( banner, finetop, newtop, weektop );
			List<ResultItem> gameList = data.getItems( "gamelist" );
			if (!BeanUtils.isEmpty( gameList )) {
				setGameListData( gameList );
			}
			topGameName = data.getString( "topgame" );
			searchTextView.setText( topGameName );
		}
	}

	private void setGameListData(List<ResultItem> resultItems) {
		for (ResultItem item : resultItems) {
			GameModel model = new GameModel();
			model.setGameId( item.getIntValue( "id" ) );
			model.setImgUrl( MyApplication.getHttpUrl().getBaseUrl() + item.getString( "logo" ) );
			model.setTimes( item.getString( "download" ) );
			model.setName( item.getString( "gamename" ) );
			model.setSize( item.getString( "size" ) );
			model.setVersionsCode( item.getIntValue( "version" ) );
			model.setUrl( item.getString( "android_url" ) );
			model.setPackgeName( item.getString( "android_pack" ) );
			model.setContent( item.getString( "content" ) );
			model.setDis( item.getString( "discount" ) );
			model.setGameType( item.getString( "types" ) );
			model.setOperate( item.getIntValue( "operate" ) );
			String label = item.getString( "label" );
			if (!TextUtils.isEmpty( label )) {
				String[] array = label.split( "," );
				model.setLabelArray( array );
			}
			items.add( model );
		}
		adapter.notifyDataSetChanged();
	}

	private void setHeaderData(List<ResultItem> banner, ResultItem finetop, ResultItem newtop, ResultItem weektop) {
		headerModel = new GameHeaderModel();
		if (!BeanUtils.isEmpty( banner )) {
			xBanner.setVisibility( View.VISIBLE );
			setBannerData( banner );
		} else {
			xBanner.setVisibility( View.GONE );
		}
		if (!BeanUtils.isEmpty( finetop ) && !BeanUtils.isEmpty( finetop.getItems( "game" ) )) {
			jpLayout.setVisibility( View.VISIBLE );
			setJPData( finetop );
		} else {
			jpLayout.setVisibility( View.GONE );
		}
		if (!BeanUtils.isEmpty( newtop ) && !BeanUtils.isEmpty( newtop.getItems( "game" ) )) {
			zxLayout.setVisibility( View.VISIBLE );
			setZXData( newtop );
		} else {
			zxLayout.setVisibility( View.GONE );
		}
		if (!BeanUtils.isEmpty( weektop ) && !BeanUtils.isEmpty( weektop.getItems( "game" ) )) {
			rmLayout.setVisibility( View.VISIBLE );
			setRMData( weektop );
		} else {
			rmLayout.setVisibility( View.GONE );
		}
		adapter.notifyDataSetChanged();
	}

	private void setRMData(ResultItem weektop) {
		if (!BeanUtils.isEmpty( weektop )) {
			List<ResultItem> wt = weektop.getItems( "game" );
			if (!BeanUtils.isEmpty( wt )) {
				for (ResultItem item : wt) {
					GameModel model = new GameModel();
					model.setGameId( item.getIntValue( "id" ) );
					model.setImgUrl( MyApplication.getHttpUrl().getBaseUrl() + item.getString( "logo" ) );
					model.setName( item.getString( "gamename" ) );
					model.setContent( item.getString( "content" ) );
					model.setSize( item.getString( "size" ) );
					String label = item.getString( "label" );
					model.setToday( item.getBooleanValue( "first", 1 ) );
					model.setGameType( item.getString( "types" ) );
					model.setOperate( item.getIntValue( "operate" ) );
					if (!TextUtils.isEmpty( label )) {
						String[] array = label.split( "," );
						model.setLabelArray( array );
					}
					rmItems.add( model );
				}
			}
			ResultItem resultItem = weektop.getItem( "slide" );
			BannerModel bannerModel = new BannerModel();
			if (!BeanUtils.isEmpty( resultItem )) {
				bannerModel.setImageUrl( MyApplication.getHttpUrl().getBaseUrl() + resultItem.getString( "pic" ) );
				bannerModel.setUrl( resultItem.getString( "url" ) );
				bannerModel.setGameId( resultItem.getIntValue( "gid" ) );
				ImageLoadUtils.loadNormalImg( rmggImg, getActivity(), bannerModel.getImageUrl() );
				rmggImg.setOnClickListener( v -> {
					String url = bannerModel.getUrl();
					int gameId = bannerModel.getGameId();
					if (gameId > 0) {
						startActivity( new Intent( getActivity(), NewGameDetailsActivity.class )
								.putExtra( "id", String.valueOf( gameId ) ) );
					} else if (!TextUtils.isEmpty( url )) {
						String title = getString( R.string.detail_of_the_activity );
						startActivity( new Intent( getActivity(), WebActivity.class )
								.putExtra( "url", url ).putExtra( "title", title ) );
					}
				} );
			} else {
				rmggImg.setVisibility( View.GONE );
			}
			JP_ZX_RM_Model model = new JP_ZX_RM_Model();
			model.setGgModel( bannerModel );
			model.setJpModels( rmItems );
			headerModel.setRmModel( model );
			rmAdapter.notifyDataSetChanged();
		}
	}

	private void setZXData(ResultItem newtop) {
		if (!BeanUtils.isEmpty( newtop )) {
			List<ResultItem> nt = newtop.getItems( "game" );
			if (!BeanUtils.isEmpty( nt )) {
				for (ResultItem item : nt) {
					GameModel model = new GameModel();
					model.setGameId( item.getIntValue( "id" ) );
					model.setImgUrl( MyApplication.getHttpUrl().getBaseUrl() + item.getString( "logo" ) );
					model.setName( item.getString( "gamename" ) );
					model.setContent( item.getString( "content" ) );
					model.setSize( item.getString( "size" ) );
					model.setToday( item.getBooleanValue( "first", 1 ) );
					model.setGameType( item.getString( "types" ) );
					model.setOperate( item.getIntValue( "operate" ) );
					String label = item.getString( "label" );
					if (!TextUtils.isEmpty( label )) {
						String[] array = label.split( "," );
						model.setLabelArray( array );
					}
					zxItems.add( model );
				}
			}
			if (!BeanUtils.isEmpty( zxItems )) {
				GameModel model = zxItems.get( 0 );
				MyApplication.setDefultGameId( model.getGameId() );
			}
			ResultItem resultItem = newtop.getItem( "slide" );
			BannerModel bannerModel = new BannerModel();
			if (!BeanUtils.isEmpty( resultItem )) {
				bannerModel.setImageUrl( MyApplication.getHttpUrl().getBaseUrl() + resultItem.getString( "pic" ) );
				bannerModel.setGameId( resultItem.getIntValue( "gid" ) );
				bannerModel.setUrl( resultItem.getString( "url" ) );
				ImageLoadUtils.loadNormalImg( zxggImg, getActivity(), bannerModel.getImageUrl() );
				zxggImg.setOnClickListener( v -> {
					String url = bannerModel.getUrl();
					int gameId = bannerModel.getGameId();
					if (gameId > 0) {
						startActivity( new Intent( getActivity(), NewGameDetailsActivity.class )
								.putExtra( "id", String.valueOf( gameId ) ) );
					} else if (!TextUtils.isEmpty( url )) {
						String title = getString( R.string.detail_of_the_activity );
						startActivity( new Intent( getActivity(), WebActivity.class )
								.putExtra( "url", url ).putExtra( "title", title ) );
					}
				} );
			} else {
				zxggImg.setVisibility( View.GONE );
			}
			JP_ZX_RM_Model model = new JP_ZX_RM_Model();
			model.setGgModel( bannerModel );
			model.setJpModels( zxItems );
			headerModel.setZxModel( model );
			zxAdapter.notifyDataSetChanged();
		}
	}

	private void setBannerData(List<ResultItem> banner) {
		if (!BeanUtils.isEmpty( banner )) {
			for (ResultItem item : banner) {
				if (!BeanUtils.isEmpty( item )) {
					BannerModel model = new BannerModel();
					model.setGameId( item.getIntValue( "gid" ) );
					model.setType( item.getIntValue( "type" ) );
					model.setImageUrl( MyApplication.getHttpUrl().getBaseUrl() + item.getString( "slide_pic" ) );
					model.setUrl( item.getString( "url" ) );
					bannerArray.add( model );
				}
			}
			headerModel.setBannerModels( bannerArray );
			setBannerData();
		}
	}

	private void setJPData(ResultItem finetop) {
		if (!BeanUtils.isEmpty( finetop )) {
			List<ResultItem> fines = finetop.getItems( "game" );
			if (!BeanUtils.isEmpty( fines )) {
				for (ResultItem item : fines) {
					GameModel model = new GameModel();
					model.setGameId( item.getIntValue( "id" ) );
					model.setImgUrl( MyApplication.getHttpUrl().getBaseUrl() + item.getString( "logo" ) );
					model.setName( item.getString( "gamename" ) );
					model.setContent( item.getString( "finetopstr" ) );
					model.setGameType( item.getString( "types" ) );
					jpItems.add( model );
				}
			}
			ResultItem resultItem = finetop.getItem( "slide" );
			BannerModel bannerModel = new BannerModel();
			if (!BeanUtils.isEmpty( resultItem )) {
				bannerModel.setImageUrl( MyApplication.getHttpUrl().getBaseUrl() + resultItem.getString( "pic" ) );
				bannerModel.setGameId( resultItem.getIntValue( "gid" ) );
				bannerModel.setUrl( resultItem.getString( "url" ) );
				ImageLoadUtils.loadNormalImg( jpggImg, getActivity(), bannerModel.getImageUrl() );
				jpggImg.setOnClickListener( v -> {
					String url = bannerModel.getUrl();
					int gameId = bannerModel.getGameId();
					if (gameId > 0) {
						startActivity( new Intent( getActivity(), NewGameDetailsActivity.class )
								.putExtra( "id", String.valueOf( gameId ) ) );
					} else if (!TextUtils.isEmpty( url )) {
						String title = getString( R.string.detail_of_the_activity );
						startActivity( new Intent( getActivity(), WebActivity.class )
								.putExtra( "url", url ).putExtra( "title", title ) );
					}
				} );
			} else {
				jpggImg.setVisibility( View.GONE );
			}
			JP_ZX_RM_Model model = new JP_ZX_RM_Model();
			model.setGgModel( bannerModel );
			model.setJpModels( jpItems );
			headerModel.setJpModel( model );
			jpAdapter.notifyDataSetChanged();
		}
	}


	private void addButtonStatistics(int platfrom, int type) {
		switch (type) {
			case 0:
				AppStatisticsManager.addStatistics( 1 == platfrom ? StatisticActionEnum.BT_NEW_GAME
						: 2 == platfrom ? StatisticActionEnum.DISCOUNT_NEW_GAME
						: StatisticActionEnum.H5_NEW_GAME );
				break;
			case 1:
				AppStatisticsManager.addStatistics( 1 == platfrom ? StatisticActionEnum.BT_GAME_TOP
						: 2 == platfrom ? StatisticActionEnum.DISCOUNT_GAME_TOP
						: StatisticActionEnum.H5_GAME_TOP );
				break;
			case 2:
				AppStatisticsManager.addStatistics( 1 == platfrom ? StatisticActionEnum.BT_GIVE_VIP
						: 2 == platfrom ? StatisticActionEnum.DISCOUNT_ULTRA_LOW
						: StatisticActionEnum.H5_EARN_COINS );
				break;
			case 3:
				AppStatisticsManager.addStatistics( 1 == platfrom ? StatisticActionEnum.BT_OPEN_SERVER
						: 2 == platfrom ? StatisticActionEnum.DISCOUNT_OPEN_SERVER
						: StatisticActionEnum.H5_OPEN_SERVER );
				break;
			case 4:
				AppStatisticsManager.addStatistics( 1 == platfrom ? StatisticActionEnum.BT_GAME_CLASSIFY
						: 2 == platfrom ? StatisticActionEnum.DISCOUNT_GAME_CLASSIFY
						: StatisticActionEnum.H5_GAME_CLASSIFY );
				break;
		}
	}


	private void addGameStatistics(int platform, int type, GameModel gameMode) {
		String prefix = platform == 1 ? AppStatisticsManager.BT_PREFIX :
				2 == platform ? AppStatisticsManager.DISCOUNT_PREFIX : AppStatisticsManager.H5_PREFIX;
		String action = prefix + (0 == type ? AppStatisticsManager.BOUTIQUE_PREFIX : AppStatisticsManager.OTHER_PREFIX) + gameMode.getName();
		AppStatisticsManager.addStatistics( action );
	}

}
