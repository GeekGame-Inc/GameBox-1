package com.tenone.gamebox.view.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * SharedPreferences 工具类 ClassName: SpUtil <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2017-3-14 下午4:05:06 <br/>
 *
 * @author John Lie
 * @since JDK 1.6
 */
public class SpUtil {

	private static SharedPreferences sp;

	private static final String KEY_SETTINGS = "185BoxMessage";

	public static String headerUrlKey = "headerUrl",// 头像地址
			isExitKey = "isExit",//是否登录
			tradingIsExitKey = "tradingIsExit",//是否登录交易账号
			nickKey = "nick",// 昵称
			userIdKey = "userId",// 用户id
			accountKey = "account",// 账户
			pwdKey = "pwd",// 密码
			phoneKey = "phone",// 电话
			mailboxKey = "mailbox",// 邮箱
			isWifiKey = "isWifiDown",// 是否仅WiFi下载
			isAutoInstallKey = "isAutoInstall",// 是否自动安装
			isAutoClearKey = "isAutoClear",// 是否自动清除
			isNotificationKey = "isNotification",// 是否接收通知
			isSoundKey = "isSound",// 通知是否有声音
			giftSearchRecordKey = "giftSearchRecord",// 礼包搜索记录
			gameSearchRecordKey = "gameSearchRecord",// 游戏搜索记录
			strategySearchRecordKey = "strategySearchRecord",// 攻略搜索记录
			allGameNamesKey = "allGameName",// 所有的游戏名字
			isShakeKey = "isShake",// 通知是否有震动
			gameKey = "allGameNames",// 所有游戏名称
			isLoginCyanKey = "isLoginCyan",// 是否登录畅言
			cyanUserIdKey = "cyanUserIdKey",// 畅言用户id
			isFirstKey = "isFirst",// 是否是第一次启动
			gdtisFirstKey = "gdtisFirst",//广点通是否是第一次
			jrttIsFirstKey = "jrttIsFirst",//今日头条是否是第一次
			tradingUidKey = "tradingUid",//交易账号uid
			tradingUserNameKey = "tradingUserName",//交易账号用户名
			tradingMobileKey = "tradingMobile",// 交易账号手机号
			tradingPwdKey = "tradingPwd",// 交易账号密码
			imeiKey = "imei",// 设备号
			showGuideKey = "showGuide",//是否显示了引导页
			isStatisticsKey = "isStatistic",//新用户是否统计过了
			actionsKey = "actions",//统计的action
			installIsFirstKey = "installIsFirst";//安装统计是否是第一次


	public static void initSharedPreferences(Context context) {
		sp = context.getSharedPreferences( KEY_SETTINGS, Context.MODE_PRIVATE );
	}

	public static String getHeaderUrl() {
		return sp.getString( headerUrlKey, "" );
	}

	public static void setHeaderUrl(String headerUrl) {
		sp.edit().putString( headerUrlKey, headerUrl ).commit();
	}

	public static String getNick() {
		return sp.getString( nickKey, "" );
	}

	public static void setNick(String nick) {
		sp.edit().putString( nickKey, nick ).commit();
	}

	public static String getUserId() {
		return sp.getString( userIdKey, "0" );
	}

	public static void setUserId(String userId) {
		sp.edit().putString( userIdKey, userId ).commit();
	}

	public static void setAccount(String account) {
		sp.edit().putString( accountKey, account ).commit();
	}

	public static String getAccount() {
		return sp.getString( accountKey, "" );
	}

	public static void setPwd(String pwd) {
		sp.edit().putString( pwdKey, pwd ).commit();
	}

	public static String getPwd() {
		return sp.getString( pwdKey, "" );
	}

	public static void setPhone(String phone) {
		sp.edit().putString( phoneKey, phone ).commit();
	}

	public static String getPhone() {
		return sp.getString( phoneKey, "" );
	}

	public static void setAutoInstall(boolean isAutoInstall) {
		sp.edit().putBoolean( isAutoInstallKey, isAutoInstall ).commit();
	}

	public static boolean getAutoInstall() {
		return sp.getBoolean( isAutoInstallKey, true );
	}

