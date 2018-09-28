package com.tenone.gamebox.view.custom.xbanner.transformers;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;


public class StackPageTransformer extends BasePageTransformer {

    @Override
    public void handleInvisiblePage(View view, float position) {
    }

    @Override
    public void handleLeftPage(View view, float position) {
    }

    @Override
    public void handleRightPage(View view, float position) {
        ViewHelper.setTranslationX(view, -view.getWidth() * position);
    }

}