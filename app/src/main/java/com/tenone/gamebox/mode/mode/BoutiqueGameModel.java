package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

public class BoutiqueGameModel implements Serializable {
	private int gameId;
	private String gameName;
	private String gameIcon;
	private String gameType;
	private String gameSize;
	private String gameIntro;
	private int gameCommentCounts;
	private String[] gameImgs;
	private String gameAdsImg;

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameIcon() {
		return gameIcon;
	}

	public void setGameIcon(String gameIcon) {
		this.gameIcon = gameIcon;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getGameSize() {
		return gameSize;
	}

	public void setGameSize(String gameSize) {
		this.gameSize = gameSize;
	}

	public String getGameIntro() {
		return gameIntro;
	}

	public void setGameIntro(String gameIntro) {
		this.gameIntro = gameIntro;
	}

	public int getGameCommentCounts() {
		return gameCommentCounts;
	}

	public void setGameCommentCounts(int gameCommentCounts) {
		this.gameCommentCounts = gameCommentCounts;
	}

	public String[] getGameImgs() {
		return gameImgs;
	}

	public void setGameImgs(String[] gameImgs) {
		this.gameImgs = gameImgs;
	}

	public String getGameAdsImg() {
		return gameAdsImg;
	}

	public void setGameAdsImg(String gameAdsImg) {
		this.gameAdsImg = gameAdsImg;
	}
}
