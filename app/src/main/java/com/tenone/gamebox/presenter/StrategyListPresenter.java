

package com.tenone.gamebox.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.StrategyListBiz;
import com.tenone.gamebox.mode.listener.GameTopListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.StrategyListView;
import com.tenone.gamebox.view.activity.NewGameDetailsActivity;
import com.tenone.gamebox.view.adapter.OldBtGameAdapter;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.HttpManager;

import java.util.ArrayList;
import java.util.List;


public class StrategyListPresenter extends BasePresenter implements
        OnClickListener, HttpResultListener, OnRefreshListener, OnLoadListener,
        GameTopListener, AdapterView.OnItemClickListener {
    private StrategyListView view;
    private StrategyListBiz biz;
    private Context mContext;
    private OldBtGameAdapter mAdapter;
    private List<GameModel> models = new ArrayList<GameModel>();
    private int page = 1, platform;
    private AlertDialog alertDialog;

    public StrategyListPresenter(StrategyListView v, Context cxt, int platform) {
        this.view = v;
        this.mContext = cxt;
        this.platform = platform;
        this.biz = new StrategyListBiz();
    }

    public void initView() {
        alertDialog = buildProgressDialog( mContext );
        getTitleBarView().setLeftImg( R.drawable.icon_xqf_b );
        getTitleBarView().setTitleText( platform == 1 ?
                mContext.getResources().getString( R.string.fullVGame ) : platform == 2 ?
						mContext.getString( R.string.ultralow_discount) : mContext.getString( R.string.make_coin) );
    }

    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new OldBtGameAdapter( models, mContext, platform );
        }
        getListView().setAdapter( mAdapter );
    }

    public void initListener() {
        getTitleBarView().getLeftImg().setOnClickListener( this );
        getRefreshLayout().setOnRefreshListener( this );
        getRefreshLayout().setOnLoadListener( this );
        getListView().setOnItemClickListener( this );
    }


    
    public void requestHttp(int what) {
        if (platform == 3) {
            HttpManager.newGameType( mContext, what, this, page, 3, 4 );
        } else {
            HttpManager.getNewGame( mContext, what, this, page, platform, platform == 1 ? 4 : 5 );
        }
    }

    public TitleBarView getTitleBarView() {
        return view.getTitleBarView();
    }

    public RefreshLayout getRefreshLayout() {
        return view.getRefreshLayout();
    }

    public ListView getListView() {
        return view.getListView();
    }

    
    public void getGameRecommends(List<ResultItem> resultItem) {
        biz.constructArray( mContext, resultItem, this );
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_titleBar_leftImg) {
            close( mContext );
        }
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        cancelProgressDialog( alertDialog );
        getRefreshLayout().setRefreshing( false );
        getRefreshLayout().setLoading( false );
        if (0 == resultItem.getIntValue( "status" )) {
            this.what = what;
            List<ResultItem> datas = platform == 3 ? resultItem.getItem( "data" ).getItems( "list" )
                    : resultItem.getItems( "data" );
            getGameRecommends( datas );
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
    public void onLoad() {
        page++;
        requestHttp( HttpType.LOADING );
    }

    @Override
    public void onRefresh() {
        page = 1;
        requestHttp( HttpType.REFRESH );
    }

    int what;

    @Override
    public void updateUI(List<GameModel> gameModels) {
        if (HttpType.REFRESH == what) {
            models.clear();
        }
        models.addAll( gameModels );
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        openOtherActivity( mContext, new Intent( mContext, NewGameDetailsActivity.class )
                .putExtra( "id", models.get( position ).getGameId() + "" ) );
    }
}
