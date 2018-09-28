/** 
 * Project Name:GameBox 
 * File Name:GamePackMode.java 
 * Package Name:com.tenone.gamebox.mode.mode 
 * Date:2017-5-4обнГ2:38:37 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

/**
 * ClassName:GamePackMode <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-5-4 обнГ2:38:37 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class GamePackMode implements Serializable {

	private static final long serialVersionUID = -430113408601485645L;
	private Integer gameId;
    private String packName;
    private String version;

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
