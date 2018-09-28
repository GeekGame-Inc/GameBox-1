/**
 * Project Name:GameBox
 * File Name:DetailsGiftFragmentPresenter.java
 * Package Name:com.tenone.gamebox.presenter
 * Date:2017-4-12����11:51:57
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.DetailsGiftFragmentBiz;
import com.tenone.gamebox.mode.listener.GameGiftButtonClickListener;
import com.tenone.gamebox.mode.listener.GetGiftDialogConfirmListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnGiftItemClickListener;
import com.tenone.gamebox.mode.mode.GiftMode;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.DetailsGiftFragmentView;
import com.tenone.gamebox.view.activity.GiftDetailsActivity;
import com.tenone.gamebox.view.adapter.DetailsGiftAdapter;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.SpacesItemDecoration;
import com.tenone.gamebox.view.custom.dialog.GetGiftDialog.Builder;
import com.tenone.gamebox.view.utils.CharSequenceUtils;
import com.tenone.gamebox.view.utils.EncryptionUtils;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.NetworkUtils;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TelephoneUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * ClassName:DetailsGiftFragmentPresenter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-12 ����11:51:57 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */
public class DetailsGiftFragmentPresenter extends BasePresenter implements
		GameGiftButtonClickListener, GetGiftDialogConfirmListener,
		HttpResultListener, SwipeRefreshLayout.OnRefreshListener, OnGiftItemClickListener {
	private DetailsGiftFragmentBiz gameGiftBiz;

	private DetailsGiftFragmentView gameGiftView;

	private Context mContext;

	private DetailsGiftAdapter mAdapter;

	private Builder builder;
	private List<GiftMode> giftModes = new ArrayList<GiftMode>();
	private int currentPosition = -1;

	public DetailsGiftFragmentPresenter(DetailsGiftFragmentView view,
																			Context cxt) {
		this.gameGiftBiz = new DetailsGiftFragmentBiz();
		this.gameGiftView = view;
		this.mContext = cxt;
		giftModes.addAll( getGameRecommends( null ) );
	}

	public void setAdapter() {
		mAdapter = new DetailsGiftAdapter( giftModes, mContext );
		mAdapter.setOnGiftItemClickListener( this );
		getListView().setLayoutManager( new LinearLayoutManager( mContext, LinearLayoutManager.VERTICAL, false ) );
		getListView().addItemDecoration( new SpacesItemDecoration( mContext, LinearLayoutManager.VERTICAL,
				1, mContext.getResources().getColor( R.color.divider ) ) );
		getListView().setAdapter( mAdapter );
		mAdapter.setButtonClickListener( this );
		getRefreshLayout().setOnRefreshListener( this );
	}

	public void requestList(int what) {
		String url = MyApplication.getHttpUrl().getGetPacksByGame();
		RequestBody requestBody = new FormBody.Builder()
				.add( "channel_id",
						MyApplication.getConfigModle().getChannelID() )
				.add( "username", SpUtil.getAccount() )
				.add( "device_id", TelephoneUtils.getImei( mContext ) )
				.add( "game_id", getGameId() ).build();
		HttpUtils.postHttp( mContext, what, url, requestBody, this );
	}

	private void requestCode(int what, int id) {
		String url = MyApplication.getHttpUrl().getGetPack();
		Map<String, String> map = new HashMap<String, String>();
		map.put( "username", SpUtil.getAccount() );
		map.put( "ip", NetworkUtils.getLocalIpAddress() );
		map.put( "terminal_type", 2 + "" );
		map.put( "pid", id + "" );
		map.put( "device_id", TelephoneUtils.getImei( mContext ) );
		RequestBody requestBody = new FormBody.Builder()
				.add( "channel_id",
						MyApplication.getConfigModle().getChannelID() )
				.add( "username", SpUtil.getAccount() )
				.add( "device_id", TelephoneUtils.getImei( mContext ) )
				.add( "ip", NetworkUtils.getLocalIpAddress() )
				.add( "terminal_type", 2 + "" ).add( "pid", id + "" )
				.add( "sign", EncryptionUtils.getSingTure( map ) ).build();
		HttpUtils.postHttp( mContext, what, url, requestBody, this );
		map = null;
	}

	public RecyclerView getListView() {
		return gameGiftView.getListView();
	}

	public SwipeRefreshLayout getRefreshLayout() {
		return gameGiftView.getRefreshLayout();
	}

	public String getGameId() {
		return gameGiftView.getGameId();
	}

	public List<GiftMode> getGameRecommends(List<ResultItem> resultItems) {
		return gameGiftBiz.constructArray( resultItems );
	}

	private String getGiftCode(ResultItem resultItem) {
		return gameGiftBiz.getGiftCode( resultItem );
	}

	private void showDialog() {
		if (builder == null) {
			builder = new Builder( mContext );
		}
		builder.setButtonText( mContext.getResources().getString(
				R.string.confirm ) );
		builder.setConfirmListener( this );
		builder.setTitle( mContext.getResources().getString(
				R.string.codeCopyHintTitle ) );
		builder.setMessage( mContext.getResources().getString(
				R.string.codeCopyHint ) );
		builder.showDialog();
	}

	@Override
	public void onButtonClick(GiftMode giftMode) {
		currentPosition = giftModes.indexOf( giftMode );
		if (TextUtils.isEmpty( giftMode.getGiftCode() )) {
			requestCode( HttpType.LOADING, giftMode.getGiftId() );
		} else {
			if (CharSequenceUtils.CopyToClipboard( mContext,
					giftMode.getGiftCode() )) {
				showDialog();
			} else {
				Toast.makeText( mContext, "\u9886\u53d6\u793c\u5305\u7801\u51fa\u9519", Toast.LENGTH_SHORT ).show();
			}
		}
	}

	@Override
	public void onConfirmClick(AlertDialog dialog) {
		dialog.dismiss();
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		getRefreshLayout().setRefreshing( false );
		if ("0".equals( resultItem.getString( "status" ) )) {
			setData( resultItem, what );
		} else {
			showToast( mContext, resultItem.getString( "msg" ) );
		}
	}

	@Override
	public void onError(int what, String error) {
		showToast( mContext, error );
		getRefreshLayout().setRefreshing( false );
	}

	private void setData(ResultItem resultItem, int what) {
		switch (what) {
			case HttpType.REFRESH:
				giftModes.clear();
				new Thread() {
					@Override
					public void run() {
						ResultItem result = resultItem.getItem( "data" );
						if (result != null) {
							giftModes
									.addAll( getGameRecommends( result.getItems( "list" ) ) );
						}
						getListView().post( () -> mAdapter.notifyDataSetChanged() );
						super.run();
					}
				}.start();
				break;
			case HttpType.LOADING:
				String card = getGiftCode( resultItem );
				giftModes.get( currentPosition ).setState( 1 );
				giftModes.get( currentPosition ).setGiftCode( card );
				String str = resultItem.getString( "msg" );
				showToast( mContext, TextUtils.isEmpty( str ) ? "\u793c\u5305\u9886\u53d6\u6210\u529f" : str );
				mAdapter.notifyDataSetChanged();
				break;
		}
	}

	@Override
	public void onRefresh() {
		requestList( HttpType.REFRESH );
	}

	@Override
	public void onGiftItemClick(GiftMode mode) {
		openOtherActivity( mContext,
				new Intent( mContext, GiftDetailsActivity.class )
						.putExtra( "giftId", mode.getGiftId() ) );
	}
}
