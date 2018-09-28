
package com.tenone.gamebox.mode.biz;

import android.content.Context;
import android.text.TextUtils;

import com.tenone.gamebox.mode.able.TodayOpenFragmentAble;
import com.tenone.gamebox.mode.mode.OpenServerMode;
import com.tenone.gamebox.mode.mode.OpenServiceNotificationMode;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

public class TodayOpenFragmentBiz implements TodayOpenFragmentAble {

	@Override
	public List<OpenServerMode> getModes(List<ResultItem> resultItem,
			Context context) {
		List<OpenServerMode> modes = new ArrayList<OpenServerMode>();
		for (ResultItem result : resultItem) {
			OpenServerMode openServerMode = new OpenServerMode();
			openServerMode.setGameName(result.getString("gamename"));
			openServerMode.setGameId(result.getString("id"));
			openServerMode.setImgUrl(MyApplication.getHttpUrl().getBaseUrl()
					+ result.getString("logo"));
			openServerMode.setGameVersions(result.getString("version"));
			
			String time = result.getString("start_time");			
			if (!TextUtils.isEmpty(time)) {
				long t = Long.valueOf(time) * 1000;
				long c = System.currentTimeMillis();
				openServerMode.setOpen( t <= c );
			}
			openServerMode.setOpenTime(time);
			
			String label = result.getString("label");
			if (!TextUtils.isEmpty(label)) {
				String[] array = label.split(",");
				openServerMode.setLabelArray(array);
			}
			String serviceId = result.getString("server_id");
			int serverId = 0;
			if (!TextUtils.isEmpty(serviceId)) {
				serverId = Integer.valueOf(serviceId).intValue();
			}
			openServerMode.setServiceId(serverId);
			modes.add(openServerMode);
		}
		comparisonSql(context, modes);
		return modes;
	}

	@Override
	public OpenServiceNotificationMode getNotificationMode(OpenServerMode mode) {
		OpenServiceNotificationMode notificationMode = new OpenServiceNotificationMode();
		notificationMode.setGameId(mode.getGameId());
		notificationMode.setGameName(mode.getGameName());
		notificationMode.setGameVersions(mode.getGameVersions());
		notificationMode.setImgUrl(mode.getImgUrl());
		notificationMode.setServiceId(mode.getServiceId() + "");
		notificationMode.setTime(mode.getOpenTime());
		return notificationMode;
	}

	@Override
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
