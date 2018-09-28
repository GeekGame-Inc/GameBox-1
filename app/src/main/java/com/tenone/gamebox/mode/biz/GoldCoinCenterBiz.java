package com.tenone.gamebox.mode.biz;

import com.tenone.gamebox.mode.able.GoldCoinCenterAble;
import com.tenone.gamebox.mode.mode.ResultItem;

public class GoldCoinCenterBiz implements GoldCoinCenterAble {

	@Override
	public String getBanlance(ResultItem resultItem) {
		String banlance = resultItem.getString("user_counts");
		return banlance;
	}

	@Override
	public String getToday(ResultItem resultItem) {
		return resultItem.getString("today_coin");
	}

	@Override
	public String getToMonth(ResultItem resultItem) {
		return resultItem.getString("month_coin");
	}
}
