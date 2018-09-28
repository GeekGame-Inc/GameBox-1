package com.tenone.gamebox.mode.mode;


import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class ReservedAlarmModel extends RealmObject {
    @PrimaryKey
    private int gameId;// ��Ϸid
    @Index
    private String gameName;// ��Ϸ����
    private String imgUrl;// ��ϷͼƬ��ַ
    @Required
    private String time;// ����ʱ��

    public ReservedAlarmModel(String gameName, int gameId, String imgUrl, String time) {
        this.gameName = gameName;
        this.gameId = gameId;
        this.imgUrl = imgUrl;
        this.time = time;
    }

    public ReservedAlarmModel() {
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
