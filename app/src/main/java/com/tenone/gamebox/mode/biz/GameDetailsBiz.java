
package com.tenone.gamebox.mode.biz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tenone.gamebox.mode.able.GameDetailsAble;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.fragment.DetailsCommentFragment;
import com.tenone.gamebox.view.fragment.DetailsGiftFragment;
import com.tenone.gamebox.view.fragment.DetailsOpenFragment;
import com.tenone.gamebox.view.fragment.GameDetailStrategyFragment;
import com.tenone.gamebox.view.fragment.GameDetailsFragment;
import com.tenone.gamebox.view.utils.ApkUtils;
import com.tenone.gamebox.view.utils.DatabaseUtils;
import com.tenone.gamebox.view.utils.database.GameDownloadTab;

import java.util.ArrayList;
import java.util.List;

public class GameDetailsBiz implements GameDetailsAble {
	GameDetailsFragment detailsFragment;
	DetailsCommentFragment detailsCommentFragment;
	DetailsGiftFragment detailsGiftFragment;
	DetailsOpenFragment detailsOpenFragment;
	GameDetailStrategyFragment gameDetailStrategyFragment;
	List<Fragment> fragments;
	Intent intent;

	@Override
	public String getGameId(Intent intent) {
		this.intent = intent;
		return intent.getExtras().getString("id");
	}

	@Override
	public List<Fragment> getFragments(ResultItem resultItem, String gameId,
			GameModel gameModel) {
		detailsFragment = new GameDetailsFragment();
		detailsFragment.setAction(intent.getAction());
		detailsCommentFragment = new DetailsCommentFragment();
		detailsGiftFragment = new DetailsGiftFragment();
		detailsOpenFragment = new DetailsOpenFragment();
        gameDetailStrategyFragment = new GameDetailStrategyFragment();
		Bundle bundle = new Bundle();
		bundle.putString("id", gameId);
		detailsCommentFragment.setArguments(bundle);
		detailsGiftFragment.setArguments(bundle);
        gameDetailStrategyFragment.setArguments( bundle );
		bundle.putSerializable("gameModel", gameModel);
		detailsOpenFragment.setArguments(bundle);
		bundle.putSerializable("resultItem", resultItem);
		detailsFragment.setArguments(bundle);


		if (fragments == null) {
			fragments = new ArrayList<Fragment>();
			fragments.add(detailsFragment);
			fragments.add(detailsCommentFragment);
			fragments.add(detailsGiftFragment);
			fragments.add(detailsOpenFragment);
            fragments.add(gameDetailStrategyFragment);
		}
		return fragments;
	}

	@Override
	public List<String> getTitles(Context context, int rId) {
		List<String> titles = new ArrayList<String>();
		String[] array = context.getResources().getStringArray(rId);
		if (array != null) {
			for (String string : array) {
				titles.add(string);
			}
		}
		array = null;
		return titles;
	}

	@Override
	public GameModel getGameModel(Context context, ResultItem resultItem) {
		GameModel gameModel = new GameModel();
		ResultItem item = resultItem.getItem("gameinfo");
		gameModel.setGameTag(item.getString("tag"));
		gameModel.setName(item.getString("gamename"));
		String id = item.getString("id");
		if (!TextUtils.isEmpty(id)) {
			gameModel.setGameId(Integer.valueOf(id));
		}
		gameModel.setImgUrl(MyApplication.getHttpUrl().getBaseUrl()
				+ item.getString("logo"));
		gameModel.setSize(item.getString("size"));
		gameModel.setUrl(item.getString("android_url"));
		gameModel.setVersionsName(item.getString("version"));
		gameModel.setPackgeName(item.getString("android_pack"));
		String grade = item.getString("score");
		float g = 0;
		if (!TextUtils.isEmpty(grade)) {
			g = Float.valueOf(grade).floatValue();
		}
		gameModel.setGrade(g);
		gameModel.setCollectde("1".equals(item.getString("collect")));
		String downTimes = item.getString("download");
		if (!TextUtils.isEmpty(downTimes)) {
			int download = Integer.valueOf(downTimes).intValue();
			if (download > 10000) {
				download = download / 10000;
				gameModel.setTimes(download + "��+");
			} else {
				gameModel.setTimes(download + "");
			}
		} else {
			gameModel.setTimes("0");
		}
		String str = item.getString("types");
		if (!TextUtils.isEmpty(str)) {
			String[] array = str.split(" ");
			gameModel.setLabelArray(array);
		}
		String where = GameDownloadTab.GAMEID + "=? AND "
				+ GameDownloadTab.GAMENAME + "=?";
		GameModel model = DatabaseUtils.getInstanse(context).getGameModel(
				where,
				new String[] { (gameModel.getGameId() + ""),
						gameModel.getName() });
		gameModel.setApkName(model.getApkName());
		gameModel.setStatus(model.getStatus());
		gameModel.setProgress(model.getProgress());
		ApkUtils.inspectApk(context, gameModel);
		model = null;
		return gameModel;
	}
}
