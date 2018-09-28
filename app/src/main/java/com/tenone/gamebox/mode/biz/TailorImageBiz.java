
package com.tenone.gamebox.mode.biz;  

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.tenone.gamebox.mode.able.TailorImageAble;
import com.tenone.gamebox.view.utils.BitmapUtils;

public class TailorImageBiz implements TailorImageAble {

	@SuppressWarnings("deprecation")
	@Override
	public Drawable getDrawable(Intent intent) {
		String path = intent.getExtras().getString("path");
		BitmapDrawable drawable = new BitmapDrawable(
				BitmapUtils.fileToBitmap(path));
		return drawable;
	}

}
  