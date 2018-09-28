package com.tenone.gamebox.mode.mode;

public class AboutForMeCommentModel extends  AboutForMePublicModel  {
    
    private String tUserId;
    
    private String tNick;
    
    private String cContent;

    public String gettUserId() {
        return tUserId;
    }

    public void settUserId(String tUserId) {
        this.tUserId = tUserId;
    }

    public String gettNick() {
        return tNick;
    }

    public void settNick(String tNick) {
        this.tNick = tNick;
    }

    public String getcContent() {
        return cContent;
    }

    public void setcContent(String cContent) {
        this.cContent = cContent;
    }
}
