/**
 * ������Ϸ
 * Project Name:GameBox
 * File Name:GameHot.java
 * Package Name:com.tenone.gamebox.presenter
 * Date:2017-3-9����2:35:13
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.ListView;

import com.tenone.gamebox.mode.biz.GameHotFragmentBiz;
import com.tenone.gamebox.mode.listener.GameHotFragmentListener;
import com.tenone.gamebox.mode.listener.GameItemClickListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.GameHotFragmentView;
import com.tenone.gamebox.view.activity.GameDetailsActivity;
import com.tenone.gamebox.view.activity.GameDetailsNewActivity;
import com.tenone.gamebox.view.adapter.GameRecommendFragmentAdapter;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.cache.ACache;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * ClassName:GameHot <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-9 ����2:35:13 <br/>
 *
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
@SuppressLint("HandlerLeak")
public class GameHotFragmentPresenter extends BasePresenter implements
        OnRefreshListener, OnLoadListener,
        GameItemClickListener,
        HttpResultListener, GameHotFragmentListener {
    GameHotFragmentBiz fragmentBiz;
    GameHotFragmentView fragmentView;
    Context mContext;
    GameRecommendFragmentAdapter mAdapter;
    /**
     * ����Դ
     */
    List<GameModel> items = new ArrayList<GameModel>();
    int page = 1;
    ACache cache;

    public GameHotFragmentPresenter(GameHotFragmentView view, Context cxt) {
        this.fragmentBiz = new GameHotFragmentBiz();
        this.fragmentView = view;
        this.mContext = cxt;
        cache = ACache.get( mContext );
        getCache();
    }

    /**
     * ���������� setAdapter:(������һ�仰�����������������). <br/>
     *
     * @author John Lie
     * @since JDK 1.6
     */
    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new GameRecommendFragmentAdapter( mContext, items );
        }
        getListView().setAdapter( mAdapter );
    }

    public void getCache() {
        ResultItem resultItem = (ResultItem) cache.getAsObject( "hot_game" );
        if (resultItem != null) {
            List<ResultItem> resultItems = resultItem.getItems( "data" );
            items.clear();
            getGameRecommends( resultItems );
        }
    }

    /**
     * ��ʼ�������� initListener:(������һ�仰�����������������). <br/>
     *
     * @author John Lie
     * @since JDK 1.6
     */
    public void initListener() {
        getRefreshLayout().setOnRefreshListener( this );
        getRefreshLayout().setOnLoadListener( this );
        mAdapter.setOnItemClick( this );
    }

    /**
     * ��ȡ���� requestHttp:(������һ�仰�����������������). <br/>
     *
     * @author John Lie
     * @param what
     * @since JDK 1.6
     */
    public void requestHttp(int what) {
        String url = MyApplication.getHttpUrl().getGameType();
        RequestBody requestBody = new FormBody.Builder()
                .add( "channel", MyApplication.getConfigModle().getChannelID() )
                .add( "page", page + "" ).add( "system", "1" ).add( "type", 2 + "" )
                .build();
        HttpUtils.postHttp( mContext, what, url, requestBody, this );
    }

    /**
     * ��ȡlistview getListView:(������һ�仰�����������������). <br/>
     *
     * @author John Lie
     * @return
     * @since JDK 1.6
     */
    public ListView getListView() {
        return fragmentView.getRecommendListView();
    }

    /**
     * ��ȡˢ�¿ؼ� getRefreshLayout:(������һ�仰�����������������). <br/>
     *
     * @author John Lie
     * @return
     * @since JDK 1.6
     */
    public RefreshLayout getRefreshLayout() {
        return fragmentView.getRefreshLayout();
    }

    /**
     * ��ȡlistview ���� getGameRecommends:(������һ�仰�����������������). <br/>
     *
     * @author John Lie
     * @return
     * @since JDK 1.6
     */
    public void getGameRecommends(List<ResultItem> resultItems) {
        fragmentBiz.constructArray( mContext, resultItems, this );
    }


    @Override
    public void gameItemClick(GameModel gameMode) {
        // ��Ϸ����
        openOtherActivity( mContext, new Intent( mContext,
                Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ? GameDetailsActivity.class : GameDetailsNewActivity.class ).putExtra( "id", gameMode.getGameId()
                + "" ) );
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        getRefreshLayout().setRefreshing( false );
        getRefreshLayout().setLoading( false );
        if ("0".equals( resultItem.getString( "status" ) )) {
            Message message = new Message();
            message.what = what;
            message.obj = resultItem;
            handler.sendMessage( message );
        } else {
            showToast( mContext, resultItem.getString( "msg" ) );
        }
    }

    @Override
    public void onError(int what, String error) {
        getRefreshLayout().setRefreshing( false );
        getRefreshLayout().setLoading( false );
        showToast( mContext, error );
    }

    @Override
    public void onLoad() {
        // ����
        page++;
        requestHttp( HttpType.LOADING );
    }

    @Override
    public void onRefresh() {
        page = 1;
        requestHttp( HttpType.REFRESH );
    }

    Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            List<ResultItem> resultItem = ((ResultItem) msg.obj)
                    .getItems( "data" );
            what = msg.what;
            switch (msg.what) {
                case HttpType.REFRESH:
                    cache.put( "hot_game", ((ResultItem) msg.obj) );
                    getGameRecommends( resultItem );
                    break;
                case HttpType.LOADING:
                    getGameRecommends( resultItem );
                    break;
            }
        }
    };

    int what;

    @Override
    public void uadapteUI(List<GameModel> gameModels) {
        if (HttpType.REFRESH == what) {
            items.clear();
        }
        items.addAll( gameModels );
        mAdapter.notifyDataSetChanged();
    }

}
