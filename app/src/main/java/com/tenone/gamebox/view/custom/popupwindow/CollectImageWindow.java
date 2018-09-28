/** 
 * Project Name:GameBox 
 * File Name:CollectImageWindow.java 
 * Package Name:com.tenone.gamebox.view.custom.popupwindow 
 * Date:2017-3-23����2:26:03 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.custom.popupwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.CollectImageClickListener;
import com.tenone.gamebox.view.adapter.CollectImageWindowAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.utils.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ѡ���ղ�ͼƬ ClassName:CollectImageWindow <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-23 ����2:26:03 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
@SuppressLint("InflateParams")
public class CollectImageWindow extends PopupWindow implements
		OnItemClickListener {
	View view;
	List<String> imgs = new ArrayList<String>();
	Context mContext;
	GridView gridView;
	CollectImageWindowAdapter mAdapter;
	CollectImageClickListener listener;

	@SuppressWarnings("deprecation")
	public CollectImageWindow(List<String> list, Context cxt) {
		this.imgs = list;
		this.mContext = cxt;
		// ���õ����ڿɵ��
		setTouchable(true);
		// ���õ�����ɵ��
		setOutsideTouchable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setAnimationStyle(R.style.PopupAnimation);
		setBackgroundDrawable(getBackground());
		initView();
		setContentView(view);
		setAdapter();
	}

	private void initView() {
		view = LayoutInflater.from(mContext).inflate(R.layout.window_collect,
				null);
		gridView = view
				.findViewById(R.id.id_window_collect_gridView);
	}

	private void setAdapter() {
		mAdapter = new CollectImageWindowAdapter(imgs, mContext);
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(this);
	}

	@Override
	public void dismiss() {
		BeanUtils.editAlpha((BaseActivity) mContext, 1f);
		super.dismiss();
	}

	public void setListener(CollectImageClickListener listener) {
		this.listener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (listener != null) {
			listener.onImageClick(imgs.get(position));
		}
		dismiss();
	}
	
	@Override
	public void showAsDropDown(View anchor) {
		BeanUtils.editAlpha((BaseActivity) mContext, 0.5f);
		super.showAsDropDown(anchor);
	}
}
