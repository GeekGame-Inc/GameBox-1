
package com.tenone.gamebox.view.receiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class BootReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
			String packageName = intent.getDataString();
		}
		if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
			String packageName = intent.getDataString();
		}
	}
}
