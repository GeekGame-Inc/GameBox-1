
package com.tenone.gamebox.mode.able;

import android.content.Intent;

import com.tenone.gamebox.mode.mode.GiftMode;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.List;

public interface GiftSearchResultAble {
	String gameName(Intent intent);

	List<GiftMode> getGiftModes(ResultItem resultItem);

	List<String> getAllGameName();

	void saveRecord(String record);
}
