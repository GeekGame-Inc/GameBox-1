package com.tenone.gamebox.mode.listener;

import com.tenone.gamebox.mode.mode.DynamicCommentModel;

/**
 * Created by Eddy on 2018/1/23.
 */

public interface OnDynamicCommentItemClickListener {

    void onCommentClick(DynamicCommentModel model);

    void onPraiseClick(DynamicCommentModel model);

    void onHeaderClick(DynamicCommentModel model);

    void onDeleteClick(DynamicCommentModel model);
}
