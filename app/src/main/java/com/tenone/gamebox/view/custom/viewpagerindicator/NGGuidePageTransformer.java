package com.tenone.gamebox.view.custom.viewpagerindicator;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

public class NGGuidePageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_ALPHA = 0.0f;    //��С͸����

    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();    //�õ�view��

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left. ���������Ļ
            view.setAlpha(0);

        } else if (position <= 1) { // [-1,1]
            if (position < 0) {
                //��ʧ��ҳ��
                view.setTranslationX(-pageWidth * position);  //��ֹ��ʧҳ��Ļ���
            } else {
                //���ֵ�ҳ��
                view.setTranslationX(pageWidth);        //ֱ�����ó��ֵ�ҳ�浽��
                view.setTranslationX(-pageWidth * position);  //��ֹ����ҳ��Ļ���
            }
            // Fade the page relative to its size.
            float alphaFactor = Math.max(MIN_ALPHA, 1 - Math.abs(position));
            //͸���ȸı�Log
            view.setAlpha(alphaFactor);
        } else { // (1,+Infinity]
            // This page is way off-screen to the right.    �����ұ���Ļ
            view.setAlpha(0);
        }
    }

    int nowPostion = 0; //��ǰҳ��
    Context context;
    List<Fragment> fragments;

    public void setCurrentItem(Context context, int nowPostion, List<Fragment> fragments) {
        this.nowPostion = nowPostion;
        this.context = context;
        this.fragments = fragments;
    }

    public void setCurrentItem(int nowPostion) {
        this.nowPostion = nowPostion;
    }

}