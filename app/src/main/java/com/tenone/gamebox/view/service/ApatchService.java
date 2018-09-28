package com.tenone.gamebox.view.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.HttpUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ApatchService extends Service implements HttpResultListener {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (null != intent && "apatch".equals(intent.getAction())) {
			HttpManager.getApatch(1, getApplicationContext(), this);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	String downUrl = "";

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		if (1 == resultItem.getIntValue("status")) {
			ResultItem item = resultItem.getItem("data");
			if (null != item) {
				downUrl = item.getString("patch_url");
				if (!TextUtils.isEmpty(downUrl)) {
					HttpUtils.downFile(getApplicationContext(), downUrl,
							handler);
				}
			}
		}
	}

	@Override
	public void onError(int what, String error) {

	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String path = getApplicationContext().getFilesDir()
						.getAbsolutePath() + "/185box.apatch";
				byte[] bytes = (byte[]) msg.obj;
				BufferedOutputStream bos = null;
				FileOutputStream fos = null;
				File file = null;
				try {
					File dir = new File(getApplicationContext().getFilesDir()
							.getAbsolutePath());
					if (!dir.exists() && dir.isDirectory()) {
						dir.mkdirs();
					}
					file = new File(path);
					fos = new FileOutputStream(file);
					bos = new BufferedOutputStream(fos);
					bos.write(bytes);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (bos != null) {
						try {
							bos.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					if (fos != null) {
						try {
							fos.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
				stopSelf();
				break;
			case 0:
				break;
			}
			super.dispatchMessage(msg);
		}
	};

}
