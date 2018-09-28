package com.tenone.gamebox.mode.able;

import android.content.Context;
import android.content.Intent;

import com.tenone.gamebox.mode.mode.OpenServiceNotificationMode;

import java.util.List;

public interface NotificationDetailsAble {

	String getTitle(Intent intent);

	List<OpenServiceNotificationMode> getContent(Context context);

	void deleteItem(Context context, OpenServiceNotificationMode notificationMode);

	void deleteAll(Context context);
}
