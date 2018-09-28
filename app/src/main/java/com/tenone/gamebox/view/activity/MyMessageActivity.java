package com.tenone.gamebox.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.MyMessageView;
import com.tenone.gamebox.presenter.MyMessagePresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.base.Constant;
import com.tenone.gamebox.view.custom.CustomerUnderlinePageIndicator;
import com.tenone.gamebox.view.custom.NoScrollViewPager;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;

public class MyMessageActivity extends BaseActivity implements MyMessageView {
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBarView;
    @ViewInject(R.id.id_message_tabPageIndicator)
    TabPageIndicator tabPageIndicator;
    @ViewInject(R.id.id_message_underlineIndicator)
    CustomerUnderlinePageIndicator indicator;
    @ViewInject(R.id.id_message_viewPager)
    NoScrollViewPager viewPager;

    private MyMessagePresenter presenter;
    private int position = 0;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_message );
        mContext = this;
        position = getIntent().getExtras().getInt( "tag", 0 );
        ViewUtils.inject( this );
        presenter = new MyMessagePresenter( this, this, position );
        presenter.initTitle();
        presenter.setAdapter();
        presenter.initTabView();
        presenter.initListener();
    }

    @Override
    public TitleBarView getTitleBarView() {
        return titleBarView;
    }

    @Override
    public TabPageIndicator getTabPageIndicator() {
        return tabPageIndicator;
    }

    @Override
    public CustomerUnderlinePageIndicator getIndicator() {
        return indicator;
    }

    @Override
    public NoScrollViewPager getViewPager() {
        return viewPager;
    }

    private class MessageThread extends Thread {
        @Override
        public void run() {
            HttpManager.getUnreadCounts( 0, mContext, new HttpResultListener() {
                @Override
                public void onSuccess(int what, ResultItem resultItem) {
                    if (1 == resultItem.getIntValue( "status" )) {
                        String text = resultItem.getString( "data" );
                        Log.i( "onMessageUpdate", "onSuccess text is " + text );
                        Constant.setMessageNum( text );
                        ListenerManager.sendOnMessageUpdateListener( Constant.getMessageNum() );
                    }
                }

                @Override
                public void onError(int what, String error) {
                }
            } );
            super.run();
        }
    }

    @Override
    public void onDestroy() {
        new MessageThread().start();
        super.onDestroy();
    }
}
