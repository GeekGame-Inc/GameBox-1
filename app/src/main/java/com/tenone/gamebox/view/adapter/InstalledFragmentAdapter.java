/**
 * Project Name:GameBox
 * File Name:InstalledFragmentAdapter.java
 * Package Name:com.tenone.gamebox.view.adapter
 * Date:2017-3-20����4:41:12
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.GameButtonClickListener;
import com.tenone.gamebox.mode.listener.GameItemClickListener;
import com.tenone.gamebox.mode.listener.GameItemLongClickListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName:InstalledFragmentAdapter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-20 ����4:41:12 <br/>
 *
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class InstalledFragmentAdapter extends BaseAdapter {
    List<GameModel> items = new ArrayList<GameModel>();
    Context mContext;
    LayoutInflater mInflater;

    GameItemClickListener itemClickListener;
    GameButtonClickListener buttonClickListener;
    GameItemLongClickListener gameItemLongClickListener;

    boolean isUnInstall = false;

    public boolean isUnInstall() {
        return isUnInstall;
    }

    public void setUnInstall(boolean isUnInstall) {
        this.isUnInstall = isUnInstall;
    }

    public void setItemClickListener(GameItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setButtonClickListener(
            GameButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }

    public void setGameItemLongClickListener(
            GameItemLongClickListener gameItemLongClickListener) {
        this.gameItemLongClickListener = gameItemLongClickListener;
    }

    public InstalledFragmentAdapter(List<GameModel> list, Context cxt) {
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        InstalledFragmentHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate( R.layout.item_installed_list,
                    parent, false );
            holder = new InstalledFragmentHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (InstalledFragmentHolder) convertView.getTag();
        }

        final GameModel gameModel = items.get( position );
        holder.button.setOnClickListener( v -> {
            if (buttonClickListener != null) {
                buttonClickListener.gameButtonClick( null, gameModel );
            }
        } );

        holder.layout.setOnClickListener( v -> {
            if (itemClickListener != null) {
                itemClickListener.gameItemClick( gameModel );
            }
        } );

        holder.layout.setOnLongClickListener( v -> {
            if (gameItemLongClickListener != null) {
                gameItemLongClickListener.onGameItemLongClick( position,
                        gameModel );
            }
            return false;
        } );
        String text;
        if (gameModel.isChecked()) {
            text = mContext.getResources().getString( R.string.uninstall );
        } else {
            text = mContext.getResources().getString( R.string.open );
        }
        holder.button.setText( text );
        if (TextUtils.isEmpty( gameModel.getImgUrl() )) {
            holder.img.setBackground( gameModel.getIconDrawable() );
        } else {
            ImageLoadUtils.loadNormalImg( holder.img, mContext,
                    gameModel.getImgUrl() );
        }
        holder.gameName.setText( gameModel.getName() );
        holder.gamePackgeName.setText( gameModel.getPackgeName() );
        holder.gameSize.setText( "v" + gameModel.getVersionsName() );
        return convertView;
    }

    class InstalledFragmentHolder {
        ImageView img;
        TextView gameName;
        TextView gamePackgeName;
        TextView gameSize;
        Button button;
        RelativeLayout layout;

        public InstalledFragmentHolder(View view) {
            init( view );
        }

        private void init(View view) {
            img = view.findViewById( R.id.id_installed_item_gameImg );
            gameName = view
                    .findViewById( R.id.id_installed_item_gameName );
            gamePackgeName = view
                    .findViewById( R.id.id_installed_item_gameVersions );
            gameSize = view
                    .findViewById( R.id.id_installed_item_gameSize );
            button = view.findViewById( R.id.id_installed_item_gameBt );
            layout = view.findViewById( R.id.id_installed_item );
        }
    }
}
