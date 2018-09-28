package com.tenone.gamebox.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.TailorImageView;
import com.tenone.gamebox.presenter.TailorImagePresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.tailor.ClipImageLayout;

public class TailorImageActivity extends BaseActivity implements
		TailorImageView {
	@ViewInject(R.id.id_tailor_clipImageLayout)
	ClipImageLayout clipImageLayout;
	@ViewInject(R.id.id_title_bar)
	TitleBarView barView;
	@ViewInject(R.id.id_img)
	ImageView imageView;
	TailorImagePresenter presenter;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_tailor);
		ViewUtils.inject(this);
		presenter = new TailorImagePresenter(this, this);
		presenter.initView();
		presenter.initListener();
		presenter.setZoomImgageDrawable(getIntent());
	}

	@Override
	public ClipImageLayout getClipImageLayout() {
		return clipImageLayout;
	}

	@Override
	public TitleBarView getTitleBarView() {
		return barView;
	}

	@Override
	public ImageView getImageView() {
		return imageView;
	}
}
