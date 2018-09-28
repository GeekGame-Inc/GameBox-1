package com.tenone.gamebox.mode.listener;

import com.tenone.gamebox.mode.mode.DriverModel;

/**
 * Created by Eddy on 2018/1/26.
 */

public interface OnDriverItemClickListener {
    void onDriverHeaderClick(DriverModel model);

    void onAttentionClick(DriverModel model);
}
