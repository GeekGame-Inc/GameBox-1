/** 
 * Project Name:GameBox 
 * File Name:GiftSearchPresenter.java 
 * Package Name:com.tenone.gamebox.presenter 
 * Date:2017-3-28����9:53:55 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.GiftSearchBiz;
import com.tenone.gamebox.mode.mode.RecordMode;
import com.tenone.gamebox.mode.view.GiftSearchView;
import com.tenone.gamebox.view.activity.GiftSearchResultActivity;
import com.tenone.gamebox.view.adapter.RecordAdapter;
import com.tenone.gamebox.view.adapter.SearchRecordWindowAdapter;
import com.tenone.gamebox.view.custom.SearchTitleBarView;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:GiftSearchPresenter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-28 ����9:53:55 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class GiftSearchPresenter extends BasePresenter implements
		OnItemClickListener, OnClickListener, SearchTitleBarView.OnSearchButtonClickListener {
	GiftSearchBiz biz;
	GiftSearchView view;
	Context mContext;
	RecordAdapter mAdapter;
	View headerView;
	// ����Դ
	List<RecordMode> items;
	// ������¼
	List<String> records;
	// ������¼
	List<String> allGameName;
	SearchRecordWindowAdapter adapter;

	public GiftSearchPresenter(Context cxt, GiftSearchView v) {
		this.biz = new GiftSearchBiz();
		this.mContext = cxt;
		this.view = v;
		records = new ArrayList<String>();
		records.addAll(getRecord());
		items = new ArrayList<RecordMode>();
		items.addAll(getRecordModes());
		allGameName = new ArrayList<String>();
		allGameName.addAll(getAllGameName());
	}

	public void setAdapter() {
		if (mAdapter == null) {
			mAdapter = new RecordAdapter(mContext, items);
		}
		getListView().setAdapter(mAdapter);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	public void initView() {
		headerView = LayoutInflater.from(mContext).inflate(
				R.layout.layout_record_header, null);
		headerView.setBackgroundColor(mContext.getResources().getColor(
				R.color.souBg));
		getListView().addHeaderView(headerView);
		getSearchTitleBarView().setLeftImg(R.drawable.icon_back_grey);
		getSearchTitleBarView().setRightImg(R.drawable.ic_sousuo);
		getSearchTitleBarView().getCustomizeEditText().setHint(getHint());
		adapter = new SearchRecordWindowAdapter(mContext, allGameName, "");
		getSearchTitleBarView().getCustomizeEditText().setAdapter(adapter);
		getSearchTitleBarView().getCustomizeEditText().setDropDownWidth(
				DisplayMetricsUtils.getScreenWidth(mContext));
		getSearchTitleBarView().getCustomizeEditText()
				.setDropDownVerticalOffset(
						DisplayMetricsUtils.dipTopx(mContext, 3));
	}

	public void initListener() {
		getListView().setOnItemClickListener(this);
		getSearchTitleBarView().getLeftImg().setOnClickListener(this);
		getSearchTitleBarView().getRightImg().setOnClickListener(this);
        getSearchTitleBarView().setOnSearchButtonClickListener( this );
	}

	public void update() {
		records.clear();
		records.addAll(getRecord());
		items.clear();
		items.addAll(getRecordModes());
		mAdapter.notifyDataSetChanged();
	}

	public SearchTitleBarView getSearchTitleBarView() {
		return view.getSearchTitleBarView();
	}

	public ListView getListView() {
		return view.getListView();
	}

	public List<String> getRecord() {
		return biz.getRecord(mContext);
	}

	public Intent getIntent() {
		return view.getIntent();
	}

	public List<RecordMode> getRecordModes() {
		return biz.getRecordModes(records);
	}

	public void saveRecord(List<String> records) {
		biz.saveRecord(records);
	}

	public String getHint() {
		return biz.getHint(getIntent());
	}

	public List<String> getAllGameName() {
		return biz.getAllGameName();
	}

	public void clearRecord() {
		biz.clearRecord();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// ��ͷ,��1��ʼ
		if (position == 1) {
			clearRecord();
			items.clear();
			records.clear();
			mAdapter.notifyDataSetChanged();
		} else if (position > 1) {
			// ������һ��
			String str = items.get(position - 1).getRecord();
			records.remove(str);
			records.add(0, str);
			saveRecord(records);
			openOtherActivity(mContext, new Intent(mContext,
					GiftSearchResultActivity.class).putExtra("condition", str));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_searchTitle_leftImg:
			close(mContext);
			break;
		case R.id.id_searchTitle_rightImg:
			String str = getSearchTitleBarView().getCustomizeEditText()
					.getText().toString();
			if (!TextUtils.isEmpty(str)) {
				records.remove(str);
				records.add(str);
				saveRecord(records);
				// ��һ��
				openOtherActivity(mContext, new Intent(mContext,
						GiftSearchResultActivity.class).putExtra("condition",
						str));
			} else {
				showToast(mContext, "��������������");
			}
			break;
		}
	}

    @Override
    public void onSearchButtonClick() {
        String str = getSearchTitleBarView().getCustomizeEditText()
                .getText().toString();
        if (!TextUtils.isEmpty(str)) {
					records.remove(str);
            records.add(str);
            saveRecord(records);
            openOtherActivity(mContext, new Intent(mContext,
                    GiftSearchResultActivity.class).putExtra("condition",
                    str));
        } else {
            showToast(mContext, "��������������");
        }
    }
}
