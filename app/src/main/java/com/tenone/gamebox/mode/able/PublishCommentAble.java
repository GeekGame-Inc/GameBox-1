
package com.tenone.gamebox.mode.able;

import android.content.Intent;

import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.ArrayList;
import java.util.List;

public interface PublishCommentAble {
	ArrayList<String> collectImgUrls(List<ResultItem> resultItems);

	ArrayList<String> phontoArray();

	String getGameId(Intent intent);
	
	long getTopicId(Intent intent);
	
	long getReplyId(Intent intent);
}
