package com.tenone.gamebox.mode.listener;

import android.content.Context;

import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.utils.cache.ACache;

public class CacheDataThread extends Thread {
    private ResultItem resultItem;
    private ACache cache;
    private String tag;

    public CacheDataThread(Context context, ResultItem resultItem, String tag) {
        this.resultItem = resultItem;
        this.cache = ACache.get( context );
        this.tag = tag;
    }

    @Override
    public void run() {
        if (resultItem != null) {
            cache.put( tag, resultItem );
        }
        super.run();
    }
}
