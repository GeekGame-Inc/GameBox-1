

package com.tenone.gamebox.mode.listener;

import com.tenone.gamebox.view.custom.dialog.ModificationNickDialog;


public interface ModificationNickListener {
	
	void onConfirmClick(String nick);

	
	void onCancleClick(ModificationNickDialog dialog);
}
