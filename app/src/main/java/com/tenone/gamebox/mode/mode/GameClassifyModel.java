/** 
 * Project Name:GameBox 
 * File Name:GameClassifyModel.java 
 * Package Name:com.tenone.gamebox.mode.mode 
 * Date:2017-4-20下午2:28:43 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
*/  
  
package com.tenone.gamebox.mode.mode;  

import java.io.Serializable;
import java.util.List;

/** 
 * ClassName:GameClassifyModel <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     2017-4-20 下午2:28:43 <br/> 
 * @author   John Lie 
 * @version   
 * @since    JDK 1.6 
 * @see       
 */
public class GameClassifyModel implements Serializable{

	/** 
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
	 * @since JDK 1.6 
	 */  
	private static final long serialVersionUID = 5140634322221068813L;

	String className;
	
	List<GameModel> gameModels;
	
	String classId;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<GameModel> getGameModels() {
		return gameModels;
	}

	public void setGameModels(List<GameModel> gameModels) {
		this.gameModels = gameModels;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}
	
}
  