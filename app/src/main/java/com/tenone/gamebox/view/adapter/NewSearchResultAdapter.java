package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.GameModel;

import java.util.List;

public class NewSearchResultAdapter extends BaseAdapter {

	private List<GameModel> models;

	private Context context;

	private LayoutInflater inflater;

	public NewSearchResultAdapter(List<GameModel> models, Context context) {
		this.models = models;
		this.context = context;
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
		NSRAViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate( R.layout.item_new_search_result, parent, false );
			holder = new NSRAViewHolder( convertView );
			convertView.setTag( holder );
		} else {
			holder = (NSRAViewHolder) convertView.getTag();
		}
		GameModel model = models.get( position );
		holder.leableTv.setSelected( 1 == model.getPlatform() );
		holder.leableTv.setText( 1 == model.getPlatform() ? "BT\u624b\u6e38" : 2 == model.getPlatform() ? "\u6298\u6263\u624b\u6e38" : "H5\u624b\u6e38" );
		String dis = model.getDis();
		try {
			float f = Float.valueOf( dis ).floatValue();
			if (f > 0) {
				dis = dis + "\u6298";
			} else {
				dis = "";
			}
		} catch (NumberFormatException e) {
			dis = "";
		}
		holder.disTv.setText( dis );
		holder.nameTv.setText( model.getName() );
		return convertView;
	}

	private class NSRAViewHolder {

		TextView nameTv, leableTv, disTv;

		public NSRAViewHolder(View view) {
			nameTv = view.findViewById( R.id.id_item_new_search_result_name );
			leableTv = view.findViewById( R.id.id_item_new_search_result_leable );
			disTv = view.findViewById( R.id.id_item_new_search_result_dis );
		}
	}
}
