package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;


public class ImageGridAdapter extends BaseAdapter {
    private Context context;
    private List<String> imgs;
    private LayoutInflater inflater;

    private int imgHeight = 0;

    public ImageGridAdapter(Context context, List<String> imgs) {
        this.context = context;
        this.imgs = imgs;
        this.inflater = LayoutInflater.from( context );
    }

    @Override
    public int getCount() {
        return imgs.size();
    }

    @Override
    public Object getItem(int position) {
        return imgs.get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        String url = imgs.get( position );
        if (convertView == null) {
            convertView = inflater.inflate( R.layout.item_image_grid, parent, false );
            holder = new MyViewHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty( url )) {
            if (imgHeight > 0) {
                ViewGroup.LayoutParams params = holder.imageView.getLayoutParams();
                params.height = imgHeight;
                params.width = imgHeight;
                holder.imageView.setLayoutParams( params );
            }
            if (url.endsWith( ".gif" ) || url.equals( ".GIF" )) {
                ImageLoadUtils.loadGifImg( holder.imageView, context, url );
            } else {
                ImageLoadUtils.loadNormalImg( holder.imageView, context, url );
            }
        }
        return convertView;
    }

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }

    private class MyViewHolder {
        ImageView imageView;

        public MyViewHolder(View view) {
            this.imageView = view.findViewById( R.id.id_item_img_grid_img );
        }
    }
}
