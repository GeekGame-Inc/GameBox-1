package com.tenone.gamebox.view.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OnTabLayoutTextToLeftListener;
import com.tenone.gamebox.mode.listener.OnTabLayoutTextToLeftListener2;
import com.tenone.gamebox.mode.listener.OnTabLayoutTextToLeftRightListener;
import com.tenone.gamebox.mode.listener.ReflexResultCallback;
import com.tenone.gamebox.mode.mode.ResultItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Closeable;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import q.rorbin.badgeview.QBadgeView;

@SuppressLint("SimpleDateFormat")
public class BeanUtils {
	static SimpleDateFormat dateFormat = null;

	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("all")
	public static Object loadClass(String className) {
		Object obj = null;
		try {
			Class classs = Class.forName( className );
			obj = classs.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException( className );
		}
		return obj;
	}

	@SuppressWarnings("all")
	public static boolean isEmpty(Object obj) {
		boolean flag = true;
		if (obj != null) {
			if (obj instanceof String) {
				flag = (obj.toString().trim().length() == 0);
			} else if (obj instanceof Collection<?>) {
				flag = ((Collection) obj).size() == 0;
			} else if (obj instanceof Map) {
				flag = ((Map) obj).size() == 0;

			} else if (obj instanceof ResultItem) {
				flag = ((ResultItem) obj).getValues().size() == 0;
			} else if (obj instanceof Object[]) {
				flag = ((Object[]) obj).length == 0;
			} else {
				flag = false;
			}
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	public static Object getFieldValue(Object obj, String key) {
		if (obj != null) {
			if (obj instanceof Map<?, ?>) {
				return ((Map<String, Object>) obj).get( key );
			} else if (obj instanceof ResultItem) {
				return ((ResultItem) obj).getValue( key );
			} else if (obj instanceof String) {
				return obj;
			}
		}
		return null;
	}

	public static String urlAppend(String url, String parts) {
		url = url.trim();
		if (!isEmpty( parts )) {
			if (url.indexOf( "?" ) < 0) {
				url += "?";
			} else {
				url += "&";
			}
		}
		return url + parts;
	}

	public static ResultItem convertJSONObject(JSONObject jsonObj) {
		ResultItem resultItem = new ResultItem();
		if (jsonObj != null) {
			Iterator<String> keys = jsonObj.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				try {
					Object obj = jsonObj.get( key );
					if (obj != null) {
						if (obj instanceof JSONObject) {
							resultItem.addValue( key,
									convertJSONObject( (JSONObject) obj ) );

						} else if (obj instanceof JSONArray) {
							List<Object> listItems = new ArrayList<Object>();
							JSONArray tempArray = (JSONArray) obj;
							for (int i = 0; i < tempArray.length(); i++) {
								Object itempObj = tempArray.get( i );
								if (itempObj instanceof JSONObject) {
									listItems.add( convertJSONObject( tempArray
											.getJSONObject( i ) ) );
								} else {
									listItems.add( itempObj );
								}
							}
							resultItem.addValue( key, listItems );
						} else {
							resultItem.addValue( key, obj.toString() );
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return resultItem;
	}

	public static ResultItem getResultItemByJson(String context) {
		ResultItem item = new ResultItem();
		try {
			JSONObject jsonObj = new JSONObject( context );
			item = convertJSONObject( jsonObj );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	protected static byte[] getBytes(String message) {
		byte[] values = new byte[message.length()];
		for (int i = 0; i < message.length(); i++) {
			values[i] = (byte) message.charAt( i );
		}
		return values;
	}

	public static Date parseDate(String dateStr, String formatStr) {
		Date result = null;
		try {
			if (dateStr.length() < formatStr.length()) {
				dateStr = "0" + dateStr;
			}
			SimpleDateFormat sdf = new SimpleDateFormat( formatStr );
			result = sdf.parse( dateStr );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String xmlCharDeCode(String context) {
		if (!isEmpty( context )) {
			context = context.replace( "&lt;", "<" );
			context = context.replace( "&gt;", ">" );
			context = context.replace( "&apos;", "'" );
			context = context.replace( "&quot;", "\"" );
			context = context.replace( "&amp;", "&" );
		}
		return context;
	}

	public static String getString(String... strings) {
		StringBuffer sb = new StringBuffer();
		if (strings != null) {
			for (String str : strings) {
				if (!isEmpty( str )) {
					sb.append( str );
				}
			}
		}
		return sb.toString();
	}

	public static long formatLongToTimeStr(Long l) {
		long minute = 0;
		long second = 0;
		second = l.intValue() / 1000;
		if (second > 60) {
			minute = second / 60;
			second = second % 60;
		}

		if (minute > 60) {
			minute = minute % 60;
		}
		return second;

	}

	public static String StringSort(String Str) {
		char[] arrayCh = Str.toCharArray();
		Arrays.sort( arrayCh );
		String str = new String( arrayCh );
		return str;
	}

	/**
	 * 邮箱验证 isValidEmail:(这里用一句话描述这个方法的作用). <br/>
	 *
	 * @param mail
	 * @return
	 * @author John Lie
	 * @since JDK 1.6
	 */
	public static boolean isValidEmail(String mail) {
		Pattern pattern = Pattern
				.compile( "^[A-Za-z0-9][\\w\\._]*[a-zA-Z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})" );
		Matcher mc = pattern.matcher( mail );
		return mc.matches();
	}

	public final static String PHONE_PATTERN = "^1\\d{10}$";

	public static boolean isMatchered(String patternStr, CharSequence input) {
		Pattern pattern = Pattern.compile( patternStr );
		Matcher matcher = pattern.matcher( input );
		return matcher.find();
	}

	public static void editAlpha(Activity activity, float alpha) {
		WindowManager.LayoutParams params = activity.getWindow()
				.getAttributes();
		params.alpha = alpha;
		activity.getWindow().setAttributes( params );
	}

	public static String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService( Context.ACTIVITY_SERVICE );
		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
				.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return "";
	}

	public static boolean isMainProcess(Context context) {
		return context.getPackageName().equals( getCurProcessName( context ) );
	}

	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo( context.getPackageName(), 0 );
			String version = info.versionCode + "";
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}

	public static boolean isLogin() {
		return !SpUtil.isExit()
				&& !"0".equals( SpUtil.getUserId() )
				&& !TextUtils.isEmpty( SpUtil.getUserId() )
				&& !"-1".equals( SpUtil.getUserId() );
	}

	public static boolean tradingIsLogin() {
		return !SpUtil.tradingIsExit()
				&& !"0".equals( SpUtil.getTradingUid() )
				&& !TextUtils.isEmpty( SpUtil.getTradingUid() )
				&& !"-1".equals( SpUtil.getTradingUid() );
	}

	public static boolean is185() {
		return true;
	}


	public static void packUpKeyboard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService( Context.INPUT_METHOD_SERVICE );
		imm.hideSoftInputFromWindow( view.getWindowToken(), 0 );
	}

	public static void showKeyboard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService( Context.INPUT_METHOD_SERVICE );
		imm.showSoftInputFromInputMethod( view.getWindowToken(), 0 );
	}


	public static void reflex(final Context context, final TabLayout tabLayout, final int tabCount) {
		tabLayout.post( () -> {
			try {
				//拿到tabLayout的mTabStrip属性
				LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt( 0 );
				int dp5 = DisplayMetricsUtils.dipTopx( tabLayout.getContext(), 1 );
				int count = mTabStrip.getChildCount();
				for (int i = 0; i < count; i++) {
					View tabView = mTabStrip.getChildAt( i );
					//拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
					Field mTextViewField = tabView.getClass().getDeclaredField( "mTextView" );
					mTextViewField.setAccessible( true );
					TextView mTextView = (TextView) mTextViewField.get( tabView );
					tabView.setPadding( 0, 0, 0, 0 );
					//因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
					int width = 0;
					width = mTextView.getWidth();
					if (width == 0) {
						mTextView.measure( 0, 0 );
						width = mTextView.getMeasuredWidth();
					}
					int magin = ((DisplayMetricsUtils.getScreenWidth( context ) / tabCount) - width) / 2 - dp5 * 2;
					//设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
					LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
					params.width = width;
					params.leftMargin = magin;
					params.rightMargin = magin;
					tabView.setLayoutParams( params );
					tabView.invalidate();
				}
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} );
	}

	private static int reflexIndex = 0;

	public static void reflex(final TabLayout tabLayout, ReflexResultCallback callback) {
		reflexIndex = 1;
		LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt( 0 );
		int count = mTabStrip.getChildCount();
		int dp = DisplayMetricsUtils.getScreenWidth( tabLayout.getContext() ) / count;
		for (int i = 0; i < count; i++) {
			final int index = i;
			new Thread() {
				@Override
				public void run() {
					try {
						View tabView = mTabStrip.getChildAt( index );
						Field mTextViewField = tabView.getClass().getDeclaredField( "mTextView" );
						mTextViewField.setAccessible( true );
						TextView mTextView = (TextView) mTextViewField.get( tabView );
						tabLayout.post( () -> {
							reflexIndex++;
							tabView.setPadding( 0, 0, 0, 0 );
							int width = mTextView.getWidth();
							if (width == 0) {
								mTextView.measure( 0, 0 );
								width = mTextView.getMeasuredWidth();
							}
							int margin = (dp - width) / 2;
							LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
							params.width = width;
							params.leftMargin = margin;
							params.rightMargin = margin;
							tabView.setLayoutParams( params );
							tabView.invalidate();
							if (reflexIndex == count && callback != null) {
								callback.onReflexResult();
							}
						} );
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					super.run();
				}
			}.start();
		}
	}


	public static void getTabLayoutTextViewToLeftRightMargin(TabLayout tabLayout, OnTabLayoutTextToLeftRightListener listener) {
		new Thread() {
			@Override
			public void run() {
				int margin = 0;
				LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt( 0 );
				int count = mTabStrip.getChildCount();
				int dp = DisplayMetricsUtils.getScreenWidth( tabLayout.getContext() ) / count;
				for (int i = 0; i < count; i++) {
					final int index = i;
					View tabView = mTabStrip.getChildAt( index );
					try {
						Field mTextViewField = tabView.getClass().getDeclaredField( "mTextView" );
						mTextViewField.setAccessible( true );
						TextView mTextView = (TextView) mTextViewField.get( tabView );
						//  tabView.setPadding( 0, 0, 0, 0 );
						int width = mTextView.getWidth();
						if (width == 0) {
							mTextView.measure( 0, 0 );
							width = mTextView.getMeasuredWidth();
						}
						margin = (dp - width) / 2;
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				int finalMargin = margin;
				tabLayout.post( () -> {
					if (listener != null) {
						listener.onTabLayoutTextToLeftRight( finalMargin );
					}
				} );
				super.run();
			}
		}.start();
	}

	public static void getTabLayoutTextViewToLeft2(TabLayout tabLayout, int index,
																								 OnTabLayoutTextToLeftListener2 listener) {
		LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt( 0 );
		int count = mTabStrip.getChildCount();
		ExecutorService executorService = Executors.newFixedThreadPool( 1 );
		for (int i = 0; i < count; i++) {
			if (i < index) {
				executorService.execute( new MyThread2( mTabStrip, i, tabLayout, listener ) );
			} else {
				break;
			}
		}
	}


	public static void getTabLayoutTextViewToLeft(final TabLayout tabLayout, int index,
																								OnTabLayoutTextToLeftListener listener) {
		LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt( 0 );
		int count = mTabStrip.getChildCount();
		ExecutorService executorService = Executors.newFixedThreadPool( 1 );
		for (int i = 0; i < count; i++) {
			if (i < index) {
				executorService.execute( new MyThread( mTabStrip, index, i, tabLayout, listener ) );
			} else {
				break;
			}
		}
	}

	private static class MyThread implements Runnable {
		private LinearLayout mTabStrip;
		private int index, j;
		private TabLayout tabLayout;
		private OnTabLayoutTextToLeftListener listener;

		public MyThread(LinearLayout mTabStrip, int index, int j, TabLayout tabLayout,
										OnTabLayoutTextToLeftListener listener) {
			this.mTabStrip = mTabStrip;
			this.index = index;
			this.j = j;
			this.tabLayout = tabLayout;
			this.listener = listener;
		}

		@Override
		public synchronized void run() {
			try {
				View tabView = mTabStrip.getChildAt( j );
				Field mTextViewField = tabView.getClass().getDeclaredField( "mTextView" );
				mTextViewField.setAccessible( true );
				TextView mTextView = (TextView) mTextViewField.get( tabView );
				int width = mTextView.getWidth();
				if (width == 0) {
					mTextView.measure( 0, 0 );
					width = mTextView.getMeasuredWidth();
				}
				final int w = width;
				tabLayout.post( () -> {
					LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
					int tWidth = (w + params.leftMargin + (j == (index - 1) ? 0 : params.rightMargin));
					if (listener != null) {
						listener.OnTabLayoutTextToLeft( tWidth, params.rightMargin, j == (index - 1) );
					}
				} );
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}


	private static class MyThread2 implements Runnable {
		private LinearLayout mTabStrip;
		private int j;
		private TabLayout tabLayout;
		private OnTabLayoutTextToLeftListener2 listener;

		public MyThread2(LinearLayout mTabStrip, int j, TabLayout tabLayout,
										 OnTabLayoutTextToLeftListener2 listener) {
			this.mTabStrip = mTabStrip;
			this.j = j;
			this.tabLayout = tabLayout;
			this.listener = listener;
		}

		@Override
		public synchronized void run() {
			try {
				View tabView = mTabStrip.getChildAt( j );
				Field mTextViewField = tabView.getClass().getDeclaredField( "mTextView" );
				mTextViewField.setAccessible( true );
				TextView mTextView = (TextView) mTextViewField.get( tabView );
				int width = mTextView.getWidth();
				if (width == 0) {
					mTextView.measure( 0, 0 );
					width = mTextView.getMeasuredWidth();
				}
				final int w = width;
				tabLayout.post( () -> {
					LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
					int tWidth = (w + params.leftMargin);
					MyLog.d( "w is " + w + " left is " + params.leftMargin );
					if (listener != null) {
						listener.OnTabLayoutTextToLeft( tWidth, params.leftMargin, j );
					}
				} );
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public static void setIndicatorWidth(TabLayout tabLayout) {
		LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt( 0 );
		int dp = DisplayMetricsUtils.dipTopx( tabLayout.getContext(), 4 );
		int count = mTabStrip.getChildCount();
		for (int i = 0; i < count; i++) {
			final int index = i;
			new Thread() {
				@Override
				public void run() {
					try {
						View tabView = mTabStrip.getChildAt( index );
						Field mTextViewField = tabView.getClass().getDeclaredField( "mTextView" );
						mTextViewField.setAccessible( true );
						TextView mTextView = (TextView) mTextViewField.get( tabView );
						tabLayout.post( () -> {
							tabView.setPadding( 0, 0, 0, 0 );
							int width = mTextView.getWidth();
							if (width == 0) {
								mTextView.measure( 0, 0 );
								width = mTextView.getMeasuredWidth();
							}
							LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
							params.width = width;
							params.leftMargin = dp;
							params.rightMargin = dp;
							tabView.setLayoutParams( params );
							tabView.invalidate();
						} );
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					super.run();
				}
			}.start();
		}
	}

	public static void showBadge(Context context, View targetView, String text, int gravity) {
		QBadgeView qBadgeView = new QBadgeView( context );
		qBadgeView.bindTarget( targetView );
		qBadgeView.setBadgeText( text );
		qBadgeView.setBadgeGravity( gravity );
	}

	public static List<Activity> getActivitiesByApplication(Application application) {
		List<Activity> list = new ArrayList<>();
		try {
			Class<Application> applicationClass = Application.class;
			Field mLoadedApkField = applicationClass.getDeclaredField( "mLoadedApk" );
			mLoadedApkField.setAccessible( true );
			Object mLoadedApk = mLoadedApkField.get( application );
			Class<?> mLoadedApkClass = mLoadedApk.getClass();
			Field mActivityThreadField = mLoadedApkClass.getDeclaredField( "mActivityThread" );
			mActivityThreadField.setAccessible( true );
			Object mActivityThread = mActivityThreadField.get( mLoadedApk );
			Class<?> mActivityThreadClass = mActivityThread.getClass();
			Field mActivitiesField = mActivityThreadClass.getDeclaredField( "mActivities" );
			mActivitiesField.setAccessible( true );
			Object mActivities = mActivitiesField.get( mActivityThread ); // 注意这里一定写成Map，低版本这里用的是HashMap，高版本用的是ArrayMap
			if (mActivities instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<Object, Object> arrayMap = (Map<Object, Object>) mActivities;
				for (Map.Entry<Object, Object> entry : arrayMap.entrySet()) {
					Object value = entry.getValue();
					Class<?> activityClientRecordClass = value.getClass();
					Field activityField = activityClientRecordClass.getDeclaredField( "activity" );
					activityField.setAccessible( true );
					Object o = activityField.get( value );
					list.add( (Activity) o );
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			list = null;
		}
		return list;
	}


	public static void startQQ(Context context, String qq, String key) {
		if (ApkUtils.checkAppExist( context, "com.tencent.mobileqq" )) {
			try {
				String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;// uin是发送过去的qq号码
				context.startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse( url ) ) );
			} catch (Exception e) {
				ToastUtils.showToast( context, context.getResources().getString( R.string.error_qq ) );
			}
		} else {
			if (!TextUtils.isEmpty( key )) {
				Intent intent = new Intent();
				intent.setData( Uri.parse( key ) );
				intent.setAction( Intent.ACTION_VIEW );
				context.startActivity( intent );
			} else {
				ToastUtils.showToast( context, context.getResources().getString( R.string.check_qq ) );
			}
		}
	}

	/**
	 * 判断app是否处于前台
	 *
	 * @return
	 */
	public static boolean isAppOnForeground(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService( Context.ACTIVITY_SERVICE );
		String packageName = context.getPackageName();
		/**
		 * 获取Android设备中所有正在运行的App
		 */
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals( packageName )
					&& appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

}
