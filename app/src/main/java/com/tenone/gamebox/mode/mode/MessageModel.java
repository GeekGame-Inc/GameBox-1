package com.tenone.gamebox.mode.mode;

import android.text.TextUtils;

import com.tenone.gamebox.view.utils.TimeUtils;

import java.io.Serializable;


public class MessageModel implements Serializable {
    private static final long serialVersionUID = -23483074724544014L;
    private int id;
    private String time;
    private String title;
    private String content;
    private String url;
    private int action = -1;
    private boolean isRead;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeStr() {
        if (TextUtils.isEmpty( time ))
            return "";
        try {
            String t = TimeUtils.formatDateSec( Long.valueOf( time ).longValue() * 1000 );
            return t;
        } catch (NumberFormatException e) {
            return "";
        }
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
