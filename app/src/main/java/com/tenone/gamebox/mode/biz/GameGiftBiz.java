
package com.tenone.gamebox.mode.biz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.tenone.gamebox.mode.able.GameGiftAble;
import com.tenone.gamebox.mode.listener.GameGiftListener;
import com.tenone.gamebox.mode.mode.GiftMode;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class GameGiftBiz implements GameGiftAble {

	@Override
	public void constructArray(Context context, ResultItem resultItem,
			GameGiftListener listener) {
		new MyThread(context, resultItem, listener).start();
	}

	class MyThread extends Thread {
		Context context;
		ResultItem resultItem;
		GameGiftListener listener;

		public MyThread(Context context, ResultItem resultItem,
				GameGiftListener listener) {
			this.context = context;
			this.listener = listener;
			this.resultItem = resultItem;
		}

		@Override
		public void run() {
			final List<GiftMode> items = new ArrayList<GiftMode>();
			if (resultItem != null) {
				List<ResultItem> resultItems = resultItem.getItems("list");
				if (resultItems != null) {
					for (ResultItem data : resultItems) {
						GiftMode mode = new GiftMode();
						String id = data.getString("id");
						if (!TextUtils.isEmpty(id)) {
							mode.setGiftId(Integer.valueOf(id));
						}
						mode.setGiftImgUrl(MyApplication.getHttpUrl()
								.getBaseUrl() + data.getString("pack_logo"));
						
						mode.setGiftCounts(Long.valueOf(data
								.getString("pack_counts")));
						mode.setGiftName(data.getString("pack_name"));
						
						String counts = data.getString("pack_counts");
						String usedCounts = data.getString("pack_used_counts");
					
						if (!TextUtils.isEmpty(counts)
								&& !TextUtils.isEmpty(usedCounts)) {
							mode.setResidue(Integer.valueOf(counts)
									- Integer.valueOf(usedCounts));
						}
						
						if (!TextUtils.isEmpty(data.getString("card"))) {
							mode.setGiftCode(data.getString("card"));
							mode.setState(1);
						} else {
							mode.setState(0);
						}
						items.add(mode);
					}
				}
			}

			handler.post( () -> {
				if (listener != null) {
					listener.updateUI(items);
				}
			} );
			super.run();
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};
}
