package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;

public class ProductImagesAdapter extends BaseAdapter {
    private Context context;
    private List<String> array;
    private LayoutInflater inflater;

    public ProductImagesAdapter(Context context, List<String> array) {
        this.context = context;
        this.array = array;
        this.inflater = LayoutInflater.from( context );
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PIAViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate( R.layout.item_product_img, parent, false );
            holder = new PIAViewHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (PIAViewHolder) convertView.getTag();
        }
     /*   ImageLoadUtils.loadIntoUseFitWidth( context, array.get( position ), R.drawable.ic_banner_failure,
                R.drawable.icon_banner_loading, holder.imageView );*/
     ImageLoadUtils.loadNormalImg( holder.imageView,context, array.get( position ));
        return convertView;
    }

    private class PIAViewHolder {
        ImageView imageView;

        public PIAViewHolder(View view) {
            imageView = view.findViewById( R.id.id_item_product_img );
        }
    }
}
