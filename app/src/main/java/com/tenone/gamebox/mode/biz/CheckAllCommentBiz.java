
package com.tenone.gamebox.mode.biz;

import android.content.Intent;

import com.tenone.gamebox.mode.able.CheckAllCommentAble;
import com.tenone.gamebox.mode.mode.DriverModel;
import com.tenone.gamebox.mode.mode.DynamicCommentModel;
import com.tenone.gamebox.mode.mode.ResultItem;

import java.util.ArrayList;
import java.util.List;

public class CheckAllCommentBiz implements CheckAllCommentAble {

    @Override
    public String getGameId(Intent intent) {
        return intent.getExtras().getString( "gameId" );
    }

    @Override
    public List<DynamicCommentModel> getCommentModes(ResultItem resultItem) {
        List<DynamicCommentModel> models = new ArrayList<DynamicCommentModel>();
        List<ResultItem> comments = resultItem.getItems( "list" );
        if (null != comments) {
            for (ResultItem item : comments) {
                DynamicCommentModel model = new DynamicCommentModel();
                model.setCommentContent( item.getString( "content" ) );
                model.setCommentId( item.getString( "id" ) );
                model.setCommentTime( item.getString( "create_time" ) );
                model.setCommentDislike( item.getString( "dislike" ) );
                model.setCommentLikes( item.getString( "likes" ) );
                model.setDynamicId( item.getString( "dynamics_id" ) );
                model.setToUserId( item.getString( "to_uid" ) );
                model.setToUserIsVip( item.getBooleanValue( "touid_vip", 1 ) );
                model.setToUserNick( item.getString( "touid_nickname" ) );
                model.setToUserHeader( item.getString( "touid_iconurl" ) );
                model.setLikeTy( item.getIntValue( "like_type" ) );
                model.setIsFake( item.getIntValue( "is_fake" ) );
                model.setOrder( item.getIntValue( "order" ) );

                DriverModel driverModel = new DriverModel();
                driverModel.setDriverId( item.getString( "uid" ) );
                driverModel.setHeader( item.getString( "uid_iconurl" ) );
                driverModel.setVip( item.getBooleanValue( "uid_vip", 1 ) );
                driverModel.setNick( item.getString( "uid_nickname" ) );
                model.setDriverModel( driverModel );
                models.add( model );
            }
        }
        return models;
    }
}
