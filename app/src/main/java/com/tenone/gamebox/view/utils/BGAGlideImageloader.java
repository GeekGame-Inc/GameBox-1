package com.tenone.gamebox.view.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

/**
 * Created by Eddy on 2018/1/4.
 */

public class BGAGlideImageloader extends BGAImageLoader {

    @Override
    public void displayImage(Activity activity, final ImageView imageView, String path, @DrawableRes int loadingResId,
                             @DrawableRes int failResId, int width, int height, final DisplayDelegate delegate) {
        final String finalPath = getPath( path );
        RequestOptions options = new RequestOptions();
        options.placeholder( loadingResId );
        options.error( failResId );
        options.override( width, height );

        Glide.with( activity ).asBitmap().load( finalPath ).listener( new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource,
                                           boolean isFirstResource) {
                if (delegate != null) {
                    delegate.onSuccess( imageView, finalPath );
                }
                return false;
            }
        } ).into( imageView );
    }

    @Override
    public void downloadImage(Context context, String path, final DownloadDelegate delegate) {
        final String finalPath = getPath( path );
        Glide.with( context.getApplicationContext() ).asBitmap().load( finalPath ).into( new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                if (delegate != null) {
                    delegate.onSuccess( finalPath, resource );
                }
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                if (delegate != null) {
                    delegate.onFailed( finalPath );
                }
            }
        } );
    }
}
