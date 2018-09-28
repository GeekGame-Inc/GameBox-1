package com.tenone.gamebox.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.adapter.NCGameAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ReservedUtils;
import com.tenone.gamebox.view.utils.ToastUtils;
import com.tenone.gamebox.view.utils.database.RealmUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class ClosedBetaTestSubscribeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener, HttpResultListener, AdapterView.OnItemClickListener, NCGameAdapter.OnYYClickListener {
    @ViewInject(R.id.id_closed_subscribe_refresh)
    RefreshLayout refreshLayout;
    @ViewInject(R.id.id_closed_subscribe_listView)
    ListView listView;
    @ViewInject(R.id.id_title_bar)
    TitleBarView titleBarView;

    private List<GameModel> models = new ArrayList<GameModel>();
    private NCGameAdapter adapter;
    private boolean isNc = true;
    private int page = 1;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_closed_subscribe );
        ViewUtils.inject( this );
        Intent intent = getIntent();
        isNc = "nc".equals( intent.getAction() );
        initTitle();
        initView();
        HttpManager.newGameList( this, HttpType.REFRESH, this, page, isNc ? 1 : 2 );
    }

    private void initTitle() {
			titleBarView.setTitleText( isNc ? "\u5185\u6d4b\u6e38\u620f" : "\u9884\u7ea6\u6e38\u620f" );
        titleBarView.setLeftImg( R.drawable.icon_xqf_b );
        titleBarView.setOnClickListener( R.id.id_titleBar_leftImg, v -> finish() );
    }

    private void initView() {
        refreshLayout.setRefreshing( true );
        adapter = new NCGameAdapter( models, this, isNc ? 0 : 1 );
        if (!isNc) {
            adapter.setOnYYClickListener( this );
        }
        listView.setAdapter( adapter );
        refreshLayout.setOnRefreshListener( this );
        refreshLayout.setOnLoadListener( this );
        listView.setOnItemClickListener( this );
    }

    @Override
    public void onRefresh() {
        page = 1;
        HttpManager.newGameList( this, HttpType.REFRESH, this, page, isNc ? 1 : 2 );
    }

    @Override
    public void onLoad() {
        page++;
        HttpManager.newGameList( this, HttpType.LOADING, this, page, isNc ? 1 : 2 );
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        refreshLayout.setRefreshing( false );
        refreshLayout.setLoading( false );
        if (0 == resultItem.getIntValue( "status" )) {
            if (what == HttpType.REFRESH) {
                models.clear();
            }
            List<ResultItem> data = resultItem.getItems( "data" );
            if (!BeanUtils.isEmpty( data )) {
                setData( data );
            }
        } else {
            ToastUtils.showToast( this, resultItem.getString( "msg" ) );
        }
    }

    private void setData(List<ResultItem> data) {
        for (ResultItem item : data) {
            GameModel model = new GameModel();
            model.setGameId( item.getIntValue( "id" ) );
            model.setName( item.getString( "gamename" ) );
            model.setImgUrl( MyApplication.getHttpUrl().getBaseUrl() + item.getString( "logo" ) );
            model.setTime( item.getString( "newgame_time" ) );
            String label = item.getString( "label" );
            if (!TextUtils.isEmpty( label )) {
                String[] array = label.split( "," );
                model.setLabelArray( array );
            }
            model.setContent( item.getString( "content" ) );
            model.setReserved( item.getBooleanValue( "is_reserved", 1 ) );
            models.add( model );
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(int what, String error) {
        refreshLayout.setRefreshing( false );
        refreshLayout.setLoading( false );
        ToastUtils.showToast( this, error );
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity( new Intent( this, NewGameDetailsActivity.class )
                .putExtra( "id", String.valueOf( models.get( position ).getGameId() ) )
                .setAction( isNc ? "nc" : "yy" ) );
    }

    @Override
    public void onYYClick(GameModel gameModel) {
        if (!BeanUtils.isLogin()) {
            startActivity( new Intent( this, LoginActivity.class ) );
            return;
        }
        buildProgressDialog();
        HttpManager.newGameReserve( this, 22, new HttpResultListener() {
            @Override
            public void onSuccess(int what, ResultItem resultItem) {
                cancelProgressDialog();
                if (0 == resultItem.getIntValue( "status" )) {
									ToastUtils.showToast( ClosedBetaTestSubscribeActivity.this, "\u9884\u7ea6\u6210\u529f" );
                    RealmUtils.addReservedAlarm( gameModel );
                    ReservedUtils.addReserved( ClosedBetaTestSubscribeActivity.this, gameModel );
                    gameModel.setReserved( true );
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showToast( ClosedBetaTestSubscribeActivity.this, resultItem.getString( "msg" ) );
                }
            }

            @Override
            public void onError(int what, String error) {
                cancelProgressDialog();
                ToastUtils.showToast( ClosedBetaTestSubscribeActivity.this, error );
            }
        }, gameModel.getGameId() );
    }

    @Override
    protected void onDestroy() {
        Realm.getDefaultInstance().close();
        super.onDestroy();
    }
}
