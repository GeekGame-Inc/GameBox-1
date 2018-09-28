/**
 * Project Name:GameBox
 * File Name:PrivilegeView.java
 * Package Name:com.tenone.gamebox.mode.view
 * Date:2017-4-13обнГ3:39:49
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.mode.view;


import com.tenone.gamebox.view.custom.MyRefreshListView;
import com.tenone.gamebox.view.custom.RefreshLayout;
import com.tenone.gamebox.view.custom.TitleBarView;

/**
 * ClassName:PrivilegeView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-13 обнГ3:39:49 <br/>
 *
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public interface PrivilegeView {

    TitleBarView getTitleBarView();

    MyRefreshListView getListView();

    RefreshLayout getRefreshLayout();
}
