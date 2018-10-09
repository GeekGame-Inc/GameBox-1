package com.tenone.gamebox.presenter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.GameDetailsFragmentBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnDynamicCommentItemClickListener;
import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.mode.mode.DynamicCommentModel;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.GameDetailsFragmentView;
import com.tenone.gamebox.view.activity.BrowseImageActivity;
import com.tenone.gamebox.view.activity.CheckAllCommentActivity;
import com.tenone.gamebox.view.activity.LoginActivity;
import com.tenone.gamebox.view.activity.NewGameDetailsActivity;
import com.tenone.gamebox.view.activity.PublishGameCommentActivity;
import com.tenone.gamebox.view.adapter.CommentFragmentAdapter;
import com.tenone.gamebox.view.adapter.GalleryPagerAdapter.ImageClickListener;
import com.tenone.gamebox.view.adapter.GalleryRecyclerAdapter;
import com.tenone.gamebox.view.adapter.RecommendGameAdapter;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.MoreTextView;
import com.tenone.gamebox.view.custom.MyGridView;
import com.tenone.gamebox.view.custom.MyListView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.NetworkUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class GameDetailsFragmentPresenter extends BasePresenter implements
		OnClickListener, ImageClickListener, HttpResultListener, OnDynamicCommentItemClickListener, GalleryRecyclerAdapter.ImageClickListener {
	private GameDetailsFragmentBiz fragmentBiz;
	private GameDetailsFragmentView fragmentView;
	private Context context;
	private GalleryRecyclerAdapter galleryAdapter;
	private RecommendGameAdapter gameAdapter;
	private ArrayList<String> imageArray = new ArrayList<String>();
	private List<GameModel> games = new ArrayList<GameModel>();
	private CommentFragmentAdapter mAdapter;
	private List<DynamicCommentModel> models = new ArrayList<DynamicCommentModel>();
	private boolean isPlay = false;
	private int width, height;
	private String gifUrl;
	private DynamicCommentModel commentModel;

	public GameDetailsFragmentPresenter(Context cxt, GameDetailsFragmentView v) {
		this.context = cxt;
		this.fragmentView = v;
		this.fragmentBiz = new GameDetailsFragmentBiz();
	}

	public void setAdapter() {
		if (gameAdapter == null) {
			gameAdapter = new RecommendGameAdapter( games, context );
		}
		getGridView().setAdapter( gameAdapter );
		if (mAdapter == null) {
			mAdapter = new CommentFragmentAdapter( models, context );
			mAdapter.setOnDynamicCommentItemClickListener( this );
		}
		getCommentListView().setAdapter( mAdapter );
	}


	public void initView() {
		this.imageArray = getImgUrls();
		this.games = getRecommend();
		ViewGroup.LayoutParams params = getImgsView().getLayoutParams();
		params.height = DisplayMetricsUtils.getScreenHeight( context ) / 3;
		getImgsView().setLayoutParams( params );
		LinearLayoutManager manager = new LinearLayoutManager( context, LinearLayoutManager.HORIZONTAL, false );
		getImgsView().setLayoutManager( manager );
		int imgWidth = DisplayMetricsUtils.getScreenWidth( context ) / 5 * 2 + DisplayMetricsUtils.dipTopx( context, 10 );
		int imgHeight = imgWidth * 16 / 9 + DisplayMetricsUtils.dipTopx( context, 2 );
		ViewGroup.LayoutParams params1 = getImgsView().getLayoutParams();
		params1.height = imgHeight;
		getImgsView().setLayoutParams( params1 );
		galleryAdapter = new GalleryRecyclerAdapter( imageArray, context, imgWidth, imgHeight );
		getImgsView().setAdapter( galleryAdapter );
		getMoreTextView().setText( getIntroText() );
		getFeatureMoreTextView().setText( getFeatureText() );
		String rebate = getRebateText();
		if (!TextUtils.isEmpty( rebate )) {
			getRebateLayout().setVisibility( View.VISIBLE );
			getRebateMoreTextView().setText( getRebateText() );
		} else {
			getRebateLayout().setVisibility( View.GONE );
		}
		String vip = getVipText();
		if (!TextUtils.isEmpty( vip )) {
			getVipLayout().setVisibility( View.VISIBLE );
			getVipMoreTextView().setText( vip );
		} else {
			getVipLayout().setVisibility( View.GONE );
		}
		gifUrl = getGifUrl();
		if (!TextUtils.isEmpty( gifUrl )) {
			if (isPortrait()) {
				width = (DisplayMetricsUtils.getScreenWidth( context ) - DisplayMetricsUtils
						.dipTopx( context, 20 )) / 7 * 3;
				height = 16 * width / 9;
			} else {
				width = (DisplayMetricsUtils.getScreenWidth( context ) - DisplayMetricsUtils
						.dipTopx( context, 20 ));
				height = 9 * width / 16;
			}
			gifUrl = MyApplication.getHttpUrl().getBaseUrl() + gifUrl;
			playGif();
			/*getGifPalyImageView().setOnClickListener( v -> {
				playGif();
				*//*if (!isPlay) {
					if ("WIFI".equals( NetworkUtils.GetNetworkType( context ) )) {
						playGif();
					} else {
						showWifiDialog();
					}
				}*//*
			} );*/
		} else {
			getGifLayout().setVisibility( View.GONE );
		}
		getCheckTv().setOnClickListener( v -> openOtherActivity( context, new Intent( context, CheckAllCommentActivity.class )
					.putExtra( "gameId", getGameId() ) ) );
	}

	private void playGif() {
		//getGifPalyImageView().setVisibility( View.GONE );
		ImageLoadUtils.loadGifImg( getGifImageView(), context, gifUrl, new RequestListener<GifDrawable>() {
			@Override
			public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target,
																	boolean isFirstResource) {
				return false;
			}
			@Override
			public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target,
																		 DataSource dataSource, boolean isFirstResource) {
			//	getGifPalyImageView().setVisibility( View.GONE );
				return false;
			}
		}, width, height );
