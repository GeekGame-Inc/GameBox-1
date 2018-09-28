/**
 * Project Name:GameBox
 * File Name:UpdateFragmentAdapter.java
 * Package Name:com.tenone.gamebox.view.adapter
 * Date:2017-3-20����6:08:19
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
import com.tenone.gamebox.mode.listener.GameButtonClickListener;
import com.tenone.gamebox.mode.listener.GameItemClickListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.view.custom.DownloadProgressBar;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;

/**
 * ClassName:UpdateFragmentAdapter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-20 ����6:08:19 <br/>
 *
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class UpdateFragmentAdapter extends BaseAdapter {

    List<GameModel> items;
    Context mContext;
    LayoutInflater mInflater;

    GameButtonClickListener buttonClickListener;
    GameItemClickListener itemClickListener;

    public void setButtonClickListener(
            GameButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }

    public void setItemClickListener(GameItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public UpdateFragmentAdapter(List<GameModel> list, Context cxt) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        UpdateFragmentHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate( R.layout.item_update_fragment,
                    parent, false );
            holder = new UpdateFragmentHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (UpdateFragmentHolder) convertView.getTag();
        }
        final GameModel gameModel = items.get( position );
        holder.layout.setOnClickListener( v -> {
            if (itemClickListener != null) {
                itemClickListener.gameItemClick( gameModel );
            }
        } );
        final DownloadProgressBar downProgress = holder.downProgress;
        downProgress.setOnClickListener( v -> {
            if (buttonClickListener != null) {
                buttonClickListener
                        .gameButtonClick( downProgress, gameModel );
            }
        } );
        downProgress.reset();
        downProgress.setStae( gameModel.getStatus() );
        downProgress.setProgress( gameModel.getProgress() );
        holder.gameName.setText( gameModel.getName() );
        holder.gameVersion.setText( "v" + gameModel.getVersionsName() );
			holder.gameSize.setText( gameModel.getTimes() + "\u4e0b\u8f7d" );
        ImageLoadUtils.loadNormalImg( holder.img, mContext, gameModel.getImgUrl() );
        return convertView;
    }

    class UpdateFragmentHolder {
        RelativeLayout layout;
        ImageView img;
        TextView gameName;
        TextView gameVersion;
        TextView gameSize;
        DownloadProgressBar downProgress;

        public UpdateFragmentHolder(View view) {
            init( view );
        }

        private void init(View view) {
            layout = view.findViewById( R.id.id_update_item );
            img = view.findViewById( R.id.id_update_item_gameImg );
            gameName = view
                    .findViewById( R.id.id_update_item_gameName );
            gameVersion = view
                    .findViewById( R.id.id_update_item_gameVersions );
            gameSize = view
                    .findViewById( R.id.id_update_item_gameSize );
            downProgress = view
                    .findViewById( R.id.id_update_item_gameBt );
        }
    }
}
