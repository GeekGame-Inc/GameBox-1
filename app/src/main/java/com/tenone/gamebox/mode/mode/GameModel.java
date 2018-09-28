package com.tenone.gamebox.mode.mode;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;


public class GameModel extends Observable implements Serializable {
	private static final long serialVersionUID = 5696296789519594849L;
	
	private int gameId = 0;

	
	private String name = "";

	
	private String versionsName = "0";
	
	private int versionsCode = 0;
	
	private String imgUrl;
	
	private String url;
	
	private String packgeName;
	
	private String apkName;
	
	private int progress = 0;
	
	private float grade = 5;

	
	private String times;

	
	private String size;
	
	private String time = "";

	
	private int status = 0;
	
	private int type = 0;
	
	private boolean isChecked = false;
	
	private String installPath;
	
	private String[] labelArray;
	
	boolean isCollectde = false;
	
	private String abbreviation = "";

	
	private String gameTag = "";

	private String content = "";
	
	private String dis = "";

	private boolean isToday = false;

	private Drawable iconDrawable = null;

	private int platform = 1;
	
	private boolean isReserved = false;
	
	private String gameType;

	private ArrayList<String> platforms;

	private int operate;

	
	private int players = 0;
	
	private int questions = 0;
	
	private int answers = 0;

	public boolean isCollectde() {
		return isCollectde;
	}

	public void setCollectde(boolean isCollectde) {
		this.isCollectde = isCollectde;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getInstallPath() {
		return installPath;
	}

	public void setInstallPath(String installPath) {
		this.installPath = installPath;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getGrade() {
		return grade;
	}

	public void setGrade(float grade) {
		this.grade = grade;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPackgeName() {
		return packgeName;
	}

	public void setPackgeName(String packgeName) {
		this.packgeName = packgeName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public String[] getLabelArray() {
		return labelArray;
	}

	public void setLabelArray(String[] labelArray) {
		this.labelArray = labelArray;
	}

	@Override
	public boolean equals(Object o) {
		try {
			
			if (this == o)
				return true;
			
			if (!(o instanceof GameModel))
				return false;
			GameModel gameModel = (GameModel) o;
			
			if (gameId != gameModel.gameId)
				return false;

			return url.equals( gameModel.getUrl() );
		} catch (NullPointerException e) {
			return false;
		}
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getVersionsName() {
		return versionsName;
	}

	public void setVersionsName(String versionsName) {
		this.versionsName = versionsName;
	}

	public String getGameTag() {
		return gameTag;
	}

	public void setGameTag(String gameTag) {
		this.gameTag = gameTag;
	}

	@Override
	public int hashCode() {
		return gameId ^ (gameId >>> 32);
	}

	public Drawable getIconDrawable() {
		return iconDrawable;
	}

	public void setIconDrawable(Drawable iconDrawable) {
		this.iconDrawable = iconDrawable;
	}

	public int getVersionsCode() {
		return versionsCode;
	}

	public void setVersionsCode(int versionsCode) {
		this.versionsCode = versionsCode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDis() {
		return dis;
	}

	public void setDis(String dis) {
		this.dis = dis;
	}

	public boolean isToday() {
		return isToday;
	}

	public void setToday(boolean today) {
		isToday = today;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public ArrayList<String> getPlatforms() {
		return platforms;
	}

	public void setPlatforms(ArrayList<String> platforms) {
		this.platforms = platforms;
	}

	public boolean isReserved() {
		return isReserved;
	}

	public void setReserved(boolean reserved) {
		isReserved = reserved;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public int getOperate() {
		return operate;
	}

	public void setOperate(int operate) {
		this.operate = operate;
	}


	public int getPlayers() {
		return players;
	}

	public void setPlayers(int players) {
		this.players = players;
	}

	public int getQuestions() {
		return questions;
	}

	public void setQuestions(int questions) {
		this.questions = questions;
	}

	public int getAnswers() {
		return answers;
	}

	public void setAnswers(int answers) {
		this.answers = answers;
	}
}
