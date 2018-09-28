package com.tenone.gamebox.mode.able;


import com.tenone.gamebox.mode.listener.PlatformCoinListener;
import com.tenone.gamebox.mode.mode.ResultItem;

public interface PlatformCoinDetailAble {
	void constructModels(ResultItem resultItem, PlatformCoinListener listener, boolean isGold);
}
