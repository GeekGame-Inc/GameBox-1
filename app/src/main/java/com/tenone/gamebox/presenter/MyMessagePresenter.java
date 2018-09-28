/**
 * Project Name:GameBox
 * File Name:MyMessagePresenter.java
 * Package Name:com.tenone.gamebox.presenter
 * Date:2017-3-22����2:34:28
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.MyMessageBiz;
import com.tenone.gamebox.mode.view.MyMessageView;
import com.tenone.gamebox.view.adapter.ManagementAdapter;
import com.tenone.gamebox.view.custom.CustomerUnderlinePageIndicator;
import com.tenone.gamebox.view.custom.NoScrollViewPager;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;
import com.tenone.gamebox.view.custom.xbanner.transformers.BasePageTransformer;
import com.tenone.gamebox.view.custom.xbanner.transformers.Transformer;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

import java.util.List;

public class MyMessagePresenter extends BasePresenter implements
        OnClickListener {
    MyMessageBiz messageBiz;
    MyMessageView messageView;
    Context mContext;
    ManagementAdapter mAdapter;
    private float width = 0, widthOffset = 0;
    private int position = 0;

    public MyMessagePresenter(MyMessageView v, Context cxt, int p) {
        this.mContext = cxt;
        this.position = p;
        this.messageBiz = new MyMessageBiz();
        this.messageView = v;
        Intent mIntent = new Intent();
        mIntent.setAction( "open_notification" );
        mIntent.setFlags( 0 );
        mContext.sendBroadcast( mIntent );
    }

    public void initTitle() {
        getTitleBarView().setLeftImg( R.drawable.icon_xqf_b );
        getTitleBarView().setTitleText( R.string.messageManage );
    }

    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new ManagementAdapter(
                    ((FragmentActivity) mContext).getSupportFragmentManager() );
        }
        mAdapter.setArray( getFragments() );
        mAdapter.setmTitleList( getTitles( R.array.message_titles ) );
        getViewPager().setAdapter( mAdapter );
    }

    public void initTabView() {
        getTabPageIndicator().setViewPager( getViewPager() );
        getIndicator().setViewPager( getViewPager() );
        getIndicator().setFades( false );
        getTabPageIndicator().setOnPageChangeListener( getIndicator() );
        width = getTabPageIndicator().getTextWidth();
        widthOffset = (DisplayMetricsUtils.getScreenWidth( mContext )
                / mAdapter.getCount() - width) / 2;
        getIndicator().setDefultWidth( width );
        getIndicator().setDefultOffset( widthOffset );
        getViewPager().setCurrentItem( 0 );
        getViewPager().setOffscreenPageLimit( 2 );
        getViewPager().setPageTransformer( true,
                BasePageTransformer.getPageTransformer( Transformer.Stack ) );
        getViewPager().setCurrentItem( 0 );
    }

    public void initListener() {
        getTitleBarView().getLeftImg().setOnClickListener( this );
    }

    public TitleBarView getTitleBarView() {
        return messageView.getTitleBarView();
    }

    public TabPageIndicator getTabPageIndicator() {
        return messageView.getTabPageIndicator();
    }

    public CustomerUnderlinePageIndicator getIndicator() {
        return messageView.getIndicator();
    }

    public NoScrollViewPager getViewPager() {
        return messageView.getViewPager();
    }

    public Intent getIntent() {
        return messageView.getIntent();
    }

    public List<Fragment> getFragments() {
        return messageBiz.getFragments();
    }

    public List<String> getTitles(int rid) {
        return messageBiz.getTitles( mContext, rid );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_titleBar_leftImg:
                close( mContext );
                break;
        }
    }

}
