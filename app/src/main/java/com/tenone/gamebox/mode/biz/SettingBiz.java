
package com.tenone.gamebox.mode.biz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.able.SettingAble;
import com.tenone.gamebox.mode.listener.ClearFilesListener;
import com.tenone.gamebox.mode.listener.FileSizeCalculateListener;
import com.tenone.gamebox.view.utils.ApkUtils;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.FileSizeUtil;
import com.tenone.gamebox.view.utils.FileUtils;
import com.tenone.gamebox.view.utils.SpUtil;

@SuppressLint("HandlerLeak")
public class SettingBiz implements SettingAble {
	public static final int SIZE = 0;
	public static final int CLEAR = 2;
	FileSizeCalculateListener fileSizeCalculateListener;
	ClearFilesListener clearFilesListener;

	@Override
	public boolean isWifi() {
		return SpUtil.getWifi();
	}

	@Override
	public boolean isAutoInstall() {
		return SpUtil.getAutoInstall();
	}

	@Override
	public boolean isNotification() {
		return SpUtil.getNotification();
	}

	@Override
	public boolean isVoice() {
		return SpUtil.getSound();
	}

	@Override
	public boolean isShake() {
		return SpUtil.getShake();
	}

	@Override
	public boolean isLogin() {
		return !"0".equals( SpUtil.getUserId() );
	}

	@Override
	public boolean isAutoClear() {
		return SpUtil.getAutoClear();
	}

	@Override
	public void downloadSize(String path, FileSizeCalculateListener listener) {
		fileSizeCalculateListener = listener;
		new CalculateThread( SIZE, path ).start();
	}

	@Override
	public void cacheSize(String path, FileSizeCalculateListener listener) {
		fileSizeCalculateListener = listener;
		new CalculateThread( SIZE, path ).start();
	}

	@Override
	public String versions(Context cxt) {
		return cxt.getString( R.string.curren_version ) + ApkUtils.getVersionName( cxt );
	}

	@Override
	public void setStatus(boolean b, String key) {
		new Thread( new MyThread( b, key ) ).start();
	}

	private class MyThread implements Runnable {
		boolean b;
		String key;

		public MyThread(boolean b, String key) {
			this.b = b;
			this.key = key;
		}

		@Override
		public void run() {
			SpUtil.setBoolean( b, key );
		}
	}


	private class CalculateThread extends Thread {
		private int what;
		private String path, fileSize;
		private boolean result;

		public CalculateThread(int what, String path) {
			this.what = what;
			this.path = path;
		}

		@Override
		public void run() {
			switch (what) {
				case SIZE:
					fileSize = FileSizeUtil.getFileOrFilesSize( path, FileSizeUtil.SIZETYPE_MB ) + "M";
					break;
				case CLEAR:
					result = FileUtils.delAllFile( path );
					break;
			}
			handler.post( () -> {
				switch (what) {
					case SIZE:
						if (fileSizeCalculateListener != null) {
							fileSizeCalculateListener.calculateResult( path, fileSize );
						}
						break;
					case CLEAR:
						if (clearFilesListener != null) {
							clearFilesListener.clearResult( path, result );
						}
						break;
				}
			} );
			super.run();
		}
	}

	@Override
	public void clearDownload(String path, ClearFilesListener listener) {
		clearFilesListener = listener;
		new CalculateThread( CLEAR, path ).start();
	}

	@Override
	public void clearCache(String path, ClearFilesListener listener) {
		clearFilesListener = listener;
		new CalculateThread( CLEAR, path ).start();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
		}
	};

	@Override
	public void exitLogin() {
		SpUtil.setUserId( "0" );
		SpUtil.setPhone( "" );
		SpUtil.setNick( "" );
		SpUtil.setPwd( "" );
		SpUtil.setHeaderUrl( "" );
	}

	@Override
	public boolean isBindMobile() {
		boolean isBind = false;
		isBind = (!TextUtils.isEmpty( SpUtil.getPhone() ))
				&& BeanUtils.isMatchered( BeanUtils.PHONE_PATTERN,
				SpUtil.getPhone() );
		return isBind;
	}
}
