package com.tenone.gamebox.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.CommonViewHolder;
import com.tenone.gamebox.mode.mode.HttpType;
import com.tenone.gamebox.mode.mode.PrizesModel;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.adapter.CommonAdapter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.ToastCustom;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.HttpManager;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("InflateParams")
public class MyPrizesActivity extends BaseActivity implements
		HttpResultListener {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_prizes_listview)
	ListView listView;

	private List<PrizesModel> models = new ArrayList<PrizesModel>();
	private CommonAdapter<PrizesModel> mAdapter;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_prizes);
		ViewUtils.inject(this);
		initView();
	}

	@OnClick({ R.id.id_titleBar_leftImg })
	public void onClick(View view) {
		if (view.getId() == R.id.id_titleBar_leftImg) {
			finish();
		}
	}

	private void initView() {
		titleBarView.setTitleText( "\u6211\u7684\u5956\u54c1" );
		titleBarView.setLeftImg(R.drawable.icon_xqf_b);
		HttpManager.prizes(HttpType.REFRESH, this, this);
		mAdapter = new CommonAdapter<PrizesModel>(this, models,
				R.layout.item_prizes) {
			@Override
			public void convert(CommonViewHolder holder, PrizesModel t) {
				holder.setText(R.id.id_prizes_time, t.getTime());
				holder.setText(R.id.id_prizes_type, t.getName());
				View view = holder.getView(R.id.id_prizes_layout);
				int padding = DisplayMetricsUtils.dipTopx(mContext, 10);
				view.setPadding(padding, padding, padding, padding);
			}
		};
		listView.setAdapter(mAdapter);
	}

	@Override
	public void onSuccess(int what, ResultItem resultItem) {
		if (1 == resultItem.getIntValue("status")) {
			List<ResultItem> items = resultItem.getItems("data");
			if (items != null) {
				setData(items);
			}
		} else {
			ToastCustom.makeText(this, resultItem.getString("msg"),
					ToastCustom.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onError(int what, String error) {
		ToastCustom.makeText(this, error, ToastCustom.LENGTH_SHORT).show();
	}

	private void setData(List<ResultItem> list) {
		models.clear();
		for (int i = 0; i < list.size(); i++) {
			PrizesModel model = new PrizesModel();
			ResultItem item = list.get(i);
			model.setName(item.getString("name"));
			model.setTime(item.getString("create_time"));
			models.add(model);
		}
		mAdapter.notifyDataSetChanged();
	}

}
