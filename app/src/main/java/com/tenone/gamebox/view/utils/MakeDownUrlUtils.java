/** 
 * Project Name:GameBox 
 * File Name:MakeDownUrlUtils.java 
 * Package Name:com.tenone.gamebox.view.utils 
 * Date:2017-4-14上午11:27:33 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.view.utils;

import com.tenone.gamebox.mode.mode.AppConfigModle;
import com.tenone.gamebox.view.base.MyApplication;


/**
 * 
 * 根据规则生成下载地址工具类 ClassName:MakeDownUrlUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-4-14 上午11:27:33 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class MakeDownUrlUtils {
	// 下载地址
	static String downUrl;

	public static String makeDownUrl(String abbreviation, String versions) {
		AppConfigModle configModle = MyApplication.getConfigModle();
		// 生成下载地址(规则:域名+渠道id+游戏简称+渠道id+版本号)
		downUrl = configModle.getDownDomainName() + configModle.getChannelID()
				+ "/" + abbreviation + "_" + configModle.getChannelID() + "_"
				+ versions + ".apk";
		return downUrl;
	}
}
