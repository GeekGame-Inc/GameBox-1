package com.tenone.gamebox.mode.mode;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.tenone.gamebox.view.utils.BGAGlideImageloader;
import com.tenone.gamebox.view.utils.BGAImageLoader;
import com.tenone.gamebox.view.utils.BGAPicassoImageLoader;


public class BGAImage {
    private static BGAImageLoader sImageLoader;

    private BGAImage() {
    }

    private static final BGAImageLoader getImageLoader() {
        if (sImageLoader == null) {
            synchronized (BGAImage.class) {
                if (sImageLoader == null) {
                    if (isClassExists( "com.bumptech.glide.Glide" )) {
                        sImageLoader = new BGAGlideImageloader();
                    } else if (isClassExists( "com.squareup.picasso.Picasso" )) {
                        sImageLoader = new BGAPicassoImageLoader();
                    } else {
                        throw new RuntimeException( "must have one of 「Glide、Picasso、universal-image-loader、XUtils3" );
                    }
                }
            }
        }
        return sImageLoader;
    }

    private static final boolean isClassExists(String classFullName) {
        try {
            Class.forName( classFullName );
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static void displayImage(Activity activity, ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId,
                                    int width, int height, final BGAImageLoader.DisplayDelegate delegate) {
        getImageLoader().displayImage( activity, imageView, path, loadingResId, failResId, width, height, delegate );
    }

    public static void downloadImage(Context context, String path, final BGAImageLoader.DownloadDelegate delegate) {
        getImageLoader().downloadImage( context, path, delegate );
    }
}
