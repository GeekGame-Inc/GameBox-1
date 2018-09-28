package com.tenone.gamebox.mode.listener;

import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.mode.mode.DynamicModel;

/**
 * Created by Eddy on 2018/1/23.
 */

public interface DynamicOperationListener {

    void onPraiseClick(DynamicModel model);

    void onStepOnClick(DynamicModel model);

    void onCommentClick(DynamicModel model);

    void onShareClick(DynamicModel model);

    void onAttentionClick(DriverModel model);
}
