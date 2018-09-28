
package com.tenone.gamebox.mode.able;

import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.List;

public interface GameDetailsFragmentAble {
	List<String> getImgUrls(ResultItem resultItem);

	String getIntroText(ResultItem resultItem);

	List<GameModel> getRecommend(ResultItem resultItem);

	boolean isPortrait(ResultItem resultItem);

	String getFeatureText(ResultItem resultItem);

	String getRebateText(ResultItem resultItem);

	String getVipText(ResultItem resultItem);

	String getGifUrl(ResultItem resultItem);
}
