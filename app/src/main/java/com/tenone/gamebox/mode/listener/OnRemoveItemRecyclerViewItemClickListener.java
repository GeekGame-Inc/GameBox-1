package com.tenone.gamebox.mode.listener;

import android.view.View;



public interface OnRemoveItemRecyclerViewItemClickListener {

    
    void onRemoveItemRecyclerViewItemClick(View view, int position);

    
    void onRemoveItemRecyclerViewDeleteClick(int position);
}
