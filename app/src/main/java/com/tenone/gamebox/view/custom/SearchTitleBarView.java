/**
 * Project Name:GameBox
 * File Name:SearchTitleBarView.java
 * Package Name:com.tenone.gamebox.view.custom
 * Date:2017-3-27����6:23:40
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tenone.gamebox.R;

/**
 * ClassName:SearchTitleBarView <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-27 ����6:23:40 <br/>
 *
 * @author John Lie
 * @see
 * @since JDK 1.6
 */
@SuppressLint({"NewApi", "InflateParams"})
@SuppressWarnings("deprecation")
public class SearchTitleBarView extends RelativeLayout {

    private OnSearchButtonClickListener listener;

    public SearchTitleBarView(Context context, AttributeSet attrs,
                              int defStyleAttr, int defStyleRes) {
        super( context, attrs, defStyleAttr, defStyleRes );
        this.mContext = context;
        initView( context );
    }

    public SearchTitleBarView(Context context, AttributeSet attrs,
                              int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        this.mContext = context;
        initView( context );
    }

    public SearchTitleBarView(Context context, AttributeSet attrs) {
        super( context, attrs );
        this.mContext = context;
        initView( context );
    }

    public SearchTitleBarView(Context context) {
        super( context );
        this.mContext = context;
        initView( context );
    }

    private Context mContext;
    private View view;
    private ImageView leftImg, rightImg;
    private CustomizeEditText customizeEditText;

    private void initView(Context context) {
        try {
            view = LayoutInflater.from( context )
                    .inflate( R.layout.layout_search_title_bar, null )
                    .findViewById( R.id.id_searchTitle_layout );
            leftImg = view
                    .findViewById( R.id.id_searchTitle_leftImg );
            rightImg = view
                    .findViewById( R.id.id_searchTitle_rightImg );
            customizeEditText = view
                    .findViewById( R.id.id_searchTitle_editText );
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
            addView( view );
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        customizeEditText.setOnEditorActionListener( (v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                if (listener != null) {
                    listener.onSearchButtonClick();
                }
                return true;
            }
            return false;
        } );
    }

    public ImageView getLeftImg() {
        return leftImg;
    }

    public void setLeftImg(int rId) {
        if (leftImg != null) {
            BitmapDrawable drawable = (BitmapDrawable) mContext.getResources()
                    .getDrawable( rId );
            leftImg.setImageBitmap( drawable.getBitmap() );
        }
    }

    public ImageView getRightImg() {
        return rightImg;
    }

    public void setRightImg(int rId) {
        if (rightImg != null) {
            BitmapDrawable drawable = (BitmapDrawable) mContext.getResources()
                    .getDrawable( rId );
            rightImg.setImageBitmap( drawable.getBitmap() );
        }
    }

    public CustomizeEditText getCustomizeEditText() {
        return customizeEditText;
    }

    public void setCustomizeEditTextBackground(int rId) {
        if (customizeEditText != null) {
            Drawable drawable = mContext.getResources()
                    .getDrawable( rId );
            customizeEditText.setBackgroundDrawable( drawable );
            drawable = null;
        }
    }

    public void setEditTextHint(String hint) {
        customizeEditText.setHint( hint );
    }

    public void setEditTextHint(int rId) {
        customizeEditText.setHint( rId );
    }

    public void setOnSearchButtonClickListener(OnSearchButtonClickListener listener) {
        this.listener = listener;
    }

    public interface OnSearchButtonClickListener {
        void onSearchButtonClick();
    }
}
