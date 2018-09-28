package com.tenone.gamebox.mode.mode;

import java.io.Serializable;


public class TopModel implements Serializable {
    private static final long serialVersionUID = -15461257332129596L;
    private String uid;
    private String userName;
    private String count;
    private String icon;
    private int isVip;
    private int sex;
    private String ranking;
    private String coin;
    private int isGot;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        if (userName.length() > 5) {
            return userName = userName.substring( 0, 4 ) + "***";
        }else {
            return userName+"***";
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int isVip() {
        return isVip;
    }

    public void setVip(int vip) {
        isVip = vip;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public int isGot() {
        return isGot;
    }

    public void setGot(int got) {
        isGot = got;
    }
}
