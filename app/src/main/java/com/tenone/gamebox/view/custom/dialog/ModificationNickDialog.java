/** 
 * Project Name:GameBox 
 * File Name:ModificationNickDialog.java 
 * Package Name:com.tenone.gamebox.view.custom.dialog 
 * Date:2017-3-14����1:57:04 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.custom.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.ModificationNickListener;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

/**
 * �޸��ǳ�dialog ClassName:ModificationNickDialog <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-14 ����1:57:04 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class ModificationNickDialog extends Dialog {

	public ModificationNickDialog(Context context, int theme) {
		super(context, R.style.modificationNickDialogStyle);
	}

	public ModificationNickDialog(Context cxt) {
		this(cxt, 0);
	}

	public static class Builder {
		Context context;
		String hint, cancleBtText, confirmBtText;
		ModificationNickListener listener;
		Button cancleBt, confirmBt;
		CustomizeEditText editText;

		public Builder(Context context) {
			this.context = context;
		}

		public Button getCancleBt() {
			return cancleBt;
		}

		public void setCancleBt(Button cancleBt) {
			this.cancleBt = cancleBt;
		}

		public Button getConfirmBt() {
			return confirmBt;
		}

		public void setConfirmBt(Button confirmBt) {
			this.confirmBt = confirmBt;
		}

		public CustomizeEditText getEditText() {
			return editText;
		}

		public void setEditText(CustomizeEditText editText) {
			this.editText = editText;
		}

		public Builder setEidtHint(String hint) {
			this.hint = hint;
			return this;
		}

		public Builder setEidtHint(int rid) {
			this.hint = context.getResources().getString(rid);
			return this;
		}

		public Builder setCancleBtText(String text) {
			this.cancleBtText = text;
			return this;
		}

		public Builder setCancleBtText(int rid) {
			this.cancleBtText = context.getResources().getString(rid);
			return this;
		}

		public Builder setConfirmBtText(String text) {
			this.confirmBtText = text;
			return this;
		}

		public Builder setConfirmBtText(int rid) {
			this.confirmBtText = context.getResources().getString(rid);
			return this;
		}

		public Builder setOnCancleListener(ModificationNickListener listener) {
			this.listener = listener;
			return this;
		}

		@SuppressLint("InflateParams")
		public ModificationNickDialog create() {
			final ModificationNickDialog dialog = new ModificationNickDialog(
					context);
			View layout = LayoutInflater.from(context).inflate(
					R.layout.dialog_modification_nick, null);
			dialog.setContentView(layout);
			cancleBt = layout
					.findViewById(R.id.id_modificationNick_cancleBt);
			confirmBt = layout
					.findViewById(R.id.id_modificationNick_confirmBt);
			editText = layout
					.findViewById(R.id.id_modificationNick_editText);
			LayoutParams params = editText.getLayoutParams();
			params.width = DisplayMetricsUtils.getScreenWidth(context) / 4 * 3;
			editText.setLayoutParams(params);
			cancleBt.setText(cancleBtText);
			confirmBt.setText(confirmBtText);
			editText.setHint(hint);
			cancleBt.setOnClickListener( v -> {
				if (listener != null) {
					listener.onCancleClick(dialog);
				}
			} );
			confirmBt.setOnClickListener( v -> {
				if (listener != null) {
					listener.onConfirmClick(editText.getText().toString());
				}
			} );
			return dialog;
		}
	}
}
