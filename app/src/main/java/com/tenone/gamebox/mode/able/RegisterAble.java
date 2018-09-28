
package com.tenone.gamebox.mode.able;

import android.content.Context;
import android.widget.TextView;

import com.tenone.gamebox.mode.mode.RegisterMode;
import com.tenone.gamebox.view.custom.CustomizeEditText;

public interface RegisterAble {

	boolean verification(Context cxt, CustomizeEditText account,
											 CustomizeEditText password, CustomizeEditText phone,
											 CustomizeEditText code, String codeText, String type);

	RegisterMode getRegisterMode(CustomizeEditText account,
															 CustomizeEditText password, CustomizeEditText phone,
															 CustomizeEditText code);

	void changeButton(Context cxt, TextView button, CustomizeEditText phone);
	
}
