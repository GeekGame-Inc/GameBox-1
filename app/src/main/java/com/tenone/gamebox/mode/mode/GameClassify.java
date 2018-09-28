/** 
 * Project Name:GameBox 
 * File Name:GameClassify.java 
 * Package Name:com.tenone.gamebox.mode.mode 
 * Date:2017-3-9下午4:16:47 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

/**
 * 游戏分类 ClassName:GameClassify <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-9 下午4:16:47 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class GameClassify implements Serializable {
	private static final long serialVersionUID = 5398922446232828524L;
	/* 分类名字 */
	String classifyName;
	/* 分类图标地址 */
	String classifyImgUrl;
	/* 分类id */
	int classifyId;

	public int getClassifyId() {
		return classifyId;
	}

	public void setClassifyId(int classifyId) {
		this.classifyId = classifyId;
	}

	public String getClassifyName() {
		return classifyName;
	}

	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}

	public String getClassifyImgUrl() {
		return classifyImgUrl;
	}

	public void setClassifyImgUrl(String classifyImgUrl) {
		this.classifyImgUrl = classifyImgUrl;
	}
}
