package com.tenone.gamebox.mode.mode;

public enum OnScrollState {

	SCROLL_NO( 0x000 ), SCROLL_UP( 0x001 ), SCROLL_DOWN( 0x002 );

	private int value = 0;

	OnScrollState(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
