package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
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

public class OldBtGameAdapter extends BaseAdapter {

	private List<GameModel> models;
	private Context context;
	private int type;
	private LayoutInflater inflater;

	public OldBtGameAdapter(List<GameModel> models, Context context, int type) {
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
		GameAdapterViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate( R.layout.item_old_new_game_list, parent, false );
			holder = new GameAdapterViewHolder( convertView );
			convertView.setTag( holder );
		} else {
			holder = (GameAdapterViewHolder) convertView.getTag();
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
		if (type == 3) {
			BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable( context, R.drawable.c_icon_kaishiyouxi );
			holder.img.setImageBitmap( drawable.getBitmap() );
		} else {
			holder.img.setSelected( 1 == type );
		}
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
		int t = model.getType();
		if (4 == t) {
			holder.timeTv.setVisibility( View.VISIBLE );
			holder.timeTv.setText( model.getTime() );
		} else {
			holder.timeTv.setText( "" );
			holder.timeTv.setVisibility( View.GONE );
		}
		ImageLoadUtils.loadNormalImg( holder.iconImg, context, model.getImgUrl() );

		return convertView;
	}

	private class GameAdapterViewHolder {
		private ImageView iconImg, img;
		private TextView nameTv, sizeTv, disTv, todayTv, welfareTv, timeTv;
		private LableView lableView;

		public GameAdapterViewHolder(View v) {
			iconImg = v.findViewById( R.id.id_new_game_list_icon );
			img = v.findViewById( R.id.id_new_game_list_img );
			nameTv = v.findViewById( R.id.id_new_game_list_gameName );
			sizeTv = v.findViewById( R.id.id_new_game_list_size );
			disTv = v.findViewById( R.id.id_new_game_list_dis );
			todayTv = v.findViewById( R.id.id_new_game_list_today );
			welfareTv = v.findViewById( R.id.id_new_game_list_welfare );
			lableView = v.findViewById( R.id.id_new_game_list_lableView );
			timeTv = v.findViewById( R.id.id_new_game_list_time );
		}
	}
}
