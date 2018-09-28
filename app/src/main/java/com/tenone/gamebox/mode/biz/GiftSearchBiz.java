package com.tenone.gamebox.mode.biz;

import android.content.Context;
import android.content.Intent;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.able.GiftSearchAble;
import com.tenone.gamebox.mode.mode.RecordMode;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

public class GiftSearchBiz implements GiftSearchAble {
	List<RecordMode> modes = new ArrayList<RecordMode>();
	List<String> records = new ArrayList<String>();
	@Override
	public List<String> getRecord(Context mContext) {
		if (SpUtil.getGiftSearchRecord() != null) {
			records.addAll(SpUtil.getGiftSearchRecord());
		}
		return records;
	}

	@Override
	public String getHint(Intent intent) {
		return "\u8bf7\u8f93\u5165\u641c\u7d22\u5185\u5bb9";
	}

	@Override
	public void saveRecord(List<String> records) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < records.size(); i++) {
			if (i < records.size() - 1) {
				builder.append(records.get(i) + ",");
			} else {
				builder.append(records.get(i));
			}
		}
		SpUtil.setGiftSearchRecord(builder);
	}

	@Override
	public List<RecordMode> getRecordModes(List<String> records) {
		if (records != null && !records.isEmpty() && modes.isEmpty()) {
			for (int i = 0; i < records.size() + 1; i++) {
				RecordMode mode = new RecordMode();
				int resId = 0;
				String str = "";
				if (i == 0) {
					resId = R.drawable.ic_delete_gray;
					str = "\u6e05\u9664\u641c\u7d22\u8bb0\u5f55";
				} else {
					resId = R.drawable.ic_search_time;
					str = records.get(i - 1);
				}
				mode.setRecord(str);
				mode.setResId(resId);
				modes.add(mode);
			}
		}
		return modes;
	}

	@Override
	public void clearRecord() {
		StringBuilder builder = new StringBuilder();
		SpUtil.setGiftSearchRecord(builder);
	}

	@Override
	public List<String> getAllGameName() {
		return Configuration.getAllGameNames();
	}
}
