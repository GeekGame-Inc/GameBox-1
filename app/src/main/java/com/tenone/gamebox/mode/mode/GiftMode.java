/**
 * Project Name:GameBox
 * File Name:GiftMode.java
 * Package Name:com.tenone.gamebox.mode.mode
 * Date:2017-3-10下午3:47:18
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

/**
 * 礼包数据模型 ClassName:GiftMode <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-10 下午3:47:18 <br/>
 *
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class GiftMode implements Serializable {

	private static final long serialVersionUID = 1851642129458176623L;
	/* 礼包图片地址 */
	private String giftImgUrl;
	/* 礼包名字 */
	private String giftName;
	/* 礼包码 */
	private String giftCode;
	/* 礼包总数 */
	private long giftCounts = 100;

	/* 礼包状态 0，待领取1,已经领取 */
	private int state = 0;
	/* 礼包剩余数量 */
	private int residue = 20;
	/* 礼包id */
	private int giftId;
	/*礼包内容*/
	private String giftContent;

	public String getGiftCode() {
		return giftCode;
	}

	public void setGiftCode(String giftCode) {
		this.giftCode = giftCode;
	}

	public int getGiftId() {
		return giftId;
	}

	public void setGiftId(int giftId) {
		this.giftId = giftId;
	}

	public String getGiftImgUrl() {
		return giftImgUrl;
	}

	public void setGiftImgUrl(String giftImgUrl) {
		this.giftImgUrl = giftImgUrl;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public long getGiftCounts() {
		return giftCounts;
	}

	public void setGiftCounts(long counts) {
		this.giftCounts = counts;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getResidue() {
		return residue;
	}

	public void setResidue(int residue) {
		this.residue = residue;
	}

	public String getGiftContent() {
		return giftContent;
	}

	public void setGiftContent(String giftContent) {
		this.giftContent = giftContent;
	}
}
