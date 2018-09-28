package com.tenone.gamebox.mode.mode;

import android.text.TextUtils;

import com.tenone.gamebox.view.utils.TimeUtils;

import java.io.Serializable;
import java.util.List;

public class QuestionModel implements Serializable {
	private String questionId;
	private String question;
	private int reward = 0;
	private int type, num, coin;
	private List<AnswerModel> answers;
	private String time;
	private UserInfoModel userInfoModel;
	private boolean coinsIsReceived;

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<AnswerModel> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswerModel> answers) {
		this.answers = answers;
	}

	public String getTime() {
		if (!TextUtils.isEmpty( time )) {
			try {
				long t = Long.valueOf( time ).longValue() * 1000;
				return TimeUtils.formatData( t, "yy-MM-dd" );
			} catch (NumberFormatException e) {
				return time;
			}
		}
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public UserInfoModel getUserInfoModel() {
		return userInfoModel;
	}

	public void setUserInfoModel(UserInfoModel userInfoModel) {
		this.userInfoModel = userInfoModel;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public boolean isCoinsIsReceived() {
		return coinsIsReceived;
	}

	public void setCoinsIsReceived(boolean coinsIsReceived) {
		this.coinsIsReceived = coinsIsReceived;
	}
}
