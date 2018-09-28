/**
 * Project Name:GameBox
 * File Name:AttentionAdapter.java
 * Package Name:com.tenone.gamebox.view.adapter
 * Date:2017-3-24����3:25:06
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.CancleAttentionListener;
import com.tenone.gamebox.mode.listener.GameButtonClickListener;
import com.tenone.gamebox.mode.listener.GameItemClickListener;
import com.tenone.gamebox.mode.listener.GameItemLongClickListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.view.custom.DownloadProgressBar;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class AttentionAdapter extends BaseAdapter {

    List<GameModel> items = new ArrayList<GameModel>();

    Context mContext;
    LayoutInflater mInflater;
    GameButtonClickListener buttonClickListener;
    GameItemClickListener itemClickListener;
    GameItemLongClickListener itemLongClickListener;
    CancleAttentionListener cancleAttentionListener;
    int currentPosition = -1;

    public AttentionAdapter(Context cxt, List<GameModel> list) {
        this.mContext = cxt;
        this.mInflater = LayoutInflater.from( mContext );
        this.items = list;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int arg0) {
        return items.get( arg0 );
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AttentionHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate( R.layout.item_attention_list,
                    parent, false );
            holder = new AttentionHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (AttentionHolder) convertView.getTag();
        }
        holder.cancleTv
                .setVisibility( currentPosition == position ? View.VISIBLE
                        : View.GONE );
        final GameModel model = items.get( position );
        holder.layout.setOnClickListener( v -> {
            if (itemClickListener != null) {
                itemClickListener.gameItemClick( model );
            }
        } );
        holder.layout.setOnLongClickListener( v -> {
            if (itemLongClickListener != null) {
                itemLongClickListener.onGameItemLongClick( position, model );
            }
            return false;
        } );

        holder.cancleTv.setOnClickListener( v -> {
            if (cancleAttentionListener != null) {
                cancleAttentionListener.cancleAttention( model, position );
            }
        } );

        final DownloadProgressBar downProgress = holder.downProgress;
        downProgress.setOnClickListener( v -> {
            if (buttonClickListener != null) {
                buttonClickListener.gameButtonClick( downProgress, model );
            }
        } );
        downProgress.reset();
        downProgress.setStae( model.getStatus() );
        downProgress.setProgress( model.getProgress() );
        holder.gameGradeBar.setRating( model.getGrade() );
        holder.gameNameTv.setText( model.getName() );
			holder.gameTimesTv.setText( model.getTimes() + "\u4e0b\u8f7d   " + model.getSize() + "M" );
        ImageLoadUtils.loadNormalImg( holder.gameImg, mContext,
                model.getImgUrl() );
        return convertView;
    }

    public void setOnButtonClick(GameButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    public void setOnItemClick(GameItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setItemLongClickListener(
            GameItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    public void setCancleAttentionListener(
            CancleAttentionListener cancleAttentionListener) {
        this.cancleAttentionListener = cancleAttentionListener;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    class AttentionHolder {
        ImageView gameImg;
        TextView gameNameTv, gameTimesTv, cancleTv;
        RatingBar gameGradeBar;
        DownloadProgressBar downProgress;
        RelativeLayout layout;

        public AttentionHolder(View convertView) {
            initView( convertView );
        }

        private void initView(View convertView) {
            downProgress = convertView
                    .findViewById( R.id.id_attention_item_gameBt );
            gameGradeBar = convertView
                    .findViewById( R.id.id_attention_item_gameRatingBar );
            layout = convertView
                    .findViewById( R.id.id_attention_item );
            gameImg = convertView
                    .findViewById( R.id.id_attention_item_gameImg );
            gameNameTv = convertView
                    .findViewById( R.id.id_attention_item_gameName );
            gameTimesTv = convertView
                    .findViewById( R.id.id_attention_item_gameDownTimes );
            cancleTv = convertView
                    .findViewById( R.id.id_attention_item_cancleAttention );
        }

    }
}
