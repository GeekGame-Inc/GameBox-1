package com.tenone.gamebox.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.view.PublishCommentView;
import com.tenone.gamebox.presenter.PublishCommentPresenter;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.custom.CustomizeEditText;
import com.tenone.gamebox.view.custom.TitleBarView;

public class PublishCommentActivity extends BaseActivity implements
		PublishCommentView {
	@ViewInject(R.id.id_title_bar)
	TitleBarView titleBarView;
	@ViewInject(R.id.id_publish_album)
	ImageView albumImg;
	@ViewInject(R.id.id_publish_collect)
	ImageView collectImg;
	@ViewInject(R.id.id_publish_image)
	ImageView imageView;
	@ViewInject(R.id.id_publish_editText)
	CustomizeEditText customizeEditText;

	PublishCommentPresenter presenter;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_publish_comment);
		ViewUtils.inject(this);
		presenter = new PublishCommentPresenter(this, this);
		presenter.initView();
		presenter.initListener();
	}

	@Override
	public TitleBarView getTitleBarView() {
		return titleBarView;
	}

	@Override
	public CustomizeEditText getEditText() {
		return customizeEditText;
	}

	@Override
	public ImageView getAlbumImgView() {
		return albumImg;
	}

	@Override
	public ImageView getImgView() {
		return imageView;
	}

	@Override
	public ImageView getCollectImgView() {
		return collectImg;
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		presenter.onActivityResult(arg0, arg1, arg2);
		super.onActivityResult(arg0, arg1, arg2);
	}
}
