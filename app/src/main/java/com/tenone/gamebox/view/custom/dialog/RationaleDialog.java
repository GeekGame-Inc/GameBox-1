package com.tenone.gamebox.view.custom.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

public class RationaleDialog extends AlertDialog {

	protected RationaleDialog(@NonNull Context context) {
		super( context );
	}

	public RationaleDialog(@NonNull Context context, int themeResId) {
		super( context, themeResId );
	}

	public static class RationaleBuilder extends Builder {
		private Context context;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private String title;
		private RationaleDialog dialog;
		private DialogInterface.OnClickListener onClickListener;
		private int screenHeight;

		public RationaleBuilder(Context context) {
			super( context );
			this.context = context;
			screenHeight = DisplayMetricsUtils.getScreenHeight( context );
		}

		public RationaleBuilder setMessage(String message) {
			this.message = message;
			return this;
		}

		public RationaleBuilder setPositiveButtonText(String positiveButtonText) {
			this.positiveButtonText = positiveButtonText;
			return this;
		}

		public RationaleBuilder setNegativeButtonText(String negativeButtonText) {
			this.negativeButtonText = negativeButtonText;
			return this;
		}

		public RationaleBuilder setTitle(String title) {
			this.title = title;
			return this;
		}

		public RationaleBuilder setOnClickListener(OnClickListener onClickListener) {
			this.onClickListener = onClickListener;
			return this;
		}

		public RationaleDialog create() {
			/*dialog = new RationaleDialog( context, R.style.dialog );*/
			dialog = new RationaleDialog( context );
			View view = LayoutInflater.from( context ).inflate( R.layout.dialog_rationale, null, false );
			if (!TextUtils.isEmpty( title )) {
				((TextView) view.findViewById( R.id.id_rationale_title )).setText( title );
			}
			if (!TextUtils.isEmpty( message )) {
				((TextView) view.findViewById( R.id.id_rationale_message )).setText( message );
			}
			if (!TextUtils.isEmpty( negativeButtonText )) {
				((TextView) view.findViewById( R.id.id_rationale_negativeButton )).setText( negativeButtonText );
			}
			if (!TextUtils.isEmpty( positiveButtonText )) {
				((TextView) view.findViewById( R.id.id_rationale_positiveButton )).setText( positiveButtonText );
			}
			view.findViewById( R.id.id_rationale_positiveButton ).setOnClickListener( v -> {
				if (onClickListener != null) {
					onClickListener.onClick( dialog, DialogInterface.BUTTON_POSITIVE );
				}
				dismiss();
			} );
			view.findViewById( R.id.id_rationale_negativeButton ).setOnClickListener( v -> {
				if (onClickListener != null) {
					onClickListener.onClick( dialog, DialogInterface.BUTTON_NEGATIVE );
				}
				dismiss();
			} );
			dialog.show();
			dialog.setContentView( view );
			dialog.setCanceledOnTouchOutside( false );
			Window window = dialog.getWindow();
			window.setBackgroundDrawable( ContextCompat.getDrawable( context, R.drawable.radiu_line_border ) );
			WindowManager.LayoutParams params = window.getAttributes();
			params.width = DisplayMetricsUtils.getScreenWidth( context ) - DisplayMetricsUtils.dipTopx( context, 30 );
			params.height = screenHeight / (screenHeight > 1920 ? 4 : 3);
			dialog.getWindow().setAttributes( params );
			return dialog;
		}

		public void dismiss() {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
		}
	}
}
