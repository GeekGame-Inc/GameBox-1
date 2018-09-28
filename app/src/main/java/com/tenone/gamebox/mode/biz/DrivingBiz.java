package com.tenone.gamebox.mode.biz;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.tenone.gamebox.mode.able.DrivingAble;
import com.tenone.gamebox.view.fragment.AboutForMeFragment;
import com.tenone.gamebox.view.fragment.AllDynamicFragment;
import com.tenone.gamebox.view.fragment.AttentionDynamicFragment;
import com.tenone.gamebox.view.fragment.ImageDynamicFragment;
import com.tenone.gamebox.view.fragment.MineDynamicFragment;

import java.util.ArrayList;
import java.util.List;


public class DrivingBiz implements DrivingAble {

    @Override
    public List<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add( new AllDynamicFragment() );
        fragments.add( new ImageDynamicFragment() );
        fragments.add( new AttentionDynamicFragment() );
        fragments.add( new MineDynamicFragment() );
        fragments.add( new AboutForMeFragment() );
        return fragments;
    }

    @Override
    public List<String> getFragmentTitles(Context context, int rId) {
        List<String> titles = new ArrayList<String>();
        String[] array = context.getResources().getStringArray( rId );
        if (array != null) {
            for (String string : array) {
                titles.add( string );
            }
        }
        array = null;
        return titles;
    }
}
