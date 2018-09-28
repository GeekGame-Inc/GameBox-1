/**
 * Project Name:GameBox
 * File Name:DialogUtils.java
 * Package Name:com.tenone.gamebox.view.utils
 * Date:2017-4-6����3:53:12
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.utils;

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

/**
 * ClassName:DialogUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-6 ����3:53:12 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */
public class DialogUtils {
	// ��ӵ��������������ʾ
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
		public void handleMessage(Message msg) {
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

	/* ��ʾ����wifi */
	public static void showNotWifiDialog(Context cxt,
																			 DeleteDialogConfrimListener listener) {
		Buidler deleteBuidler = new DeleteDialog.Buidler( cxt );
		deleteBuidler.setMessage( "�������ڷ�WIFI����,���鵽WIFI��������" );
		deleteBuidler.setCancleText( "����,���ǵ�WIFI��" );
		deleteBuidler.setConfirmText( "��������,��������" );
		deleteBuidler.setConfrimListener( listener );
		deleteBuidler.createDialog();
	}

	public static void showConfirmDialog(Context cxt,
																			 DeleteDialogConfrimListener listener, String message, String cancle, String confirm) {
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
