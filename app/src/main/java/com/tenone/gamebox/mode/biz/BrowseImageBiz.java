
package com.tenone.gamebox.mode.biz;

import android.content.Intent;

import com.tenone.gamebox.mode.able.BrowseImageAble;

import java.util.ArrayList;
import java.util.List;

public class BrowseImageBiz implements BrowseImageAble {

	@Override
	public List<String> getImageUrls(Intent intent) {
		List<String> list = new ArrayList<String>();
		List<String> array = intent.getExtras().getStringArrayList("urls");
		if (array != null) {
			list.addAll(array);
		}
		return list;
	}

	@Override
	public String getCurrentUrl(Intent intent) {
		String url = intent.getExtras().getString("url");
		return url;
	}
}
