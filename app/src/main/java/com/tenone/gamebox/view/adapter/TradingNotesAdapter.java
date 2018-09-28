package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tenone.gamebox.R;

import java.util.List;

public class TradingNotesAdapter extends BaseAdapter {
    private List<String> array;
    private LayoutInflater inflater;

    public TradingNotesAdapter(Context context, List<String> array) {
        this.array = array;
        this.inflater = LayoutInflater.from( context );
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TNAViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate( R.layout.item_trading_notes, parent, false );
            holder = new TNAViewHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (TNAViewHolder) convertView.getTag();
        }
        holder.tv1.setText( (position + 1) + "" );
        holder.tv2.setText( array.get( position ) );
        return convertView;
    }

    private class TNAViewHolder {

        TextView tv1, tv2;

        public TNAViewHolder(View view) {
            tv1 = view.findViewById( R.id.id_item_trading_notes_index );

            tv2 = view.findViewById( R.id.id_item_trading_notes_content );
        }
    }
}
