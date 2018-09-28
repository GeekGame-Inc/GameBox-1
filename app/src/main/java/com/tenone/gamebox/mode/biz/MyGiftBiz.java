package com.tenone.gamebox.mode.biz;

import android.text.TextUtils;

import com.tenone.gamebox.mode.able.MyGiftAble;
import com.tenone.gamebox.mode.mode.GiftMode;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class MyGiftBiz implements MyGiftAble {

	@Override
	public List<GiftMode> getGiftModes(ResultItem resultItem) {
		List<GiftMode> items = new ArrayList<GiftMode>();
		List<ResultItem> resultItems = resultItem.getItems("list");
		for (ResultItem data : resultItems) {
			GiftMode mode = new GiftMode();
			String id = data.getString("pid");
			if (!TextUtils.isEmpty(id)) {
				mode.setGiftId(Integer.valueOf(id));
			}
			mode.setGiftImgUrl(MyApplication.getHttpUrl().getBaseUrl()
					+ data.getString("pack_logo"));
			mode.setGiftName(data.getString("pack_name"));
			
			String counts = data.getString("pack_counts");
			if (!TextUtils.isEmpty(counts)) {
				mode.setGiftCounts(Long.valueOf(counts));
			}
			
			String used = data.getString("pack_used_counts");
			if (!TextUtils.isEmpty(counts) && !TextUtils.isEmpty(used)) {
				mode.setResidue(Integer.valueOf(counts) - Integer.valueOf(used));
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
}
