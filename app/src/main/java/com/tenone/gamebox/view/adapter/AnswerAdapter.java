package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.john.superadapter.SuperAdapter;
import com.john.superadapter.SuperViewHolder;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.AnswerModel;
import com.tenone.gamebox.mode.mode.UserInfoModel;
import com.tenone.gamebox.view.base.Constant;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;

public class AnswerAdapter extends SuperAdapter<AnswerModel> {
	private Context mContext;

	public AnswerAdapter(Context context, List<AnswerModel> items, int layoutResId) {
		super( context, items, layoutResId );
		this.mContext = context;
	}

	@Override
	public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, AnswerModel item) {
		UserInfoModel infoModel = item.getUserInfoModel();
		if (infoModel != null) {
			ImageView header = holder.findViewById( R.id.id_item_answer_header );
			ImageLoadUtils.loadCircleImg( mContext, infoModel.getHeader(), header );
			holder.setText( R.id.id_item_answer_name, infoModel.getNick() );
		}
		holder.setText( R.id.id_item_answer_content, item.getAnswer() );
		holder.setText( R.id.id_item_answer_time, item.getTime() );
		holder.setVisibility( R.id.id_item_answer_best, item.isBest() ? View.VISIBLE : View.GONE );
		holder.setVisibility( R.id.id_item_answer_top, item.isTop() ? View.VISIBLE : View.GONE );
		holder.setVisibility( R.id.id_item_answer_coin, item.isTaskBonus() ? View.VISIBLE : View.GONE );
		holder.setText( R.id.id_item_answer_coin, "+" + Constant.getQuestionTaskCoin() );
	}
}
