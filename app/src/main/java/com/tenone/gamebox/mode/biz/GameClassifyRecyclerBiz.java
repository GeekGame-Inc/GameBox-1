/**
 * Project Name:GameBox
 * File Name:GameClassifyRecyclerBiz.java
 * Package Name:com.tenone.gamebox.mode.biz
 * Date:2017-5-11����11:10:57
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.mode.biz;

import android.text.TextUtils;

import com.tenone.gamebox.mode.able.GameClassifyRecyclerAble;
import com.tenone.gamebox.mode.mode.GameClassify;
import com.tenone.gamebox.mode.mode.GameClassifyModel;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.utils.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class GameClassifyRecyclerBiz implements GameClassifyRecyclerAble {

    @Override
    public List<GameClassify> constructArray(List<ResultItem> resultItem) {
        List<GameClassify> items = new ArrayList<GameClassify>();
        for (ResultItem result : resultItem) {
            GameClassify classify = new GameClassify();
            String id = result.getString( "id" );
            if (!TextUtils.isEmpty( id )) {
                classify.setClassifyId( Integer.valueOf( id ) );
            }
            classify.setClassifyImgUrl( MyApplication.getHttpUrl().getBaseUrl()
                    + result.getString( "logo" ) );
            classify.setClassifyName( result.getString( "name" ) );
            items.add( classify );
        }
        return items;
    }

    @Override
    public List<GameClassifyModel> cinstructRecommendArray(List<ResultItem> resultItem) {
        List<GameClassifyModel> classifyModels = new ArrayList<GameClassifyModel>();
        for (ResultItem results : resultItem) {
            GameClassifyModel classifyModel = new GameClassifyModel();
            classifyModel.setClassName( results.getString( "className" ) );
            List<GameModel> gameModels = new ArrayList<GameModel>();
            List<ResultItem> items = results.getItems( "list" );
            if (!BeanUtils.isEmpty( items )) {
                for (ResultItem resultItem2 : items) {
                    GameModel gameModel = new GameModel();
                    String id = resultItem2.getString( "id" );
                    if (!TextUtils.isEmpty( id )) {
                        gameModel.setGameId( Integer.valueOf( id ) );
                    }
                    gameModel.setName( resultItem2.getString( "gamename" ) );
                    gameModel.setImgUrl( MyApplication.getHttpUrl().getBaseUrl()
                            + resultItem2.getString( "logo" ) );
                    gameModels.add( gameModel );
                    classifyModel.setClassId( resultItem2.getString( "tid" ) );
                }
                classifyModel.setGameModels( gameModels );
                classifyModels.add( classifyModel );
            }
        }
        return classifyModels;
    }
}
