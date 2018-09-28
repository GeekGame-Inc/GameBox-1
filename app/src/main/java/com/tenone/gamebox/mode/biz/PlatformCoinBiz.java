package com.tenone.gamebox.mode.biz;

import android.annotation.SuppressLint;
import android.os.Handler;

import com.tenone.gamebox.mode.able.PlatformCoinDetailAble;
import com.tenone.gamebox.mode.listener.PlatformCoinListener;
import com.tenone.gamebox.mode.mode.PlatformCoinModel;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.ArrayList;
import java.util.List;

public class PlatformCoinBiz implements PlatformCoinDetailAble {

	@Override
	public void constructModels(ResultItem resultItem,
			PlatformCoinListener listener, boolean isGold) {
		new MyThread(resultItem, listener, isGold).start();
	}

	private class MyThread extends Thread {
		PlatformCoinListener listener;
		ResultItem resultItem;
		List<PlatformCoinModel> models = new ArrayList<PlatformCoinModel>();
		boolean isGold;

		public MyThread(ResultItem result, PlatformCoinListener l, boolean b) {
			this.listener = l;
			this.resultItem = result;
			this.isGold = b;
		}

		@Override
		public void run() {
			List<ResultItem> items = resultItem.getItems("list");
			if (items != null) {
				for (int i = 0; i < items.size(); i++) {
					ResultItem item = items.get(i);
					PlatformCoinModel model = new PlatformCoinModel();
					model.setCounts(getString(item, isGold ? "coin_counts"
							: "platform_counts"));
					model.setNum(getString(item, isGold ? "coin_change"
							: "platform_change"));
					model.setTime(getString(item, "create_time"));
					model.setType(getString(item, "type"));
					model.setGold(isGold);
					models.add(model);
				}
			}
			if (listener != null) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						listener.onPlatformCoinConstruct(models);
					}
				});
			}
			super.run();
		}

		private String getString(ResultItem item, String key) {
			return item.getString(key);
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
		}
	};
}
