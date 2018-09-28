package com.tenone.gamebox.view.utils;

import android.app.Notification;
import android.content.Context;
import android.media.AudioManager;

public class SoundAndVibrateUtil {

	public static void setAlarmParams(Notification notification, Context context) {
		AudioManager volMgr = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		switch (volMgr.getRingerMode()) {
		case AudioManager.RINGER_MODE_SILENT:
			notification.sound = null;
			notification.vibrate = null;
			break;
		case AudioManager.RINGER_MODE_VIBRATE:
			notification.sound = null;
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			break;
		case AudioManager.RINGER_MODE_NORMAL:
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;// ��������
			break;
		}
	}
}
