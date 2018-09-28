
package com.tenone.gamebox.mode.able;


import android.content.Context;

import com.tenone.gamebox.mode.listener.GameRecommendFragmentListener;
import com.tenone.gamebox.mode.mode.ResultItem;

public interface GameRecommendFragmentAble {
	void constructArray(Context cxt, ResultItem resultItem,
											GameRecommendFragmentListener listener);

	void constructBannerArray(Context cxt, ResultItem resultItem,
														GameRecommendFragmentListener listener);

}
