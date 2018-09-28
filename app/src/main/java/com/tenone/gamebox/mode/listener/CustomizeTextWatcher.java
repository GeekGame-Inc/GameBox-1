

package com.tenone.gamebox.mode.listener;

import android.text.TextWatcher;


public interface CustomizeTextWatcher extends TextWatcher {
	@Override
	 void beforeTextChanged(CharSequence s, int start, int count,
																int after);
}
