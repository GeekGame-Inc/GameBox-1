package com.tenone.gamebox.view.utils.download;

import android.content.ContentValues;
import android.content.Context;

import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.view.service.DownloadService.DownThread;
import com.tenone.gamebox.view.service.NoRequestHttpDownloadService;
import com.tenone.gamebox.view.utils.DatabaseUtils;
import com.tenone.gamebox.view.utils.database.GameDownloadTab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DownloadManager {
	public static DownloadManager instanse;

	public static List<GameModel> downloadList = new ArrayList<GameModel>();

	private Context mContext;
	private DatabaseUtils databaseUtils;
	private Map<String, DownThread> threads;

    private Map<String, NoRequestHttpDownloadService.DownGameThread> newThreads;

	public DownloadManager(Context context) {
		this.mContext = context;
		databaseUtils = DatabaseUtils.getInstanse(mContext);
		downloadList.clear();
		downloadList.addAll(databaseUtils.getDownloadList());
		if (threads == null) {
			threads = new HashMap<String, DownThread>();
		}
        if (newThreads == null) {
            newThreads = new HashMap<String, NoRequestHttpDownloadService.DownGameThread>();
        }
	}

	public Map<String, DownThread> getThreads() {
		return threads;
	}


	public void addThreads(DownThread thread) {
		threads.put(thread.getName(), thread);
	}


    public Map<String, NoRequestHttpDownloadService.DownGameThread> getNewThreads() {
        return newThreads;
    }


    public void addNewThreads(NoRequestHttpDownloadService.DownGameThread thread) {
        newThreads.put(thread.getName(), thread);
    }

	public static DownloadManager getInstanse(Context context) {
		if (instanse == null) {
			instanse = new DownloadManager(context);
		}
		return instanse;
	}

	public boolean hasObject(GameModel model) {
		return getDownloadList().contains(model);
	}

	public List<GameModel> getDownloadList() {
		return downloadList;
	}

	public void addDownload(GameModel model) {
		if (downloadList == null) {
			downloadList = new ArrayList<GameModel>();
		}
		if (!downloadList.contains(model)) {
			downloadList.add(model);
			databaseUtils.addDownload(model);
		}
	}

	public void removeDownload(GameModel model) {
		if (downloadList == null) {
			downloadList = new ArrayList<GameModel>();
		}
		databaseUtils.deleteDownload(model);
		downloadList.remove(model);
		DownThread thread = getThreads().get(model.getUrl());
		if (thread != null) {
			thread.interrupt();
			thread = null;
			getThreads().remove(model.getUrl());
		}
	}

	public void removeDownloads(List<GameModel> list) {
		for (GameModel gameModel : list) {
			removeDownload(gameModel);
		}
	}


	public void pauseDownload(GameModel model) {
		for (GameModel gameModel : downloadList) {
			if (gameModel.equals(model)) {
				gameModel.setStatus(GameStatus.PAUSEING);
				ContentValues values = new ContentValues();
				values.put(GameDownloadTab.DOWNSTATUS, gameModel.getStatus());
				// String selection = GameDownloadTab.GAMEURL + "="
				// + gameModel.getUrl();
				// // databaseUtils.updateDownload(values, selection);
				break;
			}
		}
	}

	public void updateDownloadProgress(GameModel model) {
		for (GameModel gameModel : downloadList) {
			if (gameModel.equals(model)) {
				gameModel.setProgress(model.getProgress());
				ContentValues values = new ContentValues();
				values.put(GameDownloadTab.PROGRESS, gameModel.getProgress()
						+ "");
				String selection = GameDownloadTab.GAMEURL + " = ?";
				databaseUtils.updateDownload(values, selection,
						new String[] { gameModel.getUrl() });
				break;
			}
		}
	}

	public void updateDownload(GameModel model, String key, String value) {
		for (GameModel gameModel : downloadList) {
			if (gameModel.equals(model)) {
				gameModel = model;
				ContentValues values = new ContentValues();
				values.put(key, value);
				String selection = GameDownloadTab.GAMEURL + " = ?";
				databaseUtils.updateDownload(values, selection,
						new String[] { gameModel.getUrl() });
				break;
			}
		}
	}

}
