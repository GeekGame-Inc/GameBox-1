/**
 * Project Name:GameBox
 * File Name:GameTopPresenter.java
 * Package Name:com.tenone.gamebox.presenter
 * Date:2017-3-10����11:38:06
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.GameTopBiz;
import com.tenone.gamebox.mode.listener.GameItemClickListener;
import com.tenone.gamebox.mode.listener.GameTopListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnMessageUpdateListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.GameTopView;
import com.tenone.gamebox.view.activity.GameDetailsActivity;
import com.tenone.gamebox.view.activity.GameDetailsNewActivity;
import com.tenone.gamebox.view.activity.GameSearchActivity;
import com.tenone.gamebox.view.activity.LoginActivity;
import com.tenone.gamebox.view.activity.ManagementActivity;
import com.tenone.gamebox.view.activity.MyMessageActivity;
import com.tenone.gamebox.view.adapter.GameRecommendFragmentAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.base.Constant;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.bga.BGABadgeView;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DatabaseUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.cache.ACache;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * ClassName:GameTopPresenter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-10 ����11:38:06 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */
@SuppressLint("HandlerLeak")
public class GameTopPresenter extends BasePresenter implements OnClickListener,
        GameItemClickListener, OnRefreshListener, OnLoadListener,
        HttpResultListener, GameTopListener, OnMessageUpdateListener {

    private GameTopBiz gameTopBiz;
    private GameTopView gameTopView;
    private Context mContext;
    private GameRecommendFragmentAdapter mAdapter;
    private int page = 1;
    /**
     * ����Դ
     */
    private List<GameModel> items = new ArrayList<GameModel>();

    private BGABadgeView messageBadge;
    private BGABadgeView downBadge;
    private AlertDialog alertDialog;

    private ACache cache;
    private boolean toMessageList = false;

    public GameTopPresenter(GameTopView view, Context cxt) {
        this.gameTopView = view;
        this.mContext = cxt;
        this.gameTopBiz = new GameTopBiz();
        cache = ACache.get( mContext );
        getCache();
    }

    public void getCache() {
        ResultItem resultItem = (ResultItem) cache.getAsObject( "top_game" );
        if (resultItem != null) {
            List<ResultItem> resultItems = resultItem.getItems( "data" );
            items.clear();
            getGameRecommends( resultItems );
        }
    }

    /* ��ʼ�� */
    public void init() {
        alertDialog = buildProgressDialog( mContext );
        if (getIntent().hasExtra( "from" )) {
            getTitleBarView().setTitleText(
                    mContext.getResources().getString( R.string.rankingList ) );
            getTitleBarView().setLeftImg( R.drawable.icon_back_grey );
            getSerachBar().setVisibility( View.GONE );
        } else {
            getTitleBarView().setVisibility( View.GONE );
        }

		/* ɨ�����ض��� */
        List<GameModel> gameModels = DatabaseUtils.getInstanse( mContext )
                .getDownloadList();
        int a = 0;
        for (GameModel mode : gameModels) {
            if (mode.getStatus() == GameStatus.LOADING
                    || mode.getStatus() == GameStatus.PAUSEING
                    || mode.getStatus() == GameStatus.COMPLETED) {
                a++;
            }
        }
        if (a > 0) {
            downBadge = showDownBadgeView( mContext, getDownBadgeTv(), a + "" );
        }
        messageBadge = showMessageBadgeView( mContext,
                getMessageBadgeTv(), "0" );
        ListenerManager.registerOnMessageUpdateListener( this );
        if (BeanUtils.isLogin()) {
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
        }
    }

    /* ��ʼ�������� */
    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new GameRecommendFragmentAdapter( mContext, items );
        }
        getRefreshLayout().setOnRefreshListener( this );
        getRefreshLayout().setOnLoadListener( this );
        getListView().setAdapter( mAdapter );
    }

    /* ��ʼ�������� */
    public void initListener() {
        if (getIntent().hasExtra( "from" )) {
            getTitleBarView().getLeftImg().setOnClickListener( this );
        }
        mAdapter.setOnItemClick( this );
        getSerachBar().findViewById( R.id.id_main_search ).setOnClickListener(
                this );
        getSerachBar().findViewById( R.id.id_main_down ).setOnClickListener( this );
        getSerachBar().findViewById( R.id.id_main_message ).setOnClickListener(
                this );
    }

    public void requestHttp(int what) {
        String url = MyApplication.getHttpUrl().getGameType();
        RequestBody requestBody = new FormBody.Builder()
                .add( "channel", MyApplication.getConfigModle().getChannelID() )
                .add( "page", page + "" ).add( "system", "1" ).add( "type", 3 + "" )
                .build();
        HttpUtils.postHttp( mContext, what, url, requestBody, this );
    }

    /* ��ȡ��ͼ */
    public Intent getIntent() {
        return gameTopView.getIntent();
    }

    /* ��ȡ������ */
    public View getSerachBar() {
        return gameTopView.getSerachBar();
    }

    /* ��ȡ���� */
    public TitleBarView getTitleBarView() {
        return gameTopView.getTitleBarView();
    }

    /* ��ȡˢ�¿ؼ� */
    public RefreshLayout getRefreshLayout() {
        return gameTopView.getRefreshLayout();
    }

    /* ��ȡ�б� */
    public ListView getListView() {
        return gameTopView.getListView();
    }

    /* ��ȡ���� */
    public void getGameRecommends(List<ResultItem> resultItem) {
        gameTopBiz.constructArray( mContext, resultItem, this );
    }

    public TextView getDownBadgeTv() {
        return gameTopView.getDownBadgeTv();
    }

    public TextView getMessageBadgeTv() {
        return gameTopView.getMessageBadgeTv();
    }

    /* �ر�ҳ�� */
    public void finish() {
        /* �ͷ�imagview */
        getTitleBarView().getLeftImg().setImageDrawable( null );
        ((BaseActivity) mContext).finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_titleBar_leftImg:
                finish();
                break;
            case R.id.id_main_search:
                openOtherActivity( mContext, new Intent( mContext,
                        GameSearchActivity.class ) );
                break;
            case R.id.id_main_down:
                openOtherActivity( mContext, new Intent( mContext,
                        ManagementActivity.class ) );
                break;
            case R.id.id_main_message:
                if ("0".equals( SpUtil.getUserId() )) {
                    openOtherActivity( mContext, new Intent( mContext,
                            LoginActivity.class ) );
                    break;
                }
                // ��Ϣ
                openOtherActivity( mContext, new Intent( mContext,
                        MyMessageActivity.class ).putExtra( "tag", toMessageList ? 1 : 0 ) );
                break;
        }
    }

    @Override
    public void gameItemClick(GameModel gameMode) {
        // ��������
        openOtherActivity( mContext, new Intent( mContext,
                Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ? GameDetailsActivity.class : GameDetailsNewActivity.class ).putExtra( "id", gameMode.getGameId()
                + "" ) );
    }


    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        cancelProgressDialog( alertDialog );
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
        page++;
        requestHttp( HttpType.LOADING );
    }

    @Override
    public void onRefresh() {
        page = 1;
        requestHttp( HttpType.REFRESH );
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<ResultItem> resultItem = ((ResultItem) msg.obj)
                    .getItems( "data" );
            what = msg.what;
            switch (msg.what) {
                case HttpType.REFRESH:
                    cache.put( "top_game", ((ResultItem) msg.obj) );
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
    public void updateUI(List<GameModel> gameModels) {
        if (HttpType.REFRESH == what) {
            items.clear();
        }
        items.addAll( gameModels );
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMessageUpdate(final String text) {
        Log.i( "onMessageUpdate", "text is " + text );
        handler1.post( new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty( text ) && !"0".equals( text )) {
                    toMessageList = true;
                    if (messageBadge != null) {
                        if (!messageBadge.isShown()) {
                            messageBadge.showShape( true );
                        }
                        messageBadge.setText( text );
                    }
                } else {
                    toMessageList = false;
                    if (messageBadge != null && messageBadge.isShown()) {
                        messageBadge.hide();
                    }
                }
            }
        } );
    }

    Handler handler1 = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage( msg );
        }
    };
}
