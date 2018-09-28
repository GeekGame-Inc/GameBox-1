/**
 * Project Name:GameBox
 * File Name:GameSearchPresenter.java
 * Package Name:com.tenone.gamebox.presenter
 * Date:2017-3-29����9:34:26
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.GameSearchBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.RecordMode;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.GameSearchView;
import com.tenone.gamebox.view.activity.GameSearchResultActivity;
import com.tenone.gamebox.view.activity.NewGameDetailsActivity;
import com.tenone.gamebox.view.adapter.GameSearchLabelAdapter;
import com.tenone.gamebox.view.adapter.RecordAdapter;
import com.tenone.gamebox.view.adapter.SearchGameListAdapter;
import com.tenone.gamebox.view.adapter.SearchRecordWindowAdapter;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.MyGridView;
import com.tenone.gamebox.view.custom.MyListView;
import com.tenone.gamebox.view.custom.SearchTitleBarView;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * ClassName:GameSearchPresenter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-29 ����9:34:26 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */
public class GameSearchPresenter extends BasePresenter implements
		OnItemClickListener, OnClickListener, HttpResultListener, SearchTitleBarView.OnSearchButtonClickListener {
	private GameSearchView searchView;
	private GameSearchBiz searchBiz;
	private Context mContext;
	private View headerView;

	// ������¼����Դ
	private List<RecordMode> items = new ArrayList<RecordMode>();
	// ������¼
	private List<String> records = new ArrayList<String>();
	// ��Ϸ����
	private List<String> allGameName = new ArrayList<String>();

	// ������Ϸ����Դ
	private List<GameModel> topGridItems = new ArrayList<GameModel>();
	// ��ǩ����Դ
	private List<GameModel> bottomGridItems = new ArrayList<GameModel>();

	private SearchRecordWindowAdapter adapter;
	// ����gridView������
	private SearchGameListAdapter gameAdapter;
	// ��ǩ������
	private GameSearchLabelAdapter labelAdapter;
	// ������¼������
	private RecordAdapter recordAdapter;
	private int platform;
	private String topGameName;

	public GameSearchPresenter(GameSearchView v, Context cxt, int platform, String topGameName) {
		this.mContext = cxt;
		this.searchView = v;
		this.platform = platform;
		this.topGameName = topGameName;
		this.searchBiz = new GameSearchBiz();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	public void initView() {
		allGameName.addAll( getAllGameNames() );
		records.clear();
		records.addAll( getRecords() );
		items.clear();
		items.addAll( getRecordModes() );
		headerView = LayoutInflater.from( mContext ).inflate(
				R.layout.layout_record_header, null );
		getListView().addHeaderView( headerView );
		getSearchTitleBarView().setLeftImg( R.drawable.icon_xqf_b );
		getSearchTitleBarView().setRightImg( R.drawable.ic_sousuo );
		if (!TextUtils.isEmpty( topGameName )) {
			getCustomizeEditText().setText( topGameName );
		}
		adapter = new SearchRecordWindowAdapter( mContext, allGameName, "" );
		getCustomizeEditText().setAdapter( adapter );

		getCustomizeEditText().setDropDownWidth(
				DisplayMetricsUtils.getScreenWidth( mContext ) );

		getCustomizeEditText().setDropDownVerticalOffset(
				DisplayMetricsUtils.dipTopx( mContext, 3 ) );

	}

	public void setAdapter() {
		gameAdapter = new SearchGameListAdapter( topGridItems, mContext );
		getTopGridView().setAdapter( gameAdapter );
		labelAdapter = new GameSearchLabelAdapter( bottomGridItems, mContext );
		getBottomGridView().setAdapter( labelAdapter );
		recordAdapter = new RecordAdapter( mContext, items );
		getListView().setAdapter( recordAdapter );
	}

	public void initListener() {
		getListView().setOnItemClickListener( this );
		getSearchTitleBarView().getLeftImg().setOnClickListener( this );
		getSearchTitleBarView().getRightImg().setOnClickListener( this );
		getTopGridView().setOnItemClickListener( this );
		getBottomGridView().setOnItemClickListener( this );
		getSearchTitleBarView().setOnSearchButtonClickListener( this );
	}

	public void requestList(int what) {
		String url = MyApplication.getHttpUrl().getHotGameSearch();
		RequestBody requestBody = new FormBody.Builder()
				.add( "channel", MyApplication.getConfigModle().getChannelID() )
				.add( "system", "1" )
				.build();
		HttpUtils.postHttp( mContext, what, url, requestBody, this );
	}

	private CustomizeEditText getCustomizeEditText() {
		return getSearchTitleBarView().getCustomizeEditText();
	}

	private SearchTitleBarView getSearchTitleBarView() {
		return searchView.getSearchTitleBarView();
	}

	private MyGridView getTopGridView() {
		return searchView.getTopGridView();
	}

	private MyGridView getBottomGridView() {
		return searchView.getBottomGridView();
	}

	private MyListView getListView() {
		return searchView.getListView();
	}

	/* ��ȡ������Ϸ���֣�����ģ��ƥ�䣩 */
	private List<String> getAllGameNames() {
		return searchBiz.getAllGameNames();
	}

	/* ����������Ϸ */
	private List<GameModel> getGameModels(List<ResultItem> resultItems) {
		return searchBiz.getGameModels( resultItems );
	}

	/* ��ǩ */
	private List<GameModel> getLabels(List<ResultItem> resultItems) {
		return searchBiz.getLabels( resultItems );
	}

	/* ������¼(���������) */
	private List<RecordMode> getRecordModes() {
		return searchBiz.getRecordModes( records );
	}

	/* ������¼(���������) */
	private List<String> getRecords() {
		return searchBiz.getRecords();
	}

	private void saveRecord(List<String> records) {
		searchBiz.saveRecord( records );
	}

	private void clearRecord() {
		searchBiz.clearRecord();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
													long id) {
		switch (parent.getId()) {
			case R.id.id_gameSearch_gridViewTop:
				openOtherActivity( mContext, new Intent( mContext, NewGameDetailsActivity.class )
						.putExtra( "id", topGridItems.get( position ).getGameId() + "" ) );
				break;
			case R.id.id_gameSearch_gridViewBottom:
				openOtherActivity( mContext, new Intent( mContext, NewGameDetailsActivity.class )
						.putExtra( "id", bottomGridItems.get( position ).getGameId() + "" ) );
				break;
			default:
				// ��һ��
				// ��ͷ,��1��ʼ
				if (position == 1) {
					clearRecord();
					items.clear();
					records.clear();
					recordAdapter.notifyDataSetChanged();
				} else if (position > 1) {
					// ������һ��
					String str = items.get( position - 1 ).getRecord();
					records.remove( str );
					records.add( 0, str );
					saveRecord( records );
					openOtherActivity( mContext, new Intent( mContext, GameSearchResultActivity.class )
							.putExtra( "condition", str )
							.putExtra( "platform", platform ) );
				}
				break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.id_searchTitle_leftImg:
				close( mContext );
				break;
			case R.id.id_searchTitle_rightImg:
				String str = getCustomizeEditText().getText().toString();
				if (!TextUtils.isEmpty( str )) {
					records.remove( str );
					records.add( str );
					saveRecord( records );
					// ��һ��
					openOtherActivity( mContext, new Intent( mContext, GameSearchResultActivity.class )
							.putExtra( "condition", str )
							.putExtra( "platform", platform ) );
				} else {
					showToast( mContext, "��������������" );
				}
				break;
		}
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		if ("0".equals( resultItem.getString( "status" ) )) {
			Message message = new Message();
			message.what = what;
			message.obj = resultItem;
			handler.sendMessage( message );
		} else {
			showToast( mContext, resultItem.getString( "msg" ) );
		}
	}

	@Override
	public void onError(int what, String error) {
		showToast( mContext, error );
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ResultItem resultItem = (ResultItem) msg.obj;
			List<ResultItem> list = resultItem.getItems( "data" );
			switch (msg.what) {
				case HttpType.REFRESH:
					if (list != null) {
						topGridItems.clear();
						topGridItems.addAll( getGameModels( list ) );
						bottomGridItems.clear();
						bottomGridItems.addAll( getLabels( list ) );
					}
					break;
			}
			gameAdapter.notifyDataSetChanged();
			labelAdapter.notifyDataSetChanged();
		}
	};

	@Override
	public void onSearchButtonClick() {
		String str = getSearchTitleBarView().getCustomizeEditText()
				.getText().toString();
		if (!TextUtils.isEmpty( str )) {
			records.remove( str );
			records.add( str );
			saveRecord( records );
			// ��һ��
			openOtherActivity( mContext, new Intent( mContext, GameSearchResultActivity.class )
					.putExtra( "condition", str )
					.putExtra( "platform", platform ) );
		} else {
			showToast( mContext, "��������������" );
		}
	}
}
