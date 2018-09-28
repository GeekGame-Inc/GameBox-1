package com.tenone.gamebox.mode.able;

import android.content.Intent;

import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.StrategyMode;

import java.util.List;

public interface StrategySearchResultAble {
	List<StrategyMode> getStrategyModes(ResultItem resultItem);

	String getHint(Intent intent);

	List<String> getAllGameName();

	void saveRecord(String record);
}
