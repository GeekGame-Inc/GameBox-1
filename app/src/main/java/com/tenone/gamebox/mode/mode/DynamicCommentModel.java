package com.tenone.gamebox.mode.mode;

import android.text.TextUtils;

import com.tenone.gamebox.view.utils.TimeUtils;

import java.io.Serializable;



public class DynamicCommentModel implements Serializable {
    private static final long serialVersionUID = 8381225362341146822L;
    
    private String commentId;
    
    private String dynamicId;
    
    private String commentLikes;
    
    private String commentDislike;
    
    private String commentTime;
    
    private String toUserId;
    
    private String toUserNick;
    
    private String toUserHeader;
    
    private boolean toUserIsVip;
    
    private String commentContent;
    
    private DriverModel driverModel;
    
    private int likeTy;
    
    private int isFake;
    
    private int order;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getCommentLikes() {
        return commentLikes;
    }

    public void setCommentLikes(String commentLikes) {
        this.commentLikes = commentLikes;
    }

    public String getCommentDislike() {
        return commentDislike;
    }

    public void setCommentDislike(String commentDislike) {
        this.commentDislike = commentDislike;
    }

    public String getCommentTime() {
        if (!TextUtils.isEmpty( commentTime )) {
            try {
                long time = Long.valueOf( commentTime );
                String str = TimeUtils.formatDateMin( time * 1000 );
                return str;
            } catch (NumberFormatException e) {
                return commentTime;
            }
        }
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserNick() {
        return toUserNick;
    }

    public void setToUserNick(String toUserNick) {
        this.toUserNick = toUserNick;
    }

    public String getToUserHeader() {
        return toUserHeader;
    }

    public void setToUserHeader(String toUserHeader) {
        this.toUserHeader = toUserHeader;
    }

    public boolean isToUserIsVip() {
        return toUserIsVip;
    }

    public void setToUserIsVip(boolean toUserIsVip) {
        this.toUserIsVip = toUserIsVip;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public DriverModel getDriverModel() {
        return driverModel;
    }

    public void setDriverModel(DriverModel driverModel) {
        this.driverModel = driverModel;
    }

    public int getLikeTy() {
        return likeTy;
    }

    public void setLikeTy(int likeTy) {
        this.likeTy = likeTy;
    }

    public int getIsFake() {
        return isFake;
    }

    public void setIsFake(int isFake) {
        this.isFake = isFake;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
