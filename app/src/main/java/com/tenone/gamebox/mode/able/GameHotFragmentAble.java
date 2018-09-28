
package com.tenone.gamebox.mode.able;

import android.content.Context;

import com.tenone.gamebox.mode.listener.GameHotFragmentListener;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.List;

public interface GameHotFragmentAble {
	void constructArray(Context context, List<ResultItem> resultItem,
											GameHotFragmentListener listener);
}
