package com.tenone.gamebox.view.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.tenone.gamebox.view.utils.NetworkUtils;

public class NetworkChangeRexeiver extends BroadcastReceiver {

	OnNetworkChangeListener listener;

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
			if (listener != null) {
				if (networkInfo != null && networkInfo.isAvailable()) {
					String netWorkType = NetworkUtils.GetNetworkType(context);
					if ("WIFI".equals(netWorkType)) {
						listener.networkChange(true, true);
					} else {
						listener.networkChange(true, false);
					}
				} else {
					Toast.makeText(context, "\u7f51\u7edc\u672a\u8fde\u63a5!", Toast.LENGTH_LONG).show();
					listener.networkChange(false, false);
				}
			}
		}
	}

	public void setOnNetworkChangeListener(OnNetworkChangeListener l) {
		this.listener = l;
	}

	public interface OnNetworkChangeListener {
		void networkChange(boolean isAvailable, boolean isWifi);
	}
}
