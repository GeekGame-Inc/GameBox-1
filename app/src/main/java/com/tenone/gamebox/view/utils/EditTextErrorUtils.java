/** 
 * Project Name:GameBox 
 * File Name:EditTextErrorUtils.java 
 * Package Name:com.tenone.gamebox.view.utils 
 * Date:2017-3-17上午11:52:06 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;

import com.tenone.gamebox.view.custom.CustomizeEditText;

/**
 * 输入框错误提示工具类 ClassName:EditTextErrorUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-17 上午11:52:06 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class EditTextErrorUtils {
	@SuppressWarnings("deprecation")
	public static void setError(Context cxt, CustomizeEditText editText, int rid) {
		String text = cxt.getResources().getString(rid);
		editText.setError(Html.fromHtml("<font color=#E10979>" + text
				+ "</font>"));
		editText.requestFocus();
		Message message = new Message();
		message.obj = editText;
		handler.sendMessageDelayed(message, 3000);
	}

	/**
	 * 取消错误提示
	 */
	public static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.obj != null) {
				CustomizeEditText editText = (CustomizeEditText) msg.obj;
				editText.setError(null);
			}
		}
	};
}
