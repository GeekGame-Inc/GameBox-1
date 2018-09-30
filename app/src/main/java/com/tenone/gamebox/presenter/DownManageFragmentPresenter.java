

package com.tenone.gamebox.presenter;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.DownManageFragmentBiz;
import com.tenone.gamebox.mode.listener.ApkInstallListener;
import com.tenone.gamebox.mode.listener.ApkInstallListener.InstallListener;
import com.tenone.gamebox.mode.listener.DeleteDialogConfrimListener;
import com.tenone.gamebox.mode.listener.GameButtonClickListener;
import com.tenone.gamebox.mode.listener.GameItemClickListener;
import com.tenone.gamebox.mode.listener.GameItemLongClickListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.view.DownManageFragmentView;
import com.tenone.gamebox.view.activity.NewGameDetailsActivity;
import com.tenone.gamebox.view.adapter.DownManageFragmentAdapter;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.custom.DownloadProgressBar;
import com.tenone.gamebox.view.custom.dialog.DeleteDialog;
import com.tenone.gamebox.view.custom.dialog.DeleteDialog.Buidler;
import com.tenone.gamebox.view.receiver.DownReceiver;
import com.tenone.gamebox.view.receiver.DownReceiver.DownStatusChangeListener;
import com.tenone.gamebox.view.utils.ApkUtils;
import com.tenone.gamebox.view.utils.DatabaseUtils;
import com.tenone.gamebox.view.utils.WindowUtils;

import java.util.ArrayList;
import java.util.List;


