package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.view.custom.LableView;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;

public class GameTopAdapter extends BaseAdapter {
	private List<GameModel> models;
	private Context context;
	private int type;
	private boolean isTop3 = false;
	private LayoutInflater inflater;

	public GameTopAdapter(List<GameModel> models, Context context, int type) {
		this.models = models;
		this.context = context;
		this.type = type;
		this.inflater = LayoutInflater.from( context );
	}

	@Override
	public int getCount() {
		return models.size();
	}

	@Override
	public Object getItem(int position) {
		return models.get( position );
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GTAViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate( R.layout.item_game_top_list, parent, false );
			holder = new GTAViewHolder( convertView );
			convertView.setTag( holder );
		} else {
			holder = (GTAViewHolder) convertView.getTag();
		}
		GameModel model = models.get( position );
		holder.nameTv.setText( model.getName() );
		int operate = model.getOperate();
		if (operate > 0) {
			holder.sizeTv.setText( operate == 1 ? "\u72ec\u5bb6" : "\u8054\u5408" );
			holder.sizeTv.setTextColor( ContextCompat.getColorStateList( context, R.color.operate_select_color ) );
			holder.sizeTv.setBackground( ContextCompat.getDrawable( context, R.drawable.selector_operate_lable ) );
			holder.sizeTv.setSelected( operate == 2 );
		} else {
			holder.sizeTv.setText( model.getSize() + "M" );
		}
		holder.welfareTv.setText( model.getContent() );
		String[] labels = model.getLabelArray();
		holder.lableView.addLable( labels );
		int ranking = isTop3 ? (position + 4) : (position + 1);
		holder.rankingTv.setText( "No." + ranking );
		if (type == 2) {
			String dis = model.getDis();
			boolean isVisibility;
			try {
				isVisibility = !TextUtils.isEmpty( dis ) && Float.valueOf( dis ).floatValue() > 0f;
			} catch (Exception e) {
				isVisibility = false;
			}
			holder.disTv.setVisibility( isVisibility ? View.VISIBLE : View.GONE );
			if (isVisibility) {
				float d = Float.valueOf( dis ).floatValue();
				holder.disTv.setText( d + "\u6298" );
			}
		}
		holder.todayTv.setVisibility( model.isToday() ? View.VISIBLE : View.GONE );
		ImageLoadUtils.loadNormalImg( holder.iconImg, context, model.getImgUrl() );
		return convertView;
	}

	public void setTop3(boolean top3) {
		isTop3 = top3;
	}

	private class GTAViewHolder {
		private ImageView iconImg;
		private TextView nameTv, sizeTv, disTv, todayTv, welfareTv, rankingTv;
		private LableView lableView;

		public GTAViewHolder(View v) {
			rankingTv = v.findViewById( R.id.id_game_top_ranking );
			iconImg = v.findViewById( R.id.id_game_top_icon );
			nameTv = v.findViewById( R.id.id_game_top_gameName );
			sizeTv = v.findViewById( R.id.id_game_top_size );
			disTv = v.findViewById( R.id.id_game_top_dis );
			todayTv = v.findViewById( R.id.id_game_top_today );
			welfareTv = v.findViewById( R.id.id_game_top_welfare );
			lableView = v.findViewById( R.id.id_game_top_lableView );
		}
	}
}
