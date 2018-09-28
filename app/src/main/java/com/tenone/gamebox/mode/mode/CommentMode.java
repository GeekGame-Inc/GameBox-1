

package com.tenone.gamebox.mode.mode;

import android.text.TextUtils;

import com.tenone.gamebox.view.utils.TimeUtils;

import java.io.Serializable;


public class CommentMode implements Serializable {

    private static final long serialVersionUID = -5880171193814438768L;
    
    private String commentId;
    
    private String comment;
    
    private String commentTime;
    
    private String commentImgUrl;
    
    private String replyNum = "0";
    
    private String commentName = "";
    
    private String commentUserId;
    
    private String replyNick;
    
    private String replyId;

    private String topId;
    
    private int isFake;
    
    private String dynamicsId;
    
    private String commentType;


    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentTime() {
        if (!TextUtils.isEmpty( commentTime )) {
            try {
                long t = Long.valueOf( commentTime ).longValue()*1000;
                return TimeUtils.formatDateSec( t );
            } catch (NumberFormatException e) {
                return commentTime;
            }
        }
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentImgUrl() {
        return commentImgUrl;
    }

    public void setCommentImgUrl(String commentImgUrl) {
        this.commentImgUrl = commentImgUrl;
    }

    public String getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }

    public String getCommentName() {
        return commentName;
    }

    public void setCommentName(String commentName) {
        this.commentName = commentName;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getTopId() {
        return topId;
    }

    public void setTopId(String topId) {
        this.topId = topId;
    }

    public String getreplyNick() {
        return replyNick;
    }

    public void setreplyNick(String replyNick) {
        this.replyNick = replyNick;
    }

    public int getIsFake() {
        return isFake;
    }

    public void setIsFake(int isFake) {
        this.isFake = isFake;
    }

    public String getDynamicsId() {
        return dynamicsId;
    }

    public void setDynamicsId(String dynamicsId) {
        this.dynamicsId = dynamicsId;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }
}
