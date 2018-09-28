package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.TradingRecordModel;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;

import java.util.List;

public class TradingRecordAdapter extends BaseAdapter {
    private Context context;
    private List<TradingRecordModel> models;
    private int type, padding, dp15;
    private LayoutInflater inflater;
    private OnBtClickListener onBtClickListener;
    private boolean isSetPadding = false;

    public TradingRecordAdapter(Context context, List<TradingRecordModel> models, int type) {
        this.context = context;
        this.models = models;
        this.type = type;
        this.inflater = LayoutInflater.from( context );
        dp15 = DisplayMetricsUtils.dipTopx( context, 15 );
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
        TRAViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate( R.layout.item_trading_record, parent, false );
            holder = new TRAViewHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (TRAViewHolder) convertView.getTag();
        }
        TradingRecordModel model = models.get( position );
        holder.gameNameTv.setText( "\u6e38\u620f\u540d: " + model.getGameName() );
        holder.titleTv.setText( model.getTitle() );
        holder.moneyTv.setText( "\u91d1\u989d: " + model.getMoney() );
        String reason = model.getReason();
        if (!TextUtils.isEmpty( reason )) {
            holder.reasonTv.setVisibility( View.VISIBLE );
            holder.reasonTv.setText( "\u5931\u8d25\u539f\u56e0: " + reason );
        } else {
            holder.reasonTv.setVisibility( View.GONE );
        }
        if (type == 0) {
            holder.statusTv.setVisibility( View.VISIBLE );
            holder.statusTv.setText( getStatus( model.getStatus() ) );
        } else {
            holder.statusTv.setVisibility( View.GONE );
        }

        holder.btTv.setVisibility( (type == 2 || type == 3 || type == 4) ? View.VISIBLE : View.GONE );
        holder.btTv.setOnClickListener( v -> {
            if (onBtClickListener != null) {
                onBtClickListener.onBtClick( model.getProductId(),position );
            }
        } );
        if (model.isDelete()) {
            holder.btTv.setText( context.getString( R.string.delete ) );
        } else {
            holder.btTv.setText( type == 2 ? "\u4e0b\u67b6" : type == 3 ?
								context.getString( R.string.delete )  : type == 4 ?
								"\u91cd\u65b0\u4e0a\u67b6" : "" );
        }
        holder.timeTv.setText( getText() + model.getTime() );
        if (isSetPadding && padding > 0) {
            holder.rootView.setPadding( padding, dp15, padding, dp15 );
        }
        return convertView;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public void setSetPadding(boolean setPadding) {
        isSetPadding = setPadding;
    }


    private String getText() {
        String text = "";
        switch (type) {
					case 0:
						text = "\u4e0b\u5355\u65f6\u95f4: ";
						break;
					case 1:
						text = "\u63d0\u4ea4\u65f6\u95f4: ";
						break;
					case 2:
						text = "\u4e0a\u67b6\u65f6\u95f4: ";
						break;
					case 3:
						text = "\u51fa\u552e\u65f6\u95f4: ";
						break;
					case 4:
						text = "\u4e0b\u67b6\u65f6\u95f4: ";
						break;
        }
        return text;
    }

    public void setOnBtClickListener(OnBtClickListener onBtClickListener) {
        this.onBtClickListener = onBtClickListener;
    }


    private class TRAViewHolder {
        TextView statusTv, gameNameTv, btTv, titleTv, timeTv, reasonTv, moneyTv;
        RelativeLayout rootView;


        public TRAViewHolder(View view) {
            rootView = view.findViewById( R.id.id_item_trading_record_root );
            statusTv = view.findViewById( R.id.id_item_trading_record_status );
            gameNameTv = view.findViewById( R.id.id_item_trading_record_gameName );
            btTv = view.findViewById( R.id.id_item_trading_record_bt );
            titleTv = view.findViewById( R.id.id_item_trading_record_title );
            moneyTv = view.findViewById( R.id.id_item_trading_record_money );
            timeTv = view.findViewById( R.id.id_item_trading_record_time );
            reasonTv = view.findViewById( R.id.id_item_trading_record_reson );
        }
    }

    private String getStatus(int status) {
        String text = "";
        switch (status) {
					case 1:
						text = "\u652f\u4ed8\u6210\u529f";
						break;
					case 4:
						text = "\u4ea4\u6613\u5b8c\u6210";
						break;
					case 3:
						text = "\u4ea4\u6613\u53d6\u6d88";
						break;
					case 2:
						text = "\u7b49\u5f85\u6253\u6b3e";
						break;
        }
        return text;
    }

    public interface OnBtClickListener {
        void onBtClick(String productId, int position);
    }
}
