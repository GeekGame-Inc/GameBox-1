/**
 * Project Name:GameBox
 * File Name:StrategyAdapter.java
 * Package Name:com.tenone.gamebox.view.adapter
 * Date:2017-3-29����4:30:06
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OnStrategyItemClickListener;
import com.tenone.gamebox.mode.mode.StrategyMode;
import com.tenone.gamebox.view.custom.MyGridView;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class StrategyAdapter extends RecyclerView.Adapter<StrategyAdapter.StrategyHolder> {

	private List<StrategyMode> items;

	private Context mContext;
	private LayoutInflater mInflater;
	private OnStrategyItemClickListener onStrategyItemClickListener;
	private int[] rids = {R.drawable.h_icon_yuedu, R.drawable.selector_good, R.drawable.selector_disgood,
			R.drawable.icon_dynamic_share};
	private OperationListener listener;

	public StrategyAdapter(List<StrategyMode> list, Context cxt) {
		this.items = list;
		this.mContext = cxt;
		this.mInflater = LayoutInflater.from( mContext );
	}

	@Override
	public StrategyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View convertView = mInflater.inflate( R.layout.item_strategy,
				parent, false );
		StrategyHolder holder = new StrategyHolder( convertView );
		return holder;
	}

	@Override
	public void onBindViewHolder(StrategyHolder holder, int position) {
		StrategyMode model = items.get( position );
		holder.writerTv.setText( "\u4f5c\u8005:" + model.getWriter() );
		holder.timeTv.setText( model.getTime() );
		holder.titleTv.setText( model.getStrategyName() );
		ImageLoadUtils.loadNormalImg( holder.imageView, mContext, model.getStrategyImgUrl() );
		List<String> items = new ArrayList<String>();
		items.add( model.getViewCounts() + "" );
		items.add( model.getLikes() + "" );
		items.add( model.getDisLikes() + "" );
		items.add( model.getShareCounts() + "" );
		TextViewGridAdapter adapter = new TextViewGridAdapter( mContext, items );
		adapter.setRids( rids );
		adapter.setStrategy( true );
		adapter.setGood( model.getLikeType() == 1 );
		adapter.setDisGood( model.getLikeType() == 0 );
		holder.myGridView.setAdapter( adapter );
		holder.myGridView.setOnItemClickListener( (parent, view, position1, id) -> {
			if (listener != null) {
				switch (position1) {
					case 1:
						listener.onPraiseClick( model );
						break;
					case 2:
						listener.onStepOnClick( model );
						break;
					case 3:
						listener.onShareClick( model );
						break;
				}
			}
		} );
		holder.isTopIv.setVisibility( model.isTop() ? View.VISIBLE : View.GONE );
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public int getItemCount() {
		return items.size();
	}

	public void setOnStrategyItemClickListener(OnStrategyItemClickListener onStrategyItemClickListener) {
		this.onStrategyItemClickListener = onStrategyItemClickListener;
	}

	public class StrategyHolder extends RecyclerView.ViewHolder {
		TextView titleTv, timeTv, writerTv;
		ImageView imageView, isTopIv;
		RelativeLayout rootView;
		MyGridView myGridView;

		public StrategyHolder(View v) {
			super( v );
			initView( v );
		}
		@SuppressWarnings( "deprecation" )
		void initView(View v) {
			isTopIv = (ImageView) findViewById( v, R.id.id_item_strategy_stick );
			titleTv = (TextView) findViewById( v, R.id.id_item_strategy_title );
			timeTv = (TextView) findViewById( v, R.id.id_item_strategy_time );
			writerTv = (TextView) findViewById( v, R.id.id_item_strategy_intro );
			imageView = (ImageView) findViewById( v, R.id.id_item_strategy_gameImg );
			rootView = (RelativeLayout) findViewById( v, R.id.id_item_strategy_root );
			rootView.setOnClickListener( view -> {
				if (onStrategyItemClickListener != null) {
					onStrategyItemClickListener.onStrategtItemClick( items.get( getPosition() ) );
				}
			} );
			myGridView = (MyGridView) findViewById( v, R.id.id_item_strategy_grid );
		}

		View findViewById(View v, int id) {
			return v.findViewById( id );
		}
	}

	public void setListener(OperationListener listener) {
		this.listener = listener;
	}

	/**
	 * Created by Eddy on 2018/1/23.
	 */

	public interface OperationListener {

		void onPraiseClick(StrategyMode model);

		void onStepOnClick(StrategyMode model);

		void onShareClick(StrategyMode model);

	}
}
