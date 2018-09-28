package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.DynamicOperationListener;
import com.tenone.gamebox.mode.listener.OnDynamicDeleteClickListener;
import com.tenone.gamebox.mode.listener.OnDynamicsItemClickListener;
import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.mode.mode.DynamicCommentModel;
import com.tenone.gamebox.mode.mode.DynamicModel;
import com.tenone.gamebox.view.activity.BrowseImageActivity;
import com.tenone.gamebox.view.custom.AuditTextView;
import com.tenone.gamebox.view.custom.CircleImageView;
import com.tenone.gamebox.view.custom.MyGridView;
import com.tenone.gamebox.view.custom.MyListView;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.List;

public class MineDynamicsAdapter extends RecyclerView.Adapter<MineDynamicsAdapter.ViewHolder> {

    private Context context;
    private List<DynamicModel> models = new ArrayList<DynamicModel>();
    private int dp40, anInt, scrWidth;
    private LayoutInflater inflater;
    private DynamicOperationListener listener;
    private OnDynamicsItemClickListener onDynamicsItemClickListener;
    private OnDynamicDeleteClickListener onDynamicDeleteClickListener;

    public MineDynamicsAdapter(Context context, List<DynamicModel> models) {
        this.context = context;
        this.models = models;
        this.inflater = LayoutInflater.from( context );
        dp40 = DisplayMetricsUtils.dipTopx( context, 40 );
        scrWidth = DisplayMetricsUtils.getScreenWidth( context );
        anInt = scrWidth - dp40;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflater.inflate( R.layout.item_dynamic, parent, false );
        ViewHolder holder = new ViewHolder( convertView );
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DynamicModel model = models.get( position );
        List<String> items = new ArrayList<String>();
			items.add( model.getLikes() + "\u8d5e" );
			items.add( model.getDislikes() + "\u8e29" );
			items.add( model.getShares() + "\u5206\u4eab" );
			items.add( model.getComments() + "\u8bc4\u8bba" );
        TextViewGridAdapter adapter = new TextViewGridAdapter( context, items );
        holder.leavelIv.setVisibility( 1 == model.getLeavel() ? View.VISIBLE : View.GONE );
        if (TextUtils.isEmpty( model.getRemark() )) {
            holder.remakTv.setVisibility( View.GONE );
        } else {
            holder.remakTv.setVisibility( View.VISIBLE );
					holder.remakTv.setText( "\u5c0f\u7f16\u70b9\u8bc4:" + model.getRemark() );
        }
        adapter.setDisGood( model.isDisGood() );
        adapter.setGood( model.isGood() );
        holder.timeTv.setText( model.getTime() );
        holder.contentTv.setText( model.getContent() );
        holder.itemGrid.setAdapter( adapter );
        holder.itemGrid.setOnItemClickListener( (parent, v, p, id) -> {
            if (listener != null) {
                switch (p) {
                    case 0:
                        listener.onPraiseClick( model );
                        break;
                    case 1:
                        listener.onStepOnClick( model );
                        break;
                    case 2:
                        listener.onShareClick( model );
                        break;
                    case 3:
                        listener.onCommentClick( model );
                        break;
                }
            }
        } );
        DriverModel driverModel = model.getModel();
        if (null != driverModel) {
            ImageLoadUtils.loadNormalImg( context, driverModel.getHeader(), R.drawable.ic_logo_failure,
                    R.drawable.ic_loading_logo, holder.circleImageView );
            holder.nameTv.setText( driverModel.getNick() );
            holder.vipIv.setVisibility( driverModel.isVip() ? View.VISIBLE : View.GONE );
            if ("\u672a\u8bbe\u7f6e".equals( driverModel.getSex() )) {
                holder.sexIv.setVisibility( View.GONE );
            } else {
                holder.sexIv.setVisibility( View.VISIBLE );
                BitmapDrawable drawable = (BitmapDrawable) context.getResources().getDrawable(
                        context.getString( R.string.women )
														.equals( driverModel.getSex() ) ? R.drawable.icon_dynamic_girl : R.drawable.icon_dynamic_boy );
                holder.sexIv.setImageBitmap( drawable.getBitmap() );
            }
            holder.publishTimeTv.setText( model.getStatus() == 1 ? model.getPublishTime() + "\u53d1\u5e03" : "" );
            if (model.getStatus() > 0 &&
                    !BeanUtils.isEmpty( driverModel.getDriverId() ) &&
                    driverModel.getDriverId().equals( SpUtil.getUserId() )) {
                holder.auditTextView.setVisibility( View.VISIBLE );
                holder.auditTextView.setStatus( model.getStatus() );
                holder.delTv.setVisibility( View.VISIBLE );
                holder.publishTimeTv.setVisibility( View.VISIBLE );
            } else {
                holder.auditTextView.setVisibility( View.GONE );
                holder.delTv.setVisibility( View.GONE );
                holder.publishTimeTv.setVisibility( View.GONE );
            }
        }
        final ArrayList<String> imgs = model.getDynamicImg();
        if (BeanUtils.isEmpty( imgs )) {
            holder.dynamicGrid.setVisibility( View.GONE );
        } else {
            holder.dynamicGrid.setVisibility( View.VISIBLE );
            int imgHeight = 0;
            switch (imgs.size()) {
                case 1:
                    holder.dynamicGrid.setNumColumns( 1 );
                    imgHeight = anInt / 2;
                    break;
                case 2:
                    holder.dynamicGrid.setNumColumns( 2 );
                    imgHeight = anInt / 2;
                    break;
                default:
                    holder.dynamicGrid.setNumColumns( 3 );
                    imgHeight = anInt / 3;
                    break;
            }
            holder.dynamicGrid.setOnItemClickListener( (parent, view, p, id) -> {
                context.startActivity(
                        new Intent( context, BrowseImageActivity.class )
                                .putStringArrayListExtra( "urls", imgs ).putExtra(
                                "url", imgs.get( p ) ) );
            } );
            ImageGridAdapter imageGridAdapter = new ImageGridAdapter( context, imgs );
            imageGridAdapter.setImgHeight( imgHeight );
            holder.dynamicGrid.setAdapter( imageGridAdapter );
        }
        List<DynamicCommentModel> commentModels = model.getCommentModels();
        DynamicListCommentAdapter commentAdapter = new DynamicListCommentAdapter( commentModels, context );
        holder.listView.setAdapter( commentAdapter );
        holder.listView.setOnItemClickListener( (parent, view, position1, id) -> {
            if (listener != null) {
                listener.onCommentClick( model );
            }
        } );
    }

