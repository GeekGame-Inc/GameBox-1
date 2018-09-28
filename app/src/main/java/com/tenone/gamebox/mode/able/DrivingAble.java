package com.tenone.gamebox.mode.able;


import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Created by Eddy on 2018/1/8.
 */

public interface DrivingAble {

    List<Fragment> getFragments();

    List<String> getFragmentTitles(Context context, int rId);
}
