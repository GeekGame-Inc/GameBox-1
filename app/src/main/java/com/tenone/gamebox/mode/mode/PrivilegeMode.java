/**
 * Project Name:GameBox
 * File Name:PrivilegeMode.java
 * Package Name:com.tenone.gamebox.mode.mode
 * Date:2017-4-13下午3:35:01
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

/**
 * ClassName:PrivilegeMode <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-13 下午3:35:01 <br/>
 *
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class PrivilegeMode implements Serializable {
    private static final long serialVersionUID = 693809480244320853L;
    // 活动id
    private int privilegeId;
    //活动图片
    private String privilegeImgUrl;

    // 活动名称
    private String privilegeName;
    // 活动时间段
    private String privilegeTime;
    // 活动简介
    private String privilegeIntro;
    //详情地址
    private String detailsUrl;

    public String getPrivilegeImgUrl() {
        return privilegeImgUrl;
    }

    public void setPrivilegeImgUrl(String privilegeImgUrl) {
        this.privilegeImgUrl = privilegeImgUrl;
    }

    public int getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(int privilegeId) {
        this.privilegeId = privilegeId;
    }

    public String getPrivilegeName() {
        return privilegeName;
    }

    public void setPrivilegeName(String privilegeName) {
        this.privilegeName = privilegeName;
    }

    public String getPrivilegeTime() {
        return privilegeTime;
    }

    public void setPrivilegeTime(String privilegeTime) {
        this.privilegeTime = privilegeTime;
    }

    public String getPrivilegeIntro() {
        return privilegeIntro;
    }

    public void setPrivilegeIntro(String privilegeIntro) {
        this.privilegeIntro = privilegeIntro;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

}
