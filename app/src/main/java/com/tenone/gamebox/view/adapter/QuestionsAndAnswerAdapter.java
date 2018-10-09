package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.john.superadapter.SuperAdapter;
import com.john.superadapter.SuperViewHolder;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.AnswerModel;
import com.tenone.gamebox.mode.mode.QuestionModel;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

import java.util.List;

public class QuestionsAndAnswerAdapter extends SuperAdapter<QuestionModel> {
	private Context mContext;
	private View answerView;

	public QuestionsAndAnswerAdapter(Context context, List<QuestionModel> items, int layoutResId) {
		super( context, items, layoutResId );
		this.mContext = context;
	}

	@Override
	public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, QuestionModel item) {
		holder.setText( R.id.id_item_question, item.getQuestion() );
		List<AnswerModel> answerModels = item.getAnswers();
		LinearLayout linearLayout = holder.getView( R.id.id_item_answerLayout );
		linearLayout.removeAllViews();
		if (!BeanUtils.isEmpty( answerModels )) {
			for (AnswerModel model : answerModels) {
				answerView = LayoutInflater.from( mContext ).inflate( R.layout.layout_item_answer, linearLayout, false );
				linearLayout.addView( answerView );
				((TextView) answerView.findViewById( R.id.id_item_answer )).setText( model.getAnswer() );
			}
			holder.setText( R.id.id_item_time, item.getTime() );
			holder.setText( R.id.id_item_answerNum, getPlayerTxt( item.getNum() ) );
		} else {
			holder.setText( R.id.id_item_answerNum, "\u6682\u65e0\u56de\u7b54,\u5feb\u6765\u5e2e\u5e2eTa\u5427~" );
		}
		TextView coinTv = holder.findViewById( R.id.id_item_coin );
		if (item.getCoin() > 0) {
			boolean isReceived = item.isCoinsIsReceived();
			coinTv.setVisibility( View.VISIBLE );
			int rId = isReceived ? R.drawable.g_icon_jingbi_an : R.drawable.f_icon_jinbin_liang;
			Drawable leftDrawable = ContextCompat.getDrawable( mContext, rId );
			leftDrawable.setBounds( 0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight() );
			coinTv.setCompoundDrawables( leftDrawable, null, null, null );
			coinTv.setText( isReceived ? "\u5df2\u88ab\u9886\u53d6" : "+" + item.getCoin() );
		} else {
			coinTv.setVisibility( View.GONE );
		}
	}

	private Spanned getPlayerTxt(int num) {
		String text = mContext.getString( R.string.huida_txt, num + "" );
		return Html.fromHtml( text );
	}
}
