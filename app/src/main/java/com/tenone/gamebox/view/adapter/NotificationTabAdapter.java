/** 
 * Project Name:GameBox 
 * File Name:NotificationTabAdapter.java 
 * Package Name:com.tenone.gamebox.view.adapter 
 * Date:2017-5-9����1:46:58 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.OpenServiceNotificationMode;
import com.tenone.gamebox.view.utils.TimeUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;

public class NotificationTabAdapter extends BaseAdapter {
	Context mContext;
	List<OpenServiceNotificationMode> items ;
	LayoutInflater mInflater;

	public NotificationTabAdapter(Context cxt,
			List<OpenServiceNotificationMode> list) {
		this.mContext = cxt;
		this.items = list;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NotificationTabHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_notification_list,
					parent, false);
			holder = new NotificationTabHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (NotificationTabHolder) convertView.getTag();
		}
		OpenServiceNotificationMode notificationMode = items.get(position);
		ImageLoadUtils.loadNormalImg(holder.imageView, mContext,
				notificationMode.getImgUrl());
		holder.nameTv.setText(notificationMode.getGameName());

		holder.contentTv.setText(notificationMode.getGameName() + " "
				+ notificationMode.getServiceId() + " \u670d,\u5f00\u670d\u5566!!! \u5feb\u6765\u73a9\u513f\u5427\u3002" );

		String time = notificationMode.getTime();
		if (!TextUtils.isEmpty(time)) {
			long t = Long.valueOf(time).longValue() * 1000;
			time = TimeUtils.formatDateMin(t);
		}
		holder.timeTv.setText(time);
		
		return convertView;
	}

	class NotificationTabHolder {
		ImageView imageView;
		TextView nameTv, timeTv, contentTv;
		CheckBox checkBox;

		public NotificationTabHolder(View v) {
			initView(v);
		}

		private void initView(View v) {
			imageView = v.findViewById(R.id.id_notificaiton_img);
			nameTv = v.findViewById(R.id.id_notificaiton_gameName);
			timeTv = v.findViewById(R.id.id_notificaiton_time);
			contentTv = v.findViewById(R.id.id_notificaiton_content);
			checkBox = v.findViewById(R.id.id_notificaiton_check);
		}
	}

}
