/**
 * Project Name:GameBox
 * File Name:GameRecommendFragmentBiz.java
 * Package Name:com.tenone.gamebox.mode.biz
 * Date:2017-3-8����5:30:33
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.mode.biz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.tenone.gamebox.mode.able.GameRecommendFragmentAble;
import com.tenone.gamebox.mode.listener.GameRecommendFragmentListener;
import com.tenone.gamebox.mode.mode.BannerModel;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class GameRecommendFragmentBiz implements GameRecommendFragmentAble {

    @Override
    public void constructArray(Context cxt, ResultItem resultItem,
                               GameRecommendFragmentListener listener) {
        new MyThread( cxt, resultItem, listener ).start();
    }

    @Override
    public void constructBannerArray(Context cxt, ResultItem resultItem,
                                     GameRecommendFragmentListener listener) {
        new BannerThread( cxt, resultItem, listener ).start();
    }

    class MyThread extends Thread {

        Context cxt;
        ResultItem resultItem;
        GameRecommendFragmentListener listener;

        public MyThread(Context cxt, ResultItem resultItem,
                        GameRecommendFragmentListener listener) {
            this.cxt = cxt;
            this.resultItem = resultItem;
            this.listener = listener;
        }

        @Override
        public void run() {
            if (resultItem != null) {
                List<ResultItem> resultItems = resultItem.getItems( "gamelist" );
                final List<GameModel> items = new ArrayList<GameModel>();
                if (resultItems != null) {
                    for (ResultItem resultItem2 : resultItems) {
                        GameModel model = new GameModel();
                        model.setGameTag( resultItem2.getString( "tag" ) );
                        model.setType( 1 );
                        String grade = resultItem2.getString( "score" );
                        float b = 0;
                        if (!TextUtils.isEmpty( grade )) {
                            b = Float.valueOf( grade ).floatValue();
                        }
                        model.setGrade( b );
                        model.setImgUrl( MyApplication.getHttpUrl().getBaseUrl()
                                + resultItem2.getString( "logo" ) );
                        model.setName( resultItem2.getString( "gamename" ) );
                        String id = resultItem2.getString( "id" );
                        if (!TextUtils.isEmpty( id )) {
                            model.setGameId( Integer.valueOf( id ).intValue() );
                        }
                        model.setSize( resultItem2.getString( "size" ) );
                        model.setUrl( resultItem2.getString( "android_url" ) );
                        model.setVersionsName( resultItem2.getString( "version" ) );
                        String downStr = resultItem2.getString( "download" );
                        long download = 0;
                        if (!TextUtils.isEmpty( downStr )) {
                            download = Long.valueOf(
                                    resultItem2.getString( "download" ) )
                                    .longValue();
                        }
                        if (download > 10000) {
                            download = download / 10000;
                            model.setTimes( download + "\u4e07+" );
                        } else {
                            model.setTimes( download + "" );
                        }
                        model.setContent( resultItem2.getString( "content" ) );
                        model.setPackgeName( resultItem2
                                .getString( "android_pack" ) );
                        model.setStatus( GameStatus.UNLOAD );
                        String str = resultItem2.getString( "label" );
                        if (!TextUtils.isEmpty( str )) {
                            String[] lableArray = str.trim().split( "," );
                            model.setLabelArray( lableArray );
                        }
                        items.add( model );
                    }
                }
                handler.post( () -> {
                    if (listener != null) {
                        listener.updateGameListUi( items );
                        items.clear();
                    }
                } );
            }
            super.run();
        }
    }

    class BannerThread extends Thread {
        Context cxt;
        ResultItem resultItem;
        GameRecommendFragmentListener listener;

        public BannerThread(Context cxt, ResultItem resultItem,
                            GameRecommendFragmentListener listener) {
            this.cxt = cxt;
            this.resultItem = resultItem;
            this.listener = listener;
        }

        @Override
        public void run() {
            final List<BannerModel> imgArray = new ArrayList<BannerModel>();
            if (resultItem != null) {
                List<ResultItem> items = resultItem.getItems( "banner" );
                if (items != null) {
                    for (ResultItem resultItem2 : items) {
                        BannerModel bannerModel = new BannerModel();
                        bannerModel.setGameId( resultItem2.getIntValue( "gid" ) );
                        bannerModel.setImageUrl( MyApplication.getHttpUrl()
                                .getBaseUrl()
                                + resultItem2.getString( "slide_pic" ) );
                        bannerModel.setType( resultItem2.getIntValue( "type" ) );
                        bannerModel.setUrl( resultItem2.getString( "url" ) );
                        imgArray.add( bannerModel );
                    }
                }

                handler.post( new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.uddateBannerUi( imgArray );
                            imgArray.clear();
                        }
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
