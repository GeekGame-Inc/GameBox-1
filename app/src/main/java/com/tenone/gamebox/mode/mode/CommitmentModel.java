package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

public class CommitmentModel implements Serializable {
    private static final long serialVersionUID = 2072687841595360688L;
    private String title;
    private String content;
    private String qQ;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getqQ() {
        return qQ;
    }

    public void setqQ(String qQ) {
        this.qQ = qQ;
    }
}
