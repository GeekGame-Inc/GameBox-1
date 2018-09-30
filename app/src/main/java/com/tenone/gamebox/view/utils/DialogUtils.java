package com.tenone.gamebox.view.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tenone.gamebox.mode.listener.DeleteDialogConfrimListener;
import com.tenone.gamebox.view.custom.dialog.AddDownloadDialog;
import com.tenone.gamebox.view.custom.dialog.AddDownloadDialog.AddDownloadBuilder;
import com.tenone.gamebox.view.custom.dialog.DeleteDialog;
import com.tenone.gamebox.view.custom.dialog.DeleteDialog.Buidler;
import com.tenone.gamebox.view.custom.dialog.RationaleDialog;

public class DialogUtils {
	// 添加到下载任务管理提示
	static AddDownloadBuilder addDownloadBuilder;

	public static void showAddDownloadDialog(Context context, String message,
																					 long time) {
		if (addDownloadBuilder == null) {
			addDownloadBuilder = new AddDownloadDialog.AddDownloadBuilder(
					context );
		}
		addDownloadBuilder.setMessage( message );
		addDownloadBuilder.showDialog();
		Message msg = new Message();
		msg.what = 0;
		handler.sendMessageDelayed( msg, time );
	}

	static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:
					if (addDownloadBuilder != null) {
						addDownloadBuilder.dismiss();
					}
					break;
				default:
					break;
			}
		}

	};

	/* 显示不是wifi */
	public static void showNotWifiDialog(Context cxt,
																			 DeleteDialogConfrimListener listener) {
		Buidler deleteBuidler = new DeleteDialog.Buidler( cxt );
		deleteBuidler.setMessage( "\u60a8\u6b63\u5904\u4e8e\u975eWIFI\u7f51\u7edc,\u5efa\u8bae\u5230WIFI\u73af\u5883\u4e0b\u8f7d" );
		deleteBuidler.setCancleText( "\u7b97\u4e86,\u8fd8\u662f\u7b49WIFI\u5427" );
		deleteBuidler.setConfirmText( "\u7ee7\u7eed\u4e0b\u8f7d,\u6211\u662f\u571f\u8c6a" );
		deleteBuidler.setConfrimListener( listener );
		deleteBuidler.createDialog();
	}

	public static void showConfirmDialog(Context cxt,
																			 DeleteDialogConfrimListener listener, String message, String cancle, String confirm) {
		if (cxt instanceof Activity) {
			if (cxt == null || ((Activity) cxt).isFinishing()) {
				return;
			}
		}
		Buidler deleteBuidler = new DeleteDialog.Buidler( cxt );
		deleteBuidler.setMessage( message );
		deleteBuidler.setCancleText( cancle );
		deleteBuidler.setConfirmText( confirm );
		deleteBuidler.setConfrimListener( listener );
		deleteBuidler.createDialog();
	}

	public static RationaleDialog.RationaleBuilder showRationDialog(Context context, DialogInterface.OnClickListener listener, String... parms) {
		RationaleDialog.RationaleBuilder builder = new RationaleDialog.RationaleBuilder( context );
		if (parms != null && parms.length > 0) {
			builder.setTitle( parms[0] );
			try {
				builder.setMessage( parms[1] );
			} catch (IndexOutOfBoundsException e) {
				Log.i( "RationaleDialog", "has no message" );
			}
			try {
				builder.setPositiveButtonText( parms[2] );
			} catch (IndexOutOfBoundsException e) {
				Log.i( "RationaleDialog", "has no positive text" );
			}
			try {
				builder.setNegativeButtonText( parms[3] );
			} catch (IndexOutOfBoundsException e) {
				Log.i( "RationaleDialog", "has no negative text" );
			}
		}
		builder.setOnClickListener( listener );
		builder.create();
		return builder;
	}

}
