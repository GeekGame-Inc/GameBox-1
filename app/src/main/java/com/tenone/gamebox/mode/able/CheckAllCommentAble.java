
package com.tenone.gamebox.mode.able;


import android.content.Intent;

import com.tenone.gamebox.mode.mode.DynamicCommentModel;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.List;
public interface CheckAllCommentAble {
	
	String getGameId(Intent intent);

	List<DynamicCommentModel> getCommentModes(ResultItem resultItem);
}
