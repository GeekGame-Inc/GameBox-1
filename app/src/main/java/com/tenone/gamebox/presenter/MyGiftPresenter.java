package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.MyGiftBiz;
import com.tenone.gamebox.mode.listener.GameGiftButtonClickListener;
import com.tenone.gamebox.mode.listener.GetGiftDialogConfirmListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnGiftItemClickListener;
import com.tenone.gamebox.mode.mode.GiftMode;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.MyGiftView;
import com.tenone.gamebox.view.activity.GiftDetailsActivity;
import com.tenone.gamebox.view.adapter.GameGiftListAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.RecommendListView;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.dialog.GetGiftDialog.Builder;
import com.tenone.gamebox.view.utils.CharSequenceUtils;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class MyGiftPresenter extends BasePresenter implements OnClickListener,
		OnRefreshListener, OnLoadListener, GameGiftButtonClickListener,
		GetGiftDialogConfirmListener, HttpResultListener, OnGiftItemClickListener {
	MyGiftBiz giftBiz;
	MyGiftView giftView;
	Context mContext;

	GameGiftListAdapter mAdapter;
	Builder builder;
	int page = 1;
	List<GiftMode> giftModes = new ArrayList<GiftMode>();
	AlertDialog alertDialog;

	public MyGiftPresenter(Context cxt, MyGiftView v) {
		this.giftBiz = new MyGiftBiz();
		this.giftView = v;
		this.mContext = cxt;
	}

	public void initView() {
		alertDialog = buildProgressDialog( mContext );
		getTitleBarView().setTitleText( "\u6211\u7684\u793c\u5305" );
		getTitleBarView().setLeftImg( R.drawable.icon_back_grey );
	}

	public void setAdapter() {
		if (mAdapter == null) {
			mAdapter = new GameGiftListAdapter( giftModes, mContext );
		}
		getListView().setAdapter( mAdapter );
	}

	public void initListener() {
		getTitleBarView().getLeftImg().setOnClickListener( this );
		getRefreshLayout().setOnRefreshListener( this );
		getRefreshLayout().setOnLoadListener( this );
		mAdapter.setButtonClickListener( this );
		mAdapter.setOnGiftItemClickListener( this );
	}

	public void requestList(int what) {
		String url = MyApplication.getHttpUrl().getGetPacksByUser();
		RequestBody requestBody = new FormBody.Builder()
				.add( "page", page + "" )
				.add( "username", SpUtil.getAccount() )
				.add( "channel_id",
						MyApplication.getConfigModle().getChannelID() )
				.add( "order", "create_time" ).add( "order_type", "DESC" ).build();

		Log.i( "GameBox", "url is " + url + "?page=" + page + "&username=" + SpUtil.getAccount() + "&channel_id=" + MyApplication.getConfigModle().getChannelID() + "&order=create_time&order_type=DESC" );
		HttpUtils.postHttp( mContext, what, url, requestBody, this );
	}

	public TitleBarView getTitleBarView() {
		return giftView.getTitleBarView();
	}

	public RefreshLayout getRefreshLayout() {
		return giftView.getRefreshLayout();
	}

	public RecommendListView getListView() {
		return giftView.getListView();
	}

	public List<GiftMode> getGiftModes(ResultItem resultItem) {
		return giftBiz.getGiftModes( resultItem );
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
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.id_titleBar_leftImg:
				((BaseActivity) mContext).finish();
				break;
		}
	}

	@Override
	public void onRefresh() {
		page = 1;
		requestList( HttpType.REFRESH );
	}

	@Override
	public void onLoad() {
		page++;
		requestList( HttpType.LOADING );
	}

	@Override
	public void onButtonClick(GiftMode giftMode) {
		if (CharSequenceUtils.CopyToClipboard( mContext, giftMode.getGiftCode() )) {
			giftMode.setState( 1 );
			mAdapter.notifyDataSetChanged();
			showDialog();
		} else {
			Toast.makeText( mContext, "\u9886\u53d6\u793c\u5305\u7801\u51fa\u9519", Toast.LENGTH_SHORT ).show();
		}
	}

	@Override
	public void onConfirmClick(AlertDialog dialog) {
		dialog.dismiss();
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		cancelProgressDialog( alertDialog );
		getRefreshLayout().setLoading( false );
		getRefreshLayout().setRefreshing( false );
		if ("0".equals( resultItem.getString( "status" ) )) {
			Message message = new Message();
			message.obj = resultItem;
			message.what = what;
			handler.sendMessage( message );
		} else {
			showToast( mContext, resultItem.getString( "msg" ) );
		}
	}

	@Override
	public void onError(int what, String error) {
		cancelProgressDialog( alertDialog );
		getRefreshLayout().setLoading( false );
		getRefreshLayout().setRefreshing( false );
		showToast( mContext, error );
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ResultItem resultItem = (ResultItem) msg.obj;
			switch (msg.what) {
				case HttpType.REFRESH:
					giftModes.clear();
					giftModes.addAll( getGiftModes( resultItem.getItem( "data" ) ) );
					break;
				case HttpType.LOADING:
					giftModes.addAll( getGiftModes( resultItem.getItem( "data" ) ) );
					break;
			}
			mAdapter.notifyDataSetChanged();
		}

	};


	@Override
	public void onGiftItemClick(GiftMode mode) {
		Intent intent = new Intent( mContext, GiftDetailsActivity.class );
		intent.putExtra( "giftId", mode.getGiftId() );
		openOtherActivity( mContext, intent );
	}
}
