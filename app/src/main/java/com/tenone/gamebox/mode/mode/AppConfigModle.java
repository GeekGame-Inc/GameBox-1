

package com.tenone.gamebox.mode.mode;

import java.io.Serializable;


public class AppConfigModle implements Serializable {
	
	private static final long serialVersionUID = -1914753009081547911L;
	
	String channelID;
	
	String downDomainName;

	public String getChannelID() {
		return channelID;
	}

	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}

	public String getDownDomainName() {
		return downDomainName;
	}

	public void setDownDomainName(String downDomainName) {
		this.downDomainName = downDomainName;
	}
}
