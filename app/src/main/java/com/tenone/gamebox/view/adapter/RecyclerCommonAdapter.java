package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tenone.gamebox.mode.listener.OnRecyclerViewItemClickListener;
import com.tenone.gamebox.mode.mode.RecyclerViewHolder;

import java.util.List;

/**
 * Created by Eddy on 2018/1/11.
 */

public abstract class RecyclerCommonAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder> {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private int layoutId;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private View view;

    public RecyclerCommonAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from( context );
        this.mDatas = datas;
        this.layoutId = layoutId;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = mInflater.inflate( layoutId, parent, false );
        RecyclerViewHolder holder = RecyclerViewHolder.get( mContext, view );
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        convert( holder, mDatas.get( position ) );
        view.setOnClickListener( v -> {
						if (onRecyclerViewItemClickListener != null) {
								onRecyclerViewItemClickListener.onRecyclerViewItemClick( mDatas.get( position ) );
						}
				} );
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public abstract void convert(RecyclerViewHolder holder, T t);

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }
}
