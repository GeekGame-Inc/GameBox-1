
package com.tenone.gamebox.mode.biz;

import android.content.Context;
import android.content.Intent;

import com.tenone.gamebox.mode.able.NotificationDetailsAble;
import com.tenone.gamebox.mode.mode.OpenServiceNotificationMode;
import com.tenone.gamebox.view.utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

public class NotificationDetailsBiz implements NotificationDetailsAble {

	DatabaseUtils databaseUtils;

	@Override
	public String getTitle(Intent intent) {
		return "\u5f00\u670d\u63d0\u9192";
	}

	@Override
	public List<OpenServiceNotificationMode> getContent(Context context) {
		List<OpenServiceNotificationMode> notificationModes = new ArrayList<OpenServiceNotificationMode>();
		if (databaseUtils == null) {
			databaseUtils = DatabaseUtils.getInstanse(context);
		}
		notificationModes.addAll(databaseUtils.getAlready());
		return notificationModes;
	}

	@Override
	public void deleteItem(Context context,
			OpenServiceNotificationMode notificationMode) {
		if (databaseUtils == null) {
			databaseUtils = DatabaseUtils.getInstanse(context);
		}
		new MyThread(0, notificationMode).start();
	}

	@Override
	public void deleteAll(Context context) {
		if (databaseUtils == null) {
			databaseUtils = DatabaseUtils.getInstanse(context);
		}
		new MyThread(1, null).start();
	}

	class MyThread extends Thread {
		int what = -1;
		OpenServiceNotificationMode notificationMode;
		
		public MyThread(int w,OpenServiceNotificationMode m) {
			this.what = w;
			this.notificationMode = m;
		}

		@Override
		public void run() {
			if (what == 0) {
				databaseUtils.deleteAlready(notificationMode);
			} else {
				databaseUtils.deleteAlready();
			}
			super.run();
		}
	}

}
