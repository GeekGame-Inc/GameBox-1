package com.tenone.gamebox.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.RequestPermissionThread;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.mode.GamePackMode;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.presenter.AppStatisticsManager;
import com.tenone.gamebox.view.base.Configuration;
import com.tenone.gamebox.view.base.Constant;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.service.AlarmService;
import com.tenone.gamebox.view.service.DetectionServer;
import com.tenone.gamebox.view.service.DownSplashService;
import com.tenone.gamebox.view.service.ScanService;
import com.tenone.gamebox.view.utils.BeanUtils;
import com.tenone.gamebox.view.utils.DatabaseUtils;
import com.tenone.gamebox.view.utils.GDTActionUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.JrttUtils;
import com.tenone.gamebox.view.utils.MyLog;
import com.tenone.gamebox.view.utils.ReservedUtils;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.TrackingUtils;
import com.tenone.gamebox.view.utils.download.FileDownloaderManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressLint("NewApi")
public class SplashActivity extends Activity {
	private Context mContext;
	private boolean isStart = false;
	private boolean timeEnd = false;
	private boolean initEnd = false;
	private TimerTask timerTask;
	private Timer timer;
	private int t = 0;
	private Bundle dataBundle;


	private Drawable drawable1 = null;
	private ExecutorService executorService;
	/* 最大下载队列数 */
	public static final int MAXDOWMTASKSIZE = 5;
	/* 重试时间单位/s */
	public static final int RETRYTIME = 4;
	private ImageView imageView;

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		FileDownloaderManager.initFileDownloader( getApplicationContext(), MAXDOWMTASKSIZE, RETRYTIME );
		super.onCreate( savedInstanceState );
		/**全屏设置，隐藏窗口所有装饰**/
		getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN );
		/**标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题**/
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		drawable1 = ContextCompat.getDrawable( this, R.drawable.ic_loading );
		setActivityBackground();
		ViewUtils.inject( this );
		mContext = this;
		RequestPermissionThread.requestAllPermissions( this );
		executorService = Executors.newFixedThreadPool( 1 );
		executorService.execute( new MyThread() );
		executorService.shutdown();
		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				t++;
				if (t > 2) {
					timeEnd = true;
					handler.sendEmptyMessage( 12 );
				}
			}
		};
		timer.schedule( timerTask, 1000, 1000 );
	}

	private void startAlarmServer() {
		Intent i = new Intent( getApplicationContext(), AlarmService.class );
		startService( i );
		ReservedUtils.registerReserved( getApplicationContext() );
	}

	private void startScanServer() {
		Intent scanIntent = new Intent( getApplicationContext(), ScanService.class );
		startService( scanIntent );
	}

	private void startDetectionServer() {
		/*
		 * 比对启动页面进行 start
		 */
		Intent intent = new Intent( getApplicationContext(),
				DetectionServer.class );
		startService( intent );
		/* end */
	}

	private void setActivityBackground() {
		if (imageView == null) {
			imageView = new ImageView( this );
		}
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT );
		imageView.setLayoutParams( params );
		imageView.setBackground( drawable1 );
		setContentView( imageView );
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

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler( msg -> {
		if (!isStart && timeEnd && initEnd) {
			if (msg.what == 12) {
				cancleTimer();
				startActivityForResult( new Intent( this, InitJRTTActivity.class ), 12 );
			} else if (msg.what == 1) {
				cancleTimer();
				String splashImgpath = Configuration.SPLASHIMGPATH + "/" + Configuration.SPLASHIMGNAME + ".JPEG";
				File file = new File( splashImgpath );
				Uri uri = getIntent().getData();
				//startMainActivity( uri );
				if (!file.exists()) {
					startMainActivity( uri );
				} else {
					startAdsActivity( uri );
				}
			}
		}
		return false;
	} );

	private void startAdsActivity(Uri uri) {
		Intent intent = new Intent( mContext, ADSActivity.class );
		intent.setData( uri );
		if (dataBundle != null) {
			intent.putExtra( "bundle", dataBundle );
		}
		startActivity( intent );
		isStart = true;
		finish();
		overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
	}

	private void startMainActivity(Uri uri) {
		Intent intent;
		if (SpUtil.getShowGuide()) {
			intent = new Intent( mContext, MainActivity.class );
		} else {
			intent = new Intent( mContext, GuidanceActivity.class );
		}
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
		isStart = true;
		finish();
		overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
	}

	private class MyThread extends Thread implements HttpResultListener {
		@Override
		public void run() {
			startDetectionServer();
			startScanServer();
			startAlarmServer();
			HttpManager.doInitV2( 12, getApplicationContext(), this, SpUtil.installIsFirst() ? 1 : 0 );
			super.run();
		}

		@Override
		public void onSuccess(int what, ResultItem resultItem) {
			if (1 == resultItem.getIntValue( "status" )) {
				ResultItem item = resultItem.getItem( "data" );
				Constant.setIsShowTrading( item.getBooleanValue( "business_enbaled", 1 ) );
				setBoxStatic( item );
				setUpAndNotice( item );
				setConsult( item );
				SpUtil.setinstallIsFirst( false );
				setGameNams( item );
				setGamePacks( item );
				startServer( item.getString( "start_page" ) );
				Log.d( "AppStatisticsManager", "actstatic_enabled is " + item.getBooleanValue( "actstatic_enabled", 1 ) );
				if (item.getBooleanValue( "actstatic_enabled", 1 )) {
					AppStatisticsManager.init( getApplicationContext() );
				}
			} else {
				TrackingUtils.setIsStatistical( false );
				GDTActionUtils.setIsStartGDTAction( false );
				JrttUtils.setIsJrttStatistical( false );
				MyLog.e( resultItem.getString( "msg" ) );
			}
			startMain();
		}

		private void setConsult(ResultItem item) {
			ResultItem consult = item.getItem( "consult" );
			if (consult != null) {
				List<ResultItem> protocol = consult.getItems( "protocol" );
				List<ResultItem> notice = consult.getItems( "notice" );
				if (!BeanUtils.isEmpty( protocol )) {
						Constant.getConsultProtocol().clear();
						int size = protocol.size();
					for (int i = 0; i < size; i++) {
						Constant.getConsultProtocol().add( String.valueOf( protocol.get( i ) ) );
					}
				}
				if (!BeanUtils.isEmpty( notice )) {
					Constant.getConsultNotice().clear();
					int size = notice.size();
					for (int i = 0; i < size; i++) {
						Constant.getConsultNotice().add( String.valueOf( notice.get( i ) ) );
					}
				}
			}
		}

		private void startMain() {
			if (handler != null) {
				initEnd = true;
				handler.sendEmptyMessage( 12 );
			}
		}

		private void setUpAndNotice(ResultItem item) {
			dataBundle = new Bundle();
			String updateUrl = item.getString( "update_url" );
			dataBundle.putString( "updateUrl", updateUrl );
			dataBundle.putString( "link", item.getString( "start_page_link" ) );
			ResultItem notice = item.getItem( "app_notice" );
			if (!BeanUtils.isEmpty( notice )) {
				String title = notice.getString( "title" );
				String content = notice.getString( "content" );
				dataBundle.putString( "title", title );
				dataBundle.putString( "content", content );
			}
		}

		private void setBoxStatic(ResultItem item) {
			int a = item.getIntValue( "box_static" );
			TrackingUtils.setIsStatistical( 2 == a );
			GDTActionUtils.setIsStartGDTAction( 3 == a );
			JrttUtils.setIsJrttStatistical( 4 == a );
			int b = item.getIntValue( "discount_enabled" );
			MyApplication.setIsShowDiscount( 1 == b );
		}

		private void setGamePacks(ResultItem resultItem) {
			List<ResultItem> gamePacks = resultItem.getItems( "game_packs" );
			if (gamePacks != null) {
				List<GamePackMode> packModes = new ArrayList<GamePackMode>();
				for (ResultItem item : gamePacks) {
					GamePackMode mode = new GamePackMode();
					String gameId = item.getString( "id" );
					if (!TextUtils.isEmpty( gameId )) {
						mode.setGameId( Integer.valueOf( gameId ) );
					}
					mode.setPackName( item.getString( "android_pack" ) );
					mode.setVersion( item.getString( "version" ) );
					packModes.add( mode );
				}
				DatabaseUtils.getInstanse( mContext ).cleanGamePacks();
				DatabaseUtils.getInstanse( mContext ).insertGamePacks( packModes );
			}
		}

		private void setGameNams(ResultItem resultItem) {
			List<ResultItem> gameNames = resultItem.getItems( "game_names" );
			if (!BeanUtils.isEmpty( gameNames )) {
				List<String> allName = new ArrayList<String>();
				for (int i = 0; i < gameNames.size(); i++) {
					allName.add( String.valueOf( gameNames.get( i ) ) );
				}
				if (!allName.isEmpty()) {
					SpUtil.setAllGameNames( allName );
				}
			}
		}

		private void startServer(String url) {
			Intent service = new Intent( getApplicationContext(),
					DownSplashService.class );
			service.putExtra( "url", url );
			startService( service );
		}

		@Override
		public void onError(int what, String error) {
			TrackingUtils.setIsStatistical( false );
			GDTActionUtils.setIsStartGDTAction( false );
			JrttUtils.setIsJrttStatistical( false );
			startMain();
		}
	}

	@Override
	protected void onDestroy() {
		cancleTimer();
		imageView.setBackground( null );
		super.onDestroy();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult( requestCode, resultCode, data );
		if (requestCode == 12 && resultCode == RESULT_OK) {
			handler.sendEmptyMessage( 1 );
		}
	}

}
