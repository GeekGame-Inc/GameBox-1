package com.tenone.gamebox.view.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.utils.ApkUtils;
import com.tenone.gamebox.view.utils.DatabaseUtils;
import com.tenone.gamebox.view.utils.database.GameDownloadTab;
import com.tenone.gamebox.view.utils.download.DownloadManager;
import com.tenone.gamebox.view.utils.download.FileDownloaderManager;

import java.io.File;
import java.util.List;

public class DetectionServer extends Service {
	String TAG = "DetectionServer";
	DownloadManager downloadManager;
	DatabaseUtils databaseUtils;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (databaseUtils == null) {
			databaseUtils = DatabaseUtils
					.getInstanse(getApplicationContext());
		}
		if (intent != null ) {
			if (downloadManager == null) {
				downloadManager = DownloadManager
						.getInstanse(getApplicationContext());
			}
			List<GameModel> items = downloadManager.getDownloadList();
			if (items != null) {
				for (GameModel gameModel : items) {
					if (gameModel.getStatus() == GameStatus.INSTALLCOMPLETED
							|| gameModel.getStatus() == GameStatus.COMPLETED) {
						String packgeName = gameModel.getPackgeName();
						boolean isInstall = ApkUtils.isAppInstalled(
								getApplicationContext(), packgeName);
						if (!isInstall) {
							String filepath = Configuration.downloadpath + "/"
									+ gameModel.getApkName();
							File file = new File(filepath);
							if (file.exists()) {
								gameModel.setStatus(GameStatus.COMPLETED);
								ContentValues values = new ContentValues();
								values.put(GameDownloadTab.DOWNSTATUS,
										GameStatus.COMPLETED);
								String selection = GameDownloadTab.GAMEURL
										+ "=?";
								databaseUtils.updateDownload(values, selection,
										new String[] { gameModel.getUrl() });
							} else {
								gameModel.setStatus(GameStatus.UNLOAD);
								FileDownloaderManager.getInstance()
										.deleteDownloadFile(gameModel.getUrl(),
												null);
								if (!TextUtils.isEmpty(gameModel.getUrl())) {
									databaseUtils.deleteDownload(gameModel);
								}
							}
						}
					}
				}
			}
			sendBroadcast(getApplicationContext());
		}
		return super.onStartCommand(intent, flags, startId);
	}

	public void sendBroadcast(Context cxt) {
		Intent intent = new Intent();
		intent.setAction("start_mian");
		intent.setAction("detection_server");
		cxt.sendBroadcast(intent);
		stopSelf();
	}
}
