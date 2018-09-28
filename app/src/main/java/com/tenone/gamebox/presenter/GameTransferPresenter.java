package com.tenone.gamebox.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.GameTransferBiz;
import com.tenone.gamebox.mode.listener.FragmentChangeListener;
import com.tenone.gamebox.mode.view.GameTransferView;
import com.tenone.gamebox.view.activity.RebateHelpActivity;
import com.tenone.gamebox.view.adapter.ManagementAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.CustomerUnderlinePageIndicator;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;
import com.tenone.gamebox.view.custom.xbanner.transformers.BasePageTransformer;
import com.tenone.gamebox.view.custom.xbanner.transformers.Transformer;
import com.tenone.gamebox.view.utils.ChenColorUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.ListenerManager;

import java.util.List;

public class GameTransferPresenter implements OnClickListener,
        FragmentChangeListener {

    private Context mContext;
    private GameTransferView view;
    private GameTransferBiz biz;
    private ManagementAdapter mAdapter;
    private float width = 0, widthOffset = 0;

    public GameTransferPresenter(Context cxt, GameTransferView v) {
        this.mContext = cxt;
        this.view = v;
        this.biz = new GameTransferBiz();
    }

    public void initView() {
        initTitle();
        setAdapter();
        initTabView();
        initListener();
    }

    public void initTitle() {
        titleBarView().setLeftImg( R.drawable.icon_xqf_b );
        Drawable rightDrawable = mContext.getResources().getDrawable(
                R.drawable.icon_wen );
        titleBarView().setRightImg( ChenColorUtils.tintDrawable( rightDrawable,
                ColorStateList.valueOf( mContext.getResources().getColor( R.color.gray_69 ) ) ) );
        titleBarView().setTitleText( mContext.getString( R.string.zhuanyou_shengqing) );
    }

    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new ManagementAdapter(
                    ((FragmentActivity) mContext).getSupportFragmentManager() );
        }
        mAdapter.setArray( getFragments() );
        mAdapter.setmTitleList( getTitles( R.array.transfer_titles ) );
        viewPager().setAdapter( mAdapter );
    }

    public void initTabView() {
        tabPageIndicator().setViewPager( viewPager() );
        indicator().setViewPager( viewPager() );
        indicator().setFades( false );
        tabPageIndicator().setOnPageChangeListener( indicator() );
        width = tabPageIndicator().getTextWidth();
        widthOffset = (DisplayMetricsUtils.getScreenWidth( mContext )
                / mAdapter.getCount() - width) / 2;
        indicator().setDefultWidth( width );
        indicator().setDefultOffset( widthOffset );
        viewPager().setCurrentItem( 0 );
        viewPager().setOffscreenPageLimit( 2 );
        viewPager().setPageTransformer( true,
                BasePageTransformer.getPageTransformer( Transformer.Stack ) );
    }

    public void initListener() {
        titleBarView().getLeftImg().setOnClickListener( this );
        titleBarView().getRightImg().setOnClickListener( this );
        ListenerManager.registerFragmentChangeListener( this );
    }

    private TabPageIndicator tabPageIndicator() {
        return view.tabPageIndicator();
    }

    private CustomerUnderlinePageIndicator indicator() {
        return view.indicator();
    }

    private ViewPager viewPager() {
        return view.viewPager();
    }

    private TitleBarView titleBarView() {
        return view.titleBarView();
    }

    private List<Fragment> getFragments() {
        return biz.getFragments();
    }

    private List<String> getTitles(int rId) {
        return biz.getTitles( mContext, rId );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_titleBar_leftImg:
                ((BaseActivity) mContext).finish();
                break;
            case R.id.id_titleBar_rightImg:
                Intent intent = new Intent();
                intent.putExtra( "what", 1 );
                intent.setClass( mContext, RebateHelpActivity.class );
                mContext.startActivity( intent );
                break;
        }
    }

    @Override
    public void onFragmentChange(int which) {
        viewPager().setCurrentItem( which );
    }
}
