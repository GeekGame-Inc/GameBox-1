package com.tenone.gamebox.view.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.view.utils.DatabaseUtils;

import java.util.List;

public class DownActionReceiver extends BroadcastReceiver {
	DownActivonListener listener;

	@Override
	public void onReceive(Context context, Intent intent) {
		List<GameModel> gameModels = DatabaseUtils.getInstanse(context)
				.getDownloadList();
		int a = 0;
		for (GameModel mode : gameModels) {
			if (mode.getStatus() == GameStatus.LOADING
					|| mode.getStatus() == GameStatus.PAUSEING
					|| mode.getStatus() == GameStatus.COMPLETED) {
				a++;
			}
		}
		if (listener != null) {
			listener.downAction(a);
		}
	}

	public void setDownActivonListener(DownActivonListener listener) {
		this.listener = listener;
	}

	public interface DownActivonListener {
		void downAction(int size);
	}
}
