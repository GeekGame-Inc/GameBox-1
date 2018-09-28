package com.tenone.gamebox.mode.mode;

import java.io.Serializable;

public class RebateHelp implements Serializable {
	
	private static final long serialVersionUID = -9179680935740334599L;

	private String title;
	
	private String arrays;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArrays() {
		return arrays;
	}

	public void setArrays(String arrays) {
		this.arrays = arrays;
	}
}
