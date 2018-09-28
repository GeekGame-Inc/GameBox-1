
package com.tenone.gamebox.mode.biz;

import android.content.Context;

import com.tenone.gamebox.mode.able.DownManageFragmentAble;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.view.utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

public class DownManageFragmentBiz implements DownManageFragmentAble {

	@Override
	public List<GameModel> getGameModels(Context cxt) {
		List<GameModel> models = new ArrayList<GameModel>();
		List<GameModel> list = DatabaseUtils.getInstanse(cxt).getDownloadList();
		if (list != null) {
			for (GameModel gameModel : list) {
				if (GameStatus.UNLOAD != gameModel.getStatus()
						&& gameModel.getStatus() != GameStatus.INSTALLCOMPLETED) {
					models.add(gameModel);
				}
			}
		}
		return models;
	}

	@Override
	public List<GameModel> getCheckedModels(List<GameModel> items) {
		List<GameModel> list = new ArrayList<GameModel>();
		if (items != null) {
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).isChecked()) {
					list.add(items.get(i));
				}
			}
		}
		return list;
	}
}
