package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OnCommentItemClickListener;
import com.tenone.gamebox.mode.mode.CommentMode;

import java.util.ArrayList;
import java.util.List;

public class CommentReplyAdapter extends RecyclerView.Adapter<CommentReplyAdapter.CRAViewHolder> {
    private List<CommentMode> modes = new ArrayList<CommentMode>();
    private LayoutInflater inflater;
    private Context context;
    private OnCommentItemClickListener onCommentItemClickListener;

    public CommentReplyAdapter(List<CommentMode> modes, Context context) {
        this.modes = modes;
        this.context = context;
        this.inflater = LayoutInflater.from( context );
    }

    @Override
    public CRAViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate( R.layout.item_comment_reply, parent, false );
        CRAViewHolder holder = new CRAViewHolder( view );
        return holder;
    }

    @Override
    public void onBindViewHolder(CRAViewHolder holder, int position) {
        CommentMode mode = modes.get( position );
        holder.nameTv.setText( mode.getreplyNick() );
        holder.contentTv.setText( mode.getComment() );
        holder.timeTv.setText( mode.getCommentTime() );
    }

    @Override
    public int getItemCount() {
        return modes.size();
    }

    public void setOnCommentItemClickListener(OnCommentItemClickListener onCommentItemClickListener) {
        this.onCommentItemClickListener = onCommentItemClickListener;
    }

    public class CRAViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv, contentTv, timeTv, replyTv;
        RelativeLayout rootView;

        public CRAViewHolder(View itemView) {
            super( itemView );
            this.nameTv = itemView.findViewById( R.id.id_item_comment_reply_nameTv1 );
            this.contentTv = itemView.findViewById( R.id.id_item_comment_reply_contentTv );
            this.timeTv = itemView.findViewById( R.id.id_item_comment_reply_timeTv );
            this.replyTv = itemView.findViewById( R.id.id_item_comment_reply_reply );
            rootView = itemView.findViewById( R.id.id_item_comment_reply_root );
            rootView.setOnClickListener( v -> {
                if (onCommentItemClickListener != null) {
                    onCommentItemClickListener.onCommentItemClick( modes.get( getPosition() ) );
                }
            } );
            replyTv.setOnClickListener( v -> {
                if (onCommentItemClickListener != null) {
                    onCommentItemClickListener.onCommentItemClick( modes.get( getPosition() ) );
                }
            } );
        }
    }
}
