/** 
 * Project Name:GameBox 
 * File Name:TailorImagePresenter.java 
 * Package Name:com.tenone.gamebox.presenter 
 * Date:2017-3-22ÉÏÎç10:30:30 
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tenone.gamebox.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.TailorImageBiz;
import com.tenone.gamebox.mode.view.TailorImageView;
import com.tenone.gamebox.view.base.BaseActivity;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.custom.TitleBarView;
import com.tenone.gamebox.view.custom.tailor.ClipImageLayout;
import com.tenone.gamebox.view.utils.BitmapUtils;

/**
 * ²Ã¼ô ClassName:TailorImagePresenter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017-3-22 ÉÏÎç10:30:30 <br/>
 * 
 * @author John Lie
 * @version
 * @since JDK 1.6
 * @see
 */
public class TailorImagePresenter implements OnClickListener {

	TailorImageBiz tailorImageBiz;
	TailorImageView tailorImageView;
	Context mContext;

	public TailorImagePresenter(TailorImageView v, Context c) {
		this.mContext = c;
		this.tailorImageView = v;
		this.tailorImageBiz = new TailorImageBiz();
	}

	public void initView() {
		getTitleBarView().setTitleText("²Ã¼ôÍ¼Æ¬");
		getTitleBarView().setLeftImg(R.drawable.icon_back_grey);
		getTitleBarView().setRightText("Íê³É");

	}

	public void initListener() {
		getTitleBarView().getRightText().setOnClickListener(this);
		getTitleBarView().getLeftImg().setOnClickListener(this);
	}

	public ClipImageLayout getClipImageLayout() {
		return tailorImageView.getClipImageLayout();
	}

	public TitleBarView getTitleBarView() {
		return tailorImageView.getTitleBarView();
	}

	public void setZoomImgageDrawable(Intent intent) {
		getClipImageLayout().setImageDrawable(
				tailorImageBiz.getDrawable(intent));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_titleBar_rightText:
			Bitmap bitmap = getClipImageLayout().clip();
			String path = BitmapUtils.saveBitmapToFile(bitmap,
					Configuration.getCachepath(), "header");
			bitmap.recycle();
			Intent intent = new Intent();
			intent.putExtra("path", path);
			((BaseActivity) mContext).setResult(Activity.RESULT_OK, intent);
			((BaseActivity) mContext).finish();
			break;
		case R.id.id_titleBar_leftImg:
			((BaseActivity) mContext).finish();
			break;
		}
	}

}
