/** 
 * Project Name:GameBox 
 * File Name:WarnTab.java 
 * Package Name:com.tenone.gamebox.view.utils.database 
 * Date:2017-4-12下午2:02:09 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.utils.database;

/**
 * 提醒表 ClassName:WarnTab <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-12 下午2:02:09 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class WarnTab {
	public static final String _ID = "_id";// 自增id
	public static final String TABLE_NAME = "warntab";// 表名
	public static final String GAMENAME = "game_name";// 游戏名字
	public static final String GAMEID = "game_id";// 游戏id
	public static final String IMGURL = "game_imgUrl";// 游戏图片地址
	public static final String GAMEVERSIONS = "game_versions";// 游戏版本
	public static final String SERVICEID = "service_id";// 服务器id
	public static final String TIME = "open_time";// 开服时间

	/**
	 * 创建数据库命令
	 */
	public static final String CREATE_TABLE_SQL = "create table " + TABLE_NAME
			+ " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + GAMEID
			+ " INTEGER," + GAMENAME + " TEXT," + TIME + " TEXT," + SERVICEID
			+ " INTEGER," + IMGURL + " TEXT," + GAMEVERSIONS + " TEXT);";
	/**
	 * 定义删除表的SQL语句（DROP TABLE IF EXISTS TABLENAME）
	 */
	public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;
}
