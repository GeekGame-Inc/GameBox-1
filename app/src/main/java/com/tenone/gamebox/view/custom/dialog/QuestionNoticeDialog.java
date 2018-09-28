package com.tenone.gamebox.view.custom.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OnConfirmListener;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

public class QuestionNoticeDialog extends AlertDialog {

	protected QuestionNoticeDialog(Context context) {
		super( context );
	}


	public static class QuestionNoticeBuilder extends Builder {

		private Context context;
		private OnConfirmListener onConfirmListener;
		private String titleTxt;
		private String confirmTxt;
		private String[] protocols;
		private String[] notices;

		public QuestionNoticeBuilder(Context context) {
			super( context );
			this.context = context;
		}

		public QuestionNoticeBuilder setOnConfirmListener(OnConfirmListener onConfirmListener) {
			this.onConfirmListener = onConfirmListener;
			return this;
		}

		public QuestionNoticeBuilder setTitleTxt(String titleTxt) {
			this.titleTxt = titleTxt;
			return this;
		}

		public QuestionNoticeBuilder setConfirmTxt(String confirmTxt) {
			this.confirmTxt = confirmTxt;
			return this;
		}

		public QuestionNoticeBuilder setProtocols(String[] protocols) {
			if (protocols == null || protocols.length <= 0) {
				protocols = new String[]{"理性提问，中肯发言；","问的清楚，答的明白。"};
			}
			this.protocols = protocols;
			return this;
		}

		public QuestionNoticeBuilder setNotices(String[] notices) {
			if (notices == null || notices.length <= 0) {
				notices = new String[]{"每日单个游戏最多发起2条提问；","每日前10条回复均可获得50金币奖励。"};
			}
			this.notices = notices;
			return this;
		}

		public QuestionNoticeDialog create() {
			QuestionNoticeDialog dialog = new QuestionNoticeDialog( context );
			View contentView = LayoutInflater.from( context ).inflate(
					R.layout.dialog_question_notice, null );
			dialog.show();
			dialog.setCancelable( false );
			dialog.setCanceledOnTouchOutside( false );
			dialog.setContentView( contentView );
			Window window = dialog.getWindow();
			window.setBackgroundDrawable( ContextCompat.getDrawable( context, R.drawable.radiu_line_border ) );
			WindowManager.LayoutParams params = window.getAttributes();
			params.width = DisplayMetricsUtils.getScreenWidth( context ) - DisplayMetricsUtils.dipTopx( context, 30 );
			params.height = DisplayMetricsUtils.getScreenHeight( context ) / 2;
			dialog.getWindow().setAttributes( params );
			if (!TextUtils.isEmpty( titleTxt )) {
				TextView messageTv = contentView.findViewById( R.id.id_dialog_title );
				messageTv.setText( titleTxt );
			}
			if (!TextUtils.isEmpty( confirmTxt )) {
				TextView textView = contentView.findViewById( R.id.id_dialog_know );
				textView.setText( confirmTxt );
			}
			int dp3 = DisplayMetricsUtils.dipTopx( context, 3 );
			if (protocols != null && protocols.length > 0) {
				LinearLayout linearLayout = contentView.findViewById( R.id.id_dialog_protocol );
				linearLayout.removeAllViews();
				for (String protocol : protocols) {
					TextView textView = new TextView( context );
					textView.setText( protocol );
					textView.setTextColor( ContextCompat.getColor( context, R.color.defultTextColor ) );
					textView.setTextSize( TypedValue.COMPLEX_UNIT_SP, 12 );
					textView.setPadding( 0, dp3, 0, 0 );
					linearLayout.addView( textView );
				}
			}

			if (notices != null && notices.length > 0) {
				LinearLayout linearLayout = contentView.findViewById( R.id.id_dialog_notice );
				linearLayout.removeAllViews();
				for (int i = 0; i < notices.length; i++) {
					TextView textView = new TextView( context );
					textView.setText( (i + 1) + "." + notices[i] );
					textView.setTextColor( ContextCompat.getColor( context, R.color.defultTextColor ) );
					textView.setTextSize( TypedValue.COMPLEX_UNIT_SP, 12 );
					textView.setPadding( 0, dp3, 0, 0 );
					linearLayout.addView( textView );
				}
			}
			contentView.findViewById( R.id.id_dialog_know ).setOnClickListener( v -> {
				if (onConfirmListener != null) {
					onConfirmListener.onConfirm();
				}
				dialog.dismiss();
			} );
			return dialog;
		}
	}
}
