/** 
 * Project Name:GameBox 
 * File Name:ReplyMode.java 
 * Package Name:com.tenone.gamebox.mode.mode 
 * Date:2017-3-22下午5:27:45 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.mode;

import java.io.Serializable;
import java.util.List;

/**
 * 评论数据模型 ClassName:ReplyMode <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-22 下午5:27:45 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class ReplyMode implements Serializable {
	private static final long serialVersionUID = -1787848512302949626L;
	/* 消息id */
	private long msgId;
	/* 用户id */
	private long uId;
	/* 用户昵称 */
	private String nick;
	/* 信息内容 */
	private String content;
	/* 信息图片地址 */
	private String imgUrl;

	/* 所有的子信息(如果没有,那本身就是子消息,如果有本身就是父消息) */
	private List<ReplyMode> replyMode;

	/* 消息组id(本身是子消息才会有) */
	private long groupId;

	/* 对象的昵称 */
	private String replyNick;
	/*对象的uid*/
	private long replyUId;

	/*时间*/
	private long time;
	
	
	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public List<ReplyMode> getReplyMode() {
		return replyMode;
	}

	public void setReplyMode(List<ReplyMode> replyMode) {
		this.replyMode = replyMode;
	}

	public long getMessageId() {
		return msgId;
	}

	public void setMessageId(long msgId) {
		this.msgId = msgId;
	}

	public long getuId() {
		return uId;
	}

	public void setuId(long uId) {
		this.uId = uId;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getReplyNick() {
		return replyNick;
	}

	public void setReplyNick(String replyNick) {
		this.replyNick = replyNick;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getReplyUId() {
		return replyUId;
	}

	public void setReplyUId(long replyUId) {
		this.replyUId = replyUId;
	}
}
