package com.tenone.gamebox.view.adapter;

import android.content.Context;
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
import com.tenone.gamebox.view.custom.TwoStateButton;
import com.tenone.gamebox.view.utils.TimeUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;

public class NCGameAdapter extends BaseAdapter {

	private List<GameModel> models;
	private Context context;
	private int type;
	private LayoutInflater inflater;
	private OnYYClickListener onYYClickListener;

	public NCGameAdapter(List<GameModel> models, Context context, int type) {
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
			convertView = inflater.inflate( R.layout.item_nc_game_list, parent, false );
			holder = new GameAdapterViewHolder( convertView );
			convertView.setTag( holder );
		} else {
			holder = (GameAdapterViewHolder) convertView.getTag();
		}
		GameModel model = models.get( position );
		holder.nameTv.setText( model.getName() );
		holder.welfareTv.setText( model.getContent() );
		String[] labels = model.getLabelArray();
		holder.lableView.addLable( labels );
		String time = model.getTime();
		holder.img.setVisibility( 1 == type ? View.GONE : View.VISIBLE );
		if (!TextUtils.isEmpty( time )) {
			try {
				long t = Long.valueOf( time ).longValue() * 1000;
				if (t > 0) {
					time = TimeUtils.formatDateMin( t );
					holder.timeTv.setText( 0 == type ? ("\u5185\u6d4b\u65f6\u95f4:" + time) : ("\u4e0a\u7ebf\u65f6\u95f4:" + time) );
				} else {
					holder.timeTv.setText( 0 == type ? ("\u5185\u6d4b\u65f6\u95f4:\u656c\u8bf7\u671f\u5f85") : ("\u4e0a\u7ebf\u65f6\u95f4:\u656c\u8bf7\u671f\u5f85") );
				}

			} catch (NumberFormatException e) {
				holder.timeTv.setText( 0 == type ? ("\u5185\u6d4b\u65f6\u95f4:\u656c\u8bf7\u671f\u5f85") : ("\u4e0a\u7ebf\u65f6\u95f4:\u656c\u8bf7\u671f\u5f85") );
			}
			if (1 == type) {
				holder.twoStateButton.setVisibility( View.VISIBLE );
				if (!model.isReserved()) {
					holder.twoStateButton.setOnClickListener( v -> {
						if (onYYClickListener != null) {
							onYYClickListener.onYYClick( model );
						}
					} );
				} else {
					holder.twoStateButton.setOnClickListener( null );
				}
				holder.twoStateButton.setState( model.isReserved() ? 1 : 0 );
			}
		}
		ImageLoadUtils.loadNormalImg( holder.iconImg, context, model.getImgUrl() );
		return convertView;
	}

	public void setOnYYClickListener(OnYYClickListener onYYClickListener) {
		this.onYYClickListener = onYYClickListener;
	}

	private class GameAdapterViewHolder {
		private ImageView iconImg, img;
		private TextView nameTv, welfareTv, timeTv;
		private LableView lableView;
		private TwoStateButton twoStateButton;

		public GameAdapterViewHolder(View v) {
			iconImg = v.findViewById( R.id.id_nc_game_list_icon );
			img = v.findViewById( R.id.id_nc_game_list_img );
			nameTv = v.findViewById( R.id.id_nc_game_list_gameName );
			welfareTv = v.findViewById( R.id.id_nc_game_list_welfare );
			lableView = v.findViewById( R.id.id_nc_game_list_lableView );
			timeTv = v.findViewById( R.id.id_nc_game_list_time );
			twoStateButton = v.findViewById( R.id.id_nc_game_list_yy );
		}
	}

	public interface OnYYClickListener {
		void onYYClick(GameModel gameModel);
	}
}
