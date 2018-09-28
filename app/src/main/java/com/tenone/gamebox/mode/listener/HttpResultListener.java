package com.tenone.gamebox.mode.listener;

import com.tenone.gamebox.mode.mode.ResultItem;


public interface HttpResultListener {
	
	void onSuccess(int what, ResultItem resultItem);

	void onError(int what, String error);
}
