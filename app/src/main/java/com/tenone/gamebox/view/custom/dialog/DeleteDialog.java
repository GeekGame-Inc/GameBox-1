

package com.tenone.gamebox.view.custom.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.DeleteDialogConfrimListener;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;


@SuppressLint("InflateParams")
public class DeleteDialog extends AlertDialog {

    protected DeleteDialog(Context context, int theme) {
        super( context, theme );
    }

    protected DeleteDialog(Context context) {
        super( context );
    }

    public static class Buidler {
        Context mContext;
        String message;
        String cancleText;
        String confirmText;

        DeleteDialogConfrimListener confrimListener;

        public Buidler(Context cxt) {
            this.mContext = cxt;
        }

        public Buidler setMessage(String message) {
            this.message = message;
            return this;
        }

        public Buidler setCancleText(String cancleText) {
            this.cancleText = cancleText;
            return this;
        }

        public Buidler setConfirmText(String confirmText) {
            this.confirmText = confirmText;
            return this;
        }

        public Buidler setMessage(int rId) {
            this.message = mContext.getResources().getString( rId );
            return this;
        }

        public Buidler setCancleText(int rId) {
            this.cancleText = mContext.getResources().getString( rId );
            return this;
        }

        public Buidler setConfirmText(int rId) {
            this.confirmText = mContext.getResources().getString( rId );
            return this;
        }

        public Buidler setConfrimListener(
                DeleteDialogConfrimListener confrimListener) {
            this.confrimListener = confrimListener;
            return this;
        }

        public void createDialog() {
            final DeleteDialog dialog = new DeleteDialog( mContext );
            View contentView = LayoutInflater.from( mContext ).inflate(
                    R.layout.dialog_delete, null );
            dialog.show();
            dialog.setContentView( contentView );
            TextView messageTv = contentView
                    .findViewById( R.id.id_deleteDialog_title );
            if (!TextUtils.isEmpty( message )) {
                messageTv.setText( message );
            }
            Button cancle = contentView
                    .findViewById( R.id.id_deleteDialog_cancle );
            if (!TextUtils.isEmpty( cancleText )) {
                cancle.setText( cancleText );
            }
            cancle.setOnClickListener( v -> dialog.dismiss() );
            Button confrimBt = contentView
                    .findViewById( R.id.id_deleteDialog_confirm );
            if (!TextUtils.isEmpty( confirmText )) {
                confrimBt.setText( confirmText );
            }
            confrimBt.setOnClickListener( v -> {
                if (confrimListener != null) {
                    confrimListener.onConfrimClick( dialog );
                }
            } );
            LayoutParams params = messageTv.getLayoutParams();
            params.width = DisplayMetricsUtils.getScreenWidth( mContext )
                    - DisplayMetricsUtils.dipTopx( mContext, 40 );
            params.height = DisplayMetricsUtils.getScreenHeight( mContext ) / 7
                    - confrimBt.getHeight();
            messageTv.setLayoutParams( params );
        }
    }
}
