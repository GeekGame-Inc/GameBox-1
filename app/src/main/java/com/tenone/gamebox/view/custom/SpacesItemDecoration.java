package com.tenone.gamebox.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class SpacesItemDecoration extends DividerItemDecoration {

    private int space, drawColor;

    public SpacesItemDecoration(Context context, int orientation, int space, int drawColor) {
        super( context, orientation );
        this.space = space;
        this.drawColor = drawColor;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = 0;
        outRect.right = 0;
        outRect.bottom = space;
        if (parent.getChildAdapterPosition( view ) == 0) {
            outRect.top = space;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        c.drawColor( drawColor );
        super.onDraw( c, parent, state );
    }
}

