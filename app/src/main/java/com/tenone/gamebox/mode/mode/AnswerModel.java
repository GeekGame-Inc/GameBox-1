package com.tenone.gamebox.mode.mode;

import com.tenone.gamebox.view.utils.TimeUtils;

import java.io.Serializable;

import cz.msebera.android.httpclient.util.TextUtils;

public class AnswerModel implements Serializable {
	private String answer;

	private int id;

	private UserInfoModel userInfoModel;

	private boolean isTop;

	private boolean isBest;

	private int coin;

	private String time;

	private boolean isTaskBonus;

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserInfoModel getUserInfoModel() {
		return userInfoModel;
	}

	public void setUserInfoModel(UserInfoModel userInfoModel) {
		this.userInfoModel = userInfoModel;
	}

	public boolean isTop() {
		return isTop;
	}

	public void setTop(boolean top) {
		isTop = top;
	}

	public boolean isBest() {
		return isBest;
	}

	public void setBest(boolean best) {
		isBest = best;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public String getTime() {
		if (!TextUtils.isEmpty( time )) {
			try {
				long t = Long.valueOf( time ).longValue() * 1000;
				return TimeUtils.formatData( t, "yy-MM-dd" );
			} catch (NumberFormatException e) {
				return time;
			}
		}
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean isTaskBonus() {
		return isTaskBonus;
	}

	public void setTaskBonus(boolean taskBonus) {
		isTaskBonus = taskBonus;
	}
}
