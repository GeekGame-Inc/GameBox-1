package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.tenone.gamebox.R;

public class MyRefreshListView extends ListView {

    public MyRefreshListView(Context context) {
        this( context, null );
    }

    public MyRefreshListView(Context context, AttributeSet attrs) {
        super( context, attrs );
    }

    @SuppressLint("InflateParams")
    @Override
    public void setAdapter(ListAdapter adapter) {
        View mListViewFooter = LayoutInflater.from( getContext() ).inflate(
                R.layout.layout_footer, null, false );
        mListViewFooter.setVisibility( View.GONE );
        addFooterView( mListViewFooter );
        super.setAdapter( adapter );
        removeFooterView( mListViewFooter );
    }
}
