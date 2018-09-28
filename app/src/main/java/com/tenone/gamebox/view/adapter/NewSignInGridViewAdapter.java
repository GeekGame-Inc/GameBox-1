package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;

import java.util.List;

public class NewSignInGridViewAdapter extends BaseAdapter {
	private Context context;
	private List<String> items;
	private boolean isShowBg;
	private LayoutInflater inflater;

	public NewSignInGridViewAdapter(Context context, List<String> items, boolean isShowBg) {
		this.context = context;
		this.items = items;
		this.isShowBg = isShowBg;
		this.inflater = LayoutInflater.from( context );
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get( position );
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate( R.layout.item_new_grid, parent, false );
			holder = new ViewHolder( convertView );
			convertView.setTag( holder );
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (isShowBg) {
			holder.textView.setBackground( ContextCompat.getDrawable( context, R.drawable.d_icon_shuzidikuang ) );
			holder.leftImage.setVisibility( View.VISIBLE );
		}
		holder.textView.setText( items.get( position ) );
		return convertView;
	}

	private class ViewHolder {
		private TextView textView;
		private ImageView leftImage;

		public ViewHolder(View itemView) {
			this.textView = itemView.findViewById( R.id.id_item_new_sign_tv );
			this.leftImage = itemView.findViewById( R.id.id_item_new_sign_left );
		}
	}
}
