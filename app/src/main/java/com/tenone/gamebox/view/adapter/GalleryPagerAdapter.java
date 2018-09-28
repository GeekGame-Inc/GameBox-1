/**
 * Project Name:GameBox
 * File Name:GalleryPagerAdapter.java
 * Package Name:com.tenone.gamebox.view.adapter
 * Date:2017-4-11����9:58:21
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;

public class GalleryPagerAdapter extends PagerAdapter {

    List<String> imgUrls;
    Context mContext;
    ImageClickListener listener;

    public GalleryPagerAdapter(List<String> list, Context cxt) {
        this.imgUrls = list;
        this.mContext = cxt;
    }

    @Override
    public int getCount() {
        return imgUrls.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @SuppressWarnings("deprecation")
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = new ImageView( mContext );
        imageView.setScaleType( ImageView.ScaleType.FIT_XY );
        imageView.setBackgroundColor( mContext.getResources().getColor(
                R.color.gameImgColor ) );
        container.addView( imageView );
        ImageLoadUtils.loadGameDetailsImg( imageView, mContext,
                imgUrls.get( position ) );
        imageView.setOnClickListener( v -> {
            if (listener != null) {
                listener.onImageClick( imgUrls.get( position ) );
            }
        } );
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView( (ImageView) object );
    }

    public void setImageClickListener(ImageClickListener listener) {
        this.listener = listener;
    }

    public interface ImageClickListener {
        void onImageClick(String url);
    }



}