//	  ImageLoadUtils.loadGifPlayImg( getGifPalyImageView(), context );
		isPlay = true;
	}

	private void showWifiDialog() {
		Builder builder = new Builder( context );
		builder.setTitle( context.getResources().getString( R.string.is4G ) );
		builder.setMessage( context.getResources().getString( R.string.not_wifi_text ) );
		builder.setNegativeButton( context.getResources().getString( R.string.go_on_play ), (dialog, which) -> {
			dialog.dismiss();
			playGif();
		} );
		builder.setPositiveButton( context.getResources().getString( R.string.no_play ), (dialog, which) -> {
			dialog.dismiss();
		} );
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.getButton( AlertDialog.BUTTON_POSITIVE ).setTextColor( Color.BLACK );
		dialog.getButton( DialogInterface.BUTTON_NEGATIVE ).setTextColor( Color.BLACK );
	}

	public void initListener() {
		HttpManager.gameCommentList( HttpType.REFRESH, context, this, getGameId(), 2, 1 );
		getGridView().setOnItemClickListener( (parent, view, position, id) -> {
			openOtherActivity( context, new Intent( context, NewGameDetailsActivity.class )
					.putExtra( "id", games.get( position ).getGameId() + "" ) );
			close( context );
		} );
		getPublishBt().setOnClickListener( this );
		galleryAdapter.setImageClickListener( this );
	}

	private RecyclerView getImgsView() {
		return fragmentView.getImgsView();
	}

	private MyGridView getGridView() {
		return fragmentView.getGridView();
	}

	private MoreTextView getMoreTextView() {
		return fragmentView.getMoreTextView();
	}

	private MoreTextView getFeatureMoreTextView() {
		return fragmentView.getFeatureMoreTextView();
	}

	private MoreTextView getRebateMoreTextView() {
		return fragmentView.getRebateMoreTextView();
	}

	private MoreTextView getVipMoreTextView() {
		return fragmentView.getVipMoreTextView();
	}

	private RelativeLayout getRebateLayout() {
		return fragmentView.getRebateLayout();
	}

	private NestedScrollView getNestedScrollView() {
		return fragmentView.getNestedScrollView();
	}

	private TextView getCheckTv() {
		return fragmentView.getCheckTv();
	}

	private LinearLayout getVipLayout() {
		return fragmentView.getVipLayout();
	}

	private LinearLayout getGifLayout() {
		return fragmentView.getGifLayout();
	}

	private ResultItem getResultItem() {
		return fragmentView.getResultItem();
	}

	private TextView getPublishBt() {
		return fragmentView.getPublishBt();
	}

	private MyListView getCommentListView() {
		return fragmentView.getCommentListView();
	}

	private ImageView getGifImageView() {
		return fragmentView.getGifImageView();
	}

	private ImageView getGifPalyImageView() {
		return fragmentView.getGifPalyImageView();
	}

	private String getGameId() {
		return fragmentView.getGameId();
	}

	private ArrayList<String> getImgUrls() {
		return fragmentBiz.getImgUrls( getResultItem() );
	}

	private String getFeatureText() {
		return fragmentBiz.getFeatureText( getResultItem() );
	}

	private String getIntroText() {
		return fragmentBiz.getIntroText( getResultItem() );
	}

	private String getRebateText() {
		return fragmentBiz.getRebateText( getResultItem() );
	}

	private String getVipText() {
		return fragmentBiz.getVipText( getResultItem() );
	}

	private String getGifUrl() {
		return fragmentBiz.getGifUrl( getResultItem() );
	}

	private List<GameModel> getRecommend() {
		return fragmentBiz.getRecommend( getResultItem() );
	}

	private boolean isPortrait() {
		return fragmentBiz.isPortrait( getResultItem() );
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.id_fragment_details_publishComment:
				toComment();
				break;
		}
	}

	private void toComment() {
		if (!BeanUtils.isLogin()) {
			openOtherActivity( context, new Intent( context, LoginActivity.class ) );
			return;
		}
		buildProgressDialog( context );
		HttpManager.userLoginApp( 0, context, new HttpResultListener() {
			@Override
			public void onSuccess(int what, ResultItem resultItem) {
				cancelProgressDialog();
				if (1 == resultItem.getIntValue( "status" )) {
					openOtherActivity(
							context,
							new Intent( context, PublishGameCommentActivity.class )
									.putExtra( "model", commentModel ).putExtra( "gameId",
									getGameId() ) );
				} else {
					ToastCustom.makeText( context, resultItem.getString( "msg" ), ToastCustom.LENGTH_SHORT ).show();
				}
			}

			@Override
			public void onError(int what, String error) {
				cancelProgressDialog();
				ToastCustom.makeText( context, error, ToastCustom.LENGTH_SHORT ).show();
			}
		}, getGameId() );
	}

	@Override
	public void onImageClick(String url) {
		openOtherActivity(
				context,
				new Intent( context, BrowseImageActivity.class )
						.putStringArrayListExtra( "urls", imageArray ).putExtra(
						"url", url ) );
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		if (1 == resultItem.getIntValue( "status" )) {
			ResultItem item = resultItem.getItem( "data" );
			if (!BeanUtils.isEmpty( item )) {
				setData( item );
			}
		}
	}

	@Override
	public void onError(int what, String error) {
	}

	public void setData(ResultItem resultItem) {
		List<ResultItem> comments = resultItem.getItems( "hot_list" );
		if (null != comments) {
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
			mAdapter.notifyDataSetChanged();
		}
		getNestedScrollView().scrollTo( 0, 0 );
	}

	@Override
	public void onCommentClick(DynamicCommentModel model) {
		if (!BeanUtils.isLogin()) {
			openOtherActivity( context, new Intent( context, LoginActivity.class ) );
			return;
		}
		if (MyApplication.getInstalledGameIds().contains( getGameId() )) {
			openOtherActivity(
					context,
					new Intent( context, PublishGameCommentActivity.class )
							.putExtra( "model", model )
							.putExtra( "gameId", getGameId() ) );
		} else {
			ToastCustom.makeText( context, "\u8bf7\u5148\u4e0b\u8f7d\u5e76\u5b89\u88c5\u6e38\u620f", ToastCustom.LENGTH_SHORT ).show();
		}

	}

	@Override
	public void onPraiseClick(DynamicCommentModel model) {
		if (!BeanUtils.isLogin()) {
			openOtherActivity( context, new Intent( context, LoginActivity.class ) );
			return;
		}
		if (!(model.getLikeTy() == 1)) {
			HttpManager.commentLike( 2500, context, new HttpResultListener() {
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
							mAdapter.notifyDataSetChanged();
					}
					ToastCustom.makeText( context,
							resultItem.getString( "msg" ), ToastCustom.LENGTH_SHORT ).show();
				}

				@Override
				public void onError(int what, String error) {
					ToastCustom.makeText( context, error, ToastCustom.LENGTH_SHORT ).show();
				}
			}, model.getCommentId(), 1 );
		} else {
			ToastCustom.makeText( context, "\u60a8\u5df2\u7ecf\u8d5e\u8fc7\u4e86", ToastCustom.LENGTH_SHORT ).show();
		}
	}

	@Override
	public void onHeaderClick(DynamicCommentModel model) {

	}

	@Override
	public void onDeleteClick(DynamicCommentModel model) {
		if (!BeanUtils.isLogin()) {
			openOtherActivity( context, new Intent( context, LoginActivity.class ) );
			return;
		}
		HttpManager.deleteComment( 2006, context, new HttpResultListener() {
			@Override
			public void onSuccess(int what, ResultItem resultItem) {
				if (1 == resultItem.getIntValue( "status" )) {
					if (models.indexOf( model ) > -1) {
						models.remove( model );
						mAdapter.notifyDataSetChanged();
					}
				}
				ToastCustom.makeText( context,
						resultItem.getString( "msg" ), ToastCustom.LENGTH_SHORT ).show();
			}

			@Override
			public void onError(int what, String error) {
				ToastCustom.makeText( context, error, ToastCustom.LENGTH_SHORT ).show();
			}
		}, model.getCommentId() );
	}
}
