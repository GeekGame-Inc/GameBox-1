package com.tenone.gamebox.view.utils;

import android.content.Context;

import com.reyun.sdk.TrackingIO;
import com.tenone.gamebox.view.base.Constant;
import com.tenone.gamebox.view.base.MyApplication;

import java.util.HashMap;
import java.util.Map;

public class TrackingUtils {
	private static boolean isStatistical = false;
	private static boolean isTrackingInit = false;

	public static final String DOWNLOADEVENT = "download_game";
	public static final String SIGNEVENT = "sign_in";
	public static final String COMMENTSEVENT = "comments_game";
	public static final String SHAREEVENT = "share_box";
	public static final String LUCKYDRAWEVENT = "lucky_draw";
	public static final String EXCHANGEEVENT = "exchange_platform_coin";
	public static final String OPENVIPEVENT = "open_vip_action";

	public static final String USERNAMEKEY = "user_name";
	public static final String NICKNAMEKEY = "nick_name";
	public static final String MOBILEKEY = "user_mobile";
	public static final String BIRTHDAYKEY = "user_birthday";
	public static final String GENDERKEY = "user_gender";
	public static final String LOCKEY = "user_loc";
	public static final String SHARESTYLEKEY = "share_way";
	public static final String PUBLISGDYNAMIC = "publish_dynamic";
	public static final String GAMENAME = "game_name";
	public static final String EDITUSERINFOKEY = "edit_user_info";

	private static Map<String, Object> userInfoMap = new HashMap<String, Object>();


	public static boolean isIsStatistical() {
		return isStatistical;
	}

	public static boolean isIsTrackingInit() {
		return isTrackingInit;
	}


	public static void setIsTrackingInit(boolean isTrackingInit) {
		TrackingUtils.isTrackingInit = isTrackingInit;
	}

	public static void initUserInfoMap() {
		userInfoMap.put( USERNAMEKEY, SpUtil.getAccount() );
		userInfoMap.put( NICKNAMEKEY, SpUtil.getNick() );
		userInfoMap.put( MOBILEKEY, SpUtil.getPhone() );
	}

	public static Map<String, Object> getUserInfoMap() {
		return userInfoMap;
	}

	public static void setIsStatistical(boolean isStatistical) {
		TrackingUtils.isStatistical = isStatistical;
	}

	public static void initTracking(Context context) {
		if (isIsStatistical()) {
			new Thread() {
				@Override
				public void run() {
					TrackingIO.initWithKeyAndChannelId( context,
							Constant.TRACKINGIOAPPKEY, MyApplication.getConfigModle().getChannelID() );
					setIsTrackingInit( true );
					super.run();
				}
			}.start();
		}
	}

	public static void setRegisterWithAccountID(String uid) {
		if (isStatistical && isTrackingInit) {
			TrackingIO.setRegisterWithAccountID( uid );
		}
	}


	public static void setLoginSuccessBusiness(String uid) {
		if (isStatistical && isTrackingInit) {
			TrackingIO.setLoginSuccessBusiness( uid );
		}
	}

	public static void setEvent(String eventName, Map<String, Object> extra) {
		if (isStatistical && isTrackingInit) {
			TrackingIO.setEvent( eventName, extra );
		}
	}

	public static void setProfile(Map<String, Object> dataDic) {
		if (isStatistical && isTrackingInit) {
			TrackingIO.setProfile( dataDic );
		}
	}

	public static void setPaymentStart(String transactionId, String paymentType, String currencyType, float currencyAmount) {
		if (isStatistical && isTrackingInit) {
			TrackingIO.setPaymentStart( transactionId, paymentType, currencyType, currencyAmount );
		}
	}

	public static void setPayment(String transactionId, String paymentType, String currencyType, float currencyAmount) {
		if (isStatistical && isTrackingInit) {
			TrackingIO.setPayment( transactionId, paymentType, currencyType, currencyAmount );
		}
	}

}
