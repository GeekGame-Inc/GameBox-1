/** 
 * Project Name:GameBox 
 * File Name:MonitorService.java 
 * Package Name:com.tenone.gamebox.view.service 
 * Date:2017-4-12обнГ3:29:15 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
*/  
  
package com.tenone.gamebox.view.service;  

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MonitorService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		while (true) {
		}
	}
}
  