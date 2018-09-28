
package com.tenone.gamebox.mode.biz;

import android.content.Intent;

import com.tenone.gamebox.mode.able.WebAble;

public class WebBiz implements WebAble {

	@Override
	public String getUrl(Intent intent) {
		return intent.getExtras().getString("url");
	}

	@Override
	public String getTitle(Intent intent) {
		return intent.getExtras().getString("title");
	}

}
