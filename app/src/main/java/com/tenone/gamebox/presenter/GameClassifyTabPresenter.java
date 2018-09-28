package com.tenone.gamebox.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.GameClassifyTabBiz;
import com.tenone.gamebox.mode.listener.GameItemClickListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.GameClassifyTabView;
import com.tenone.gamebox.view.activity.NewGameDetailsActivity;
import com.tenone.gamebox.view.adapter.OldBtGameAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class GameClassifyTabPresenter extends BasePresenter implements
        OnClickListener, GameItemClickListener,
        OnRefreshListener, OnLoadListener, HttpResultListener, AdapterView.OnItemClickListener {

    private GameClassifyTabBiz classifyTabBiz;
    private GameClassifyTabView classifyTabView;
    private Context mContext;
    private OldBtGameAdapter mAdapter;
    private List<GameModel> items = new ArrayList<GameModel>();
    private int page = 1, platform;

    public GameClassifyTabPresenter(GameClassifyTabView view, Context cxt, int platform) {
        this.classifyTabView = view;
        this.mContext = cxt;
        this.classifyTabBiz = new GameClassifyTabBiz();
        this.platform = platform;
    }

    AlertDialog alertDialog;

    public void init() {
        alertDialog = buildProgressDialog( mContext );
        getTitleBarView().setTitleText( getTitle() );
        getTitleBarView().setLeftImg( R.drawable.icon_back_grey );
        getRefreshLayout().setRefreshing( true );
    }

    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new OldBtGameAdapter( items, mContext, platform);
        }
        getRecommendListView().setAdapter( mAdapter );
    }

    public void initListener() {
        getTitleBarView().getLeftImg().setOnClickListener( this );
        getRecommendListView().setOnItemClickListener( this );
        getRefreshLayout().setOnRefreshListener( this );
        getRefreshLayout().setOnLoadListener( this );
    }

    /**
     * ��ȡ���� requestHttp:(������һ�仰�����������������). <br/>
     *
     * @param what
     * @author John Lie
     * @since JDK 1.6
     */
    public void requestHttp(int what) {
        String url = MyApplication.getHttpUrl().getGameClassInfo();
        if (TextUtils.isEmpty( getClassifyId() )) {
            return;
        }
        RequestBody requestBody = new FormBody.Builder()
                .add( "channel", MyApplication.getConfigModle().getChannelID() )
                .add( "page", page + "" )
                .add( "system", "1" )
                .add( "platform", platform + "" )
                .add( "classId", getClassifyId() ).build();
        HttpUtils.postHttp( mContext, what, url, requestBody, this );
    }

    public String getTitle() {
        return classifyTabBiz.getTitle( getIntent() );
    }
    public Intent getIntent() {
        return classifyTabView.getIntent();
    }

    public String getClassifyId() {
        return classifyTabBiz.getClassifyId( getIntent() );
    }

    public TitleBarView getTitleBarView() {
        return classifyTabView.getTitleBarView();
    }

    public RefreshLayout getRefreshLayout() {
        return classifyTabView.getRefreshLayout();
    }

    public ListView getRecommendListView() {
        return classifyTabView.getRecommendListView();
    }

    public List<GameModel> getGameRecommends(List<ResultItem> resultItems) {
        return classifyTabBiz.constructArray( mContext, resultItems );
    }

    public void finish() {
        getTitleBarView().getLeftImg().setImageDrawable( null );
        ((BaseActivity) mContext).finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_titleBar_leftImg:
                finish();
                break;
        }
    }

    @Override
    public void gameItemClick(GameModel gameMode) {
        openOtherActivity( mContext, new Intent( mContext, NewGameDetailsActivity.class )
                .putExtra( "id", gameMode.getGameId() + "" ) );
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


    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        cancelProgressDialog( alertDialog );
        getRefreshLayout().setRefreshing( false );
        getRefreshLayout().setLoading( false );
        if ("0".equals( resultItem.getString( "status" ) )) {
            if (resultItem != null) {
                List<ResultItem> resultItems = resultItem.getItems( "data" );
                if (resultItems != null) {
                    if (what == HttpType.REFRESH) {
                        items.clear();
                    }
                    items.addAll( getGameRecommends( resultItems ) );
                }
            }
            mAdapter.notifyDataSetChanged();
        } else {
            showToast( mContext, resultItem.getString( "msg" ) );
        }
    }

    @Override
    public void onError(int what, String error) {
        cancelProgressDialog( alertDialog );
        getRefreshLayout().setRefreshing( false );
        getRefreshLayout().setLoading( false );
        showToast( mContext, error );
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        openOtherActivity( mContext, new Intent( mContext, NewGameDetailsActivity.class )
                .putExtra( "id", items.get( position ).getGameId() + "" ) );
    }
}
