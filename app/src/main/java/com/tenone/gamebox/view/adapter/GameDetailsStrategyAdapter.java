/** 
 * Project Name:GameBox 
 * File Name:GameDetailsStrategyAdapter.java 
 * Package Name:com.tenone.gamebox.view.adapter 
 * Date:2017-5-15����11:52:21 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.StrategyMode;

import java.util.ArrayList;
import java.util.List;

public class GameDetailsStrategyAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {

	List<StrategyMode> items = new ArrayList<StrategyMode>();

	Context mContext;
	LayoutInflater mInflater;
	OnItemClickListener itemClickListener;
	public GameDetailsStrategyAdapter(Context cxt, List<StrategyMode> list) {
		this.items = list;
		this.mContext = cxt;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder arg0, int position) {
	/*	StrategyHolder holder = (StrategyHolder) arg0;
		StrategyMode mode = items.get(position);
		holder.numTv.setText("(" + mode.getCommentNum() + ")");
		holder.sourceTv.setText("����:" + mode.getStrategySource());
		holder.writerTv.setText("����:" + mode.getWriter());
		holder.timeTv.setText(mode.getTime());
		holder.strategyNameTv.setText(mode.getStrategyName());
		String url = mode.getStrategyImgUrl();
		if (!TextUtils.isEmpty(url)) {
			holder.imageView.setVisibility(View.VISIBLE);
			ImageLoadUtils.loadNormalImg(holder.imageView, mContext,
					mode.getStrategyImgUrl());
		} else {
			holder.imageView.setVisibility(View.GONE);
		}*/
		return;
	}

	@Override
	public StrategyHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View view = mInflater.inflate(R.layout.item_strategy_list, arg0, false);
		StrategyHolder strategyHolder = new StrategyHolder(view);
		return strategyHolder;
	}

	public static class StrategyHolder extends RecyclerView.ViewHolder implements OnClickListener {

		TextView strategyNameTv, sourceTv, timeTv, numTv, writerTv;
		ImageView imageView;
		LinearLayout linearLayout;

		void initView(View v) {
			strategyNameTv = (TextView) findViewById(v, R.id.id_strategy_nameTv);
			sourceTv = (TextView) findViewById(v, R.id.id_strategy_sourceTv);
			timeTv = (TextView) findViewById(v, R.id.id_strategy_timeTv);
			numTv = (TextView) findViewById(v, R.id.id_strategy_num);
			writerTv = (TextView) findViewById(v, R.id.id_strategy_writerTv);
			imageView = (ImageView) findViewById(v, R.id.id_strategy_img);
			linearLayout = (LinearLayout) findViewById(v,
					R.id.id_strategy_comment);
			v.setOnClickListener(this);
		}

		View findViewById(View v, int id) {
			return v.findViewById(id);
		}

		public StrategyHolder(View v) {
			super(v);
			initView(v);
		}

		@Override
		public void onClick(View v) {
		}
	}
	
	public void setOnItemClickListener(OnItemClickListener o){
		this.itemClickListener = o;
	}

	public interface OnItemClickListener{
		void onItemClickListener(int position, StrategyMode strategyMode);
	}
	
}
