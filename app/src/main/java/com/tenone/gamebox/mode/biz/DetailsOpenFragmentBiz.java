
package com.tenone.gamebox.mode.biz;

import android.content.Context;
import android.text.TextUtils;

import com.tenone.gamebox.mode.able.DetailsOpenFragmentAble;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.OpenServerMode;
import com.tenone.gamebox.mode.mode.OpenServiceNotificationMode;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

public class DetailsOpenFragmentBiz implements DetailsOpenFragmentAble {

	@Override
	public List<OpenServerMode> getModes(GameModel model,
			List<ResultItem> resultItems, Context context) {
		List<OpenServerMode> modes = new ArrayList<OpenServerMode>();
		if (model != null && resultItems != null) {
			for (ResultItem resultItem : resultItems) {
				OpenServerMode mode = new OpenServerMode();
				String id = resultItem.getString("server_id");
				if (!TextUtils.isEmpty(id)) {
					mode.setServiceId(Integer.valueOf(id));
				}
				String url = model.getUrl();
				if (!TextUtils.isEmpty(url)) {
					mode.setDownloadUrl(url);
				}
				mode.setGameId(model.getGameId() + "");
				mode.setDownStatus(model.getStatus());
				mode.setGameName(model.getName());
				mode.setGameSize(model.getSize());
				mode.setGameVersions(model.getVersionsName());
				mode.setImgUrl(model.getImgUrl());
				mode.setLabelArray(model.getLabelArray());
				String time = resultItem.getString("start_time");
				if (!TextUtils.isEmpty(time)) {
					Long timeL = Long.valueOf(time) * 1000;
					Long currentTiem = System.currentTimeMillis();
					mode.setOpen( (currentTiem - timeL) >= 0 );
				}
				mode.setOpenTime(time);
				mode.setPackgeName(model.getPackgeName());
				modes.add(mode);
			}
			comparisonSql(context, modes);
		}
		return modes;
	}

	@Override
	public OpenServiceNotificationMode getNotificationMode(OpenServerMode mode,
			GameModel gameModel) {
		OpenServiceNotificationMode notificationMode = new OpenServiceNotificationMode();
		notificationMode.setGameId(gameModel.getGameId() + "");
		notificationMode.setGameName(gameModel.getName());
		notificationMode.setGameVersions(gameModel.getVersionsName());
		notificationMode.setImgUrl(gameModel.getImgUrl());
		notificationMode.setServiceId(mode.getServiceId() + "");
		notificationMode.setTime(mode.getOpenTime());
		return notificationMode;
	}

	public void comparisonSql(Context context, List<OpenServerMode> modes) {
		List<OpenServiceNotificationMode> notificationModes = DatabaseUtils
				.getInstanse(context).getWarns();
		for (OpenServerMode openServerMode : modes) {
			openServerMode.setNotification(false);
			for (OpenServiceNotificationMode openServiceNotificationMode : notificationModes) {
				if (openServiceNotificationMode.getServiceId().equals(
						(openServerMode.getServiceId() + ""))
						&& openServiceNotificationMode.getGameId().equals(
								(openServerMode.getGameId() + ""))) {
					openServerMode.setNotification(true);
					break;
				} else {
					openServerMode.setNotification(false);
				}
			}
		}
	}
}
