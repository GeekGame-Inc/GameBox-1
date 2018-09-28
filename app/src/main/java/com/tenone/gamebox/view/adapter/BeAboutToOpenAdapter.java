package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OpenItemClickListener;
import com.tenone.gamebox.mode.listener.OpenServiceWarnListener;
import com.tenone.gamebox.mode.mode.OpenServerMode;
import com.tenone.gamebox.view.custom.ThreeStateButton;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.TimeUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class BeAboutToOpenAdapter extends BaseAdapter {

	List<OpenServerMode> items = new ArrayList<OpenServerMode>();
	Context mContext;
	LayoutInflater mInflater;
	OpenServiceWarnListener listener;
	OpenItemClickListener openItemClickListener;

	public BeAboutToOpenAdapter(List<OpenServerMode> list, Context cxt) {
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
		BeAboutToOpenHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_today_open_list,
					parent, false);
			holder = new BeAboutToOpenHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (BeAboutToOpenHolder) convertView.getTag();
		}

        ViewGroup.LayoutParams imgparams = holder.imageView.getLayoutParams();
        imgparams.width = DisplayMetricsUtils.getScreenHeight( mContext ) /
                (DisplayMetricsUtils.getScreenHeight( mContext ) / DisplayMetricsUtils.getScreenWidth( mContext ) == 16 / 9 ? 11 : 12);
        imgparams.height = imgparams.width;
        holder.imageView.setLayoutParams( imgparams );

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
		holder.labelLayout.removeAllViews();
		if (mode.getLabelArray() != null) {
			String[] labels = mode.getLabelArray();
			if (labels != null) {
				for (int j = 0; j < labels.length; j++) {
					String label = labels[j];
					if (j < 3) {
						if (!TextUtils.isEmpty(label) || !" ".equals(label)) {
							TextView textView = new TextView(mContext);
							textView.setText(label);
							textView.setGravity( Gravity.CENTER );
							textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
							textView.setTextColor(mContext.getResources()
									.getColor(R.color.white));
							setTextBackground(textView, j);
							textView.setPadding(
									DisplayMetricsUtils.dipTopx(mContext, 5),
									1,
									DisplayMetricsUtils.dipTopx(mContext, 5),
									1);
							holder.labelLayout.addView(textView);
                            holder.labelLayout.setGravity( Gravity.CENTER_VERTICAL );
							LayoutParams params = (LayoutParams) textView
									.getLayoutParams();
							params.setMargins(0, 0,
									DisplayMetricsUtils.dipTopx(mContext, 3), 0);
							textView.setLayoutParams(params);
						} else {
							break;
						}
					}
				}
			}
		}
		return convertView;
	}
	
	int[] drawables = { R.drawable.shape_lable_1, R.drawable.shape_lable_2,
			R.drawable.shape_lable_3 };

	@SuppressWarnings("deprecation")
	private void setTextBackground(TextView textView, int index) {
		textView.setBackground(mContext.getResources().getDrawable(
				drawables[index]));
	}

	
	
	public void setOpenServiceWarnListener(OpenServiceWarnListener l) {
		this.listener = l;
	}

	public void setOpenItemClickListener(
			OpenItemClickListener openItemClickListener) {
		this.openItemClickListener = openItemClickListener;
	}

	class BeAboutToOpenHolder {
		ImageView imageView;
		TextView nameTv, sizeTv;
		LinearLayout labelLayout;
		ThreeStateButton stateButton;
		RelativeLayout layout;

		public BeAboutToOpenHolder(View v) {
			initView(v);
		}

		private void initView(View v) {
			imageView = (ImageView) findViewById(v, R.id.id_item_todayOpen_img);
			nameTv = (TextView) findViewById(v, R.id.id_item_todayOpen_nameTv);
			sizeTv = (TextView) findViewById(v, R.id.id_item_todayOpen_sizeTv);
			labelLayout = (LinearLayout) findViewById(v,
					R.id.id_item_todayOpen_labelLayout);
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
