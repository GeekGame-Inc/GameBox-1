package com.tenone.gamebox.mode.mode;

import android.text.TextUtils;

import com.tenone.gamebox.view.utils.TimeUtils;

import java.io.Serializable;



public class AboutForMePublicModel implements Serializable {

    private static final long serialVersionUID = 4034042690894428025L;
    
    private String dyId;
    
    private String dyUId;
    
    private String dyUNick;
    
    private String dyContent;
    
    private String dyImg;

    
    private String cId;
    
    private String cUId;
    
    private String cUHedaer;
    
    private boolean cUIsVip;
    
    private String cUNick;
    
    private String time;

    
    private int type;

    public String getDyId() {
        return dyId;
    }

    public void setDyId(String dyId) {
        this.dyId = dyId;
    }

    public String getDyUId() {
        return dyUId;
    }

    public void setDyUId(String dyUId) {
        this.dyUId = dyUId;
    }

    public String getDyUNick() {
        return dyUNick;
    }

    public void setDyUNick(String dyUNick) {
        this.dyUNick = dyUNick;
    }

    public String getDyContent() {
        return dyContent;
    }

    public void setDyContent(String dyContent) {
        this.dyContent = dyContent;
    }

    public String getDyImg() {
        return dyImg;
    }

    public void setDyImg(String dyImg) {
        this.dyImg = dyImg;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcUId() {
        return cUId;
    }

    public void setcUId(String cUId) {
        this.cUId = cUId;
    }

    public String getcUHedaer() {
        return cUHedaer;
    }

    public void setcUHedaer(String cUHedaer) {
        this.cUHedaer = cUHedaer;
    }

    public boolean iscUIsVip() {
        return cUIsVip;
    }

    public void setcUIsVip(boolean cUIsVip) {
        this.cUIsVip = cUIsVip;
    }

    public String getcUNick() {
        return cUNick;
    }

    public void setcUNick(String cUNick) {
        this.cUNick = cUNick;
    }

    public String getTime() {
        if (!TextUtils.isEmpty( time )) {
            try {
                long a = Long.valueOf( time );
                return TimeUtils.formatDateMin( a * 1000 );
            } catch (NumberFormatException e) {
                return "1970-01-01";
            }
        } else{
            return "1970-01-01";
        }
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
}
