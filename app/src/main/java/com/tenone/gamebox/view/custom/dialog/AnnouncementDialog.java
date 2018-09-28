package com.tenone.gamebox.view.custom.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

public class AnnouncementDialog extends AlertDialog {

	protected AnnouncementDialog(Context context, int theme) {
		super(context, theme);
	}

	public AnnouncementDialog(Context context) {
		super(context);
	}

	public static class AnnouncementDialogBuilder {
		Context mContext;
		String title, content;

		public AnnouncementDialogBuilder(Context cxt) {
			this.mContext = cxt;
		}

		public AnnouncementDialogBuilder setTitle(String title) {
			this.title = title;
			return this;
		}

		public AnnouncementDialogBuilder setContent(String content) {
			this.content = content;
			return this;
		}

		@SuppressLint("InflateParams")
		public void showDialog() {
			final AnnouncementDialog dialog = new AnnouncementDialog(mContext);
			View view = LayoutInflater.from(mContext).inflate(
					R.layout.dialog_announcement, null);
			dialog.show();
			dialog.setContentView(view);

			int pandding = DisplayMetricsUtils.dipTopx(mContext, 15);
			int width = DisplayMetricsUtils.getScreenWidth(mContext) - 2
					* pandding;
			int height = width / 5 * 3;
			WindowManager.LayoutParams params = dialog.getWindow()
					.getAttributes();
			params.height = height;
			params.width = width;
			dialog.getWindow().setAttributes(params);
			TextView titleTv = view
					.findViewById(R.id.id_announcement_title);
			TextView contentTv = view
					.findViewById(R.id.id_announcement_content);
			ImageView imageView = view
					.findViewById(R.id.id_announcement_close);
			Button button = view
					.findViewById(R.id.id_announcement_confirm);
			imageView.setOnClickListener( v -> dialog.dismiss() );

			button.setOnClickListener( v -> dialog.dismiss() );
			
			titleTv.setText(title);
			contentTv.setText(content);

		}
	}

}
