/** 
 * Project Name:GameBox 
 * File Name:GetGiftDialog.java 
 * Package Name:com.tenone.gamebox.view.custom.dialog 
 * Date:2017-3-21����11:00:42 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.custom.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.GetGiftDialogConfirmListener;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

/**
 * ��������� ClassName:GetGiftDialog <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-21 ����11:00:42 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
@SuppressLint("InflateParams")
public class GetGiftDialog extends AlertDialog {

	protected GetGiftDialog(Context context, int theme) {
		super(context, theme);
	}

	protected GetGiftDialog(Context context) {
		super(context);
	}

	public static class Builder {
		Context mContext;
		String title, message, buttonText;
		GetGiftDialogConfirmListener confirmListener;

		public Builder(Context cxt) {
			this.mContext = cxt;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setButtonText(String buttonText) {
			this.buttonText = buttonText;
			return this;
		}

		public Builder setConfirmListener(
				GetGiftDialogConfirmListener confirmListener) {
			this.confirmListener = confirmListener;
			return this;
		}

		public void showDialog() {
			final GetGiftDialog dialog = new GetGiftDialog(mContext);
			View contentView = LayoutInflater.from(mContext).inflate(
					R.layout.dialog_gift, null);
			dialog.show();
			dialog.setContentView(contentView);
			TextView titleTv = contentView
					.findViewById(R.id.id_getGift_title);
			TextView messageTv = contentView
					.findViewById(R.id.id_getGift_message);
			TextView confirmTv = contentView
					.findViewById(R.id.id_getGift_confirm);
			LayoutParams params = titleTv.getLayoutParams();
			params.width = DisplayMetricsUtils.getScreenWidth(mContext)
					- DisplayMetricsUtils.dipTopx(mContext, 53);
			titleTv.setLayoutParams(params);
			if (!TextUtils.isEmpty(title)) {
				titleTv.setText(title);
			}

			if (!TextUtils.isEmpty(message)) {
				messageTv.setText(message);
			}
			
			if (!TextUtils.isEmpty(buttonText)) {
				confirmTv.setText(buttonText);
			}

			confirmTv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (confirmListener != null) {
						confirmListener.onConfirmClick(dialog);
					}
				}
			});
		}
	}
}
