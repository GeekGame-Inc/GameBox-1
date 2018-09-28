
package com.tenone.gamebox.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnDynamicCommentItemClickListener;
import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.mode.mode.DynamicCommentModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.activity.LoginActivity;
import com.tenone.gamebox.view.activity.PublishGameCommentActivity;
import com.tenone.gamebox.view.adapter.RecycleCommentsAdapter;
import com.tenone.gamebox.view.base.BaseLazyFragment;
import com.tenone.gamebox.view.custom.LoadMoreRecyclerView;
import com.tenone.gamebox.view.custom.SpacesItemDecoration;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;

import java.util.ArrayList;
import java.util.List;

public class DetailsCommentFragment extends BaseLazyFragment implements
		HttpResultListener, OnDynamicCommentItemClickListener, SwipeRefreshLayout.OnRefreshListener {
	@ViewInject(R.id.id_gameDetailsComment_listView)
	LoadMoreRecyclerView listView;
	@ViewInject(R.id.id_gameDetailsComment_refreshLayout)
	SwipeRefreshLayout refreshLayout;

	private View view;
	private String gameId;
	private int page = 1, count = 0;
	private RecycleCommentsAdapter adapter;
	private List<DynamicCommentModel> models = new ArrayList<DynamicCommentModel>();

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
													 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate( R.layout.fragment_details_comment,
					container, false );
		}
		ViewUtils.inject( this, view );
		Bundle bundle = getArguments();
		gameId = bundle.getString( "id" );
		return view;
	}

	private void initView() {
		adapter = new RecycleCommentsAdapter( models, getActivity() );
		adapter.setOnlyOnce( true );
		adapter.setOnDynamicCommentItemClickListener( this );
		LinearLayoutManager manager = new LinearLayoutManager( getActivity(), LinearLayoutManager.VERTICAL, false );
		listView.setLayoutManager( manager );
		listView.addItemDecoration( new SpacesItemDecoration( getActivity(), LinearLayoutManager.VERTICAL,
				1, getResources().getColor( R.color.divider ) ) );
		listView.setAdapter( adapter );
		refreshLayout.setOnRefreshListener( this );
		listView.initLoadMore( () -> {
			page++;
			HttpManager.gameCommentList( HttpType.LOADING, getActivity(),
					DetailsCommentFragment.this, gameId, 2, page );
		} );
		listView.setLoadMoreEnabled( true );
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		refreshLayout.setRefreshing( false );
		listView.loadMoreComplete();
		if (1 == resultItem.getIntValue( "status" )) {
			if (HttpType.REFRESH == what) {
				if (adapter != null) {
					adapter.clear();
				}
				models.clear();
			}
			ResultItem item = resultItem.getItem( "data" );
			if (!BeanUtils.isEmpty( item )) {
				count = item.getIntValue( "count" );
				List<ResultItem> comments = item.getItems( "list" );
				if (!BeanUtils.isEmpty( comments )) {
					setData( comments );
				} else {
					listView.loadMoreFailed();
					listView.setLoadMoreEnabled( false );
				}
				adapter.notifyDataSetChanged();
			} else if (HttpType.LOADING == what) {
				listView.loadMoreFailed();
				listView.setLoadMoreEnabled( false );
			}
		}
	}

	@Override
	public void onError(int what, String error) {
		refreshLayout.setRefreshing( false );
		listView.loadMoreFailed();
		listView.setLoadMoreEnabled( false );
	}

	@Override
	public void onCommentClick(DynamicCommentModel model) {
		if (!BeanUtils.isLogin()) {
			startActivity( new Intent( getActivity(), LoginActivity.class ) );
			return;
		}
		buildProgressDialog();
		HttpManager.userLoginApp( 0, getActivity(), new HttpResultListener() {
			@Override
			public void onSuccess(int what, ResultItem resultItem) {
				cancelProgressDialog();
				if (1 == resultItem.getIntValue( "status" )) {
					startActivity(
							new Intent( getActivity(), PublishGameCommentActivity.class )
									.putExtra( "model", model ).putExtra( "gameId",
									gameId ) );
				} else {
					ToastCustom.makeText( getActivity(), resultItem.getString( "msg" ), ToastCustom.LENGTH_SHORT ).show();
				}
			}

			@Override
			public void onError(int what, String error) {
				cancelProgressDialog();
				ToastCustom.makeText( getActivity(), error, ToastCustom.LENGTH_SHORT ).show();
			}
		}, gameId );
	}

	@Override
	public void onPraiseClick(DynamicCommentModel model) {
		if (!BeanUtils.isLogin()) {
			startActivity( new Intent( getActivity(), LoginActivity.class ) );
			return;
		}
		if (!(model.getLikeTy() == 1)) {
			HttpManager.commentLike( 2500, getActivity(), new HttpResultListener() {
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
						if (models.indexOf( model ) > -1)
							adapter.notifyDataSetChanged();
					}
					ToastCustom.makeText( getActivity(),
							resultItem.getString( "msg" ), ToastCustom.LENGTH_SHORT ).show();
				}

				@Override
				public void onError(int what, String error) {
					ToastCustom.makeText( getActivity(), error, ToastCustom.LENGTH_SHORT ).show();
				}
			}, model.getCommentId(), 1 );
		} else {
			ToastCustom.makeText( getActivity(), "\u60a8\u5df2\u7ecf\u8d5e\u8fc7\u4e86", ToastCustom.LENGTH_SHORT ).show();
		}
	}

	@Override
	public void onHeaderClick(DynamicCommentModel model) {

	}

	@Override
	public void onDeleteClick(DynamicCommentModel model) {
		if (!BeanUtils.isLogin()) {
			startActivity( new Intent( getActivity(), LoginActivity.class ) );
			return;
		}
		buildProgressDialog();
		HttpManager.deleteComment( 2006, getActivity(), new HttpResultListener() {
			@Override
			public void onSuccess(int what, ResultItem resultItem) {
				cancelProgressDialog();
				if (1 == resultItem.getIntValue( "status" )) {
					if (models.indexOf( model ) > -1) {
						models.remove( model );
						adapter.notifyDataSetChanged();
					}
				}
				ToastCustom.makeText( getActivity(),
						resultItem.getString( "msg" ), ToastCustom.LENGTH_SHORT ).show();
			}

			@Override
			public void onError(int what, String error) {
				cancelProgressDialog();
				ToastCustom.makeText( getActivity(), error, ToastCustom.LENGTH_SHORT ).show();
			}
		}, model.getCommentId() );
	}

	public void setData(List<ResultItem> comments) {
		if (!BeanUtils.isEmpty( comments )) {
			for (ResultItem item : comments) {
				DynamicCommentModel model = new DynamicCommentModel();
				model.setCommentContent( item.getString( "content" ) );
				model.setCommentId( item.getString( "id" ) );
				model.setCommentTime( item.getString( "create_time" ) );
				model.setCommentDislike( item.getString( "dislike" ) );
				model.setCommentLikes( item.getString( "likes" ) );
				model.setDynamicId( item.getString( "dynamics_id" ) );
				model.setToUserId( item.getString( "to_uid" ) );
				model.setToUserIsVip( item.getBooleanValue( "touid_vip", 1 ) );
				model.setToUserNick( item.getString( "touid_nickname" ) );
				model.setToUserHeader( item.getString( "touid_iconurl" ) );
				model.setLikeTy( item.getIntValue( "like_type" ) );
				model.setIsFake( item.getIntValue( "is_fake" ) );
				model.setOrder( item.getIntValue( "order" ) );
				DriverModel driverModel = new DriverModel();
				driverModel.setDriverId( item.getString( "uid" ) );
				driverModel.setHeader( item.getString( "uid_iconurl" ) );
				driverModel.setVip( item.getBooleanValue( "uid_vip", 1 ) );
				driverModel.setNick( item.getString( "uid_nickname" ) );
				model.setDriverModel( driverModel );
				models.add( model );
			}
		}
	}

	@Override
	public void onRefresh() {
		listView.setLoadMoreEnabled( true );
		page = 1;
		HttpManager.gameCommentList( HttpType.REFRESH, getActivity(), DetailsCommentFragment.this, gameId, 2, page );
	}

	@Override
	public void onLazyLoad() {
		initView();
		page = 1;
		refreshLayout.setRefreshing( true );
		HttpManager.gameCommentList( HttpType.REFRESH, getActivity(), DetailsCommentFragment.this, gameId, 2, page );
	}
}
