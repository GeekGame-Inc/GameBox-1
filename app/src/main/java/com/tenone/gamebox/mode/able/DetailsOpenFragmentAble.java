/** 
 * Project Name:GameBox 
 * File Name:DetailsOpenFragmentAble.java 
 * Package Name:com.tenone.gamebox.mode.able 
 * Date:2017-4-12????1:46:12 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.able;

import android.content.Context;

import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.OpenServerMode;
import com.tenone.gamebox.mode.mode.OpenServiceNotificationMode;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.List;

/**
 * ClassName:DetailsOpenFragmentAble <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-12 ????1:46:12 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface DetailsOpenFragmentAble {
	List<OpenServerMode> getModes(GameModel gameModel,
																List<ResultItem> resultItem, Context context);

	OpenServiceNotificationMode getNotificationMode(OpenServerMode mode,
																									GameModel gameModel);
}
