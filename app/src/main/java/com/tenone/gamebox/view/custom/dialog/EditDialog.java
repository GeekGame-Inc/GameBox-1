/**
 * Project Name:GameBox
 * File Name:EditDialog.java
 * Package Name:com.tenone.gamebox.view.custom
 * Date:2017-3-21����10:10:00
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.custom.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.EditDialogConfrimListener;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.utils.SpUtil;

@SuppressLint("InflateParams")
public class EditDialog extends AlertDialog {

    protected EditDialog(Context context, int theme) {
        super( context, theme );
    }

    public EditDialog(Context context) {
        super( context );
    }

    public static class EditDialogBuilder {
        Context mContext;
        String hint;
        String title;
        String action;
        EditDialogConfrimListener confrimListener;
        CustomizeEditText editText;
        TextView titleTv;
        EditDialog dialog;

        public EditDialogBuilder(Context cxt) {
            this.mContext = cxt;
        }

        public EditDialogBuilder setHint(String hint) {
            this.hint = hint;
            return this;
        }

        public EditDialogBuilder setHint(int rId) {
            this.hint = mContext.getResources().getString( rId );
            return this;
        }

        public EditDialogBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public EditDialogBuilder setTitle(int rId) {
            this.title = mContext.getResources().getString( rId );
            return this;
        }

        public EditDialogBuilder setAction(String action) {
            this.action = action;
            return this;
        }

        public EditDialogBuilder setAction(int rId) {
            this.action = mContext.getResources().getString( rId );
            return this;
        }

        public EditDialogBuilder setConfrimListener(
                EditDialogConfrimListener confrimListener) {
            this.confrimListener = confrimListener;
            return this;
        }

        public CustomizeEditText getEditText() {
            return this.editText;
        }

        public void showDialog() {
            if (dialog == null) {
                dialog = new EditDialog( mContext );
            }
            View contentView = LayoutInflater.from( mContext ).inflate(
                    R.layout.dialog_edit, null );
            dialog.show();
            dialog.setContentView( contentView );
            editText = contentView
                    .findViewById( R.id.id_editDialog_editText );
            titleTv = contentView
                    .findViewById( R.id.id_editDialog_title );
            titleTv.setText( title );
            if (TextUtils.isEmpty( SpUtil.getNick() )) {
                editText.setText( SpUtil.getNick() );
                editText.setSelection( editText.getText().toString().length() );
            }
            if (!TextUtils.isEmpty( hint )) {
                editText.setHint( hint );
            }
            dialog.getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM );
            Button confirm = contentView
                    .findViewById( R.id.id_editDialog_confirm );
            Button cancle = contentView
                    .findViewById( R.id.id_editDialog_cancle );
            confirm.setOnClickListener( v -> {
								String text = editText.getText().toString();
								if (!TextUtils.isEmpty( text )) {
										if (confrimListener != null) {
												confrimListener.onConfrimClick( action, text );
										}
										dialog.dismiss();
								} else {
										editText.setError( "������Ҫ�޸ĵ�����" );
								}
						} );
            cancle.setOnClickListener( v -> dialog.dismiss() );
        }

        public void dismiss() {
            if (dialog != null)
                dialog.dismiss();
            dialog = null;
        }
    }
}
