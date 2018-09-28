package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.DynamicOperationListener;
import com.tenone.gamebox.mode.listener.OnHeaderClickListener;
import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.mode.mode.DynamicCommentModel;
import com.tenone.gamebox.mode.mode.DynamicModel;
import com.tenone.gamebox.view.activity.BrowseImageActivity;
import com.tenone.gamebox.view.custom.AttentionTextView;
import com.tenone.gamebox.view.custom.CircleImageView;
import com.tenone.gamebox.view.custom.MyGridView;
import com.tenone.gamebox.view.custom.MyListView;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class DynamicAdapter extends BaseAdapter {
    private static final int MIN_CLICK_DELAY_TIME = 200;
    private List<DynamicModel> models = new ArrayList<DynamicModel>();
    private Context context;
    private LayoutInflater inflater;
    private int dp40, anInt, scrWidth;
    private DynamicOperationListener listener;

    private OnHeaderClickListener onHeaderClickListener;
    private boolean isShowAttention = false, isShowTop = false;
    private long lastClickTime = 0;


    public DynamicAdapter(List<DynamicModel> models, Context context) {
        this.models = models;
        this.context = context;
        this.inflater = LayoutInflater.from( context );
        dp40 = DisplayMetricsUtils.dipTopx( context, 40 );
        scrWidth = DisplayMetricsUtils.getScreenWidth( context );
        anInt = scrWidth - dp40;
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
        if (convertView == null) {
            convertView = inflater.inflate( R.layout.item_dynamic, parent, false );
            holder = new ViewHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final DynamicModel model = models.get( position );
        holder.leavelIv.setVisibility( 1 == model.getLeavel() ? View.VISIBLE : View.GONE );

        if (TextUtils.isEmpty( model.getRemark() )) {
            holder.remakTv.setVisibility( View.GONE );
        } else {
            holder.remakTv.setVisibility( View.VISIBLE );
            holder.remakTv.setText( "\u5c0f\u7f16\u70b9\u8bc4:" +model.getRemark() );
        }
        List<String> items = new ArrayList<String>();
        items.add( model.getLikes() + "\u8d5e" );
        items.add( model.getDislikes() + "\u8e29" );
        items.add( model.getShares() + "\u5206\u4eab" );
        items.add( model.getComments() + "\u8bc4\u8bba" );
        TextViewGridAdapter adapter = new TextViewGridAdapter( context, items );
        adapter.setDisGood( model.isDisGood() );
        adapter.setGood( model.isGood() );
			  holder.itemGrid.setAdapter( adapter );
        holder.timeTv.setText( model.getTime() );
        holder.contentTv.setText( model.getContent() );
        holder.topIv.setVisibility( isShowTop && model.isTop() ? View.VISIBLE : View.GONE );
        holder.itemGrid.setOnItemClickListener( (pv, view, p, id) -> {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
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
            }
        } );
        final DriverModel driverModel = model.getModel();
        if (null != driverModel) {
            ImageLoadUtils.loadNormalImg( context, driverModel.getHeader(), R.drawable.ic_logo_failure,
                    R.drawable.ic_loading_logo, holder.circleImageView );
            holder.circleImageView.setOnClickListener( v -> {
                if (null != onHeaderClickListener) {
                    onHeaderClickListener.onHeaderClick( driverModel );
                }
            } );
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
            holder.attentionIv.setVisibility( isShowAttention ?
                    (driverModel.getDriverId().equals( SpUtil.getUserId() ) ? View.GONE : View.VISIBLE) : View.GONE );
            holder.attentionIv.setSelected( driverModel.isAttention() == 1 );
            holder.attentionIv.setOnClickListener( v -> {
                if (listener != null) {
                    listener.onAttentionClick( driverModel );
                }
            } );
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
            holder.dynamicGrid.setOnItemClickListener( (pv, view, p, id) -> {
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
        if (BeanUtils.isEmpty( commentModels )) {
            holder.listView.setVisibility( View.GONE );
        } else {
            holder.listView.setVisibility( View.VISIBLE );
            DynamicListCommentAdapter commentAdapter = new DynamicListCommentAdapter( commentModels, context );
            holder.listView.setAdapter( commentAdapter );
            holder.listView.setOnItemClickListener( (parent1, view, position1, id) -> {
								if (listener != null) {
										listener.onCommentClick( model );
								}
						} );
        }
        return convertView;
    }


    public void setListener(DynamicOperationListener listener) {
        this.listener = listener;
    }

    public void setOnHeaderClickListener(OnHeaderClickListener onHeaderClickListener) {
        this.onHeaderClickListener = onHeaderClickListener;
    }

    public void setShowAttention(boolean showAttention) {
        this.isShowAttention = showAttention;
    }

    private class ViewHolder {
        CircleImageView circleImageView;
        TextView nameTv, timeTv, contentTv, remakTv;
        ImageView sexIv, vipIv, leavelIv, topIv;
        MyGridView dynamicGrid, itemGrid;
        AttentionTextView attentionIv;
        MyListView listView;


        public ViewHolder(View view) {
            leavelIv = view.findViewById( R.id.id_item_dynamic_leavel );
            circleImageView = view.findViewById( R.id.id_item_dynamic_header );
            nameTv = view.findViewById( R.id.id_item_dynamic_name );
            timeTv = view.findViewById( R.id.id_item_dynamic_time );
            contentTv = view.findViewById( R.id.id_item_dynamic_content );
            sexIv = view.findViewById( R.id.id_item_dynamic_sexImg );
            vipIv = view.findViewById( R.id.id_item_dynamic_vipImg );
            attentionIv = view.findViewById( R.id.id_item_dynamic_attentionImg );
            dynamicGrid = view.findViewById( R.id.id_item_dynamic_grid );
            itemGrid = view.findViewById( R.id.id_item_dynamic_item );
            listView = view.findViewById( R.id.id_item_dynamic_commments );
            topIv = view.findViewById( R.id.id_item_dynamic_top );
            remakTv = view.findViewById( R.id.id_item_dynamic_text );
        }
    }
}
