package com.tenone.gamebox.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.CheckAllCommentBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnDynamicCommentItemClickListener;
import com.tenone.gamebox.mode.mode.DynamicCommentModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.CheckAllCommentView;
import com.tenone.gamebox.view.activity.LoginActivity;
import com.tenone.gamebox.view.activity.PublishGameCommentActivity;
import com.tenone.gamebox.view.adapter.CommentFragmentAdapter;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;

import java.util.ArrayList;
import java.util.List;

public class CheckAllCommentPresenter extends BasePresenter implements
		OnClickListener, OnRefreshListener,
		OnLoadListener, HttpResultListener, OnDynamicCommentItemClickListener {
	private CheckAllCommentBiz commentBiz;
	private CheckAllCommentView commentView;
	private Context mContext;
	private List<DynamicCommentModel> modes = new ArrayList<DynamicCommentModel>();
	private int pageNo = 1;
	private CommentFragmentAdapter mAdapter;

	public CheckAllCommentPresenter(CheckAllCommentView view, Context cxt) {
		this.commentView = view;
		this.mContext = cxt;
		this.commentBiz = new CheckAllCommentBiz();
	}

	public void initView() {
		getTitleBarView().setTitleText( "\u5168\u90e8\u8bc4\u8bba" );
		getTitleBarView().setLeftImg( R.drawable.icon_xqf_b );
	}

	public void initListener() {
		getTitleBarView().getLeftImg().setOnClickListener( this );
		getreRefreshLayout().setOnRefreshListener( this );
		getreRefreshLayout().setOnLoadListener( this );
	}

	public void setAdapter() {
		mAdapter = new CommentFragmentAdapter( modes, mContext );
		mAdapter.setOnDynamicCommentItemClickListener( this );
		getListView().setAdapter( mAdapter );
		request( HttpType.REFRESH );
	}

	public void request(int what) {
		HttpManager.gameCommentList( what, mContext, this, getGameId(), 2, pageNo );
	}

	public TitleBarView getTitleBarView() {
		return commentView.getTitleBarView();
	}

	public RefreshLayout getreRefreshLayout() {
		return commentView.getreRefreshLayout();
	}

	public ListView getListView() {
		return commentView.getListView();
	}

	public Intent getIntent() {
		return commentView.getIntent();
	}

	public String getGameId() {
		return commentBiz.getGameId( getIntent() );
	}

	public List<DynamicCommentModel> getCommentModes(ResultItem resultItem) {
		return commentBiz.getCommentModes( resultItem );
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.id_titleBar_leftImg:
				close( mContext );
				break;
		}
	}


	@Override
	public void onLoad() {
		pageNo++;
		request( HttpType.LOADING );
	}

	@Override
	public void onRefresh() {
		pageNo = 1;
		request( HttpType.REFRESH );
	}


	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		getreRefreshLayout().setRefreshing( false );
		getreRefreshLayout().setLoading( false );
		if (1 == resultItem.getIntValue( "status" )) {
			if (what == HttpType.REFRESH) {
				modes.clear();
			}
			ResultItem data = resultItem.getItem( "data" );
			if (!BeanUtils.isEmpty( data ))
				modes.addAll( getCommentModes( data ) );
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onError(int what, String error) {
		getreRefreshLayout().setRefreshing( false );
		getreRefreshLayout().setLoading( false );
	}

	@Override
	public void onCommentClick(DynamicCommentModel model) {
		if (!BeanUtils.isLogin()) {
			openOtherActivity( mContext, new Intent( mContext, LoginActivity.class ) );
			return;
		}
		openOtherActivity(
				mContext,
				new Intent( mContext, PublishGameCommentActivity.class )
						.putExtra( "model", model ).putExtra( "gameId",
						getGameId() ) );
	}

	@Override
	public void onPraiseClick(DynamicCommentModel model) {
		if (!BeanUtils.isLogin()) {
			openOtherActivity( mContext, new Intent( mContext, LoginActivity.class ) );
			return;
		}
		if (!(model.getLikeTy() == 1)) {
			HttpManager.commentLike( 2500, mContext, new HttpResultListener() {
				@Override
				public void onSuccess(int what, ResultItem resultItem) {
					if (1 == resultItem.getIntValue( "status" )) {
						model.setLikeTy( 1 );
						String likes = model.getCommentLikes();
						if (!TextUtils.isEmpty( likes )) {
							try {
								int l = Integer.valueOf( likes ).intValue();
								l++;
								model.setCommentLikes( l + "" );
							} catch (NumberFormatException e) {
							}
						}
						if (modes.indexOf( model ) > -1)
							mAdapter.notifyDataSetChanged();
					}
					ToastCustom.makeText( mContext,
							resultItem.getString( "msg" ), ToastCustom.LENGTH_SHORT ).show();
				}

				@Override
				public void onError(int what, String error) {
					ToastCustom.makeText( mContext, error, ToastCustom.LENGTH_SHORT ).show();
				}
			}, model.getCommentId(), 1 );
		} else {
			ToastCustom.makeText( mContext, "\u60a8\u5df2\u7ecf\u8d5e\u8fc7\u4e86", ToastCustom.LENGTH_SHORT ).show();
		}
	}

	@Override
	public void onHeaderClick(DynamicCommentModel model) {

	}

	@Override
	public void onDeleteClick(DynamicCommentModel model) {
		if (!BeanUtils.isLogin()) {
			openOtherActivity( mContext, new Intent( mContext, LoginActivity.class ) );
			return;
		}
		HttpManager.deleteComment( 2006, mContext, new HttpResultListener() {
			@Override
			public void onSuccess(int what, ResultItem resultItem) {
				if (1 == resultItem.getIntValue( "status" )) {
					if (modes.indexOf( model ) > -1) {
						modes.remove( model );
						mAdapter.notifyDataSetChanged();
					}
				}
				ToastCustom.makeText( mContext,
						resultItem.getString( "msg" ), ToastCustom.LENGTH_SHORT ).show();
			}

			@Override
			public void onError(int what, String error) {
				ToastCustom.makeText( mContext, error, ToastCustom.LENGTH_SHORT ).show();
			}
		}, model.getCommentId() );
	}
}
