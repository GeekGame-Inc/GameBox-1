package com.tenone.gamebox.mode.able;

import com.tenone.gamebox.mode.mode.GameClassify;
import com.tenone.gamebox.mode.mode.GameClassifyModel;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.List;

public interface GameClassifyRecyclerAble {
	List<GameClassify> constructArray(List<ResultItem> resultItem);

	List<GameClassifyModel> cinstructRecommendArray(List<ResultItem> resultItem);
}
  