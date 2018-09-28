package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;

import com.john.superadapter.SuperAdapter;
import com.john.superadapter.SuperViewHolder;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.GameQuestionModel;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;

public class MyQuestionAdapter extends SuperAdapter<GameQuestionModel> {
	private Context mContext;

	public MyQuestionAdapter(Context context, List<GameQuestionModel> items, int layoutResId) {
		super( context, items, layoutResId );
		this.mContext = context;
	}

	@Override
	public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, GameQuestionModel item) {
		ImageView imageView = holder.findViewById( R.id.id_item_my_question_icon );
		ImageLoadUtils.loadNormalImg( imageView, mContext, item.getGameIcon() );
		holder.setText( R.id.id_item_my_question_name, item.getGameName() );
		holder.setText( R.id.id_item_my_question_question, item.getQuestionModel().getQuestion() );
		holder.setText( R.id.id_item_my_question_time, item.getQuestionModel().getTime() );
		TextView counts = holder.findViewById( R.id.id_item_my_question_count );
		counts.setText( getText( item.getQuestionModel().getNum() ) );
	}

	private Spanned getText(int count) {
		return Html.fromHtml( mContext.getString( R.string.huida_txt, count + "" ) );
	}
}
