
package com.tenone.gamebox.view.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class unInstallListener extends Service {

	private static final String TAG = "MyApplication";

	private int mObserverProcessPid = -1;

	private native int init(String userSerial);
	
	static {
		System.loadLibrary("GameBox");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (Build.VERSION.SDK_INT < 17) {
			mObserverProcessPid = init(null);
		}
		else {
			mObserverProcessPid = init(getUserSerial());
		}
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public class MyBind extends Binder {
		public void logMessage(String msg) {
			
		}
	}

	private String getUserSerial() {
		Object userManager = getSystemService("user");
		if (userManager == null) {
			return null;
		}
		try {
			Method myUserHandleMethod = android.os.Process.class.getMethod(
					"myUserHandle", (Class<?>[]) null);
			Object myUserHandle = myUserHandleMethod.invoke(
					android.os.Process.class, (Object[]) null);
			Method getSerialNumberForUser = userManager.getClass().getMethod(
					"getSerialNumberForUser", myUserHandle.getClass());
			long userSerial = (Long) getSerialNumberForUser.invoke(userManager,
					myUserHandle);
			return String.valueOf(userSerial);
		} catch (NoSuchMethodException e) {
			Log.e(TAG, "NoSuchMethodException", e);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "IllegalArgumentException", e);
		} catch (IllegalAccessException e) {
			Log.e(TAG, "IllegalAccessException", e);
		} catch (InvocationTargetException e) {
			Log.e(TAG, "InvocationTargetException", e);
		}
		return null;
	}
	
}
