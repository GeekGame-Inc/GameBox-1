package com.tenone.gamebox.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.AutoInstallApkThread;
import com.tenone.gamebox.mode.listener.ApkInstallListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.share.SharePopupWindow;
import com.tenone.gamebox.view.adapter.ManagementAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.CoordinatorTabLayout;
import com.tenone.gamebox.view.custom.DownProgress;
import com.tenone.gamebox.view.custom.TwoStateImageView;
import com.tenone.gamebox.view.custom.bga.BGABadgeView;
import com.tenone.gamebox.view.fragment.DetailsCommentFragment;
import com.tenone.gamebox.view.fragment.DetailsGiftFragment;
import com.tenone.gamebox.view.fragment.DetailsOpenFragment;
import com.tenone.gamebox.view.fragment.GameDetailStrategyFragment;
import com.tenone.gamebox.view.fragment.GameDetailsFragment;
import com.tenone.gamebox.view.receiver.DownActionReceiver;
import com.tenone.gamebox.view.receiver.DownReceiver;
import com.tenone.gamebox.view.utils.ApkUtils;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DatabaseUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TrackingUtils;
import com.tenone.gamebox.view.utils.WindowUtils;
import com.tenone.gamebox.view.utils.database.GameDownloadTab;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import okhttp3.FormBody;
import okhttp3.RequestBody;


public class GameDetailsNewActivity extends BaseActivity implements HttpResultListener, PlatformActionListener, ApkInstallListener.InstallListener, DownReceiver.DownStatusChangeListener, DownActionReceiver.DownActivonListener {
    @ViewInject(R.id.coordinatortablayout)
    CoordinatorTabLayout mCoordinatorTabLayout;
    @ViewInject(R.id.id_gameDetails_viewPager)
    ViewPager mViewPager;
    @ViewInject(R.id.id_gameDetails_collect)
    TwoStateImageView collectBt;
    @ViewInject(R.id.id_gameDetails_dwon)
    DownProgress progress;
    @ViewInject(R.id.id_gameDetails_share)
    TwoStateImageView shareTIV;


