package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tenone.gamebox.R;

import java.util.List;

public class TradingCustomerAdapter extends RecyclerView.Adapter<TradingCustomerAdapter.TCAViewHolder> {
    private Context context;
    private List<String> qqArray;
    private LayoutInflater inflater;
    private OnQQClickListener onQQClickListener;

    public TradingCustomerAdapter(Context context, List<String> qqArray) {
        this.context = context;
        this.qqArray = qqArray;
        this.inflater = LayoutInflater.from( context );
    }

    @Override
    public TCAViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TCAViewHolder( inflater.inflate( R.layout.item_trading_custromer, parent, false ) );
    }

    @Override
    public void onBindViewHolder(TCAViewHolder holder, int position) {
        holder.qqTv.setText( "QQ:" + qqArray.get( position ) );
        holder.btTv.setOnClickListener( v -> {
            if (onQQClickListener != null) {
                onQQClickListener.onQQClick( qqArray.get( position ) );
            }
        } );
    }

    public void setOnQQClickListener(OnQQClickListener onQQClickListener) {
        this.onQQClickListener = onQQClickListener;
    }

    @Override
    public int getItemCount() {
        return qqArray.size();
    }

    public class TCAViewHolder extends RecyclerView.ViewHolder {
        TextView qqTv, btTv;

        public TCAViewHolder(View itemView) {
            super( itemView );
            qqTv = itemView.findViewById( R.id.id_item_trading_customer_qq );
            btTv = itemView.findViewById( R.id.id_item_trading_customer_bt );
        }
    }

    public interface OnQQClickListener {
        void onQQClick(String qq);
    }
}
