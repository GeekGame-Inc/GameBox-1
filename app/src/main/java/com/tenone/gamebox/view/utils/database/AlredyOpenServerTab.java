/** 
 * Project Name:GameBox 
 * File Name:AlredyOpenServerTab.java 
 * Package Name:com.tenone.gamebox.view.utils.database 
 * Date:2017-5-9����11:40:19 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.utils.database;

/**
 * �Ѿ������˵���Ϸ ClassName:AlredyOpenServerTab <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-5-9 ����11:40:19 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class AlredyOpenServerTab {
	public static final String _ID = "_id";// ����id
	public static final String TABLE_NAME = "alread_open_tab";// ����
	public static final String GAMENAME = "game_name";// ��Ϸ����
	public static final String GAMEID = "game_id";// ��Ϸid
	public static final String IMGURL = "game_imgUrl";// ��ϷͼƬ��ַ
	public static final String GAMEVERSIONS = "game_versions";// ��Ϸ�汾
	public static final String SERVICEID = "service_id";// ������id
	public static final String TIME = "open_time";// ����ʱ��

	/**
	 * �������ݿ�����
	 */
	public static final String CREATE_TABLE_SQL = "create table " + TABLE_NAME
			+ " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + GAMEID
			+ " INTEGER," + GAMENAME + " TEXT," + TIME + " TEXT," + SERVICEID
			+ " INTEGER," + IMGURL + " TEXT," + GAMEVERSIONS + " TEXT);";
	/**
	 * ����ɾ�����SQL��䣨DROP TABLE IF EXISTS TABLENAME��
	 */
	public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;
}
