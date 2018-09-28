package com.tenone.gamebox.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tenone.gamebox.R;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.utils.DisplayMetricsUtils;
import com.tenone.gamebox.view.utils.JrttUtils;

import java.util.Timer;
import java.util.TimerTask;

public class ADSActivity extends Activity {
	@ViewInject(R.id.id_ads_layout)
	RelativeLayout relativeLayout;
	@ViewInject(R.id.id_ads_dump)
	TextView dumpTv;

	private Context mContext;
	private TimerTask timerTask;
	private Timer timer;
	private int t = 3;
	private Bundle dataBundle;
	private String link;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		super.onCreate( arg0 );
		mContext = this;
		getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN );
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView( R.layout.activity_ads );
		dataBundle = getIntent().getBundleExtra( "bundle" );
		if (dataBundle != null) {
			link = dataBundle.getString( "link" );
		}
		ViewUtils.inject( this );
		new Thread() {
			@Override
			public void run() {
				String splashImgpath = Configuration.SPLASHIMGPATH + "/"
						+ Configuration.SPLASHIMGNAME + ".JPEG";
				Drawable drawable = Drawable.createFromPath( splashImgpath );
				runOnUiThread( () -> {
					relativeLayout.setBackground( drawable );
					RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) dumpTv.getLayoutParams();
					params.topMargin = DisplayMetricsUtils.getStatusBarHeight( mContext ) +
							DisplayMetricsUtils.dipTopx( mContext, 10 );
					params.rightMargin = DisplayMetricsUtils.getStatusBarHeight( mContext ) +
							DisplayMetricsUtils.dipTopx( mContext, 10 );
					dumpTv.setLayoutParams( params );
					timer = new Timer();
					timerTask = new TimerTask() {
						@Override
						public void run() {
							runOnUiThread( () -> {
								t--;
								dumpTv.setText( "\u8df3\u8fc7 " + t + "s" );
								if (t < 1) {
									startMain();
									return;
								}
							} );
						}
					};
					timer.schedule( timerTask, 1000, 1000 );
				} );
				super.run();
			}
		}.start();
		dumpTv.setOnClickListener( v -> startMain() );
		relativeLayout.setOnClickListener( v -> startLink() );
	}

	private void startMain() {
		cancleTimer();
		Uri uri = getIntent().getData();
		Intent intent = new Intent( mContext, MainActivity.class );
		if (dataBundle != null) {
			intent.putExtra( "bundle", dataBundle );
		}
		if (uri != null) {
			String dynamicId = uri.getQueryParameter( "dynamicId" );
			if (!TextUtils.isEmpty( dynamicId )) {
				intent.putExtra( "dynamicId", dynamicId );
			}
		}
		startActivity( intent );
		finish();
		overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
	}

	private void startLink() {
		if (!TextUtils.isEmpty( link )) {
			startMain();
			Intent intent = new Intent();
			intent.setData( Uri.parse( link ) );
			intent.setAction( Intent.ACTION_VIEW );
			startActivity( intent );
		}
	}

	private void cancleTimer() {
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
		}
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	@Override
	protected void onDestroy() {
		cancleTimer();
		super.onDestroy();
	}


	@Override
	protected void onResume() {
		super.onResume();
		JrttUtils.onResume( this );
	}

	@Override
	protected void onPause() {
		super.onPause();
		JrttUtils.onPause( this );
	}
}
