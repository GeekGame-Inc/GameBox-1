package com.tenone.gamebox.mode.biz;

import android.content.Context;

import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.utils.cache.ACache;

/**
 * Created by Eddy on 2018/1/25.
 */

public class SaveCacheThread extends Thread {

    public static void startThread(Context context, String key, ResultItem data) {
        getInstance( context, key, data ).start();
    }

    public static SaveCacheThread getInstance(Context context, String key, ResultItem data) {
        return new SaveCacheThread( context, key, data );
    }


    private String key;
    private ResultItem data;
    private ACache cache;
    private Context context;

    public SaveCacheThread(Context context, String key, ResultItem data) {
        this.key = key;
        this.data = data;
        cache = ACache.get( context );
    }

    @Override
    public void run() {
        cache.put( key, data );
        super.run();
    }
}
