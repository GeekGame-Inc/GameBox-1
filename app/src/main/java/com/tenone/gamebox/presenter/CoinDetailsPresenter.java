package com.tenone.gamebox.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.CoinDetailsBiz;
import com.tenone.gamebox.mode.view.MyMessageView;
import com.tenone.gamebox.view.adapter.ManagementAdapter;
import com.tenone.gamebox.view.custom.CustomerUnderlinePageIndicator;
import com.tenone.gamebox.view.custom.NoScrollViewPager;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.viewpagerindicator.TabPageIndicator;
import com.tenone.gamebox.view.custom.xbanner.transformers.BasePageTransformer;
import com.tenone.gamebox.view.custom.xbanner.transformers.Transformer;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

import java.util.List;

public class CoinDetailsPresenter extends BasePresenter implements
		OnClickListener {
	CoinDetailsBiz messageBiz;
	MyMessageView messageView;
	Context mContext;
	ManagementAdapter mAdapter;
	private float width = 0, widthOffset = 0;

	public CoinDetailsPresenter(MyMessageView v, Context cxt) {
		this.mContext = cxt;
		this.messageBiz = new CoinDetailsBiz();
		this.messageView = v;
	}

	
	public void initTitle() {
		getTitleBarView().setLeftImg(R.drawable.icon_back_grey);
		getTitleBarView().setTitleText( "\u8d27\u5e01\u660e\u7ec6" );
	}

	
	public void setAdapter() {
		if (mAdapter == null) {
			mAdapter = new ManagementAdapter(
					((FragmentActivity) mContext).getSupportFragmentManager());
		}
		mAdapter.setArray(getFragments());
		mAdapter.setmTitleList(getTitles(R.array.details_title));
		getViewPager().setAdapter(mAdapter);
		showCustomToast(mContext, "\u53ea\u80fd\u67e5\u770b\u6700\u8fd17\u5929\u7684\u8bb0\u5f55\u54e6\uff01", Gravity.CENTER);
	}

	
	public void initTabView() {
		getTabPageIndicator().setViewPager(getViewPager());
		getIndicator().setViewPager(getViewPager());
		getIndicator().setFades(false);
		getTabPageIndicator().setOnPageChangeListener(getIndicator());
		
		width = getTabPageIndicator().getTextWidth();
		
		widthOffset = (DisplayMetricsUtils.getScreenWidth(mContext)
				/ mAdapter.getCount() - width) / 2;
		getIndicator().setDefultWidth(width);
		getIndicator().setDefultOffset(widthOffset);
		getViewPager().setCurrentItem(0);
		getViewPager().setOffscreenPageLimit(2);
		getViewPager().setPageTransformer(true,
				BasePageTransformer.getPageTransformer(Transformer.Stack));
	}

	
	public void initListener() {
		getTitleBarView().getLeftImg().setOnClickListener(this);
	}

	public TitleBarView getTitleBarView() {
		return messageView.getTitleBarView();
	}

	public TabPageIndicator getTabPageIndicator() {
		return messageView.getTabPageIndicator();
	}

	public CustomerUnderlinePageIndicator getIndicator() {
		return messageView.getIndicator();
	}

	public NoScrollViewPager getViewPager() {
		return messageView.getViewPager();
	}

	public Intent getIntent() {
		return messageView.getIntent();
	}

	public List<Fragment> getFragments() {
		return messageBiz.getFragments();
	}

	public List<String> getTitles(int rid) {
		return messageBiz.getTitles(mContext, rid);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_titleBar_leftImg:
			close(mContext);
			break;
		}
	}
}
