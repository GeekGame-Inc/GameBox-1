/**
 * Project Name:GameBox
 * File Name:TodayOpenFragmentAdapter.java
 * Package Name:com.tenone.gamebox.view.adapter
 * Date:2017-3-29����9:55:55
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.OpenServiceWarnListener;
import com.tenone.gamebox.mode.mode.OpenServerMode;
import com.tenone.gamebox.view.custom.ThreeStateButton;
import com.tenone.gamebox.view.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class DetailsOpenAdapter extends RecyclerView.Adapter<DetailsOpenAdapter.DetailsOpenHolder> {
    List<OpenServerMode> items = new ArrayList<OpenServerMode>();
    Context mContext;
    LayoutInflater mInflater;
    OpenServiceWarnListener listener;

    public DetailsOpenAdapter(List<OpenServerMode> list, Context cxt) {
        this.items = list;
        this.mContext = cxt;
        this.mInflater = LayoutInflater.from( mContext );
    }

    @Override
    public DetailsOpenHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DetailsOpenHolder holder = null;
        View convertView = mInflater.inflate( R.layout.item_details_open, parent,
                false );
        holder = new DetailsOpenHolder( convertView );
        return holder;
    }

    @Override
    public void onBindViewHolder(DetailsOpenHolder holder, int position) {
        final OpenServerMode mode = items.get( position );
        holder.stateButton.setOnClickListener( v -> {
            if (listener != null) {
                listener.onOpenServiceClick( mode );
            }
        } );
        int state = 0;
        state = mode.isNotification() ? 1 : 0;
        state = mode.isOpen() ? 2 : state;
        holder.stateButton.setState( state );
			holder.nameTv.setText( mode.getGameName() + " - " + mode.getServiceId()
					+ " \u670d" );
        String time = mode.getOpenTime();
        if (!TextUtils.isEmpty( time )) {
            time = TimeUtils.formatDateMin( Long.valueOf( time ) * 1000 );
        }
			holder.timeTv.setText( "\u5f00\u670d\u65f6\u95f4: " + time );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOpenServiceWarnListener(OpenServiceWarnListener l) {
        this.listener = l;
    }

    class DetailsOpenHolder extends RecyclerView.ViewHolder {
        TextView nameTv, timeTv;
        ThreeStateButton stateButton;

        public DetailsOpenHolder(View v) {
            super( v );
            initView( v );
        }

        private void initView(View v) {
            nameTv = (TextView) findViewById( v, R.id.id_item_detailsOpen_nameTv );
            timeTv = (TextView) findViewById( v, R.id.id_item_detailsOpen_timeTv );
            stateButton = (ThreeStateButton) findViewById( v,
                    R.id.id_item_detailsOpen_button );
        }

        private View findViewById(View v, int rId) {
            return v.findViewById( rId );
        }
    }
}
