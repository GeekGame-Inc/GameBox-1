
package com.tenone.gamebox.mode.biz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.tenone.gamebox.mode.able.StrategyListAble;
import com.tenone.gamebox.mode.listener.GameTopListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.utils.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class StrategyListBiz implements StrategyListAble {

    @Override
    public void constructArray(Context context, List<ResultItem> resultItems,
                               GameTopListener listener) {
        if (!BeanUtils.isEmpty( resultItems )) {
            new MyThread( context, resultItems, listener ).start();
        }
    }

    private class MyThread extends Thread {
        Context context;
        List<ResultItem> resultItems;
        List<GameModel> items = new ArrayList<GameModel>();
        GameTopListener listener;

        public MyThread(Context context, List<ResultItem> resultItems,
                        GameTopListener listener) {
            this.context = context;
            this.resultItems = resultItems;
            this.listener = listener;
        }

        @Override
        public void run() {
            if (!BeanUtils.isEmpty( resultItems )) {
                for (ResultItem data : resultItems) {
                    GameModel model = new GameModel();
                    model.setGameTag( data.getString( "tag" ) );
                    model.setImgUrl( MyApplication.getHttpUrl().getBaseUrl()
                            + data.getString( "logo" ) );
                    model.setName( data.getString( "gamename" ) );
									  model.setOperate( data.getIntValue( "operate" ) );
                    model.setGameId( data.getIntValue( "id" ) );
                    model.setDis( data.getString( "discount" ) );
                    model.setSize( data.getString( "size" ) );
                    model.setUrl( data.getString( "android_url" ) );
                    model.setVersionsName( data.getString( "version" ) );
                    String down = data.getString( "download" );
                    int download = 0;
                    if (!TextUtils.isEmpty( down )) {
                        download = Integer.valueOf( down ).intValue();
                    }
                    if (download > 10000) {
                        download = download / 10000;
                        model.setTimes( download + "\u4e07+" );
                    } else {
                        model.setTimes( download + "" );
                    }
                    model.setContent( data.getString( "content" ) );
                    model.setPackgeName( data.getString( "android_pack" ) );
                    model.setStatus( GameStatus.UNLOAD );
                    String str = data.getString( "label" );
                    if (!TextUtils.isEmpty( str )) {
                        String[] lableArray = str.split( "," );
                        model.setLabelArray( lableArray );
                    }
                    items.add( model );
                }
                handler.post( () -> {
                    if (listener != null) {
                        listener.updateUI( items );
                    }
                } );
            }
            super.run();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage( msg );
        }
    };
}
