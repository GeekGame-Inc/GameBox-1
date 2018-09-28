/**
 * Project Name:GameBox
 * File Name:HttpUrlModel.java
 * Package Name:com.tenone.gamebox.mode.mode
 * Date:2017-4-18����10:51:22
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

public class HttpUrlModel implements Serializable {

	private static final long serialVersionUID = 1113896159775685174L;

	private String baseUrl = "http://www.185sy.com";
	private String getPacksList = baseUrl + "/api-packs-get_list";
	private String getPacksByGame = baseUrl + "/api-packs-get_list_by_game";
	private String getPacksByUser = baseUrl + "/api-packs-get_list_by_user";
	private String getPack = baseUrl + "/api-packs-get_pack";
	private String getSlide = baseUrl + "/api-packs-get_slide";

	private String boxInstallInfo = baseUrl + "/api-game-boxInstallInfo";

	private String boxStartInfo = baseUrl + "/api-game-boxStartInfo";
	private String gameIndex = baseUrl + "/api-game-index";
	private String newGameType = baseUrl + "/api-game-newGameType";

	private String newGameList = baseUrl + "/api-game-newgameList";

	private String gameType = baseUrl + "/api-game-gameType";
	private String openServer = baseUrl + "/api-game-openServer";
	private String gameInfo = baseUrl + "/api-game-gameInfo";
	private String gameCollect = "/api-game-collect";
	private String gameClass = baseUrl + "/api-game-gameClass";
	private String gameClassInfo = baseUrl + "/api-game-gameClassInfo";
	private String gameAllName = baseUrl + "/api-game-getAllGameName";
	private String gameUpdata = baseUrl + "/api-game-gameUpdata";
	private String hotGameSearch = baseUrl + "/api-game-hotGameSearch";
	private String gameSearchList = baseUrl + "/api-game-gameSearchList";
	private String channelDownload = baseUrl + "/api-game-channelDownload";
	private String myCollect = baseUrl + "/api-game-myCollect";
	private String gameGrade = baseUrl + "/api-game-gameGrade";

	private String gameInstall = baseUrl + "/api-game-gameInstall";

	private String gameUninstall = baseUrl + "/api-game-gameUninstall";
	private String downloadingTimes = baseUrl + "/api-game-downloadRecord";
	private String articleListByGame = baseUrl + "/api-article-get_list_by_game";

	private String articleList = baseUrl + "/api-article-get_list";
	private String exclusiveList = "http://api.185sy.com/index.php?g=api&m=article&a=exclusive_list";
	private String gameOpenServer = baseUrl + "/api-game-gameOpenServer";
	private String login = baseUrl + "/api-user-login";
	private String register = baseUrl + "/api-user-register";
	private String sendCode = baseUrl + "/api-user-send_message";

	private String checkSmscode = baseUrl + "/api-user-check_smscode";
	private String forgetPassword = baseUrl + "/api-user-forget_password";
	private String modifyPassword = baseUrl + "/api-user-modify_password";
	private String modifyNicename = baseUrl + "/api-user-modify_nicename";
	private String uploadPortrait = baseUrl + "/api-user-upload_portrait";
	private String checkApp = baseUrl + "/api-game-checkClient";


	private String getVipOption = baseUrl + "/index.php?g=api&m=pay&a=getVipOption";
	private String payStart = baseUrl + "/index.php?g=api&m=pay&a=payStart";
	private String payReady = baseUrl + "/index.php?g=api&m=pay&a=payReady";
	private String payQuery = baseUrl + "/index.php?g=api&m=pay&a=payQuery";
	private String changegameApply = baseUrl + "/index.php?g=api&m=changegame&a=apply";
	private String changegameLog = baseUrl + "/index.php?g=api&m=changegame&a=log";
	private String changegameKnow = baseUrl + "/index.php?g=api&m=changegame&a=notice";
	private String customerService = baseUrl + "/index.php?g=api&m=user&a=customer_service";
	private String coinInfo = baseUrl + "/index.php?g=api&m=coin&a=coin_info";
	private String goldCoin = baseUrl + "/index.php?g=api&m=coin&a=log";
	private String myCoin = baseUrl + "/index.php?g=api&m=coin&a=my_coin";
	private String platExchange = baseUrl + "/index.php?g=api&m=platformmoney&a=exchange";
	private String platformmoney = baseUrl + "/index.php?g=api&m=platformmoney&a=log";
	private String signInit = baseUrl + "/index.php?g=api&m=sign&a=sign_init";
	private String doSign = baseUrl + "/index.php?g=api&m=sign&a=do_sign";
	private String rebateNotice = baseUrl + "/index.php?g=api&m=selfRebate&a=rebateNotice";
	private String rebateInfo = baseUrl + "/index.php?g=api&m=selfRebate&a=rebateInfo";
	private String rebateRecord = baseUrl + "/index.php?g=api&m=selfRebate&a=rebateRecord";
	private String rebateApply = baseUrl + "/index.php?g=api&m=selfRebate&a=rebateApply";

	private String rebateKnow = baseUrl + "/index.php?g=api&m=selfRebate&a=rebateKnow";

	private String frendRecom = baseUrl + "/user/register/friend_recom_register.html";

	private String userCenter = baseUrl + "/index.php?g=api&m=userbox&a=user_center";

	private String friendRecomInfo = baseUrl + "/index.php?g=api&m=userbox&a=friend_recom_info";

	private String giveCoin = baseUrl + "/index.php?g=api&m=comment&a=giveCoin";

	private String luckyUrl = baseUrl + "/index.php?g=api&m=luckydraw&a=show";

	private String prizes = baseUrl + "/index.php?g=api&m=luckydraw&a=myPrize";

	private String bindMobile = "http://api.185sy.com/index.php?g=api&m=user&a=bind_mobile";

	private String unBindMobile = "http://api.185sy.com/index.php?g=api&m=user&a=unbind_mobile";

	private String getStartImgs = "http://api.185sy.com/index.php?g=api&m=game&a=getStartImgs";

	private String notice = "http://api.185sy.com/index.php?g=api&m=userbox&a=notice";

	private String getApatch = "http://api.185sy.com/index.php?g=api&m=userbox&a=get_patch";

	private String messageList = "http://api.185sy.com/index.php?g=api&m=message&a=get_message_list";

	private String messageInfo = "http://api.185sy.com/index.php?g=api&m=message&a=get_message_info";

	private String deleteMessage = "http://api.185sy.com/index.php?g=api&m=message&a=delete_message";

	private String getBonus = "http://api.185sy.com/index.php?g=api&m=platformmoney&a=get_reigster_bonus";

	private String unreadCounts = "http://api.185sy.com/index.php?g=api&m=message&a=unread_counts";


	private String getDynamics = "http://api.185sy.com/index.php?g=api&m=dynamics&a=getDynamics";

	private String dynamicsLike = "http://api.185sy.com/index.php?g=api&m=likeinfo&a=dynamics_like";

	private String cancelDynamicsLike = "http://api.185sy.com/index.php?g=api&m=likeinfo&a=cancel_dynamics_like";


	private String publishDynamics = "http://api.185sy.com/index.php?g=api&m=dynamics&a=publishDynamics";

	private String doComment = "http://api.185sy.com/index.php?g=api&m=comment&a=do_comment";

	private String commentList = "http://api.185sy.com/index.php?g=api&m=comment&a=comment_list";

	private String commentLike = "http://api.185sy.com/index.php?g=api&m=likeinfo&a=comment_like";

	private String cancelCommentLike = "http://api.185sy.com/index.php?g=api&m=likeinfo&a=cancel_comment_like";

	private String deleteComment = "http://api.185sy.com/index.php?g=api&m=comment&a=delete_comment";

	private String followList = "http://api.185sy.com/index.php?g=api&m=userbox&a=follow_list";

	private String userDesc = "http://api.185sy.com/index.php?g=api&m=userbox&a=user_desc";

	private String editDesc = "http://api.185sy.com/index.php?g=api&m=userbox&a=edit_desc";

	private String followOrCancel = "http://api.185sy.com/index.php?g=api&m=dynamics&a=followOrCancel";


	private String shareDynamics = "http://api.185sy.com/index.php?g=api&m=dynamics&a=shareDynamics";

	private String shareArticle = "http://api.185sy.com/index.php?g=api&m=Article&a=share";

	private String dynamicsWapInfo = "http://api.185sy.com/api/dynamics/webDisplay.html";

	private String newUpCounts = "http://api.185sy.com/index.php?g=api&m=userbox&a=new_up_counts";

	private String myCommentZan = "http://api.185sy.com/index.php?g=api&m=userbox&a=my_comment_zan";

	private String doInit = "http://api.185sy.com/index.php?g=api&m=userbox&a=do_init";

	private String agreement = "http://api.185sy.com/index.php?g=api&m=userbox&a=agreement";

	private String rankingList = "http://api.185sy.com/index.php?g=api&m=userbox&a=rankingList";

	private String receiveReward = "http://api.185sy.com/index.php?g=api&m=userbox&a=receiveReward";

	private String delDynamic = "http://api.185sy.com/index.php?g=api&m=dynamics&a=delDynamic";

	private String commentCount = "http://api.185sy.com/index.php?g=api&m=comment&a=get_comment_counts";

	private String packInfo = "http://api.185sy.com/index.php?g=api&m=package&a=pack_info";

	private String userRanking = "http://api.185sy.com/index.php?g=api&m=userbox&a=userRanking";

	private String rankNotice = "http://api.185sy.com/index.php?g=api&m=userbox&a=rankNotice";

	private String replayComment = "http://api.185sy.com/index.php?m=comment&a=get_replay_comment";

	private String userLoginApp = "http://api.185sy.com/index.php?g=api&m=comment&a=user_login_app";

	private String reportData = "http://api.185sy.com/index.php?g=api&m=gdtstatic&a=report_data";

	private String jrttReportData = "http://api.185sy.com/index.php?g=api&m=jrttstatic&a=report_data";

	private String newIndex = "http://api.185sy.com/g=api-game-newIndex";

	private String taskCenter = "http://api.185sy.com/index.php?g=api&m=userbox&a=task_center";

	private String appPromise = "http://api.185sy.com/index.php?g=api&m=userbox&a=app_promise";

	private String doInitV2 = "http://api.185sy.com/index.php?g=api&m=userbox&a=do_init_v2";

	private String sendMessage = "http://api.185sy.com/index.php?g=api&m=businessplayer&a=send_message";

	private String tradingRegister = "http://api.185sy.com/index.php?g=api&m=businessplayer&a=register";

	private String tradingLogin = "http://api.185sy.com/index.php?g=api&m=businessplayer&a=login";

	private String tradingModifyPassword = "http://api.185sy.com/index.php?g=api&m=businessplayer&a=modify_password";

	private String tradingForgetPassword = "http://api.185sy.com/index.php?g=api&m=businessplayer&a=forget_password";

	private String productList = "http://api.185sy.com/index.php?g=api&m=Products&a=get_product_list";

	private String sdkuserList = "http://api.185sy.com/index.php?g=api&m=businessplayer&a=sdkuser_list";

	private String unBindSdkUser = "http://api.185sy.com/index.php?g=api&m=businessplayer&a=unbind_sdkuser";

	private String bindSdkUser = "http://api.185sy.com/index.php?g=api&m=businessplayer&a=bind_sdkuser";

	private String sellProduct = "http://api.185sy.com/index.php?g=api&m=Products&a=sell_product";

	private String productInfo = "http://api.185sy.com/index.php?g=api&m=Products&a=product_info";

	private String tradingStartPayment = "http://api.185sy.com/index.php?g=api&m=AccountTrade&a=startPayment";

	private String tradingCancelPayment = "http://api.185sy.com/index.php?g=api&m=AccountTrade&a=cancelPayment";

	private String editUser = "http://api.185sy.com/index.php?g=api&m=businessplayer&a=edit_user";

	private String customer = "http://api.185sy.com/index.php?g=api&m=Products&a=customer";

	private String buyerRecord = "http://api.185sy.com/index.php?g=api&m=AccountTrade&a=buyerRecord";

	private String getProductByUser = "http://api.185sy.com/index.php?g=api&m=Products&a=get_product_by_user";

	private String deleteProduct = "http://api.185sy.com/index.php?g=api&m=Products&a=delete_product";

	private String withdrawProduct = "http://api.185sy.com/index.php?g=api&m=Products&a=withdraw_product";

	private String applyOnsale = "http://api.185sy.com/index.php?g=api&m=Products&a=apply_onsale";

	private String tradeNotesH5 = "http://p.185sy.com/tradenotes.html";

	private String tradeNotes = "http://api.185sy.com/index.php?g=api&m=Products&a=trade_notes";

	private String sellerRecord = "http://api.185sy.com/index.php?g=api&m=AccountTrade&a=sellerRecord";

	private String newgameReserve = "http://www.185sy.com/api-game-newgame_reserve";

	private String reserveSuccess = "http://www.185sy.com/api-game-reserve_success";

	private String sdkLogin = "http://api.185sy.com/index.php?g=api&m=user&a=login";

	private String articleLike = "http://api.185sy.com/index.php?g=api&m=likeinfo&a=article_like";

	private  String doReport="http://api.185sy.com/index.php?g=api&m=ActStatic&a=do_report";

	private  String consultList="http://api.185sy.com/index.php?g=api&m=Consult&a= consultList";

	private  String putQuestion="http://api.185sy.com/index.php?g=api&m=Consult&a=putQuestion";

	private  String consultInfo="http://api.185sy.com/index.php?g=api&m=ConsultInfo&a=info";

	private  String doAnswer="http://api.185sy.com/index.php?g=api&m=ConsultInfo&a=do_answer";

	private  String doReward="http://api.185sy.com/index.php?g=api&m=ConsultInfo&a=do_reward";

	private  String myQuestions="http://api.185sy.com/index.php?g=api&m=Consult&a=myQuestions";

	private  String answerGame="http://api.185sy.com/index.php?g=api&m=ConsultInfo&a=get_answer_game";

	private  String boutiqueGame="http://api.185sy.com/g=api-game-hqGame";

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getGetPacksList() {
		return getPacksList;
	}

	public void setGetPacksList(String getPacksList) {
		this.getPacksList = getPacksList;
	}

	public String getGetPacksByGame() {
		return getPacksByGame;
	}

	public void setGetPacksByGame(String getPacksByGame) {
		this.getPacksByGame = getPacksByGame;
	}

	public String getGetPacksByUser() {
		return getPacksByUser;
	}

	public void setGetPacksByUser(String getPacksByUser) {
		this.getPacksByUser = getPacksByUser;
	}

	public String getGetPack() {
		return getPack;
	}

	public void setGetPack(String getPack) {
		this.getPack = getPack;
	}

	public String getGameIndex() {
		return gameIndex;
	}

	public void setGameIndex(String gameIndex) {
		this.gameIndex = gameIndex;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getOpenServer() {
		return openServer;
	}

	public void setOpenServer(String openServer) {
		this.openServer = openServer;
	}

	public String getGameInfo() {
		return gameInfo;
	}

	public void setGameInfo(String gameInfo) {
		this.gameInfo = gameInfo;
	}

	public String getGameCollect() {
		return gameCollect;
	}

	public void setGameCollect(String gameCollect) {
		this.gameCollect = gameCollect;
	}

	public String getGameClass() {
		return gameClass;
	}

	public void setGameClass(String gameClass) {
		this.gameClass = gameClass;
	}

	public String getGameClassInfo() {
		return gameClassInfo;
	}

	public void setGameClassInfo(String gameClassInfo) {
		this.gameClassInfo = gameClassInfo;
	}

	public String getArticleListByGame() {
		return articleListByGame;
	}

	public void setArticleListByGame(String getArticleListByGame) {
		this.articleListByGame = getArticleListByGame;
	}

	public String getGameOpenServer() {
		return gameOpenServer;
	}

	public void setGameOpenServer(String gameOpenServer) {
		this.gameOpenServer = gameOpenServer;
	}

	public String getArticleList() {
		return articleList;
	}

	public void setArticleList(String articleList) {
		this.articleList = articleList;
	}

	public String getSlide() {
		return getSlide;
	}

	public void setSlide(String getSlide) {
		this.getSlide = getSlide;
	}

	public String getGameAllName() {
		return gameAllName;
	}

	public void setGameAllName(String gameAllName) {
		this.gameAllName = gameAllName;
	}

	public String getGameUpdata() {
		return gameUpdata;
	}

	public void setGameUpdata(String gameUpdata) {
		this.gameUpdata = gameUpdata;
	}

	public String getHotGameSearch() {
		return hotGameSearch;
	}

	public void setHotGameSearch(String hotGameSearch) {
		this.hotGameSearch = hotGameSearch;
	}

	public String getGameSearchList() {
		return gameSearchList;
	}

	public void setGameSearchList(String gameSearchList) {
		this.gameSearchList = gameSearchList;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getRegister() {
		return register;
	}

	public void setRegister(String register) {
		this.register = register;
	}

	public String getSendCode() {
		return sendCode;
	}

	public void setSendCode(String sendCode) {
		this.sendCode = sendCode;
	}

	public String getGetSlide() {
		return getSlide;
	}

	public void setGetSlide(String getSlide) {
		this.getSlide = getSlide;
	}

	public String getCheckSmscode() {
		return checkSmscode;
	}

	public void setCheckSmscode(String checkSmscode) {
		this.checkSmscode = checkSmscode;
	}

	public String getForgetPassword() {
		return forgetPassword;
	}

	public void setForgetPassword(String forgetPassword) {
		this.forgetPassword = forgetPassword;
	}

	public String getModifyPassword() {
		return modifyPassword;
	}

	public void setModifyPassword(String modifyPassword) {
		this.modifyPassword = modifyPassword;
	}

	public String getChannelDownload() {
		return channelDownload;
	}

	public void setChannelDownload(String channelDownload) {
		this.channelDownload = channelDownload;
	}

	public String getMyCollect() {
		return myCollect;
	}

	public void setMyCollect(String myCollect) {
		this.myCollect = myCollect;
	}

	public String getModifyNicename() {
		return modifyNicename;
	}

	public void setModifyNicename(String modifyNicename) {
		this.modifyNicename = modifyNicename;
	}

	public String getUploadPortrait() {
		return uploadPortrait;
	}

	public void setUploadPortrait(String uploadPortrait) {
		this.uploadPortrait = uploadPortrait;
	}

	public String getGameGrade() {
		return gameGrade;
	}

	public void setGameGrade(String gameGrade) {
		this.gameGrade = gameGrade;
	}

	public String getGameInstall() {
		return gameInstall;
	}

	public void setGameInstall(String gameInstall) {
		this.gameInstall = gameInstall;
	}

	public String getGameUninstall() {
		return gameUninstall;
	}

	public void setGameUninstall(String gameUninstall) {
		this.gameUninstall = gameUninstall;
	}

	public String getCheckApp() {
		return checkApp;
	}

	public void setCheckApp(String checkApp) {
		this.checkApp = checkApp;
	}

	public String getDownloadingTimes() {
		return downloadingTimes;
	}

	public void setDownloadingTimes(String downloadingTimes) {
		this.downloadingTimes = downloadingTimes;
	}

	public String getPlatformmoney() {
		return platformmoney;
	}

	public void setPlatformmoney(String platformmoney) {
		this.platformmoney = platformmoney;
	}

	public String getPayStart() {
		return payStart;
	}

	public void setPayStart(String payStart) {
		this.payStart = payStart;
	}

	public String getPayReady() {
		return payReady;
	}

	public void setPayReady(String payReady) {
		this.payReady = payReady;
	}

	public String getPayQuery() {
		return payQuery;
	}

	public void setPayQuery(String payQuery) {
		this.payQuery = payQuery;
	}

	public String getGoldCoin() {
		return goldCoin;
	}

	public void setGoldCoin(String goldCoin) {
		this.goldCoin = goldCoin;
	}

	public String getChangegameApply() {
		return changegameApply;
	}

	public void setChangegameApply(String changegameApply) {
		this.changegameApply = changegameApply;
	}

	public String getChangegameLog() {
		return changegameLog;
	}

	public void setChangegameLog(String changegameLog) {
		this.changegameLog = changegameLog;
	}

	public String getCustomerService() {
		return customerService;
	}

	public void setCustomerService(String customerService) {
		this.customerService = customerService;
	}

	public String getCoinInfo() {
		return coinInfo;
	}

	public void setCoinInfo(String coinInfo) {
		this.coinInfo = coinInfo;
	}

	public String getPlatExchange() {
		return platExchange;
	}

	public void setPlatExchange(String platExchange) {
		this.platExchange = platExchange;
	}

	public String getSignInit() {
		return signInit;
	}

	public void setSignInit(String signInit) {
		this.signInit = signInit;
	}

	public String getDoSign() {
		return doSign;
	}

	public void setDoSign(String doSign) {
		this.doSign = doSign;
	}

	public String getFrendRecom() {
		return frendRecom;
	}

	public void setFrendRecom(String frendRecom) {
		this.frendRecom = frendRecom;
	}

	public String getMyCoin() {
		return myCoin;
	}

	public void setMyCoin(String myCoin) {
		this.myCoin = myCoin;
	}

	public String getRebateNotice() {
		return rebateNotice;
	}

	public void setRebateNotice(String rebateNotice) {
		this.rebateNotice = rebateNotice;
	}

	public String getRebateInfo() {
		return rebateInfo;
	}

	public void setRebateInfo(String rebateInfo) {
		this.rebateInfo = rebateInfo;
	}

	public String getRebateRecord() {
		return rebateRecord;
	}

	public void setRebateRecord(String rebateRecord) {
		this.rebateRecord = rebateRecord;
	}

	public String getRebateApply() {
		return rebateApply;
	}

	public void setRebateApply(String rebateApply) {
		this.rebateApply = rebateApply;
	}

	public String getRebateKnow() {
		return rebateKnow;
	}

	public void setRebateKnow(String rebateKnow) {
		this.rebateKnow = rebateKnow;
	}

	public String getChangegameKnow() {
		return changegameKnow;
	}

	public void setChangegameKnow(String changegameKnow) {
		this.changegameKnow = changegameKnow;
	}

	public String getUserCenter() {
		return userCenter;
	}

	public void setUserCenter(String userCenter) {
		this.userCenter = userCenter;
	}

	public String getFriendRecomInfo() {
		return friendRecomInfo;
	}

	public void setFriendRecomInfo(String friendRecomInfo) {
		this.friendRecomInfo = friendRecomInfo;
	}

	public String getGetVipOption() {
		return getVipOption;
	}

	public void setGetVipOption(String getVipOption) {
		this.getVipOption = getVipOption;
	}

	public String getGiveCoin() {
		return giveCoin;
	}

	public void setGiveCoin(String giveCoin) {
		this.giveCoin = giveCoin;
	}

	public String getLuckyUrl() {
		return luckyUrl;
	}

	public void setLuckyUrl(String luckyUrl) {
		this.luckyUrl = luckyUrl;
	}

	public String getPrizes() {
		return prizes;
	}

	public void setPrizes(String prizes) {
		this.prizes = prizes;
	}

	public String getBindMobile() {
		return bindMobile;
	}

	public void setBindMobile(String bindMobile) {
		this.bindMobile = bindMobile;
	}

	public String getUnBindMobile() {
		return unBindMobile;
	}

	public void setUnBindMobile(String unBindMobile) {
		this.unBindMobile = unBindMobile;
	}

	public String getGetStartImgs() {
		return getStartImgs;
	}

	public void setGetStartImgs(String getStartImgs) {
		this.getStartImgs = getStartImgs;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public String getGetApatch() {
		return getApatch;
	}

	public void setGetApatch(String getApatch) {
		this.getApatch = getApatch;
	}

	public String getBoxInstallInfo() {
		return boxInstallInfo;
	}

	public void setBoxInstallInfo(String boxInstallInfo) {
		this.boxInstallInfo = boxInstallInfo;
	}

	public String getBoxStartInfo() {
		return boxStartInfo;
	}

	public void setBoxStartInfo(String boxStartInfo) {
		this.boxStartInfo = boxStartInfo;
	}

	public String getMessageList() {
		return messageList;
	}

	public void setMessageList(String messageList) {
		this.messageList = messageList;
	}

	public String getMessageInfo() {
		return messageInfo;
	}

	public void setMessageInfo(String messageInfo) {
		this.messageInfo = messageInfo;
	}

	public String getDeleteMessage() {
		return deleteMessage;
	}

	public void setDeleteMessage(String deleteMessage) {
		this.deleteMessage = deleteMessage;
	}

	public String getGetBonus() {
		return getBonus;
	}

	public void setGetBonus(String getBonus) {
		this.getBonus = getBonus;
	}

	public String getUnreadCounts() {
		return unreadCounts;
	}

	public void setUnreadCounts(String unreadCounts) {
		this.unreadCounts = unreadCounts;
	}

	public String getGetDynamics() {
		return getDynamics;
	}

	public void setGetDynamics(String getDynamics) {
		this.getDynamics = getDynamics;
	}

	public String getDynamicsLike() {
		return dynamicsLike;
	}

	public void setDynamicsLike(String dynamicsLike) {
		this.dynamicsLike = dynamicsLike;
	}

	public String getPublishDynamics() {
		return publishDynamics;
	}

	public void setPublishDynamics(String publishDynamics) {
		this.publishDynamics = publishDynamics;
	}

	public String getDoComment() {
		return doComment;
	}

	public void setDoComment(String doComment) {
		this.doComment = doComment;
	}

	public String getCommentList() {
		return commentList;
	}

	public void setCommentList(String commentList) {
		this.commentList = commentList;
	}

	public String getCommentLike() {
		return commentLike;
	}

	public void setCommentLike(String commentLike) {
		this.commentLike = commentLike;
	}

	public String getFollowList() {
		return followList;
	}

	public void setFollowList(String followList) {
		this.followList = followList;
	}

	public String getUserDesc() {
		return userDesc;
	}

	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}

	public String getEditDesc() {
		return editDesc;
	}

	public void setEditDesc(String editDesc) {
		this.editDesc = editDesc;
	}

	public String getFollowOrCancel() {
		return followOrCancel;
	}

	public void setFollowOrCancel(String followOrCancel) {
		this.followOrCancel = followOrCancel;
	}

	public String getDeleteComment() {
		return deleteComment;
	}

	public void setDeleteComment(String deleteComment) {
		this.deleteComment = deleteComment;
	}

	public String getShareDynamics() {
		return shareDynamics;
	}

	public void setShareDynamics(String shareDynamics) {
		this.shareDynamics = shareDynamics;
	}

	public String getDynamicsWapInfo() {
		return dynamicsWapInfo;
	}

	public void setDynamicsWapInfo(String dynamicsWapInfo) {
		this.dynamicsWapInfo = dynamicsWapInfo;
	}

	public String getNewUpCounts() {
		return newUpCounts;
	}

	public void setNewUpCounts(String newUpCounts) {
		this.newUpCounts = newUpCounts;
	}

	public String getMyCommentZan() {
		return myCommentZan;
	}

	public void setMyCommentZan(String myCommentZan) {
		this.myCommentZan = myCommentZan;
	}

	public String getDoInit() {
		return doInit;
	}

	public void setDoInit(String doInit) {
		this.doInit = doInit;
	}


	public String getCancelDynamicsLike() {
		return cancelDynamicsLike;
	}

	public void setCancelDynamicsLike(String cancelDynamicsLike) {
		this.cancelDynamicsLike = cancelDynamicsLike;
	}

	public String getCancelCommentLike() {
		return cancelCommentLike;
	}

	public void setCancelCommentLike(String cancelCommentLike) {
		this.cancelCommentLike = cancelCommentLike;
	}

	public String getAgreement() {
		return agreement;
	}

	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}

	public void setRankingList(String rankingList) {
		this.rankingList = rankingList;
	}

	public String getRankingList() {
		return rankingList;
	}

	public void setReceiveReward(String receiveReward) {
		this.receiveReward = receiveReward;
	}

	public String getReceiveReward() {
		return receiveReward;
	}

	public String getDelDynamic() {
		return delDynamic;
	}

	public void setDelDynamic(String delDynamic) {
		this.delDynamic = delDynamic;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

	public String getPackInfo() {
		return packInfo;
	}

	public void setPackInfo(String packInfo) {
		this.packInfo = packInfo;
	}

	public String getUserRanking() {
		return userRanking;
	}

	public void setUserRanking(String userRanking) {
		this.userRanking = userRanking;
	}

	public String getRankNotice() {
		return rankNotice;
	}

	public void setRankNotice(String rankNotice) {
		this.rankNotice = rankNotice;
	}

	public String getReplayComment() {
		return replayComment;
	}

	public void setReplayComment(String replayComment) {
		this.replayComment = replayComment;
	}

	public String getUserLoginApp() {
		return userLoginApp;
	}

	public void setUserLoginApp(String userLoginApp) {
		this.userLoginApp = userLoginApp;
	}

	public String getReportData() {
		return reportData;
	}

	public void setReportData(String reportData) {
		this.reportData = reportData;
	}

	public String getJrttReportData() {
		return jrttReportData;
	}

	public void setJrttReportData(String jrttReportData) {
		this.jrttReportData = jrttReportData;
	}


	public String getNewIndex() {
		return newIndex;
	}

	public void setNewIndex(String newIndex) {
		this.newIndex = newIndex;
	}

	public String getTaskCenter() {
		return taskCenter;
	}

	public void setTaskCenter(String taskCenter) {
		this.taskCenter = taskCenter;
	}

	public String getAppPromise() {
		return appPromise;
	}

	public void setAppPromise(String appPromise) {
		this.appPromise = appPromise;
	}


	public String getDoInitV2() {
		return doInitV2;
	}

	public void setDoInitV2(String doInitV2) {
		this.doInitV2 = doInitV2;
	}

	public String getNewGameType() {
		return newGameType;
	}

	public void setNewGameType(String newGameType) {
		this.newGameType = newGameType;
	}

	public String getNewGameList() {
		return newGameList;
	}

	public void setNewGameList(String newGameList) {
		this.newGameList = newGameList;
	}

	public String getSendMessage() {
		return sendMessage;
	}

	public void setSendMessage(String sendMessage) {
		this.sendMessage = sendMessage;
	}

	public String getTradingRegister() {
		return tradingRegister;
	}

	public void setTradingRegister(String tradingRegister) {
		this.tradingRegister = tradingRegister;
	}

	public String getTradingLogin() {
		return tradingLogin;
	}

	public void setTradingLogin(String tradingLogin) {
		this.tradingLogin = tradingLogin;
	}

	public String getTradingModifyPassword() {
		return tradingModifyPassword;
	}

	public void setTradingModifyPassword(String tradingModifyPassword) {
		this.tradingModifyPassword = tradingModifyPassword;
	}

	public String getTradingForgetPassword() {
		return tradingForgetPassword;
	}

	public void setTradingForgetPassword(String tradingForgetPassword) {
		this.tradingForgetPassword = tradingForgetPassword;
	}

	public String getProductList() {
		return productList;
	}

	public void setProductList(String productList) {
		this.productList = productList;
	}

	public String getSdkuserList() {
		return sdkuserList;
	}

	public void setSdkuserList(String sdkuserList) {
		this.sdkuserList = sdkuserList;
	}

	public String getUnBindSdkUser() {
		return unBindSdkUser;
	}

	public void setUnBindSdkUser(String unBindSdkUser) {
		this.unBindSdkUser = unBindSdkUser;
	}

	public String getBindSdkUser() {
		return bindSdkUser;
	}

	public void setBindSdkUser(String bindSdkUser) {
		this.bindSdkUser = bindSdkUser;
	}

	public String getSellProduct() {
		return sellProduct;
	}

	public void setSellProduct(String sellProduct) {
		this.sellProduct = sellProduct;
	}

	public String getProductInfo() {
		return productInfo;
	}

	public void setProductInfo(String productInfo) {
		this.productInfo = productInfo;
	}

	public String getTradingStartPayment() {
		return tradingStartPayment;
	}

	public void setTradingStartPayment(String tradingStartPayment) {
		this.tradingStartPayment = tradingStartPayment;
	}

	public String getTradingCancelPayment() {
		return tradingCancelPayment;
	}

	public void setTradingCancelPayment(String tradingCancelPayment) {
		this.tradingCancelPayment = tradingCancelPayment;
	}

	public String getEditUser() {
		return editUser;
	}

	public void setEditUser(String editUser) {
		this.editUser = editUser;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getBuyerRecord() {
		return buyerRecord;
	}

	public void setBuyerRecord(String buyerRecord) {
		this.buyerRecord = buyerRecord;
	}

	public String getGetProductByUser() {
		return getProductByUser;
	}

	public void setGetProductByUser(String getProductByUser) {
		this.getProductByUser = getProductByUser;
	}

	public String getDeleteProduct() {
		return deleteProduct;
	}

	public void setDeleteProduct(String deleteProduct) {
		this.deleteProduct = deleteProduct;
	}

	public String getWithdrawProduct() {
		return withdrawProduct;
	}

	public void setWithdrawProduct(String withdrawProduct) {
		this.withdrawProduct = withdrawProduct;
	}

	public String getApplyOnsale() {
		return applyOnsale;
	}

	public void setApplyOnsale(String applyOnsale) {
		this.applyOnsale = applyOnsale;
	}

	public String getTradeNotesH5() {
		return tradeNotesH5;
	}

	public void setTradeNotesH5(String tradeNotesH5) {
		this.tradeNotesH5 = tradeNotesH5;
	}

	public String getTradeNotes() {
		return tradeNotes;
	}

	public void setTradeNotes(String tradeNotes) {
		this.tradeNotes = tradeNotes;
	}

	public String getExclusiveList() {
		return exclusiveList;
	}

	public void setExclusiveList(String exclusiveList) {
		this.exclusiveList = exclusiveList;
	}

	public String getSellerRecord() {
		return sellerRecord;
	}

	public void setSellerRecord(String sellerRecord) {
		this.sellerRecord = sellerRecord;
	}

	public String getNewgameReserve() {
		return newgameReserve;
	}

	public void setNewgameReserve(String newgameReserve) {
		this.newgameReserve = newgameReserve;
	}

	public String getReserveSuccess() {
		return reserveSuccess;
	}

	public void setReserveSuccess(String reserveSuccess) {
		this.reserveSuccess = reserveSuccess;
	}

	public String getSdkLogin() {
		return sdkLogin;
	}

	public void setSdkLogin(String sdkLogin) {
		this.sdkLogin = sdkLogin;
	}

	public String getArticleLike() {
		return articleLike;
	}

	public void setArticleLike(String articleLike) {
		this.articleLike = articleLike;
	}

	public String getShareArticle() {
		return shareArticle;
	}

	public void setShareArticle(String shareArticle) {
		this.shareArticle = shareArticle;
	}

	public String getDoReport() {
		return doReport;
	}

	public void setDoReport(String doReport) {
		this.doReport = doReport;
	}

	public String getConsultList() {
		return consultList;
	}

	public void setConsultList(String consultList) {
		this.consultList = consultList;
	}

	public String getPutQuestion() {
		return putQuestion;
	}

	public void setPutQuestion(String putQuestion) {
		this.putQuestion = putQuestion;
	}

	public String getConsultInfo() {
		return consultInfo;
	}

	public void setConsultInfo(String consultInfo) {
		this.consultInfo = consultInfo;
	}

	public String getDoAnswer() {
		return doAnswer;
	}

	public void setDoAnswer(String doAnswer) {
		this.doAnswer = doAnswer;
	}

	public String getDoReward() {
		return doReward;
	}

	public void setDoReward(String doReward) {
		this.doReward = doReward;
	}

	public String getMyQuestions() {
		return myQuestions;
	}

	public void setMyQuestions(String myQuestions) {
		this.myQuestions = myQuestions;
	}

	public String getAnswerGame() {
		return answerGame;
	}

	public void setAnswerGame(String answerGame) {
		this.answerGame = answerGame;
	}

	public String getBoutiqueGame() {
		return boutiqueGame;
	}

	public void setBoutiqueGame(String boutiqueGame) {
		this.boutiqueGame = boutiqueGame;
	}
}