public class DownManageFragmentPresenter extends BasePresenter implements
		GameButtonClickListener, OnClickListener, GameItemLongClickListener,
		GameItemClickListener, DeleteDialogConfrimListener,
		DownStatusChangeListener, InstallListener {
	DownManageFragmentBiz fragmentBiz;
	DownManageFragmentView fragmentView;

	Context mContext;
	DownManageFragmentAdapter mAdapter;

	Buidler deleteBuidler;
	DownReceiver receiver;
	CancleBroadcast broadcast;
	List<GameModel> items = new ArrayList<GameModel>();

	boolean isEdit = false;

	int deleteNum = 0;
	ApkInstallListener installListener;

	public DownManageFragmentPresenter(DownManageFragmentView view, Context cxt) {
		this.fragmentView = view;
		this.mContext = cxt;
		this.fragmentBiz = new DownManageFragmentBiz();
		items.clear();
		items.addAll( getGameModels() );
	}

	public void setAdapter() {
		if (mAdapter == null) {
			mAdapter = new DownManageFragmentAdapter( mContext, items );
		}
		getListView().setAdapter( mAdapter );
	}

	public void initListener() {
		mAdapter.setButtonClickListener( this );
		mAdapter.setGameItemLongClickListener( this );
		mAdapter.setItemClickListener( this );
		getButton().setOnClickListener( this );
		receiver = new DownReceiver();
		installListener = new ApkInstallListener();
		registerDownReceiver( mContext, this, receiver );
		registerInstallReceiver( mContext, this, installListener );
		registerCancleBroadcast();
	}


	private void registerCancleBroadcast() {
		broadcast = new CancleBroadcast();
		IntentFilter filter = new IntentFilter();
		filter.addAction( "cancle_action" );
		mContext.registerReceiver( broadcast, filter );
	}

	public void onDestroy() {
		unRegisterReceiver( mContext, receiver );
		unRegisterInstallListener( mContext, installListener );
		mContext.unregisterReceiver( broadcast );
	}

	public ListView getListView() {
		return fragmentView.getListView();
	}

	public Button getButton() {
		return fragmentView.getButton();
	}


	public List<GameModel> getGameModels() {
		return fragmentBiz.getGameModels( mContext );
	}


	public List<GameModel> getCheckedModes() {
		return fragmentBiz.getCheckedModels( items );
	}


	public void showDeleteDialog() {
		if (deleteBuidler == null) {
			deleteBuidler = new DeleteDialog.Buidler( mContext );
		}
		deleteBuidler.setConfrimListener( this );
		deleteBuidler.createDialog();
	}


	private void sendVisibilityBroadcast() {
		Intent intent = new Intent();
		intent.setAction( "show_cancle_button" );
		intent.putExtra( "isEdit", isEdit );
		mContext.sendBroadcast( intent );
	}


	private void setButtonText() {
		deleteNum = 0;
		for (GameModel mode : items) {
			if (mode.isChecked()) {
				deleteNum++;
			}
		}
		getButton().setText( "(" + deleteNum + "/" + items.size() + ")" + mContext.getString( R.string.delete ) );
	}

	@Override
	public void gameButtonClick(DownloadProgressBar progress, GameModel gameMode) {
		downloadGame( gameMode );
	}

	private void downloadGame(GameModel gameMode) {

		int status = gameMode.getStatus();
		switch (status) {
			case GameStatus.UNLOAD:
				gameMode.setStatus( GameStatus.LOADING );
				openDownService( mContext, gameMode );
				WindowUtils.showAddDownloadWindow( mContext, getListView(), 1500,
						"\u5df2\u6dfb\u52a0\u5230\u4e0b\u8f7d\u5217\u8868!" );
				break;
			case GameStatus.LOADING:
				gameMode.setStatus( GameStatus.PAUSEING );
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
	public void onClick(View v) {
		if (v.getId() == R.id.id_downManage_deleteBt) {
			if (deleteNum > 0) {
				showDeleteDialog();
			} else {
				Toast.makeText( mContext, "\u8bf7\u9009\u62e9\u8981\u5220\u9664\u7684\u4efb\u52a1", Toast.LENGTH_SHORT )
						.show();
			}
		}
	}

	@Override
	public void gameItemClick(GameModel gameMode) {
		if (isEdit) {
			gameMode.setChecked( !gameMode.isChecked() );
			setButtonText();
			mAdapter.notifyDataSetChanged();
		} else

			mContext.startActivity( new Intent( mContext, NewGameDetailsActivity.class )
					.putExtra( "id", gameMode.getGameId() + "" ) );
	}

	@Override
	public void onGameItemLongClick(int position, GameModel gameModel) {
		if (!isEdit) {
			mAdapter.setVisibility( true );
			gameModel.setChecked( true );
			isEdit = true;
			getButton().setVisibility( View.VISIBLE );
			setButtonText();
			sendVisibilityBroadcast();
		}
	}


	@Override
	public void onConfrimClick(AlertDialog dialog) {
		List<GameModel> list = new ArrayList<GameModel>();
		for (int i = 0; i < items.size(); i++) {
			GameModel mode = items.get( i );
			if (mode.isChecked()) {
				mode.setStatus( GameStatus.UNLOAD );
				openDownService( mContext, mode );
				DatabaseUtils.getInstanse( mContext ).deleteDownload( mode );
			} else {
				list.add( mode );
			}
		}
		items.clear();
		items.addAll( list );
		isEdit = false;
		getButton().setVisibility( View.GONE );
		mAdapter.setVisibility( false );
		mAdapter.notifyDataSetChanged();
		sendVisibilityBroadcast();
		dialog.dismiss();
	}


	@Override
	public void onDownStatusChange(GameModel model) {
		int index = -1;
		boolean isGame = false;
		for (int i = 0; i < items.size(); i++) {
			GameModel game = items.get( i );
			if (model.getGameId() == game.getGameId()) {
				index = i;
				isGame = true;
				break;
			}
		}
		if (isGame) {
			if (model.getStatus() != GameStatus.DELETE) {
				model.setChecked( items.get( index ).isChecked() );
				items.remove( index );
				items.add( index, model );
				switch (model.getStatus()) {
					case GameStatus.UNLOAD:
						model.setProgress( 0 );
						break;
				}
			} else {
				items.remove( index );
			}
		}
		mAdapter.notifyDataSetChanged();
	}


	public class CancleBroadcast extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			for (GameModel mode : items) {
				mode.setChecked( false );
				getButton().setVisibility( View.GONE );
				isEdit = false;
			}
			mAdapter.setVisibility( false );
			mAdapter.notifyDataSetChanged();
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
}