    @Override
    public int getItemCount() {
        return models.size();
    }


    public void setListener(DynamicOperationListener listener) {
        this.listener = listener;
    }

    public void setOnDynamicsItemClickListener(OnDynamicsItemClickListener onDynamicsItemClickListener) {
        this.onDynamicsItemClickListener = onDynamicsItemClickListener;
    }

    public void setOnDynamicDeleteClickListener(OnDynamicDeleteClickListener onDynamicDeleteClickListener) {
        this.onDynamicDeleteClickListener = onDynamicDeleteClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView nameTv, timeTv, contentTv, publishTimeTv, delTv, remakTv;
        ImageView sexIv, vipIv, leavelIv;
        MyGridView dynamicGrid, itemGrid;
        LinearLayout rootView;
        MyListView listView;
        AuditTextView auditTextView;

        public ViewHolder(View view) {
            super( view );
            remakTv = view.findViewById( R.id.id_item_dynamic_text );
            leavelIv = view.findViewById( R.id.id_item_dynamic_leavel );
            rootView = view.findViewById( R.id.id_item_dynamic_root );
            circleImageView = view.findViewById( R.id.id_item_dynamic_header );
            nameTv = view.findViewById( R.id.id_item_dynamic_name );
            timeTv = view.findViewById( R.id.id_item_dynamic_time );
            contentTv = view.findViewById( R.id.id_item_dynamic_content );
            sexIv = view.findViewById( R.id.id_item_dynamic_sexImg );
            vipIv = view.findViewById( R.id.id_item_dynamic_vipImg );
            listView = view.findViewById( R.id.id_item_dynamic_commments );
            dynamicGrid = view.findViewById( R.id.id_item_dynamic_grid );
            itemGrid = view.findViewById( R.id.id_item_dynamic_item );
            publishTimeTv = view.findViewById( R.id.id_item_dynamic_auditTimeTv );
            delTv = view.findViewById( R.id.id_item_dynamic_delTv );
            rootView.setOnClickListener( v -> {
                if (null != onDynamicsItemClickListener) {
                    onDynamicsItemClickListener.onDynamicsItemClick( models.get( getPosition() ) );
                }
            } );
            auditTextView = view.findViewById( R.id.id_item_dynamic_auditTv );
            delTv.setOnClickListener( v -> {
                if (null != onDynamicDeleteClickListener) {
                    onDynamicDeleteClickListener.onDynamicDeleteClick( models.get( getPosition() ) );
                }
            } );
            publishTimeTv.setVisibility( View.VISIBLE );
        }
    }
}
