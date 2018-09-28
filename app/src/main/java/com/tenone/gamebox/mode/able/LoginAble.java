
package com.tenone.gamebox.mode.able;

import android.content.Context;

import com.tenone.gamebox.view.custom.CustomizeEditText;

public interface LoginAble {
	boolean verification(Context cxt, CustomizeEditText account, CustomizeEditText pwd);
	
}
