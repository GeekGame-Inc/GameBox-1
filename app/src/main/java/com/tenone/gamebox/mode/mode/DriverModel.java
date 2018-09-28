package com.tenone.gamebox.mode.mode;

import android.text.TextUtils;

import com.tenone.gamebox.view.utils.TimeUtils;

import java.io.Serializable;



public class DriverModel implements Serializable {
    private static final long serialVersionUID = -8040184331857605011L;
    
    private String header;
    
    private String backUrl;
    
    private String nick;
    
    private String driverId;
    
    private String intro;
    
    private String sex;
    
    private String score;
    
    private boolean isVip;
    
    private String userName;
    
    private String address;
    
    private String birth;
    
    private String email;
    
    private String qq;
    
    private String regTime;
    
    private int isAttention;
    
    private String fansNum;

    
    private String driverNum;

    
    private String attentionNum;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

	public String getSex() {
		String str = "\u672a\u8bbe\u7f6e";
		if (!TextUtils.isEmpty( sex ))
			switch (sex) {
				case "1":
					str = "\u7537";
					break;
				case "2":
					str = "\u5973";
					break;
				case "0":
					str = "\u672a\u8bbe\u7f6e";
					break;
			}
		return str;
	}

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getRegTime() {
        if (!TextUtils.isEmpty( regTime )) {
            try {
                return TimeUtils.formatDateMin( Long.valueOf( regTime ) * 1000 );
            } catch (NumberFormatException e) {
            }
        }
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public int isAttention() {
        return isAttention;
    }

    public void setAttention(int attention) {
        isAttention = attention;
    }

    public String getFansNum() {
        return fansNum;
    }

    public void setFansNum(String fansNum) {
        this.fansNum = fansNum;
    }

    public String getDriverNum() {
        return driverNum;
    }

    public void setDriverNum(String driverNum) {
        this.driverNum = driverNum;
    }

    public String getAttentionNum() {
        return attentionNum;
    }

    public void setAttentionNum(String attentionNum) {
        this.attentionNum = attentionNum;
    }
}
