package com.tenone.gamebox.view.utils;

import android.content.Context;
import android.view.View;

import com.tenone.gamebox.mode.listener.SearchRecordClickListener;
import com.tenone.gamebox.view.custom.popupwindow.AddDownloadWindow;
import com.tenone.gamebox.view.custom.popupwindow.SearchRecordWindow;

import java.util.List;

public class WindowUtils {

	static AddDownloadWindow addDownloadWindow;
	static SearchRecordWindow recordWindow;

	public static void showAddDownloadWindow(Context context, View view,
			long l, String string) {
		if (addDownloadWindow == null) {
			addDownloadWindow = new AddDownloadWindow(context, string, l);
		}
		addDownloadWindow.showCenter(view);
	}

	public static SearchRecordWindow showSearchRecordWindow(Context context,
			View view, List<String> list, String key,
			SearchRecordClickListener listener) {
		if (recordWindow == null) {
			recordWindow = new SearchRecordWindow(context, list, key);
		}
		if (!recordWindow.isShowing()) {
			recordWindow.showAsDropDown(view);
			recordWindow.setSearchRecordClickListener(listener);
		}
		return recordWindow;
	}

}
