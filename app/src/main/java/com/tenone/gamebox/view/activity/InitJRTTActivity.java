package com.tenone.gamebox.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.tenone.gamebox.view.utils.JrttUtils;


public class InitJRTTActivity extends Activity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		if (Build.VERSION.SDK_INT > 23 && ContextCompat.checkSelfPermission( this,
				Manifest.permission.READ_PHONE_STATE ) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_PHONE_STATE,}, 1221 );
		} else {
			JrttUtils.init( this );
			setResult( RESULT_OK );
			finish();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult( requestCode, permissions, grantResults );
		if (requestCode == 1221) {
			JrttUtils.init( this );
			setResult( RESULT_OK );
			finish();
		}
	}
}
