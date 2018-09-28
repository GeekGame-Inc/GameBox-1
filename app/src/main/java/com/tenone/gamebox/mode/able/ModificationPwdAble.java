
package com.tenone.gamebox.mode.able;

import android.content.Context;
import android.content.Intent;

import com.tenone.gamebox.view.custom.CustomizeEditText;
public interface ModificationPwdAble {
	String getOldPwd();

	boolean isShowOldPwdView(Intent intent);

	boolean verificationModification(Context context,
																	 CustomizeEditText oldPwdEdit, String oldPwd,
																	 CustomizeEditText newPwdEdit, CustomizeEditText confirmEdit, String action);

	boolean verificationForget(Context context, CustomizeEditText newPwdEdit,
														 CustomizeEditText confirmEdit);
	
}
