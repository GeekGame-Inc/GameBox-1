/*
 *
 */

package com.tenone.gamebox.share;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;

public class ShareAdapter extends BaseAdapter {

    private static String[] shareNames;
    private int[] shareIcons = new int[]{R.mipmap.icon_wechat,
            R.mipmap.icon_moments, R.mipmap.icon_qq, R.mipmap.icon_qzone};
    private LayoutInflater inflater;
    private Context mContext;

    public ShareAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from( context );
        shareNames = new String[]{context.getResources().getString( R.string.wechat ), context.getResources().getString( R.string.frind ), context.getResources().getString( R.string.QQ ), context.getResources().getString( R.string.QZon )};
    }

    @Override
    public int getCount() {
        return shareNames.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate( R.layout.item_share, null );
        }
       ImageView shareIcon = convertView
                .findViewById(R.id.share_icon);
        TextView shareTitle = convertView
                .findViewById( R.id.share_title );
        shareIcon.setBackground(mContext.getResources().getDrawable( shareIcons[position] ));
        shareTitle.setText( shareNames[position] );
      /*  Drawable drawable = mContext.getResources().getDrawable( shareIcons[position] );
        drawable.setBounds( 0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight() );
        shareTitle.setCompoundDrawables( null, drawable, null, null );*/
        return convertView;
    }
}
