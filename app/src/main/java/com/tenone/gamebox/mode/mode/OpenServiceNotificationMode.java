/** 
 * Project Name:GameBox 
 * File Name:OpenServiceNotificationMode.java 
 * Package Name:com.tenone.gamebox.mode.mode 
 * Date:2017-4-13����10:17:40 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

public class OpenServiceNotificationMode implements Serializable {
	private static final long serialVersionUID = -8266965314232550412L;
	private String gameName;
	private String gameId;
	private String imgUrl;
	private String gameVersions;
	private String serviceId;
	private String time;

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getGameVersions() {
		return gameVersions;
	}

	public void setGameVersions(String gameVersions) {
		this.gameVersions = gameVersions;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
