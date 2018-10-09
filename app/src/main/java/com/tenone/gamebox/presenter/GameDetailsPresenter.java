/**
 * Project Name:GameBox
 * File Name:GameDetailsPresenter.java
 * Package Name:com.tenone.gamebox.presenter
 * Date:2017-3-30����3:29:41
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.GameDetailsBiz;
import com.tenone.gamebox.mode.listener.ApkInstallListener;
import com.tenone.gamebox.mode.listener.ApkInstallListener.InstallListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.GameDetailsView;
import com.tenone.gamebox.share.SharePopupWindow;
import com.tenone.gamebox.view.activity.LoginActivity;
import com.tenone.gamebox.view.activity.ManagementActivity;
import com.tenone.gamebox.view.adapter.ManagementAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.CustomerUnderlinePageIndicator;
import com.tenone.gamebox.view.custom.DownProgress;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.TwoStateImageView;
import com.tenone.gamebox.view.custom.bga.BGABadgeView;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;
import com.tenone.gamebox.view.receiver.DownActionReceiver;
import com.tenone.gamebox.view.receiver.DownReceiver;
import com.tenone.gamebox.view.receiver.DownReceiver.DownStatusChangeListener;
import com.tenone.gamebox.view.utils.ApkUtils;
import com.tenone.gamebox.view.utils.DatabaseUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TrackingUtils;
import com.tenone.gamebox.view.utils.WindowUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import okhttp3.FormBody;
import okhttp3.RequestBody;

@SuppressLint("HandlerLeak")
public class GameDetailsPresenter extends BasePresenter implements
        OnClickListener, DownStatusChangeListener, InstallListener,
        HttpResultListener, PlatformActionListener, DownActionReceiver.DownActivonListener {
    private GameDetailsView detailsView;
    private GameDetailsBiz detailsBiz;
    private Context mContext;
    private ManagementAdapter mAdapter;
    private List<String> title = new ArrayList<String>();
    private float width = 0, widthOffset = 0;
    private ApkInstallListener installListener;
    private DownReceiver receiver;
    private GameModel gameModel;

    private SharePopupWindow sharePopupWindow;
    private AlertDialog alertDialog;
    private static final int COLLECT = 5;
    private String qqUrl, commenCount = "0";

    private BGABadgeView downBadge;
    private DownActionReceiver actionReceiver;


    public GameDetailsPresenter(GameDetailsView v, Context cxt) {
        this.detailsView = v;
        this.mContext = cxt;
        this.detailsBiz = new GameDetailsBiz();
        alertDialog = buildProgressDialog( mContext );
    }

    public void setAdapter(Activity activity, ResultItem resultItem) {
        List<String> array = getTitles( R.array.game_details_titles );
        for (int i = 0; i < array.size(); i++) {
            if (i == 1) {
                String str = array.get( i );
                str = str + "(" + commenCount + ")";
                array.set( i, str );
            }
        }
        title.addAll( array );
        if (mAdapter == null) {
            mAdapter = new ManagementAdapter(
                    ((FragmentActivity) activity).getSupportFragmentManager() );
        }
        mAdapter.setArray( getFragments( resultItem ) );
        mAdapter.setmTitleList( title );
        getViewPager().setAdapter( mAdapter );
    }

    public void initView() {
        getTitleBarView().setLeftImg( R.drawable.icon_back_grey );
        getTabPageIndicator().setViewPager( getViewPager() );
        getTitleBarView().setRigthImg( R.drawable.ic_xiazai );
        initDownBadge();
        getPageIndicator().setViewPager( getViewPager() );
        getPageIndicator().setFades( false );
        getTabPageIndicator().setOnPageChangeListener( getPageIndicator() );
        width = getTabPageIndicator().getTextWidth();
        widthOffset = (DisplayMetricsUtils.getScreenWidth( mContext )
                / mAdapter.getCount() - width) / 2;
        getPageIndicator().setDefultWidth( width );
        getPageIndicator().setDefultOffset( widthOffset );
        getViewPager().setOffscreenPageLimit( 4 );
        getViewPager().setCurrentItem( ("mine".equals( getIntent().getAction() )) ? 1 : 0 );
        if (!TextUtils.isEmpty( qqUrl )) {
            getQQLayout().setVisibility( View.VISIBLE );
        }
        getShareBt().setVisibility( is185() ? View.VISIBLE : View.INVISIBLE );
    }

    private void initDownBadge() {
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
            downBadge = showDownBadgeView( mContext, getTitleBarView().getRightImg(),
                    a + "", BGABadgeView.POSITION_TOP_RIGHT );
        }
    }

    public void requestHttp(int what) {
        String url = MyApplication.getHttpUrl().getGameInfo();
        RequestBody requestBody = new FormBody.Builder()
                .add( "channel", MyApplication.getConfigModle().getChannelID() )
                .add( "system", "1" ).add( "gid", getGameId() )
                .add( "username", SpUtil.getAccount() ).build();
        HttpUtils.postHttp( mContext, what, url, requestBody, this );
    }

    private void collect(int what, int type) {
        String url = MyApplication.getHttpUrl().getGameCollect();
        RequestBody requestBody = new FormBody.Builder().add( "type", type + "" )
                .add( "gid", getGameId() ).add( "username", SpUtil.getAccount() )
                .build();
        HttpUtils.postHttp( mContext, what, url, requestBody, this );
    }

    private void initShow(GameModel gameModel, ResultItem resultItem) {
        getTitleBarView().setTitleText( gameModel.getName() );
        // ��ʼ����ʾ
        ImageLoadUtils.loadNormalImg( getImageView(), mContext,
                gameModel.getImgUrl() );
        getNameTv().setText( gameModel.getName() );
        getSizeTv().setText(
                gameModel.getTimes() + "����     " + gameModel.getSize() + "M" );
        getRatingBar().setRating( gameModel.getGrade() );
        getCollectBt().setState( gameModel.isCollectde() ? 1 : 0 );
        getProgress().setStatus( gameModel.getStatus() );
        getProgress().setProgress( gameModel.getProgress() );
        String[] labels = gameModel.getLabelArray();
        if (labels != null) {
            for (int i = 0; i < labels.length; i++) {
                if (i < 3) {
                    String label = labels[i];
                    TextView textView = new TextView( mContext );
                    textView.setText( label );
                    textView.setTextSize( TypedValue.COMPLEX_UNIT_SP, 10 );
                    textView.setTextColor( ContextCompat.getColor(mContext, R.color.underLineColor ) );
                    textView.setBackground( mContext.getResources().getDrawable(
                            R.drawable.radius_underline_border ) );
                    textView.setPadding(
                            DisplayMetricsUtils.dipTopx( mContext, 5 ),
                            DisplayMetricsUtils.dipTopx( mContext, 2 ),
                            DisplayMetricsUtils.dipTopx( mContext, 5 ),
                            DisplayMetricsUtils.dipTopx( mContext, 2 ) );
                    getLabelLayout().addView( textView );
                    LayoutParams params = (LayoutParams) textView
                            .getLayoutParams();
                    params.setMargins( DisplayMetricsUtils.dipTopx( mContext, 3 ),
                            0, 0, 0 );
                    textView.setLayoutParams( params );
                } else {
                    break;
                }
            }
        }
        setAdapter( (BaseActivity) mContext, resultItem );
        initView();
    }

    public void initListener() {
        getTitleBarView().getLeftImg().setOnClickListener( this );
        getTitleBarView().getRightImg().setOnClickListener( this );
        getCollectBt().setOnClickListener( this );
        getProgress().setOnClickListener( this );
        getShareBt().setOnClickListener( this );
        getQQLayout().setOnClickListener( this );
        receiver = new DownReceiver();
        installListener = new ApkInstallListener();
        registerDownReceiver( mContext, this, receiver );
        registerInstallReceiver( mContext, this, installListener );
        actionReceiver = new DownActionReceiver();
        registerDownloadActionReceiver( mContext, actionReceiver );
        actionReceiver.setDownActivonListener( this );
        HttpManager.commentCounts( 1, mContext, this, getGameId() );
    }

    public void onDestroy() {
        if (installListener != null) {
            mContext.unregisterReceiver( installListener );
        }
        if (receiver != null) {
            mContext.unregisterReceiver( receiver );
        }
    }

    public TitleBarView getTitleBarView() {
        return detailsView.getTitleBarView();
    }

    public ImageView getImageView() {
        return detailsView.getImageView();
    }

    public TextView getNameTv() {
        return detailsView.getNameTv();
    }

    public RatingBar getRatingBar() {
        return detailsView.getRatingBar();
    }

    public TextView getSizeTv() {
        return detailsView.getSizeTv();
    }

    public LinearLayout getLabelLayout() {
        return detailsView.getLabelLayout();
    }

    public TabPageIndicator getTabPageIndicator() {
        return detailsView.getTabPageIndicator();
    }

    public CustomerUnderlinePageIndicator getPageIndicator() {
        return detailsView.getPageIndicator();
    }

    public ViewPager getViewPager() {
        return detailsView.getViewPager();
    }

    public Intent getIntent() {
        return detailsView.getIntent();
    }

    public TwoStateImageView getCollectBt() {
        return detailsView.getCollectBt();
    }

    public TwoStateImageView getShareBt() {
        return detailsView.getShareBt();
    }

    public DownProgress getProgress() {
        return detailsView.getProgress();
    }

    public LinearLayout getQQLayout() {
        return detailsView.getQQLayout();
    }

    public String getGameId() {
        return detailsBiz.getGameId( getIntent() );
    }

    public List<Fragment> getFragments(ResultItem resultItem) {
        return detailsBiz.getFragments( resultItem, getGameId(),
                getGameModel( resultItem ) );
    }

    public List<String> getTitles(int rId) {
        return detailsBiz.getTitles( mContext, rId );
    }

    public GameModel getGameModel(ResultItem resultItem) {
        return detailsBiz.getGameModel( mContext, resultItem );
    }

    private void showPopuwindow() {
        if (sharePopupWindow == null) {
            sharePopupWindow = new SharePopupWindow( mContext, gameModel );
        }
        sharePopupWindow
                .showAtLocation( getTitleBarView(), Gravity.BOTTOM, 0, 0 );
        sharePopupWindow.setPlatformActionListener( this );
    }

    int type = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_titleBar_rightImg:
                openOtherActivity( mContext, new Intent( mContext,
                        ManagementActivity.class ) );
                break;
            case R.id.id_titleBar_leftImg:
                close( mContext );
                break;
            case R.id.id_gameDetails_collect:
                if (gameModel != null) {
                    if ("0".equals( SpUtil.getUserId() )) {
                        openOtherActivity( mContext, new Intent( mContext,
                                LoginActivity.class ) );
                        break;
                    }
                    type = gameModel.isCollectde() ? 2 : 1;
                    collect( COLLECT, type );
                }
                break;
            case R.id.id_gameDetails_dwon:
                if (gameModel != null) {
                    // ��ť���
                    int status = gameModel.getStatus();
                    switch (status) {
                        case GameStatus.UNLOAD:// ��ʼ���أ��˴����ж��Ƿ���wifi��
                            if (retuestStoragePermission( (BaseActivity) mContext )) {
                                if (!TextUtils.isEmpty( gameModel.getUrl() )) {
                                    gameModel.setStatus( GameStatus.LOADING );
                                    openDownService( mContext, gameModel );
                                    WindowUtils.showAddDownloadWindow( mContext,
                                            getTitleBarView(), 1500, mContext.getResources()
                                                    .getString( R.string.already_add_downlaod_list ) );
                                    sendDownloadActionBroadcast( mContext );
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put( TrackingUtils.USERNAMEKEY, SpUtil.getAccount() );
                                    map.put( TrackingUtils.NICKNAMEKEY, SpUtil.getNick() );
                                    map.put( TrackingUtils.MOBILEKEY, SpUtil.getPhone() );
                                    map.put( TrackingUtils.GAMENAME, gameModel.getName() );
                                    TrackingUtils.setEvent( TrackingUtils.DOWNLOADEVENT, map );
                                } else {
                                    showToast( mContext, mContext.getResources().getString( R.string.no_download_path ) );
                                }
                            }
                            break;
                        case GameStatus.LOADING:// ��������
                            gameModel.setStatus( GameStatus.PAUSEING );
                            mAdapter.notifyDataSetChanged();
                            openDownService( mContext, gameModel );
                            break;
                        case GameStatus.PAUSEING:// ��ͣ״̬
                            gameModel.setStatus( GameStatus.LOADING );
                            openDownService( mContext, gameModel );
                            break;
                        case GameStatus.COMPLETED:// ���������(�ȴ���װ)
                            String apkName = gameModel.getApkName();
                            ApkUtils.installApp( apkName, mContext );
                            break;
                        case GameStatus.INSTALLING:// ��װ������
                            Toast.makeText( mContext, mContext.getResources()
                                    .getString( R.string.instaling_txt ), Toast.LENGTH_SHORT )
                                    .show();
                            break;
                        case GameStatus.INSTALLCOMPLETED:// ��װ���
                            ApkUtils.doStartApplicationWithPackageName(
                                    gameModel.getPackgeName(), mContext );
                            break;
                        case GameStatus.DELETE:
                            gameModel.setStatus( GameStatus.LOADING );
                            openDownService( mContext, gameModel );
                        case GameStatus.UNINSTALLING:// δ��װ
                            ApkUtils.installApp( gameModel.getApkName(), mContext );
                            break;
                    }
                }
                break;
            case R.id.id_gameDetails_share:
                if (is185()) {
                    showPopuwindow();
                }
                break;
            case R.id.id_gameDetails_qq:
                if (TextUtils.isEmpty( qqUrl )) {
                    break;
                }
                Intent intent = new Intent();
                intent.setData( Uri.parse( qqUrl ) );
                intent.setAction( Intent.ACTION_VIEW );
                mContext.startActivity( intent );
                break;
        }
    }

    @Override
    public void onDownStatusChange(GameModel model) {
        Message message = new Message();
        message.obj = model;
        mHandler.sendMessage( message );
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            final GameModel model = (GameModel) msg.obj;
            if (model.getGameId() == gameModel.getGameId()) {
                gameModel.setProgress( model.getProgress() );
                gameModel.setStatus( model.getStatus() );
                gameModel.setUrl( model.getUrl() );
                switch (model.getStatus()) {
                    case GameStatus.UNLOAD:
                        model.setProgress( 0 );
                        break;
                }
                gameModel.setType( 1 );
                getProgress().setStatus( gameModel.getStatus() );
                getProgress().setProgress( gameModel.getProgress() );
            }
            super.handleMessage( msg );
        }
    };

    @Override
    public void installed(String packgeName, int status) {
        if (gameModel != null) {
            if (packgeName.equals( gameModel.getPackgeName() )) {
                gameModel.setStatus( status );
                sendBroadcast( Configuration.completedFilter, gameModel,
                        mContext );
            }
        }
    }

    @Override
    public void unInstall(String packgeName, int status) {
        if (packgeName.equals( gameModel.getPackgeName() )) {
            gameModel.setStatus( status );
            sendBroadcast( Configuration.deleteFilter, gameModel, mContext );
        }
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        if (1 == what) {
            commenCount = resultItem.getString( "data" );
            requestHttp( HttpType.REFRESH );
        } else {
            cancelProgressDialog( alertDialog );
            if ("0".equals( resultItem.getString( "status" ) )) {
                Message message = new Message();
                message.what = what;
                message.obj = resultItem.getItem( "data" );
                handler.sendMessage( message );
            } else {
                showToast( mContext, resultItem.getString( "msg" ) );
            }
        }
    }

    @Override
    public void onError(int what, String error) {
        cancelProgressDialog( alertDialog );
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ResultItem resultItem = (ResultItem) msg.obj;
            switch (msg.what) {
                case HttpType.REFRESH:
                    gameModel = getGameModel( resultItem );
                    ResultItem item = resultItem.getItem( "gameinfo" );
                    qqUrl = item.getString( "qq_group" );
                    initShow( gameModel, resultItem );
                    break;
                case COLLECT:
                    gameModel.setCollectde( type == 1 );
                    getCollectBt().setState( gameModel.isCollectde() ? 1 : 0 );
                    String str = type == 1 ? mContext.getResources().getString( R.string.collect_success )
                            : mContext.getResources().getString( R.string.cancle_success );
                    showToast( mContext, str );
                    break;
            }
        }

		};

    @Override
    public void onCancel(Platform arg0, int arg1) {
        showToast( mContext, mContext.getResources().getString( R.string.share_cancle ) );
    }

    @Override
    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
        showToast( mContext, mContext.getResources().getString( R.string.share_success ) );
    }

    @Override
    public void onError(Platform arg0, int arg1, Throwable arg2) {
        showToast( mContext, mContext.getResources().getString( R.string.share_error ) );
    }

    @Override
    public void downAction(int size) {
        if (size > 0) {
            if (downBadge == null) {
                downBadge = showDownBadgeView( mContext, getTitleBarView().getRightImg(), size
                        + "", BGABadgeView.POSITION_TOP_RIGHT );
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
}
