package com.tenone.gamebox.mode.able;

import android.content.Context;

import com.tenone.gamebox.mode.mode.OpenServerMode;
import com.tenone.gamebox.mode.mode.OpenServiceNotificationMode;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.List;
public interface TodayOpenFragmentAble {

	List<OpenServerMode> getModes(List<ResultItem> resultItem, Context context);

	OpenServiceNotificationMode getNotificationMode(OpenServerMode mode);
	
	void comparisonSql(Context context, List<OpenServerMode> modes);
}
