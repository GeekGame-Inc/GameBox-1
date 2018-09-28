/** 
 * Project Name:GameBox 
 * File Name:GameNameTab.java 
 * Package Name:com.tenone.gamebox.view.utils.database 
 * Date:2017-3-28����5:15:58 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.utils.database;

/**
 * ��Ϸ���� ClassName:GameNameTab <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-28 ����5:15:58 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class GameNameTab {

	public static final String _ID = "_id";// ����id
	public static final String GAMENAME = "game_name";// ��Ϸ����
	public static final String TABLE_NAME = "gamenametab";// ����
	public static final String GAMEID = "game_id";// ��Ϸid
	public static final String VERSION = "game_version";// ��Ϸ�汾

	/**
	 * �������ݿ�����
	 */
	public static final String CREATE_TABLE_SQL = "create table " + TABLE_NAME
			+ " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + GAMENAME
			+ " TEXT," + GAMEID + " INTEGER," + VERSION + " TEXT);";
	/**
	 * ����ɾ�����SQL��䣨DROP TABLE IF EXISTS TABLENAME��
	 */
	public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

}
