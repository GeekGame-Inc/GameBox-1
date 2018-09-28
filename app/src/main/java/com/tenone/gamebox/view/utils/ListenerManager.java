package com.tenone.gamebox.view.utils;

import android.text.TextUtils;

import com.tenone.gamebox.mode.listener.FragmentChangeListener;
import com.tenone.gamebox.mode.listener.OnAboutForMeMessageUpdateListener;
import com.tenone.gamebox.mode.listener.OnDataChangeListener;
import com.tenone.gamebox.mode.listener.OnDataRefreshListener;
import com.tenone.gamebox.mode.listener.OnDynamicTabChangeListener;
import com.tenone.gamebox.mode.listener.OnDynamicsChangeListener;
import com.tenone.gamebox.mode.listener.OnFansChangeListener;
import com.tenone.gamebox.mode.listener.OnLoginStateChangeListener;
import com.tenone.gamebox.mode.listener.OnMainViewPagerChangeListener;
import com.tenone.gamebox.mode.listener.OnMessageUpdateListener;
import com.tenone.gamebox.mode.listener.OnPageTitleChangeListener;
import com.tenone.gamebox.mode.listener.OnTabLayoutTextToLeftRightListener;
import com.tenone.gamebox.mode.listener.OnTradingLoginStatusListener;
import com.tenone.gamebox.mode.listener.OnTypeSelecteListener;
import com.tenone.gamebox.mode.listener.OnUserInfoChangeListener;
import com.tenone.gamebox.mode.listener.OnUserInfoDataChangeListener;
import com.tenone.gamebox.mode.listener.PayResultCallback;
import com.tenone.gamebox.mode.listener.UpdateListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ListenerManager {
	private static FragmentChangeListener changeListener;
	private static List<OnLoginStateChangeListener> onLoginStateChangeListeners = new ArrayList<OnLoginStateChangeListener>();
	private static OnPageTitleChangeListener onPageTitleChangeListener;
	private static List<OnMessageUpdateListener> onMessageUpdateListeners = new ArrayList<OnMessageUpdateListener>();
	private static List<OnDataChangeListener> onDataChangeListeners = new ArrayList<OnDataChangeListener>();
	private static List<OnUserInfoDataChangeListener> onUserInfoDataChangeListeners = new ArrayList<OnUserInfoDataChangeListener>();
	private static List<OnFansChangeListener> onFansChangeListeners = new ArrayList<OnFansChangeListener>();
	private static List<OnDynamicsChangeListener> onDynamicsChangeListeners = new ArrayList<OnDynamicsChangeListener>();
	private static List<OnUserInfoChangeListener> onHeaderChangeListeners = new ArrayList<OnUserInfoChangeListener>();
	private static List<OnAboutForMeMessageUpdateListener> onAboutForMeMessageUpdateListeners = new ArrayList<OnAboutForMeMessageUpdateListener>();
	private static List<OnDataRefreshListener> onDataRefreshListeners = new ArrayList<OnDataRefreshListener>();

	private static List<OnTypeSelecteListener> onTypeSelecteListeners = new ArrayList<OnTypeSelecteListener>();

	private static List<OnDynamicTabChangeListener> onDynamicTabChangeListeners = new ArrayList<OnDynamicTabChangeListener>();

	private static List<OnTabLayoutTextToLeftRightListener> onTabLayoutTextToLeftRightListeners = new ArrayList<OnTabLayoutTextToLeftRightListener>();
	private static List<PayResultCallback> payResultCallbacks = new ArrayList<PayResultCallback>();
	private static List<OnTradingLoginStatusListener> onTradingLoginStatusListeners = new ArrayList<OnTradingLoginStatusListener>();

	private static List<UpdateListener> updateListeners = new ArrayList<UpdateListener>();

	private static Set<OnMainViewPagerChangeListener> onMainViewPagerChangeListeners = new CopyOnWriteArraySet<OnMainViewPagerChangeListener>();

	public static void registerFragmentChangeListener(
			FragmentChangeListener listener) {
		ListenerManager.changeListener = listener;
	}


	public static void sendFragmentChange(int which) {
		if (changeListener != null) {
			changeListener.onFragmentChange( which );
		}
	}

	public static void registerOnLoginStateChangeListener(
			OnLoginStateChangeListener listener) {
		onLoginStateChangeListeners.add( listener );
	}

	public static void sendOnLoginStateChange(boolean isLogin) {
		if (onLoginStateChangeListeners != null) {
			for (OnLoginStateChangeListener listener : onLoginStateChangeListeners) {
				listener.onLoginStateChange( isLogin );
			}
		}
	}

	public static void registerOnPageTitleChangeListener(OnPageTitleChangeListener listener) {
		ListenerManager.onPageTitleChangeListener = listener;
	}

	public static void sendOnPageTitleChange(String pageTitle, int position) {
		if (null != ListenerManager.onPageTitleChangeListener) {
			ListenerManager.onPageTitleChangeListener.onPageTitleChange( pageTitle, position );
		}
	}

	public static void registerOnMessageUpdateListener(OnMessageUpdateListener listener) {
		onMessageUpdateListeners.add( listener );
	}

	public static void sendOnMessageUpdateListener(String text) {
		for (OnMessageUpdateListener listener : onMessageUpdateListeners) {
			if (listener != null) {
				listener.onMessageUpdate( text );
			}
		}
	}

	public static void registerOnDataChangeListener(OnDataChangeListener listener) {
		onDataChangeListeners.add( listener );
	}

	public static void sendOnDataChangeListener() {
		for (OnDataChangeListener listener : onDataChangeListeners) {
			if (listener != null) {
				listener.onDataChange();
			}
		}
	}

	public static void registerOnDynamicsChangeListener(OnDynamicsChangeListener listener) {
		onDynamicsChangeListeners.add( listener );
	}

	public static void sendOnDynamicsChangeListener() {
		for (OnDynamicsChangeListener listener : onDynamicsChangeListeners) {
			if (listener != null) {
				listener.onDynamicsChange();
			}
		}
	}

	public static void registerOnUserInfoDataChangeListener(OnUserInfoDataChangeListener listener) {
		onUserInfoDataChangeListeners.add( listener );
	}

	private static String oldDriving = "0", oldFans = "0", oldAttention = "0";

	public static void sendOnUserInfoDataChangeListener(String driving, String fans, String attention) {
		for (OnUserInfoDataChangeListener listener : onUserInfoDataChangeListeners) {
			if (listener != null && !TextUtils.isEmpty( driving ) && !oldDriving.equals( driving )) {
				listener.onDrivingChange( driving );
				oldDriving = driving;
			}
			if (listener != null && !TextUtils.isEmpty( fans ) && !oldFans.equals( fans )) {
				listener.onFansChange( fans );
				oldFans = fans;
			}
			if (listener != null && !TextUtils.isEmpty( attention ) && !oldAttention.equals( attention )) {
				listener.onAttentionChange( attention );
				oldAttention = attention;
			}
		}
	}


	public static void registerOnFansChangeListeners(OnFansChangeListener listener) {
		onFansChangeListeners.add( listener );
	}

	public static void sendOnFansChangeListener() {
		for (OnFansChangeListener listener : onFansChangeListeners) {
			listener.onFansChange();
		}
	}

	public static void registerOnUserInfoChangeListener(OnUserInfoChangeListener listener) {
		onHeaderChangeListeners.add( listener );
	}

	public static void sendOnUserInfoChangeListener() {
		for (OnUserInfoChangeListener listener : onHeaderChangeListeners) {
			if (listener != null) {
				listener.onUserInfoChange();
			}
		}
	}

	public static void unRegisterOnUserInfoChangeListener(OnUserInfoChangeListener listener) {
		onHeaderChangeListeners.remove( listener );
	}

	public static void registerOnAboutForMeMessageUpdateListener(OnAboutForMeMessageUpdateListener listener) {
		onAboutForMeMessageUpdateListeners.add( listener );
	}

	public static void sendOnAboutForMeMessageUpdateListener(String text) {
		for (OnAboutForMeMessageUpdateListener listener : onAboutForMeMessageUpdateListeners) {
			if (listener != null) {
				listener.onAboutForMeMessage( text );
			}
		}
	}


	public static void registerOnTypeSelecteListener(OnTypeSelecteListener listener) {
		onTypeSelecteListeners.add( listener );
	}

	public static void sendOnTypeSelecteListener(int type) {
		for (OnTypeSelecteListener listener : onTypeSelecteListeners) {
			if (listener != null) {
				listener.onTypeSelect( type );
			}
		}
	}

	public static void registerOnDataRefreshListener(OnDataRefreshListener listener) {
		onDataRefreshListeners.add( listener );
	}

	public static void sendOnDataRefreshListener(int which) {
		for (OnDataRefreshListener listener : onDataRefreshListeners) {
			if (listener != null) {
				listener.onDataRefresh( which );
			}
		}
	}

	public static void unRegisterOnDataRefreshListener(OnDataRefreshListener listener) {
		onDataRefreshListeners.remove( listener );
	}

	public static void registerOnDynamicTabChangeListener(OnDynamicTabChangeListener listener) {
		onDynamicTabChangeListeners.add( listener );
	}

	public static void sendregisterOnDynamicTabChangeListener() {
		for (OnDynamicTabChangeListener listener : onDynamicTabChangeListeners) {
			if (listener != null) {
				listener.onDynamicTabChange();
			}
		}
	}

	public static void unRegisterOnDynamicTabChangeListener(OnDynamicTabChangeListener listener) {
		onDynamicTabChangeListeners.remove( listener );
	}

	public static void registerOnTabLayoutTextToLeftRightListener(OnTabLayoutTextToLeftRightListener listener) {
		onTabLayoutTextToLeftRightListeners.add( listener );
	}

	public static void sendOnTabLayoutTextToLeftRightListener(int margin) {
		for (OnTabLayoutTextToLeftRightListener listener : onTabLayoutTextToLeftRightListeners) {
			if (listener != null) {
				listener.onTabLayoutTextToLeftRight( margin );
			}
		}
	}

	public static void unRegisterOnTabLayoutTextToLeftRightListener(OnTabLayoutTextToLeftRightListener listener) {
		onTabLayoutTextToLeftRightListeners.remove( listener );
	}

	public static void registerPayResultCallback(PayResultCallback listener) {
		payResultCallbacks.add( listener );
	}

	public static void sendPayResultCallback(int type) {
		for (PayResultCallback listener : payResultCallbacks) {
			switch (type) {
				case 0:
					listener.onPaySuccess();
					break;
				case -1:
					listener.onPayFail();
					break;
				case -2:
					listener.onPayCancle();
					break;
			}
		}
	}

	public static void unRegisterPayResultCallback(PayResultCallback listener) {
		payResultCallbacks.remove( listener );
	}


	public static void registerOnTradingLoginStatusListener(OnTradingLoginStatusListener listener) {
		onTradingLoginStatusListeners.add( listener );
	}

	public static void sendOnTradingLoginStatusListener(boolean isLogin) {
		for (OnTradingLoginStatusListener listener : onTradingLoginStatusListeners) {
			listener.onTradingLogin( isLogin );
		}
	}

	public static void unRegisterOnTradingLoginStatusListener(OnTradingLoginStatusListener listener) {
		onTradingLoginStatusListeners.remove( listener );
	}

	public static void registerUpdateListener(UpdateListener listener) {
		updateListeners.add( listener );
	}

	public static void sendUpdateListener(int progerss, String apkName) {
		for (UpdateListener listener : updateListeners) {
			listener.update( progerss, apkName );
		}
	}

	public static void unRegisterUpdateListener(UpdateListener listener) {
		updateListeners.remove( listener );
	}

	public static void registerOnMainViewPagerChangeListener(OnMainViewPagerChangeListener listener) {
		onMainViewPagerChangeListeners.add( listener );
	}

	public static void sendOnMainViewPagerChangeListener(int index) {
		for (OnMainViewPagerChangeListener listener : onMainViewPagerChangeListeners) {
			listener.onMainViewPagerChange( index );
		}
	}

	public static void unRegisterOnMainViewPagerChangeListener(OnMainViewPagerChangeListener listener) {
		onMainViewPagerChangeListeners.remove( listener );
	}
}
