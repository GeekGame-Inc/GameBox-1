package com.tenone.gamebox.mode.biz;

import android.text.TextUtils;

import com.tenone.gamebox.mode.able.RecommentGameAble;
import com.tenone.gamebox.mode.mode.BannerModel;
import com.tenone.gamebox.mode.mode.GiftMode;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class RecommentGameGiftBiz implements RecommentGameAble {

	@Override
	public List<GiftMode> getModes(List<ResultItem> resultItems) {
		List<GiftMode> giftModes = new ArrayList<GiftMode>();
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
			giftModes.add(mode);
		}
		return giftModes;
	}

	@Override
	public List<BannerModel> constructBannerArray(List<ResultItem> resultItem) {
		List<BannerModel> bannerModels = new ArrayList<BannerModel>();
		for (ResultItem result : resultItem) {
			BannerModel model = new BannerModel();
			String gameId = result.getString("gid");
			if (!TextUtils.isEmpty(gameId)) {
				model.setGameId(Integer.valueOf(gameId).intValue());
			}
			model.setImageUrl(MyApplication.getHttpUrl().getBaseUrl()
					+ result.getString("slide_pic"));
			bannerModels.add(model);
		}
		return bannerModels;
	}
}
