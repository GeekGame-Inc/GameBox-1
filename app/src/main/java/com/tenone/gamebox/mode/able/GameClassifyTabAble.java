package com.tenone.gamebox.mode.able;

import android.content.Context;
import android.content.Intent;

import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.List;
public interface GameClassifyTabAble {
	List<GameModel> constructArray(Context context, List<ResultItem> resultItem);

	String getTitle(Intent intent);
	
	String getClassifyId(Intent intent);
}
