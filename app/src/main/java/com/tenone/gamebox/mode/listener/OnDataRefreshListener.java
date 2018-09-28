package com.tenone.gamebox.mode.listener;

/**
 * Created by Eddy on 2018/3/7.
 */

public interface OnDataRefreshListener {
    int HOT=5896;
    int ALL=5893;
    int ATT=5898;
    int MIN=5890;

    void onDataRefresh(int which);
}
