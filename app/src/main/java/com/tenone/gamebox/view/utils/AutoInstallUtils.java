package com.tenone.gamebox.view.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.tenone.gamebox.view.base.Configuration;

import java.io.File;

public class AutoInstallUtils {
	@SuppressWarnings("unused")
	private static String mUrl;
	private static Context mContext;

	public static void setUrl(String url) {
		mUrl = url;
	}

	public static void install(Context context, String apkName) {
		if (context == null || TextUtils.isEmpty(apkName)) {
			return;
		}
		mContext = context;
		String str = Configuration.getDownloadpath() + "/" + apkName;
		if (TextUtils.isEmpty(str)) {
			return;
		}
		File file = new File(str);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		if (Build.VERSION.SDK_INT >= 24) {
			Uri apkUri = FileProvider.getUriForFile(context,
					"com.tenone.gamebox.fileprovider", file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.setDataAndType(apkUri,
					"application/vnd.android.package-archive");
		} else {
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
		}
		mContext.startActivity(intent);
	}
}
