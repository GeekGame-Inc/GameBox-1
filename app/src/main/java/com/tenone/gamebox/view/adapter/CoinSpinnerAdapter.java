package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;

public class CoinSpinnerAdapter implements SpinnerAdapter {
	private String[] coins;
	private Context context;
	private LayoutInflater inflater;

	public CoinSpinnerAdapter(String[] coins, Context context) {
		this.coins = coins;
		this.context = context;
		this.inflater = LayoutInflater.from( context );
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		CSAViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate( R.layout.item_coin_spinner, parent, false );
			holder = new CSAViewHolder( convertView );
			convertView.setTag( holder );
		} else {
			holder = (CSAViewHolder) convertView.getTag();
		}
		holder.textView.setText( coins[position] );
		return convertView;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {

	}

	@Override
	public int getCount() {
		return coins.length;
	}

	@Override
	public Object getItem(int position) {
		return coins[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CSAViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate( R.layout.item_coin_spinner, parent, false );
			holder = new CSAViewHolder( convertView );
			convertView.setTag( holder );
		} else {
			holder = (CSAViewHolder) convertView.getTag();
		}
		holder.textView.setText( coins[position] );
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		return 1;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return !(coins == null && coins.length > 0);
	}


	private class CSAViewHolder {
		@ViewInject(R.id.id_item_coin_spinner_tv)
		TextView textView;

		public CSAViewHolder(View view) {
			ViewUtils.inject( this, view );
		}
	}
}
