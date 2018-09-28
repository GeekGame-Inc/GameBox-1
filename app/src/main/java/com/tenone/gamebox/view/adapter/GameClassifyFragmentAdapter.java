/** 
 * Project Name:GameBox 
 * File Name:GameNewFragmentAdapter.java 
 * Package Name:com.tenone.gamebox.view.adapter 
 * Date:2017-3-9����4:16:08 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.GameClassify;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class GameClassifyFragmentAdapter extends BaseAdapter {
	/* ����Դ */
	List<GameClassify> items = new ArrayList<GameClassify>();
	Context mContext;
	LayoutInflater mInflater;

	public GameClassifyFragmentAdapter(Context cxt, List<GameClassify> list) {
		this.items = list;
		this.mContext = cxt;
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
		ClassifyViewHolder classifyViewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.item_game_classify_gridview, parent, false);
			classifyViewHolder = new ClassifyViewHolder(convertView);
			convertView.setTag(classifyViewHolder);
		} else {
			classifyViewHolder = (ClassifyViewHolder) convertView.getTag();
		}
		GameClassify gameClassify = items.get(position);
		ImageLoadUtils.loadNormalImg(classifyViewHolder.classifyImage,
				mContext, gameClassify.getClassifyImgUrl());
		classifyViewHolder.classifyNameTv.setText(gameClassify
				.getClassifyName());
		return convertView;
	}

	class ClassifyViewHolder {
		/* ͼ�� */
		ImageView classifyImage;
		/* ���� */
		TextView classifyNameTv;

		public ClassifyViewHolder(View convertView) {
			initView(convertView);
		}

		private void initView(View convertView) {
			classifyImage = convertView
					.findViewById(R.id.id_game_classify_img);
			classifyNameTv = convertView
					.findViewById(R.id.id_game_classify_name);
		}
	}
}
