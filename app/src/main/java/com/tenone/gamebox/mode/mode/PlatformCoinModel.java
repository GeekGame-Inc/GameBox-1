package com.tenone.gamebox.mode.mode;

import android.text.TextUtils;

import java.io.Serializable;

public class PlatformCoinModel implements Serializable {
	private static final long serialVersionUID = 4320755710511973783L;
	private String time = "";
	private String type = "";
	private String num = "";
	private String counts = "";
	private boolean isGold = false;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getType() {
		String t = "";
		if (isGold) {
			switch (type) {
				case "1":
					t = "\u7b7e\u5230";
					break;
				case "2":
					t = "\u8bc4\u8bba";
					break;
				case "3":
					t = "\u597d\u53cb\u63a8\u8350";
					break;
				case "4":
					t = "\u91d1\u5e01\u5151\u6362";
					break;
				case "5":
					t = "\u91d1\u5e01\u62bd\u5956";
					break;
				case "6":
					t = "\u540e\u53f0\u8865\u53d1";
					break;
				case "7":
					t = "\u793e\u533a\u8bc4\u8bba\u5956\u52b1";
					break;
				case "8":
					t = "\u793e\u533a\u70b9\u8d5e\u5956\u52b1";
					break;
				case "9":
					t = "\u793e\u533a\u5f00\u8f66\u5956\u52b1";
					break;
				case "10":
					t = "\u9080\u8bf7\u6392\u884c\u5956\u52b1";
					break;
				case "11":
					t = "\u63d0\u95ee\u60ac\u8d4f";
					break;
				case "12":
					t = "\u83b7\u5f97\u60ac\u8d4f";
					break;
				case "13":
					t = "\u63d0\u95ee\u524d10\u56de\u7b54\u5956\u52b1";
					break;
				case "14":
					t = "\u6d88\u606f\u9644\u4ef6";
					break;
			}
		} else {
			switch (type) {
				case "1":
					t = "\u91d1\u5e01\u5151\u6362";
					break;
				case "2":
					t = "\u6e38\u620f\u652f\u4ed8";
					break;
				case "3":
					t = "\u91d1\u5e01\u62bd\u5956";
					break;
				case "4":
					t = "\u540e\u53f0\u8865\u53d1";
					break;
				case "5":
					t = "vip\u5145\u503c";
					break;

				case "6":
					t = "\u624b\u673a\u6ce8\u518c";
					break;
			}
		}
		return t;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNum() {
		if (TextUtils.isEmpty( num )) {
			num = "0";
		} else {
			try {
				int a = Integer.valueOf( num ).intValue();
				if (a > 0) {
					num = "+" + a;
				}
			} catch (NumberFormatException e) {
			}
		}
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getCounts() {
		return counts;
	}

	public void setCounts(String counts) {
		this.counts = counts;
	}

	public boolean isGold() {
		return isGold;
	}

	public void setGold(boolean isGold) {
		this.isGold = isGold;
	}
}
