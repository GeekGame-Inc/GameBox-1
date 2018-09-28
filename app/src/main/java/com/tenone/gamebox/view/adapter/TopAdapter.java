package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OnTopGetClickListener;
import com.tenone.gamebox.mode.mode.TopModel;
import com.tenone.gamebox.view.custom.CircleImageView;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class TopAdapter extends BaseAdapter {
    private Context context;
    private List<TopModel> models = new ArrayList<TopModel>();
    private LayoutInflater inflater;


    private boolean isToday = false;
    private OnTopGetClickListener onTopGetClickListener;

    public TopAdapter(Context context, List<TopModel> models, boolean isToday) {
        this.context = context;
        this.models = models;
        this.inflater = LayoutInflater.from( context );
        this.isToday = isToday;
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
        TopViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate( R.layout.item_fragment_top, parent, false );
            holder = new TopViewHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (TopViewHolder) convertView.getTag();
        }
        TopModel model = models.get( position );
        ImageLoadUtils.loadCircleImg( context, model.getIcon(), holder.headerIv );
        holder.usernameTv.setText( model.getUserName() );

        if (model.getSex() == 1 || model.getSex() == 2) {
            holder.sexTv.setVisibility( View.VISIBLE );
            holder.sexTv.setSelected( model.getSex() == 2 );
        } else {
            holder.sexTv.setVisibility( View.GONE );
        }

        holder.vipIv.setVisibility( model.isVip() == 1 ? View.VISIBLE : View.GONE );
        holder.countTv.setText( model.getCount() );
        switch (model.getRanking()) {
            case "1":
                holder.rankingTv.setText( "" );
                holder.rankingTv.setBackground(ContextCompat.getDrawable( context, R.drawable.icon_top_first ) );
                break;
            case "2":
                holder.rankingTv.setText( "" );
                holder.rankingTv.setBackground( ContextCompat.getDrawable( context,R.drawable.icon_top_second ) );
                break;
            case "3":
                holder.rankingTv.setText( "" );
                holder.rankingTv.setBackground( ContextCompat.getDrawable( context, R.drawable.icon_top_thirdly ) );
                break;
            default:
                holder.rankingTv.setText( model.getRanking() );
                holder.rankingTv.setBackground( ContextCompat.getDrawable( context, R.drawable.icon_top_other ) );
                break;
        }

        if (model.getUid().equals( SpUtil.getUserId() ) && !isToday) {
            if (model.isGot() == 0) {
							holder.coinTv.setText( model.getCoin() + "\n\u9886\u53d6" );
                holder.coinTv.setTextColor( context.getResources().getColor( R.color.rebateColor ) );
               /* holder.coinTv.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onTopGetClickListener != null) {
                            onTopGetClickListener.onGetClick( position );
                        }
                    }
                } );*/
                holder.coinTv.setOnClickListener( view -> {
                    if (onTopGetClickListener != null) {
                        onTopGetClickListener.onGetClick( position );
                    }
                } );
            } else {
							holder.coinTv.setText( model.getCoin() + "\n\u5df2\u9886\u53d6" );
                holder.coinTv.setTextColor( context.getResources().getColor( R.color.textColor1 ) );
                holder.coinTv.setOnClickListener( null );
            }
        } else {
            holder.coinTv.setText( model.getCoin() );
            holder.coinTv.setTextColor( context.getResources().getColor( R.color.textColor1 ) );
            holder.coinTv.setOnClickListener( null );
        }
        return convertView;
    }

    public void setOnTopGetClickListener(OnTopGetClickListener onTopGetClickListener) {
        this.onTopGetClickListener = onTopGetClickListener;
    }

    private class TopViewHolder {
        TextView rankingTv, usernameTv, countTv, coinTv;
        ImageView sexTv, vipIv;
        CircleImageView headerIv;

        public TopViewHolder(View view) {
            this.rankingTv = view.findViewById( R.id.id_item_top_ranking );
            this.usernameTv = view.findViewById( R.id.id_item_top_username );
            this.countTv = view.findViewById( R.id.id_item_top_count );
            this.vipIv = view.findViewById( R.id.id_item_top_vip );
            this.coinTv = view.findViewById( R.id.id_item_top_coin );
            this.sexTv = view.findViewById( R.id.id_item_top_sex );
            this.headerIv = view.findViewById( R.id.id_item_top_header );
        }
    }
}
