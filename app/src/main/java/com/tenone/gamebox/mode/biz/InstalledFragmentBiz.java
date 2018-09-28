
package com.tenone.gamebox.mode.biz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.tenone.gamebox.mode.able.InstalledFragmentAble;
import com.tenone.gamebox.mode.listener.GameHotFragmentListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GamePackMode;
import com.tenone.gamebox.view.utils.ApkUtils;
import com.tenone.gamebox.view.utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

public class InstalledFragmentBiz implements InstalledFragmentAble {

    @Override
    public void getGameModels(Context cxt, GameHotFragmentListener listener) {
        new MyThread( cxt, listener ).start();
    }

    private class MyThread extends Thread {
        Context cxt;
        GameHotFragmentListener listener;
        List<GameModel> gameModels = new ArrayList<GameModel>();

        public MyThread(Context c, GameHotFragmentListener l) {
            this.cxt = c;
            this.listener = l;
        }

        @Override
        public void run() {
            List<GamePackMode> gamePackModes = DatabaseUtils.getInstanse( cxt )
                    .gamePackModes();
            ApkUtils.inspectApk( cxt, gameModels, gamePackModes );
            handler.post( () -> {
                if (listener != null) {
                    listener.uadapteUI( gameModels );
                }
            } );
            super.run();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage( msg );
        }
    };
}
