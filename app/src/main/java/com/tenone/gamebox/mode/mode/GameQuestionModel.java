package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

public class GameQuestionModel implements Serializable {
	private String gameIcon;
	private String gameName;
	private int gameId;

	private QuestionModel questionModel;

	public String getGameIcon() {
		return gameIcon;
	}

	public void setGameIcon(String gameIcon) {
		this.gameIcon = gameIcon;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public QuestionModel getQuestionModel() {
		return questionModel;
	}

	public void setQuestionModel(QuestionModel questionModel) {
		this.questionModel = questionModel;
	}

}
