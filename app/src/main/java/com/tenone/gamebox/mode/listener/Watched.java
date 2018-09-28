/** 
 * Project Name:GameBox 
 * File Name:Watched.java 
 * Package Name:com.tenone.gamebox.mode.listener 
 * Date:2017-3-31����6:18:05 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.listener;

import android.database.ContentObserver;
import android.os.Handler;

public abstract class Watched extends ContentObserver {
	
	public Watched(Handler handler) {
		super(handler);
	}
	
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		onUpdate();
	}
	
	public abstract void onUpdate();
}
