
package com.tenone.gamebox.mode.biz;


import android.content.Intent;

import com.tenone.gamebox.mode.able.PublishGameCommentAble;
import com.tenone.gamebox.mode.mode.DynamicCommentModel;

public class PublishGameCommentBiz implements PublishGameCommentAble {

    @Override
    public DynamicCommentModel getCommentModel(Intent intent) {
        return (DynamicCommentModel) intent.getExtras().get( "model" );
    }

    @Override
    public String getGameId(Intent intent) {
        return intent.getExtras().getString( "gameId" );
    }

    @Override
    public String getAction(Intent intent) {
        return intent.getAction();
    }
}
