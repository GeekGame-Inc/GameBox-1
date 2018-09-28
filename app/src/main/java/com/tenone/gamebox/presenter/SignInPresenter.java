package com.tenone.gamebox.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.SignInBiz;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.mode.view.SignInSignInView;
import com.tenone.gamebox.view.activity.CallCenterActivity;
import com.tenone.gamebox.view.custom.SignInView;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.TrackingUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class SignInPresenter extends BasePresenter implements OnClickListener,
		HttpResultListener {
	private Context context;
	private SignInBiz biz;
	private SignInSignInView view;
	private List<String> arrays = new ArrayList<String>();
	private List<String> items = new ArrayList<String>();

	public SignInPresenter(Context cxt, SignInSignInView v) {
		this.biz = new SignInBiz();
		this.context = cxt;
		this.view = v;
	}

	public void intView() {
		titleBarView().setTitleText("ǩ��");
		titleBarView().setLeftImg(R.drawable.icon_xqf_b);
		titleBarView().setRigthImg(R.drawable.icon_wen);
		titleBarView().getLeftImg().setOnClickListener(this);
		titleBarView().getRightImg().setOnClickListener(this);
		button().setOnClickListener(this);
		HttpManager.signInit(HttpType.REFRESH, context, this);
	}

	int current = -1;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_titleBar_leftImg:
			close(context);
			break;
		case R.id.id_signin_button:
			HttpManager.signIn(HttpType.LOADING, context, this);
			break;
		case R.id.id_titleBar_rightImg:
			openOtherActivity(context, new Intent(context,
					CallCenterActivity.class));
			break;
		}
	}

	private TitleBarView titleBarView() {
		return view.titleBarView();
	}

	private Button button() {
		return view.button();
	}

	private TextView textview1() {
		return view.textview1();
	}

	private TextView textview2() {
		return view.textview2();
	}

	private TextView textview3() {
		return view.textview3();
	}

	private SignInView signInView() {
		return view.signInView();
	}

	private String getStr1(ResultItem resultItem) {
		return biz.getStr1(context, resultItem);
	}

	private String getStr2(ResultItem resultItem) {
		return biz.getStr2(context, resultItem);
	}

	private String getStr3(ResultItem resultItem) {
		return biz.getStr3(context, resultItem);
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		if (1 == resultItem.getIntValue("status")) {
			ResultItem item = resultItem.getItem("data");
			switch (what) {
			case HttpType.REFRESH:
				setData(item);
				break;
			case HttpType.LOADING:
				setDataBeforeSignIn(resultItem);
				TrackingUtils.setEvent( TrackingUtils.SIGNEVENT, TrackingUtils.getUserInfoMap());
				break;
			}
		} else {
			showToast(context,  resultItem.getString("msg"));
		}
	}

	@Override
	public void onError(int what, String error) {
		showCustomToast(context, error,Gravity.BOTTOM);
	}

	// �ۼ�ǩ������
	private int counts = 0;
	// ���⽱��������
	private List<Integer> integers = new ArrayList<Integer>();

	private void setData(ResultItem resultItem) {
		button().setText(
				(1 == resultItem.getIntValue("today_is_sign") ? "��ǩ��" : "ǩ��"));
		textview1().setText(Html.fromHtml(getStr1(resultItem)));
		List<ResultItem> list = resultItem.getItems("accum_bonus");
		counts = resultItem.getIntValue("sign_counts");
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				ResultItem item = list.get(i);
				String time = item.getString("num");
				String bonus = item.getString("bonus");
				arrays.add((i == list.size() - 1) ? "����" : time);
				items.add("+" + bonus);
				integers.add(Integer.valueOf(time));
			}
		}

		for (int i = 0; i < integers.size(); i++) {
			int day = integers.get(i);
			if (counts > 0) {
				if (day > counts) {
					current = i > 0 ? (i - 1) : -1;
					break;
				}
				if (day == counts) {
					current = i;
					break;
				}
			}
		}

		signInView().setDatas(arrays, items);
		signInView().setCurrent(current);
		textview2().setText(getStr2(resultItem));
		textview3().setText(getStr3(resultItem));
	}

	private void setDataBeforeSignIn(ResultItem item) {
		button().setText("��ǩ��");
		counts++;
		int days = 1;
		for (int i = 0; i < integers.size(); i++) {
			int day = integers.get(i).intValue();
			if (counts > 0) {
				if (day > counts) {
					days = day - counts;
					current = i > 0 ? (i - 1) : -1;
					break;
				}

				if (day == counts) {
					if (i < (integers.size() - 1)) {
						days = integers.get(i + 1) - counts;
					}
					current = i;
					break;
				}
			}
		}
		String str = "";
		if (counts >= integers.get((integers.size() - 1))) {
			str = context.getResources().getString(R.string.sing_in_title4,
					counts + "" );
		} else {
			str = context.getResources().getString(R.string.sing_in_title1,
					counts + "", days + "" );
		}
		showToast(context, "������ȡ" + item.getString("data") + "���");
		signInView().setCurrent(current);
		textview1().setText(Html.fromHtml(str));
		ListenerManager.sendOnLoginStateChange(true);
	}
}
