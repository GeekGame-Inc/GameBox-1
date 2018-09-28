
package com.tenone.gamebox.mode.able;

import android.content.Context;
import android.widget.TextView;

import com.tenone.gamebox.view.custom.CustomizeEditText;

public interface FindPwdAble {

	boolean verification(Context cxt, CustomizeEditText phone);

	boolean verificationNext(Context context, CustomizeEditText phone,
													 CustomizeEditText code);
	void changeButton(Context cxt, TextView button, CustomizeEditText phone);
} 
