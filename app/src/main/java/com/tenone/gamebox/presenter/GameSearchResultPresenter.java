/**
 * Project Name:GameBox
 * File Name:GameSearchResultPresenter.java
 * Package Name:com.tenone.gamebox.presenter
 * Date:2017-4-25����5:25:17
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.GameSearchResultBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.GameSearchResultView;
import com.tenone.gamebox.view.activity.NewGameDetailsActivity;
import com.tenone.gamebox.view.adapter.NewSearchResultAdapter;
import com.tenone.gamebox.view.adapter.SearchRecordWindowAdapter;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.custom.SearchTitleBarView;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * ClassName:GameSearchResultPresenter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-25 ����5:25:17 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */
public class GameSearchResultPresenter extends BasePresenter implements
        HttpResultListener, OnClickListener, OnRefreshListener, OnLoadListener,
        SearchTitleBarView.OnSearchButtonClickListener, AdapterView.OnItemClickListener {
    private GameSearchResultBiz resultBiz;
    private GameSearchResultView resultView;
    private Context mContext;

    private List<GameModel> items = new ArrayList<GameModel>();
    private NewSearchResultAdapter gameAdapter;
    private SearchRecordWindowAdapter adapter;
    // ������Ϸ����
    private List<String> allGameName = new ArrayList<String>();
    private String condition;
    private int page = 1, platform;


    public GameSearchResultPresenter(Context context, GameSearchResultView view, int platform) {
        this.resultView = view;
        this.mContext = context;
        this.platform = platform;
        this.resultBiz = new GameSearchResultBiz();
    }

    public void initView() {
        allGameName.clear();
        allGameName.addAll( getAllGameName() );
        condition = getCondition();
        getSearchTitleBarView().setLeftImg( R.drawable.icon_xqf_b );
        getSearchTitleBarView().setRightImg( R.drawable.ic_sousuo );
        adapter = new SearchRecordWindowAdapter( mContext, allGameName, "" );
        CustomizeEditText editText = getSearchTitleBarView()
                .getCustomizeEditText();
        editText.setText( getCondition() );
        editText.setSelection( getCondition().length() );
        editText.setAdapter( adapter );
        editText.setDropDownWidth( DisplayMetricsUtils.getScreenWidth( mContext ) );
        editText.setDropDownVerticalOffset( DisplayMetricsUtils.dipTopx( mContext, 3 ) );
    }

    public void setAdapter() {
        gameAdapter = new NewSearchResultAdapter( items, mContext );
        getListView().setAdapter( gameAdapter );
    }

    public void initListener() {
        getSearchTitleBarView().getLeftImg().setOnClickListener( this );
        getSearchTitleBarView().getRightImg().setOnClickListener( this );
        getListView().setOnItemClickListener( this );
        getRefreshLayout().setOnRefreshListener( this );
        getRefreshLayout().setOnLoadListener( this );
        getSearchTitleBarView().setOnSearchButtonClickListener( this );
    }

    /**
     * �������� request:(������һ�仰�����������������). <br/>
     *
     * @author John Lie
     * @since JDK 1.6
     */
    public void requestList(int what) {
        String url = MyApplication.getHttpUrl().getGameSearchList();
        RequestBody requestBody = new FormBody.Builder()
                .add( "channel", MyApplication.getConfigModle().getChannelID() )
                .add( "system", "1" )
                .add( "keyword", condition )
                .add( "page", page + "" )
                .build();
        HttpUtils.postHttp( mContext, what, url, requestBody, this );
    }

    private SearchTitleBarView getSearchTitleBarView() {
        return resultView.getSearchTitleBarView();
    }

    private RefreshLayout getRefreshLayout() {
        return resultView.getRefreshLayout();
    }

    private ListView getListView() {
        return resultView.getListView();
    }

    private Intent getIntent() {
        return resultView.getIntent();
    }

    private String getCondition() {
        return resultBiz.getCondition( getIntent() );
    }

    private List<GameModel> getGameModels(List<ResultItem> resultItems) {
        return resultBiz.getGameModels( mContext, resultItems );
    }

    private List<String> getAllGameName() {
        return resultBiz.getAllGameName();
    }

    private void saveRecord(String record) {
        resultBiz.saveRecord( record );
    }


    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        getRefreshLayout().setRefreshing( false );
        getRefreshLayout().setLoading( false );
        if ("0".equals( resultItem.getString( "status" ) )) {
            List<ResultItem> list = resultItem.getItems( "data" );
            if (what == HttpType.REFRESH) {
                items.clear();
            }
            if (list != null) {
                items.addAll( getGameModels( list ) );
            }
            gameAdapter.notifyDataSetChanged();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_searchTitle_leftImg:
                close( mContext );
                break;
            case R.id.id_searchTitle_rightImg:
                String str = getSearchTitleBarView().getCustomizeEditText()
                        .getText().toString();
                saveRecord( str );
                condition = str;
                requestList( HttpType.REFRESH );
                break;
        }
    }


    @Override
    public void onLoad() {
        page++;
        requestList( HttpType.LOADING );
    }

    @Override
    public void onRefresh() {
        page = 1;
        requestList( HttpType.REFRESH );
    }

    @Override
    public void onSearchButtonClick() {
        String str = getSearchTitleBarView().getCustomizeEditText()
                .getText().toString();
        saveRecord( str );
        condition = str;
        requestList( HttpType.REFRESH );
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // ��������
        openOtherActivity( mContext, new Intent( mContext, NewGameDetailsActivity.class )
                .putExtra( "id", items.get( position ).getGameId() + "" ) );
    }
}
