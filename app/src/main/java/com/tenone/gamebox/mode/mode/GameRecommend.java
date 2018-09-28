/** 
 * Project Name:GameBox 
 * File Name:GameRecommend.java 
 * Package Name:com.tenone.gamebox.mode.mode 
 * Date:2017-3-8����4:38:24 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.mode;

import java.io.Serializable;
import java.util.List;

/**
 * ��Ϸ�Ƽ�����ģ�� ClassName:GameRecommend <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-8 ����4:38:24 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class GameRecommend implements Serializable {

	private static final long serialVersionUID = 5642557942705432020L;

	/**
	 * �������� 0(ʱ��),1(����)
	 */
	private int type;

	/**
	 * ����
	 */
	private List<GameModel> model;
	
	/**
	 * ʱ��
	 */
	private String time;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<GameModel> getModel() {
		return model;
	}

	public void setModel(List<GameModel> model) {
		this.model = model;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
