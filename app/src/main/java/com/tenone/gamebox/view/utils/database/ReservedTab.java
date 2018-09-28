package com.tenone.gamebox.view.utils.database;

public class ReservedTab {
    public static final String _ID = "_id";// 自增id
    public static final String TABLE_NAME = "reservedtab";// 表名
    public static final String GAMENAME = "game_name";// 游戏名字
    public static final String GAMEID = "game_id";// 游戏id
    public static final String IMGURL = "game_imgUrl";// 游戏图片地址
    public static final String TIME = "open_time";// 上线时间

    /**
     * 创建数据库命令
     */
    public static final String CREATE_TABLE_SQL = "create table " + TABLE_NAME
            + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + GAMEID
            + " INTEGER," + GAMENAME + " TEXT," + TIME + " TEXT," + IMGURL + " TEXT);";
    /**
     * 定义删除表的SQL语句（DROP TABLE IF EXISTS TABLENAME）
     */
    public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS "
            + TABLE_NAME;
}
