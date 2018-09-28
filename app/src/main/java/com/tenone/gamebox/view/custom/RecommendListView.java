/** 
 * Project Name:GameBox 
 * File Name:LoadingScrollView.java 
 * Package Name:com.tenone.gamebox.view.custom 
 * Date:2017-3-7����2:12:52 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.tenone.gamebox.R;

/**
 * ��Ϸ�Ƽ�listview ClassName:RecommendListViewView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-7 ����2:12:52 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
@SuppressLint("InflateParams")
public class RecommendListView extends ListView {
	View headerView;
	Context mContext;
	boolean o = true;

	public RecommendListView(Context context) {
		this(context, null);
		this.mContext = context;
	}

	public RecommendListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		this.mContext = context;
		init();
	}

	public RecommendListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init();
	}

	/* ����ĳЩ�ֻ����� */
	private void init() {
		addHeaderView(new View(mContext));
	}

	public void addHeader() {
		headerView = LayoutInflater.from(mContext).inflate(
				R.layout.layout_header, null);
		setFooterDividersEnabled(true);
		setHeaderDividersEnabled(true);
		addHeaderView(headerView);
	}

	/**
	 * ��ȡͷ������ getHeaderView:(������һ�仰�����������������). <br/>
	 * 
	 * @author John Lie
	 * @return
	 * @since JDK 1.6
	 */
	public View getHeaderView() {
		return headerView;
	}
}
