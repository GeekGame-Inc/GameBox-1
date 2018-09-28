/** 
 * Project Name:GameBox 
 * File Name:DownStatus.java 
 * Package Name:com.tenone.gamebox.mode.mode 
 * Date:2017-3-15上午10:15:29 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.mode;

/**
 * 游戏状态 ClassName:DownStatus <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-15 上午10:15:29 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class GameStatus {
	/**
	 * 未下载
	 */
	public static final int UNLOAD = 0;
	/**
	 * 下载中
	 */
	public static final int LOADING = 1;
	/**
	 * 暂停中
	 */
	public static final int PAUSEING = 2;
	/**
	 * 下载完成
	 */
	public static final int COMPLETED = 3;
	/**
	 * 安装中
	 */
	public static final int INSTALLING = 4;
	/**
	 * 安装完成
	 */
	public static final int INSTALLCOMPLETED = 5;

	/**
	 * 更新
	 */
	public static final int UPDATE = 6;
	
	/**
	 * 删除
	 */
	public static final int DELETE=7;
	
	/**
	 * 未安装
	 */
	public static final int UNINSTALLING=8;
}
