
package com.tenone.gamebox.mode.able;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.List;

public interface GameDetailsAble {
	String getGameId(Intent intent);

	List<Fragment> getFragments(ResultItem resultItem, String gameId,
															GameModel gameModel);

	List<String> getTitles(Context context, int rId);

	GameModel getGameModel(Context context, ResultItem resultItem);
}
