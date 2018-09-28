/**
 * Project Name:GameBox
 * File Name:GameGiftListAdapter.java
 * Package Name:com.tenone.gamebox.view.adapter
 * Date:2017-3-10����3:46:34
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.GameGiftButtonClickListener;
import com.tenone.gamebox.mode.listener.OnGiftItemClickListener;
import com.tenone.gamebox.mode.mode.GiftMode;
import com.tenone.gamebox.view.custom.TwoStateButton;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class GameGiftListAdapter extends BaseAdapter {
    private List<GiftMode> items = new ArrayList<GiftMode>();
    private Context mContext;
    private LayoutInflater mInflater;
    private GameGiftButtonClickListener buttonClickListener;
    private OnGiftItemClickListener onGiftItemClickListener;

    public GameGiftListAdapter(List<GiftMode> list, Context cxt) {
        this.items = list;
        this.mContext = cxt;
        this.mInflater = LayoutInflater.from( mContext );
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<GiftMode> list) {
        this.items = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GiftViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate( R.layout.item_gift_list, parent,
                    false );
            holder = new GiftViewHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (GiftViewHolder) convertView.getTag();
        }
        final GiftMode giftMode = items.get( position );
        holder.giftNum.setText( ":" + giftMode.getGiftCounts() );
        holder.gameName.setText( giftMode.getGiftName() );
        holder.residueTv.setText( "\u5269\u4f59: " + giftMode.getResidue() );
        holder.stateButton.setState( giftMode.getState() );
        holder.stateButton.setOnClickListener( v -> {
            if (buttonClickListener != null) {
                buttonClickListener.onButtonClick( giftMode );
            }
        } );
        ImageLoadUtils.loadNormalImg( holder.imageView, mContext,
                giftMode.getGiftImgUrl() );
        holder.rootView.setOnClickListener( v -> {
            if (onGiftItemClickListener != null) {
                onGiftItemClickListener.onGiftItemClick(giftMode);
            }
        } );
        return convertView;
    }

    public void setButtonClickListener(GameGiftButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }

    public void setOnGiftItemClickListener(OnGiftItemClickListener onGiftItemClickListener) {
        this.onGiftItemClickListener = onGiftItemClickListener;
    }

    class GiftViewHolder {
        ImageView imageView;
        TextView gameName;
        TextView giftNum;
        TextView residueTv;
        TwoStateButton stateButton;
        RelativeLayout rootView;

        public GiftViewHolder(View view) {
            init( view );
        }

        private void init(View view) {
            rootView = view.findViewById( R.id.id_gift_list_root );
            imageView = view
                    .findViewById( R.id.id_gift_list_giftImg );
            gameName = view.findViewById( R.id.id_gift_list_giftName );
            giftNum = view.findViewById( R.id.id_gift_list_giftNum );
            stateButton = view
                    .findViewById( R.id.id_gift_list_button );
            residueTv = view.findViewById( R.id.id_gift_list_residue );
        }
    }
}
