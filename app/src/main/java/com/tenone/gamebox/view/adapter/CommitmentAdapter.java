package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OnRecyclerViewItemClickListener;
import com.tenone.gamebox.mode.mode.CommitmentModel;

import java.util.List;

public class CommitmentAdapter extends RecyclerView.Adapter<CommitmentAdapter.CAViewHolder> {

    private List<CommitmentModel> models;
    private Context context;
    private LayoutInflater inflater;
    private OnRecyclerViewItemClickListener<CommitmentModel> onRecyclerViewItemClickListener;

    public CommitmentAdapter(List<CommitmentModel> models, Context context) {
        this.models = models;
        this.inflater = LayoutInflater.from( context );
        this.context = context;
    }

    @Override
    public CAViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CAViewHolder( inflater.inflate( R.layout.item_commitment, parent, false ) );
    }

    @Override
    public void onBindViewHolder(CAViewHolder holder, int position) {
        CommitmentModel model = models.get( position );
        holder.titleTv.setText( model.getTitle() );
        String content = model.getContent();
        if (!TextUtils.isEmpty( content )) {
            String[] array = content.split( "\\|\\|\\|" );
            if (array != null) {
                holder.contenTv.setText( array[0] );
                if (array.length > 1) {
                    holder.textView.setText( Html.fromHtml( context.getResources()
                            .getString( R.string.commitment_txt,
																array[1], model.getqQ() ) ) );
                }
            }
        }
       holder.textView.setOnClickListener( v -> {
            if (onRecyclerViewItemClickListener != null) {
                onRecyclerViewItemClickListener.onRecyclerViewItemClick( model );
            }
        } );
    }


    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener<CommitmentModel> onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class CAViewHolder extends RecyclerView.ViewHolder {
        TextView titleTv, contenTv, textView;

        public CAViewHolder(View itemView) {
            super( itemView );
            textView = itemView.findViewById( R.id.id_text );
            titleTv = itemView.findViewById( R.id.id_item_commitment_title );
            contenTv = itemView.findViewById( R.id.id_item_commitment_content );
        }
    }
}
