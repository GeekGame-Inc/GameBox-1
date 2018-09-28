/** 
 * Project Name:GameBox 
 * File Name:OpenServerMode.java 
 * Package Name:com.tenone.gamebox.mode.mode 
 * Date:2017-3-24����6:22:41 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

/**
 * ����������ģ�� ClassName:OpenServerMode <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-24 ����6:22:41 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class OpenServerMode implements Serializable {
	private static final long serialVersionUID = -8307533831501438150L;
	/* ������id */
	int serviceId;
	/* ͼƬ��ַ */
	String imgUrl;
	/* ��Ϸ���� */
	String gameName;
	/* ���������� */
	String serviceName;
	/* ��Ϸ��С */
	String gameSize;
	/* ����ʱ�� */
	String openTime;
	/* ����״̬(ͬ��Ϸ����״̬) */
	int downStatus;
	/* �Ƿ�֪ͨ */
	boolean isNotification;
	/* �Ƿ񿪷� */
	boolean isOpen;
	/* ��Ϸ��ǩ */
	String[] labelArray;
	/* ��Ϸ���� */
	String packgeName;
	/* ���ص�ַ */
	String downloadUrl;
	/* ��Ϸ�汾 */
	String gameVersions;
	String gameId;

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameSize() {
		return gameSize;
	}

	public void setGameSize(String gameSize) {
		this.gameSize = gameSize;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public int getDownStatus() {
		return downStatus;
	}

	public void setDownStatus(int downStatus) {
		this.downStatus = downStatus;
	}

	public boolean isNotification() {
		return isNotification;
	}

	public void setNotification(boolean isNotification) {
		this.isNotification = isNotification;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String[] getLabelArray() {
		return labelArray;
	}

	public void setLabelArray(String[] labelArray) {
		this.labelArray = labelArray;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String getPackgeName() {
		return packgeName;
	}

	public void setPackgeName(String packgeName) {
		this.packgeName = packgeName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getGameVersions() {
		return gameVersions;
	}

	public void setGameVersions(String gameVersions) {
		this.gameVersions = gameVersions;
	}

}
