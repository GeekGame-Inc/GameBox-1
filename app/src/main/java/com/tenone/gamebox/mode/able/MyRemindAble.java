/** 
 * Project Name:GameBox 
 * File Name:MyRemindAble.java 
 * Package Name:com.tenone.gamebox.mode.able 
 * Date:2017-3-29����11:55:22 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.able;

import android.content.Context;

import com.tenone.gamebox.mode.listener.OnClearListener;
import com.tenone.gamebox.mode.mode.OpenServerMode;

import java.util.List;

public interface MyRemindAble {
	List<OpenServerMode> getModes(Context context);
	
	void clearAllNotification(List<OpenServerMode> modes, OnClearListener listener, int id);
	
	void clearOneNotification(OnClearListener listener, int id, int gameId);
}
