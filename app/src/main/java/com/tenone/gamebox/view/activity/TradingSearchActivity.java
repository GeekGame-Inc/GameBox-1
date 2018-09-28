package com.tenone.gamebox.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.adapter.NewSearchResultAdapter;
import com.tenone.gamebox.view.adapter.SearchRecordWindowAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.ToastUtils;
import com.thoughtworks.xstream.mapper.Mapper;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class TradingSearchActivity extends BaseActivity implements AdapterView.OnItemClickListener, HttpResultListener {

    @ViewInject(R.id.id_trading_search_editText)
    CustomizeEditText editText;

    @ViewInject(R.id.id_trading_search_list)
    ListView listView;

    private SearchRecordWindowAdapter adapter;
    private List<String> allGameName = new ArrayList<String>();

    private List<GameModel> items = new ArrayList<GameModel>();
    private NewSearchResultAdapter gameAdapter;

    private String gameName;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate( arg0 );
        setContentView( R.layout.activity_trading_search );
        ViewUtils.inject( this );
        initView();
    }

    private void initView() {
        allGameName.addAll( Configuration.getAllGameNames() );
        adapter = new SearchRecordWindowAdapter( this, allGameName, "" );
        editText.setAdapter( adapter );
        editText.setDropDownWidth( DisplayMetricsUtils.getScreenWidth( this ) );
        editText.setDropDownVerticalOffset( DisplayMetricsUtils.dipTopx( this, 11 ) );
        editText.setOnEditorActionListener( (v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                gameName = editText.getText().toString();
                if (TextUtils.isEmpty( gameName )) {
									ToastUtils.showToast( this, "\u8bf7\u8f93\u5165\u8981\u641c\u7d22\u7684\u6e38\u620f\u540d" );
                    return true;
                }
                requestList();
                return true;
            }
            return false;
        } );
        gameAdapter = new NewSearchResultAdapter( items, this );
        listView.setAdapter( gameAdapter );
        listView.setOnItemClickListener( this );
    }


    @OnClick({R.id.id_trading_search_search, R.id.id_trading_search_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_trading_search_search:
                gameName = editText.getText().toString();
                if (TextUtils.isEmpty( gameName )) {
									ToastUtils.showToast( this, "\u8bf7\u8f93\u5165\u8981\u641c\u7d22\u7684\u6e38\u620f\u540d" );
                    return;
                }
                requestList();
                break;
            case R.id.id_trading_search_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GameModel model = items.get( position );
        Intent intent = getIntent();
        intent.putExtra( "gameName", model.getName() );
        setResult( RESULT_OK, intent );
        finish();
    }

    private void requestList() {
        buildProgressDialog();
        String url = MyApplication.getHttpUrl().getGameSearchList();
        RequestBody requestBody = new FormBody.Builder()
                .add( "channel", MyApplication.getConfigModle().getChannelID() )
                .add( "system", "1" )
                .add( "keyword", gameName )
                .add( "page", 1 + "" )
                .build();
        HttpUtils.postHttp( this, HttpType.REFRESH, url, requestBody, this );
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        cancelProgressDialog();
        if ("0".equals( resultItem.getString( "status" ) )) {
            items.clear();
            List<ResultItem> list = resultItem.getItems( "data" );
            if (!BeanUtils.isEmpty( list )) {
                new AsyncTask<List<ResultItem>, Mapper.Null, List<GameModel>>() {
                    @Override
                    protected List<GameModel> doInBackground(List<ResultItem>... lists) {
                        List<ResultItem> list = lists[0];
                        return getGameModels( list );
                    }

                    @Override
                    protected void onPostExecute(List<GameModel> gameModels) {
                        super.onPostExecute( gameModels );
                        items.addAll( gameModels );
                        gameAdapter.notifyDataSetChanged();
                    }
                }.execute( list );
            }
        } else {
            ToastUtils.showToast( this, resultItem.getString( "msg" ) );
        }
    }

    @Override
    public void onError(int what, String error) {
        cancelProgressDialog();
        ToastUtils.showToast( this, error );
    }

    private List<GameModel> getGameModels(List<ResultItem> resultItems) {
        List<GameModel> items = new ArrayList<GameModel>();
        for (ResultItem data : resultItems) {
            GameModel model = new GameModel();
            model.setName( data.getString( "gamename" ) );
            String id = data.getString( "id" );
            if (!TextUtils.isEmpty( id )) {
                model.setGameId( Integer.valueOf( id ).intValue() );
            }
            model.setPlatform( data.getIntValue( "platform" ) );
            model.setDis( data.getString( "discount" ) );
            items.add( model );
        }
        return items;
    }
}
