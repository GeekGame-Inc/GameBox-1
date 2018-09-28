/**
 * Project Name:GameBox
 * File Name:RecommentGamePresenter.java
 * Package Name:com.tenone.gamebox.presenter
 * Date:2017-3-27����1:53:36
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.RecommentGameGiftBiz;
import com.tenone.gamebox.mode.listener.GameGiftButtonClickListener;
import com.tenone.gamebox.mode.listener.GetGiftDialogConfirmListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnGiftItemClickListener;
import com.tenone.gamebox.mode.mode.BannerModel;
import com.tenone.gamebox.mode.mode.GiftMode;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.RecommentGameGiftView;
import com.tenone.gamebox.view.activity.GameDetailsActivity;
import com.tenone.gamebox.view.activity.GiftDetailsActivity;
import com.tenone.gamebox.view.activity.LoginActivity;
import com.tenone.gamebox.view.activity.MyGiftActivity;
import com.tenone.gamebox.view.activity.TradingSearchActivity;
import com.tenone.gamebox.view.adapter.GameGiftListAdapter;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.dialog.GetGiftDialog.Builder;
import com.tenone.gamebox.view.custom.xbanner.XBanner;
import com.tenone.gamebox.view.custom.xbanner.XBanner.OnItemClickListener;
import com.tenone.gamebox.view.custom.xbanner.XBanner.XBannerAdapter;
import com.tenone.gamebox.view.utils.CharSequenceUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.EncryptionUtils;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.NetworkUtils;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TelephoneUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * ClassName:RecommentGamePresenter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-27 ����1:53:36 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */
public class RecommentGameGiftPresenter extends BasePresenter implements
        GameGiftButtonClickListener, OnClickListener, HttpResultListener,
        XBannerAdapter, OnItemClickListener, GetGiftDialogConfirmListener,
        OnRefreshListener, OnLoadListener, OnGiftItemClickListener {
    RecommentGameGiftBiz giftBiz;
    RecommentGameGiftView giftView;
    Context mContext;
    GameGiftListAdapter mAdapter;
    Builder builder;
    CustomizeEditText linearLayout;
    XBanner banner;
    int page = 1;
    List<GiftMode> items = new ArrayList<GiftMode>();
    public static final int BANNER = 3;
    public static final int GETCODE = 4;
    List<BannerModel> bannerModels = new ArrayList<BannerModel>();
    int currentPosition = -1;
    AlertDialog alertDialog;

    public RecommentGameGiftPresenter(RecommentGameGiftView v, Context cxt) {
        this.giftView = v;
        this.mContext = cxt;
        this.giftBiz = new RecommentGameGiftBiz();
    }

    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new GameGiftListAdapter( items, mContext );
        }
        getListView().setAdapter( mAdapter );
        mAdapter.setButtonClickListener( this );
        mAdapter.setOnGiftItemClickListener( this );
    }

    @SuppressLint("InflateParams")
    public void initView() {
        alertDialog = buildProgressDialog( mContext );
        getTitleBarView().setLeftTwoText( R.string.gift );
        getTitleBarView().setLeftImg( R.drawable.icon_back_grey );
        getTitleBarView().setRightText( R.string.myGift );
        // ���ͷ��
        View headView = LayoutInflater.from( mContext ).inflate(
                R.layout.layout_game_gift_header, getListView(), false );
        linearLayout = headView
                .findViewById( R.id.id_search_editText );
        getListView().addHeaderView( headView );
        banner = headView.findViewById( R.id.id_banner );
        ViewGroup.LayoutParams params = banner.getLayoutParams();
        params.width = DisplayMetricsUtils.getScreenWidth( mContext );
        params.height = (params.width * 19) / 36;
        banner.setLayoutParams( params );
    }

    /* ��ʼ������ */
    public void initListener() {
        getTitleBarView().getLeftImg().setOnClickListener( this );
        getTitleBarView().getRightText().setOnClickListener( this );
        linearLayout.setOnClickListener( this );
        banner.setOnItemClickListener( this );
        getRefreshLayout().setOnRefreshListener( this );
        getRefreshLayout().setOnLoadListener( this );
        getListView().setOnItemClickListener( ((parent, view, position, id) -> {
            if (position > 0) {
                openOtherActivity( mContext, new Intent( mContext, GiftDetailsActivity.class )
                        .putExtra( "giftId", items.get( position - 1 ).getGiftId() ) );
            }
        }) );
    }

    /**
     * ��ȡ���� requestHttp:(������һ�仰�����������������). <br/>
     *
     * @param what
     * @author John Lie
     * @since JDK 1.6
     */
    public void requestHttp(int what) {
        String url = MyApplication.getHttpUrl().getGetPacksList();
        RequestBody requestBody = new FormBody.Builder()
                .add( "channel_id",
                        MyApplication.getConfigModle().getChannelID() )
                .add( "page", page + "" ).add( "username", SpUtil.getAccount() )
                .add( "device_id", TelephoneUtils.getImei( mContext ) ).build();
        HttpUtils.postHttp( mContext, what, url, requestBody, this );
    }

    /**
     * ��ȡ���� requestHttp:(������һ�仰�����������������). <br/>
     *
     * @param what
     * @author John Lie
     * @since JDK 1.6
     */
    public void requestBanner(int what) {
        String url = MyApplication.getHttpUrl().getSlide();
        RequestBody requestBody = new FormBody.Builder().add( "channel_id",
                MyApplication.getConfigModle().getChannelID() ).build();
        HttpUtils.postHttp( mContext, what, url, requestBody, this );
    }

    /**
     * ��ȡ����� requestCode:(������һ�仰�����������������). <br/>
     *
     * @author John Lie
     * @since JDK 1.6
     */
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

    /**
     * ���ù��ͼ���� setBannerData:(������һ�仰�����������������). <br/>
     *
     * @author John Lie
     * @since JDK 1.6
     */
    public void setBannerData(List<BannerModel> bannerArray) {
        banner.setData( bannerArray, null );
        banner.setmAdapter( this );
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

    public TitleBarView getTitleBarView() {
        return giftView.getTitleBarView();
    }

    public RefreshLayout getRefreshLayout() {
        return giftView.getRefreshLayout();
    }

    public ListView getListView() {
        return giftView.getListView();
    }

    public LinearLayout getLinearLayout() {
        return giftView.getLinearLayout();
    }

    public List<GiftMode> getModes(List<ResultItem> resultItems) {
        return giftBiz.getModes( resultItems );
    }

    /**
     * ��ȡ���ͼ���� getBannerArray:(������һ�仰�����������������). <br/>
     *
     * @param resultItem
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public List<BannerModel> getBannerArray(List<ResultItem> resultItem) {
        return giftBiz.constructBannerArray( resultItem );
    }

    @Override
    public void onButtonClick(GiftMode giftMode) {
        currentPosition = items.indexOf( giftMode );
        if (giftMode.getState() == 0) {
            requestCode( GETCODE, giftMode.getGiftId() );
            alertDialog = buildProgressDialog( mContext );
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_titleBar_leftImg:
                // ����
                close( mContext );
                break;
            case R.id.id_titleBar_rightText:
                if ("0".equals( SpUtil.getUserId() )) {
                    openOtherActivity( mContext, new Intent( mContext,
                            LoginActivity.class ) );
                } else {
                    // �ҵ����
                    openOtherActivity( mContext, new Intent( mContext,
                            MyGiftActivity.class ) );
                }
                break;
            case R.id.id_search_editText:
                // ����
                openOtherActivity( mContext, new Intent( mContext,
                        TradingSearchActivity.class ) );
                break;
        }
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
                    ResultItem data = resultItem.getItem( "data" );
                    if (data != null) {
                        List<ResultItem> list = data.getItems( "list" );
                        if (list != null) {
                            items.clear();
                            items.addAll( getModes( list ) );
                        }
                    }
                    break;
                case HttpType.LOADING:
                    ResultItem item = resultItem.getItem( "data" );
                    if (item != null) {
                        List<ResultItem> list = item.getItems( "list" );
                        if (list != null) {
                            items.addAll( getModes( list ) );
                        }
                    }
                    break;
                case BANNER:
                    List<ResultItem> resultItems = resultItem.getItems( "data" );
                    bannerModels.clear();
                    bannerModels.addAll( getBannerArray( resultItems ) );
                    setBannerData( bannerModels );
                    break;
                case GETCODE:
                    GiftMode giftMode = items.get( currentPosition );
                    giftMode.setGiftCode( resultItem.getString( "data" ) );
                    giftMode.setState( 1 );
                    showToast( mContext, "��ȡ�����ɹ�" );
                    break;
            }
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void loadBanner(XBanner banner, Object model, View view, int position) {
        BannerModel bannerModel = (BannerModel) model;
        String str = bannerModel.getImageUrl();
        ImageLoadUtils.loadBannerImg( (ImageView) view, mContext, str );
    }

    @Override
    public void onItemClick(XBanner banner, int position) {
        openOtherActivity( mContext, new Intent( mContext,
                GameDetailsActivity.class ).putExtra( "id",
                bannerModels.get( position ).getGameId() + "" ) );
    }

    @Override
    public void onConfirmClick(AlertDialog dialog) {
        dialog.dismiss();
    }

    @Override
    public void onGiftItemClick(GiftMode mode) {
        openOtherActivity( mContext, new Intent( mContext, GiftDetailsActivity.class )
                .putExtra( "giftId", mode.getGiftId() ) );
    }
}
