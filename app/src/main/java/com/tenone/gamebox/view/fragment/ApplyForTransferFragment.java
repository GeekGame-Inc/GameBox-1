package com.tenone.gamebox.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.FragmentChangeListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;


public class ApplyForTransferFragment extends Fragment implements
		HttpResultListener {
	@ViewInject(R.id.id_apply_for_transfer_oldGameName)
	CustomizeEditText oldNameEt;
	@ViewInject(R.id.id_apply_for_transfer_newGameName)
	CustomizeEditText newNameEt;
	@ViewInject(R.id.id_apply_for_transfer_oldServer)
	CustomizeEditText oldServerEt;
	@ViewInject(R.id.id_apply_for_transfer_newServer)
	CustomizeEditText newServerEt;
	@ViewInject(R.id.id_apply_for_transfer_oldRoleName)
	CustomizeEditText oldRoleNameEt;
	@ViewInject(R.id.id_apply_for_transfer_newRoleName)
	CustomizeEditText newRoleNameEt;

	@ViewInject(R.id.id_transfer_phoneEt)
	CustomizeEditText phoneEt;
	@ViewInject(R.id.id_transfer_qqEt)
	CustomizeEditText qqEt;

	@ViewInject(R.id.id_transfer_confirmBt)
	Button confirmBt;

	private Context mContext;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_apply_for_transfer,
				container, false);
		ViewUtils.inject(this, view);
		mContext = getActivity();
		return view;
	}

	private void requset(String origin_appname, String origin_servername,
			String origin_rolename, String new_appname, String new_servername,
			String new_rolename, String qq, String mobile) {
		HttpManager.changegame(HttpType.REFRESH, mContext, this,
				origin_appname, origin_servername, origin_rolename,
				new_appname, new_servername, new_rolename, qq, mobile);
	}

	@OnClick({ R.id.id_transfer_confirmBt })
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.id_transfer_confirmBt:
			String origin_appname = oldNameEt.getText().toString();
			String origin_servername = oldServerEt.getText().toString();
			String origin_rolename = oldRoleNameEt.getText().toString();
			String new_appname = newNameEt.getText().toString();
			String new_servername = newServerEt.getText().toString();
			String new_rolename = newRoleNameEt.getText().toString();
			String qq = qqEt.getText().toString();
			String phone = phoneEt.getText().toString();
			if (TextUtils.isEmpty(origin_appname)) {
				ToastCustom.makeText(mContext, "\u8bf7\u8f93\u5165\u65e7\u6e38\u620f\u540d\u5b57",
						ToastCustom.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(origin_servername)) {
				ToastCustom.makeText(mContext, "\u8bf7\u8f93\u5165\u65e7\u6e38\u620f\u533a\u670d",
						ToastCustom.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(origin_rolename)) {
				ToastCustom.makeText(mContext, "\u8bf7\u8f93\u5165\u65e7\u6e38\u620f\u89d2\u8272\u540d",
						ToastCustom.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(new_appname)) {
				ToastCustom.makeText(mContext, "\u8bf7\u8f93\u5165\u65b0\u6e38\u620f\u540d\u5b57",
						ToastCustom.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(new_servername)) {
				ToastCustom.makeText(mContext, "\u8bf7\u8f93\u5165\u65b0\u6e38\u620f\u533a\u670d",
						ToastCustom.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(new_rolename)) {
				ToastCustom.makeText(mContext, "\u8bf7\u8f93\u5165\u65b0\u6e38\u620f\u89d2\u8272\u540d",
						ToastCustom.LENGTH_SHORT).show();
				return;
			}

			if (TextUtils.isEmpty(qq) && TextUtils.isEmpty(phone)) {
				ToastCustom.makeText(mContext, "\u8bf7\u81f3\u5c11\u8f93\u5165\u4e00\u79cd\u8054\u7cfb\u65b9\u5f0f",
						ToastCustom.LENGTH_SHORT).show();
				return;
			}
			requset(origin_appname, origin_servername, origin_rolename,
					new_appname, new_servername, new_rolename, qq, phone);
			break;
		}
	}

	private void reset() {
		oldNameEt.setText("");
		oldRoleNameEt.setText("");
		oldServerEt.setText("");
		newNameEt.setText("");
		newRoleNameEt.setText("");
		newServerEt.setText("");
		phoneEt.setText("");
		qqEt.setText("");
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		ToastCustom.makeText(mContext, resultItem.getString("msg"),
				ToastCustom.LENGTH_SHORT).show();
		if (1 == resultItem.getIntValue("status")) {
			ListenerManager
					.sendFragmentChange(FragmentChangeListener.FRAGMENTTWO);
			reset();
		}
	}

	@Override
	public void onError(int what, String error) {
		ToastCustom.makeText(mContext, error, ToastCustom.LENGTH_SHORT).show();
	}
}
