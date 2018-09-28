
package com.tenone.gamebox.mode.biz;

import android.content.Context;
import android.text.TextUtils;

import com.tenone.gamebox.mode.able.AttentionAble;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.utils.ApkUtils;
import com.tenone.gamebox.view.utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

public class AttentionBiz implements AttentionAble {

	@Override
	public List<GameModel> getGameModels(Context context,
			List<ResultItem> resultItems) {
		List<GameModel> items = new ArrayList<GameModel>();
		for (ResultItem resultItem2 : resultItems) {
			GameModel model = new GameModel();
			model.setGameTag(resultItem2.getString("tag"));
			model.setType(1);
			model.setImgUrl(MyApplication.getHttpUrl().getBaseUrl()
					+ resultItem2.getString("logo"));
			model.setName(resultItem2.getString("gamename"));
			String id = resultItem2.getString("id");
			if (!TextUtils.isEmpty(id)) {
				model.setGameId(Integer.valueOf(id));
			}
			
			model.setVersionsName(resultItem2.getString("version"));
			model.setSize(resultItem2.getString("size"));
			model.setUrl(resultItem2.getString("android_url"));
			String str = resultItem2.getString("download");
			int download = 0;
			if (!TextUtils.isEmpty(str)) {
				download = Integer.valueOf(str).intValue();
			}

			if (download > 10000) {
				download = download / 10000;
				model.setTimes(download + "\u4e07+" );
			} else {
				model.setTimes(download + "");
			}
			model.setPackgeName(resultItem2.getString("android_pack"));
			model.setStatus(GameStatus.UNLOAD);
			String type = resultItem2.getString("types");
			if (!TextUtils.isEmpty(type)) {
				String[] lableArray = type.split(" ");
				model.setLabelArray(lableArray);
			}
			items.add(model);
		}

		List<GameModel> array = DatabaseUtils.getInstanse(context)
				.getDownloadList();
		if (array != null) {
			for (int i = 0; i < items.size(); i++) {
				GameModel gameModel = items.get(i);
				String packgeName = gameModel.getPackgeName();
				if (!TextUtils.isEmpty(packgeName)) {
					for (GameModel mode : array) {
						if (packgeName.equals(mode.getPackgeName())) {
							int index = items.indexOf(gameModel);
							items.remove(gameModel);
							mode.setType(1);
							items.add(index, mode);
						}
					}
				}
			}
		}
		ApkUtils.inspectApk(context, items);
		return items;
	}
}
