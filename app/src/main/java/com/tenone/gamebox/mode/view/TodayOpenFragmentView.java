/**
 * Project Name:GameBox
 * File Name:TodayOpenFragmentView.java
 * Package Name:com.tenone.gamebox.mode.view
 * Date:2017-3-29ионГ9:51:17
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.mode.view;

import android.widget.ListView;
import android.widget.TextView;

import com.tenone.gamebox.view.custom.RefreshLayout;

/**
 * ClassName:TodayOpenFragmentView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-29 ионГ9:51:17 <br/>
 *
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface TodayOpenFragmentView {

    RefreshLayout getRefreshLayout();

    ListView getListView();

    TextView getJRTv();

    TextView getJJTv();

    TextView getYKTv();

}
