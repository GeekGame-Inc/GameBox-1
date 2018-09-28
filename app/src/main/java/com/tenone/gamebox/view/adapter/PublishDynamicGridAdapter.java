package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.ArrayList;

public class PublishDynamicGridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> paths = new ArrayList<String>();
    private LayoutInflater inflater;
    private int dp50;
    private int scWidth;
    private BitmapDrawable drawable;
    private OnDeleteClickListener onDeleteClickListener;
    private int longClickPosition = -1;

    private int maxSize = 4;

    public PublishDynamicGridAdapter(Context context, ArrayList<String> paths) {
        this.context = context;
        this.paths = paths;
        this.inflater = LayoutInflater.from( context );
        dp50 = DisplayMetricsUtils.dipTopx( context, 50 );
        scWidth = DisplayMetricsUtils.getScreenWidth( context );
        drawable = (BitmapDrawable) context.getResources().getDrawable( R.drawable.icon_dynamic_add );
    }

    @Override
    public int getCount() {
        int size = paths.size();
        if (size == 0) {
            return 1;
        }
        if (size < maxSize) {
            return size + 1;
        }
        return size;
    }

    @Override
    public Object getItem(int position) {
        return paths.get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (null == convertView) {
            convertView = inflater.inflate( R.layout.item_image_grid, parent, false );
            holder = new MyViewHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();
        params.width = (scWidth - dp50) / 4;
        params.height = params.width;
        holder.imageView.setLayoutParams( params );
        int size = paths.size();
        if (size == 0) {
            ImageLoadUtils.loadDraImg( context, holder.imageView, R.drawable.icon_dynamic_add );
            holder.deleteView.setVisibility( View.GONE );
        } else if (size == maxSize) {
            ImageLoadUtils.loadLocImg( context, paths.get( position ), holder.imageView );
            holder.deleteView.setVisibility( (position == longClickPosition) ? View.VISIBLE : View.GONE );
            holder.deleteView.setOnClickListener( v -> {
                if (null != onDeleteClickListener) {
                    onDeleteClickListener.onDeleteClick( position );
                    longClickPosition = -1;
                }
            } );
        } else {
            if (position == size) {
                holder.deleteView.setVisibility( View.GONE );
                ImageLoadUtils.loadDraImg( context, holder.imageView, R.drawable.icon_dynamic_add );
            } else {
                ImageLoadUtils.loadLocImg( context, paths.get( position ), holder.imageView );
                holder.deleteView.setVisibility( (position == longClickPosition) ? View.VISIBLE : View.GONE );
                holder.deleteView.setOnClickListener( v -> {
                    if (null != onDeleteClickListener) {
                        onDeleteClickListener.onDeleteClick( position );
                        longClickPosition = -1;
                    }
                } );
            }
        }
        return convertView;
    }

    public void setLongClickPosition(int longClickPosition) {
        this.longClickPosition = longClickPosition;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    private class MyViewHolder {
        ImageView imageView;
        ImageView deleteView;

        public MyViewHolder(View view) {
            this.imageView = view.findViewById( R.id.id_item_img_grid_img );
            this.deleteView = view.findViewById( R.id.id_item_img_grid_delete );
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }
}
