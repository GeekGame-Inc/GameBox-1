/** 
 * Project Name:GameBox 
 * File Name:DeleteDownloadFileReceiver.java 
 * Package Name:com.tenone.gamebox.view.receiver 
 * Date:2017-5-12����10:54:40 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.service.DownloadService;
import com.tenone.gamebox.view.utils.DatabaseUtils;

import org.wlf.filedownloader.DownloadFileInfo;
import org.wlf.filedownloader.FileDownloader;
import org.wlf.filedownloader.listener.OnDeleteDownloadFileListener;

import java.util.List;

public class DeleteDownloadFileReceiver extends BroadcastReceiver {
	DeleteDownloadFileListener listener;
	DatabaseUtils databaseUtils;
	Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		if (databaseUtils == null) {
			databaseUtils = DatabaseUtils.getInstanse(context);
		}
		List<GameModel> gameModels = databaseUtils.getDownloadList();
		for (GameModel gameModel : gameModels) {
			if (gameModel.getStatus() == GameStatus.COMPLETED) {
				gameModel.setStatus(GameStatus.UNLOAD);
				gameModel.setProgress(0);
			}
			if (gameModel.getStatus() == GameStatus.LOADING) {
				new MyThread(gameModel).start();
			}
			sendBroadcast(Configuration.loadFilter, gameModel, context);
		}
	}
	
	private void sendBroadcast(String action, GameModel gameModel,Context context) {
		Intent intent = new Intent();
		intent.setAction(action);
		intent.putExtra("data", gameModel);
		if (Build.VERSION.SDK_INT >= 24) {
			context.sendOrderedBroadcast(intent,
					"com.tenone.gamebox.broadcast.permission");
		} else {
			context.sendBroadcast(intent);
		}
	}

	public void setDeleteDownloadFileListener(DeleteDownloadFileListener l) {
		this.listener = l;
	}

	public interface DeleteDownloadFileListener {
		void deleteDownloadFile();
	}

	class MyThread extends Thread implements OnDeleteDownloadFileListener {
		GameModel gameModel;

		public MyThread(GameModel g) {
			this.gameModel = g;
		}

		@Override
		public void run() {
			FileDownloader.delete(gameModel.getUrl(), true, this);
			super.run();
		}

		@Override
		public void onDeleteDownloadFileFailed(DownloadFileInfo arg0,
				DeleteDownloadFileFailReason arg1) {
		}

		@Override
		public void onDeleteDownloadFilePrepared(DownloadFileInfo arg0) {
		}

		@Override
		public void onDeleteDownloadFileSuccess(DownloadFileInfo arg0) {
			Intent intent = new Intent(mContext, DownloadService.class);
			intent.putExtra("gameModel", gameModel);
			mContext.startService(intent);
			
			Intent broadcastIntent = new Intent();
			intent.setAction("download_action");
			if (Build.VERSION.SDK_INT >= 24) {
				mContext.sendOrderedBroadcast(broadcastIntent,
						"com.tenone.gamebox.broadcast.permission");
			} else {
				mContext.sendBroadcast(broadcastIntent);
			}
		}
	}
}
