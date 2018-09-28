/**
 * Project Name:GameBox
 * File Name:BrowseImageActivity.java
 * Package Name:com.tenone.gamebox.view.activity
 * Date:2017-4-26����10:42:28
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.BrowseImageView;
import com.tenone.gamebox.presenter.BrowseImagePresenter;
import com.tenone.gamebox.view.utils.GetImageCacheAsyncTask;
import com.tenone.gamebox.view.utils.SystemBarUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ResourceAsColor")
public class BrowseImageActivity extends AppCompatActivity implements
        BrowseImageView, ViewPager.OnPageChangeListener {

    @ViewInject(R.id.id_browse_viewpager)
    ViewPager viewPager;
    @ViewInject(R.id.id_browse_page)
    TextView pageTv;

    BrowseImagePresenter presenter;
    private List<String> imgUrls = new ArrayList<String>();
    private String currentUrl;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_browse_image );
        ViewUtils.inject( this );
        SystemBarUtils.compat( this, R.color.titleBarColor, false );
        presenter = new BrowseImagePresenter( this, this );
        presenter.initView();
        presenter.setAdapter();
        imgUrls.clear();
        imgUrls.addAll( presenter.getImageUrls() );
        currentUrl = presenter.getCurrentUrl();
        pageTv.setText( (imgUrls.indexOf( currentUrl ) + 1) + "/" + imgUrls.size() );
        viewPager.setOnPageChangeListener( this );
    }

    @Override
    public ViewPager getViewPager() {
        return viewPager;
    }

    @OnClick({R.id.id_browse_save})
    public void onClick(View view) {
        if (view.getId() == R.id.id_browse_save) {
            new GetImageCacheAsyncTask( this ).execute( currentUrl );
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentUrl = imgUrls.get( position );
        pageTv.setText( (position + 1) + "/" + imgUrls.size() );
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
