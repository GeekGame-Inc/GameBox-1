package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.AttentionBiz;
import com.tenone.gamebox.mode.biz.AutoInstallApkThread;
import com.tenone.gamebox.mode.listener.ApkInstallListener;
import com.tenone.gamebox.mode.listener.ApkInstallListener.InstallListener;
import com.tenone.gamebox.mode.listener.CancleAttentionListener;
import com.tenone.gamebox.mode.listener.DeleteDialogConfrimListener;
import com.tenone.gamebox.mode.listener.GameButtonClickListener;
import com.tenone.gamebox.mode.listener.GameItemClickListener;
import com.tenone.gamebox.mode.listener.GameItemLongClickListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.AttentionView;
import com.tenone.gamebox.view.activity.NewGameDetailsActivity;
import com.tenone.gamebox.view.adapter.AttentionAdapter;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.DownloadProgressBar;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.RefreshLayout.OnLoadListener;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.dialog.DeleteDialog.Buidler;
import com.tenone.gamebox.view.receiver.DownReceiver;
import com.tenone.gamebox.view.receiver.DownReceiver.DownStatusChangeListener;
import com.tenone.gamebox.view.utils.ApkUtils;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.WindowUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class AttentionPresenter extends BasePresenter implements
        OnClickListener, OnRefreshListener, OnLoadListener,
        GameItemClickListener, GameButtonClickListener,
        CancleAttentionListener, GameItemLongClickListener,
        DeleteDialogConfrimListener, DownStatusChangeListener, InstallListener,
        HttpResultListener {
    int currentPosition = -1;
    AttentionBiz attentionBiz;
    AttentionView attentionView;
    Context mContext;
    AttentionAdapter mAdapter;
    Buidler deleteBuidler;

    ApkInstallListener installListener;
    DownReceiver receiver;
    int page = 1;
    List<GameModel> items = new ArrayList<GameModel>();
    public static final int CANCLE = 5;
    public static final int ALLCANCLE = 6;

    AlertDialog alertDialog;

    public AttentionPresenter(AttentionView v, Context cxt) {
        this.attentionBiz = new AttentionBiz();
        this.attentionView = v;
        this.mContext = cxt;
    }

    public void initView() {
        alertDialog = buildProgressDialog( mContext );
        getTitleBarView().setTitleText( R.string.myCollect );
        getTitleBarView().setLeftImg( R.drawable.icon_back_grey );
        getTitleBarView().setRightText( R.string.allCancle );
    }

    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new AttentionAdapter( mContext, items );
        }
        getListView().setAdapter( mAdapter );
    }

    public void initListener() {
        getTitleBarView().getRightText().setOnClickListener( this );
        getTitleBarView().getLeftImg().setOnClickListener( this );
        getRefreshLayout().setOnRefreshListener( this );
        getRefreshLayout().setOnLoadListener( this );
        mAdapter.setOnItemClick( this );
        mAdapter.setOnButtonClick( this );
        mAdapter.setCancleAttentionListener( this );
        mAdapter.setItemLongClickListener( this );
        receiver = new DownReceiver();
        installListener = new ApkInstallListener();
        registerDownReceiver( mContext, this, receiver );
        registerInstallReceiver( mContext, this, installListener );
    }

    public void requestHttp(int what) {
        String url = MyApplication.getHttpUrl().getMyCollect();
        RequestBody requestBody = new FormBody.Builder()
                .add( "username", SpUtil.getAccount() ).add( "page", page + "" )
                .add( "system", "1" ).build();
        HttpUtils.postHttp( mContext, what, url, requestBody, this );
    }

    public void collect(int what, String gid) {
        String url = MyApplication.getHttpUrl().getGameCollect();
        RequestBody requestBody = new FormBody.Builder().add( "type", "2" )
                .add( "gid", gid ).add( "username", SpUtil.getAccount() ).build();
        HttpUtils.postHttp( mContext, what, url, requestBody, this );
    }

    public TitleBarView getTitleBarView() {
        return attentionView.getTitleBarView();
    }

    public RefreshLayout getRefreshLayout() {
        return attentionView.getRefreshLayout();
    }

    public ListView getListView() {
        return attentionView.getListView();
    }

    public List<GameModel> getGameModels(List<ResultItem> resultItems) {
        return attentionBiz.getGameModels( mContext, resultItems );
    }

    public void destory() {
        unRegisterInstallListener( mContext, installListener );
        unRegisterReceiver( mContext, receiver );
    }

    private void showDialog() {
        if (deleteBuidler == null) {
            deleteBuidler = new Buidler( mContext );
        }
        deleteBuidler.setMessage( R.string.confirmAllCancle );
        deleteBuidler.setConfrimListener( this );
        deleteBuidler.createDialog();
    }

    private void showToast(String text) {
        Toast.makeText( mContext, text, Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_titleBar_leftImg:
                close( mContext );
                break;
            case R.id.id_titleBar_rightText:
                if (!items.isEmpty()) {
                    showDialog();
                } else {
									showToast( "\u60a8\u8fd8\u672a\u6536\u85cf\u4efb\u4f55\u6e38\u620f" );
                }
                break;
        }
    }

    @Override
    public void cancleAttention(GameModel model, int position) {
        collect( CANCLE, model.getGameId() + "" );
        currentPosition = position;
    }

    @Override
    public void onGameItemLongClick(int position, GameModel gameModel) {
        currentPosition = position;
        mAdapter.setCurrentPosition( position );
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConfrimClick(AlertDialog dialog) {
        dialog.dismiss();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            GameModel mode = items.get( i );
            String id = mode.getGameId() + "";
            if (i < (items.size() - 1)) {
                builder.append( id + "," );
            } else {
                builder.append( id );
            }
        }
        collect( ALLCANCLE, builder.toString() );
    }

    @Override
    public void gameItemClick(GameModel gameMode) {
        if (currentPosition != -1
                && gameMode.equals( items.get( currentPosition ) )) {
            mAdapter.setCurrentPosition( -1 );
            currentPosition = -1;
            mAdapter.notifyDataSetChanged();
        } else {
            openOtherActivity( mContext, new Intent( mContext,
                    NewGameDetailsActivity.class ).putExtra( "id",
                    gameMode.getGameId() + "" ) );
        }

    }

    @Override
    public void gameButtonClick(DownloadProgressBar progress, GameModel gameMode) {
        int status = gameMode.getStatus();
        switch (status) {
            case GameStatus.UNLOAD:
                if (!TextUtils.isEmpty( gameMode.getUrl() )) {
                    gameMode.setStatus( GameStatus.LOADING );
                    openDownService( mContext, gameMode );
									WindowUtils.showAddDownloadWindow( mContext, getListView(),
											1500, "\u5df2\u6dfb\u52a0\u5230\u4e0b\u8f7d\u5217\u8868!" );
                } else {
									showToast( mContext, "\u4e0b\u8f7d\u5730\u5740\u4e3a\u7a7a" );
                }
                break;
            case GameStatus.LOADING:
                gameMode.setStatus( GameStatus.PAUSEING );
                mAdapter.notifyDataSetChanged();
                openDownService( mContext, gameMode );
                break;
            case GameStatus.PAUSEING:
                gameMode.setStatus( GameStatus.LOADING );
                openDownService( mContext, gameMode );
                break;
            case GameStatus.COMPLETED:
                ApkUtils.installApp( gameMode.getApkName(), mContext );
                break;
            case GameStatus.INSTALLING:
							Toast.makeText( mContext, "\u6b63\u5728\u5b89\u88c5,\u8bf7\u7a0d\u5019", Toast.LENGTH_SHORT ).show();
                break;
            case GameStatus.INSTALLCOMPLETED:
                ApkUtils.doStartApplicationWithPackageName(
                        gameMode.getPackgeName(), mContext );
                break;
            case GameStatus.DELETE:
                gameMode.setStatus( GameStatus.LOADING );
                openDownService( mContext, gameMode );
            case GameStatus.UNINSTALLING:
                ApkUtils.installApp( gameMode.getApkName(), mContext );
                break;
        }
    }

    @Override
    public void installed(String packgeName, int status) {
        try {
            for (GameModel gameMode : items) {
                if (gameMode.getPackgeName().equals( packgeName )) {
                    gameMode.setStatus( status );
                    sendBroadcast( Configuration.completedFilter, gameMode,
                            mContext );
                    break;
                }
            }
        } catch (NullPointerException e) {
            Log.e( "185Box", e.toString() );
        }
    }

    @Override
    public void unInstall(String packgeName, int status) {
        try {
            for (GameModel gameMode : items) {
                if (gameMode.getPackgeName().equals( packgeName )) {
                    gameMode.setStatus( status );
                    sendBroadcast( Configuration.deleteFilter, gameMode,
                            mContext );
                    break;
                }
            }
        } catch (NullPointerException e) {
            Log.e( "185Box", e.toString() );
        }
    }

    @Override
    public void onDownStatusChange(GameModel model) {
        Message message = new Message();
        message.obj = model;
        mHandler.sendMessage( message );
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            final GameModel model = (GameModel) msg.obj;
            boolean isGame = false;
            int index = -1;
            for (int i = 0; i < items.size(); i++) {
                GameModel game = items.get( i );
                if (game.getGameId() == model.getGameId()) {
                    isGame = true;
                    index = i;
                    break;
                }
            }
            if (isGame) {
                items.remove( index );
                if (model.getStatus() == GameStatus.UNLOAD) {
                    model.setStatus( GameStatus.UNLOAD );
                    model.setProgress( 0 );
                }
                if (model.getStatus() == GameStatus.COMPLETED) {
                    if (!SpUtil.getAutoInstall()) {
                        model.setStatus( GameStatus.UNINSTALLING );
                    } else {
                        new AutoInstallApkThread( mContext, model.getApkName() )
                                .start();
                    }
                }
                model.setType( 1 );
                items.add( index, model );
                mAdapter.notifyDataSetChanged();
                super.handleMessage( msg );
            }
        }
    };

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

    @Override
    public void onRefresh() {
        page = 1;
        requestHttp( HttpType.REFRESH );
    }

    @Override
    public void onLoad() {
        page++;
        requestHttp( HttpType.LOADING );
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<ResultItem> resultItem = ((ResultItem) msg.obj)
                    .getItems( "data" );
            switch (msg.what) {
                case HttpType.REFRESH:
                    items.clear();
                    items.addAll( getGameModels( resultItem ) );
                    break;
                case HttpType.LOADING:
                    items.addAll( getGameModels( resultItem ) );
                    break;
                case CANCLE:
                    items.remove( currentPosition );
                    mAdapter.setCurrentPosition( -1 );
                    mAdapter.notifyDataSetChanged();
                    break;
                case ALLCANCLE:
                    items.clear();
                    mAdapter.notifyDataSetChanged();
                    break;
            }
            mAdapter.notifyDataSetChanged();
        }
    };
}
