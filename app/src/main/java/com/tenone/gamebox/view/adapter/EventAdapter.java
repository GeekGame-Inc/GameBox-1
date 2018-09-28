package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.StrategyMode;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends BaseAdapter {
    private List<StrategyMode> modes = new ArrayList<StrategyMode>();
    private Context context;
    private LayoutInflater inflater;

    public EventAdapter(List<StrategyMode> modes, Context context) {
        this.modes = modes;
        this.context = context;
        this.inflater = LayoutInflater.from( context );
    }

    @Override
    public int getCount() {
        return modes.size();
    }

    @Override
    public Object getItem(int position) {
        return modes.get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EAViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate( R.layout.item_event, parent, false );
            holder = new EAViewHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (EAViewHolder) convertView.getTag();
        }
        StrategyMode mode = modes.get( position );
        holder.textView.setText( mode.getStrategyName() );
        return convertView;
    }

    private class EAViewHolder {
        TextView textView;

        public EAViewHolder(View view) {
            textView = view.findViewById( R.id.id_item_event_tv );
        }
    }
}
