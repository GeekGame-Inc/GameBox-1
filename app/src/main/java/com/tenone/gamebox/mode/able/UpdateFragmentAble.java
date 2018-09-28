
package com.tenone.gamebox.mode.able;

import com.tenone.gamebox.mode.listener.GameHotFragmentListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.List;

public interface UpdateFragmentAble {
	List<GameModel> getGameModels();

	String getIds(List<GameModel> modes);

	void getUpdateGame(List<ResultItem> resultItems,
										 GameHotFragmentListener listener);
}
