

package com.tenone.gamebox.mode.listener;

import com.tenone.gamebox.mode.mode.BannerModel;
import com.tenone.gamebox.mode.mode.GameModel;

import java.util.List;


public interface GameRecommendFragmentListener {
	
	void updateGameListUi(List<GameModel> gameModels);

	void uddateBannerUi(List<BannerModel> bannerModels);
}
