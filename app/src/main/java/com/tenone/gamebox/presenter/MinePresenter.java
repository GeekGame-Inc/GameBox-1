/**
 * Project Name:GameBox
 * File Name:MinePresenter.java
 * Package Name:com.tenone.gamebox.presenter
 * Date:2017-3-14����10:17:10
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.MineBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.OnLoginStateChangeListener;
import com.tenone.gamebox.mode.listener.OnUserInfoChangeListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.MineView;
import com.tenone.gamebox.view.activity.AboutActivity;
import com.tenone.gamebox.view.activity.BindMobileActivity;
import com.tenone.gamebox.view.activity.CallCenterActivity;
import com.tenone.gamebox.view.activity.CoinDetailsActivity;
import com.tenone.gamebox.view.activity.ExchangePlatformActivity;
import com.tenone.gamebox.view.activity.GameDetailsActivity;
import com.tenone.gamebox.view.activity.GameDetailsNewActivity;
import com.tenone.gamebox.view.activity.GameTransferActivity;
import com.tenone.gamebox.view.activity.GoldCoinCenterActivity;
import com.tenone.gamebox.view.activity.LoginActivity;
import com.tenone.gamebox.view.activity.ManagementActivity;
import com.tenone.gamebox.view.activity.ModificationPwdActivity;
import com.tenone.gamebox.view.activity.MyAttentionActivity;
import com.tenone.gamebox.view.activity.MyGiftActivity;
import com.tenone.gamebox.view.activity.MyMessageActivity;
import com.tenone.gamebox.view.activity.OpeningVipActvity;
import com.tenone.gamebox.view.activity.PlatformCoinDetailActivity;
import com.tenone.gamebox.view.activity.PrivilegeActivity;
import com.tenone.gamebox.view.activity.RebateActivity;
import com.tenone.gamebox.view.activity.SettingActivity;
import com.tenone.gamebox.view.activity.ShareActivity;
import com.tenone.gamebox.view.activity.SignInActivity;
import com.tenone.gamebox.view.activity.TopActivity;
import com.tenone.gamebox.view.activity.UserInfoActivity;
import com.tenone.gamebox.view.activity.WebActivity;
import com.tenone.gamebox.view.adapter.MineItemAdapter;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TrackingUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinePresenter extends BasePresenter implements
        OnItemClickListener, OnClickListener,
        HttpResultListener, OnLoginStateChangeListener, OnUserInfoChangeListener {
    public static int max = 1;
    private MineBiz mineBiz;
    private MineView mineView;
    private Context mContext;
    private MineItemAdapter mAdapter;
    private List<Class> items = new ArrayList<Class>();
    private String headerPath = "";

    public MinePresenter(MineView view, Context cxt) {
        this.mContext = cxt;
        this.mineBiz = new MineBiz();
        this.mineView = view;
    }

    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new MineItemAdapter( mContext, items.size() + 1 );
        }
        getGridView().setAdapter( mAdapter );
    }

    public void init() {
        initClass();
        ListenerManager.registerOnLoginStateChangeListener( this );
        initBeforeLogin( !BeanUtils.isLogin() );
        HttpManager.userCenter( 18, mContext, this );
        ListenerManager.registerOnUserInfoChangeListener( this );
    }

    public void onResume() {
        HttpManager.userCenter( 18, mContext, this );
    }


    private void initClass() {
        items.add( SignInActivity.class );
        items.add( Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ?
                GameDetailsActivity.class : GameDetailsNewActivity.class );
        items.add( ShareActivity.class );
        items.add( TopActivity.class );
        items.add( ExchangePlatformActivity.class );
        items.add( WebActivity.class );
        items.add( CoinDetailsActivity.class );
        items.add( RebateActivity.class );
        items.add( GameTransferActivity.class );
        items.add( ManagementActivity.class );
        items.add( MyGiftActivity.class );
        items.add( MyAttentionActivity.class );
        items.add( PrivilegeActivity.class );
        items.add( MyMessageActivity.class );
        items.add( CallCenterActivity.class );
        items.add( ModificationPwdActivity.class );
        items.add( BindMobileActivity.class );
        if (is185()) {
            items.add( AboutActivity.class );
        }
        Log.i( "MinePresenter", "items size is " + items.size() );
    }

    private void initBeforeLogin(boolean isReset) {
        getHeaderLayout().setVisibility( isReset ? View.GONE : View.VISIBLE );
        getHeaderView2().setVisibility( isReset ? View.VISIBLE : View.GONE );
        setHeaderImage( isReset );
        getNickView().setText( isReset ? "���¼" : getNick() );
        vipTv().setVisibility(
                isReset ? View.VISIBLE
                        : (MyApplication.getInstance().isVip() ? View.GONE
                        : View.VISIBLE) );
        vipImg().setSelected(
						!isReset && MyApplication.getInstance().isVip() );
        shareEarningsTv().setText(
                getString( R.string.share_html, isReset ? "0" : MyApplication
                        .getInstance().getRecom_bonus() ) );
        goldTv().setText(
                getString( R.string.gold_html, isReset ? "0" : MyApplication
                        .getInstance().getCoin() ) );
        platformTv().setText(
                getString( R.string.platform_html, isReset ? "0" : MyApplication
                        .getInstance().getPlatform() ) );
        goldTv().setOnClickListener( isReset ? null : this );
        platformTv().setOnClickListener( isReset ? null : this );
        vipImg().setOnClickListener( isReset ? null : this );
    }

    private void setHeaderImage(boolean reset) {
        Message message = new Message();
        message.obj = reset;
        headerHandler.sendMessage( message );
    }

    private Spanned getString(int id, String str) {
        return Html.fromHtml( mContext.getResources().getString( id,
						str ) );
    }

    public void initListener() {
        getGridView().setOnItemClickListener( this );
        getHeaderView2().setOnClickListener( this );
        getHeaderView().setOnClickListener( this );
        settingImg().setOnClickListener( this );
        vipTv().setOnClickListener( this );
        settingImg2().setOnClickListener( this );
    }

    private ListView getGridView() {
        return mineView.getGridView();
    }

    private ImageView getHeaderView() {
        return mineView.getHeaderView();
    }

    private ImageView getHeaderView2() {
        return mineView.getHeaderView2();
    }

    private RelativeLayout getHeaderLayout() {
        return mineView.getHeaderLayout();
    }

    private TextView getNickView() {
        return mineView.getNickView();
    }

    private TextView shareEarningsTv() {
        return mineView.shareEarningsTv();
    }

    private TextView goldTv() {
        return mineView.goldTv();
    }

    private TextView platformTv() {
        return mineView.platformTv();
    }

    private ImageView settingImg() {
        return mineView.settingImg();
    }

    private TextView vipImg() {
        return mineView.vipImg();
    }

    private ImageView settingImg2() {
        return mineView.settingImg2();
    }

    public TextView vipTv() {
        return mineView.vipTv();
    }

    private String getHeaderUrl() {
        return mineBiz.getHeaderUrl();
    }

    private String getNick() {
        return mineBiz.getNick();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 80:
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (!BeanUtils.isLogin()) {
            openOtherActivity( mContext, new Intent( mContext,
                    LoginActivity.class ) );
            return;
        }
        Intent intent = null;
        if (position > 4) {
            intent = new Intent( mContext, items.get( position - 1 ) );
        } else if (position < 4) {
            intent = new Intent( mContext, items.get( position ) );
        } else {
            return;
        }
        switch (position) {
            case 1:
                int topGameId = MyApplication.getTopGameId();
                intent.putExtra( "id", topGameId+"" );
                intent.setAction( "mine" );
                break;
            case 6:
                intent.putExtra( "title", "��ҳ齱" );
                String url = MyApplication.getHttpUrl().getLuckyUrl() + "&uid="
                        + SpUtil.getUserId();
                intent.putExtra( "url", url );
                Map<String, Object> map = new HashMap<String, Object>();
                map.put( TrackingUtils.USERNAMEKEY, SpUtil.getAccount() );
                map.put( TrackingUtils.NICKNAMEKEY, SpUtil.getNick() );
                map.put( TrackingUtils.MOBILEKEY, SpUtil.getPhone() );
                TrackingUtils.setEvent( TrackingUtils.LUCKYDRAWEVENT, map );
                break;
            case 14:
                intent.putExtra( "tag", 0 );
                break;
            case 17:
                intent.setAction( isBindMobile() ? "unbind" : "bind" );
                break;
        }
        openOtherActivityForResult( mContext, 80, intent );
    }

    public boolean isBindMobile() {
        boolean isBind = false;
        isBind = (!TextUtils.isEmpty( SpUtil.getPhone() ))
                && BeanUtils.isMatchered( BeanUtils.PHONE_PATTERN,
                SpUtil.getPhone() );
        return isBind;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_mine_headerImg:
                openOtherActivity( mContext,
                        new Intent( mContext, UserInfoActivity.class ).putExtra( "uid", SpUtil.getUserId() ) );
                break;
            case R.id.id_mine_headerImg2:
                if (!BeanUtils.isLogin()) {
                    openOtherActivityForResult( mContext, 8, new Intent( mContext,
                            LoginActivity.class ) );
                }
            case R.id.id_mine_gold:
                if (BeanUtils.isLogin()) {
                    openOtherActivity( mContext, new Intent( mContext,
                            GoldCoinCenterActivity.class ) );
                }
                break;
            case R.id.id_mine_platform:
                if (BeanUtils.isLogin()) {
                    openOtherActivity( mContext, new Intent( mContext,
                            PlatformCoinDetailActivity.class ).setAction( "platform" ) );
                }
                break;
            case R.id.id_mine_setting:
                openOtherActivity( mContext, new Intent( mContext,
                        SettingActivity.class ) );
                break;
            case R.id.id_mine_setting2:
                openOtherActivity( mContext, new Intent( mContext,
                        SettingActivity.class ) );
                break;
            case R.id.id_mine_vip:// vip
                if (BeanUtils.isLogin()) {
                    openOtherActivity( mContext, new Intent( mContext,
                            OpeningVipActvity.class ) );
                } else {
                    openOtherActivityForResult( mContext, 8, new Intent( mContext,
                            LoginActivity.class ) );
                }
                break;
            case R.id.id_mine_vipTv:
                if (BeanUtils.isLogin()) {
                    openOtherActivity( mContext, new Intent( mContext,
                            OpeningVipActvity.class ) );
                } else {
                    openOtherActivityForResult( mContext, 8, new Intent( mContext,
                            LoginActivity.class ) );
                }
                break;
        }
    }


    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        if ("1".equals( resultItem.getString( "status" ) )) {
            switch (what) {
                case 18:
                    ResultItem item = resultItem.getItem( "data" );
                    String recom_top = item.getString( "recom_top" );
                    ResultItem sign_day_bonus = item.getItem( "sign_day_bonus" );
                    String platform_coin_ratio = item
                            .getString( "platform_coin_ratio" );
                    String pl_coin = item.getString( "pl_coin" );
                    String vipSign = sign_day_bonus.getString( "vip_extra" );
                    String sign = sign_day_bonus.getString( "normal" );
                    String mobile = item.getString( "mobile" );
                    String deplete_coin = item.getString( "deplete_coin" );
                    String topGold = item.getString( "rank_recom_top" );
                    mAdapter.setInstruction( sign, pl_coin, recom_top,
                            platform_coin_ratio, vipSign, mobile, deplete_coin, topGold );
                    if (!BeanUtils.isLogin()) {
                        return;
                    }
                    String platform_money = item.getString( "platform_money" );
                    String coin = item.getString( "coin" );
                    String recom_bonus = item.getString( "recom_bonus" );
                    int isVip = item.getIntValue( "is_vip" );
                    MyApplication.getInstance().setPlatform( platform_money );
                    MyApplication.getInstance().setCoin( coin );
                    MyApplication.getInstance().setRecom_bonus( recom_bonus );
                    MyApplication.getInstance().setVip( 1 == isVip );
                    initBeforeLogin( false );
                    break;
                default:
                    Message message = new Message();
                    message.what = what;
                    message.obj = resultItem;
                    handler2.sendMessage( message );
                    break;
            }
        } else {
            showToast( mContext, resultItem.getString( "msg" ) );
        }
    }

    @Override
    public void onError(int what, String error) {
        showToast( mContext, error );
    }

    @SuppressLint("HandlerLeak")
    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HttpType.LOADING:
                    ResultItem resultItem = ((ResultItem) msg.obj);
                    if (resultItem != null) {
                        SpUtil.setHeaderUrl( resultItem.getString( "data" ) );
                        setHeaderImage( false );
                    }
                    break;
            }
            super.handleMessage( msg );
        }
    };

    @Override
    public void onLoginStateChange(boolean isLogin) {
        if (isLogin) {
            HttpManager.userCenter( 18, mContext, this );
        } else {
            initBeforeLogin( true );
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler headerHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            boolean reset = (boolean) msg.obj;
            if (reset) {
                getHeaderView().setImageDrawable( null );
            } else {
                ImageLoadUtils.loadNormalImg( getHeaderView(), mContext,
                        getHeaderUrl() );
            }
            super.dispatchMessage( msg );
        }
    };

    @Override
    public void onUserInfoChange() {
        setHeaderImage( false );
    }
}
