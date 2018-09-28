/** 
 * Project Name:GameBox 
 * File Name:SelectPictureDialog.java 
 * Package Name:com.tenone.gamebox.view.custom.dialog 
 * Date:2017-3-14ÉÏÎç11:48:45 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.custom.dialog;

import android.app.Dialog;
import android.content.Context;

/**
 * Í¼Æ¬Ñ¡Ôñ ClassName:SelectPictureDialog <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-14 ÉÏÎç11:48:45 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class SelectPictureDialog extends Dialog {
	Context mContext;

	public SelectPictureDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
	}

	public SelectPictureDialog(Context context) {
		this(context, 0);
		this.mContext = context;
	}

	public static class Builder {
	}
}
