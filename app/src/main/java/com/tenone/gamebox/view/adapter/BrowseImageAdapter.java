/**
 * Project Name:GameBox
 * File Name:GalleryPagerAdapter.java
 * Package Name:com.tenone.gamebox.view.adapter
 * Date:2017-4-11����9:58:21
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class BrowseImageAdapter extends PagerAdapter {

    List<String> imgUrls = new ArrayList<String>();
    Context mContext;
    LayoutInflater mInflater;
    ImageViewClickListener listener;

    public BrowseImageAdapter(List<String> list, Context cxt) {
        this.imgUrls = list;
        this.mContext = cxt;
        this.mInflater = LayoutInflater.from( mContext );
    }

    @Override
    public int getCount() {
        return imgUrls.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate( R.layout.item_browse_image, container,
                false );
        container.addView( view );
        SubsamplingScaleImageView imageView = view
                .findViewById( R.id.id_beowse_image );
        ImageView imageView1 = view.findViewById( R.id.id_beowse_image2 );
        String url = imgUrls.get( position );
        if (!url.contains( ".gif" ) && !url.contains( ".GIF" )) {
            imageView.setMaxScale( 10 );
            imageView.setMinScale( 0 );
            imageView.setScaleAndCenter( 0, new PointF( imageView.getWidth() / 2, imageView.getWidth() / 2 ) );
            imageView.invalidate();
            imageView.setVisibility( View.VISIBLE );
            imageView1.setVisibility( View.GONE );
            ImageLoadUtils.loadLongImg( mContext,
                    imgUrls.get( position ), imageView );
            imageView.setOnClickListener( v -> {
                if (listener != null) {
                    listener.onImageClick();
                }
            } );
        } else {
            imageView.setVisibility( View.GONE );
            imageView1.setVisibility( View.VISIBLE );
            ImageLoadUtils.loadGifImg( imageView1, mContext, url );
            imageView1.setOnClickListener( v -> {
                if (listener != null) {
                    listener.onImageClick();
                }
            } );
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView( (RelativeLayout) object );
    }

    public void setImageViewClickListener(ImageViewClickListener l) {
        this.listener = l;
    }

    public interface ImageViewClickListener {
        void onImageClick();
    }
}
