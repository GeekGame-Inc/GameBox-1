/** 
 * Project Name:GameBox 
 * File Name:UpdateDetectionDialog.java 
 * Package Name:com.tenone.gamebox.view.custom.dialog 
 * Date:2017-3-27����10:08:51 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.custom.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

/**
 * ClassName:UpdateDetectionDialog <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-27 ����10:08:51 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class UpdateDetectionDialog extends AlertDialog {

	protected UpdateDetectionDialog(Context context) {
		super(context);
	}

	public static class DetectionBuilder {
		Context mContext;
		String message;
		UpdateDetectionDialog dialog;

		public DetectionBuilder(Context cxt) {
			this.mContext = cxt;
		}

		public DetectionBuilder setMessage(String msg) {
			this.message = msg;
			return this;
		}

		public DetectionBuilder setMessage(int msgId) {
			this.message = mContext.getResources().getString(msgId);
			return this;
		}

		public void showDialog() {
			dialog = new UpdateDetectionDialog(mContext);
			dialog.show();
			View view = LayoutInflater.from(mContext).inflate(
					R.layout.dialog_detection_update, null);
			dialog.setContentView(view);
			TextView messageTextView = view
					.findViewById(R.id.id_detection_message);
			LayoutParams params = messageTextView.getLayoutParams();
			params.width = DisplayMetricsUtils.getScreenWidth(mContext)
					- DisplayMetricsUtils.dipTopx(mContext, 53);
			params.height = params.width / 3;
			messageTextView.setLayoutParams(params);
			if (!TextUtils.isEmpty(message)) {
				messageTextView.setText(message);
			}
		}

		public void dismiss() {
			if (dialog != null) {
				dialog.dismiss();
			}
		}
	}
}
