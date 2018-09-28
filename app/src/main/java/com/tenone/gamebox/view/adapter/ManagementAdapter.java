

package com.tenone.gamebox.view.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.ArrayList;
import java.util.List;



public class ManagementAdapter extends FragmentPagerAdapter {
	
	private List<Fragment> array = new ArrayList<Fragment>();
	
	private List<String> mTitleList = new ArrayList<String>();

	public ManagementAdapter(FragmentManager fm) {
		super( fm );
	}

	@Override
	@NonNull
	public Object instantiateItem(View container, int position) {
		return array.get( position );
	}

	@SuppressWarnings("deprecation")
	@Override
	public void destroyItem(View container, int position, Object object) {
		super.destroyItem( container, position, object );
	}

	
	public void setArray(List<Fragment> array) {
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
		return array.get( pos );
	}

	@Override
	public int getCount() {
		return array == null ? 0 : array.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (mTitleList != null && !mTitleList.isEmpty()) {
			return mTitleList.get( position );
		}
		return super.getPageTitle( position );
	}

	
}
