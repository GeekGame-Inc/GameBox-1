package com.tenone.gamebox.view.utils;

import android.content.Context;
import android.content.Intent;

import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.view.service.ReservedService;

public class ReservedUtils {

    public static void addReserved(Context context, GameModel gameModel) {
        Intent intent = new Intent( context, ReservedService.class );
        intent.putExtra( "gameModel", gameModel );
        context.startService( intent );
    }

    public static void registerReserved(Context context){
        Intent intent = new Intent( context, ReservedService.class );
        context.startService( intent );
    }
}
