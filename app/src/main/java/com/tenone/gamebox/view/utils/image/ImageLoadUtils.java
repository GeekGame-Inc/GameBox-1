/**
 * Project Name:GameBox
 * File Name:ImageLoadUtils.java
 * Package Name:com.tenone.gamebox.view.utils
 * Date:2017-3-14����10:31:48
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.utils.image;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.ImageViewState;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

import java.io.File;

@SuppressLint("NewApi")
public class ImageLoadUtils {

	public static void loadNormalImg(ImageView img, Context cxt, String url) {
		if (cxt != null && !((Activity) cxt).isFinishing()
				&& !((Activity) cxt).isDestroyed()) {
			RequestOptions options = new RequestOptions();
			options.placeholder( R.drawable.ic_loading_logo );
			options.error( R.drawable.ic_logo_failure );
			Glide.with( cxt ).load( url ).thumbnail( 0.1f ).apply( options ).into( img );
		}
	}

	public static void loadCommitmentImg(ImageView img, Context cxt, String url) {
		if (cxt != null && !((Activity) cxt).isFinishing()
				&& !((Activity) cxt).isDestroyed()) {
			RequestOptions options = new RequestOptions();
			options.placeholder( R.drawable.icon_chengnuo );
			options.error( R.drawable.icon_chengnuo );
			Glide.with( cxt ).load( url )
					.thumbnail( 0.1f ).transition( GenericTransitionOptions.with( R.anim.item_alpha_in ) )
					.apply( options )
					.into( img );

		}
	}

	public static void loadImg(ImageView img, Context cxt, String url) {
		if (cxt != null && !((Activity) cxt).isFinishing()
				&& !((Activity) cxt).isDestroyed()) {
			RequestOptions options = new RequestOptions();
			options.placeholder( R.drawable.icon_banner_loading );
			options.error( R.drawable.ic_banner_failure );
			Glide.with( cxt ).load( url )
					.thumbnail( 0.1f )
					.apply( options )
					.transition( GenericTransitionOptions.with( R.anim.item_alpha_in ) )
					.into( img );
		}
	}

	public static void loadGifImg(ImageView img, Context cxt, String url) {
		if (cxt != null && !((Activity) cxt).isFinishing()
				&& !((Activity) cxt).isDestroyed()) {
			RequestOptions options = new RequestOptions();
			options.placeholder( R.drawable.icon_banner_loading );
			options.error( R.drawable.ic_banner_failure );
			Glide.with( cxt ).asGif().load( url )
					.thumbnail( 0.1f )
					.apply( options )
					.transition( GenericTransitionOptions.with( R.anim.item_alpha_in ) )
					.into( img );
		}
	}

	public static void loadGifImg(ImageView img, Context cxt, String url,
																RequestListener<GifDrawable> listener, int width, int height) {
		if (cxt != null && !((Activity) cxt).isFinishing()
				&& !((Activity) cxt).isDestroyed()) {
			RequestOptions options = new RequestOptions();
			options.placeholder( R.drawable.icon_banner_loading );
			options.error( R.drawable.ic_banner_failure );
			options.override( width, height );
			options.diskCacheStrategy( DiskCacheStrategy.RESOURCE );
			Glide.with( cxt ).asGif().load( url )
					.apply( options )
					.transition( GenericTransitionOptions.with( R.anim.item_alpha_in ) )
					.listener( listener ).into( img );
		}
	}

	public static void loadGifPlayImg(ImageView img, Context cxt) {
		if (cxt != null && !((Activity) cxt).isFinishing()
				&& !((Activity) cxt).isDestroyed()) {
			Glide.with( cxt ).asGif().load( R.drawable.ic_loading_gif )
					.transition( GenericTransitionOptions.with( R.anim.item_alpha_in ) )
					.into( img );
		}
	}


	public static void loadBannerImg(ImageView img, Context cxt, String url) {
		if (cxt != null && !((Activity) cxt).isFinishing()
				&& !((Activity) cxt).isDestroyed()) {
			RequestOptions options = new RequestOptions();
			options.placeholder( R.drawable.icon_banner_loading );
			options.error( R.drawable.ic_banner_failure );
			options.diskCacheStrategy( DiskCacheStrategy.RESOURCE );
			Glide.with( cxt ).load( url )
					.apply( options )
					.thumbnail( 0.1f )
					.transition( GenericTransitionOptions.with( R.anim.item_alpha_in ) )
					.into( img );
		}
	}

	public static void loadGameDetailsImg(ImageView img, Context cxt, String url) {
		if (cxt != null && !((Activity) cxt).isFinishing()
				&& !((Activity) cxt).isDestroyed()) {
			RequestOptions options = new RequestOptions();
			options.placeholder( R.drawable.icon_details_loading );
			options.error( R.drawable.ic_game_failure );
			options.diskCacheStrategy( DiskCacheStrategy.RESOURCE );
			Glide.with( cxt ).load( url )
					.apply( options )
					.transition( GenericTransitionOptions.with( R.anim.item_alpha_in ) )
					.thumbnail( 0.1f )
					.into( img );
		}
	}

	public static void loadRoundGameDetailsImg(ImageView img, Context cxt, String url, int radius) {
		if (cxt != null && !((Activity) cxt).isFinishing()
				&& !((Activity) cxt).isDestroyed()) {
			Glide.with( cxt ).load( url )
					.apply( new RequestOptions()
							.transforms(new GlideRoundTransform( cxt, radius ) )
							.placeholder( R.drawable.icon_details_loading )
							.error( R.drawable.ic_game_failure )
							.diskCacheStrategy( DiskCacheStrategy.RESOURCE ) )
					.thumbnail( 0.1f )
					.transition( GenericTransitionOptions.with( R.anim.item_alpha_in ) )
					.into( img );
		}
	}

	public static void loadNormalImg(Context context, String url, int erroImg,
																	 int emptyImg, ImageView iv) {
		if (context != null && !((Activity) context).isFinishing()
				&& !((Activity) context).isDestroyed()) {
			RequestOptions options = new RequestOptions();
			options.placeholder( R.drawable.ic_loading_logo );
			options.error( erroImg );
			options.diskCacheStrategy( DiskCacheStrategy.RESOURCE );
			Glide.with( context ).load( url )
					.apply( options )
					.thumbnail( 0.1f )
					.transition( GenericTransitionOptions.with( R.anim.item_alpha_in ) )
					.into( iv );
		}
	}

	public static void loadNormalImg(Context context, String url, int erroImg,
																	 ImageView iv) {
		if (context != null && !((Activity) context).isFinishing()
				&& !((Activity) context).isDestroyed()) {
			RequestOptions options = new RequestOptions();
			options.placeholder( R.drawable.ic_loading_logo );
			options.error( erroImg );
			options.diskCacheStrategy( DiskCacheStrategy.RESOURCE );
			Glide.with( context ).load( url )
					.apply( options )
					.thumbnail( 0.1f )
					.transition( GenericTransitionOptions.with( R.anim.item_alpha_in ) )
					.into( iv );
		}
	}


	public static void loadMessageImg(final Context context, String url, int erroImg,
																		final ImageView iv) {
		if (context != null && !((Activity) context).isFinishing()
				&& !((Activity) context).isDestroyed()) {
			RequestOptions options = new RequestOptions();
			options.placeholder( R.drawable.ic_loading_logo );
			options.error( erroImg );
			options.diskCacheStrategy( DiskCacheStrategy.RESOURCE );
			Glide.with( context ).asBitmap().load( url )
					.apply( options )
					.thumbnail( 0.1f )
					.transition( GenericTransitionOptions.with( R.anim.item_alpha_in ) )
					.into( new SimpleTarget<Bitmap>() {
						@Override
						public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
							int width = bitmap.getWidth();
							int height = bitmap.getHeight();
							int scWidth = DisplayMetricsUtils.getScreenWidth( context );
							int ivheigth = 0;
							ivheigth = scWidth * height / width;
							ViewGroup.LayoutParams params = iv.getLayoutParams();
							params.width = scWidth;
							params.height = ivheigth;
							iv.setLayoutParams( params );
							iv.setImageBitmap( bitmap );
						}
					} );
		}
	}


	/**
	 * ����Բ��ͼƬ loadCircleImg:(������һ�仰�����������������). <br/>
	 *
	 * @param context
	 * @param url
	 * @param iv
	 * @author John Lie
	 * @since JDK 1.6
	 */
	public static void loadCircleImg(Context context, String url, ImageView iv) {
		if (context != null && !((Activity) context).isFinishing()
				&& !((Activity) context).isDestroyed()) {
			RequestOptions options = new RequestOptions();
			options.placeholder( R.drawable.ic_loading_logo );
			options.error( R.drawable.ic_logo_failure );
			options.diskCacheStrategy( DiskCacheStrategy.RESOURCE );
			options.transform( new GlideCircleTransform( context ) );
			Glide.with( context ).load( url )
					.apply( options )
					.thumbnail( 0.1f )
					.transition( GenericTransitionOptions.with( R.anim.item_alpha_in ) )
					.into( iv );
		}
	}


	public static void loadLocImg(Context context, String path, ImageView iv) {
		if (context != null && !((Activity) context).isFinishing()
				&& !((Activity) context).isDestroyed()) {
			RequestOptions options = new RequestOptions();
			options.placeholder( R.drawable.ic_loading_logo );
			options.error( R.drawable.ic_logo_failure );
			File file = new File( path );
			Glide.with( context ).load( file )
					.apply( options )
					.thumbnail( 0.1f )
					.transition( GenericTransitionOptions.with( R.anim.item_alpha_in ) )
					.into( iv );
		}
	}

	public static void loadDraImg(Context context, ImageView iv, int rId) {
		if (context != null && !((Activity) context).isFinishing()
				&& !((Activity) context).isDestroyed()) {
			Glide.with( context ).asBitmap().load( rId )
					.thumbnail( 0.1f )
					.transition( GenericTransitionOptions.with( R.anim.item_alpha_in ) )
					.into( iv );
		}
	}

	/**
	 * ���س�ͼ
	 *
	 * @param context
	 * @param url
	 * @param imageView
	 */
	public static void loadLongImg(Context context, String url, final SubsamplingScaleImageView imageView) {
		//����ͼƬ���浽����
		Glide.with( context )
				.load( url )
				.downloadOnly( new SimpleTarget<File>() {
					@Override
					public void onResourceReady(File resource, Transition<? super File> transition) {
						// �������ͼƬ��ַ��SubsamplingScaleImageView,����ע������ImageViewState���ó�ʼ��ʾ����
						imageView.setImage( ImageSource.uri( Uri.fromFile( resource ) ),
								new ImageViewState( 1.0F, new PointF( 0, 0 ), 0 ) );
					}
				} );
	}

	public static void loadIntoUseFitWidth(Context context, final String imageUrl, int errorImageId, int placeholderImageId, final ImageView imageView) {
		RequestOptions options = new RequestOptions();
		options.placeholder( placeholderImageId );
		options.error( errorImageId );
		options.diskCacheStrategy( DiskCacheStrategy.RESOURCE );
		Glide.with( context )
				.load( imageUrl )
				.listener( new RequestListener<Drawable>() {
					@Override
					public boolean onLoadFailed(@Nullable GlideException e, Object model,
																			Target<Drawable> target, boolean isFirstResource) {
						return false;
					}

					@Override
					public boolean onResourceReady(Drawable resource, Object model,
																				 Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
						if (imageView == null) {
							return false;
						}
						if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
							imageView.setScaleType( ImageView.ScaleType.FIT_XY );
						}
						ViewGroup.LayoutParams params = imageView.getLayoutParams();
						int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
						float scale = (float) vw / (float) resource.getIntrinsicWidth();
						int vh = Math.round( resource.getIntrinsicHeight() * scale );
						params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
						imageView.setLayoutParams( params );
						return false;
					}
				} )
				.into( imageView );
	}
}
