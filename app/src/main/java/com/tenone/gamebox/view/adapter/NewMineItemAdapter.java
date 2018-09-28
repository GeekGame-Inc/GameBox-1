package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OnMineItemClickListener;
import com.tenone.gamebox.mode.mode.MineItemModel;

import java.util.List;

public class NewMineItemAdapter extends RecyclerView.Adapter<NewMineItemAdapter.MIAViewHolder> {
    private List<MineItemModel> itemModels;
    private Context mContext;
    private LayoutInflater inflater;
    private OnMineItemClickListener onMineItemClickListener;

    public NewMineItemAdapter(List<MineItemModel> itemModels, Context mContext) {
        this.itemModels = itemModels;
        this.mContext = mContext;
        this.inflater = LayoutInflater.from( mContext );
    }

    @Override
    public MIAViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MIAViewHolder holder = null;
        View view = inflater.inflate( R.layout.item_new_mine, parent, false );
        holder = new MIAViewHolder( view );
        return holder;
    }


    @Override
    public void onBindViewHolder(MIAViewHolder holder, int position) {
        MineItemModel model = itemModels.get( position );
        holder.itemNameTv.setText( model.getItemName() );
        holder.line.setVisibility( model.isShowLine() ? View.VISIBLE : View.GONE );
        BitmapDrawable drawable = (BitmapDrawable) mContext.getResources()
                .getDrawable( model.getDrawableId() );
        holder.iconIv.setImageBitmap( drawable.getBitmap() );
        String text = model.getText();
        if (!TextUtils.isEmpty( text )) {
            if (model.isHtml()) {
                holder.textView.setText( Html.fromHtml( model.getText() ) );
            } else {
                holder.textView.setText( model.getText() );
            }
        } else {
            holder.textView.setText( "" );
        }
        holder.rootLayout.setOnClickListener( v -> {
            if (onMineItemClickListener != null) {
                onMineItemClickListener.onMineItemClic( position );
            }
        } );
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    public void setOnMineItemClickListener(OnMineItemClickListener onMineItemClickListener) {
        this.onMineItemClickListener = onMineItemClickListener;
    }

    public class MIAViewHolder extends RecyclerView.ViewHolder {
        private View line;
        private ImageView iconIv;
        private TextView itemNameTv, textView;
        private LinearLayout rootLayout;

        public MIAViewHolder(View itemView) {
            super( itemView );
            line = itemView.findViewById( R.id.id_new_mine_line );
            iconIv = itemView.findViewById( R.id.id_new_mine_iv );
            itemNameTv = itemView.findViewById( R.id.id_new_mine_tv );
            textView = itemView.findViewById( R.id.id_new_mine_tv1 );
            rootLayout = itemView.findViewById( R.id.id_new_mine_layout );
        }
    }
}
