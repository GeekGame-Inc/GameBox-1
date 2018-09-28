package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.MessageModel;
import com.tenone.gamebox.mode.mode.RemoveItemRecyclerViewHolder;

import java.util.ArrayList;


public class MessageListAdapter extends RecyclerView.Adapter<RemoveItemRecyclerViewHolder> {

    private ArrayList<MessageModel> models = new ArrayList<MessageModel>();
    private Context context;
    private LayoutInflater mInflater;

    public MessageListAdapter(ArrayList<MessageModel> list, Context context) {
        this.models = list;
        this.context = context;
        this.mInflater = LayoutInflater.from( context );
    }

    @Override
    public RemoveItemRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate( R.layout.item_remove_item_recycler, parent, false );
        RemoveItemRecyclerViewHolder holder = new RemoveItemRecyclerViewHolder( context, view );
        return holder;
    }

    @Override
    public void onBindViewHolder(RemoveItemRecyclerViewHolder holder, int position) {
        MessageModel model = models.get( position );
        holder.contentTv.setText( model.getContent() );
        holder.timeTv.setText( model.getTimeStr() );
        holder.titleTv.setText( model.getTitle() );
        if (!model.isRead()) {
            holder.badgeView.show();
        } else {
            holder.badgeView.hide();
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
