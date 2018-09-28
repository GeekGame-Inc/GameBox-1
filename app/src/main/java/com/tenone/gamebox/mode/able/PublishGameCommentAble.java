
package com.tenone.gamebox.mode.able;


import android.content.Intent;

import com.tenone.gamebox.mode.mode.DynamicCommentModel;

public interface PublishGameCommentAble {

    DynamicCommentModel getCommentModel(Intent intent);

    String getGameId(Intent intent);

    String getAction(Intent intent);

}
