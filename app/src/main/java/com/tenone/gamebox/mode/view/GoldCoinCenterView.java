package com.tenone.gamebox.mode.view;

import android.widget.Button;
import android.widget.TextView;

import com.tenone.gamebox.view.custom.TitleBarView;

public interface GoldCoinCenterView {
	TitleBarView titleBarView();

	TextView banlanceTv();

	TextView todayTv();

	TextView tomonthTv();

	Button luckyBt();

	Button changeBt();
}
