package com.tenone.gamebox.mode.biz;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.tenone.gamebox.mode.able.ManagementAble;
import com.tenone.gamebox.view.fragment.DownManageFragment;
import com.tenone.gamebox.view.fragment.InstalledFragment;
import com.tenone.gamebox.view.fragment.UpdateFragment;

import java.util.ArrayList;
import java.util.List;

public class ManagementBiz implements ManagementAble {

	@Override
	public List<Fragment> getFragments() {
		List<Fragment> fragments = new ArrayList<Fragment>();
		DownManageFragment fragment = new DownManageFragment();
		InstalledFragment fragment1 = new InstalledFragment();
		UpdateFragment fragment2 = new UpdateFragment();
		fragments.add(fragment);
		fragments.add(fragment1);
		fragments.add(fragment2);
		return fragments;
	}

	@Override
	public List<String> getTitles(Context cxt, int resId) {
		List<String> titles = new ArrayList<String>();
		String[] array = cxt.getResources().getStringArray(resId);
		if (array != null) {
			for (String string : array) {
				titles.add(string);
			}
		}
		return titles;
	}
}
