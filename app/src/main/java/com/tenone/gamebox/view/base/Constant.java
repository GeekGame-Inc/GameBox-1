package com.tenone.gamebox.view.base;


import java.util.ArrayList;
import java.util.List;

public class Constant {
	public static final String TOUTIAOAPPNAME = "sy185";
	public static final int TOUTIAOAPPID = 152123;
	public static final String IS_FIRST_RUN = "isFirstRun";
	public static final String APPKEY = "709931298992c123ba79f9394032e91e";
	public static final String APPID = "1013";
	public static String messageNum = "0";
	public static final String TRACKINGIOAPPKEY = "e4d2a5dcb1ae3fb5794386f75abbdf04";
	public static final String GDTACTIONID = "1106853768";
	private static String random = "";
	private static float productMinPrice = 20;
	private static boolean isShowTrading = true;
	private static boolean isVip = false;
	private static List<String> buyerNotes = new ArrayList<String>();
	private static List<String> sellerNotes = new ArrayList<String>();
	private static List<String> businessNotice = new ArrayList<String>();
	private static List<String> consultProtocol = new ArrayList<String>();
	private static List<String> consultNotice = new ArrayList<String>();
	private static String questionTaskCoin = "50";

	public static String getMessageNum() {
		return messageNum;
	}

	public static void setMessageNum(String messageNum) {
		Constant.messageNum = messageNum;
	}

	public static String getRandom() {
		return random;
	}

	public static void setRandom(String random) {
		Constant.random = random;
	}


	public static List<String> getBuyerNotes() {
		return buyerNotes;
	}

	public static List<String> getSellerNotes() {
		return sellerNotes;
	}


	public static List<String> getBusinessNotice() {
		return businessNotice;
	}

	public static float getProductMinPrice() {
		return productMinPrice;
	}

	public static void setProductMinPrice(float productMinPrice) {
		Constant.productMinPrice = productMinPrice;
	}

	public static boolean isIsShowTrading() {
		return isShowTrading;
	}

	public static void setIsShowTrading(boolean isShowTrading) {
		Constant.isShowTrading = isShowTrading;
	}

	public static boolean isIsVip() {
		return isVip;
	}

	public static void setIsVip(boolean isVip) {
		Constant.isVip = isVip;
	}

	public static List<String> getConsultProtocol() {
		return consultProtocol;
	}

	public static List<String> getConsultNotice() {
		return consultNotice;
	}


	public static String getQuestionTaskCoin() {
		return questionTaskCoin;
	}

	public static void setQuestionTaskCoin(String questionTaskCoin) {
		Constant.questionTaskCoin = questionTaskCoin;
	}
}
