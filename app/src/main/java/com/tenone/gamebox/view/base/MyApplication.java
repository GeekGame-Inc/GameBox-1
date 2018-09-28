package com.tenone.gamebox.view.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.AppConfigModle;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.HttpUrlModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.presenter.AppStatisticsManager;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.EasyPermissions;
import com.tenone.gamebox.view.utils.FileUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.HttpUtils;
import com.tenone.gamebox.view.utils.MyLog;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.database.RealmUtils;
import com.thoughtworks.xstream.XStream;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;

public class MyApplication extends MultiDexApplication implements HttpResultListener, Application.ActivityLifecycleCallbacks {
	public static AppConfigModle configModle;
	public static HttpUrlModel httpUrlModel;
	public int downSize = 0;
	public static List<String> installingPacks = new ArrayList<String>();

	public static List<String> installedGameIds = new ArrayList<String>();

	private static MyApplication instance;

	private static IWXAPI iwxapi;

//	private PatchManager mPatchManager;

	public static MyApplication getInstance() {
		return instance;
	}

	public static IWXAPI getIwxapi() {
		return iwxapi;
	}

	private String platform = "0";
	private String coin = "0";
	private String recom_bonus = "0";
	private boolean isVip = false;

	private static boolean isShowDiscount = false;

	public static boolean isIsShowDiscount() {
		return isShowDiscount;
	}

	public static void setIsShowDiscount(boolean isShowDiscount) {
		MyApplication.isShowDiscount = isShowDiscount;
	}

	private static int topGameId = 0, defultGameId = 0;

	public static int getTopGameId() {
		return topGameId;
	}

	public static void setTopGameId(int topGameId) {
		MyApplication.topGameId = topGameId;
	}

	public static void setDefultGameId(int defultGameId) {
		MyApplication.defultGameId = defultGameId;
	}

	public static int getDefultGameId() {
		return defultGameId;
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext( base );
		MultiDex.install( this );
	}

	@Override
	public void onCreate() {
		CrashReport.initCrashReport( getApplicationContext(), "5db0ac0b63", true );
		//update();
		instance = this;
		SpUtil.initSharedPreferences( getApplicationContext() );
		initConfigModle();
		Configuration.initConfiguration( getApplicationContext() );
		initHttpUrl();
		ShareSDK.initSDK( getApplicationContext() );
		EasyPermissions.initEasyPermissionsHintMap();
		iwxapi = WXAPIFactory.createWXAPI( getApplicationContext(), null );
		iwxapi.registerApp( "wx998abec7ee53ed78" );
		RealmUtils.initRealm( this );
		new InitAccountThread().start();
		registerActivityLifecycleCallbacks( this );
		super.onCreate();
	}

	/*private void update() {
		String patchFileStr = getApplicationContext().getFilesDir().getAbsolutePath() + "/185box.apatch";
		File file = new File( patchFileStr );
		if (file.exists()) {
			String versionName = ApkUtils.getVersionName( this );
			mPatchManager = new PatchManager( this );
			mPatchManager.init( versionName );
			mPatchManager.loadPatch();
			try {
				mPatchManager.addPatch( patchFileStr );
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}*/

