/**
 * Project Name:GameBox
 * File Name:GameRecommendFragmentAdapter.java
 * Package Name:com.tenone.gamebox.view.adapter
 * Date:2017-3-8上午11:02:49
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.GameButtonClickListener;
import com.tenone.gamebox.mode.listener.GameItemClickListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ViewHolder")
public class GameRecommendFragmentAdapter extends BaseAdapter {
    private List<GameModel> items = new ArrayList<GameModel>();

    private Context mContext;
    private LayoutInflater mInflater;
    GameButtonClickListener buttonClickListener;
    private GameItemClickListener itemClickListener;


    public GameRecommendFragmentAdapter(Context cxt, List<GameModel> list) {
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
        GameViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate( R.layout.item_game_list, parent,
                    false );
            holder = new GameViewHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (GameViewHolder) convertView.getTag();
        }

        ViewGroup.LayoutParams imgparams = holder.gameImg.getLayoutParams();
        imgparams.width = DisplayMetricsUtils.getScreenHeight( mContext ) /
                (DisplayMetricsUtils.getScreenHeight( mContext ) /
                        DisplayMetricsUtils.getScreenWidth( mContext ) == 16 / 9 ? 11 : 12);
        imgparams.height = imgparams.width;
        holder.gameImg.setLayoutParams( imgparams );
        setData( position, holder );
        return convertView;
    }

    @SuppressWarnings("deprecation")
    private void setData(final int position, final GameViewHolder holder) {
        final GameModel model = items.get( position );
        int type = model.getType();
        if (type == 0) {
            holder.topLayout.setVisibility( View.VISIBLE );
            holder.timeTv.setText( model.getTime() );
            holder.layout.setVisibility( View.GONE );
        } else {
            holder.topLayout.setVisibility( View.GONE );
            holder.layout.setVisibility( View.VISIBLE );
            holder.gameNameTv.setText( model.getName() );
            holder.gameSizeTv.setText( model.getSize() + "M" );
            holder.gameTimesTv.setText( model.getContent() );
            ImageLoadUtils.loadNormalImg( holder.gameImg, mContext,
                    model.getImgUrl() );
        }
        holder.layout.setOnClickListener( v -> {
						if (itemClickListener != null) {
								itemClickListener.gameItemClick( model );
						}
				} );
        holder.labelLayout.removeAllViews();
        if (model.getLabelArray() != null) {
            String[] labels = model.getLabelArray();
            if (labels != null) {
                for (int j = 0; j < labels.length; j++) {
                    String label = labels[j];
                    if (j < 3) {
                        if (!TextUtils.isEmpty( label ) || " ".equals( label )) {
                            TextView textView = new TextView( mContext );
                            textView.setGravity( Gravity.CENTER );
                            textView.setText( label );
                            textView.setTextSize( TypedValue.COMPLEX_UNIT_SP, 10 );
                            textView.setTextColor( mContext.getResources()
                                    .getColor( R.color.white ) );
                            setTextBackground( textView, j );
                            textView.setPadding(
                                    DisplayMetricsUtils.dipTopx( mContext, 5 ),
                                   1,
                                    DisplayMetricsUtils.dipTopx( mContext, 5 ),
                                    1);
                            holder.labelLayout.addView( textView );
                            holder.labelLayout.setGravity( Gravity.CENTER_VERTICAL );
                            LayoutParams params = (LayoutParams) textView
                                    .getLayoutParams();
                            params.setMargins( 0, 0,
                                    DisplayMetricsUtils.dipTopx( mContext, 3 ), 0 );
                            textView.setLayoutParams( params );
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }

    int[] drawables = {R.drawable.shape_lable_1, R.drawable.shape_lable_2,
            R.drawable.shape_lable_3};

    @SuppressWarnings("deprecation")
    private void setTextBackground(TextView textView, int index) {
        textView.setBackground( mContext.getResources().getDrawable(
                drawables[index] ) );
    }

    public void setOnButtonClick(GameButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    public void setOnItemClick(GameItemClickListener listener) {
        this.itemClickListener = listener;
    }

    class GameViewHolder {
        ImageView gameImg;
        TextView gameNameTv, gameTimesTv, timeTv, gameSizeTv;
        RelativeLayout layout;
        LinearLayout topLayout;
        LinearLayout labelLayout;

        public GameViewHolder(View view) {
            initView( view );
        }

        private void initView(View convertView) {
            gameSizeTv = convertView
                    .findViewById( R.id.id_game_list_item_gameSize );
            layout = convertView
                    .findViewById( R.id.id_game_list_item );
            gameImg = convertView
                    .findViewById( R.id.id_game_list_item_gameImg );
            gameNameTv = convertView
                    .findViewById( R.id.id_game_list_item_gameName );
            gameTimesTv = convertView
                    .findViewById( R.id.id_game_list_item_gameDownTimes );
            timeTv = convertView.findViewById( R.id.id_textView );
            topLayout = convertView
                    .findViewById( R.id.id_game_list_item_topLayout );
            labelLayout = convertView
                    .findViewById( R.id.id_game_list_item_label );
        }
    }
}
