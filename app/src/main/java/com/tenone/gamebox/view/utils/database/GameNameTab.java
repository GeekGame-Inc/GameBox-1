/** 
 * Project Name:GameBox 
 * File Name:GameNameTab.java 
 * Package Name:com.tenone.gamebox.view.utils.database 
 * Date:2017-3-28下午5:15:58 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.utils.database;

/**
 * 游戏包名 ClassName:GameNameTab <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-28 下午5:15:58 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class GameNameTab {

	public static final String _ID = "_id";// 自增id
	public static final String GAMENAME = "game_name";// 游戏包名
	public static final String TABLE_NAME = "gamenametab";// 表名
	public static final String GAMEID = "game_id";// 游戏id
	public static final String VERSION = "game_version";// 游戏版本

	/**
	 * 创建数据库命令
	 */
	public static final String CREATE_TABLE_SQL = "create table " + TABLE_NAME
			+ " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + GAMENAME
			+ " TEXT," + GAMEID + " INTEGER," + VERSION + " TEXT);";
	/**
	 * 定义删除表的SQL语句（DROP TABLE IF EXISTS TABLENAME）
	 */
	public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

}
