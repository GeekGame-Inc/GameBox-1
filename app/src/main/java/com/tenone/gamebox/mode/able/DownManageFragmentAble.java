/** 
 * Project Name:GameBox 
 * File Name:DownManageFragmentAble.java 
 * Package Name:com.tenone.gamebox.mode.able 
 * Date:2017-3-20????10:46:20 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.able;

import android.content.Context;

import com.tenone.gamebox.mode.mode.GameModel;

import java.util.List;

/**
 * ClassName:DownManageFragmentAble <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-20 ????10:46:20 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface DownManageFragmentAble {
	
	List<GameModel> getGameModels(Context cxt);

	List<GameModel> getCheckedModels(List<GameModel> items);
}
