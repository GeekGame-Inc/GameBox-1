
package com.tenone.gamebox.mode.biz;

import android.text.TextUtils;

import com.tenone.gamebox.mode.able.GameDetailsFragmentAble;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.MyApplication;

import java.util.ArrayList;
import java.util.List;
public class GameDetailsFragmentBiz implements GameDetailsFragmentAble {

	@Override
	public ArrayList<String> getImgUrls(ResultItem resultItem) {
		ArrayList<String> urls = new ArrayList<String>();
		if (resultItem.getItem("gameinfo") != null) {
			List<ResultItem> items = resultItem.getItem("gameinfo").getItems(
					"imgs");
			int size = items.size();
			for (int i = 0; i < size; i++) {
				String url = MyApplication.getHttpUrl().getBaseUrl()
						+ String.valueOf(items.get(i));
				urls.add(url);
			}
		}
		return urls;
	}

	@Override
	public String getIntroText(ResultItem resultItem) {
		String content = "";
		if (resultItem.getItem("gameinfo") != null) {
			content = resultItem.getItem("gameinfo").getString("abstract");
		}
		return content;
	}

	@Override
	public List<GameModel> getRecommend(ResultItem resultItem) {
		List<GameModel> items = new ArrayList<GameModel>();
		List<ResultItem> list = resultItem.getItems("like");
		for (ResultItem results : list) {
			GameModel model = new GameModel();
			model.setGameId(Integer.valueOf(results.getString("id")).intValue());
			model.setName(results.getString("gamename"));
			model.setImgUrl(MyApplication.getHttpUrl().getBaseUrl()
					+ results.getString("logo"));
			items.add(model);
		}
		return items;
	}

	@Override
	public String getFeatureText(ResultItem resultItem) {
		String content = "";
		if (resultItem.getItem("gameinfo") != null) {
			content = resultItem.getItem("gameinfo").getString("feature");
		}
		return content;
	}

	@Override
	public String getRebateText(ResultItem resultItem) {
		String content = "";
		if (resultItem.getItem("gameinfo") != null) {
			content = resultItem.getItem("gameinfo").getString("rebate");
		}
		return content;
	}

	@Override
	public String getVipText(ResultItem resultItem) {
		String vip = "";
		if (resultItem.getItem("gameinfo") != null) {
			vip = resultItem.getItem("gameinfo").getString("vip");
		}
		return vip;
	}

	@Override
	public String getGifUrl(ResultItem resultItem) {
		String url = "";
		if (resultItem.getItem("gameinfo") != null) {
			url = resultItem.getItem("gameinfo").getString("gif");
		}
		return url;
	}

	@Override
	public boolean isPortrait(ResultItem resultItem) {
		String str = resultItem.getItem("gameinfo").getString("gif_model");
		if (TextUtils.isEmpty(str)) {
			return false;
		}
		return !"1".equals( str );
	}
}
