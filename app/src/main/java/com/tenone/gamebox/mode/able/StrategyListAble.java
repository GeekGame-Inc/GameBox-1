package com.tenone.gamebox.mode.able;

import android.content.Context;

import com.tenone.gamebox.mode.listener.GameTopListener;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.List;

public interface StrategyListAble {
	void constructArray(Context context, List<ResultItem> resultItem, GameTopListener listener);
}
