/** 
 * Project Name:GameBox 
 * File Name:GameViewPagerAdapter.java 
 * Package Name:com.tenone.gamebox.view.adapter 
 * Date:2017-3-3����2:39:02 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class GameViewPagerAdapter<T> extends FragmentPagerAdapter {
	private List<T> array = new ArrayList<T>();
	private List<String> mTitleList = new ArrayList<String>();

	public GameViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}


	@Override
	public Object instantiateItem(View container, int position) {
		return array.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		try {
			container.removeView((View) array.get(position));
		} catch (ClassCastException e) {
			Log.e("GameBoxError", array.get(position).getClass().toString());
		}
	}
	public void setArray(List<T> array) {
		this.array = array;
	}

	public void setmTitleList(List<String> mTitleList) {
		this.mTitleList = mTitleList;
	}

	@Override
	public Fragment getItem(int pos) {
		if (array == null) {
			return null;
		}
		return (Fragment) array.get(pos);
	}

	@Override
	public int getCount() {
		return array == null ? 0 : array.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (mTitleList != null && !mTitleList.isEmpty()) {
			return mTitleList.get(position);
		}
		return super.getPageTitle(position);
	}
}
