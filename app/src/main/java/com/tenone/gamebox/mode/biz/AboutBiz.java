
package com.tenone.gamebox.mode.biz;

import android.content.Context;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.able.AboutAble;
import com.tenone.gamebox.view.utils.ApkUtils;
public class AboutBiz implements AboutAble {

	@Override
	public String getVersion(Context context) {
		return context.getString( R.string.curren_version) + ApkUtils.getVersionName(context);
	}

	@Override
	public String getWeb() {
		return "\u5b98\u7f51\u5730\u5740 : www.185sy.com";
	}

	@Override
	public String getWebo() {
		return "\u5b98\u65b9\u5fae\u535a : 185sy@163.com";
	}

	@Override
	public String getWeChat() {
		return "\u5fae\u4fe1\u516c\u4f17\u53f7 : 185sy@163.com";
	}
}
