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

public class DynamicDetailsCommentAdapter extends BaseAdapter {
    private Context context;
    private List<DynamicCommentModel> models = new ArrayList<DynamicCommentModel>();
    private LayoutInflater inflater;
    private OnDynamicCommentItemClickListener onDynamicCommentItemClickListener;

    public DynamicDetailsCommentAdapter(Context context, List<DynamicCommentModel> models) {
        this.context = context;
        this.models = models;
        this.inflater = LayoutInflater.from( context );
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
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = inflater.inflate( R.layout.item_dynamic_comment, parent, false );
            holder = new ViewHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final DynamicCommentModel model = models.get( position );
        if (null != model) {
            DriverModel driverModel = model.getDriverModel();
            if (driverModel != null) {
                ImageLoadUtils.loadNormalImg( holder.headerView, context, driverModel.getHeader() );
                holder.vipIv.setVisibility( driverModel.isVip() ? View.VISIBLE : View.GONE );
                holder.nameTv.setText( driverModel.getNick() );
                String uid = SpUtil.getUserId();
                String id = driverModel.getDriverId();
                if (!TextUtils.isEmpty( uid ) && !"0".equals( uid ) && uid.equals( id )) {
                    holder.deleteTv.setVisibility( View.VISIBLE );
                    holder.deleteTv.setOnClickListener( v -> {
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
            Drawable drawable = context.getResources().getDrawable( R.drawable.selector_good );
            drawable.setBounds( 0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight() );
            holder.praiseIv.setCompoundDrawables( drawable, null, null, null );
            holder.praiseIv.setText( model.getCommentLikes() );
            holder.praiseIv.setSelected( 1 == type );
            holder.praiseIv.setOnClickListener( v -> {
								if (null != onDynamicCommentItemClickListener) {
										onDynamicCommentItemClickListener.onPraiseClick( model );
								}
						} );

            holder.commentIv.setOnClickListener( v -> {
								if (null != onDynamicCommentItemClickListener) {
										onDynamicCommentItemClickListener.onCommentClick( model );
								}
						} );
            holder.headerView.setOnClickListener( v -> {
								if (null != onDynamicCommentItemClickListener) {
										onDynamicCommentItemClickListener.onHeaderClick( model );
								}
						} );


        }
        return convertView;
    }

    public void setOnDynamicCommentItemClickListener(OnDynamicCommentItemClickListener onDynamicCommentItemClickListener) {
        this.onDynamicCommentItemClickListener = onDynamicCommentItemClickListener;
    }

    private Spanned getString(int id, String str, String str2) {
        return Html.fromHtml( context.getResources().getString( id,
						str, str2 ) );
    }

    private class ViewHolder {
        private CircleImageView headerView;
        private ImageView vipIv,topIv;
        private TextView nameTv, commentIv, contentTv, timeTv, praiseIv, deleteTv;

        public ViewHolder(View view) {
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
