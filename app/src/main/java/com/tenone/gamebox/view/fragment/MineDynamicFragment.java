package com.tenone.gamebox.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.DynamicOperationListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnDataRefreshListener;
import com.tenone.gamebox.mode.listener.OnDynamicDeleteClickListener;
import com.tenone.gamebox.mode.listener.OnDynamicsItemClickListener;
import com.tenone.gamebox.mode.listener.OnLoginStateChangeListener;
import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.mode.mode.DynamicCommentModel;
import com.tenone.gamebox.mode.mode.DynamicModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.share.SharePopupWindow;
import com.tenone.gamebox.view.activity.DynamicDetailsActivity;
import com.tenone.gamebox.view.activity.LoginActivity;
import com.tenone.gamebox.view.adapter.MineDynamicsAdapter;
import com.tenone.gamebox.view.base.BaseLazyFragment;
import com.tenone.gamebox.view.custom.EmptyRecyclerView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DialogUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.OnScrollHelper;
import com.tenone.gamebox.view.utils.SpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class MineDynamicFragment extends BaseLazyFragment implements DynamicOperationListener, OnDynamicsItemClickListener, HttpResultListener, SwipeRefreshLayout.OnRefreshListener, OnLoginStateChangeListener, OnDataRefreshListener, PlatformActionListener, OnDynamicDeleteClickListener {
	@ViewInject(R.id.id_fragment_fans_attention_refresh)
	SwipeRefreshLayout refreshLayout;
	@ViewInject(R.id.id_fragment_fans_attention_recycler)
	EmptyRecyclerView recyclerView;
	@ViewInject(R.id.id_empty_root)
	View emptyView;
	@ViewInject(R.id.id_empty_tv)
	TextView emptyTv;

	private String uid;
	private List<DynamicModel> models = new ArrayList<DynamicModel>();
	private MineDynamicsAdapter adapter;
	private DynamicModel currentModel;
	private int page = 1, senNum = 0;
	private static final int PRAISE = 58;
	private static final int CANCLEPRAISE = 60;
	private static final int STEPON = 59;
	private static final int CANCLESTEPON = 61;
	private boolean hasData = true, isRefresh = true, isLoading = false;
	private OnShareCallback onShareCallback;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate( R.layout.fragment_fans_attention, container, false );
		ViewUtils.inject( this, view );
		Bundle bundle = getArguments();
		if (bundle != null && !TextUtils.isEmpty( bundle.getString( "uid" ) ))
			uid = bundle.getString( "uid" );
		else
			uid = SpUtil.getUserId();
		initView();
		OnScrollHelper.getInstance().onScrollStateUpdate( recyclerView);
		return view;
	}


	@Override
	public void onPraiseClick(final DynamicModel model) {
		if (!(model.getStatus() == 1)) {
			return;
		}
		if (BeanUtils.isLogin()) {
			currentModel = model;
			if (currentModel.isDisGood()) {
				HttpManager.cancleDynamicsLike( CANCLESTEPON, getActivity(), new HttpResultListener() {
					@Override
					public void onSuccess(int what, ResultItem resultItem) {
						if (1 == resultItem.getIntValue( "status" )) {
							ResultItem item = resultItem.getItem( "data" );
							if (item != null && 2 == item.getIntValue( "operate" )) {
								model.setDisGood( false );
								String txt = model.getDislikes();
								try {
									int num = Integer.valueOf( txt ).intValue();
									num--;
									model.setDislikes( num + "" );
								} catch (NumberFormatException e) {
								}
								HttpManager.dynamicsLike( PRAISE, getActivity(),
										new HttpResultListener() {
											@Override
											public void onSuccess(int what, ResultItem resultItem) {
												if (1 == resultItem.getIntValue( "status" )) {
													ResultItem item = resultItem.getItem( "data" );
													if (item != null && 1 == item.getIntValue( "operate" )) {
														String txt = "";
														int num = 0;
														model.setGood( true );
														txt = model.getLikes();
														try {
															num = Integer.valueOf( txt );
														} catch (NumberFormatException e) {
														}
														num++;
														model.setLikes( num + "" );
														adapter.notifyDataSetChanged();
														int bonus = item.getIntValue( "bonus" );
														if (bonus > 0)
															ToastCustom.makeText( getActivity(),
																	"\u6bcf\u65e5\u9996\u6b21\u70b9\u8d5e\u6210\u529f\uff0c\u5956\u52b1" +
																			bonus + "\u91d1\u5e01",
																	ToastCustom.LENGTH_SHORT ).show();
													}
												}
											}

											@Override
											public void onError(int what, String error) {
											}
										}, currentModel.getDynamicModelId(), 1 );
							}
						}
					}

					@Override
					public void onError(int what, String error) {
					}
				}, model.getDynamicModelId(), 0 );
			} else if (currentModel.isGood()) {
				HttpManager.cancleDynamicsLike( CANCLEPRAISE, getActivity(),
						new HttpResultListener() {
							@Override
							public void onSuccess(int what, ResultItem resultItem) {
								if (1 == resultItem.getIntValue( "status" )) {
									ResultItem item = resultItem.getItem( "data" );
									if (item != null && 2 == item.getIntValue( "operate" )) {
										model.setGood( false );
										String txt = model.getLikes();
										try {
											int num = Integer.valueOf( txt ).intValue();
											num--;
											model.setLikes( num + "" );
										} catch (NumberFormatException e) {
										}
										adapter.notifyDataSetChanged();
									}
								}
							}

							@Override
							public void onError(int what, String error) {
							}
						}, currentModel.getDynamicModelId(), 1 );
			} else {
				HttpManager.dynamicsLike( PRAISE, getActivity(),
						new HttpResultListener() {
							@Override
							public void onSuccess(int what, ResultItem resultItem) {
								if (1 == resultItem.getIntValue( "status" )) {
									ResultItem item = resultItem.getItem( "data" );
									if (item != null && 1 == item.getIntValue( "operate" )) {
										String txt = "";
										int num = 0;
										model.setGood( true );
										model.setDisGood( false );
										txt = model.getLikes();
										try {
											num = Integer.valueOf( txt ).intValue();
										} catch (NumberFormatException e) {
										}
										num++;
										model.setLikes( num + "" );
										int bonus = item.getIntValue( "bonus" );
										if (bonus > 0)
											ToastCustom.makeText( getActivity(),
													"\u6bcf\u65e5\u9996\u6b21\u70b9\u8d5e\u6210\u529f\uff0c\u5956\u52b1" + bonus + "\u91d1\u5e01",
													ToastCustom.LENGTH_SHORT ).show();
										adapter.notifyDataSetChanged();
									}
								}
							}

							@Override
							public void onError(int what, String error) {
							}
						}, currentModel.getDynamicModelId(), 1 );
			}
		} else {
			startActivity( new Intent( getActivity(), LoginActivity.class ) );
		}
	}

	@Override
	public void onStepOnClick(final DynamicModel model) {
		if (!(model.getStatus() == 1)) {
			return;
		}
		if (BeanUtils.isLogin()) {
			currentModel = model;
			if (currentModel.isGood()) {
				HttpManager.cancleDynamicsLike( 5585, getActivity(), new HttpResultListener() {
					@Override
					public void onSuccess(int what, ResultItem resultItem) {
						if (1 == resultItem.getIntValue( "status" )) {
							ResultItem item = resultItem.getItem( "data" );
							if (item != null && 2 == item.getIntValue( "operate" )) {
								model.setGood( false );
								String txt = model.getLikes();
								try {
									int num = Integer.valueOf( txt ).intValue();
									num--;
									model.setLikes( num + "" );
								} catch (NumberFormatException e) {
								}
								HttpManager.dynamicsLike( STEPON, getActivity(),
										new HttpResultListener() {
											@Override
											public void onSuccess(int what, ResultItem resultItem) {
												if (1 == resultItem.getIntValue( "status" )) {
													ResultItem item = resultItem.getItem( "data" );
													if (item != null && 0 == item.getIntValue( "operate" )) {
														model.setGood( false );
														model.setDisGood( true );
														String txt = model.getDislikes();
														try {
															int num = Integer.valueOf( txt ).intValue();
															num++;
															model.setDislikes( num + "" );
															adapter.notifyDataSetChanged();
														} catch (NumberFormatException e) {
														}
													}
												}
											}

											@Override
											public void onError(int what, String error) {
											}
										}, currentModel.getDynamicModelId(), 0 );
							}
						}
					}

					@Override
					public void onError(int what, String error) {
					}
				}, model.getDynamicModelId(), 1 );
			} else if (currentModel.isDisGood()) {
				HttpManager.cancleDynamicsLike( CANCLESTEPON, getActivity(),
						new HttpResultListener() {
							@Override
							public void onSuccess(int what, ResultItem resultItem) {
								if (1 == resultItem.getIntValue( "status" )) {
									ResultItem item = resultItem.getItem( "data" );
									if (item != null && 2 == item.getIntValue( "operate" )) {
										model.setDisGood( false );
										String txt = model.getDislikes();
										try {
											int num = Integer.valueOf( txt ).intValue();
											num--;
											model.setDislikes( num + "" );
											adapter.notifyDataSetChanged();
										} catch (NumberFormatException e) {
										}
									}
								}
							}

							@Override
							public void onError(int what, String error) {
							}
						}, currentModel.getDynamicModelId(), 0 );
			} else {
				HttpManager.dynamicsLike( STEPON, getActivity(),
						new HttpResultListener() {
							@Override
							public void onSuccess(int what, ResultItem resultItem) {
								if (1 == resultItem.getIntValue( "status" )) {
									ResultItem item = resultItem.getItem( "data" );
									if (item != null && 0 == item.getIntValue( "operate" )) {
										model.setDisGood( true );
										String txt = currentModel.getDislikes();
										try {
											int num = Integer.valueOf( txt ).intValue();
											num++;
											model.setDislikes( num + "" );
											adapter.notifyDataSetChanged();
										} catch (NumberFormatException e) {
										}
									}
								}
							}

							@Override
							public void onError(int what, String error) {
							}
						}, currentModel.getDynamicModelId(), 0 );
			}
		} else {
			startActivity( new Intent( getActivity(), LoginActivity.class ) );
		}
	}

	@Override
	public void onCommentClick(DynamicModel model) {
		if (!(model.getStatus() == 1)) {
			return;
		}
		startActivity( new Intent( getActivity(), DynamicDetailsActivity.class )
				.putExtra( "dynamicId", model.getDynamicModelId() ) );
	}

	private String currentDynamicId;
	private SharePopupWindow sharePopupWindow;

	@Override
	public void onShareClick(DynamicModel model) {
		if (!(model.getStatus() == 1)) {
			return;
		}
		if (onShareCallback != null) {
			onShareCallback.onShare( model );
		} else {
			currentDynamicId = model.getDynamicModelId();
			if (sharePopupWindow != null && sharePopupWindow.isShowing()) {
				sharePopupWindow.dismiss();
			}
			sharePopupWindow = null;
			sharePopupWindow = new SharePopupWindow( getActivity(), model );
			sharePopupWindow.setPlatformActionListener( MineDynamicFragment.this );
			sharePopupWindow.showAtLocation( refreshLayout, Gravity.BOTTOM, 0, 0 );
		}
	}

	@Override
	public void onAttentionClick(DriverModel model) {

	}

	@Override
	public void onDynamicsItemClick(DynamicModel dynamicModel) {
		if (!(dynamicModel.getStatus() == 1)) {
			return;
		}
		startActivity( new Intent( getActivity(), DynamicDetailsActivity.class )
				.putExtra( "dynamicId", dynamicModel.getDynamicModelId() ) );
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		refreshLayout.setRefreshing( false );
		isRefresh = false;
		isLoading = false;
		if (what == HttpType.REFRESH) {
			models.clear();
		}
		if (1 == resultItem.getIntValue( "status" )) {
			List<ResultItem> items = resultItem.getItems( "data" );
			if (!BeanUtils.isEmpty( items )) {
				setData( items );
			} else {
				hasData = false;
			}
		} else {
			if (HttpType.LOADING == what) {
				hasData = false;
				page--;
			}
		}
	}

	@Override
	public void onError(int what, String error) {
		isRefresh = false;
		isLoading = false;
		refreshLayout.setRefreshing( false );
	}

	@Override
	public void onRefresh() {
		if (BeanUtils.isLogin()) {
			isLoading = false;
			isRefresh = true;
			hasData = true;
			page = 1;
			HttpManager.getDynamics( HttpType.REFRESH, getActivity(), this, uid, page, 5 );
		} else {
			refreshLayout.setRefreshing( false );
		}
	}

	private void initView() {
		ListenerManager.registerOnLoginStateChangeListener( this );
		ListenerManager.registerOnDataRefreshListener( this );
		adapter = new MineDynamicsAdapter( getActivity(), models );
		adapter.setListener( this );
		adapter.setOnDynamicsItemClickListener( this );
		adapter.setOnDynamicDeleteClickListener( this );
		LinearLayoutManager manager = new LinearLayoutManager( getActivity() );
		manager.setOrientation( LinearLayoutManager.VERTICAL );
		manager.setAutoMeasureEnabled( true );
		recyclerView.setLayoutManager( manager );
		recyclerView.setEmptyView( LayoutInflater.from( getActivity() ).inflate( R.layout.layout_empty, null ) );
		recyclerView.setAdapter( adapter );
		refreshLayout.setOnRefreshListener( this );
		recyclerView.addOnScrollListener( new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled( recyclerView, dx, dy );
				LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
				int totalItemCount = layoutManager.getItemCount();
				int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
				if (hasData && !isRefresh && !isLoading && totalItemCount < (lastVisibleItem + 3)) {
					if (BeanUtils.isLogin()) {
						page++;
						HttpManager.getDynamics( HttpType.LOADING, getActivity(),
								MineDynamicFragment.this, uid, page, 5 );
						isLoading = true;
					}
				}
			}
		} );
	}

	private void setData(List<ResultItem> items) {
		if (items.size() > 0) {
			emptyView.setVisibility( View.GONE );
		} else {
			emptyView.setVisibility( View.VISIBLE );
		}
		for (ResultItem item : items) {
			ResultItem dynamics = item.getItem( "dynamics" );
			DynamicModel model = new DynamicModel();
			model.setComments( dynamics.getString( "comment" ) );
			model.setContent( dynamics.getString( "content" ) );
			model.setLikes( dynamics.getString( "likes" ) );
			model.setDislikes( dynamics.getString( "dislike" ) );
			model.setTime( dynamics.getString( "create_time" ) );
			model.setDynamicModelId( dynamics.getString( "id" ) );
			model.setStatus( dynamics.getIntValue( "status" ) );
			model.setLeavel( dynamics.getIntValue( "level" ) );
			model.setShares( dynamics.getString( "share" ) );
			model.setTop( dynamics.getBooleanValue( "top", 1 ) );
			model.setPublishTime( dynamics.getString( "publish_time" ) );
			model.setRemark( dynamics.getString( "remark" ) );
			List<ResultItem> imgs = dynamics.getItems( "imgs" );
			if (null != imgs && imgs.size() > 0) {
				ArrayList<String> imgsArray = new ArrayList<String>();
				for (int i = 0; i < imgs.size(); i++) {
					String img = String.valueOf( imgs.get( i ) );
					imgsArray.add( String.valueOf( img ) );
				}
				model.setDynamicImg( imgsArray );
			}
			ResultItem user = item.getItem( "user" );
			DriverModel driverModel = new DriverModel();
			driverModel.setNick( user.getString( "nick_name" ) );
			driverModel.setSex( user.getString( "sex" ) );
			driverModel.setVip( user.getBooleanValue( "vip", 1 ) );
			driverModel.setHeader( user.getString( "icon_url" ) );
			driverModel.setDriverId( dynamics.getString( "uid" ) );
			model.setModel( driverModel );
			model.setGood( "1".equals( user.getString( "operate" ) ) );
			model.setDisGood( "0".equals( user.getString( "operate" ) ) );


			List<ResultItem> dynamicComments = dynamics.getItems( "comment_info" );
			ArrayList<DynamicCommentModel> dynamicCommentModels = new ArrayList<DynamicCommentModel>();
			if (dynamicCommentModels != null) {
				for (ResultItem dm : dynamicComments) {
					DynamicCommentModel dynamicCommentModel = new DynamicCommentModel();
					dynamicCommentModel.setCommentContent( dm.getString( "content" ) );
					dynamicCommentModel.setToUserNick( dm.getString( "username" ) );
					dynamicCommentModels.add( dynamicCommentModel );
				}
			}
			model.setCommentModels( dynamicCommentModels );

			models.add( model );
		}
		adapter.notifyDataSetChanged();
	}

	public void setOnShareCallback(OnShareCallback onShareCallback) {
		this.onShareCallback = onShareCallback;
	}

	@Override
	public void onLoginStateChange(boolean isLogin) {
		models.clear();
		if (!isLogin) {
			emptyView.setVisibility( View.VISIBLE );
			emptyTv.setText( "\u60a8\u8fd8\u672a\u767b\u5f55\uff0c\u8bf7\u524d\u5f80\u4e2a\u4eba\u4e2d\u5fc3\u767b\u5f55" );
			adapter.notifyDataSetChanged();
		} else {
			emptyTv.setText( "\u6682\u65e0\u6570\u636e" );
			refreshLayout.setRefreshing( true );
			hasData = true;
			page = 1;
			HttpManager.getDynamics( HttpType.LOADING, getActivity(),
					MineDynamicFragment.this, uid, page, 5 );
		}
	}

	@Override
	public void onDataRefresh(int which) {
		if (which == OnDataRefreshListener.MIN) {
			page = 1;
			refreshLayout.setRefreshing( true );
			HttpManager.getDynamics( HttpType.REFRESH, getActivity(), this, uid, page, 5 );
		}
	}

	@Override
	public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
		HttpManager.shareDynamics( 15, getActivity(), new HttpResultListener() {
			@Override
			public void onSuccess(int what, ResultItem resultItem) {
				Log.i( "share_dynamics", resultItem.getString( "msg" ) );
			}

			@Override
			public void onError(int what, String error) {
				Log.i( "share_dynamics", error );
			}
		}, currentDynamicId );
	}

	@Override
	public void onError(Platform platform, int i, Throwable throwable) {
	}

	@Override
	public void onCancel(Platform platform, int i) {
	}

	@Override
	public void onDynamicDeleteClick(DynamicModel model) {

		DialogUtils.showConfirmDialog( getActivity(), (dialog) -> {
			dialog.dismiss();
			buildProgressDialog();
			HttpManager.deleteDynamic( 1, getActivity(), new HttpResultListener() {
				@Override
				public void onSuccess(int what, ResultItem resultItem) {
					cancelProgressDialog();
					if (1 == resultItem.getIntValue( "status" )) {
						models.remove( model );
						adapter.notifyDataSetChanged();
					}
				}

				@Override
				public void onError(int what, String error) {
					cancelProgressDialog();
					ToastCustom.makeText( getActivity(), error, ToastCustom.LENGTH_SHORT ).show();
				}
			}, model.getDynamicModelId() );
		}, "\u786e\u8ba4\u5220\u9664\u53d1\u5e03\u7684\u52a8\u6001\uff1f",
				getString( R.string.cancle ), getString( R.string.confirm ) );
	}

	@Override
	public void onLazyLoad() {
		if (BeanUtils.isLogin()) {
			emptyTv.setText( "\u6682\u65e0\u6570\u636e" );
			refreshLayout.setRefreshing( true );
			HttpManager.getDynamics( HttpType.REFRESH, getActivity(), this, uid, page, 5 );
		} else {
			emptyTv.setText( "\u60a8\u8fd8\u672a\u767b\u5f55\uff0c\u8bf7\u524d\u5f80\u4e2a\u4eba\u4e2d\u5fc3\u767b\u5f55" );
		}
	}

	public interface OnShareCallback {
		void onShare(DynamicModel model);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
