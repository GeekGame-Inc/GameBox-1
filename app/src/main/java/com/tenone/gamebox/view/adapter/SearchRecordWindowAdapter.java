/** 
 * Project Name:GameBox 
 * File Name:SearchRecordWindowAdapter.java 
 * Package Name:com.tenone.gamebox.view.adapter 
 * Date:2017-4-10����1:36:26 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tenone.gamebox.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * ClassName:SearchRecordWindowAdapter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-10 ����1:36:26 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class SearchRecordWindowAdapter extends BaseAdapter implements
		Filterable {
	private Context context;
	private List<String> items;
	private ArrayList<String> mOriginalValues;
	private LayoutInflater mInflater;
	private String keyWords;
	private final Object mLock = new Object();

	public SearchRecordWindowAdapter(Context cxt, List<String> list,
			String keyWords) {
		this.context = cxt;
		this.items = list;
		this.mInflater = LayoutInflater.from(context);
		this.keyWords = keyWords;
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
		SearchWindowHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_search_record_window,
					parent, false);
			holder = new SearchWindowHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (SearchWindowHolder) convertView.getTag();
		}
		String str = items.get(position);
		if (!keyWords.equals("")) {
			String patten = "" + keyWords;
			try {
				Pattern p = Pattern.compile(patten);
				Matcher m = p.matcher(str);
				SpannableString spannableString = new SpannableString(str);
				while (m.find()) {
					if (str.contains(m.group())) {
						spannableString.setSpan(new ForegroundColorSpan(context
								.getResources()
								.getColor(R.color.underLineColor)), m.start(),
								m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
				holder.textView.setText(spannableString);
			} catch (PatternSyntaxException e) {
				System.err.println(e);
			}
		} else {
			SpannableString spannableString = new SpannableString(str);
			spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
					str.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			holder.textView.setText(spannableString);
		}
		return convertView;
	}

	class SearchWindowHolder {
		TextView textView;

		public SearchWindowHolder(View v) {
			initView(v);
		}

		private void initView(View v) {
			textView = v
					.findViewById(R.id.id_searchRecordWindow_textView);
		}
	}

	@Override
	public Filter getFilter() {
		return filter;
	}

	Filter filter = new Filter() {
		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			items = (List<String>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}
		@SuppressLint("DefaultLocale")
		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults filterResults = new FilterResults();
			if (mOriginalValues == null) {
				synchronized (mLock) {
					mOriginalValues = new ArrayList<String>(items);
				}
			}
			if (prefix == null || prefix.length() == 0) {
				ArrayList<String> list;
				synchronized (mLock) {
					list = new ArrayList<String>(mOriginalValues);
				}
				filterResults.values = list;
				filterResults.count = list.size();
				keyWords = "";
			} else {
				String prefixString = prefix.toString().toLowerCase();
				ArrayList<String> values;
				synchronized (mLock) {
					values = new ArrayList<String>(mOriginalValues);
				}
				final int count = values.size();
				final ArrayList<String> newValues = new ArrayList<String>();
				for (int i = 0; i < count; i++) {
					final String value = values.get(i);
					final String valueText = value.toLowerCase();
					if (valueText.contains(prefixString)) {
						newValues.add(value);
						keyWords = prefixString;
					}
				}
				filterResults.values = newValues;
				filterResults.count = newValues.size();
			}
			return filterResults;
		}
	};
}
