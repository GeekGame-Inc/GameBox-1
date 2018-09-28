
package com.tenone.gamebox.mode.able;

import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.mode.StrategyMode;

import java.util.List;

public interface StrategyAble {
	List<StrategyMode> getHeader(List<ResultItem> resultItem);

	List<StrategyMode> getModes(List<ResultItem> resultItem);
}
