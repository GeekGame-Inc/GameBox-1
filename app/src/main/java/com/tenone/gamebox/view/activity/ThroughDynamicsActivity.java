package com.tenone.gamebox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.DynamicOperationListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnHeaderClickListener;
import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.mode.mode.DynamicCommentModel;
import com.tenone.gamebox.mode.mode.DynamicModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.share.SharePopupWindow;
import com.tenone.gamebox.view.adapter.DynamicAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.MyRefreshListView;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;



public class ThroughDynamicsActivity extends BaseActivity implements DynamicOperationListener, OnHeaderClickListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, HttpResultListener, PlatformActionListener {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_through_time)
	TextView timeTv;
	@ViewInject(R.id.id_through_refresh)
	RefreshLayout refreshLayout;
	@ViewInject(R.id.id_through_listView)
	MyRefreshListView listView;

	private List<DynamicModel> models = new ArrayList<DynamicModel>();
	private DynamicAdapter adapter;
	private int page = 1, dp20, scrWidth;
	private boolean hasData = true;
	private static final int PRAISE = 58;
	private static final int CANCLEPRAISE = 60;
	private static final int STEPON = 59;
	private static final int CANCLESTEPON = 61;
	private DynamicModel currentModel;
	private Context context;
	private SharePopupWindow sharePopupWindow;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		setContentView( R.layout.activity_through );
		context = this;
		ViewUtils.inject( this );
		buildProgressDialog();
		initTitle();
		initView();
	}

	@Override
	public void onPraiseClick(final DynamicModel model) {
		if (BeanUtils.isLogin()) {
			currentModel = model;
			if (currentModel.isDisGood()) {
				HttpManager.cancleDynamicsLike( CANCLESTEPON, context, new HttpResultListener() {
					@Override
					public void onSuccess(int what, ResultItem resultItem) {
						if (1 == resultItem.getIntValue( "status" )) {
							model.setDisGood( false );
							String txt = model.getDislikes();
							try {
								int num = Integer.valueOf( txt ).intValue();
								num--;
								model.setDislikes( num + "" );
							} catch (NumberFormatException e) {
							}
							ResultItem item = resultItem.getItem( "data" );
							if (2 == item.getIntValue( "operate" )) {
								HttpManager.dynamicsLike( PRAISE, context,
										new HttpResultListener() {
											@Override
											public void onSuccess(int what, ResultItem resultItem) {
												if (1 == resultItem.getIntValue( "status" )) {
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
													ResultItem item = resultItem.getItem( "data" );
													if (item != null) {
														int bonus = item.getIntValue( "bonus" );
														if (bonus > 0)
															ToastCustom.makeText( context,
																	"\u6bcf\u65e5\u9996\u6b21\u70b9\u8d5e\u6210\u529f\uff0c\u5956\u52b1" + bonus + "\u91d1\u5e01",
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
				HttpManager.cancleDynamicsLike( CANCLEPRAISE, context,
						new HttpResultListener() {
							@Override
							public void onSuccess(int what, ResultItem resultItem) {
								if (1 == resultItem.getIntValue( "status" )) {
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

							@Override
							public void onError(int what, String error) {
							}
						}, currentModel.getDynamicModelId(), 1 );
			} else {
				HttpManager.dynamicsLike( PRAISE, context,
						new HttpResultListener() {
							@Override
							public void onSuccess(int what, ResultItem resultItem) {
								if (1 == resultItem.getIntValue( "status" )) {
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
									ResultItem item = resultItem.getItem( "data" );
									if (item != null) {
										int bonus = item.getIntValue( "bonus" );
										if (bonus > 0)
											ToastCustom.makeText( context, "\u6bcf\u65e5\u9996\u6b21\u70b9\u8d5e\u6210\u529f\uff0c\u5956\u52b1" + bonus + "\u91d1\u5e01",
													ToastCustom.LENGTH_SHORT ).show();
									}
									adapter.notifyDataSetChanged();
								}
							}

							@Override
							public void onError(int what, String error) {
							}
						}, currentModel.getDynamicModelId(), 1 );
			}
		} else {
			startActivity( new Intent( context, LoginActivity.class ) );
		}
	}

	@Override
	public void onStepOnClick(final DynamicModel model) {
		if (BeanUtils.isLogin()) {
			currentModel = model;
			if (currentModel.isGood()) {
				HttpManager.cancleDynamicsLike( 5585, context, new HttpResultListener() {
					@Override
					public void onSuccess(int what, ResultItem resultItem) {
						if (1 == resultItem.getIntValue( "status" )) {
							model.setGood( false );
							String txt = model.getLikes();
							try {
								int num = Integer.valueOf( txt ).intValue();
								num--;
								model.setLikes( num + "" );
							} catch (NumberFormatException e) {
							}
							ResultItem item = resultItem.getItem( "data" );
							if (2 == item.getIntValue( "operate" )) {
								HttpManager.dynamicsLike( STEPON, context,
										new HttpResultListener() {
											@Override
											public void onSuccess(int what, ResultItem resultItem) {
												if (1 == resultItem.getIntValue( "status" )) {
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
				HttpManager.cancleDynamicsLike( CANCLESTEPON, context,
						new HttpResultListener() {
							@Override
							public void onSuccess(int what, ResultItem resultItem) {
								if (1 == resultItem.getIntValue( "status" )) {
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

							@Override
							public void onError(int what, String error) {
							}
						}, currentModel.getDynamicModelId(), 0 );
			} else {
				HttpManager.dynamicsLike( STEPON, context,
						new HttpResultListener() {
							@Override
							public void onSuccess(int what, ResultItem resultItem) {
								if (1 == resultItem.getIntValue( "status" )) {
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

							@Override
							public void onError(int what, String error) {
							}
						}, currentModel.getDynamicModelId(), 0 );
			}
		} else {
			startActivity( new Intent( context, LoginActivity.class ) );
		}
	}

	@Override
	public void onCommentClick(DynamicModel model) {
		startActivity( new Intent( this, DynamicDetailsActivity.class )
				.putExtra( "dynamicId", model.getDynamicModelId() ) );
	}

	private String currentDynamicId;

	@Override
	public void onShareClick(DynamicModel model) {
		currentDynamicId = model.getDynamicModelId();
		if (sharePopupWindow != null && sharePopupWindow.isShowing()) {
			sharePopupWindow.dismiss();
			sharePopupWindow = null;
		}
		sharePopupWindow = new SharePopupWindow( this, model );
		sharePopupWindow.setPlatformActionListener( this );
		sharePopupWindow.showAtLocation( titleBarView, Gravity.BOTTOM, 0, 0 );
	}

	@Override
	public void onAttentionClick(final DriverModel model) {
		if (BeanUtils.isLogin()) {
			HttpManager.followOrCancel( 188, context, new HttpResultListener() {
				@Override
				public void onSuccess(int what, ResultItem resultItem) {
					if (1 == resultItem.getIntValue( "status" )) {
						model.setAttention( model.isAttention() == 1 ? 0 : 1 );
						adapter.notifyDataSetChanged();
					}
				}

				@Override
				public void onError(int what, String error) {
				}
			}, model.getDriverId(), model.isAttention() == 1 ? 2 : 1 );
		} else {
			startActivity( new Intent( context, LoginActivity.class ) );
		}
	}

	@Override
	public void onHeaderClick(DriverModel model) {
		if (BeanUtils.isLogin()) {
			startActivity( new Intent( this, UserInfoActivity.class )
					.putExtra( "uid", model.getDriverId() ) );
		} else {
			startActivity( new Intent( this, LoginActivity.class ) );
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		startActivity( new Intent( this, DynamicDetailsActivity.class )
				.putExtra( "dynamicId", models.get( position ).getDynamicModelId() ) );
	}


	@Override
	public void onRefresh() {
		hasData = true;
		page = 1;
		requestData( HttpType.REFRESH, page );
		refreshLayout.setOnLoadListener( null );
		refreshLayout.setOnLoadListener( this );
	}

	@Override
	public void onLoad() {
		if (hasData) {
			page++;
			requestData( HttpType.LOADING, page );
		} else {
			refreshLayout.setOnLoadListener( null );
			showToast( "\u5df2\u7ecf\u5230\u5e95\u4e86" );
			refreshLayout.setLoading( false );
		}
	}

	private void requestData(int loading, int page) {
		HttpManager.getDynamics( loading, this, this, page, 3 );
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		cancelProgressDialog();
		refreshLayout.setRefreshing( false );
		refreshLayout.setLoading( false );
		if (1 == resultItem.getIntValue( "status" )) {
			if (what == HttpType.REFRESH) {
				models.clear();
			}
			List<ResultItem> items = resultItem.getItems( "data" );
			if (!BeanUtils.isEmpty( items )) {
				setData( items );
			} else {
				page = 1;
				requestData( HttpType.LOADING, page );
			}
		} else {
			if (HttpType.LOADING == what) {
				page = 1;
				requestData( HttpType.LOADING, page );
			} else {
				showToast( resultItem.getString( "msg" ) );
			}
		}
	}

	@Override
	public void onError(int what, String error) {
		cancelProgressDialog();
		refreshLayout.setRefreshing( false );
		refreshLayout.setLoading( false );
		showToast( error );
	}

	@Override
	public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
		HttpManager.shareDynamics( 15, this, new HttpResultListener() {
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
		ToastCustom.makeText( this, "\u5206\u4eab\u51fa\u9519", ToastCustom.LENGTH_SHORT ).show();
	}

	@Override
	public void onCancel(Platform platform, int i) {
		ToastCustom.makeText( this, "\u5206\u4eab\u53d6\u6d88", ToastCustom.LENGTH_SHORT ).show();
	}


	private void initTitle() {
		titleBarView.setTitleText( "\u7a7f\u8d8a" );
		titleBarView.setLeftImg( R.drawable.icon_back_grey );
		titleBarView.setOnClickListener( R.id.id_titleBar_leftImg, v -> finish() );
	}

	private void initView() {
		adapter = new DynamicAdapter( models, this );
		adapter.setShowAttention( true );
		adapter.setListener( this );
		adapter.setOnHeaderClickListener( this );
		listView.setAdapter( adapter );
		listView.setOnItemClickListener( this );
		requestData( HttpType.REFRESH, page );
		refreshLayout.setOnRefreshListener( this );
		refreshLayout.setOnLoadListener( this );
	}

	private void setData(List<ResultItem> items) {
		if (items.size() > 0) {
			ResultItem resultItem = items.get( 0 );
			ResultItem dynamics = resultItem.getItem( "dynamics" );
			String str = dynamics.getString( "create_time" );
			if (!TextUtils.isEmpty( str )) {
				String t = "";
				try {
					long t1 = Long.valueOf( str );
					t = TimeUtils.formatData( t1 * 1000, "yyyy-MM-dd" );
					timeTv.setText( "\u968f\u673a\u7a7f\u8d8a\u5230~" + t );
				} catch (NumberFormatException e) {
				}
			}
		}
		toDark();
		for (ResultItem item : items) {
			ResultItem dynamics = item.getItem( "dynamics" );
			DynamicModel model = new DynamicModel();
			model.setComments( dynamics.getString( "comment" ) );
			model.setContent( dynamics.getString( "content" ) );
			model.setLikes( dynamics.getString( "likes" ) );
			model.setDislikes( dynamics.getString( "dislike" ) );
			model.setTime( dynamics.getString( "create_time" ) );
			model.setDynamicModelId( dynamics.getString( "id" ) );
			model.setShares( dynamics.getString( "share" ) );
			model.setLeavel( dynamics.getIntValue( "level" ) );
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

	private float alpha = 1.0f;

	private void toDark() {
		new Thread( () -> {
			while (alpha > 0.0f) {
				try {
					Thread.sleep( 30 );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = handler.obtainMessage();
				msg.what = 1;
				alpha -= 0.01f;
				msg.obj = alpha;
				handler.sendMessage( msg );
			}
		} ).start();
	}

	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			float alpha = (float) msg.obj;
			backgroundAlpha( alpha );
			super.dispatchMessage( msg );
		}
	};


	public void backgroundAlpha(float alpha) {
		timeTv.setAlpha( alpha );
	}
}
