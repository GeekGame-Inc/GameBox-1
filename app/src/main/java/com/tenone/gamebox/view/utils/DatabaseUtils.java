

package com.tenone.gamebox.view.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GamePackMode;
import com.tenone.gamebox.mode.mode.OpenServiceNotificationMode;
import com.tenone.gamebox.mode.mode.ReservedAlarmModel;
import com.tenone.gamebox.view.utils.database.AlredyOpenServerTab;
import com.tenone.gamebox.view.utils.database.DatabaseOperator;
import com.tenone.gamebox.view.utils.database.GameDownloadTab;
import com.tenone.gamebox.view.utils.database.GameNameTab;
import com.tenone.gamebox.view.utils.database.ReservedTab;
import com.tenone.gamebox.view.utils.database.WarnTab;

import java.util.ArrayList;
import java.util.List;


public class DatabaseUtils {
    private DatabaseOperator dbOperator;

    public volatile static DatabaseUtils instance;

    private DatabaseUtils(Context context) {
        dbOperator = DatabaseOperator.getIntance( context );
    }

    
    public static DatabaseUtils getInstanse(Context context) {
        if (instance == null) {
            synchronized (DatabaseUtils.class) {
                if (instance == null) {
                    instance = new DatabaseUtils( context );
                }
            }
        }
        return instance;
    }

    
    public synchronized boolean isHave(String tabName, String selection) {
        boolean isHave = false;
        Cursor cursor = dbOperator.query( tabName, null, selection, null, null,
                null, null );
        try {
            isHave = cursor.moveToNext();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return isHave;
    }

    
    public synchronized void insertOneGamePack(GamePackMode mode) {
        String sql = "insert into " + GameNameTab.TABLE_NAME + "("
                + GameNameTab.GAMENAME + "," + GameNameTab.GAMEID + ","
                + GameNameTab.VERSION + ")values(" + "'" + mode.getPackName()
                + "','" + mode.getGameId() + "','" + mode.getVersion() + "')";
        dbOperator.insertBySQL( sql );
    }

    
    public synchronized void insertGamePacks(List<GamePackMode> list) {
        for (GamePackMode mode : list) {
            insertOneGamePack( mode );
        }
    }

    public synchronized List<GamePackMode> gamePackModes() {
        List<GamePackMode> gamePackModes = new ArrayList<GamePackMode>();
        Cursor cursor = dbOperator.selectAll( GameNameTab.TABLE_NAME );
        try {
            while (cursor.moveToNext()) {
                GamePackMode mode = new GamePackMode();
                mode.setGameId( cursor.getInt( cursor
                        .getColumnIndex( GameNameTab.GAMEID ) ) );
                mode.setPackName( cursor.getString( cursor
                        .getColumnIndex( GameNameTab.GAMENAME ) ) );
                mode.setVersion( cursor.getString( cursor
                        .getColumnIndex( GameNameTab.VERSION ) ) );
                gamePackModes.add( mode );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return gamePackModes;
    }

    public synchronized void cleanGamePacks() {
        dbOperator.deleteAll( GameNameTab.TABLE_NAME );
    }

    
    public synchronized void insertWarn(OpenServiceNotificationMode notificationMode) {
        String selection = WarnTab.SERVICEID + "='"
                + notificationMode.getServiceId() + "'" + " AND "
                + WarnTab.GAMEID + "='" + notificationMode.getGameId() + "'";
        if (!isHave( WarnTab.TABLE_NAME, selection )) {
            String time = notificationMode.getTime();
            
            
            
            
            String sql = "insert into " + WarnTab.TABLE_NAME + "("
                    + WarnTab.GAMENAME + "," + WarnTab.GAMEID + ","
                    + WarnTab.GAMEVERSIONS + "," + WarnTab.TIME + ","
                    + WarnTab.IMGURL + "," + WarnTab.SERVICEID + ")values("
                    + "'" + notificationMode.getGameName() + "','"
                    + notificationMode.getGameId() + "','"
                    + notificationMode.getGameVersions() + "','" + time + "','"
                    + notificationMode.getImgUrl() + "','"
                    + notificationMode.getServiceId() + "')";
            dbOperator.insertBySQL( sql );
        }
    }


    
    public synchronized void insertReserved(ReservedAlarmModel reservedAlarmModel) {
        String selection = ReservedTab.GAMEID + "='"
                + reservedAlarmModel.getGameId() + "'" + " AND "
                + ReservedTab.GAMENAME + "='" + reservedAlarmModel.getGameName() + "'";
        if (!isHave( ReservedTab.TABLE_NAME, selection )) {
            String time = reservedAlarmModel.getTime();
            String sql = "insert into " + ReservedTab.TABLE_NAME + "("
                    + ReservedTab.GAMENAME + "," + ReservedTab.GAMEID + ","
                    + ReservedTab.TIME + "," + ReservedTab.IMGURL + ")values("
                    + "'" + reservedAlarmModel.getGameName() + "','"
                    + reservedAlarmModel.getGameId() + "','" + time + "','"
                    + reservedAlarmModel.getImgUrl() + "')";
            dbOperator.insertBySQL( sql );
        }
    }

    
    public synchronized void deleteReserved(ReservedAlarmModel reservedAlarmModel) {
        String sql = "delete from " + ReservedTab.TABLE_NAME + " where "
                + ReservedTab.GAMENAME + "=" + "'" + reservedAlarmModel.getGameName()
                + "'" + " and " + ReservedTab.GAMEID + "=" + "'" + reservedAlarmModel.getGameId() + "'";
        dbOperator.deleteBySQL( sql );
    }

    
    public synchronized List<ReservedAlarmModel> getReserveds() {
        List<ReservedAlarmModel> reservedAlarmModels = new ArrayList<ReservedAlarmModel>();
        Cursor cursor = dbOperator.selectAll( ReservedTab.TABLE_NAME );
        while (cursor.moveToNext()) {
            ReservedAlarmModel reservedAlarmModel = new ReservedAlarmModel();
            reservedAlarmModel.setGameId( cursor.getInt( cursor
                    .getColumnIndex( ReservedTab.GAMEID ) ) );
            reservedAlarmModel.setGameName( cursor.getString( cursor
                    .getColumnIndex( ReservedTab.GAMENAME ) ) );
            reservedAlarmModel.setTime( cursor.getString( cursor
                    .getColumnIndex( ReservedTab.TIME ) ) );
            reservedAlarmModel.setImgUrl( cursor.getString( cursor
                    .getColumnIndex( ReservedTab.IMGURL ) ) );
            reservedAlarmModels.add( reservedAlarmModel );
        }
        return reservedAlarmModels;
    }

    
    public synchronized void insertAlready(
            OpenServiceNotificationMode notificationMode) {
        String selection = AlredyOpenServerTab.SERVICEID + "='"
                + notificationMode.getServiceId() + "'" + " AND "
                + WarnTab.GAMEID + "='" + notificationMode.getGameId() + "'";
        if (!isHave( AlredyOpenServerTab.TABLE_NAME, selection )) {
            String time = notificationMode.getTime();
            String sql = "insert into " + AlredyOpenServerTab.TABLE_NAME + "("
                    + AlredyOpenServerTab.GAMENAME + ","
                    + AlredyOpenServerTab.GAMEID + ","
                    + AlredyOpenServerTab.GAMEVERSIONS + ","
                    + AlredyOpenServerTab.TIME + ","
                    + AlredyOpenServerTab.IMGURL + ","
                    + AlredyOpenServerTab.SERVICEID + ")values(" + "'"
                    + notificationMode.getGameName() + "','"
                    + notificationMode.getGameId() + "','"
                    + notificationMode.getGameVersions() + "','" + time + "','"
                    + notificationMode.getImgUrl() + "','"
                    + notificationMode.getServiceId() + "')";
            dbOperator.insertBySQL( sql );
        }
    }

    
    public synchronized void deleteWarn(
            OpenServiceNotificationMode notificationMode) {
        String sql = "delete from " + WarnTab.TABLE_NAME + " where "
                + WarnTab.GAMENAME + "=" + "'" + notificationMode.getGameName()
                + "'" + " and " + WarnTab.SERVICEID + "=" + "'"
                + notificationMode.getServiceId() + "'" + " and "
                + WarnTab.GAMEID + "=" + "'" + notificationMode.getGameId()
                + "'";
        dbOperator.deleteBySQL( sql );
    }


    
    public synchronized void deleteAlready(
            OpenServiceNotificationMode notificationMode) {
        String sql = "delete from " + AlredyOpenServerTab.TABLE_NAME + " where "
                + AlredyOpenServerTab.GAMENAME + "=" + "'" + notificationMode.getGameName()
                + "'" + " and " + AlredyOpenServerTab.SERVICEID + "=" + "'"
                + notificationMode.getServiceId() + "'" + " and "
                + AlredyOpenServerTab.GAMEID + "=" + "'" + notificationMode.getGameId()
                + "'";
        dbOperator.deleteBySQL( sql );
    }


    public synchronized boolean deleteWarn(String serviceId, String gameId) {
        String sql = "delete from " + WarnTab.TABLE_NAME + " where "
                + WarnTab.SERVICEID + "=" + "'" + serviceId + "'" + " and "
                + WarnTab.GAMEID + "=" + "'" + gameId + "'";
        return dbOperator.deleteBySQL( sql );
    }

    public synchronized boolean deleteWarn() {
        return dbOperator.deleteAll( WarnTab.TABLE_NAME );
    }


    public synchronized boolean deleteAlready() {
        return dbOperator.deleteAll( AlredyOpenServerTab.TABLE_NAME );
    }


    public synchronized OpenServiceNotificationMode getWarn(String serviceId,
                                                            String gameId) {
        OpenServiceNotificationMode notificationMode = new OpenServiceNotificationMode();
        String selection = WarnTab.SERVICEID + "='" + serviceId + "' And "
                + WarnTab.GAMEID + "='" + gameId + "'";
        Cursor cursor = dbOperator.query( WarnTab.TABLE_NAME, null, selection,
                null, null, null, null );
        try {
            while (cursor.moveToNext()) {
                notificationMode.setGameId( cursor.getString( cursor
                        .getColumnIndex( WarnTab.GAMEID ) ) );
                notificationMode.setServiceId( cursor.getString( cursor
                        .getColumnIndex( WarnTab.SERVICEID ) ) );
                notificationMode.setGameName( cursor.getString( cursor
                        .getColumnIndex( WarnTab.GAMENAME ) ) );
                notificationMode.setGameVersions( cursor.getString( cursor
                        .getColumnIndex( WarnTab.GAMEVERSIONS ) ) );
                notificationMode.setTime( cursor.getString( cursor
                        .getColumnIndex( WarnTab.TIME ) ) );
                notificationMode.setImgUrl( cursor.getString( cursor
                        .getColumnIndex( WarnTab.IMGURL ) ) );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return notificationMode;
    }


    
    public synchronized List<OpenServiceNotificationMode> getWarns() {
        List<OpenServiceNotificationMode> notificationModes = new ArrayList<OpenServiceNotificationMode>();
        Cursor cursor = dbOperator.selectAll( WarnTab.TABLE_NAME );
        while (cursor.moveToNext()) {
            OpenServiceNotificationMode notificationMode = new OpenServiceNotificationMode();
            notificationMode.setGameId( cursor.getString( cursor
                    .getColumnIndex( WarnTab.GAMEID ) ) );
            notificationMode.setServiceId( cursor.getString( cursor
                    .getColumnIndex( WarnTab.SERVICEID ) ) );
            notificationMode.setGameName( cursor.getString( cursor
                    .getColumnIndex( WarnTab.GAMENAME ) ) );
            notificationMode.setTime( cursor.getString( cursor
                    .getColumnIndex( WarnTab.TIME ) ) );
            notificationMode.setGameVersions( cursor.getString( cursor
                    .getColumnIndex( WarnTab.GAMEVERSIONS ) ) );
            notificationMode.setImgUrl( cursor.getString( cursor
                    .getColumnIndex( WarnTab.IMGURL ) ) );
            notificationModes.add( notificationMode );
        }
        return notificationModes;
    }


    
    public synchronized List<OpenServiceNotificationMode> getAlready() {
        List<OpenServiceNotificationMode> notificationModes = new ArrayList<OpenServiceNotificationMode>();
        Cursor cursor = dbOperator.selectAll( AlredyOpenServerTab.TABLE_NAME );
        try {
            while (cursor.moveToNext()) {
                OpenServiceNotificationMode notificationMode = new OpenServiceNotificationMode();
                notificationMode.setGameId( cursor.getString( cursor
                        .getColumnIndex( AlredyOpenServerTab.GAMEID ) ) );
                notificationMode.setServiceId( cursor.getString( cursor
                        .getColumnIndex( AlredyOpenServerTab.SERVICEID ) ) );
                notificationMode.setGameName( cursor.getString( cursor
                        .getColumnIndex( AlredyOpenServerTab.GAMENAME ) ) );
                notificationMode.setTime( cursor.getString( cursor
                        .getColumnIndex( AlredyOpenServerTab.TIME ) ) );
                notificationMode.setGameVersions( cursor.getString( cursor
                        .getColumnIndex( AlredyOpenServerTab.GAMEVERSIONS ) ) );
                notificationMode.setImgUrl( cursor.getString( cursor
                        .getColumnIndex( AlredyOpenServerTab.IMGURL ) ) );
                notificationModes.add( notificationMode );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return notificationModes;
    }

    
    public synchronized List<GameModel> getDownloadList() {
        List<GameModel> gameModels = new ArrayList<GameModel>();
        Cursor cursor = dbOperator.selectAll( GameDownloadTab.TABLE_NAME );
        try {
            while (cursor.moveToNext()) {
                GameModel model = new GameModel();
                model.setName( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.GAMENAME ) ) );
                model.setApkName( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.APKNAME ) ) );
                model.setGameId( cursor.getInt( cursor
                        .getColumnIndex( GameDownloadTab.GAMEID ) ) );
                model.setVersionsName( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.GAMEVERSIONS ) ) );
                model.setProgress( Integer.valueOf(
                        cursor.getString( cursor
                                .getColumnIndex( GameDownloadTab.PROGRESS ) ) )
                        .intValue() );
                model.setGrade( Float.valueOf(
                        cursor.getString( cursor
                                .getColumnIndex( GameDownloadTab.GAMEGRADE ) ) )
                        .floatValue() );
                model.setImgUrl( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.IMGURL ) ) );
                String label = cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.LABEL ) );
                String[] array = label.split( "," );
                model.setLabelArray( array );
                model.setPackgeName( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.PACKGENAME ) ) );
                model.setTime( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.TIME ) ) );
                model.setTimes( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.DOWNTIMES ) ) );
                model.setStatus( cursor.getInt( cursor
                        .getColumnIndex( GameDownloadTab.DOWNSTATUS ) ) );
                model.setSize( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.GAMESIZE ) ) );
                model.setUrl( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.GAMEURL ) ) );
                model.setInstallPath( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.INSTALLPATH ) ) );
                model.setGameTag( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.TAG ) ) );
                gameModels.add( model );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return gameModels;
    }

    public synchronized GameModel getGameModel(String where, String[] values) {
        GameModel model = new GameModel();
        Cursor cursor = dbOperator.selectByWhere( GameDownloadTab.TABLE_NAME,
                where, values );
        try {
            while (cursor.moveToNext()) {
                model.setName( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.GAMENAME ) ) );
                model.setApkName( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.APKNAME ) ) );
                model.setGameId( cursor.getInt( cursor
                        .getColumnIndex( GameDownloadTab.GAMEID ) ) );
                model.setVersionsName( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.GAMEVERSIONS ) ) );
                model.setProgress( Integer.valueOf(
                        cursor.getString( cursor
                                .getColumnIndex( GameDownloadTab.PROGRESS ) ) )
                        .intValue() );
                model.setGrade( Float.valueOf(
                        cursor.getString( cursor
                                .getColumnIndex( GameDownloadTab.GAMEGRADE ) ) )
                        .floatValue() );
                model.setImgUrl( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.IMGURL ) ) );
                String label = cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.LABEL ) );
                String[] array = label.split( "," );
                model.setLabelArray( array );
                model.setPackgeName( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.PACKGENAME ) ) );
                model.setTime( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.TIME ) ) );
                model.setTimes( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.DOWNTIMES ) ) );
                model.setStatus( cursor.getInt( cursor
                        .getColumnIndex( GameDownloadTab.DOWNSTATUS ) ) );
                model.setSize( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.GAMESIZE ) ) );
                model.setUrl( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.GAMEURL ) ) );
                model.setInstallPath( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.INSTALLPATH ) ) );
                model.setGameTag( cursor.getString( cursor
                        .getColumnIndex( GameDownloadTab.TAG ) ) );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return model;
    }

    
    public synchronized void addDownload(GameModel gameModel) {
        ContentValues values = new ContentValues();
        values.put( GameDownloadTab.APKNAME, gameModel.getApkName() );
        values.put( GameDownloadTab.DOWNSTATUS, gameModel.getStatus() );
        values.put( GameDownloadTab.DOWNTIMES, gameModel.getTimes() );
        values.put( GameDownloadTab.GAMEGRADE, gameModel.getGrade() + "" );
        values.put( GameDownloadTab.GAMEID, gameModel.getGameId() );
        values.put( GameDownloadTab.GAMENAME, gameModel.getName() );
        values.put( GameDownloadTab.GAMESIZE, gameModel.getSize() );
        values.put( GameDownloadTab.GAMEURL, gameModel.getUrl() );
        values.put( GameDownloadTab.GAMEVERSIONS, gameModel.getVersionsName() );
        values.put( GameDownloadTab.IMGURL, gameModel.getImgUrl() );
        values.put( GameDownloadTab.INSTALLPATH, gameModel.getInstallPath() );
        values.put( GameDownloadTab.TAG, gameModel.getGameTag() );
        StringBuilder builde = new StringBuilder();
        if (gameModel.getLabelArray() != null) {
            for (int i = 0; i < gameModel.getLabelArray().length; i++) {
                if (i < (gameModel.getLabelArray().length - 1)) {
                    builde.append( gameModel.getLabelArray()[i] + "," );
                } else {
                    builde.append( gameModel.getLabelArray()[i] );
                }
            }
        }
        values.put( GameDownloadTab.LABEL, builde.toString() );
        values.put( GameDownloadTab.PACKGENAME, gameModel.getPackgeName() );
        values.put( GameDownloadTab.PROGRESS, gameModel.getProgress() + "" );
        values.put( GameDownloadTab.TIME, gameModel.getTime() );
        dbOperator.insert( GameDownloadTab.TABLE_NAME, values );
    }

    
    public synchronized void deleteDownload(GameModel gameModel) {
        String sql = "delete from " + GameDownloadTab.TABLE_NAME + " where "
                + GameDownloadTab.GAMENAME + "=" + "'" + gameModel.getName()
                + "'" + " and " + GameDownloadTab.GAMEURL + "=" + "'"
                + gameModel.getUrl() + "'" + " and " + GameDownloadTab.GAMEID
                + "=" + "'" + gameModel.getGameId() + "'";
        dbOperator.deleteBySQL( sql );
        
        
        
        
    }

    
    public synchronized void deleteGame(String packgeName) {
        String sql = "delete from " + GameDownloadTab.TABLE_NAME + " where "
                + GameDownloadTab.PACKGENAME + "=" + "'" + packgeName + "'";
        dbOperator.deleteBySQL( sql );
        
        
        
        
    }

    
    public synchronized void deleteAllDownload() {
        dbOperator.deleteAll( GameDownloadTab.TABLE_NAME );
    }

    
    public synchronized void updateDownload(ContentValues values,
                                            String selection, String[] arg) {
        dbOperator.update( GameDownloadTab.TABLE_NAME, values, selection, arg );
    }

    
    public synchronized void updateDownload(String sql) {
        dbOperator.updateBySQL( sql );
    }

    public synchronized void updateDownload(GameModel gameModel) {
        dbOperator.update(
                GameDownloadTab.TABLE_NAME,
                GameDownloadTab.GAMEURL,
                gameModel.getUrl(),
                new String[]{GameDownloadTab.PROGRESS,
                        GameDownloadTab.APKNAME, GameDownloadTab.DOWNSTATUS},
                new String[]{gameModel.getProgress() + "",
                        gameModel.getApkName(), gameModel.getStatus() + ""} );
    }
}
