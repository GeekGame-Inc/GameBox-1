package com.tenone.gamebox.view.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tenone.gamebox.mode.biz.AutoInstallApkThread;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.view.utils.SpUtil;

public class InstallReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		GameModel model = (GameModel) intent.getExtras().get("data");
		if (null != model) {
			if (GameStatus.COMPLETED == model.getStatus()) {
				if (SpUtil.getAutoInstall()) {
					new AutoInstallApkThread(context, model.getApkName())
							.start();
				}
			}
		}
	}
}
