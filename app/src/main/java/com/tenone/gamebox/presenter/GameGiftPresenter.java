/**
 * Project Name:GameBox
 * File Name:GameGiftPresenter.java
 * Package Name:com.tenone.gamebox.presenter
 * Date:2017-3-10����5:53:34
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

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
import android.widget.TextView;
import android.widget.Toast;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.GameGiftBiz;
import com.tenone.gamebox.mode.listener.GameGiftButtonClickListener;
import com.tenone.gamebox.mode.listener.GameGiftListener;
import com.tenone.gamebox.mode.listener.GetGiftDialogConfirmListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnGiftItemClickListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.mode.GiftMode;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.GameGiftView;
import com.tenone.gamebox.view.activity.GiftDetailsActivity;
import com.tenone.gamebox.view.activity.LoginActivity;
import com.tenone.gamebox.view.activity.ManagementActivity;
import com.tenone.gamebox.view.activity.MyMessageActivity;
import com.tenone.gamebox.view.activity.TradingSearchActivity;
import com.tenone.gamebox.view.adapter.GameGiftListAdapter;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.custom.bga.BGABadgeView;
import com.tenone.gamebox.view.custom.dialog.GetGiftDialog.Builder;
import com.tenone.gamebox.view.receiver.DownActionReceiver;
import com.tenone.gamebox.view.receiver.DownActionReceiver.DownActivonListener;
import com.tenone.gamebox.view.receiver.WarnReceiver;
import com.tenone.gamebox.view.receiver.WarnReceiver.WarnNotifiacticonListener;
import com.tenone.gamebox.view.utils.CharSequenceUtils;
import com.tenone.gamebox.view.utils.DatabaseUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TelephoneUtils;
import com.tenone.gamebox.view.utils.cache.ACache;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * ClassName:GameGiftPresenter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-10 ����5:53:34 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */
public class GameGiftPresenter extends BasePresenter implements
        GameGiftButtonClickListener, GetGiftDialogConfirmListener,
        OnClickListener, HttpResultListener, OnRefreshListener, OnLoadListener,
        WarnNotifiacticonListener, DownActivonListener, GameGiftListener, OnGiftItemClickListener {

    GameGiftBiz gameGiftBiz;

    GameGiftView gameGiftView;

    Context mContext;

    GameGiftListAdapter mAdapter;

    Builder builder;
    List<GiftMode> giftModes = new ArrayList<GiftMode>();
    public static final int GETCODE = 3;
    int page = 1;
    int currentPosition = -1;

    WarnReceiver warnReceiver;
    DownActionReceiver actionReceiver;
    BGABadgeView messageBadge;
    BGABadgeView downBadge;
    AlertDialog alertDialog;
    ACache cache;

    public GameGiftPresenter(GameGiftView view, Context cxt) {
        this.gameGiftBiz = new GameGiftBiz();
        this.gameGiftView = view;
        this.mContext = cxt;
        cache = ACache.get( mContext );
        getCache();
    }

    /**
     * ��ʼ�������� setAdapter:(������һ�仰�����������������). <br/>
     *
     * @author John Lie
     * @since JDK 1.6
     */
    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new GameGiftListAdapter( giftModes, mContext );
        }
        mAdapter.setButtonClickListener( this );
        mAdapter.setOnGiftItemClickListener( this );
        getRecommendListView().setAdapter( mAdapter );
    }

    public void getCache() {
        ResultItem resultItem = (ResultItem) cache.getAsObject( "gift" );
        if (resultItem != null) {
            giftModes.clear();
            getGameRecommends( resultItem.getItem( "data" ) );
        }
    }

    public void initView() {
        alertDialog = buildProgressDialog( mContext );
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
    }

    /**
     * ��ʼ�� init:(������һ�仰�����������������). <br/>
     *
     * @author John Lie
     * @since JDK 1.6
     */
    public void initListener() {
        getSerachBar().findViewById( R.id.id_main_search ).setOnClickListener(
                this );
        getSerachBar().findViewById( R.id.id_main_down ).setOnClickListener( this );
        getSerachBar().findViewById( R.id.id_main_message ).setOnClickListener(
                this );
        getRefreshLayout().setOnRefreshListener( this );
        getRefreshLayout().setOnLoadListener( this );
        warnReceiver = new WarnReceiver();
        registerWarnReceiver( mContext, warnReceiver );
        warnReceiver.setWarnNotifiacticonListener( this );
        actionReceiver = new DownActionReceiver();
        registerDownloadActionReceiver( mContext, actionReceiver );
        actionReceiver.setDownActivonListener( this );
    }

    /**
     * �������� request:(������һ�仰�����������������). <br/>
     *
     * @author John Lie
     * @since JDK 1.6
     */
    public void requestList(int what) {
        String url = MyApplication.getHttpUrl().getGetPacksList();
        RequestBody requestBody = new FormBody.Builder()
                .add( "channel_id",
                        MyApplication.getConfigModle().getChannelID() )
                .add( "page", page + "" ).add( "username", SpUtil.getAccount() + "" )
                .add( "device_id", TelephoneUtils.getImei( mContext ) ).build();
        HttpUtils.postHttp( mContext, what, url, requestBody, this );
    }

    /**
     * ��ȡ����� requestCode:(������һ�仰�����������������). <br/>
     *
     * @author John Lie
     * @since JDK 1.6
     */
    private void requestCode(int what, int id) {
        HttpManager.getPack( what, mContext, this, id + "" );
    }

    /* ��ȡ��ͼ */
    public Intent getIntent() {
        return gameGiftView.getIntent();
    }

    /* ��ȡ������ */
    public View getSerachBar() {
        return gameGiftView.getSerachBar();
    }

    /* ��ȡˢ�¿ؼ� */
    public RefreshLayout getRefreshLayout() {
        return gameGiftView.getRefreshLayout();
    }

    /* ��ȡ�б� */
    public ListView getRecommendListView() {
        return gameGiftView.getRecommendListView();
    }

    public TextView getDownBadgeTv() {
        return gameGiftView.getDownBadgeTv();
    }

    public TextView getMessageBadgeTv() {
        return gameGiftView.getMessageBadgeTv();
    }

    /* ��ȡ���� */
    public void getGameRecommends(ResultItem resultItem) {
        gameGiftBiz.constructArray( mContext, resultItem, this );
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
    public void onConfirmClick(AlertDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_main_search:
                openOtherActivity( mContext, new Intent( mContext,
                        TradingSearchActivity.class ) );
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
                        MyMessageActivity.class ) );
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
        getRefreshLayout().setRefreshing( false );
        getRefreshLayout().setLoading( false );
        showToast( mContext, error );
    }

    /* ���ݴ���handler */
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ResultItem resultItem = (ResultItem) msg.obj;
            what = msg.what;
            switch (msg.what) {
                case HttpType.REFRESH:
                    cache.put( "gift", resultItem );
                    mAdapter.notifyDataSetChanged();
                    getGameRecommends( resultItem.getItem( "data" ) );
                    break;
                case HttpType.LOADING:
                    getGameRecommends( resultItem.getItem( "data" ) );
                    break;
                case GETCODE:
                    GiftMode giftMode = giftModes.get( currentPosition );
                    giftMode.setGiftCode( resultItem.getString( "data" ) );
                    giftMode.setState( 1 );
                    showToast( mContext, "�����ȡ�ɹ�" );
                    mAdapter.notifyDataSetChanged();
                    break;
            }

        }
    };

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
    public void downAction(int size) {
        if (size > 0) {
            if (downBadge == null) {
                downBadge = showDownBadgeView( mContext, getDownBadgeTv(), size
                        + "" );
            } else {
                if (!downBadge.isShown()) {
                    downBadge.show( true );
                }
                downBadge.setText( size + "" );
            }
        } else {
            if (downBadge != null && downBadge.isShown()) {
                downBadge.hide( true );
            }
        }
    }

    @Override
    public void notificaticonFun(boolean b) {
        // �п���֪ͨ
        if (b) {
            if (messageBadge == null || !messageBadge.isShown()) {
                messageBadge = showMessageBadgeView( mContext,
                        getMessageBadgeTv(), "" );
            }
        } else {
            if (messageBadge != null && messageBadge.isShown()) {
                messageBadge.hide( true );
            }
        }
    }

    int what;

    @Override
    public void updateUI(List<GiftMode> modes) {
        if (HttpType.REFRESH == what) {
            giftModes.clear();
        }
        giftModes.addAll( modes );
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onGiftItemClick(GiftMode mode) {
        openOtherActivity( mContext, new Intent( mContext, GiftDetailsActivity.class )
                .putExtra( "giftId", mode.getGiftId() ) );
    }
}
