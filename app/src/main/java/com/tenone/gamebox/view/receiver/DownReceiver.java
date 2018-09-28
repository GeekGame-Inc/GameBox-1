package com.tenone.gamebox.view.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tenone.gamebox.mode.mode.GameModel;

public  class DownReceiver extends BroadcastReceiver {
	
	DownStatusChangeListener changeListener;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.hasExtra("data")) {
			GameModel gameModel = (GameModel) intent.getExtras().get("data");
			if (changeListener != null) {
				changeListener.onDownStatusChange(gameModel);
			}
		} 
	}

	public void setChangeListener(DownStatusChangeListener changeListener) {
		this.changeListener = changeListener;
	}
	
	public interface DownStatusChangeListener {
		void onDownStatusChange(GameModel model);
	}
}
