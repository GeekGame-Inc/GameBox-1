/** 
 * Project Name:GameBox 
 * File Name:GameNewFragmentAble.java 
 * Package Name:com.tenone.gamebox.mode.able 
 * Date:2017-3-9����3:47:39 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.able;

import android.content.Context;

import com.tenone.gamebox.mode.listener.GameNewFragmentListener;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.List;

public interface GameNewFragmentAble {
	void  constructArray(List<ResultItem> resultItem, Context cxt,
											 GameNewFragmentListener listener);
}
