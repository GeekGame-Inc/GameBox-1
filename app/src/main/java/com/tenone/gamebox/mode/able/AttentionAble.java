
package com.tenone.gamebox.mode.able;

import android.content.Context;

import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.List;

public interface AttentionAble {
	List<GameModel> getGameModels(Context context, List<ResultItem> resultItem);
}
