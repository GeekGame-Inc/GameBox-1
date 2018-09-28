/** 
 * Project Name:GameBox 
 * File Name:GiftSearchResultBiz.java 
 * Package Name:com.tenone.gamebox.mode.biz 
 * Date:2017-3-28����2:29:30 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.biz;

import android.content.Intent;
import android.text.TextUtils;

import com.tenone.gamebox.mode.able.GiftSearchResultAble;
import com.tenone.gamebox.mode.mode.GiftMode;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

public class GiftSearchResultBiz implements GiftSearchResultAble {

	@Override
	public String gameName(Intent intent) {
		return intent.getExtras().getString("condition");
	}

	@Override
	public List<GiftMode> getGiftModes(ResultItem resultItem) {
		List<GiftMode> items = new ArrayList<GiftMode>();
		List<ResultItem> resultItems = resultItem.getItems("list");
		for (ResultItem data : resultItems) {
			GiftMode mode = new GiftMode();
			String id = data.getString("id");
			if (!TextUtils.isEmpty(id)) {
				mode.setGiftId(Integer.valueOf(id));
			}
			mode.setGiftImgUrl(MyApplication.getHttpUrl().getBaseUrl()
					+ data.getString("pack_logo"));
			mode.setGiftCounts(Long.valueOf(data.getString("pack_counts")));
			mode.setGiftName(data.getString("pack_name"));
			String counts = data.getString("pack_counts");
			String used_counts = data.getString("pack_used_counts");
			if (!TextUtils.isEmpty(used_counts) && !TextUtils.isEmpty(counts)) {
				mode.setResidue(Integer.valueOf(counts)
						- Integer.valueOf(used_counts));
			}
			if (!TextUtils.isEmpty(data.getString("card"))) {
				mode.setGiftCode(data.getString("card"));
				mode.setState(1);
			} else {
				mode.setState(0);
			}
			items.add(mode);
		}
		return items;
	}

	@Override
	public List<String> getAllGameName() {
		return Configuration.getAllGameNames();
	}

	@Override
	public void saveRecord(String record) {
		List<String> list = SpUtil.getGameSearchRecord();
		list.remove(record);
		list.add(0, record);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (i < list.size() - 1) {
				builder.append(list.get(i) + ",");
			} else {
				builder.append(list.get(i));
			}
		}
		SpUtil.setGiftSearchRecord(builder);
	}
}
