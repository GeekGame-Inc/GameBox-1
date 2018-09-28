package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OpenItemClickListener;
import com.tenone.gamebox.mode.listener.OpenServiceWarnListener;
import com.tenone.gamebox.mode.mode.OpenServerMode;
import com.tenone.gamebox.view.custom.ThreeStateButton;
import com.tenone.gamebox.view.utils.TimeUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class MyRemindAapter extends BaseAdapter {

	List<OpenServerMode> items = new ArrayList<OpenServerMode>();
	Context mContext;
	LayoutInflater mInflater;
	OpenServiceWarnListener listener;
	OpenItemClickListener openItemClickListener;

	public MyRemindAapter(List<OpenServerMode> list, Context cxt) {
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

	int a = 1;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TodayOpenFragmentHolder holder = null;
		a++;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_today_open_list,
					parent, false);
			holder = new TodayOpenFragmentHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (TodayOpenFragmentHolder) convertView.getTag();
		}

		final OpenServerMode mode = items.get(position);
		holder.stateButton.setOnClickListener( v -> {
			if (listener != null) {
				listener.onOpenServiceClick(mode);
			}
		} );
		int state = 0;
		state = mode.isNotification() ? 1 : 0;
		state = mode.isOpen() ? 2 : state;
		holder.stateButton.setState(state);
		holder.nameTv.setText(mode.getGameName() + " - " + mode.getServiceId()
				+ " \u670d" );
		String time = mode.getOpenTime();
		if (!TextUtils.isEmpty(time)) {
			time = TimeUtils.formatDateMin(Long.valueOf(time) * 1000);
		}
		holder.sizeTv.setText( "\u5f00\u670d\u65f6\u95f4: " + time);
		ImageLoadUtils.loadNormalImg(holder.imageView, mContext,
				mode.getImgUrl());
		holder.layout.setOnClickListener( v -> {
			if (openItemClickListener != null) {
				openItemClickListener.onOpenItemClick(mode);
			}
		} );
		holder.stateButton.setOnClickListener( v -> {
			if (listener != null) {
				listener.onOpenServiceClick(mode);
			}
		} );
		return convertView;
	}

	public void setOpenServiceWarnListener(OpenServiceWarnListener l) {
		this.listener = l;
	}

	public void setOpenItemClickListener(
			OpenItemClickListener openItemClickListener) {
		this.openItemClickListener = openItemClickListener;
	}

	private class TodayOpenFragmentHolder {
		ImageView imageView;
		TextView nameTv, sizeTv;
		ThreeStateButton stateButton;
		RelativeLayout layout;

		public TodayOpenFragmentHolder(View v) {
			initView(v);
		}

		private void initView(View v) {
			imageView = (ImageView) findViewById(v, R.id.id_item_todayOpen_img);
			nameTv = (TextView) findViewById(v, R.id.id_item_todayOpen_nameTv);
			sizeTv = (TextView) findViewById(v, R.id.id_item_todayOpen_sizeTv);
			stateButton = (ThreeStateButton) findViewById(v,
					R.id.id_item_todayOpen_button);
			layout = (RelativeLayout) findViewById(v,
					R.id.id_item_todayOpen_layout);
		}

		private View findViewById(View v, int rId) {
			return v.findViewById(rId);
		}
	}
}
