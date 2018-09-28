/** 
 * Project Name:GameBox 
 * File Name:GameDownloadTab.java 
 * Package Name:com.tenone.gamebox.view.utils.database 
 * Date:2017-4-1上午10:28:44 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.utils.database;

/**
 * 游戏下载表 ClassName:GameDownloadTab <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-1 上午10:28:44 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class GameDownloadTab {

	public static final String _ID = "_id";// 自增id

	public static final String TABLE_NAME = "downloadtab";// 表名
	public static final String GAMENAME = "game_name";// 游戏名字
	public static final String GAMEID = "game_id";// 游戏id
	public static final String GAMEURL = "game_url";// 游戏下载地址
	public static final String GAMESIZE = "game_size";// 游戏大小
	public static final String DOWNSTATUS = "down_status";// 下载状态
	public static final String GAMEGRADE = "game_grade";// 游戏评分
	public static final String DOWNTIMES = "down_times";// 下载次数
	public static final String TIME = "game_time";// 游戏发布时间
	public static final String INSTALLPATH = "game_installPath";// 游戏安装地址
	public static final String PACKGENAME = "game_packgeName";// 游戏包名
	public static final String APKNAME = "game_apkName";// 游戏安装包名字
	public static final String IMGURL = "game_imgUrl";// 游戏图片地址
	public static final String GAMEVERSIONS = "game_versions";// 游戏版本
	public static final String PROGRESS = "down_progress";// 下载进度
	public static final String LABEL = "game_label";// 游戏标签
	public static final String TAG = "game_tag";// 游戏简写

	/**
	 * 创建数据库命令
	 */
	public static final String CREATE_TABLE_SQL = "create table " + TABLE_NAME
			+ " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + GAMEID
			+ " INTEGER," + GAMENAME + " TEXT," + GAMEURL + " TEXT," + GAMESIZE
			+ " TEXT," + DOWNSTATUS + " INTEGER," + GAMEGRADE + " TEXT,"
			+ DOWNTIMES + " TEXT," + TIME + " TEXT," + INSTALLPATH + " TEXT," 
			+ PACKGENAME + " TEXT," + APKNAME + " TEXT," + IMGURL + " TEXT,"
			+ GAMEVERSIONS + " TEXT," + PROGRESS + " TEXT," + LABEL + " TEXT,"
			+ TAG + " TEXT);";
	/**
	 * 定义删除表的SQL语句（DROP TABLE IF EXISTS TABLENAME）
	 */
	public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;
}
