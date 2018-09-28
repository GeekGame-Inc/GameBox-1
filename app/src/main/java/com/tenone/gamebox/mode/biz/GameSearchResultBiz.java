
package com.tenone.gamebox.mode.biz;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.tenone.gamebox.mode.able.GameSearchResultAble;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.utils.SpUtil;

import java.util.ArrayList;
import java.util.List;

public class GameSearchResultBiz implements GameSearchResultAble {

    @Override
    public String getCondition(Intent intent) {
        return intent.getExtras().getString( "condition" );
    }

    @Override
    public List<GameModel> getGameModels(Context cxt,
                                         List<ResultItem> resultItems) {
        List<GameModel> items = new ArrayList<GameModel>();
        for (ResultItem data : resultItems) {
            GameModel model = new GameModel();
            model.setName( data.getString( "gamename" ) );
            String id = data.getString( "id" );
            if (!TextUtils.isEmpty( id )) {
                model.setGameId( Integer.valueOf( id ).intValue() );
            }
            model.setPlatform( data.getIntValue( "platform" ) );
            model.setDis( data.getString( "discount" ) );
            items.add( model );
        }
        return items;
    }

    @Override
    public List<String> getAllGameName() {
        return Configuration.getAllGameNames();
    }

    @Override
    public void saveRecord(String record) {
        List<String> list = SpUtil.getGameSearchRecord();
			list.remove( record );
        list.add( 0, record );
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i < list.size() - 1) {
                builder.append( list.get( i ) + "," );
            } else {
                builder.append( list.get( i ) );
            }
        }
        SpUtil.setGameSearchRecord( builder );
    }
}
