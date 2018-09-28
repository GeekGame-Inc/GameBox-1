/** 
 * Project Name:GameBox 
 * File Name:DeleteDialog.java 
 * Package Name:com.tenone.gamebox.view.custom.dialog 
 * Date:2017-3-20����4:00:11 
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
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.DeleteDialogConfrimListener;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

/**
 * ClassName:DeleteDialog <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-20 ����4:00:11 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
@SuppressLint("InflateParams")
public class UpdateDialog extends AlertDialog {

	protected UpdateDialog(Context context, int theme) {
		super(context, theme);
	}

	protected UpdateDialog(Context context) {
		super(context);
	}

	public static class UpdateBuidler {
		Context mContext;
		String message;
		String cancleText;
		String confirmText;
		int prosgerss;
		DeleteDialogConfrimListener confrimListener;
		ProgressBar progressBar;
		TextView textView;
        TextView confirmBt, cancleBt;

		public UpdateBuidler(Context cxt) {
			this.mContext = cxt;
		}

		public UpdateBuidler setMessage(String message) {
			this.message = message;
			return this;
		}

		public UpdateBuidler setCancleText(String cancleText) {
			this.cancleText = cancleText;
			return this;
		}

		public UpdateBuidler setConfirmText(String confirmText) {
			this.confirmText = confirmText;
			return this;
		}

		public UpdateBuidler setMessage(int rId) {
			this.message = mContext.getResources().getString(rId);
			return this;
		}

		public UpdateBuidler setCancleText(int rId) {
			this.cancleText = mContext.getResources().getString(rId);
			return this;
		}

		public UpdateBuidler setConfirmText(int rId) {
			this.confirmText = mContext.getResources().getString(rId);
			return this;
		}

		public UpdateBuidler setConfrimListener(
				DeleteDialogConfrimListener confrimListener) {
			this.confrimListener = confrimListener;
			return this;
		}

		public UpdateBuidler setProsgerss(int p) {
			this.prosgerss = p;
			return this;
		}

		public ProgressBar getProgressBar() {
			return progressBar;
		}

		public TextView getMessageTv() {
			return textView;
		}

		public TextView getConfirmBt() {
			return confirmBt;
		}

		public TextView getCancleBt() {
			return cancleBt;
		}

		public void show() {
			final UpdateDialog dialog = new UpdateDialog(mContext);
			View contentView = LayoutInflater.from(mContext).inflate(
					R.layout.layout_update, null);
			dialog.show();
			dialog.setContentView(contentView);
			textView = contentView
					.findViewById(R.id.id_updateDialog_title);
			if (!TextUtils.isEmpty(message)) {
				textView.setText(message);
			}
			cancleBt = contentView
					.findViewById(R.id.id_updateDialog_cancle);
			if (!TextUtils.isEmpty(cancleText)) {
				cancleBt.setText(cancleText);
			}
			cancleBt.setOnClickListener( v -> dialog.dismiss() );

			confirmBt = contentView
					.findViewById(R.id.id_updateDialog_confirm);
			if (!TextUtils.isEmpty(confirmText)) {
				confirmBt.setText(confirmText);
			}

			confirmBt.setOnClickListener( v -> {
                if (confrimListener != null) {
                    confrimListener.onConfrimClick(dialog);
                }
            } );
			progressBar = contentView
					.findViewById(R.id.id_updateDialog_progressBar);
			dialog.setCancelable(false);

			RelativeLayout layout = contentView
					.findViewById(R.id.id_updateDialog_layout);
			int width = DisplayMetricsUtils.getScreenWidth(mContext)
					- DisplayMetricsUtils.dipTopx(mContext, 40);
			int height = DisplayMetricsUtils.getScreenHeight(mContext) /4;

			ViewGroup.LayoutParams params = layout.getLayoutParams();
			params.width = width;
			params.height = height;
			layout.setLayoutParams(params);
		}
	}
}
