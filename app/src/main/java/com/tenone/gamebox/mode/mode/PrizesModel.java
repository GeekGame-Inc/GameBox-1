package com.tenone.gamebox.mode.mode;

import android.text.TextUtils;

import com.tenone.gamebox.view.utils.TimeUtils;

import java.io.Serializable;

public class PrizesModel implements Serializable {

	private static final long serialVersionUID = 4866296210613030573L;

	private String time;
	private String name;

	public String getTime() {
		if (!TextUtils.isEmpty(time)) {
			try {
				long t = Long.valueOf(time).longValue() * 1000;
				time = TimeUtils.formatDateSec(t);
			} catch (NumberFormatException e) {
			}
		}
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
