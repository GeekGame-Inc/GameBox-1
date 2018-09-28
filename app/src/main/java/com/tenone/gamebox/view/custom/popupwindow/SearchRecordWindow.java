/** 
 * Project Name:GameBox 
 * File Name:SearchRecordWindow.java 
 * Package Name:com.tenone.gamebox.view.custom.popupwindow 
 * Date:2017-4-10����10:42:16 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.custom.popupwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.SearchRecordClickListener;
import com.tenone.gamebox.view.adapter.SearchRecordWindowAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ������¼ ClassName:SearchRecordWindow <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-10 ����10:42:16 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
@SuppressLint("InflateParams")
public class SearchRecordWindow extends PopupWindow implements
		OnItemClickListener {
	Context context;
	View view;
	ListView listView;
	SearchRecordWindowAdapter adapter;
	List<String> records = new ArrayList<String>();
	String keyWords = "";
	SearchRecordClickListener clickListener;

	@SuppressWarnings("deprecation")
	public SearchRecordWindow(Context cxt, List<String> list, String key) {
		this.context = cxt;
		this.keyWords = key;
		this.records = list;
		// ���ÿ��Ի�ý���
		setFocusable(true);
		// ���õ����ڿɵ��
		setTouchable(true);
		// ���õ�����ɵ��
		setOutsideTouchable(true);
		// ���õ����Ŀ�Ⱥ͸߶�
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(DisplayMetricsUtils.getScreenHeight(context) / 3 * 2);
		setBackgroundDrawable(new BitmapDrawable());
		view = LayoutInflater.from(context).inflate(
				R.layout.window_search_record, null);
		setContentView(view);
		initView();
	}

	private void initView() {
		listView = view
				.findViewById(R.id.id_searchRecordWindow_listView);
		adapter = new SearchRecordWindowAdapter(context, records, keyWords);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	public void setKeyWords(String str) {
		this.keyWords = str;
		if (TextUtils.isEmpty(keyWords)) {
			adapter.getFilter().filter(null);
		} else {
			adapter.getFilter().filter(keyWords);
		}
	}

	public void setSearchRecordClickListener(SearchRecordClickListener l) {
		this.clickListener = l;
	}

	@Override
	public void dismiss() {
		DisplayMetricsUtils.backgroundAlpha(1, (BaseActivity) context);
		super.dismiss();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (clickListener != null) {
			clickListener.onSearchRecordClick(records.get(position));
		}
	}
}
