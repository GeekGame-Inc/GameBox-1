package com.tenone.gamebox.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;

import com.tenone.gamebox.mode.biz.BrowseImageBiz;
import com.tenone.gamebox.mode.view.BrowseImageView;
import com.tenone.gamebox.view.adapter.BrowseImageAdapter;
import com.tenone.gamebox.view.adapter.BrowseImageAdapter.ImageViewClickListener;

import java.util.ArrayList;
import java.util.List;

public class BrowseImagePresenter extends BasePresenter implements
		ImageViewClickListener {
	BrowseImageBiz browseBiz;
	BrowseImageView browseView;
	Context mContext;
	ArrayList<String> arrayList = new ArrayList<String>();
	BrowseImageAdapter mAdapter;

	public BrowseImagePresenter(Context context, BrowseImageView view) {
		this.browseBiz = new BrowseImageBiz();
		this.browseView = view;
		this.mContext = context;
	}

	public void initView() {
		arrayList.addAll(getImageUrls());
	}

	public void setAdapter() {
		mAdapter = new BrowseImageAdapter(arrayList, mContext);
		getViewPager().setAdapter(mAdapter);
		int index = getImageUrls().indexOf(getCurrentUrl());
		getViewPager().setCurrentItem(index);
		mAdapter.setImageViewClickListener(this);
	}

	public ViewPager getViewPager() {
		return browseView.getViewPager();
	}

	public Intent getIntent() {
		return browseView.getIntent();
	}

	public List<String> getImageUrls() {
		return browseBiz.getImageUrls(getIntent());
	}

	public String getCurrentUrl() {
		return browseBiz.getCurrentUrl(getIntent());
	}

	@Override
	public void onImageClick() {
		close(mContext);
	}
}
