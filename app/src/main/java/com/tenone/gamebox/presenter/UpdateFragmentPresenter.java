/**
 * Project Name:GameBox
 * File Name:UpdateFragmentPresenter.java
 * Package Name:com.tenone.gamebox.presenter
 * Date:2017-3-20����6:06:03
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.AutoInstallApkThread;
import com.tenone.gamebox.mode.biz.UpdateFragmentBiz;
import com.tenone.gamebox.mode.listener.ApkInstallListener;
import com.tenone.gamebox.mode.listener.ApkInstallListener.InstallListener;
import com.tenone.gamebox.mode.listener.GameButtonClickListener;
import com.tenone.gamebox.mode.listener.GameHotFragmentListener;
import com.tenone.gamebox.mode.listener.GameItemClickListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.UpdateFragmentView;
import com.tenone.gamebox.view.activity.NewGameDetailsActivity;
import com.tenone.gamebox.view.adapter.UpdateFragmentAdapter;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.DownloadProgressBar;
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

/**
 * ClassName:UpdateFragmentPresenter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-20 ����6:06:03 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */
public class UpdateFragmentPresenter extends BasePresenter implements
        GameButtonClickListener, GameItemClickListener, OnClickListener,
        HttpResultListener, DownStatusChangeListener, InstallListener,
        GameHotFragmentListener {
    private UpdateFragmentView fragmentView;
    private UpdateFragmentBiz fragmentBiz;
    private Context mContext;
    private UpdateFragmentAdapter mAdapter;
    private String ids;
    private List<GameModel> gameModels = new ArrayList<GameModel>();

    private DownReceiver receiver;
    private ApkInstallListener installListener;

    public UpdateFragmentPresenter(Context cxt, UpdateFragmentView view) {
        this.fragmentView = view;
        this.mContext = cxt;
        this.fragmentBiz = new UpdateFragmentBiz( cxt );
        ids = fragmentBiz.getIds( getGameModels() );
    }

    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new UpdateFragmentAdapter( gameModels, mContext );
        }
        getListView().setAdapter( mAdapter );
    }

    public void initListener() {
        mAdapter.setButtonClickListener( this );
        mAdapter.setItemClickListener( this );
        getAllUpdateView().setOnClickListener( this );
        receiver = new DownReceiver();
        installListener = new ApkInstallListener();
        registerDownReceiver( mContext, this, receiver );
        registerInstallReceiver( mContext, this, installListener );
    }

    /**
     * ��ȡ���� requestHttp:(������һ�仰�����������������). <br/>
     *
     * @param what
     * @author John Lie
     * @since JDK 1.6
     */
    public void requestHttp(int what) {
        if (TextUtils.isEmpty( ids )) {
            return;
        }
        String url = MyApplication.getHttpUrl().getGameUpdata();
        RequestBody requestBody = new FormBody.Builder().add( "system", "1" )
                .add( "ids", ids ).build();
        HttpUtils.postHttp( mContext, what, url, requestBody, this );
    }

    public ListView getListView() {
        return fragmentView.getListView();
    }

    public TextView getAllUpdateView() {
        return fragmentView.getAllUpdateView();
    }

    public List<GameModel> getGameModels() {
        return fragmentBiz.getGameModels();
    }

    public void getUpdateGame(List<ResultItem> resultItems) {
        fragmentBiz.getUpdateGame( resultItems, this );
    }

    /**
     * ���� destory:(������һ�仰�����������������). <br/>
     *
     * @author John Lie
     * @since JDK 1.6
     */
    public void destory() {
        unRegisterInstallListener( mContext, installListener );
        unRegisterReceiver( mContext, receiver );
    }

    @Override
    public void gameItemClick(GameModel gameMode) {
        // ��Ϸ����
        openOtherActivity( mContext, new Intent( mContext, NewGameDetailsActivity.class )
                .putExtra( "id", gameMode.getGameId() + "" ) );
    }

    @Override
    public void gameButtonClick(DownloadProgressBar progress, GameModel gameMode) {
        downloadGame( gameMode );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_updateFragment_allUpdate:
                // ȫ������
                break;
        }
    }

    private void downloadGame(GameModel gameMode) {
        // ��ť���
        int status = gameMode.getStatus();
        switch (status) {
            case GameStatus.UNLOAD:// ��ʼ���أ��˴����ж��Ƿ���wifi��
                gameMode.setStatus( GameStatus.LOADING );
                openDownService( mContext, gameMode );
                WindowUtils.showAddDownloadWindow( mContext, getListView(), 1500,
                        "����ӵ������б�!" );
                break;
            case GameStatus.LOADING:// ��������
                gameMode.setStatus( GameStatus.PAUSEING );
                openDownService( mContext, gameMode );
                break;
            case GameStatus.PAUSEING:// ��ͣ״̬
                gameMode.setStatus( GameStatus.LOADING );
                openDownService( mContext, gameMode );
                break;
            case GameStatus.COMPLETED:// ���������(�ȴ���װ)
                ApkUtils.installApp( gameMode.getApkName(), mContext );
                break;
            case GameStatus.INSTALLING:// ��װ������
                Toast.makeText( mContext, "���ڰ�װ,���Ժ�", Toast.LENGTH_SHORT ).show();
                break;
            case GameStatus.INSTALLCOMPLETED:// ��װ���
                ApkUtils.doStartApplicationWithPackageName(
                        gameMode.getPackgeName(), mContext );
                break;
            case GameStatus.DELETE:
                gameMode.setStatus( GameStatus.LOADING );
                openDownService( mContext, gameMode );
            case GameStatus.UNINSTALLING:// δ��װ
                ApkUtils.installApp( gameMode.getApkName(), mContext );
                break;
        }
    }

    @Override
    public void onSuccess(int what, ResultItem resultItem) {
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
        showToast( mContext, error );
    }

    @Override
    public void installed(String packgeName, int status) {
        try {
            for (GameModel gameMode : gameModels) {
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
            for (GameModel gameMode : gameModels) {
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
            super.handleMessage( msg );
            final GameModel model = (GameModel) msg.obj;
            if (gameModels.contains( model )) {
                int index = gameModels.indexOf( model );
                gameModels.remove( model );
                if (model.getStatus() == GameStatus.UNLOAD) {
                    model.setStatus( GameStatus.UNLOAD );
                    model.setProgress( 0 );
                }
                if (model.getStatus() == GameStatus.COMPLETED) {
                    if (!SpUtil.getAutoInstall()) {// û���趨�Զ���װ����δ��װ
                        model.setStatus( GameStatus.UNINSTALLING );
                    } else {// �趨���Զ���װ�Ϳ���һ�����߳�ȥ��װ
                        new AutoInstallApkThread( mContext, model.getApkName() )
                                .start();
                    }
                }
                model.setType( 1 );
                gameModels.add( index, model );
                mAdapter.notifyDataSetChanged();
            }
        }
    };
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<ResultItem> resultItem = ((ResultItem) msg.obj)
                    .getItems( "data" );
            switch (msg.what) {
                case HttpType.REFRESH:
                    getUpdateGame( resultItem );
                    break;
            }
        }
    };

    @Override
    public void uadapteUI(List<GameModel> gameModels) {
        this.gameModels.clear();
        this.gameModels.addAll( gameModels );
        mAdapter.notifyDataSetChanged();
    }
}
