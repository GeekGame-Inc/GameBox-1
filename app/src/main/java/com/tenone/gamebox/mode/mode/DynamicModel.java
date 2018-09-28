package com.tenone.gamebox.mode.mode;

import android.text.TextUtils;

import com.tenone.gamebox.view.utils.TimeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class DynamicModel implements Serializable {
    private static final long serialVersionUID = -8154541022851669846L;
    
    private String dynamicModelId;
    
    private DriverModel model;
    
    private String time;

    
    private String content;
    
    private String likes;
    
    private String dislikes;
    
    private String comments;
    
    private String shares;
    
    private ArrayList<String> dynamicImg = new ArrayList<String>();
    
    private ArrayList<DynamicCommentModel> commentModels = new ArrayList<DynamicCommentModel>();
    
    private int status;
    
    private boolean isGood, isDisGood;
    
    private int leavel = 0;

    private boolean isTop = false;
    
    private String publishTime;
    
    private String remark;

    public String getDynamicModelId() {
        return dynamicModelId;
    }

    public void setDynamicModelId(String dynamicModelId) {
        this.dynamicModelId = dynamicModelId;
    }

    public DriverModel getModel() {
        return model;
    }

    public void setModel(DriverModel model) {
        this.model = model;
    }

    public String getTime() {
        if (!TextUtils.isEmpty( time )) {
            String t = "";
            try {
                long t1 = Long.valueOf( time );
                t = TimeUtils.formatDateMin( t1 * 1000 );
                return t;
            } catch (NumberFormatException e) {
            }
        }
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getDislikes() {
        return dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ArrayList<String> getDynamicImg() {
        return dynamicImg;
    }

    public void setDynamicImg(ArrayList<String> dynamicImg) {
        this.dynamicImg = dynamicImg;
    }

    public List<DynamicCommentModel> getCommentModels() {
        return commentModels;
    }

    public void setCommentModels(ArrayList<DynamicCommentModel> commentModels) {
        this.commentModels = commentModels;
    }

    public boolean isGood() {
        return isGood;
    }

    public void setGood(boolean good) {
        isGood = good;
    }

    public boolean isDisGood() {
        return isDisGood;
    }

    public void setDisGood(boolean disGood) {
        isDisGood = disGood;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLeavel() {
        return leavel;
    }

    public void setLeavel(int leavel) {
        this.leavel = leavel;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public String getPublishTime() {
        if (!TextUtils.isEmpty( publishTime )) {
            String t = "";
            try {
                long t1 = Long.valueOf( publishTime );
                t = TimeUtils.formatDateMin( t1 * 1000 );
                return t;
            } catch (NumberFormatException e) {
                return "";
            }
        }
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
