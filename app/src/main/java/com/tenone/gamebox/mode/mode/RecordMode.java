/** 
 * Project Name:GameBox 
 * File Name:RecordMode.java 
 * Package Name:com.tenone.gamebox.mode.mode 
 * Date:2017-3-28����10:38:06 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

/**
 * ������¼ ClassName:RecordMode <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-28 ����10:38:06 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class RecordMode implements Serializable {

	/**
	 * serialVersionUID:TODO(��һ�仰�������������ʾʲô).
	 * 
	 * @since JDK 1.6
	 */
	private static final long serialVersionUID = -1028560005982222582L;

	/* ͼƬ��Դid */
	int resId;
	/* ��¼ */
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
