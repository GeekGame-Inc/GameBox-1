package com.tenone.gamebox.mode.biz;

import android.content.Context;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.able.SignInAble;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.ArrayList;
import java.util.List;

public class SignInBiz implements SignInAble {

	@Override
	public String getStr1(Context cxt, ResultItem item) {
		int counts = Integer.valueOf(item.getString("sign_counts"));
		List<ResultItem> list = item.getItems("accum_bonus");
		List<Integer> integers = new ArrayList<Integer>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				ResultItem resultItem = list.get(i);
				String tiem = resultItem.getString("num");
				integers.add(Integer.valueOf(tiem));
			}
		}
		int days = 1;
		for (int i = 0; i < integers.size(); i++) {
			int day = integers.get(i).intValue();
			if (day > counts) {
				days = day - counts;
				break;
			}
			if (day == counts) {
				if (i < (integers.size() - 1)) {
					days = integers.get(i + 1) - counts;
				}
				break;
			}
		}
		String str = "";
		if (counts >= integers.get((integers.size() - 1))) {
			str = cxt.getResources().getString(R.string.sing_in_title4,
					counts + "" );
		} else {
			str = cxt.getResources().getString(R.string.sing_in_title1,
					counts + "", days + "" );
		}
		return str;
	}

	@Override
	public String getStr2(Context cxt, ResultItem item) {
		ResultItem resultItem = item.getItem("day_bonus");
		String str1 = resultItem.getString("normal");
		String str2 = resultItem.getString("vip_extra");
		return "普通用户每日签到获取" + str1 + "金币，vip用户额外获得" + str2 + "金币。";
	}

	@Override
	public String getStr3(Context cxt, ResultItem item) {
		StringBuilder builder = new StringBuilder();
		List<ResultItem> list = item.getItems("accum_bonus");
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				ResultItem resultItem = list.get(i);
				String tiem = resultItem.getString("num");
				String bonus = resultItem.getString("bonus");
				if (i < list.size() - 1) {
					builder.append( "\u7d2f\u8ba1\u7b7e\u5230" + tiem + "\u5929,\u989d\u5916\u83b7\u53d6" + bonus + "\u91d1\u5e01," );
				} else {
					builder.append( "\u672c\u6708\u5168\u90e8\u7b7e\u5230\uff0c\u989d\u5916\u83b7\u53d6" + bonus + "\u91d1\u5e01\u3002" );
				}
			}
		}
		return builder.toString();
	}
}
