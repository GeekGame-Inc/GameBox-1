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

public class InTheWareHouseAdapter extends BaseAdapter {
    private List<TradingRecordModel> models;
    private int padding, dp15;
    private LayoutInflater inflater;
    private OnBtClickListener onBtClickListener;
    private boolean isSetPadding = false;

    public InTheWareHouseAdapter(Context context, List<TradingRecordModel> models) {
        this.models = models;
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
        ITWHAViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate( R.layout.item_trading_record_inthewarehouse, parent, false );
            holder = new ITWHAViewHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (ITWHAViewHolder) convertView.getTag();
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
        if (model.isSaleEnabled()) {
            holder.btTv.setVisibility( View.VISIBLE );
            holder.btTv.setOnClickListener( v -> {
                if (onBtClickListener != null) {
                    onBtClickListener.onBtClick( model.getProductId() );
                }
            } );
        } else {
            holder.btTv.setVisibility( View.GONE );
        }
			holder.timeTv.setText( "\u4e0b\u67b6\u65f6\u95f4: " + model.getTime() );
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

    public void setOnBtClickListener(OnBtClickListener onBtClickListener) {
        this.onBtClickListener = onBtClickListener;
    }


    private class ITWHAViewHolder {
        TextView gameNameTv, btTv, titleTv, timeTv, reasonTv, moneyTv;
        RelativeLayout rootView;


        public ITWHAViewHolder(View view) {
            rootView = view.findViewById( R.id.id_item_trading_record_root );
            gameNameTv = view.findViewById( R.id.id_item_trading_record_gameName );
            btTv = view.findViewById( R.id.id_item_trading_record_bt );
            titleTv = view.findViewById( R.id.id_item_trading_record_title );
            moneyTv = view.findViewById( R.id.id_item_trading_record_money );
            timeTv = view.findViewById( R.id.id_item_trading_record_time );
            reasonTv = view.findViewById( R.id.id_item_trading_record_reson );
        }
    }

    public interface OnBtClickListener {
        void onBtClick(String productId);
    }
}
