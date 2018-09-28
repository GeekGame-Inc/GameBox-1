package com.tenone.gamebox.mode.biz;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tenone.gamebox.mode.able.RebateAble;
import com.tenone.gamebox.view.fragment.ApplyForRebateFragment;
import com.tenone.gamebox.view.fragment.RebateRecordFragment;

import java.util.ArrayList;
import java.util.List;

public class RebateBiz implements RebateAble {

	@Override
	public List<Fragment> getFragments(String uid) {
		List<Fragment> fragments = new ArrayList<Fragment>();
		Bundle bundle = new Bundle();
		bundle.putString( "uid",uid );
		ApplyForRebateFragment fragment = new ApplyForRebateFragment();
		fragment.setArguments( bundle );
		RebateRecordFragment fragment1 = new RebateRecordFragment();
		fragment1.setArguments( bundle );
		fragments.add( fragment );
		fragments.add( fragment1 );
		return fragments;
	}

	@Override
	public List<String> getTitles(Context cxt, int rId) {
		List<String> titles = new ArrayList<String>();
		String[] array = cxt.getResources().getStringArray( rId );
		if (array != null) {
			for (String string : array) {
				titles.add( string );
			}
		}
		array = null;
		return titles;
	}

}
