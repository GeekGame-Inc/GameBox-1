package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class CollectImageWindowAdapter extends BaseAdapter {
	List<String> items = new ArrayList<String>();
	Context mContext;
	LayoutInflater mInflater;

	public CollectImageWindowAdapter(List<String> list, Context cxt) {
		this.items = list;
		this.mContext = cxt;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
		IamgeHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_image, parent, false);
			holder = new IamgeHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (IamgeHolder) convertView.getTag();
		}

		LayoutParams params = holder.image.getLayoutParams();
		params.width = DisplayMetricsUtils.getScreenWidth(mContext) / 3;
		params.height = params.width;
		holder.image.setLayoutParams(params);
		ImageLoadUtils.loadNormalImg(holder.image, mContext,
				items.get(position));
		return convertView;
	}

	class IamgeHolder {
		ImageView image;

		public IamgeHolder(View v) {
			initView(v);
		}

		private void initView(View v) {
			image = v.findViewById(R.id.id_item_imageView);
		}
	}
}