    private RelativeLayout mGroupView;
    private String gameId, qqUrl;
    private Context mContext;
    private ManagementAdapter mAdapter;
    private List<String> title = new ArrayList<String>();
    private GameDetailsFragment detailsFragment;
    private DetailsCommentFragment detailsCommentFragment;
    private DetailsGiftFragment detailsGiftFragment;
    private DetailsOpenFragment detailsOpenFragment;
    private GameDetailStrategyFragment gameDetailStrategyFragment;
    private List<Fragment> fragments;
    private GameModel gameModel;
    private SharePopupWindow sharePopupWindow;
    private ApkInstallListener installListener;
    private DownReceiver receiver;
    private static final int COLLECT = 5;
    private int type = 0;
    private ImageView rightImageView;
    private BGABadgeView downBadge;
    private View rightView;
    private DownActionReceiver actionReceiver;
    private String commentCounts = "0";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_game_details_new );
        ViewUtils.inject( this );
        mContext = this;
        mCoordinatorTabLayout.setupWithViewPager( mViewPager );
        mGroupView = mCoordinatorTabLayout.getGruopView();
        rightImageView = mCoordinatorTabLayout.getRightImageView();
        rightView = mCoordinatorTabLayout.getRightView();
        gameId = getIntent().getExtras().getString( "id" );
        HttpManager.commentCounts( 1, mContext, this, gameId );
        rightImageView.setOnClickListener( v -> {
            startActivity( new Intent( mContext, ManagementActivity.class ) );
        } );
        shareTIV.setVisibility( BeanUtils.is185() ? View.VISIBLE : View.INVISIBLE );
    }

    private void initFragments(ResultItem resultItem) {
        detailsFragment = new GameDetailsFragment();
        detailsFragment.setAction( getIntent().getAction() );
        detailsCommentFragment = new DetailsCommentFragment();
        detailsGiftFragment = new DetailsGiftFragment();
        detailsOpenFragment = new DetailsOpenFragment();
        gameDetailStrategyFragment = new GameDetailStrategyFragment();
        Bundle bundle = new Bundle();
        bundle.putString( "id", gameId );
        detailsCommentFragment.setArguments( bundle );
        detailsGiftFragment.setArguments( bundle );
        gameDetailStrategyFragment.setArguments( bundle );
        bundle.putSerializable( "gameModel", gameModel );
        detailsOpenFragment.setArguments( bundle );
        bundle.putSerializable( "resultItem", resultItem );
        detailsFragment.setArguments( bundle );
        if (fragments == null) {
            fragments = new ArrayList<Fragment>();
            fragments.add( detailsFragment );
            fragments.add( detailsCommentFragment );
            fragments.add( detailsGiftFragment );
            fragments.add( detailsOpenFragment );
            fragments.add( gameDetailStrategyFragment );
        }
        initView();
        initViewPager();
    }

    private void initView() {
        initDownBadge();
        actionReceiver = new DownActionReceiver();
        registerDownloadActionReceiver( mContext, actionReceiver );
        actionReceiver.setDownActivonListener( this );
        ImageView imageView = mGroupView.findViewById( R.id.id_gameDetails_img );
        LinearLayout qqLayout = mGroupView.findViewById( R.id.id_gameDetails_qq );
        ImageLoadUtils.loadNormalImg( imageView, mContext, gameModel.getImgUrl() );
        ((TextView) mGroupView.findViewById( R.id.id_gameDetails_gameName )).setText( gameModel.getName() );
        String[] labels = gameModel.getLabelArray();
        if (labels != null) {
            for (int i = 0; i < labels.length; i++) {
                if (i < 3) {
                    String label = labels[i];
                    TextView textView = new TextView( mContext );
                    textView.setText( label );
                    textView.setTextSize( TypedValue.COMPLEX_UNIT_SP, 10 );
                    textView.setTextColor( mContext.getResources().getColor(
                            R.color.underLineColor ) );
                    textView.setBackground( mContext.getResources().getDrawable(
                            R.drawable.radius_underline_border ) );
                    textView.setPadding(
                            DisplayMetricsUtils.dipTopx( mContext, 5 ),
                            DisplayMetricsUtils.dipTopx( mContext, 2 ),
                            DisplayMetricsUtils.dipTopx( mContext, 5 ),
                            DisplayMetricsUtils.dipTopx( mContext, 2 ) );
                    ((LinearLayout) (mGroupView.findViewById( R.id.id_gameDetails_label ))).addView( textView );
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView
                            .getLayoutParams();
                    params.setMargins( 0,
                            0, DisplayMetricsUtils.dipTopx( mContext, 3 ), 0 );
                    textView.setLayoutParams( params );
                } else {
                    break;
                }
            }
        }
        collectBt.setState( gameModel.isCollectde() ? 1 : 0 );
        progress.setStatus( gameModel.getStatus() );
        progress.setProgress( gameModel.getProgress() );
        ((TextView) mGroupView.findViewById( R.id.id_gameDetails_size )).setText(
                gameModel.getTimes() + "����     " + gameModel.getSize() + "M" );
        if (!TextUtils.isEmpty( qqUrl )) {
            qqLayout.setVisibility( View.VISIBLE );
            qqLayout.setOnClickListener( v -> {
                Intent intent = new Intent();
                intent.setData( Uri.parse( qqUrl ) );
                intent.setAction( Intent.ACTION_VIEW );
                mContext.startActivity( intent );
            } );
        }
    }

    public void setTitles() {
        String[] array = getResources().getStringArray( R.array.game_details_titles );
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                String string = array[i];
                if (i == 1) {
                    string = string + "(" + commentCounts + ")";
                }
                title.add( string );
            }
        }
    }

    private void initViewPager() {
        setTitles();
        mAdapter = new ManagementAdapter( getSupportFragmentManager() );
        mAdapter.setmTitleList( title );
        mAdapter.setArray( fragments );
        mViewPager.setAdapter( mAdapter );
        mViewPager.setOffscreenPageLimit( 4 );
        mViewPager.setCurrentItem( ("mine".equals( getIntent().getAction() )) ? 1 : 0 );
        initListener();
        mCoordinatorTabLayout.setIndicatorWidth( mCoordinatorTabLayout.getTabLayout() );
    }

    private void initListener() {
        receiver = new DownReceiver();
        installListener = new ApkInstallListener();
        registerDownReceiver( mContext, this, receiver );
        registerInstallReceiver( mContext, this, installListener );
    }

    private void showPopuwindow() {
        if (sharePopupWindow == null) {
            sharePopupWindow = new SharePopupWindow( mContext, gameModel );
        }
        sharePopupWindow
                .showAtLocation( mCoordinatorTabLayout, Gravity.BOTTOM, 0, 0 );
        sharePopupWindow.setPlatformActionListener( this );
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
       /* if (a > 0) {
            downBadge = showDownBadgeView( mContext, rightView, a + "" );
        }*/
    }

    private void requestHttp() {
        String url = MyApplication.getHttpUrl().getGameInfo();
        RequestBody requestBody = new FormBody.Builder()
                .add( "channel", MyApplication.getConfigModle().getChannelID() )
                .add( "system", "1" ).add( "gid", gameId )
                .add( "username", SpUtil.getAccount() ).build();
        HttpUtils.postHttp( mContext, 0, url, requestBody, this );
    }

    private void collect(int what, int type) {
        String url = MyApplication.getHttpUrl().getGameCollect();
        RequestBody requestBody = new FormBody.Builder().add( "type", type + "" )
                .add( "gid", gameId ).add( "username", SpUtil.getAccount() )
                .build();
        HttpUtils.postHttp( mContext, what, url, requestBody, this );
    }


    private void setGameModel(ResultItem resultItem) {
        GameModel gameModel = new GameModel();
        ResultItem item = resultItem.getItem( "gameinfo" );
        gameModel.setGameTag( item.getString( "tag" ) );
        gameModel.setName( item.getString( "gamename" ) );
        String id = item.getString( "id" );
        if (!TextUtils.isEmpty( id )) {
            gameModel.setGameId( Integer.valueOf( id ) );
        }
        gameModel.setImgUrl( MyApplication.getHttpUrl().getBaseUrl()
                + item.getString( "logo" ) );
        gameModel.setSize( item.getString( "size" ) );
        gameModel.setUrl( item.getString( "android_url" ) );
        gameModel.setVersionsName( item.getString( "version" ) );
        gameModel.setPackgeName( item.getString( "android_pack" ) );
        String grade = item.getString( "score" );
        float g = 0;
        if (!TextUtils.isEmpty( grade )) {
            g = Float.valueOf( grade ).floatValue();
        }
        gameModel.setGrade( g );
        gameModel.setCollectde( "1".equals( item.getString( "collect" ) ) );
        String downTimes = item.getString( "download" );
        if (!TextUtils.isEmpty( downTimes )) {
            int download = Integer.valueOf( downTimes ).intValue();
            if (download > 10000) {
                download = download / 10000;
                gameModel.setTimes( download + "��+" );
            } else {
                gameModel.setTimes( download + "" );
            }
        } else {
            gameModel.setTimes( "0" );
        }
        String str = item.getString( "types" );
        if (!TextUtils.isEmpty( str )) {
            String[] array = str.split( " " );
            gameModel.setLabelArray( array );
        }
        String where = GameDownloadTab.GAMEID + "=? AND "
                + GameDownloadTab.GAMENAME + "=?";
        GameModel model = DatabaseUtils.getInstanse( mContext ).getGameModel(
                where,
                new String[]{(gameModel.getGameId() + ""),
                        gameModel.getName()} );
        gameModel.setApkName( model.getApkName() );
        gameModel.setStatus( model.getStatus() );
        gameModel.setProgress( model.getProgress() );
        ApkUtils.inspectApk( mContext, gameModel );
        model = null;
        this.gameModel = gameModel;
    }

    @OnClick({R.id.id_gameDetails_collect, R.id.id_gameDetails_dwon, R.id.id_gameDetails_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_gameDetails_collect:
                if (gameModel != null) {
                    if ("0".equals( SpUtil.getUserId() )) {
                        startActivity( new Intent( mContext,
                                LoginActivity.class ) );
                        break;
                    }
                    type = gameModel.isCollectde() ? 2 : 1;
                    collect( COLLECT, type );
                }
                break;
            case R.id.id_gameDetails_share:
                if (BeanUtils.is185()) {
                    showPopuwindow();
                }
                break;
            case R.id.id_gameDetails_dwon:
                if (gameModel != null) {
                    int status = gameModel.getStatus();
                    switch (status) {
                        case GameStatus.UNLOAD:
                            if (retuestStoragePermission( (BaseActivity) mContext )) {
                                if (!TextUtils.isEmpty( gameModel.getUrl() )) {
                                    gameModel.setStatus( GameStatus.LOADING );
                                    openDownService( mContext, gameModel );
                                    WindowUtils.showAddDownloadWindow( mContext,
                                            mCoordinatorTabLayout, 1500, getString( R.string.already_add_downlaod_list ) );
                                    sendDownloadActionBroadcast( mContext );
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put( TrackingUtils.USERNAMEKEY, SpUtil.getAccount() );
                                    map.put( TrackingUtils.NICKNAMEKEY, SpUtil.getNick() );
                                    map.put( TrackingUtils.MOBILEKEY, SpUtil.getPhone() );
                                    map.put( TrackingUtils.GAMENAME, gameModel.getName() );
                                    TrackingUtils.setEvent( TrackingUtils.DOWNLOADEVENT, map );
                                } else {
                                    showToast( mContext.getResources().getString( R.string.no_download_path ) );
                                }
                            }
                            break;
                        case GameStatus.LOADING:
                            gameModel.setStatus( GameStatus.PAUSEING );
                            mAdapter.notifyDataSetChanged();
                            openDownService( mContext, gameModel );
                            break;
                        case GameStatus.PAUSEING:
                            gameModel.setStatus( GameStatus.LOADING );
                            openDownService( mContext, gameModel );
                            break;
                        case GameStatus.COMPLETED:
                            String apkName = gameModel.getApkName();
                            ApkUtils.installApp( apkName, mContext );
                            break;
                        case GameStatus.INSTALLING:
                            Toast.makeText( mContext, R.string.instaling_txt, Toast.LENGTH_SHORT )
                                    .show();
                            break;
                        case GameStatus.INSTALLCOMPLETED:
                            ApkUtils.doStartApplicationWithPackageName(
                                    gameModel.getPackgeName(), mContext );
                            break;
                        case GameStatus.DELETE:
                            gameModel.setStatus( GameStatus.LOADING );
                            openDownService( mContext, gameModel );
                        case GameStatus.UNINSTALLING:
                            ApkUtils.installApp( gameModel.getApkName(), mContext );
                            break;
                    }
                }
                break;
        }
    }


    @Override
    public void onSuccess(int what, ResultItem resultItem) {
        if (what == 1) {
            if (1 == resultItem.getIntValue( "status" )) {
                commentCounts = resultItem.getString( "data" );
            }
            requestHttp();
        } else {
            if ("0".equals( resultItem.getString( "status" ) )) {
                Message message = new Message();
                message.what = what;
                message.obj = resultItem.getItem( "data" );
                handler.sendMessage( message );
            } else {
                showToast( resultItem.getString( "msg" ) );
            }
        }
    }

    @Override
    public void onError(int what, String error) {
    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            ResultItem resultItem = (ResultItem) msg.obj;
            switch (msg.what) {
                case HttpType.REFRESH:
                    setGameModel( resultItem );
                    initFragments( resultItem );
                    ResultItem item = resultItem.getItem( "gameinfo" );
                    qqUrl = item.getString( "qq_group" );
                    break;
                case COLLECT:
                    gameModel.setCollectde( type == 1 );
                    collectBt.setState( gameModel.isCollectde() ? 1 : 0 );
                    String str = type == 1 ? getString( R.string.collect_success ) : getString( R.string.cancle_success );
                    showToast( str );
                    break;
            }
        }
    };

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
                    case GameStatus.COMPLETED:
                        if (SpUtil.getAutoInstall()) {
                            new
                                    AutoInstallApkThread( mContext, model.getApkName() ).start();
                        } else {
                            model.setStatus( GameStatus.UNINSTALLING );
                        }
                        break;

                }
                gameModel.setType( 1 );
                progress.setStatus( model.getStatus() );
                progress.setProgress( model.getProgress() );

            }
            super.handleMessage( msg );
        }
    };

    @Override
    public void onDestroy() {
        if (installListener != null) {
            unregisterReceiver( installListener );
        }
        if (receiver != null) {
            unregisterReceiver( receiver );
        }
        if (actionReceiver != null) {
            actionReceiver.setDownActivonListener( null );
            unregisterReceiver( actionReceiver );
        }
        super.onDestroy();
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        showToast( getString( R.string.share_success ) );
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        showToast( getString( R.string.share_error ) );
    }

    @Override
    public void onCancel(Platform platform, int i) {
        showToast( getString( R.string.share_cancle ) );
    }

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
    public void onDownStatusChange(GameModel model) {
        Message message = new Message();
        message.obj = model;
        mHandler.sendMessage( message );
    }


    @Override
    public void downAction(int size) {
        if (size > 0) {
            if (downBadge == null) {
                downBadge = showDownBadgeView( mContext, rightView, size
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


}