	public static void setNotification(boolean isNotification) {
		sp.edit().putBoolean( isNotificationKey, isNotification ).commit();
	}

	public static boolean getNotification() {
		return sp.getBoolean( isNotificationKey, true );
	}

	public static void setSound(boolean isSound) {
		sp.edit().putBoolean( isSoundKey, isSound ).commit();
	}

	public static boolean getSound() {
		return sp.getBoolean( isSoundKey, true );
	}

	public static void setShake(boolean isShake) {
		sp.edit().putBoolean( isShakeKey, isShake ).commit();
	}

	public static boolean getShake() {
		return sp.getBoolean( isShakeKey, true );
	}

	public static void setWifi(boolean isWifi) {
		sp.edit().putBoolean( isWifiKey, isWifi ).commit();
	}

	public static boolean getWifi() {
		return sp.getBoolean( isWifiKey, true );
	}

	public static boolean getAutoClear() {
		return sp.getBoolean( isAutoClearKey, true );
	}

	public static void setAutoClear(boolean isAutoClear) {
		sp.edit().putBoolean( isAutoClearKey, isAutoClear ).commit();
	}

	public static void setBoolean(boolean b, String key) {
		sp.edit().putBoolean( key, b ).commit();
	}

	public static void setGiftSearchRecord(StringBuilder record) {
		sp.edit().putString( giftSearchRecordKey, record.toString() ).commit();
	}

	public static List<String> getGiftSearchRecord() {
		List<String> records = new ArrayList<String>();
		String str = sp.getString( giftSearchRecordKey, "" );
		if (!TextUtils.isEmpty( str )) {
			String[] array = str.split( "," );
			for (int i = (array.length - 1); i > -1; i--) {
				records.add( array[i] );
			}
		}
		return records;
	}

	public static void setGameSearchRecord(StringBuilder record) {
		sp.edit().putString( gameSearchRecordKey, record.toString() ).commit();
	}

	public static List<String> getGameSearchRecord() {
		List<String> records = new ArrayList<String>();
		String str = sp.getString( gameSearchRecordKey, "" );
		if (!TextUtils.isEmpty( str )) {
			String[] array = str.split( "," );
			for (int i = (array.length - 1); i > -1; i--) {
				records.add( array[i] );
			}
		}
		return records;
	}

	public static void setStrategySearchRecord(StringBuilder record) {
		sp.edit().putString( strategySearchRecordKey, record.toString() )
				.commit();
	}

	public static List<String> getStrategySearchRecord() {
		List<String> records = new ArrayList<String>();
		String str = sp.getString( strategySearchRecordKey, "" );
		if (!TextUtils.isEmpty( str )) {
			String[] array = str.split( "," );
			for (int i = (array.length - 1); i > -1; i--) {
				records.add( array[i] );
			}
		}
		return records;
	}