	public void initConfigModle() {
		String fileName = "AppConfig.xml"; // �ļ�����
		try {
			InputStream in = getResources().getAssets().open( fileName );
			XStream xstream = new XStream();
			xstream.alias( "AppConfigModle", AppConfigModle.class );
			AppConfigModle mode = (AppConfigModle) xstream.fromXML( in );
			setConfigModle( mode );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static AppConfigModle getConfigModle() {
		return configModle;
	}

	public static void setConfigModle(AppConfigModle configModle) {
		MyApplication.configModle = configModle;
	}

	public static HttpUrlModel getHttpUrl() {
		return httpUrlModel;
	}

	public static void setHttpUrl(HttpUrlModel httpUrlModel) {
		MyApplication.httpUrlModel = httpUrlModel;
	}

	private void initHttpUrl() {
		httpUrlModel = new HttpUrlModel();
		new InitUrlThread().start();
	}

	private void assignmentHttpUrl(ResultItem resultItem) {
		httpUrlModel.setBaseUrl( resultItem.getString( "DOAMIN" ) );
		httpUrlModel.setGameClass( resultItem.getString( "GAME_CLASS" ) );
		httpUrlModel.setGameClassInfo( resultItem.getString( "GAME_CLASS_INFO" ) );
		httpUrlModel.setGameCollect( resultItem.getString( "GAME_COLLECT" ) );
		httpUrlModel.setGameIndex( resultItem.getString( "GAME_INDEX" ) );
		httpUrlModel.setGameInfo( resultItem.getString( "GAME_INFO" ) );
		httpUrlModel.setGameType( resultItem.getString( "GAME_TYPE" ) );
		httpUrlModel.setGetPack( resultItem.getString( "PACKS_LINGQU" ) );
		httpUrlModel.setGetPacksByGame( resultItem.getString( "GAME_PACK" ) );
		httpUrlModel.setGetPacksByUser( resultItem.getString( "USER_PACK" ) );
		httpUrlModel.setGetPacksList( resultItem.getString( "PACKS_LIST" ) );
		httpUrlModel.setOpenServer( resultItem.getString( "OPEN_SERVER" ) );
		httpUrlModel.setArticleListByGame( resultItem.getString( "GAME_GONGLUE" ) );
		httpUrlModel
				.setGameOpenServer( resultItem.getString( "GAME_OPEN_SERVER" ) );
		httpUrlModel.setSlide( resultItem.getString( "PACKS_SLIDE" ) );
		httpUrlModel.setGameAllName( resultItem.getString( "GAME_GETALLNAME" ) );
		httpUrlModel.setGameUpdata( resultItem.getString( "GAME_UPDATA" ) );
		httpUrlModel.setHotGameSearch( resultItem.getString( "GAME_GETHOT" ) );
		httpUrlModel
				.setGameSearchList( resultItem.getString( "GAME_SEARCH_LIST" ) );
		httpUrlModel.setArticleList( resultItem.getString( "INDEX_ARTICLE" ) );
		httpUrlModel.setLogin( resultItem.getString( "USER_LOGIN" ) );
		httpUrlModel.setRegister( resultItem.getString( "USER_REGISTER" ) );
		httpUrlModel.setSendCode( resultItem.getString( "USER_SENDMSG" ) );
		httpUrlModel.setCheckSmscode( resultItem.getString( "USER_CHECKMSG" ) );
		httpUrlModel.setForgetPassword( resultItem.getString( "USER_FINDPWD" ) );
		httpUrlModel.setModifyPassword( resultItem.getString( "USER_MODIFYPWD" ) );
		httpUrlModel.setUploadPortrait( resultItem.getString( "USER_UPLOAD" ) );
		httpUrlModel.setModifyNicename( resultItem.getString( "USER_MODIFYNN" ) );
		httpUrlModel.setMyCollect( resultItem.getString( "GAME_MY_COLLECT" ) );
		httpUrlModel.setChannelDownload( resultItem
				.getString( "GAME_CHANNEL_DOWNLOAD" ) );
		httpUrlModel.setGameInstall( resultItem.getString( "GAME_INSTALL" ) );
		httpUrlModel.setGameUninstall( resultItem.getString( "GAME_UNINSTALL" ) );
		httpUrlModel.setGameGrade( resultItem.getString( "GAME_GRADE" ) );
		httpUrlModel.setCheckApp( resultItem.getString( "GAME_CHECK_CLIENT" ) );
		httpUrlModel.setDownloadingTimes( resultItem
				.getString( "GAME_DOWNLOAD_RECORD" ) );
		httpUrlModel.setChangegameApply( resultItem
				.getString( "CHANGEGAME_APPLY" ) );
		httpUrlModel.setChangegameLog( resultItem.getString( "CHANGEGAME_LOG" ) );
		httpUrlModel.setCustomerService( resultItem
				.getString( "CUSTOMER_SERVICE" ) );
		httpUrlModel.setCoinInfo( resultItem.getString( "COIN_INFO" ) );
		httpUrlModel.setGoldCoin( resultItem.getString( "COIN_LOG" ) );
		httpUrlModel.setMyCoin( resultItem.getString( "MY_COIN" ) );
		httpUrlModel.setPlatExchange( resultItem.getString( "PLAT_EXCHANGE" ) );
		httpUrlModel.setPlatformmoney( resultItem.getString( "PLAT_LOG" ) );
		httpUrlModel.setSignInit( resultItem.getString( "SIGN_INIT" ) );
		httpUrlModel.setDoSign( resultItem.getString( "DO_SIGN" ) );
		httpUrlModel.setFrendRecom( resultItem.getString( "FREND_RECOM" ) );
		httpUrlModel.setRebateNotice( resultItem.getString( "REBATE_NOTICE" ) );
		httpUrlModel.setRebateApply( resultItem.getString( "REBATE_APPLY" ) );
		httpUrlModel.setRebateInfo( resultItem.getString( "REBATE_INFO" ) );
		httpUrlModel.setRebateRecord( resultItem.getString( "REBATE_RECORD" ) );
		httpUrlModel.setChangegameKnow( resultItem
				.getString( "CHANGEGAME_NOTICE" ) );
		httpUrlModel.setRebateKnow( resultItem.getString( "REBATE_KNOW" ) );
		httpUrlModel.setUserCenter( resultItem.getString( "USER_CENTER" ) );
		httpUrlModel.setFriendRecomInfo( resultItem
				.getString( "FRIEND_RECOM_INFO" ) );
		httpUrlModel.setGetVipOption( resultItem.getString( "VIP_OPTION" ) );
		httpUrlModel.setPayReady( resultItem.getString( "PAY_READY" ) );
		httpUrlModel.setPayQuery( resultItem.getString( "PAY_QUERY" ) );
		httpUrlModel.setPayStart( resultItem.getString( "PAY_START" ) );
		httpUrlModel.setGiveCoin( resultItem.getString( "COMMENT_COIN" ) );
		httpUrlModel.setLuckyUrl( resultItem.getString( "LUCKY_DRAW" ) );
		httpUrlModel.setPrizes( resultItem.getString( "MY_PRIZE" ) );
		httpUrlModel.setBindMobile( resultItem.getString( "USER_BIND_MOBILE" ) );
		httpUrlModel.setGetStartImgs( resultItem
				.getString( "GAME_GET_START_IMGS" ) );
		httpUrlModel.setNotice( resultItem.getString( "APP_NOTICE" ) );
		httpUrlModel.setGetApatch( resultItem.getString( "GET_PATCH" ) );
		httpUrlModel.setBoxInstallInfo( resultItem.getString( "GAME_BOX_INSTALL_INFO" ) );
		httpUrlModel.setBoxStartInfo( resultItem.getString( "GAME_BOX_START_INFO" ) );

		httpUrlModel.setMessageInfo( resultItem.getString( "MESSAGE_INFO" ) );
		httpUrlModel.setMessageList( resultItem.getString( "MESSAGE_LIST" ) );
		httpUrlModel.setDeleteMessage( resultItem.getString( "MESSAGE_DELETE" ) );
		httpUrlModel.setGetBonus( resultItem.getString( "PLAT_REG_BONUS" ) );

		httpUrlModel.setGetDynamics( resultItem.getString( "GET_DYNAMICS" ) );
		httpUrlModel.setPublishDynamics( resultItem.getString( "PUBLISH_DYNAMICS" ) );
		httpUrlModel.setDoComment( resultItem.getString( "COMMENT" ) );
		httpUrlModel.setCommentList( resultItem.getString( "COMMENT_LIST" ) );
		httpUrlModel.setDeleteComment( resultItem.getString( "COMMENT_DEL" ) );
		httpUrlModel.setDynamicsLike( resultItem.getString( "DYNAMICS_LIKE" ) );
		httpUrlModel.setCommentLike( resultItem.getString( "COMMENT_LIKE" ) );
		httpUrlModel.setFollowList( resultItem.getString( "FOLLOW_LIST" ) );
		httpUrlModel.setUserDesc( resultItem.getString( "USER_DESC" ) );
		httpUrlModel.setEditDesc( resultItem.getString( "USER_EDIT" ) );
		httpUrlModel.setUnreadCounts( resultItem.getString( "MESSAGE_UNREAD" ) );
		httpUrlModel.setFollowOrCancel( resultItem.getString( "FOLLOW_OR_CANCEL" ) );
		httpUrlModel.setDynamicsWapInfo( resultItem.getString( "DYNAMICS_WAP_INFO" ) );
		httpUrlModel.setShareDynamics( resultItem.getString( "SHARE_DYNAMICS" ) );
		httpUrlModel.setNewUpCounts( resultItem.getString( "USER_NEW_UP" ) );
		httpUrlModel.setMyCommentZan( resultItem.getString( "USER_COMMENT_ZAN" ) );
		httpUrlModel.setDoInit( resultItem.getString( "BOX_INIT" ) );
		httpUrlModel.setCancelDynamicsLike( resultItem.getString( "CANCEL_DYNAMICS_LIKE" ) );
		httpUrlModel.setCancelCommentLike( resultItem.getString( "CANCEL_COMMENT_LIKE" ) );
		httpUrlModel.setAgreement( resultItem.getString( "USER_AGREEMENT" ) );
		httpUrlModel.setRankingList( resultItem.getString( "RANKING_LIST" ) );
		httpUrlModel.setReceiveReward( resultItem.getString( "RECEIVE_REWARD" ) );
		httpUrlModel.setDelDynamic( resultItem.getString( "DEL_DYNAMIC" ) );
		httpUrlModel.setPackInfo( resultItem.getString( "PACKAGE_INFO" ) );
		httpUrlModel.setCommentCount( resultItem.getString( "COMMENT_COUNTS" ) );
		httpUrlModel.setUserRanking( resultItem.getString( "USER_RANKING" ) );
		httpUrlModel.setRankNotice( resultItem.getString( "RANKNOTICE" ) );
		httpUrlModel.setReplayComment( resultItem.getString( "COMMENT_REPLY_LIST" ) );
		httpUrlModel.setUserLoginApp( resultItem.getString( "USER_APP_LOGIN" ) );
		httpUrlModel.setReportData( resultItem.getString( "GDT_REPORT" ) );
		httpUrlModel.setJrttReportData( resultItem.getString( "JRTT_REPORT" ) );
		httpUrlModel.setNewIndex( resultItem.getString( "GAME_NEWINDEX" ) );
		httpUrlModel.setTaskCenter( resultItem.getString( "TASK_CENTER" ) );
		httpUrlModel.setAppPromise( resultItem.getString( "APP_PROMISE" ) );
		httpUrlModel.setDoInitV2( resultItem.getString( "BOX_INIT_V2" ) );
		httpUrlModel.setNewGameType( resultItem.getString( "NEW_GAME_TYPE" ) );
		httpUrlModel.setNewGameList( resultItem.getString( "NEW_GAME_LIST" ) );
		httpUrlModel.setSendMessage( resultItem.getString( "BSP_SENDMSG" ) );
		httpUrlModel.setTradingLogin( resultItem.getString( "BSP_LOGIN" ) );
		httpUrlModel.setTradingRegister( resultItem.getString( "BSP_REGISTER" ) );
		httpUrlModel.setTradingModifyPassword( resultItem.getString( "BSP_MODIFYPWD" ) );
		httpUrlModel.setTradingForgetPassword( resultItem.getString( "BSP_FORGETPWD" ) );
		httpUrlModel.setProductList( resultItem.getString( "PRODUCT_LIST" ) );
		httpUrlModel.setSdkuserList( resultItem.getString( "SDKUSER_LIST" ) );
		httpUrlModel.setUnBindSdkUser( resultItem.getString( "UNBIND_SDKUSER" ) );
		httpUrlModel.setBindSdkUser( resultItem.getString( "BIND_SDKUSER" ) );
		httpUrlModel.setSellProduct( resultItem.getString( "SELL_PRODUCTS" ) );
		httpUrlModel.setProductInfo( resultItem.getString( "PRODUCT_INFO" ) );
		httpUrlModel.setTradingStartPayment( resultItem.getString( "START_PAYMENT" ) );
		httpUrlModel.setTradingCancelPayment( resultItem.getString( "CANCEL_PAYMENT" ) );
		httpUrlModel.setEditUser( resultItem.getString( "BSP_EDITUSER" ) );
		httpUrlModel.setCustomer( resultItem.getString( "PRODUCT_CUSTOMER" ) );
		httpUrlModel.setGetProductByUser( resultItem.getString( "PRODUCT_BYUSER" ) );
		httpUrlModel.setBuyerRecord( resultItem.getString( "BUYER_RECORD" ) );
		httpUrlModel.setDeleteProduct( resultItem.getString( "DELETE_PRODUCTS" ) );
		httpUrlModel.setWithdrawProduct( resultItem.getString( "WITHDRAW_PRODUCTS" ) );
		httpUrlModel.setApplyOnsale( resultItem.getString( "PRODUCT_ONSALE" ) );
		httpUrlModel.setTradeNotesH5( resultItem.getString( "TRADE_NOTES_H5" ) );
		httpUrlModel.setTradeNotes( resultItem.getString( "TRADE_NOTES" ) );
		httpUrlModel.setExclusiveList( resultItem.getString( "EXCLUSIVE_ACT" ) );
		httpUrlModel.setSellerRecord( resultItem.getString( "SELLER_RECORD" ) );
		httpUrlModel.setNewgameReserve( resultItem.getString( "RESERVE_NEWGAME" ) );
		httpUrlModel.setReserveSuccess( resultItem.getString( "RESERVE_SUCCESS" ) );
		httpUrlModel.setSdkLogin( resultItem.getString( "SDK_LOGIN_URL" ) );
		httpUrlModel.setShareArticle( resultItem.getString( "ARITCLE_SHARE" ) );
		httpUrlModel.setArticleLike( resultItem.getString( "ARTICLE_LIKE" ) );
		httpUrlModel.setUnBindMobile( resultItem.getString( "USER_UNBIND_MOBILE" ) );
		httpUrlModel.setDoReport( resultItem.getString( "ACT_STATIC" ) );
		httpUrlModel.setConsultList( resultItem.getString( "CONSULT_LIST" ) );
		httpUrlModel.setPutQuestion( resultItem.getString( "PUT_QUESTION" ) );
		httpUrlModel.setConsultInfo(resultItem.getString( "CONSULTINFO_LIST" ) );
		httpUrlModel.setDoReward( resultItem.getString( "CONSULTINFO_REWARD" )  );
		httpUrlModel.setDoAnswer( resultItem.getString( "CONSULTINFO_ANSWER" )  );
		httpUrlModel.setMyQuestions( resultItem.getString( "MY_QUESTION" )  );
		httpUrlModel.setAnswerGame( resultItem.getString( "CONSULT_ANSWERGAME" )  );
		httpUrlModel.setBoutiqueGame( resultItem.getString( "HQ_GAME" )  );
		setHttpUrl( httpUrlModel );
		new TradeNotesThread().start();
	}

	public void requestHttp(Context cxt, int what, String url) {
		HttpUtils.getHttp( cxt, what, url, this );
	}

	public void setInstallingPacks(String installingPacks) {
		MyApplication.installingPacks.add( installingPacks );
	}

	public static List<String> getInstalledGameIds() {
		return installedGameIds;
	}

	public static void setInstalledGameIds(List<String> installedGameIds) {
		MyApplication.installedGameIds = installedGameIds;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}

	public String getRecom_bonus() {
		return recom_bonus;
	}

	public void setRecom_bonus(String recom_bonus) {
		this.recom_bonus = recom_bonus;
	}

	public boolean isVip() {
		return isVip;
	}

	public void setVip(boolean isVip) {
		this.isVip = isVip;
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		if ("1".equals( resultItem.getString( "status" ) )) {
			Object obj = resultItem.getValue( "data" );
			if (obj instanceof ResultItem && obj != null && resultItem.getItem( "data" ) != null) {
				assignmentHttpUrl( resultItem.getItem( "data" ) );
			}
		} else {
			Toast.makeText( getApplicationContext(),
					resultItem.getString( "msg" ), Toast.LENGTH_SHORT ).show();
		}
	}

	@Override
	public void onError(int what, String error) {
		Toast.makeText( getApplicationContext(), error, Toast.LENGTH_SHORT )
				.show();
	}


	int activityCount = 0;

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

	}

	@Override
	public void onActivityStarted(Activity activity) {
		if (activityCount == 0) {
			//app�ص�ǰ̨
			Log.i( "AppStatisticsManager", "app \u542f\u52a8" );
		}
		activityCount++;
	}

	@Override
	public void onActivityResumed(Activity activity) {

	}

	@Override
	public void onActivityPaused(Activity activity) {

	}

	@Override
	public void onActivityStopped(Activity activity) {
		activityCount--;
		if (activityCount == 0) {
			Log.i( "AppStatisticsManager", "app \u8fdb\u5165\u540e\u53f0" );
			AppStatisticsManager.addStatistics( "", true );
		}
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

	}

	@Override
	public void onActivityDestroyed(Activity activity) {

	}

	private class InitUrlThread extends Thread {
		@Override
		public void run() {
			requestHttp( getApplicationContext(), HttpType.REFRESH,
					configModle.getDownDomainName() );
			super.run();
		}
	}

	private class InitAccountThread extends Thread {
		@Override
		public void run() {
			String str = FileUtils.getAccountNew( getApplicationContext() );
			if (TextUtils.isEmpty( str )) {
				str = FileUtils.getAccount( getApplicationContext() );
				if (!TextUtils.isEmpty( str )) {
					String[] array = str.split( ";" );
					if (array.length > 0) {
						SpUtil.setAccount( array[0] );
					}
					if (array.length > 1) {
						SpUtil.setPwd( array[1] );
					}
					if (array.length > 2) {
						SpUtil.setUserId( array[2] );
					}
					if (array.length > 3) {
						SpUtil.setPhone( array[3] );
					}
				}
			} else {
				try {
					JSONObject object = new JSONObject( str );
					SpUtil.setAccount( object.getString( "userName" ) );
					SpUtil.setPwd( object.getString( "pwd" ) );
					SpUtil.setUserId( object.getString( "uid" ) );
					SpUtil.setPhone( object.getString( "phone" ) );
					SpUtil.setTradingMobile( object.getString( "tradingMobile" ) );
					SpUtil.setTradingPwd( object.getString( "tradingPassword" ) );
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			super.run();
		}
	}


	private class TradeNotesThread extends Thread {
		@Override
		public void run() {
			HttpManager.tradeNotes( 1, getApplicationContext(), new HttpResultListener() {
				@Override
				public void onSuccess(int what, ResultItem resultItem) {
					if (1 == resultItem.getIntValue( "status" )) {
						ResultItem data = resultItem.getItem( "data" );
						String maxPrice = data.getString( "product_price_limit" );
						if (!TextUtils.isEmpty( maxPrice )) {
							Constant.setProductMinPrice( Float.valueOf( maxPrice ).floatValue() );
						}
						if (!BeanUtils.isEmpty( data )) {
							List<ResultItem> buyers = data.getItems( "buyer_notes" );
							if (!BeanUtils.isEmpty( buyers )) {
								setBuyer( buyers );
							}
							List<ResultItem> sellers = data.getItems( "seller_notes" );
							if (!BeanUtils.isEmpty( sellers )) {
								setSeller( sellers );
							}
							List<ResultItem> businesss = data.getItems( "business_notice" );
							if (!BeanUtils.isEmpty( businesss )) {
								setBusiness( businesss );
							}
						}
					} else {
						MyLog.d( resultItem.getString( "msg" ) );
					}
				}

				@Override
				public void onError(int what, String error) {
					MyLog.d( error );
				}
			} );
			super.run();
		}

		private void setBusiness(List<ResultItem> businesss) {
			Constant.getBusinessNotice().clear();
			for (int i = 0; i < businesss.size(); i++) {
				String text = String.valueOf( businesss.get( i ) );
				Constant.getBusinessNotice().add( text );
			}
		}

		private void setSeller(List<ResultItem> sellers) {
			Constant.getSellerNotes().clear();
			for (int i = 0; i < sellers.size(); i++) {
				String text = String.valueOf( sellers.get( i ) );
				Constant.getSellerNotes().add( text );
			}
		}

		private void setBuyer(List<ResultItem> buyers) {
			Constant.getBuyerNotes().clear();
			for (int i = 0; i < buyers.size(); i++) {
				String text = String.valueOf( buyers.get( i ) );
				Constant.getBuyerNotes().add( text );
			}
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		Log.i( "MyApplication", "onLowMemory" );
		System.gc();
	}
}
