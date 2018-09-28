package com.tenone.gamebox.mode.biz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.tenone.gamebox.mode.able.GameNewFragmentAble;
import com.tenone.gamebox.mode.listener.GameNewFragmentListener;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.MyApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameNewFragmentBiz implements GameNewFragmentAble {

    @SuppressLint({"SimpleDateFormat"})
    private String creatTime(String str) {
        Long milliseconds = Long.valueOf( str ) * 1000;
        Date date = new Date( milliseconds );
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd" );
        return formatter.format( date );
    }

    private List<GameModel> groupByTime(List<ResultItem> results) {
        List<GameModel> gameModels = new ArrayList<GameModel>();
        for (int j = 0; j < results.size(); j++) {
            ResultItem resultItem = results.get( j );
            gameModels.add( initGameModel( resultItem, 0 ) );
        }
        return gameModels;
    }

    private GameModel initGameModel(ResultItem data, int type) {
        GameModel model = new GameModel();
        model.setTime( creatTime( data.getString( "addtime" ) ) );
        model.setGameTag( data.getString( "tag" ) );
        model.setType( type );
        model.setImgUrl( MyApplication.getHttpUrl().getBaseUrl()
                + data.getString( "logo" ) );
        model.setName( data.getString( "gamename" ) );
        String id = data.getString( "id" );
        if (!TextUtils.isEmpty( id )) {
            model.setGameId( Integer.valueOf( id ).intValue() );
        }
			  model.setOperate( data.getIntValue( "operate" ) );
        model.setSize( data.getString( "size" ) );
        model.setUrl( data.getString( "android_url" ) );
        model.setVersionsName( data.getString( "version" ) );
        long download = 0;
        String downloads = data.getString( "download" );
        if (!TextUtils.isEmpty( downloads )) {
            download = Long.valueOf( downloads ).longValue();
        }
        if (download > 10000) {
            download = download / 10000;
            model.setTimes( download + "\u4e07+" );
        } else {
            model.setTimes( download + "" );
        }
        model.setContent( data.getString( "content" ) );
        String str = data.getString( "label" );
        if (!TextUtils.isEmpty( str )) {
            String[] lableArray = str.trim().split( "," );
            model.setLabelArray( lableArray );
        }
        model.setPackgeName( data.getString( "android_pack" ) );
        model.setStatus( GameStatus.UNLOAD );
        model.setDis( data.getString( "discount" ) );
        return model;
    }

    @Override
    public void constructArray(List<ResultItem> resultItem, Context cxt,
                               GameNewFragmentListener listener) {
        new MyThread( resultItem, cxt, listener ).start();
    }

    class MyThread extends Thread {
        List<ResultItem> resultItem;
        Context cxt;
        GameNewFragmentListener listener;

        public MyThread(List<ResultItem> resultItem, Context cxt,
                        GameNewFragmentListener listener) {
            this.cxt = cxt;
            this.listener = listener;
            this.resultItem = resultItem;
        }

        @Override
        public void run() {
            List<GameModel> items = new ArrayList<GameModel>();
            if (resultItem != null) {
                items.addAll( groupByTime( resultItem ) );
            }
            handler.post( () -> {
                if (listener != null) {
                    listener.updateUI( items );
                    items.clear();
                }
            } );
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
