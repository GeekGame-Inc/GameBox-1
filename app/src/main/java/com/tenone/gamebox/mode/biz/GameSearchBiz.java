
package com.tenone.gamebox.mode.biz;

import android.text.TextUtils;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.able.GameSearchAble;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.RecordMode;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

public class GameSearchBiz implements GameSearchAble {

	@Override
	public List<String> getAllGameNames() {
		return Configuration.getAllGameNames();
	}

	@Override
	public List<GameModel> getGameModels(List<ResultItem> resultItems) {
		List<GameModel> gameModels = new ArrayList<GameModel>();
		if (resultItems != null && resultItems.size() > 4) {
			for (int i = 0; i < 4; i++) {
				ResultItem result = resultItems.get(i);
				GameModel gameModel = new GameModel();
				gameModel.setName(result.getString("gamename"));
				String gameId = result.getString("id");
				if (!TextUtils.isEmpty(gameId)) {
					gameModel.setGameId(Integer.valueOf(gameId).intValue());
				}
				gameModel.setImgUrl(MyApplication.getHttpUrl().getBaseUrl()
						+ result.getString("logo"));
				gameModel.setVersionsName(result.getString("version"));
				gameModels.add(gameModel);
			}
		}
		return gameModels;
	}

	@Override
	public List<GameModel> getLabels(List<ResultItem> resultItems) {
		List<GameModel> gameModels = new ArrayList<GameModel>();
		if (resultItems != null && resultItems.size() > 4) {
			for (int i = 4; i < resultItems.size(); i++) {
				ResultItem result = resultItems.get(i);
				GameModel gameModel = new GameModel();
				gameModel.setName(result.getString("gamename"));
				String gameId = result.getString("id");
				if (!TextUtils.isEmpty(gameId)) {
					gameModel.setGameId(Integer.valueOf(gameId).intValue());
				}
				gameModel.setImgUrl(MyApplication.getHttpUrl().getBaseUrl()
						+ result.getString("logo"));
				gameModel.setVersionsName(result.getString("version"));
				gameModels.add(gameModel);
			}
		}
		return gameModels;
	}

	@Override
	public List<RecordMode> getRecordModes(List<String> records) {
		List<RecordMode> modes = new ArrayList<RecordMode>();
		if (records != null && !records.isEmpty() && modes.isEmpty()) {
			for (int i = 0; i < records.size() + 1; i++) {
				RecordMode mode = new RecordMode();
				int resId = 0;
				String str = "";
				if (i == 0) {
					resId = R.drawable.ic_delete_gray;
					str = "\u6e05\u9664\u641c\u7d22\u8bb0\u5f55";
				} else {
					resId = R.drawable.ic_search_time;
					str = records.get(i - 1);
				}
				mode.setRecord(str);
				mode.setResId(resId);
				modes.add(mode);
			}
		}
		return modes;
	}

	@Override
	public List<String> getRecords() {
		return SpUtil.getGameSearchRecord();
	}

	@Override
	public void saveRecord(List<String> records) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < records.size(); i++) {
			if (i < records.size() - 1) {
				builder.append(records.get(i) + ",");
			} else {
				builder.append(records.get(i));
			}
		}
		SpUtil.setGameSearchRecord(builder);
	}

	@Override
	public void clearRecord() {
		StringBuilder builder = new StringBuilder();
		SpUtil.setGameSearchRecord(builder);
	}

}
