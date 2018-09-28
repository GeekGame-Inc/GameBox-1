package com.tenone.gamebox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.adapter.GameTopAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.MyRefreshListView;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class GameTopActivity extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, HttpResultListener {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_game_top_refresh)
	RefreshLayout refreshLayout;
	@ViewInject(R.id.id_game_top_listview)
	MyRefreshListView listView;

	private List<GameModel> models = new ArrayList<GameModel>();
	private GameTopAdapter adapter;
	private Context context;
	private int platform = 1, page = 1;
	private ImageView secondIv, firstIv, thirdIv;
	private TextView secondTv, firstTv, thirdTv;
	private RelativeLayout secondLayout, firstLayout, thirdLayout;
	private LinearLayout headerRoot;
	private GameModel firstGameModel, secondGameModel, thirdGameModel;
	private View headerView;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		setContentView( R.layout.activity_game_top );
		ViewUtils.inject( this );
		Intent intent = getIntent();
		platform = intent.getIntExtra( "platform", 1 );
		context = this;
		initTitle();
		initView();
	}

	private void initTitle() {
		titleBarView.setTitleText( getString( R.string.game_top) );
		titleBarView.setLeftImg( R.drawable.icon_back_grey );
		titleBarView.setOnClickListener( R.id.id_titleBar_leftImg, v -> finish() );
	}

	private void initView() {
		refreshLayout.setOnLoadListener( this );
		refreshLayout.setOnRefreshListener( this );
		adapter = new GameTopAdapter( models, context, platform );
		listView.setAdapter( adapter );
		listView.setOnItemClickListener( this );
		headerView = initHeader();
		HttpManager.getNewGame( context, HttpType.REFRESH, this, page, platform, 3 );
	}

	private View initHeader() {
		View view = LayoutInflater.from( context ).inflate( R.layout.layout_new_top_header, null, false );
		secondIv = view.findViewById( R.id.id_new_top_second_gameIcon );
		firstIv = view.findViewById( R.id.id_new_top_first_gameIcon );
		thirdIv = view.findViewById( R.id.id_new_top_third_gameIcon );
		secondTv = view.findViewById( R.id.id_new_top_second_gameName );
		firstTv = view.findViewById( R.id.id_new_top_first_gameName );
		thirdTv = view.findViewById( R.id.id_new_top_third_gameName );
		secondLayout = view.findViewById( R.id.id_new_top_second );
		firstLayout = view.findViewById( R.id.id_new_top_first );
		thirdLayout = view.findViewById( R.id.id_new_top_third );
		headerRoot = view.findViewById( R.id.id_new_top_root );
		switch (platform) {
			case 1:
				headerRoot.setBackground( ContextCompat.getDrawable( context, R.drawable.h_icon_beijing ) );
				break;
			case 2:
				headerRoot.setBackground( ContextCompat.getDrawable( context, R.drawable.i_icon_beijing ) );
				break;
			case 3:
				headerRoot.setBackground( ContextCompat.getDrawable( context, R.drawable.g_icon_beijing ) );
				break;
		}
		firstLayout.setOnClickListener( v -> startActivity( new Intent( context, NewGameDetailsActivity.class )
				.putExtra( "id", String.valueOf( firstGameModel.getGameId() ) ) ) );
		secondLayout.setOnClickListener( v -> startActivity( new Intent( context, NewGameDetailsActivity.class )
				.putExtra( "id", String.valueOf( secondGameModel.getGameId() ) ) ) );
		thirdLayout.setOnClickListener( v -> startActivity( new Intent( context, NewGameDetailsActivity.class )
				.putExtra( "id", String.valueOf( thirdGameModel.getGameId() ) ) ) );
		return view;
	}

	@Override
	public void onLoad() {
		page += 1;
		HttpManager.getNewGame( context, HttpType.LOADING, this, page, platform, 3 );
	}

	@Override
	public void onRefresh() {
		page = 1;
		HttpManager.getNewGame( context, HttpType.REFRESH, this, page, platform, 3 );
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (models.size() > 2) {
			if (position > 0) {
				startActivity( new Intent( context, NewGameDetailsActivity.class )
						.putExtra( "id", String.valueOf( models.get( position - 1 ).getGameId() ) ) );
			}
		} else if (!models.isEmpty()) {
			startActivity( new Intent( context, NewGameDetailsActivity.class )
					.putExtra( "id", String.valueOf( models.get( position ).getGameId())));
		}
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		refreshLayout.setRefreshing( false );
		refreshLayout.setLoading( false );
		if (0 == resultItem.getIntValue( "status" )) {
			List<ResultItem> data = resultItem.getItems( "data" );
			if (!BeanUtils.isEmpty( data )) {
				setData( what, data );
			}
			adapter.notifyDataSetChanged();
		} else {
			showToast( resultItem.getString( "msg" ) );
		}
	}

	private void setData(int what, List<ResultItem> data) {
		int start = 0;
		if (what == HttpType.REFRESH) {
			models.clear();
			if (data.size() > 2) {
				if (listView.getHeaderViewsCount() == 0) {
					listView.addHeaderView( headerView );
				}
				firstGameModel = buildGameModel( data.get( 0 ) );
				secondGameModel = buildGameModel( data.get( 1 ) );
				thirdGameModel = buildGameModel( data.get( 2 ) );
				start = 3;
				setHeaderData();
				adapter.setTop3( true );
			} else {
				adapter.setTop3( false );
			}
		}
		buildList( start, data );
	}

	private void setHeaderData() {
		firstTv.setText( firstGameModel.getName() );
		ImageLoadUtils.loadNormalImg( firstIv, context, firstGameModel.getImgUrl() );
		secondTv.setText( secondGameModel.getName() );
		ImageLoadUtils.loadNormalImg( secondIv, context, secondGameModel.getImgUrl() );
		thirdTv.setText( thirdGameModel.getName() );
		ImageLoadUtils.loadNormalImg( thirdIv, context, thirdGameModel.getImgUrl() );
	}

	private void buildList(int start, List<ResultItem> data) {
		for (int i = start; i < data.size(); i++) {
			ResultItem item = data.get( i );
			models.add( buildGameModel( item ) );
		}
	}

	private GameModel buildGameModel(ResultItem item) {
		GameModel model = new GameModel();
		model.setGameTag( item.getString( "tag" ) );
		model.setImgUrl( MyApplication.getHttpUrl().getBaseUrl()
				+ item.getString( "logo" ) );
		model.setName( item.getString( "gamename" ) );
		model.setGameId( item.getIntValue( "id" ) );
		model.setDis( item.getString( "discount" ) );
		model.setSize( item.getString( "size" ) );
		model.setUrl( item.getString( "android_url" ) );
		model.setOperate( item.getIntValue( "operate" ) );
		model.setVersionsName( item.getString( "version" ) );
		String down = item.getString( "download" );
		int download = 0;
		if (!TextUtils.isEmpty( down )) {
			download = Integer.valueOf( down ).intValue();
		}
		if (download > 10000) {
			download = download / 10000;
			model.setTimes( download + "\u4e07+" );
		} else {
			model.setTimes( download + "" );
		}
		model.setContent( item.getString( "content" ) );
		model.setPackgeName( item.getString( "android_pack" ) );
		model.setStatus( GameStatus.UNLOAD );
		String str = item.getString( "label" );
		if (!TextUtils.isEmpty( str )) {
			String[] lableArray = str.split( "," );
			model.setLabelArray( lableArray );
		}
		return model;
	}

	@Override
	public void onError(int what, String error) {
		refreshLayout.setRefreshing( false );
		refreshLayout.setLoading( false );
		showToast( error );
	}
}
