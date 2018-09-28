package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tenone.gamebox.R;

public class ToastCustom {

	public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
	public static final int LENGTH_LONG = Toast.LENGTH_LONG;

	Toast toast;
	Context mContext;
	TextView toastTextField;

	@SuppressLint("InflateParams")
	public ToastCustom(Context context) {
		mContext = context;
		toast = new Toast(mContext);
		View toastRoot = LayoutInflater.from(context).inflate(
				R.layout.layout_custom_toast, null);
		toastTextField = toastRoot.findViewById(R.id.toast_text);
		toast.setView(toastRoot);
	}

	public void setDuration(int d) {
		toast.setDuration(d);
	}

	public void setText(String t) {
		toastTextField.setText(t);
	}

	public static ToastCustom makeText(Context context, String text,
			int duration) {
		ToastCustom toastCustom = new ToastCustom(context);
		toastCustom.setText(text);
		toastCustom.setDuration(duration);
		return toastCustom;
	}

	public void show() {
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void show(int position) {
		toast.setGravity(position, 0, 0);
		toast.show();
	}
}
