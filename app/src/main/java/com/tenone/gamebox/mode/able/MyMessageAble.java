
package com.tenone.gamebox.mode.able;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.List;

public interface MyMessageAble {
	List<Fragment> getFragments();

	List<String> getTitles(Context cxt, int rId);
}
