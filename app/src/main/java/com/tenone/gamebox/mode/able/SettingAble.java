package com.tenone.gamebox.mode.able;

import android.content.Context;

import com.tenone.gamebox.mode.listener.ClearFilesListener;
import com.tenone.gamebox.mode.listener.FileSizeCalculateListener;

public interface SettingAble {

	boolean isWifi();

	boolean isAutoInstall();

	boolean isAutoClear();

	boolean isNotification();

	boolean isVoice();

	boolean isShake();

	boolean isLogin();

	void setStatus(boolean b, String key);

	void downloadSize(String path, FileSizeCalculateListener listener);

	void cacheSize(String path, FileSizeCalculateListener listener);

	String versions(Context cxt);

	void clearDownload(String path, ClearFilesListener listener);

	void clearCache(String path, ClearFilesListener listener);

	void exitLogin();
	
	boolean isBindMobile();
}
