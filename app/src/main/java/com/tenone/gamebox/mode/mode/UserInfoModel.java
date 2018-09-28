package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

public class UserInfoModel implements Serializable {

	private String userId;
	private String nick;
	private String header;
	private boolean isVip;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public boolean isVip() {
		return isVip;
	}

	public void setVip(boolean vip) {
		isVip = vip;
	}
}
