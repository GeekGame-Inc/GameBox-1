package com.tenone.gamebox.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.StatisticActionEnum;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.SpUtil;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppStatisticsManager {
	private static final String TAG = "AppStatisticsManager";
	public static final String BT_PREFIX = "BT_";
	public static final String DISCOUNT_PREFIX = "\u6298\u6263_";
	public static final String H5_PREFIX = "H5_";
	public static final String BOUTIQUE_PREFIX = "\u7cbe\u54c1\u6e38\u620f_";
	public static final String OTHER_PREFIX = "\u5176\u4ed6\u6e38\u620f_";
	public static final String ADVERTISING_PREFIX = "\u5e7f\u544a_";
	public static final String CLASSIFY_PREFIX = "\u6e38\u620f\u5206\u7c7b_";
	public static final String DOWNLOAD_PREFIX = "\u4e0b\u8f7d\u6e38\u620f_";

	private static Context mContext;
	private static ArrayList<String> actionArray = new ArrayList<>();
	private static boolean isInit = false;
	private static ExecutorService executorService;

	/**
	 * ��ʼ��
	 *
	 * @param context
	 */
	public static void init(Context context) {
		mContext = context;
		if (SpUtil.isStatistics()) {
			return;
		} else if (!TextUtils.isEmpty( SpUtil.getActions() )) {
			if (executorService == null) {
				executorService = Executors.newFixedThreadPool( 1 );
			}
			executorService.execute( new UploadThread( SpUtil.getActions() ) );
			return;
		}
		isInit = true;
	}

	public static synchronized void addStatistics(String action) {
		if (actionArray == null) {
			actionArray = new ArrayList<>();
		}
		if (!isInit || actionArray.size() > 20 || SpUtil.isStatistics()) {
			return;
		}
		if (executorService == null) {
			executorService = Executors.newFixedThreadPool( 1 );
		}
		executorService.execute( new AddActionThread( action ) );
	}

	public static synchronized void addStatistics(StatisticActionEnum statisticActionEnum) {
		if (actionArray == null) {
			actionArray = new ArrayList<>();
		}
		if (!isInit || actionArray.size() > 20 || SpUtil.isStatistics()) {
			return;
		}
		if (executorService == null) {
			executorService = Executors.newFixedThreadPool( 1 );
		}
		executorService.execute( new AddActionThread( statisticActionEnum.getValue() ) );
	}

	public static synchronized void addStatistics(StatisticActionEnum statisticActionEnum, boolean uploadNow) {
		if (actionArray == null) {
			actionArray = new ArrayList<>();
		}
		if (!isInit || actionArray.size() > 20 || SpUtil.isStatistics()) {
			return;
		}
		if (executorService == null) {
			executorService = Executors.newFixedThreadPool( 1 );
		}
		executorService.execute( new AddActionThread( statisticActionEnum.getValue(), uploadNow ) );
	}

	public static synchronized void addStatistics(String action, boolean uploadNow) {
		if (actionArray == null) {
			actionArray = new ArrayList<>();
		}
		if (!isInit || actionArray.size() > 20 || SpUtil.isStatistics()) {
			return;
		}
		if (executorService == null) {
			executorService = Executors.newFixedThreadPool( 1 );
		}
		executorService.execute( new AddActionThread( action, uploadNow ) );
	}

	public synchronized static void uploadStatistics() {
		new UploadThread().start();
	}

	private static class AddActionThread extends Thread {
		private String action;
		private boolean uploadNow;

		public AddActionThread(String action) {
			this.action = action;
		}

		public AddActionThread(String action, boolean uploadNow) {
			this.action = action;
			this.uploadNow = uploadNow;
		}

		@Override
		public void run() {
			actionArray.add( action );
			if (uploadNow) {
				uploadStatistics();
			} else if (actionArray.size() == 20) {
				uploadStatistics();
			}
		}
	}

	private static class UploadThread extends Thread implements HttpResultListener {
		private String actions;

		public UploadThread(String actions) {
			this.actions = actions;
		}

		public UploadThread() {
		}

		@Override
		public void run() {
			if (!TextUtils.isEmpty( actions )) {
				if (!TextUtils.isEmpty( actions )) {
					HttpManager.doReport( HttpType.REFRESH, mContext, this, actions );
				}
			} else {
				if (actionArray != null) {
					StringBuilder builder = new StringBuilder();
					for (String action : actionArray) {
						if (!TextUtils.isEmpty( action )) {
							builder.append( action );
							builder.append( "," );
						}
					}
					String txt = builder.toString();
					if (txt.endsWith( "," )) {
						actions = txt.substring( 0, txt.length() - 1 );
					}
					if (!TextUtils.isEmpty( actions )) {
						HttpManager.doReport( HttpType.REFRESH, mContext, this, actions );
					}
				}
			}
			super.run();
		}

		@Override
		public void onSuccess(int what, ResultItem resultItem) {
			Log.d( TAG, resultItem.getString( "msg" ) );
			if (1 == resultItem.getIntValue( "status" )) {
				onDestroy();
			} else {
				SpUtil.setActions( actions );
			}
			interrupt();
		}

		@Override
		public void onError(int what, String error) {
			Log.d( TAG, error );
			SpUtil.setActions( actions );
			interrupt();
		}
	}

	private static synchronized void onDestroy() {
		SpUtil.setIsStatistics( true );
		SpUtil.setActions( "" );
		if (actionArray != null) {
			actionArray.clear();
			actionArray = null;
		}
		if (mContext != null) {
			mContext = null;
		}
		if (executorService != null) {
			executorService.shutdown();
			executorService = null;
		}
	}
}
