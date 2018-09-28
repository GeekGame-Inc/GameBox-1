/**
 * Project Name:GameBox
 * File Name:InstalledFragmentPresenter.java
 * Package Name:com.tenone.gamebox.presenter
 * Date:2017-3-20����4:55:43
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.InstalledFragmentBiz;
import com.tenone.gamebox.mode.listener.DeleteDialogConfrimListener;
import com.tenone.gamebox.mode.listener.GameButtonClickListener;
import com.tenone.gamebox.mode.listener.GameHotFragmentListener;
import com.tenone.gamebox.mode.listener.GameItemClickListener;
import com.tenone.gamebox.mode.listener.GameItemLongClickListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.view.InstalledFragmentView;
import com.tenone.gamebox.view.activity.NewGameDetailsActivity;
import com.tenone.gamebox.view.adapter.InstalledFragmentAdapter;
import com.tenone.gamebox.view.custom.DownloadProgressBar;
import com.tenone.gamebox.view.custom.dialog.DeleteDialog;
import com.tenone.gamebox.view.custom.dialog.DeleteDialog.Buidler;
import com.tenone.gamebox.view.utils.ApkUtils;
import com.tenone.gamebox.view.utils.download.DownloadManager;

import java.util.ArrayList;
import java.util.List;

/**
 * �Ѱ�װӦ�� ClassName:InstalledFragmentPresenter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-20 ����4:55:43 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */
public class InstalledFragmentPresenter implements GameButtonClickListener,
        GameItemLongClickListener, GameItemClickListener,
        DeleteDialogConfrimListener, GameHotFragmentListener {

    private InstalledFragmentView fragmentView;
    private InstalledFragmentBiz fragmentBiz;
    private Context mContext;
    private InstalledFragmentAdapter mAdapter;
    private Buidler buidler;
    private List<GameModel> items = new ArrayList<GameModel>();

    public InstalledFragmentPresenter(Context cxt, InstalledFragmentView view) {
        this.mContext = cxt;
        this.fragmentView = view;
        this.fragmentBiz = new InstalledFragmentBiz();
        getGameModels();
    }

    public void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new InstalledFragmentAdapter( items, mContext );
        }
        getListView().setAdapter( mAdapter );
    }

    public void initListener() {
        mAdapter.setButtonClickListener( this );
        mAdapter.setGameItemLongClickListener( this );
        mAdapter.setItemClickListener( this );
    }

    public ListView getListView() {
        return fragmentView.getListView();
    }

    public void getGameModels() {
        fragmentBiz.getGameModels( mContext, this );
    }

    @Override
    public void gameItemClick(GameModel gameMode) {
        if (gameMode.isChecked()) {
            // ж��
            gameMode.setChecked( false );
            mAdapter.notifyDataSetChanged();
        } else {
            // ������Ϸ����
            mContext.startActivity( new Intent( mContext, NewGameDetailsActivity.class )
                    .putExtra( "id", gameMode.getGameId() + "" ) );
        }
    }

    @Override
    public void onGameItemLongClick(int position, GameModel gameModel) {
        gameModel.setChecked( true );
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void gameButtonClick(DownloadProgressBar progress, GameModel gameMode) {
        mModel = gameMode;
        if (gameMode.isChecked()) {
            // ж��
            showDialog();
        } else {
            // ��
            ApkUtils.doStartApplicationWithPackageName(
                    gameMode.getPackgeName(), mContext );
        }
    }

    GameModel mModel;

    private void showDialog() {
        if (buidler == null) {
            buidler = new DeleteDialog.Buidler( mContext );
        }
        buidler.setMessage( mContext.getResources().getString(
                R.string.confirmUnInstall ) );
        buidler.createDialog();
        buidler.setConfrimListener( this );
    }

    @Override
    public void onConfrimClick(AlertDialog dialog) {
        if (mModel != null) {
            // ����Ӧ����ʾһ������ж�صĽ�����
            Message msg = new Message();
            msg.obj = dialog;
            handler.sendMessage( msg );
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            AlertDialog dialog = (AlertDialog) msg.obj;
            items.remove( mModel );
            ApkUtils.unstallApp( mModel.getPackgeName(), mContext );
            DownloadManager.getInstanse( mContext ).removeDownload( mModel );
            dialog.dismiss();
        }
    };

    @Override
    public void uadapteUI(List<GameModel> gameModels) {
        items.addAll( gameModels );
        mAdapter.notifyDataSetChanged();
    }
}
