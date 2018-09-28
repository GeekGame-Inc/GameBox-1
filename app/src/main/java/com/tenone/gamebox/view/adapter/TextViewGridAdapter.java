package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tenone.gamebox.R;

import java.util.List;


public class TextViewGridAdapter extends BaseAdapter {
	private Context context;
	private List<String> texts;
	private int[] rids = {R.drawable.selector_good, R.drawable.selector_disgood,
			R.drawable.icon_dynamic_share, R.drawable.icon_dynamic_comment};
	private LayoutInflater inflater;
	private boolean isGood, isDisGood;
	private boolean isStrategy = false;

	public TextViewGridAdapter(Context context, List<String> texts) {
		this.context = context;
		this.texts = texts;
		this.inflater = LayoutInflater.from( context );
	}

	@Override
	public int getCount() {
		return texts.size();
	}

	@Override
	public Object getItem(int position) {
		return texts.get( position );
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate( R.layout.item_textview, parent, false );
			holder = new MyViewHolder( convertView );
			convertView.setTag( holder );
		} else {
			holder = (MyViewHolder) convertView.getTag();
		}
		Drawable drawable = context.getResources().getDrawable( rids[position] );
		drawable.setBounds( 0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight() );
		holder.textView.setCompoundDrawables( drawable, null, null, null );
		holder.textView.setText( texts.get( position ) );
		if (!isStrategy) {
			switch (position) {
				case 0:
					holder.textView.setSelected( isGood );
					break;
				case 1:
					holder.textView.setSelected( isDisGood );
					break;
				default:
					holder.textView.setSelected( false );
					break;
			}
		} else {
			switch (position) {
				case 1:
					holder.textView.setSelected( isGood );
					break;
				case 2:
					holder.textView.setSelected( isDisGood );
					break;
				default:
					holder.textView.setSelected( false );
					break;
			}
		}
		return convertView;
	}


	public void setGood(boolean good) {
		isGood = good;
		notifyDataSetChanged();
	}

	public void setDisGood(boolean disGood) {
		isDisGood = disGood;
		notifyDataSetChanged();
	}

	public void setStrategy(boolean strategy) {
		isStrategy = strategy;
	}

	public void setRids(int[] rids) {
		this.rids = rids;
	}

	private class MyViewHolder {
		TextView textView;

		public MyViewHolder(View view) {
			this.textView = view.findViewById( R.id.id_item_textview_tv );
		}
	}
}
