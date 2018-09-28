/** 
 * Project Name:GameBox 
 * File Name:MyRemindBiz.java 
 * Package Name:com.tenone.gamebox.mode.biz 
 * Date:2017-3-29����11:55:52 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.biz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.tenone.gamebox.mode.able.MyRemindAble;
import com.tenone.gamebox.mode.listener.OnClearListener;
import com.tenone.gamebox.mode.mode.OpenServerMode;
import com.tenone.gamebox.mode.mode.OpenServiceNotificationMode;
import com.tenone.gamebox.view.utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;
@SuppressLint("HandlerLeak")
public class MyRemindBiz implements MyRemindAble {
	public MyRemindBiz(Context context) {
		this.mContext = context;
	}

	Context mContext;

	@Override
	public List<OpenServerMode> getModes(Context context) {
		mContext = context;
		List<OpenServerMode> modes = new ArrayList<OpenServerMode>();
		List<OpenServiceNotificationMode> notificationModes = DatabaseUtils
				.getInstanse(context).getWarns();
		for (OpenServiceNotificationMode openServiceNotificationMode : notificationModes) {
			OpenServerMode mode = new OpenServerMode();
			mode.setGameId(openServiceNotificationMode.getGameId());
			mode.setGameName(openServiceNotificationMode.getGameName());
			mode.setImgUrl(openServiceNotificationMode.getImgUrl());
			String serviceId = openServiceNotificationMode.getServiceId();
			if (!TextUtils.isEmpty(serviceId)) {
				mode.setServiceId(Integer.valueOf(serviceId).intValue());
			}

			mode.setGameVersions(openServiceNotificationMode.getGameVersions());
			mode.setNotification(true);
			String time = openServiceNotificationMode.getTime();
			if (!TextUtils.isEmpty(time)) {
				Long timeL = Long.valueOf(time) * 1000;
				Long currentTiem = System.currentTimeMillis();
				mode.setOpen( (currentTiem - timeL) >= 0 );
			}
			mode.setOpenTime(time);
			String serverId = openServiceNotificationMode.getServiceId();
			if (!TextUtils.isEmpty(serverId)) {
				mode.setServiceId(Integer.valueOf(serverId).intValue());
			}
			modes.add(mode);
		}
		return modes;
	}

	@Override
	public void clearAllNotification(List<OpenServerMode> modes,
			OnClearListener listener, int id) {
		new MyThread(listener, id, 0, modes).start();
	}

	@Override
	public void clearOneNotification(OnClearListener listener, int id,
			int gameId) {
		new MyThread(listener, id, gameId, null).start();
	}

	class MyThread extends Thread {
		int id;
		int gameId;

		OnClearListener listener;
		List<OpenServerMode> modes = new ArrayList<OpenServerMode>();

		public MyThread(OnClearListener l, int id, int gameId,
				List<OpenServerMode> modes) {
			this.id = id;
			this.gameId = gameId;
			this.listener = l;
			this.modes = modes;
		}

		@Override
		public void run() {
			Message message = new Message();
			message.arg1 = id;
			switch (id) {
			case 0:
				for (OpenServerMode mode : modes) {
					if (mode.isOpen()) {
						String gameId = mode.getGameId();
						int id = 0;
						if (!TextUtils.isEmpty(gameId)) {
							id = Integer.valueOf(mode.getGameId()).intValue();
						}
						new MyThread(listener, mode.getServiceId(), id, modes)
								.start();
					}
				}
				break;
			default:
				message.arg2 = DatabaseUtils.getInstanse(mContext).deleteWarn(
						id + "", gameId + "") ? 1 : 0;
				message.obj = listener;
				handler.sendMessage(message);
				break;
			}
			super.run();
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			OnClearListener listener = (OnClearListener) msg.obj;
			if (listener != null) {
				listener.onClear( msg.arg2 != 0, msg.arg1);
			}
		}
	};
}
