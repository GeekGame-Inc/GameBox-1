/**
 * Project Name:GameBox
 * File Name:PrivilegeAdapter.java
 * Package Name:com.tenone.gamebox.view.adapter
 * Date:2017-4-13����3:42:59
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.PrivilegeMode;
import com.tenone.gamebox.view.utils.image.ImageLoadUtils;

import java.util.List;

/**
 * ��б� ClassName:PrivilegeAdapter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-13 ����3:42:59 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */
public class PrivilegeAdapter extends BaseAdapter {

    private List<PrivilegeMode> items;
    private Context mContext;
    private LayoutInflater mInflater;

    public PrivilegeAdapter(List<PrivilegeMode> list, Context cxt) {
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
        PrivilegeHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate( R.layout.item_privilege_list,
                    parent, false );
            holder = new PrivilegeHolder( convertView );
            convertView.setTag( holder );
        } else {
            holder = (PrivilegeHolder) convertView.getTag();
        }
        PrivilegeMode mode = items.get( position );
        holder.nameTv.setText( mode.getPrivilegeName().trim() );
        ImageLoadUtils.loadNormalImg( holder.image, mContext, mode.getPrivilegeImgUrl() );
        return convertView;
    }

    class PrivilegeHolder {
        TextView nameTv;
        ImageView image;
        TextView introTv;

        public PrivilegeHolder(View convertView) {
            initView( convertView );
        }

        private void initView(View convertView) {
            nameTv = convertView
                    .findViewById( R.id.id_privilege_Name );
            image = convertView
                    .findViewById( R.id.id_privilege_img );
        }
    }
}
