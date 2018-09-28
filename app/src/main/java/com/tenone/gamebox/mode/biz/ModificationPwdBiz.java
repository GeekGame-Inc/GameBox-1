package com.tenone.gamebox.mode.biz;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.able.ModificationPwdAble;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.utils.EditTextErrorUtils;
import com.tenone.gamebox.view.utils.SpUtil;

public class ModificationPwdBiz implements ModificationPwdAble {

	@Override
	public String getOldPwd() {
		return SpUtil.getPwd();
	}

	@Override
	public boolean isShowOldPwdView(Intent intent) {
		return "find".equals(intent.getAction());
	}

	@Override
	public boolean verificationModification(Context context,
			CustomizeEditText oldPwdEdit, String oldPwd,
			CustomizeEditText newPwdEdit, CustomizeEditText confirmEdit,String action) {
		if (!"find".equals(action)) {
			if (TextUtils.isEmpty(oldPwdEdit.getText().toString())) {
				EditTextErrorUtils.setError(context, oldPwdEdit,
						R.string.inputOldPwdHint);
				return false;
			}
			
			if (!oldPwdEdit.getText().toString().equals(oldPwd)) {
				EditTextErrorUtils.setError(context, oldPwdEdit,
						R.string.inputRightOldPwdHint);
				return false;
			}
		}
		
		if (TextUtils.isEmpty(newPwdEdit.getText().toString())) {
			EditTextErrorUtils.setError(context, newPwdEdit,
					R.string.inputNewPwdHint);
			return false;
		}

		if (TextUtils.isEmpty(confirmEdit.getText().toString())) {
			EditTextErrorUtils.setError(context, newPwdEdit,
					R.string.inputConfirmPwdHint);
			return false;
		}

		if (!confirmEdit.getText().toString()
				.equals(newPwdEdit.getText().toString())) {
			Toast.makeText(context, "\u4e24\u6b21\u8f93\u5165\u7684\u5bc6\u7801\u4e0d\u4e00\u6837", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	@Override
	public boolean verificationForget(Context context,
			CustomizeEditText newPwdEdit, CustomizeEditText confirmEdit) {
		if (TextUtils.isEmpty(newPwdEdit.getText().toString())) {
			EditTextErrorUtils.setError(context, newPwdEdit,
					R.string.inputNewPwdHint);
			return false;
		}

		if (TextUtils.isEmpty(confirmEdit.getText().toString())) {
			EditTextErrorUtils.setError(context, newPwdEdit,
					R.string.inputConfirmPwdHint);
			return false;
		}
		
		if (!confirmEdit.getText().toString()
				.equals(newPwdEdit.getText().toString())) {
			Toast.makeText(context, "\u4e24\u6b21\u8f93\u5165\u7684\u5bc6\u7801\u4e0d\u4e00\u6837", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

}
