
package com.tenone.gamebox.mode.biz;

import android.text.TextUtils;

import com.tenone.gamebox.mode.able.AlreadyOpenFragmentAble;
import com.tenone.gamebox.mode.mode.OpenServerMode;
import com.tenone.gamebox.mode.mode.OpenServiceNotificationMode;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class AlreadyOpenFragmentBiz implements AlreadyOpenFragmentAble {

	@Override
	public List<OpenServerMode> getModes(List<ResultItem> resultItem) {
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
				Long timeL = Long.valueOf(time) * 1000;
				Long currentTiem = System.currentTimeMillis();
				openServerMode
						.setOpen( (currentTiem - timeL) >= 0 );
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
}
