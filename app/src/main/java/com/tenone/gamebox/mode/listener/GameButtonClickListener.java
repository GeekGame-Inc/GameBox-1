

package com.tenone.gamebox.mode.listener;

import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.view.custom.DownloadProgressBar;


public interface GameButtonClickListener {
	void gameButtonClick(DownloadProgressBar progress, GameModel gameMode);
}
