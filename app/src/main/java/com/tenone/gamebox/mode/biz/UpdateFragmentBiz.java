
package com.tenone.gamebox.mode.biz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.tenone.gamebox.mode.able.UpdateFragmentAble;
import com.tenone.gamebox.mode.listener.GameHotFragmentListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GamePackMode;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.utils.ApkUtils;
import com.tenone.gamebox.view.utils.DatabaseUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UpdateFragmentBiz implements UpdateFragmentAble {
	Context mContext;
	DatabaseUtils databaseUtils;

	public UpdateFragmentBiz(Context cxt) {
		this.mContext = cxt;
		this.databaseUtils = DatabaseUtils.getInstanse(mContext);
	}

	@Override
	public List<GameModel> getGameModels() {
		List<GameModel> gameModels = new ArrayList<GameModel>();
		List<PackageInfo> infos = ApkUtils.getAllApps(mContext);
		List<GamePackMode> packModes = databaseUtils.gamePackModes();
		if (infos != null) {
			for (PackageInfo packageInfo : infos) {
				if (packModes != null) {
					for (GamePackMode gamePackMode : packModes) {
						if (packageInfo.packageName.equals(gamePackMode
								.getPackName())) {
							GameModel gameModel = new GameModel();
							gameModel.setGameId(gamePackMode.getGameId());
							gameModel.setPackgeName(gamePackMode.getPackName());
							gameModel.setVersionsName(packageInfo.versionName);
							gameModels.add(gameModel);
							break;
						}
					}
				}
			}
		}
		return gameModels;
	}

	@Override
	public String getIds(List<GameModel> modes) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < modes.size(); i++) {
			GameModel gameModel = modes.get(i);
			if (i < (modes.size() - 1)) {
				builder.append(gameModel.getGameId() + ",");
			} else {
				builder.append(gameModel.getGameId() + "");
			}
		}
		return builder.toString();
	}

	@Override
	public void getUpdateGame(List<ResultItem> resultItems,
			GameHotFragmentListener listener) {
		new MyThread(resultItems, listener).start();
	}

	private class MyThread extends Thread {
		List<ResultItem> resultItems;
		GameHotFragmentListener listener;

		public MyThread(List<ResultItem> r, GameHotFragmentListener l) {
			this.resultItems = r;
			this.listener = l;
		}

		@Override
		public void run() {
			final List<GameModel> gameModels = new ArrayList<GameModel>();
			List<PackageInfo> infos = ApkUtils.getAllApps(mContext);
			if (resultItems != null) {
				for (PackageInfo packageInfo : infos) {
					for (ResultItem item : resultItems) {
						if (packageInfo.packageName.equals(item
								.getString("android_pack"))) {
							if (packageInfo.versionName.compareTo(item
									.getString("version")) < 0) {
								GameModel gameModel = new GameModel();
								String gameId = item.getString("id");
								if (!TextUtils.isEmpty(gameId)) {
									gameModel.setGameId(Integer.valueOf(gameId)
											.intValue());
								}
								gameModel.setName(item.getString("gamename"));
								gameModel.setGameTag(item.getString("tag"));
								gameModel.setImgUrl(MyApplication.getHttpUrl()
										.getBaseUrl() + item.getString("logo"));
								gameModel.setVersionsName(item
										.getString("version"));
								String down = item.getString("download");
								if (!TextUtils.isEmpty(down)) {
									double a = Double.valueOf(down);
									if (a >= 10000) {
										double b = (a / 10000);
										BigDecimal c = new BigDecimal(b);
										float f1 = c.setScale(3,
												BigDecimal.ROUND_HALF_UP)
												.floatValue();
										down = f1 + "��";
									}
								}
								gameModel.setTimes(down);
								gameModel.setUrl(item.getString("android_url"));
								gameModel.setPackgeName(item
										.getString("android_pack"));
								gameModel.setStatus(GameStatus.UPDATE);
								gameModels.add(gameModel);
								break;
							}
						}
					}
				}

				handler.post(new Runnable() {

					@Override
					public void run() {
						if (listener != null) {
							listener.uadapteUI(gameModels);
						}
					}
				});
			}

			super.run();
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
		}
	};
}
