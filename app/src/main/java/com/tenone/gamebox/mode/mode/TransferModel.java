package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

public class TransferModel implements Serializable{
	private static final long serialVersionUID = -6665210233509366585L;
	private String newGameName;
	private String oldGameName;
	private String newServer;
	private String oldServer;
	private String newRoleName;
	private String oldRoleName;
	private int state;
	private String transferTime;

	public String getNewGameName() {
		return newGameName;
	}

	public void setNewGameName(String newGameName) {
		this.newGameName = newGameName;
	}

	public String getOldGameName() {
		return oldGameName;
	}

	public void setOldGameName(String oldGameName) {
		this.oldGameName = oldGameName;
	}

	public String getNewServer() {
		return newServer;
	}

	public void setNewServer(String newServer) {
		this.newServer = newServer;
	}

	public String getOldServer() {
		return oldServer;
	}

	public void setOldServer(String oldServer) {
		this.oldServer = oldServer;
	}

	public String getNewRoleName() {
		return newRoleName;
	}

	public void setNewRoleName(String newRoleName) {
		this.newRoleName = newRoleName;
	}

	public String getOldRoleName() {
		return oldRoleName;
	}

	public void setOldRoleName(String oldRoleName) {
		this.oldRoleName = oldRoleName;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getTransferTime() {
		return transferTime;
	}

	public void setTransferTime(String transferTime) {
		this.transferTime = transferTime;
	}
}
