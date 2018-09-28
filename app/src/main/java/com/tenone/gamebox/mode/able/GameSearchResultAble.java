package com.tenone.gamebox.mode.able;

import android.content.Context;
import android.content.Intent;

import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.List;

public interface GameSearchResultAble {
	String getCondition(Intent intent);

	List<GameModel> getGameModels(Context cxt, List<ResultItem> resultItems);

	List<String> getAllGameName();

	void saveRecord(String record);

}
