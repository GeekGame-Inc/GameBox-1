/** 
 * Project Name:GameBox 
 * File Name:GameDetailsRecommendAdapter.java 
 * Package Name:com.tenone.gamebox.view.adapter 
 * Date:2017-4-11����4:36:32 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;

/**
 * ��Ϸ������������Ϸ�Ƽ������� ClassName:GameDetailsRecommendAdapter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-11 ����4:36:32 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class SearchGameListAdapter extends BaseAdapter {

	private List<GameModel> items;
	private Context mContext;
	private LayoutInflater mInflater;

	public SearchGameListAdapter(List<GameModel> list, Context cxt) {
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
		SearchGameListHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_search_game, parent,
					false);
			holder = new SearchGameListHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (SearchGameListHolder) convertView.getTag();
		}
		GameModel gameModel = items.get(position);
		// ImageLoadUtils.loadNormalImg(holder.imageView, mContext,
		// gameModel.getImgUrl());
		new MyTask(holder.imageView).execute(gameModel.getImgUrl());
		holder.textView.setText(gameModel.getName());
		return convertView;
	}

	class MyTask extends AsyncTask<String, ImageView, Void> {
		ImageView imageView;

		public MyTask(ImageView v) {
			this.imageView = v;
		}

		@Override
		protected Void doInBackground(String... params) {
			Message message = new Message();
			message.obj = imageView;
			Bundle bundle = new Bundle();
			bundle.putString("url", params[0]);
			message.setData(bundle);
			handler.sendMessage(message);
			return null;
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ImageView imageView = (ImageView) msg.obj;
			String url = msg.getData().getString("url");
			ImageLoadUtils.loadNormalImg(imageView, mContext, url);
		}
	};

	class SearchGameListHolder {
		ImageView imageView;
		TextView textView;

		public SearchGameListHolder(View v) {
			initView(v);
		}

		private void initView(View v) {
			imageView = v.findViewById(R.id.id_searchGame_img);
			textView = v.findViewById(R.id.id_searchGame_text);
		}
	}
}