	public static synchronized void setAllGameNames(List<String> names) {
		JSONArray array = new JSONArray();
		for (int i = 0; i < names.size(); i++) {
			try {
				array.put( i, names.get( i ) );
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		JSONObject object = new JSONObject();
		try {
			object.accumulate( gameKey, array );
			sp.edit().putString( allGameNamesKey, object.toString() ).commit();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static synchronized List<String> getAllGameNames() {
		List<String> list = new ArrayList<String>();
		String allGameNames = sp.getString( allGameNamesKey, "" );
		if (!TextUtils.isEmpty( allGameNames )) {
			try {
				JSONObject object = new JSONObject( allGameNames );
				for (int i = 0; i < object.optJSONArray( gameKey ).length(); i++) {
					list.add( object.optJSONArray( gameKey ).getString( i ) );
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public static synchronized void setIsLoginCyan(boolean boolean1) {
		sp.edit().putBoolean( isLoginCyanKey, boolean1 ).commit();
	}

	public static synchronized boolean getIsLoginCyan() {
		return sp.getBoolean( isLoginCyanKey, false );
	}

	public static synchronized void setCyanUserId(long userId) {
		sp.edit().putLong( cyanUserIdKey, userId ).commit();
	}

	public static synchronized long getCyanUserId() {
		return sp.getLong( cyanUserIdKey, 0 );
	}

	public static synchronized boolean isFirst() {
		return sp.getBoolean( isFirstKey, true );
	}


	public static synchronized void setIsFirst(boolean b) {
		sp.edit().putBoolean( isFirstKey, b ).commit();
	}

	public static synchronized boolean jrttIsFirst() {
		return sp.getBoolean( jrttIsFirstKey, true );
	}


	public static synchronized void setJrttIsFirst(boolean b) {
		sp.edit().putBoolean( jrttIsFirstKey, b ).commit();
	}

	public static synchronized boolean installIsFirst() {
		return sp.getBoolean( installIsFirstKey, true );
	}


	public static synchronized void setinstallIsFirst(boolean b) {
		sp.edit().putBoolean( installIsFirstKey, b ).commit();
	}

	public static synchronized boolean gdtIsFirst() {
		return sp.getBoolean( gdtisFirstKey, true );
	}


	public static synchronized void setGdtIsFirst(boolean b) {
		sp.edit().putBoolean( gdtisFirstKey, b ).commit();
	}

	public static synchronized void setIsUpdateing(boolean b) {
		sp.edit().putBoolean( "isUpdateing", b ).commit();
	}

	public static synchronized boolean isUpdateing() {
		return sp.getBoolean( "isUpdateing", false );
	}

	public static synchronized String getSplashImgUrl() {
		return sp.getString( "splash", "" );
	}

	public static synchronized void setSplashImgUrl(String splash) {
		sp.edit().putString( "splash", splash ).commit();
	}

	public static synchronized boolean isExit() {
		return sp.getBoolean( isExitKey, false );
	}

	public static synchronized void setExit(boolean isExit) {
		sp.edit().putBoolean( isExitKey, isExit ).commit();
	}

	public static synchronized String getTradingUid() {
		return sp.getString( tradingUidKey, "" );
	}

	public static synchronized void setTradingUid(String tradingUid) {
		sp.edit().putString( tradingUidKey, tradingUid ).commit();
	}

	public static synchronized String getTradingUserName() {
		return sp.getString( tradingUserNameKey, "" );
	}

	public static synchronized void setTradingUserName(String tradingUserName) {
		sp.edit().putString( tradingUserNameKey, tradingUserName ).commit();
	}

	public static synchronized String getTradingMobile() {
		return sp.getString( tradingMobileKey, "" );
	}

	public static synchronized void setTradingMobile(String tradingMobile) {
		sp.edit().putString( tradingMobileKey, tradingMobile ).commit();
	}

	public static synchronized String getTradingPwd() {
		return sp.getString( tradingPwdKey, "" );
	}

	public static synchronized void setTradingPwd(String tradingPwd) {
		sp.edit().putString( tradingPwdKey, tradingPwd ).commit();
	}


	public static synchronized boolean tradingIsExit() {
		return sp.getBoolean( tradingIsExitKey, false );
	}

	public static synchronized void setTradingIsExit(boolean tradingIsExit) {
		sp.edit().putBoolean( tradingIsExitKey, tradingIsExit ).commit();
	}

	public static synchronized String getImei() {
		return sp.getString( imeiKey, "" );
	}

	public static synchronized void setImei(String imei) {
		sp.edit().putString( imeiKey, imei ).commit();
	}

	public static synchronized boolean getShowGuide() {
		return sp.getBoolean( showGuideKey, false );
	}

	public static synchronized void setShowGuide(boolean showGuide) {
		sp.edit().putBoolean( showGuideKey, showGuide ).commit();
	}

	public static synchronized boolean isStatistics() {
		return sp.getBoolean( isStatisticsKey, false );
	}

	public static synchronized void setIsStatistics(boolean isStatistics) {
		sp.edit().putBoolean( isStatisticsKey, isStatistics ).commit();
	}

	public static synchronized String getActions() {
		return sp.getString( actionsKey, "" );
	}

	public static synchronized void setActions(String actions) {
		sp.edit().putString( actionsKey, actions ).commit();
	}
}
