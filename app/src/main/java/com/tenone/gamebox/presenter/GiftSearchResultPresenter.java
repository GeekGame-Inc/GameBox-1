package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.GiftSearchResultBiz;
import com.tenone.gamebox.mode.listener.GameGiftButtonClickListener;
import com.tenone.gamebox.mode.listener.GetGiftDialogConfirmListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnGiftItemClickListener;
import com.tenone.gamebox.mode.mode.GiftMode;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.GiftSearchResultView;
import com.tenone.gamebox.view.activity.GiftDetailsActivity;
import com.tenone.gamebox.view.adapter.GameGiftListAdapter;
import com.tenone.gamebox.view.adapter.SearchRecordWindowAdapter;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.custom.SearchTitleBarView;
import com.tenone.gamebox.view.custom.dialog.GetGiftDialog.Builder;
import com.tenone.gamebox.view.utils.CharSequenceUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.EncryptionUtils;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.NetworkUtils;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TelephoneUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class GiftSearchResultPresenter extends BasePresenter implements
        OnClickListener, HttpResultListener, GetGiftDialogConfirmListener,
        GameGiftButtonClickListener, OnRefreshListener, OnLoadListener, OnGiftItemClickListener {
    GiftSearchResultBiz biz;
    GiftSearchResultView view;
    Context mContext;
    GameGiftListAdapter mAdapter;
    List<GiftMode> giftModes = new ArrayList<GiftMode>();
    int page = 1;
    String condition;
    public static final int GETCODE = 3;
    int currentPosition = -1;
    Builder builder;
    List<String> allGameName = new ArrayList<String>();
    SearchRecordWindowAdapter adapter;

    AlertDialog alertDialog;

    public GiftSearchResultPresenter(GiftSearchResultView v, Context cxt) {
        this.mContext = cxt;
        this.view = v;
        this.biz = new GiftSearchResultBiz();
    }

    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new GameGiftListAdapter( giftModes, mContext );
        }
        mAdapter.setOnGiftItemClickListener( this );
        getListView().setAdapter( mAdapter );
    }

    public void initView() {
        alertDialog = buildProgressDialog( mContext );
        condition = gameName();
        allGameName.clear();
        allGameName.addAll( getAllGameName() );
        getSearchTitleBarView().setLeftImg( R.drawable.icon_back_grey );
        getSearchTitleBarView().setRightImg( R.drawable.ic_sousuo );
        adapter = new SearchRecordWindowAdapter( mContext, allGameName, "" );
        CustomizeEditText editText = getSearchTitleBarView()
                .getCustomizeEditText();
        editText.setText( gameName() );
        editText.setSelection( gameName().length() );
        editText.setAdapter( adapter );
        editText.setDropDownWidth( DisplayMetricsUtils.getScreenWidth( mContext ) );
        editText.setDropDownVerticalOffset( DisplayMetricsUtils.dipTopx(
                mContext, 3 ) );
    }

    public void initListener() {
        getSearchTitleBarView().getLeftImg().setOnClickListener( this );
        getSearchTitleBarView().getRightImg().setOnClickListener( this );
        mAdapter.setButtonClickListener( this );
        getRefreshLayout().setOnRefreshListener( this );
        getRefreshLayout().setOnLoadListener( this );
    }

    public void requestList(int what) {
        String url = MyApplication.getHttpUrl().getGetPacksList();
        RequestBody requestBody = new FormBody.Builder()
                .add( "channel_id",
                        MyApplication.getConfigModle().getChannelID() )
                .add( "page", page + "" ).add( "username", SpUtil.getAccount() )
                .add( "device_id", TelephoneUtils.getImei( mContext ) )
                .add( "search", condition ).build();
        HttpUtils.postHttp( mContext, what, url, requestBody, this );
    }

    private void requestCode(int what, int id) {
        String url = MyApplication.getHttpUrl().getGetPack();
        Map<String, String> map = new HashMap<String, String>();
        map.put( "username", SpUtil.getAccount() );
        map.put( "ip", NetworkUtils.getLocalIpAddress() );
        map.put( "terminal_type", 2 + "" );
        map.put( "pid", id + "" );
        map.put( "device_id", TelephoneUtils.getImei( mContext ) );

        RequestBody requestBody = new FormBody.Builder()
                .add( "channel_id",
                        MyApplication.getConfigModle().getChannelID() )
                .add( "username", SpUtil.getAccount() )
                .add( "device_id", TelephoneUtils.getImei( mContext ) )
                .add( "ip", NetworkUtils.getLocalIpAddress() )
                .add( "terminal_type", 2 + "" ).add( "pid", id + "" )
                .add( "sign", EncryptionUtils.getSingTure( map ) ).build();
        HttpUtils.postHttp( mContext, what, url, requestBody, this );
        map = null;
    }

    public List<String> getAllGameName() {
        return biz.getAllGameName();
    }

    public String gameName() {
        return biz.gameName( getIntent() );
    }

    public SearchTitleBarView getSearchTitleBarView() {
        return view.getSearchTitleBarView();
    }

    public List<GiftMode> getGiftModes(ResultItem resultItem) {
        return biz.getGiftModes( resultItem );
    }

    public RefreshLayout getRefreshLayout() {
        return view.getRefreshLayout();
    }

    public ListView getListView() {
        return view.getListView();
    }

    public Intent getIntent() {
        return view.getIntent();
    }

    private void showDialog() {
        if (builder == null) {
            builder = new Builder( mContext );
        }
        builder.setButtonText( mContext.getResources().getString(
                R.string.confirm ) );
        builder.setConfirmListener( this );
        builder.setTitle( mContext.getResources().getString(
                R.string.codeCopyHintTitle ) );
        builder.setMessage( mContext.getResources().getString(
                R.string.codeCopyHint ) );
        builder.showDialog();
    }

    public void saveRecord(String record) {
        biz.saveRecord( record );
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
                alertDialog = buildProgressDialog( mContext );
                break;
        }
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        cancelProgressDialog( alertDialog );
        getRefreshLayout().setRefreshing( false );
        getRefreshLayout().setLoading( false );
        if ("0".equals( resultItem.getString( "status" ) )) {
            Message message = new Message();
            message.obj = resultItem;
            message.what = what;
            handler.sendMessage( message );
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

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ResultItem resultItem = (ResultItem) msg.obj;
            switch (msg.what) {
                case HttpType.REFRESH:
                    giftModes.clear();
                    giftModes.addAll( getGiftModes( resultItem.getItem( "data" ) ) );
                    break;
                case HttpType.LOADING:
                    giftModes.addAll( getGiftModes( resultItem.getItem( "data" ) ) );
                    break;
                case GETCODE:
                    GiftMode giftMode = giftModes.get( currentPosition );
                    giftMode.setGiftCode( resultItem.getString( "data" ) );
                    giftMode.setState( 1 );
                    break;
            }
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onConfirmClick(AlertDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onButtonClick(GiftMode giftMode) {
        currentPosition = giftModes.indexOf( giftMode );
        if (giftMode.getState() == 0) {
            requestCode( GETCODE, giftMode.getGiftId() );
        } else {
            if (CharSequenceUtils.CopyToClipboard( mContext,
                    giftMode.getGiftCode() )) {
                showDialog();
            } else {
                Toast.makeText( mContext, "��ȡ��������", Toast.LENGTH_SHORT ).show();
            }
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
    public void onGiftItemClick(GiftMode mode) {
        openOtherActivity( mContext,
                new Intent( mContext, GiftDetailsActivity.class )
                        .putExtra( "giftId", mode.getGiftId() ) );
    }
}
