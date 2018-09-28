package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.DynamicCommentModel;

import java.util.ArrayList;
import java.util.List;



public class DynamicListCommentAdapter extends BaseAdapter {
    List<DynamicCommentModel> models = new ArrayList<DynamicCommentModel>();
    Context context;
    LayoutInflater inflater;

    public DynamicListCommentAdapter(List<DynamicCommentModel> models, Context context) {
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
        DLCViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate( R.layout.item_dynamic_list_comment, parent, false );
            holder = new DLCViewHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (DLCViewHolder) convertView.getTag();
        }
        DynamicCommentModel model = models.get( position );
        if (model != null) {
            holder.nickTv.setText( model.getToUserNick() + ":" );
            holder.contentTv.setText( model.getCommentContent() );
        }
        return convertView;
    }

    private class DLCViewHolder {
        TextView nickTv, contentTv;

        public DLCViewHolder(View view) {
            this.nickTv = view.findViewById( R.id.id_dynamic_list_comment_nick );
            this.contentTv = view.findViewById( R.id.id_dynamic_list_comment_content );
        }
    }
}
