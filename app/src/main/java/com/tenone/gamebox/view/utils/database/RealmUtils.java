package com.tenone.gamebox.view.utils.database;

import android.content.Context;

import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.ReservedAlarmModel;

import java.util.List;


public class RealmUtils {

    public static void initRealm(Context context) {
        RealmHelper.init( context );
    }

    public static void addReservedAlarm(GameModel gameModel) {
        ReservedAlarmModel m = new ReservedAlarmModel( gameModel.getName(), gameModel.getGameId(),
                gameModel.getImgUrl(), gameModel.getTime() );
        RealmHelper.addObject( m );
    }

    public static void deleteReservedAlarm(ReservedAlarmModel model) {
        RealmHelper.deleteObject( ReservedAlarmModel.class, model );
    }

    public static ReservedAlarmModel queryReservedAlarm(String gameName) {
        return (ReservedAlarmModel) RealmHelper
                .queryObjectByString( "gameName", gameName, ReservedAlarmModel.class );
    }

    public static ReservedAlarmModel queryReservedAlarm(int gameId) {
        return (ReservedAlarmModel) RealmHelper
                .queryObjectByInt( "gameId", gameId, ReservedAlarmModel.class );
    }

    public static List<ReservedAlarmModel> queryReservedAlarmAll() {
        return RealmHelper.queryObjectAll( ReservedAlarmModel.class );
    }

    public static void updateReservedAlarm(ReservedAlarmModel model) {
        RealmHelper.updateObject( model );
    }
}
