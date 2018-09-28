/**
 * Project Name:GameBox
 * File Name:DownManageFragmentAdapter.java
 * Package Name:com.tenone.gamebox.view.adapter
 * Date:2017-3-20����11:03:26
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.GameButtonClickListener;
import com.tenone.gamebox.mode.listener.GameItemClickListener;
import com.tenone.gamebox.mode.listener.GameItemLongClickListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.view.custom.DownloadProgressBar;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;

public class DownManageFragmentAdapter extends BaseAdapter {
    Context mContext;
    List<GameModel> items;
    LayoutInflater mInflater;
    GameButtonClickListener buttonClickListener;
    // �Ƿ���ʾѡ���
    boolean isVisibility = false;

    GameItemLongClickListener gameItemLongClickListener;
    GameItemClickListener itemClickListener;

    public boolean isVisibility() {
        return isVisibility;
    }

    public void setVisibility(boolean isVisibility) {
        this.isVisibility = isVisibility;
        notifyDataSetChanged();
    }

    public DownManageFragmentAdapter(Context cxt, List<GameModel> list) {
        this.mContext = cxt;
        this.items = list;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        DownManageFragmentHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate( R.layout.item_down_manage, parent,
                    false );
            holder = new DownManageFragmentHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (DownManageFragmentHolder) convertView.getTag();
        }

        final GameModel gameMode = items.get( position );
        if (isVisibility) {
            holder.checkBox.setVisibility( View.VISIBLE );
            holder.progress.setVisibility( View.GONE );
        } else {
            holder.checkBox.setVisibility( View.GONE );
            holder.progress.setVisibility( View.VISIBLE );
        }

        holder.checkBox.setChecked( gameMode.isChecked() );
        holder.layout.setOnClickListener( v -> {
            if (itemClickListener != null) {
                itemClickListener.gameItemClick( gameMode );
            }
        } );
        holder.layout.setOnLongClickListener( v -> {
            if (gameItemLongClickListener != null) {
                gameItemLongClickListener.onGameItemLongClick( position,
                        gameMode );
            }
            return false;
        } );
        final DownloadProgressBar downProgress = holder.progress;
        holder.progress.setOnClickListener( v -> {
            if (buttonClickListener != null) {
                buttonClickListener.gameButtonClick( downProgress, gameMode );
            }
        } );
        holder.progress.reset();
        holder.progress.setStae( gameMode.getStatus() );
        holder.progress.setProgress( gameMode.getProgress() );
        holder.gameNameTv.setText( gameMode.getName() );
        holder.gameSizeTv.setText( gameMode.getSize() + "M" );
        holder.gameVersionsTv.setText( "v" + gameMode.getVersionsName() );
        ImageLoadUtils.loadNormalImg( holder.gameImg, mContext, gameMode.getImgUrl() );
        return convertView;
    }

    public void deleteItem(int index) {
        if (!items.isEmpty() && index < items.size()) {
            items.remove( index );
            notifyDataSetChanged();
        }
    }

    public void setButtonClickListener(
            GameButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }

    public void setGameItemLongClickListener(
            GameItemLongClickListener gameItemLongClickListener) {
        this.gameItemLongClickListener = gameItemLongClickListener;
    }

    public void setItemClickListener(GameItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    class DownManageFragmentHolder {
        CheckBox checkBox;
        ImageView gameImg;
        TextView gameNameTv;
        TextView gameVersionsTv;
        TextView gameSizeTv;
        DownloadProgressBar progress;
        RelativeLayout layout;

        public DownManageFragmentHolder(View convertView) {
            initView( convertView );
        }

        private void initView(View convertView) {
            gameImg = convertView
                    .findViewById( R.id.id_downManage_item_gameImg );
            gameNameTv = convertView
                    .findViewById( R.id.id_downManage_item_gameName );
            gameVersionsTv = convertView
                    .findViewById( R.id.id_downManage_item_gameVersions );
            gameSizeTv = convertView
                    .findViewById( R.id.id_downManage_item_gameSize );
            progress = convertView
                    .findViewById( R.id.id_downManage_item_gameBt );
            layout = convertView
                    .findViewById( R.id.id_downManage_item );
            checkBox = convertView
                    .findViewById( R.id.id_downManage_item_checkBox );
        }
    }
}
