/** 
 * Project Name:GameBox 
 * File Name:RecordAdapter.java 
 * Package Name:com.tenone.gamebox.view.adapter 
 * Date:2017-3-28����10:58:11 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.RecordMode;

import java.util.ArrayList;
import java.util.List;

/**
 * ������¼������ ClassName:RecordAdapter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-28 ����10:58:11 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class RecordAdapter extends BaseAdapter {

	List<RecordMode> items = new ArrayList<RecordMode>();
	Context mContext;
	LayoutInflater mInflater;

	public RecordAdapter(Context cxt, List<RecordMode> list) {
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

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RecordHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_records, parent,
					false);
			holder = new RecordHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (RecordHolder) convertView.getTag();
		}
		
		RecordMode recordMode = items.get(position);
		BitmapDrawable bitmapDrawable = (BitmapDrawable) mContext
				.getResources().getDrawable(recordMode.getResId());
		holder.imageView.setImageBitmap(bitmapDrawable.getBitmap());
		holder.textView.setText(recordMode.getRecord());
		bitmapDrawable = null;
		return convertView;
	}

	class RecordHolder {
		ImageView imageView;
		TextView textView;

		public RecordHolder(View v) {
			initView(v);
		}

		private void initView(View v) {
			imageView = v.findViewById(R.id.id_records_ic);
			textView = v.findViewById(R.id.id_records_tv);
		}
	}

}
