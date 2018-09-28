
package com.tenone.gamebox.mode.biz;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.tenone.gamebox.mode.able.OpenServerTabAble;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.fragment.BTOpenFragment;
import com.tenone.gamebox.view.fragment.DisOpenFragment;

import java.util.ArrayList;
import java.util.List;

public class OpenServerTabBiz implements OpenServerTabAble {

    @Override
    public List<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add( new BTOpenFragment() );
        if (MyApplication.isIsShowDiscount()) {
            fragments.add( new DisOpenFragment() );
        }
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
        if (!MyApplication.isIsShowDiscount()) {
            titles.remove( "\u6298\u6263" );
        }
        return titles;
    }
}
