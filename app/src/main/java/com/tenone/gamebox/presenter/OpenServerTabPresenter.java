package com.tenone.gamebox.presenter;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.OpenServerTabBiz;
import com.tenone.gamebox.mode.view.OpenServerTabView;
import com.tenone.gamebox.view.adapter.ManagementAdapter;
import com.tenone.gamebox.view.utils.BeanUtils;

import java.util.List;

public class OpenServerTabPresenter extends BasePresenter {
    OpenServerTabBiz serverTabBiz;
    OpenServerTabView serverTabView;
    Context mContext;
    ManagementAdapter mAdapter;

    public OpenServerTabPresenter(OpenServerTabView v, Context cxt) {
        this.mContext = cxt;
        this.serverTabBiz = new OpenServerTabBiz();
        this.serverTabView = v;
    }

    /* ���������� */
    public void setAdapter(FragmentManager manager) {
        if (mAdapter == null) {
            mAdapter = new ManagementAdapter( manager );
        }
        mAdapter.setArray( getFragments() );
        mAdapter.setmTitleList( getTitles() );
        getViewPager().setAdapter( mAdapter );
        BeanUtils.setIndicatorWidth( getTabLayout());
    }

    /**
     * initView:(��ʼ��). <br/>
     *
     * @author John Lie
     * @since JDK 1.6
     */
    public void initView() {
        getTabLayout().setupWithViewPager( getViewPager() );
        getViewPager().setCurrentItem( 0 );
        getViewPager().setOffscreenPageLimit( 1 );
    }


    public ViewPager getViewPager() {
        return serverTabView.getViewPager();
    }

    public List<Fragment> getFragments() {
        return serverTabBiz.getFragments();
    }

    public List<String> getTitles() {
        return serverTabBiz.getTitles( mContext, R.array.fragment_new_open_title );
    }

    private TabLayout getTabLayout(){
        return  serverTabView.getTabLayout();
    }
}
