package com.tenone.gamebox.mode.mode;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.custom.BadgeView;

public class RemoveItemRecyclerViewHolder extends RecyclerView.ViewHolder {
    public Context context;
    public TextView delete, timeTv, titleTv, contentTv;
    public LinearLayout layout;
    public TextView pointView;
    public BadgeView badgeView;

    public RemoveItemRecyclerViewHolder(Context context,View itemView) {
        super( itemView );
        this.context = context;
        delete = itemView.findViewById( R.id.item_delete );
        timeTv = itemView.findViewById( R.id.item_content_time );
        titleTv = itemView.findViewById( R.id.item_content_title );
        contentTv = itemView.findViewById( R.id.item_content_content );
        layout = itemView.findViewById( R.id.item_layout );
        pointView = itemView.findViewById( R.id.item_content_point );
        badgeView = new BadgeView( context,pointView );
    }
}
