/** 
 * Project Name:GameBox 
 * File Name:RegisterMode.java 
 * Package Name:com.tenone.gamebox.mode.mode 
 * Date:2017-3-15下午6:05:10 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

/**
 * 注册数据模型
 * 
 * ClassName:RegisterMode <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-15 下午6:05:10 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class RegisterMode implements Serializable {

	private static final long serialVersionUID = 6676637993785081673L;
	/* 账户 */
	String account;
	/* 密码 */
	String password;
	/* 电话 */
	String phone;
	/* 邮箱 */
	String mailBox;
	/* 验证码 */
	String code;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMailBox() {
		return mailBox;
	}

	public void setMailBox(String mailBox) {
		this.mailBox = mailBox;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
