package com.tenone.gamebox.mode.able;

import android.content.Intent;

import com.tenone.gamebox.mode.mode.RecordMode;

import java.util.List;

public interface StrategySearchAble {
	List<String> getAllGameNames();

	String getHint(Intent intent);

	List<String> getRecord();

	void saveRecord(List<String> records);

	List<RecordMode> getRecordModes(List<String> records);

	void clearRecord();
}
