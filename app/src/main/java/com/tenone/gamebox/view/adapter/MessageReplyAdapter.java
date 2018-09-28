/** 
 * Project Name:GameBox 
 * File Name:MessageReplyAdapter.java 
 * Package Name:com.tenone.gamebox.view.adapter 
 * Date:2017-3-23����10:20:16 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.ReplyMode;
import com.tenone.gamebox.view.utils.CharSequenceUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.TimeUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageReplyAdapter extends BaseExpandableListAdapter {
	List<ReplyMode> items = new ArrayList<ReplyMode>();
	Context mContext;
	LayoutInflater mInflater;

	public MessageReplyAdapter(List<ReplyMode> list, Context cxt) {
		this.items = list;
		this.mContext = cxt;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getGroupCount() {
		return items.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return items.get(groupPosition).getReplyMode() == null ? 0 : items
				.get(groupPosition).getReplyMode().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return items.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return items.get(groupPosition).getReplyMode() == null ? null : items
				.get(groupPosition).getReplyMode().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return groupPosition * childPosition + groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ReplyHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_reply_group, parent,
					false);
			holder = new ReplyHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ReplyHolder) convertView.getTag();
		}
		ReplyMode mode = items.get(groupPosition);
		List<ReplyMode> array = mode.getReplyMode();

		if (array != null && !array.isEmpty()) {
			holder.moreTv.setVisibility(View.VISIBLE);
			holder.moreTv.setText(isExpanded ? "\u6536\u8d77" : "\u5c55\u5f00" );
		} else {
			holder.moreTv.setVisibility(View.GONE);
		}
		String url = mode.getImgUrl();
		ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();
		if (!TextUtils.isEmpty(url)) {
			params.width = DisplayMetricsUtils.dipTopx(mContext, 80);
			params.height = DisplayMetricsUtils.dipTopx(mContext, 80);
			ImageLoadUtils.loadNormalImg(holder.imageView, mContext, url);
		}else {
			params.width = DisplayMetricsUtils.dipTopx(mContext, 0);
			params.height = DisplayMetricsUtils.dipTopx(mContext, 0);
		}
		holder.imageView.setLayoutParams(params);
		String nick = mode.getNick();
		String replyNick = mode.getReplyNick();
		String title = nick + "\u56de\u590d" + replyNick + ":";
		SpannableStringBuilder builder = CharSequenceUtils
				.StringInterceptionChangeRed(title, replyNick, nick,
						Color.BLUE, Color.BLUE);
		holder.titleTv.setText(builder);
		holder.timeTv.setText(TimeUtils.formatPhotoDate(mode.getTime()));
		holder.contentTv.setText("  " + mode.getContent());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ReplyHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_reply_group, parent,
					false);
			holder = new ReplyHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ReplyHolder) convertView.getTag();
		}
		holder.layout.setPadding(DisplayMetricsUtils.dipTopx(mContext, 38), 0,
				0, 0);
		ReplyMode mode = items.get(groupPosition).getReplyMode()
				.get(childPosition);
		String url = mode.getImgUrl();
		ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();
		if (!TextUtils.isEmpty(url)) {
			params.width = DisplayMetricsUtils.dipTopx(mContext, 80);
			params.height = DisplayMetricsUtils.dipTopx(mContext, 80);
			ImageLoadUtils.loadNormalImg(holder.imageView, mContext, url);
		}else {
			params.width = DisplayMetricsUtils.dipTopx(mContext, 0);
			params.height = DisplayMetricsUtils.dipTopx(mContext, 0);
		}
		holder.imageView.setLayoutParams(params);
		String nick = mode.getNick();
		String replyNick = mode.getReplyNick();
		String title = nick + "\u56de\u590d" + replyNick + ":";
		SpannableStringBuilder builder = CharSequenceUtils
				.StringInterceptionChangeRed(title, replyNick, nick,
						Color.BLUE, Color.BLUE);
		holder.titleTv.setText(builder);
		holder.timeTv.setText(TimeUtils.formatPhotoDate(mode.getTime()));
		holder.contentTv.setText("  " + mode.getContent());
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class ReplyHolder {
		TextView titleTv, contentTv, timeTv, moreTv;
		ImageView imageView;
		RelativeLayout layout;

		public ReplyHolder(View v) {
			initView(v);
		}

		private void initView(View v) {
			titleTv = v.findViewById(R.id.id_reply_group_title);
			contentTv = v.findViewById(R.id.id_reply_group_content);
			timeTv = v.findViewById(R.id.id_reply_group_time);
			moreTv = v.findViewById(R.id.id_reply_group_more);
			imageView = v.findViewById(R.id.id_reply_gruop_img);
			layout = v
					.findViewById(R.id.id_reply_group_layout);
		}
	}
}
