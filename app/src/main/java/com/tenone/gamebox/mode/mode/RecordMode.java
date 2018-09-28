/** 
 * Project Name:GameBox 
 * File Name:RecordMode.java 
 * Package Name:com.tenone.gamebox.mode.mode 
 * Date:2017-3-28上午10:38:06 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

/**
 * 搜索记录 ClassName:RecordMode <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-28 上午10:38:06 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class RecordMode implements Serializable {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.6
	 */
	private static final long serialVersionUID = -1028560005982222582L;

	/* 图片资源id */
	int resId;
	/* 记录 */
	String record;

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

}
