
package com.tenone.gamebox.mode.able;

import com.tenone.gamebox.mode.mode.GiftMode;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.List;

public interface DetailsGiftFragmentAble {
	List<GiftMode> constructArray(List<ResultItem> resultItems);

	String getGiftCode(ResultItem resultItem);
}
