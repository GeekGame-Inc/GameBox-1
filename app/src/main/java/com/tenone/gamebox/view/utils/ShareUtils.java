/** 
 * Project Name:GameBox 
 * File Name:ShareUtils.java 
 * Package Name:com.tenone.gamebox.view.utils 
 * Date:2017-5-9����3:00:10 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.utils;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.tenone.gamebox.share.ShareModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class ShareUtils implements PlatformActionListener {
	private static ShareUtils shareUtils;
	private Context mContext;
	
	private Intent intent = null;

	private static final String tencent_package = "com.tencent.mm";
	private static final String tencent_class = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
	private static final String send_multiple = "android.intent.action.SEND_MULTIPLE";
	private static final String type = "image/*";//
	private static final String stream = "android.intent.extra.STREAM";

	private static final String qzone_package = "com.qzone";
	private static final String qzone_class = "com.qzonex.module.operation.ui.QZonePublishMoodActivity";
	private static final String extra_text = "android.intent.extra.TEXT";

	private List<String> pathArray = new ArrayList<String>();

	public static ShareUtils getInstance(Context context, int goodsId,
			String details) {
		px = DisplayMetricsUtils.getScreenWidth(context) / 4;
		if (shareUtils == null) {
			shareUtils = new ShareUtils();
		}
		shareUtils.mContext = context;
		return shareUtils;
	}

	public void sharePics(String text, int who) {
		ArrayList<Uri> list = new ArrayList<Uri>();
		for (int i = 0; i < pathArray.size(); i++) {
			String path = pathArray.get(i);
			if (!TextUtils.isEmpty(path)) {
				File file = new File(path);
				list.add(Uri.fromFile(file));
			}
		}
		switch (who) {
		case 0:
			shareMultiplePictureToWechatMoments(text, list);
			break;
		case 1:
			shareMultiplePictureToQZone(text, list);
			break;
		}
		list = null;
	}

	public void shareMultiplePictureToWechatMoments(String paramString,
			ArrayList<Uri> paramArrayOfFile) {
		try {
			intent = new Intent();
			intent.setComponent(new ComponentName(tencent_package,
					tencent_class));
			intent.setAction(send_multiple);
			intent.putExtra("Kdescription", paramString);
			intent.setType(type);
			intent.putParcelableArrayListExtra(stream, paramArrayOfFile);
			mContext.startActivity(intent);
		} catch (ActivityNotFoundException localActivityNotFoundException) {
		}
	}

	public void shareMultiplePictureToQZone(String paramString,
			ArrayList<Uri> paramArrayOfFile) {
		try {
			ComponentName localComponentName = new ComponentName(qzone_package,
					qzone_class);
			intent = new Intent(send_multiple);
			intent.setType(type);
			intent.putExtra(extra_text, paramString);
			intent.putParcelableArrayListExtra(stream, paramArrayOfFile);
			intent.setComponent(localComponentName);
			mContext.startActivity(intent);
		} catch (ActivityNotFoundException localActivityNotFoundException) {
		}
	}
	public ShareModel initModel(String imageUrl, String content, String title,
			String url, String site, String siteUrl) {
		ShareModel model = new ShareModel();
		model.setImageUrl(imageUrl);
		model.setText(content);
		model.setTitle(title);
		model.setUrl(url);
		model.setSite(site);
		model.setSiteUrl(siteUrl);
		return model;
	}

	View view;

	static int px = 0;

	private void showToast(String str) {
		Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {

	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {

	}

}
