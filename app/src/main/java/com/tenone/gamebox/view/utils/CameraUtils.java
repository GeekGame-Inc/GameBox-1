package com.tenone.gamebox.view.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tenone.gamebox.view.custom.photo.MultiImageSelectorActivity;

import java.util.ArrayList;

public class CameraUtils {

	public static void startAlbum(Context context, Intent intent,
																int requestCode) {
		((Activity) context).startActivityForResult(intent, requestCode);
	}

	public static void openAlbum(Context cxt, ArrayList<String> array,
			int requestCode) {
		Intent intent = new Intent(cxt, MultiImageSelectorActivity.class);
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
				MultiImageSelectorActivity.MODE_MULTI);
		intent.putStringArrayListExtra(
				MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, array);
		startAlbum(cxt, intent, requestCode);
	}
}
