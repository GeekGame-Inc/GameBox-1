/** 
 * Project Name:GameBox 
 * File Name:GameDownloadTab.java 
 * Package Name:com.tenone.gamebox.view.utils.database 
 * Date:2017-4-1����10:28:44 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.utils.database;

/**
 * ��Ϸ���ر� ClassName:GameDownloadTab <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-1 ����10:28:44 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class GameDownloadTab {

	public static final String _ID = "_id";// ����id

	public static final String TABLE_NAME = "downloadtab";// ����
	public static final String GAMENAME = "game_name";// ��Ϸ����
	public static final String GAMEID = "game_id";// ��Ϸid
	public static final String GAMEURL = "game_url";// ��Ϸ���ص�ַ
	public static final String GAMESIZE = "game_size";// ��Ϸ��С
	public static final String DOWNSTATUS = "down_status";// ����״̬
	public static final String GAMEGRADE = "game_grade";// ��Ϸ����
	public static final String DOWNTIMES = "down_times";// ���ش���
	public static final String TIME = "game_time";// ��Ϸ����ʱ��
	public static final String INSTALLPATH = "game_installPath";// ��Ϸ��װ��ַ
	public static final String PACKGENAME = "game_packgeName";// ��Ϸ����
	public static final String APKNAME = "game_apkName";// ��Ϸ��װ������
	public static final String IMGURL = "game_imgUrl";// ��ϷͼƬ��ַ
	public static final String GAMEVERSIONS = "game_versions";// ��Ϸ�汾
	public static final String PROGRESS = "down_progress";// ���ؽ���
	public static final String LABEL = "game_label";// ��Ϸ��ǩ
	public static final String TAG = "game_tag";// ��Ϸ��д

	/**
	 * �������ݿ�����
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
	 * ����ɾ�����SQL��䣨DROP TABLE IF EXISTS TABLENAME��
	 */
	public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;
}
