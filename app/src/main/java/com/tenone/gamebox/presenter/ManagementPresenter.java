

package com.tenone.gamebox.presenter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.ManagementBiz;
import com.tenone.gamebox.mode.view.ManagementView;
import com.tenone.gamebox.view.adapter.ManagementAdapter;
import com.tenone.gamebox.view.custom.CustomerUnderlinePageIndicator;
import com.tenone.gamebox.view.custom.NoScrollViewPager;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

import java.util.List;

public class ManagementPresenter extends BasePresenter implements
        OnClickListener {
    private ManagementBiz managementBiz;
    private ManagementView managementView;
    private Context mContext;
    private float width = 0, widthOffset = 0;
    private ManagementAdapter mAdapter;
    private VisibilityBroadcast broadcast;

    public ManagementPresenter(Context cxt, ManagementView view) {
        this.mContext = cxt;
        this.managementView = view;
        this.managementBiz = new ManagementBiz();
        registerReceiver();
    }

    
    private void registerReceiver() {
        broadcast = new VisibilityBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction( "show_cancle_button" );
        mContext.registerReceiver( broadcast, filter );
    }

    public void onDestroy() {
        mContext.unregisterReceiver( broadcast );
    }

    
    public void setAdapter(Activity activity) {
        if (mAdapter == null) {
            mAdapter = new ManagementAdapter(
                    ((FragmentActivity) activity).getSupportFragmentManager() );
        }
        mAdapter.setArray( getFragments() );
        mAdapter.setmTitleList( getTitles( R.array.management_titles ) );
        getViewPager().setAdapter( mAdapter );
        getViewPager().setTransition( true );
    }

    
    public void initTitle() {
        getTitleBarView().setLeftImg( R.drawable.icon_xqf_b );
        getTitleBarView().setTitleText( R.string.management );
        getTitleBarView().setRightText( R.string.cancle );
        goneCancleBt();
    }

    
    public void initTabView() {
        getTabPageIndicator().setViewPager( getViewPager() );
        getCustomerUnderlinePageIndicator().setViewPager( getViewPager() );
        getCustomerUnderlinePageIndicator().setFades( false );
        getTabPageIndicator().setOnPageChangeListener(
                getCustomerUnderlinePageIndicator() );
        
        width = getTabPageIndicator().getTextWidth();
        
        widthOffset = (DisplayMetricsUtils.getScreenWidth( mContext )
                / mAdapter.getCount() - width) / 2;
        getCustomerUnderlinePageIndicator().setDefultWidth( width );
        getCustomerUnderlinePageIndicator().setDefultOffset( widthOffset );
        getViewPager().setCurrentItem( 0 );
        getViewPager().setOffscreenPageLimit( 3 );
    }

    
    public void initListener() {
        getTitleBarView().getLeftImg().setOnClickListener( this );
        getTitleBarView().getRightText().setOnClickListener( this );
    }

    private List<Fragment> getFragments() {
        return managementBiz.getFragments();
    }

    private List<String> getTitles(int resId) {
        return managementBiz.getTitles( mContext, resId );
    }

    private TitleBarView getTitleBarView() {
        return managementView.getTitleBarView();
    }

    private TabPageIndicator getTabPageIndicator() {
        return managementView.getTabPageIndicator();
    }

    private CustomerUnderlinePageIndicator getCustomerUnderlinePageIndicator() {
        return managementView.getCustomerUnderlinePageIndicator();
    }

    private NoScrollViewPager getViewPager() {
        return managementView.getViewPager();
    }

    
    private void showCancleBt() {
        getTitleBarView().getRightText().setVisibility( View.VISIBLE );
    }

    
    private void goneCancleBt() {
        getTitleBarView().getRightText().setVisibility( View.GONE );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_titleBar_leftImg:
                close( mContext );
                break;
            case R.id.id_titleBar_rightText:
                
                goneCancleBt();
                sendCancleBroadcast();
                getViewPager().setTransition( true );
                getTabPageIndicator().setSelect( true );
                break;
        }
    }

    private void sendCancleBroadcast() {
        Intent intent = new Intent();
        intent.setAction( "cancle_action" );
        mContext.sendBroadcast( intent );
    }

    
    public class VisibilityBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isEdit = intent.getBooleanExtra( "isEdit", false );
            if (isEdit) {
                showCancleBt();
            } else {
                goneCancleBt();
            }
            getViewPager().setTransition( !isEdit );
            getTabPageIndicator().setSelect( !isEdit );
        }
    }
}
