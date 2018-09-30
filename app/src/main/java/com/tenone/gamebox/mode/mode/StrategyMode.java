/**
 * Project Name:GameBox
 * File Name:StrategyMode.java
 * Package Name:com.tenone.gamebox.mode.mode
 * Date:2017-3-29����3:07:33
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

public class StrategyMode implements Serializable {
	private static final long serialVersionUID = -6130357769457137485L;
	/*����ʹ�����ID*/
	private String articleId;
	/* id */
	private int strategyId;
	/* ���� */
	private String strategyName;
	/* ��Ϸ���� */
	private String gameName;
	/* ͼƬ��ַ */
	private String strategyImgUrl;
	/* ���� */
	private String writer;
	/* ʱ�� */
	private String time;
	/*�����ַ*/
	private String url;
	/*�Ķ�����*/
	private int viewCounts;
	/*������*/
	private int likes;
	/*������*/
	private int disLikes;
	/*�Ƿ��ö� 1 �� 0 ����*/
	private boolean isTop;
	/*�û��Ƿ��޲� 0 �Ѳ� 1 ���� 2 δ�޲�*/
	private int likeType;
	/*������*/
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
