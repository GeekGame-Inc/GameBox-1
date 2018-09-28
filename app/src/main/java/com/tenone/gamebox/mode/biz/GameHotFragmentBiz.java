package com.tenone.gamebox.mode.biz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.tenone.gamebox.mode.able.GameHotFragmentAble;
import com.tenone.gamebox.mode.listener.GameHotFragmentListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class GameHotFragmentBiz implements GameHotFragmentAble {

	@Override
	public void constructArray(Context context, List<ResultItem> resultItem,
			GameHotFragmentListener listener) {
		new MyThread(context, resultItem, listener).start();
	}

	class MyThread extends Thread {
		Context context;
		List<ResultItem> resultItems;
		GameHotFragmentListener listener;

		public MyThread(Context context, List<ResultItem> resultItem,
				GameHotFragmentListener listener) {
			this.context = context;
			this.resultItems = resultItem;
			this.listener = listener;
		}

		@Override
		public void run() {
			final List<GameModel> items = new ArrayList<GameModel>();
			if (resultItems != null) {
				for (ResultItem resultItem2 : resultItems) {
					GameModel model = new GameModel();
					model.setGameTag(resultItem2.getString("tag"));
					model.setType(1);
					model.setImgUrl(MyApplication.getHttpUrl().getBaseUrl()
							+ resultItem2.getString("logo"));
					model.setName(resultItem2.getString("gamename"));
					model.setGameId(Integer
							.valueOf(resultItem2.getString("id")).intValue());
					model.setSize(resultItem2.getString("size"));
					model.setVersionsName(resultItem2.getString("version"));
					model.setUrl(resultItem2.getString("android_url"));
					long download = Long.valueOf(
							resultItem2.getString("download")).longValue();
					if (download > 10000) {
						download = download / 10000;
						model.setTimes(download + "\u4e07+" );
					} else {
						model.setTimes(download + "");
					}
					model.setPackgeName(resultItem2.getString("android_pack"));
					model.setStatus(GameStatus.UNLOAD);
					model.setContent(resultItem2.getString("content"));
					String str = resultItem2.getString("label");
					if (!TextUtils.isEmpty(str)) {
						String[] lableArray = str.split(",");
						model.setLabelArray(lableArray);
					}
					items.add(model);
				}
			}
			handler.post( () -> {
				if (listener != null) {
					listener.uadapteUI(items);
					items.clear();
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
