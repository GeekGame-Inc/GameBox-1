package com.tenone.gamebox.view.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.ss.android.common.applog.TeaAgent;
import com.ss.android.common.applog.TeaConfigBuilder;
import com.ss.android.common.lib.EventUtils;
import com.tenone.gamebox.mode.mode.PurchaseModel;
import com.tenone.gamebox.view.base.Constant;
import com.tenone.gamebox.view.base.MyApplication;

public class JrttUtils {
	private static final String TAG = "JrttUtils";
	private static boolean isJrttStatistical = false;
	private static final String MOBILE = "mobile";
	private static final String ACCOUNT = "account";
	public static final int MOBILELOGIN = 0;
	public static final int MOBILEREGISTER = 1;
	public static final int ACCOUNTLOGIN = 2;
	public static final int ACCOUNTREGISTER = 3;
	private static Activity activity;

	public static boolean isIsJrttStatistical() {
		return isJrttStatistical;
	}

	public static void setIsJrttStatistical(boolean isJrttStatistical) {
		JrttUtils.isJrttStatistical = isJrttStatistical;
	}

	public static void init(Context context) {
		if (isIsJrttStatistical()) {
			TeaAgent.init( TeaConfigBuilder.create( context )
					.setAppName( Constant.TOUTIAOAPPNAME )
					.setChannel( Constant.APPID + "-" + MyApplication.getConfigModle().getChannelID() )
					.setAid( Constant.TOUTIAOAPPID )
					.createTeaConfig() );
			TeaAgent.setDebug( false );
			Log.i( TAG, "\u521d\u59cb\u5316TeaAgent" );
		}
	}

	public static void jrttReportData(int type, boolean isSuccess) {
		if (isIsJrttStatistical()) {
			new Thread() {
				@Override
				public void run() {
					switch (type) {
						case MOBILELOGIN:
							EventUtils.setLogin( MOBILE, isSuccess );
							Log.i( TAG, "\u624b\u673a\u53f7\u767b\u5f55" );
							break;
						case MOBILEREGISTER:
							EventUtils.setRegister( MOBILE, isSuccess );
							Log.i( TAG, "\u624b\u673a\u53f7\u6ce8\u518c" );
							break;
						case ACCOUNTLOGIN:
							EventUtils.setLogin( ACCOUNT, isSuccess );
							Log.i( TAG, "\u8d26\u53f7\u767b\u5f55" );
							break;
						case ACCOUNTREGISTER:
							EventUtils.setRegister( ACCOUNT, isSuccess );
							Log.i( TAG, "\u8d26\u53f7\u6ce8\u518c" );
							break;
					}
					super.run();
				}
			}.start();
		}
	}

	public static void jrttReportData(int type) {
		if (isIsJrttStatistical()) {
			new Thread() {
				@Override
				public void run() {
					TeaAgent.setUserUniqueID( SpUtil.getUserId() );
					switch (type) {
						case MOBILELOGIN:
							EventUtils.setLogin( MOBILE, true );
							Log.i( TAG, "\u624b\u673a\u53f7\u767b\u5f55" );
							break;
						case MOBILEREGISTER:
							EventUtils.setRegister( MOBILE, true );
							Log.i( TAG, "\u624b\u673a\u53f7\u6ce8\u518c" );
							break;
						case ACCOUNTLOGIN:
							EventUtils.setLogin( ACCOUNT, true );
							Log.i( TAG, "\u8d26\u53f7\u767b\u5f55" );
							break;
						case ACCOUNTREGISTER:
							EventUtils.setRegister( ACCOUNT, true );
							Log.i( TAG, "\u8d26\u53f7\u6ce8\u518c" );
							break;
					}
					super.run();
				}
			}.start();
		}
	}


	public static void jrttReportPay(PurchaseModel model) {
		if (isIsJrttStatistical()) {
			EventUtils.setPurchase( model.getContent_type(), model.getContent_name(), model.getContent_id(),
					model.getContent_num(), model.getPayment_channel(), model.getCurrency(), model.isIs_success(),
					model.getCurrency_amount() );
		}
	}

	public static void onResume(Activity activity) {
		if (JrttUtils.activity == null) {
			JrttUtils.activity = activity;
		} else if (!JrttUtils.activity.getClass().getName().equals( activity.getClass().getName() )) {
			return;
		}
		if (isIsJrttStatistical()) {
			TeaAgent.onResume( activity );
			Log.i( TAG, activity.getClass().getName() + " onResume" );
		}
	}

	public static void onPause(Activity activity) {
		if (JrttUtils.activity == null) {
			JrttUtils.activity = activity;
		} else if (!JrttUtils.activity.getClass().getName().equals( activity.getClass().getName() )) {
			return;
		}
		if (isIsJrttStatistical()) {
			TeaAgent.onPause( activity );
			Log.i( TAG, activity.getClass().getName() + " onPause" );
		}
	}
}
