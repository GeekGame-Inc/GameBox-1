/**
 * Project Name:GameBox
 * File Name:GlideRoundTransform.java
 * Package Name:com.tenone.gamebox.view.utils.image
 * Date:2017-3-14����10:38:12
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

import java.nio.ByteBuffer;
import java.security.MessageDigest;

/**
 * ��ͼƬת��ΪԲ�� �����еڶ�����������뾶 ClassName:GlideRoundTransform <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-14 ����10:38:12 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */
public class GlideRoundTransform extends BitmapTransformation {
	private static final String ID = "com.tenone.gamebox.view.utils.image.GlideRoundTransform";
	private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
	private static int radius = 0;
	public GlideRoundTransform(Context context) {
		this( context, 4 );
	}

	public GlideRoundTransform(Context context, int dp) {
		super( context );
		GlideRoundTransform.radius = DisplayMetricsUtils.dipTopx( context, dp );
		Log.i( "GlideRoundTransform", "GlideRoundTransform " );
	}

	@Override
	protected Bitmap transform(BitmapPool pool, Bitmap toTransform,
														 int outWidth, int outHeight) {
		Log.i( "GlideRoundTransform", "transform outWidth is " + outWidth + "  outHeight is " + outHeight );
		Bitmap bitmap = TransformationUtils.centerCrop( pool, toTransform, outWidth, outHeight );
		return roundCrop( pool, bitmap );
	}

	/**
	 * Բ��
	 *
	 * @param pool
	 * @param source
	 * @return
	 */
	private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
		if (source == null)
			return null;
		Bitmap result = pool.get( source.getWidth(), source.getHeight(),
				Bitmap.Config.ARGB_8888 );
		if (result == null) {
			result = Bitmap.createBitmap( source.getWidth(), source.getHeight(),
					Bitmap.Config.ARGB_8888 );
		}

		Canvas canvas = new Canvas( result );
		Paint paint = new Paint();
		paint.setShader( new BitmapShader( source, BitmapShader.TileMode.CLAMP,
				BitmapShader.TileMode.CLAMP ) );
		paint.setAntiAlias( true );
		RectF rectF = new RectF( 0f, 0f, source.getWidth(), source.getHeight() );
		Log.i( "GlideRoundTransform", " width is " + source.getWidth() + "  height is " + source.getHeight() );
		canvas.drawRoundRect( rectF, radius, radius, paint );
		return result;
	}

	/**
	 * Բ��
	 *
	 * @param pool
	 * @param source
	 * @return
	 */
	private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
		if (source == null) return null;
		int size = Math.min( source.getWidth(), source.getHeight() );
		int x = (source.getWidth() - size) / 2;
		int y = (source.getHeight() - size) / 2;
		// TODO this could be acquired from the pool too
		Bitmap squared = Bitmap.createBitmap( source, x, y, size, size );
		Bitmap result = pool.get( size, size, Bitmap.Config.ARGB_8888 );
		if (result == null) {
			result = Bitmap.createBitmap( size, size, Bitmap.Config.ARGB_8888 );
		}
		Canvas canvas = new Canvas( result );
		Paint paint = new Paint();
		paint.setShader( new BitmapShader( squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP ) );
		paint.setAntiAlias( true );
		float r = size / 2f;
		canvas.drawCircle( r, r, r, paint );
		return result;
	}

	/**
	 * Adds all uniquely identifying information to the given digest.
	 * <p>
	 * <p> Note - Using {@link MessageDigest#reset()} inside of this method will result
	 * in undefined behavior. </p>
	 *
	 * @param messageDigest
	 */
	@Override
	public void updateDiskCacheKey(MessageDigest messageDigest) {
		messageDigest.update(ID_BYTES);
		byte[] radiusData = ByteBuffer.allocate(4).putInt(radius).array();
		messageDigest.update(radiusData);
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals( obj );
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}