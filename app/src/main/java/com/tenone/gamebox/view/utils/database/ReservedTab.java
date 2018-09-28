package com.tenone.gamebox.view.utils.database;

public class ReservedTab {
    public static final String _ID = "_id";// ����id
    public static final String TABLE_NAME = "reservedtab";// ����
    public static final String GAMENAME = "game_name";// ��Ϸ����
    public static final String GAMEID = "game_id";// ��Ϸid
    public static final String IMGURL = "game_imgUrl";// ��ϷͼƬ��ַ
    public static final String TIME = "open_time";// ����ʱ��

    /**
     * �������ݿ�����
     */
    public static final String CREATE_TABLE_SQL = "create table " + TABLE_NAME
            + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + GAMEID
            + " INTEGER," + GAMENAME + " TEXT," + TIME + " TEXT," + IMGURL + " TEXT);";
    /**
     * ����ɾ�����SQL��䣨DROP TABLE IF EXISTS TABLENAME��
     */
    public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS "
            + TABLE_NAME;
}
