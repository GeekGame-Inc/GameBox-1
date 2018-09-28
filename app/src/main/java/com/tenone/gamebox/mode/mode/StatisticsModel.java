package com.tenone.gamebox.mode.mode;

import org.json.JSONObject;
/**
 * 统计数据模型
 */
public class StatisticsModel extends JSONObject {
	private String action = "";
	private String time = "";

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
