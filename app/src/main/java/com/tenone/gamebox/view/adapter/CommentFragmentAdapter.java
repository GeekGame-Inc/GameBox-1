/**
 * Project Name:GameBox
 * File Name:CommentFragmentAdapter.java
 * Package Name:com.tenone.gamebox.view.adapter
 * Date:2017-3-22����4:04:35
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OnDynamicCommentItemClickListener;
import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.mode.mode.DynamicCommentModel;
import com.tenone.gamebox.view.custom.CircleImageView;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class CommentFragmentAdapter extends BaseAdapter {
    private List<DynamicCommentModel> models = new ArrayList<DynamicCommentModel>();
    private Context mContext;
    private LayoutInflater mInflater;
    private OnDynamicCommentItemClickListener onDynamicCommentItemClickListener;

    public CommentFragmentAdapter(List<DynamicCommentModel> list, Context cxt) {
        this.models = list;
        this.mContext = cxt;
        this.mInflater = LayoutInflater.from( mContext );
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
        CommentHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate( R.layout.item_dynamic_comment, parent,
                    false );
            holder = new CommentHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (CommentHolder) convertView.getTag();
        }

        final DynamicCommentModel model = models.get( position );
        if (null != model) {
            DriverModel driverModel = model.getDriverModel();
            if (driverModel != null) {
                ImageLoadUtils.loadNormalImg( holder.headerView, mContext, driverModel.getHeader() );
                holder.vipIv.setVisibility( driverModel.isVip() ? View.VISIBLE : View.GONE );
                holder.nameTv.setText( driverModel.getNick() );
                String uid = SpUtil.getUserId();
                String id = driverModel.getDriverId();
                if (!TextUtils.isEmpty( uid ) && !"0".equals( uid ) && uid.equals( id )) {
                    holder.deleteTv.setVisibility( View.VISIBLE );
                    holder.deleteTv.setOnClickListener( view -> {
                        if (null != onDynamicCommentItemClickListener) {
                            onDynamicCommentItemClickListener.onDeleteClick( model );
                        }
                    } );
                    holder.commentIv.setVisibility( View.GONE );
                } else {
                    holder.deleteTv.setVisibility( View.GONE );
                    holder.commentIv.setVisibility( View.VISIBLE );
                }
            }
            if (!TextUtils.isEmpty( model.getToUserId() ) && !"0".equals( model.getToUserId() )) {
                holder.contentTv.setText(
                        getString( R.string.dynamic_comment, model.getToUserNick(), model.getCommentContent() ) );
            } else {
                holder.contentTv.setText( model.getCommentContent() );
            }
            holder.timeTv.setText( model.getCommentTime() );
            holder.topIv.setVisibility( model.getOrder() == 1 ? View.VISIBLE : View.GONE );
            int type = model.getLikeTy();
            Drawable drawable = mContext.getResources().getDrawable( R.drawable.selector_good );
            drawable.setBounds( 0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight() );
            holder.praiseIv.setCompoundDrawables( drawable, null, null, null );
            holder.praiseIv.setText( model.getCommentLikes() );
            holder.praiseIv.setSelected( 1 == type );
            holder.praiseIv.setOnClickListener( view -> {
                if (null != onDynamicCommentItemClickListener) {
                    onDynamicCommentItemClickListener.onPraiseClick( model );
                }
            } );

            holder.commentIv.setOnClickListener( view -> {
                if (null != onDynamicCommentItemClickListener) {
                    onDynamicCommentItemClickListener.onCommentClick( model );
                }
            } );
            holder.headerView.setOnClickListener( view -> {
                if (null != onDynamicCommentItemClickListener) {
                    onDynamicCommentItemClickListener.onHeaderClick( model );
                }
            } );

        }
        return convertView;
    }
	@SuppressWarnings( "deprecation" )
    private Spanned getString(int id, String str, String str2) {
        return Html.fromHtml( mContext.getResources().getString( id,
						str, str2 ) );
    }

    public void setOnDynamicCommentItemClickListener(OnDynamicCommentItemClickListener onDynamicCommentItemClickListener) {
        this.onDynamicCommentItemClickListener = onDynamicCommentItemClickListener;
    }

    class CommentHolder {
        private CircleImageView headerView;
        private ImageView vipIv, topIv;
        private TextView nameTv, commentIv, contentTv, timeTv, praiseIv, deleteTv;

        public CommentHolder(View v) {
            init( v );
        }

        private void init(View view) {
            this.headerView = view.findViewById( R.id.id_item_dynamic_comment_header );
            this.vipIv = view.findViewById( R.id.id_item_dynamic_comment_vipImg );
            this.nameTv = view.findViewById( R.id.id_item_dynamic_comment_name );
            this.contentTv = view.findViewById( R.id.id_item_dynamic_comment_content );
            this.timeTv = view.findViewById( R.id.id_item_dynamic_comment_time );
            this.commentIv = view.findViewById( R.id.id_item_dynamic_comment_comment );
            this.praiseIv = view.findViewById( R.id.id_item_dynamic_comment_praise );
            this.deleteTv = view.findViewById( R.id.id_item_dynamic_comment_delete );
            this.topIv = view.findViewById( R.id.id_item_dynamic_comment_top );
        }
    }

}
