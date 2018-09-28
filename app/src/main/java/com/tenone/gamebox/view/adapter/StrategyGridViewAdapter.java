/** 
 * Project Name:GameBox 
 * File Name:StrategyGridViewAdapter.java 
 * Package Name:com.tenone.gamebox.view.adapter 
 * Date:2017-3-29����5:17:57 
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
import com.tenone.gamebox.mode.mode.StrategyMode;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:StrategyGridViewAdapter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-29 ����5:17:57 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class StrategyGridViewAdapter extends BaseAdapter {
	List<StrategyMode> items = new ArrayList<StrategyMode>();
	Context context;
	LayoutInflater mInflater;

	public StrategyGridViewAdapter(List<StrategyMode> list, Context cxt) {
		this.context = cxt;
		this.items = list;
		this.mInflater = LayoutInflater.from(context);
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
	/*	StrategyGridViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_strategy_grid,
					parent, false);
			holder = new StrategyGridViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (StrategyGridViewHolder) convertView.getTag();
		}
		StrategyMode mode = items.get(position);
		holder.textView.setText(mode.getStrategySource());
		ImageLoadUtils.loadNormalImg(holder.imageView,context,mode.getStrategyImgUrl());*/
		return convertView;
	}

	class StrategyGridViewHolder {
		ImageView imageView;
		TextView textView;

		public StrategyGridViewHolder(View v) {
			initView(v);
		}

		private void initView(View v) {
			imageView = (ImageView) findViewById(v, R.id.id_strategyGrid_img);
			textView = (TextView) findViewById(v, R.id.id_strategyGrid_text);
		}

		private View findViewById(View v, int rId) {
			return v.findViewById(rId);
		}
	}

}
