package com.tenone.gamebox.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.ModificationPwdBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.ModificationPwdView;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.FileUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.SpUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class ModificationPwdPresenter extends BasePresenter implements
		OnClickListener, HttpResultListener {
	ModificationPwdBiz modificationPwdBiz;
	ModificationPwdView modificationPwdView;
	Context mContext;

	public ModificationPwdPresenter(ModificationPwdView v, Context cxt) {
		this.modificationPwdBiz = new ModificationPwdBiz();
		this.mContext = cxt;
		this.modificationPwdView = v;
	}

	public void initView() {
		getTitleBarView().setLeftImg(R.drawable.icon_back_grey);
		if (isShowOldPwdView()) {
			getOldPwdEditText().setVisibility(View.GONE);
			getTitleBarView().setTitleText(R.string.findPwd);
		} else {
			getOldPwdEditText().setVisibility(View.VISIBLE);
			getTitleBarView().setTitleText(R.string.changePwd);
		}
	}

	public void initListener() {
		getTitleBarView().getLeftImg().setOnClickListener(this);
		getButton().setOnClickListener(this);
	}

	public TitleBarView getTitleBarView() {
		return modificationPwdView.getTitleBarView();
	}

	public CustomizeEditText getOldPwdEditText() {
		return modificationPwdView.getOldPwdEditText();
	}

	public CustomizeEditText getNewPwdEditText() {
		return modificationPwdView.getNewPwdEditText();
	}

	public CustomizeEditText getConfirmPwdEditText() {
		return modificationPwdView.getConfirmPwdEditText();
	}

	public Button getButton() {
		return modificationPwdView.getButton();
	}

	public String getOldPwd() {
		return modificationPwdBiz.getOldPwd();
	}

	public Intent getIntent() {
		return modificationPwdView.getIntent();
	}

	public boolean isShowOldPwdView() {
		return modificationPwdBiz.isShowOldPwdView(getIntent());
	}

	public void modifyPassword(int what, String oldPwd, String newPwd,
			String repassword) {
		HttpManager.modifyPassword(what, mContext, this, oldPwd, newPwd);
	}

	public void forgetPassword(int what, String newPwd, String repassword,
			String token) {
		HttpManager.forgetPassword(what, mContext, this, token, newPwd);
	}

	public boolean verificationModification() {
		return modificationPwdBiz.verificationModification(mContext,
				getOldPwdEditText(), getOldPwd(), getNewPwdEditText(),
				getConfirmPwdEditText(), getIntent().getAction());
	}

	public boolean verificationForget() {
		return modificationPwdBiz.verificationForget(mContext,
				getNewPwdEditText(), getConfirmPwdEditText());
	}

	AlertDialog alertDialog;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_titleBar_leftImg:
			close(mContext);
			break;
		case R.id.id_modification_modificationBt:
			String old = getOldPwdEditText().getText().toString();
			String newPwd = getNewPwdEditText().getText().toString();
			String confirmPwd = getConfirmPwdEditText().getText().toString();
			if (isShowOldPwdView()) {
				if (verificationForget()) {
					String token = getIntent().getExtras().getString("token");
					if (!TextUtils.isEmpty(token)) {
						alertDialog = buildProgressDialog(mContext);
						forgetPassword(HttpType.LOADING, newPwd, confirmPwd,
								token);
					} else {
						showToast(mContext, "token\u4e3a\u7a7a" );
					}
				}
			} else {
				if (verificationModification()) {
					alertDialog = buildProgressDialog(mContext);
					modifyPassword(HttpType.REFRESH, old, newPwd, confirmPwd);
				}
			}
			break;
		}
	}

	@Override
	public void onSuccess(final int what, final ResultItem resultItem) {
		cancelProgressDialog(alertDialog);
		if ("1".equals(resultItem.getString("status"))) {
			new Thread() {
				public void run() {
					String pwd = getNewPwdEditText().getText().toString();
					SpUtil.setPwd(pwd);
                    JSONObject object = new JSONObject();
                    try {
                        object.put( "userName", SpUtil.getAccount() );
                        object.put( "pwd", pwd );
                        object.put( "phone", SpUtil.getPhone() );
                        object.put( "uid", SpUtil.getUserId() );
                        FileUtils
                                .saveAccountNew( mContext, object.toString() );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
					Message message = new Message();
					message.what = what;
					message.obj = resultItem;
					handler.sendMessage(message);
				}
			}.start();
		} else {
			showToast(mContext, resultItem.getString("msg"));
		}
	}

	@Override
	public void onError(int what, String error) {
		cancelProgressDialog(alertDialog);
		showToast(mContext, error);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			((BaseActivity) mContext).setResult(Activity.RESULT_OK);
			close(mContext);
		}
	};
}
