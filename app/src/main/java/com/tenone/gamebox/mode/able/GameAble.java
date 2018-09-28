
package com.tenone.gamebox.mode.able;

import android.content.Context;

import java.util.List;

public interface GameAble {
	<T> List<T> constructViews();

	List<String> getTitles(Context cxt, int resId);
}
