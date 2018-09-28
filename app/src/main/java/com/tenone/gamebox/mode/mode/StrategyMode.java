/**
 * Project Name:GameBox
 * File Name:StrategyMode.java
 * Package Name:com.tenone.gamebox.mode.mode
 * Date:2017-3-29下午3:07:33
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

/**
 * 攻略数据模型 ClassName:StrategyMode <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-29 下午3:07:33 <br/>
 *
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class StrategyMode implements Serializable {
	private static final long serialVersionUID = -6130357769457137485L;
	/*踩赞使用这个ID*/
	private String articleId;
	/* id */
	private int strategyId;
	/* 名称 */
	private String strategyName;
	/* 游戏名称 */
	private String gameName;
	/* 图片地址 */
	private String strategyImgUrl;
	/* 作者 */
	private String writer;
	/* 时间 */
	private String time;
	/*详情地址*/
	private String url;
	/*阅读数量*/
	private int viewCounts;
	/*赞数量*/
	private int likes;
	/*踩数量*/
	private int disLikes;
	/*是否置顶 1 是 0 不是*/
	private boolean isTop;
	/*用户是否赞踩 0 已踩 1 已赞 2 未赞踩*/
	private int likeType;
	/*分享数*/
	private int shareCounts;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStrategyId() {
		return strategyId;
	}

	public void setStrategyId(int strategyId) {
		this.strategyId = strategyId;
	}

	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}


	public String getStrategyImgUrl() {
		return strategyImgUrl;
	}

	public void setStrategyImgUrl(String strategyImgUrl) {
		this.strategyImgUrl = strategyImgUrl;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public int getViewCounts() {
		return viewCounts;
	}

	public void setViewCounts(int viewCounts) {
		this.viewCounts = viewCounts;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getDisLikes() {
		return disLikes;
	}

	public void setDisLikes(int disLikes) {
		this.disLikes = disLikes;
	}

	public boolean isTop() {
		return isTop;
	}

	public void setTop(boolean top) {
		isTop = top;
	}

	public int getLikeType() {
		return likeType;
	}

	public void setLikeType(int likeType) {
		this.likeType = likeType;
	}

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public int getShareCounts() {
		return shareCounts;
	}

	public void setShareCounts(int shareCounts) {
		this.shareCounts = shareCounts;
	}
}
