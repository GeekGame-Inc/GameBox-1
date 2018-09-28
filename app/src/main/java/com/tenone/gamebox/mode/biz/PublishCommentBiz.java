package com.tenone.gamebox.mode.biz;

import android.content.Intent;

import com.tenone.gamebox.mode.able.PublishCommentAble;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.ArrayList;
import java.util.List;

public class PublishCommentBiz implements PublishCommentAble {

	ArrayList<String> array = new ArrayList<String>();
	ArrayList<String> photoArray = new ArrayList<String>();

	@Override
	public ArrayList<String> collectImgUrls(List<ResultItem> resultItems) {
		return array;
	}

	@Override
	public ArrayList<String> phontoArray() {
		return photoArray;
	}

	@Override
	public String getGameId(Intent intent) {
		return intent.getExtras().getString("id");
	}

	@Override
	public long getTopicId(Intent intent) {
		return intent.getExtras().getLong("topId");
	}

	@Override
	public long getReplyId(Intent intent) {
		return intent.getExtras().getLong("replyId");
	}
}
