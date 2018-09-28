package com.tenone.gamebox.view.custom.xbanner;

import android.content.Context;
import android.widget.Scroller;

/**
 * Created by jxnk25 on 2016/10/7.
 *
 * @link https://xiaohaibin.github.io/
 * @email锛� xhb_199409@163.com
 * @github: https://github.com/xiaohaibin
 * @description锛�
 */
public class XBannerScroller extends Scroller {

    private int mDuration = 800;

    public XBannerScroller(Context context, int duration) {
        super(context);
        mDuration = duration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}
