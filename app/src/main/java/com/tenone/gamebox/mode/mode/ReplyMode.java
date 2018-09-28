/** 
 * Project Name:GameBox 
 * File Name:ReplyMode.java 
 * Package Name:com.tenone.gamebox.mode.mode 
 * Date:2017-3-22����5:27:45 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.mode;

import java.io.Serializable;
import java.util.List;

/**
 * ��������ģ�� ClassName:ReplyMode <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-22 ����5:27:45 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class ReplyMode implements Serializable {
	private static final long serialVersionUID = -1787848512302949626L;
	/* ��Ϣid */
	private long msgId;
	/* �û�id */
	private long uId;
	/* �û��ǳ� */
	private String nick;
	/* ��Ϣ���� */
	private String content;
	/* ��ϢͼƬ��ַ */
	private String imgUrl;

	/* ���е�����Ϣ(���û��,�Ǳ����������Ϣ,����б�����Ǹ���Ϣ) */
	private List<ReplyMode> replyMode;

	/* ��Ϣ��id(����������Ϣ�Ż���) */
	private long groupId;

	/* ������ǳ� */
	private String replyNick;
	/*�����uid*/
	private long replyUId;

	/*ʱ��*/
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
