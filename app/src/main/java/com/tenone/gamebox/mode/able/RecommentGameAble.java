
package com.tenone.gamebox.mode.able;

import com.tenone.gamebox.mode.mode.BannerModel;
import com.tenone.gamebox.mode.mode.GiftMode;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.List;

public interface RecommentGameAble {
	List<GiftMode> getModes(List<ResultItem> resultItems);
	
	List<BannerModel> constructBannerArray(List<ResultItem> resultItem);
}
