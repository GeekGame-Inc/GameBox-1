package com.tenone.gamebox.mode.mode;

public class AboutForMeZanCommenModel extends  AboutForMePublicModel {
    
    private String tUId;
    
    private String tUNick;
    
    private String cDyContent;
    
    private String cDyUId;
    
    private String cDyUNick;

    public String gettUId() {
        return tUId;
    }

    public void settUId(String tUId) {
        this.tUId = tUId;
    }

    public String gettUNick() {
        return tUNick;
    }

    public void settUNick(String tUNick) {
        this.tUNick = tUNick;
    }



    public String getcDyUId() {
        return cDyUId;
    }

    public void setcDyUId(String cDyUId) {
        this.cDyUId = cDyUId;
    }

    public String getcDyUNick() {
        return cDyUNick;
    }

    public void setcDyUNick(String cDyUNick) {
        this.cDyUNick = cDyUNick;
    }

    public String getcDyContent() {
        return cDyContent;
    }

    public void setcDyContent(String cDyContent) {
        this.cDyContent = cDyContent;
    }
}
