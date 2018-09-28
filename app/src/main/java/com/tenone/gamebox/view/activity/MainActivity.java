package com.tenone.gamebox.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.biz.RequestPermissionThread;
import com.tenone.gamebox.mode.listener.DeleteDialogConfrimListener;
import com.tenone.gamebox.mode.listener.HttpResultListener;
import com.tenone.gamebox.mode.listener.UpdateListener;
import com.tenone.gamebox.mode.mode.ResultItem;
import com.tenone.gamebox.view.adapter.ManagementAdapter;
import com.tenone.gamebox.view.base.Constant;
import com.tenone.gamebox.view.base.MyApplication;
import com.tenone.gamebox.view.custom.NoScrollViewPager;
import com.tenone.gamebox.view.custom.TabButtonView;
import com.tenone.gamebox.view.custom.dialog.AnnouncementDialog.AnnouncementDialogBuilder;
import com.tenone.gamebox.view.custom.dialog.UpdateDialog.UpdateBuidler;
import com.tenone.gamebox.view.fragment.DrivingFragment;
import com.tenone.gamebox.view.fragment.MineFragment;
import com.tenone.gamebox.view.fragment.NewGameFragment;
import com.tenone.gamebox.view.fragment.OpenServerFragment;
import com.tenone.gamebox.view.fragment.TradingFragment;
import com.tenone.gamebox.view.receiver.ToDrivingReceiver;
import com.tenone.gamebox.view.service.ApatchService;
import com.tenone.gamebox.view.service.DownloadService;
import com.tenone.gamebox.view.service.ScanService;
import com.tenone.gamebox.view.service.UpdateService;
import com.tenone.gamebox.view.utils.ApkUtils;
import com.tenone.gamebox.view.utils.FileUtils;
import com.tenone.gamebox.view.utils.GDTActionUtils;
import com.tenone.gamebox.view.utils.HttpManager;
import com.tenone.gamebox.view.utils.JrttUtils;
import com.tenone.gamebox.view.utils.ListenerManager;
import com.tenone.gamebox.view.utils.SpUtil;
import com.tenone.gamebox.view.utils.SystemBarUtils;
import com.tenone.gamebox.view.utils.TrackingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("ResourceAsColor")
@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity implements
		DeleteDialogConfrimListener, UpdateListener, ViewPager.OnPageChangeListener, ToDrivingReceiver.ToDrivingListener {
	@ViewInject(R.id.id_mian_viewpager)
	NoScrollViewPager viewPager;
	@ViewInject(R.id.mainRadioGroup)
	RadioGroup mRadioGroup;
	@ViewInject(R.id.id_home_share)
	ImageView shareTv;
	@ViewInject(R.id.radio_button2)
	TabButtonView tabButtonView2;

	private static final int INTERVAL = 2000;
	protected Context mContext;
	private int currentButton = 0;
	private UpdateBuidler builder;
	private List<Fragment> fragments = new ArrayList<Fragment>();
	private ManagementAdapter adapter;
	private NewGameFragment gameFragment;
	private TradingFragment tradingFragment;
	private DrivingFragment drivingFragment;
	private MineFragment mineFragment;
	private ToDrivingReceiver toDrivingReceiver;
	private OpenServerFragment openServerFragment;
	private String path = "";
	private AlertDialog alertDialog;
	private long mExitTime;


	@OnClick({R.id.id_home_share})
	public void onClick(View v) {
		if (R.id.id_home_share == v.getId()) {
			startActivity( new Intent( mContext, ShareActivity.class ) );
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mContext = this;
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		RequestPermissionThread.requestAllPermissions( this, 20065 );
		Intent intent = getIntent();
		if (intent.hasExtra( "bundle" )) {
			Bundle bundle = intent.getBundleExtra( "bundle" );
			String title = bundle.getString( "title" ), content = bundle.getString( "content" );
			if (!TextUtils.isEmpty( title ) && !TextUtils.isEmpty( content )) {
				showNotice( title, content );
			}
			String upadteUrl = bundle.getString( "updateUrl" );
			if (!TextUtils.isEmpty( upadteUrl )) {
				showUpdate( upadteUrl );
			}
		}
		ViewUtils.inject( this );
		SystemBarUtils.compat( this, R.color.white );
		new DoLoginThread().start();
		new RandomThread().start();
		initBeforeData();
		initEvents();
		startServices();
		initGuide();
		initTj();
	}

	private void initTj() {
		TrackingUtils.initTracking( getApplicationContext() );
		if (SpUtil.gdtIsFirst()) {
			if (Build.VERSION.SDK_INT > 23 && ContextCompat.checkSelfPermission( mContext,
					Manifest.permission.READ_PHONE_STATE )
					!= PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions( (Activity) mContext,
						new String[]{Manifest.permission.READ_PHONE_STATE,}, 10036 );
			} else {
				GDTActionUtils.reportData( mContext, 0 );
			}
		}

	}

	private void initGuide() {
		Intent intent1 = getIntent();
		if (intent1.hasExtra( "dynamicId" )) {
			String dynamicId = intent1.getExtras().getString( "dynamicId" );
			startActivity( new Intent( this, DynamicDetailsActivity.class )
					.putExtra( "dynamicId", dynamicId ) );
		}
		
	}

	private void startServices() {
		Intent i = new Intent( mContext, DownloadService.class );
		startService( i );
		Intent intent = new Intent( this, ApatchService.class );
		intent.setAction( "apatch" );
		startService( intent );
		Intent scan = new Intent( this, ScanService.class );
		startService( scan );
	}

	protected void initBeforeData() {
		new Thread() {
			@Override
			public void run() {
				gameFragment = new NewGameFragment();
				fragments.add( gameFragment );
				if (Constant.isIsShowTrading()) {
					tradingFragment = new TradingFragment();
					fragments.add( tradingFragment );
				} else {
					openServerFragment = new OpenServerFragment();
					fragments.add( openServerFragment );
				}
				drivingFragment = new DrivingFragment();
				fragments.add( drivingFragment );
				mineFragment = new MineFragment();
				fragments.add( mineFragment );
				adapter = new ManagementAdapter( getSupportFragmentManager() );
				adapter.setArray( fragments );
				runOnUiThread( () -> {
					registerToDriving();
					registerUpdateReceiver();
					if (Constant.isIsShowTrading()) {
						tabButtonView2.setCheckedIcon( R.drawable.c_icon_jiaoyi_an, R.drawable.c_icon_jiaoyou );
						tabButtonView2.setText( "\u4ea4\u6613" );
					} else {
						tabButtonView2.setCheckedIcon( R.drawable.ic_libao_defult, R.drawable.ic_libao_checkde );
						tabButtonView2.setText( "\u5f00\u670d\u8868" );
					}
					viewPager.setAdapter( adapter );
					viewPager.setOffscreenPageLimit( 4 );
					viewPager.setCurrentItem( 0 );
					viewPager.addOnPageChangeListener( MainActivity.this );
				} );
				super.run();
			}
		}.start();
	}

	private void registerToDriving() {
		toDrivingReceiver = new ToDrivingReceiver();
		IntentFilter intentFilter1 = new IntentFilter();
		intentFilter1.addAction( "ToDriving" );
		LocalBroadcastManager.getInstance( this ).registerReceiver( toDrivingReceiver, intentFilter1 );
		toDrivingReceiver.setToDrivingListener( this );
	}


	private void registerUpdateReceiver() {
		ListenerManager.registerUpdateListener( this );
	}

	protected void initEvents() {
		mRadioGroup
				.setOnCheckedChangeListener( (group, checkedId) -> {
					switch (checkedId) {
						case R.id.radio_button1:
							currentButton = 0;
							break;
						case R.id.radio_button2:
							currentButton = 1;
							break;
						case R.id.radio_button3:
							currentButton = 2;
							break;
						case R.id.radio_button4:
							currentButton = 3;
							break;
					}
					viewPager.setCurrentItem( currentButton );
				} );
	}

	private void showDialog() {
		if (builder == null) {
			builder = new UpdateBuidler( mContext );
		}
		builder.setMessage( getResources().getString( R.string.have_new_update ) );
		builder.setConfirmText( getResources().getString( R.string.now_update ) );
		builder.setCancleText( getResources().getString( R.string.no_update ) );
		builder.setConfrimListener( this );
		builder.show();
	}

	public String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo( this.getPackageName(), 0 );
			String version = info.versionCode + "";
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}


	private void exit() {
		if (System.currentTimeMillis() - mExitTime > INTERVAL) {
			Toast.makeText( this, getResources().getString( R.string.than_one_exit ), Toast.LENGTH_SHORT ).show();
			mExitTime = System.currentTimeMillis();
		} else {
			finish();
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				exit();
			}
			return true;
		}
		return super.dispatchKeyEvent( event );
	}


	@Override
	public void onConfrimClick(AlertDialog dialog) {
		alertDialog = dialog;
		Intent service = new Intent( mContext, UpdateService.class );
		service.putExtra( "path", path );
		startService( service );
		builder.getMessageTv().setText( getResources().getString( R.string.dowanloading ) + 0 + "%" );
		builder.getProgressBar().setVisibility( View.VISIBLE );
		builder.getProgressBar().setProgress( 0 );
		builder.getConfirmBt().setVisibility( View.GONE );
		builder.getCancleBt().setVisibility( View.GONE );
	}

	@Override
	public void update(int progerss, String apkName) {
		Log.i( "MainActivity", "update apkName is  " + apkName + " progerss is " + progerss );
		if (progerss > 0 && progerss < 100) {
			if (builder != null) {
				builder.getMessageTv().setText( getResources().getString( R.string.dowanloading ) + progerss + "%" );
				builder.getProgressBar().setProgress( progerss );
			}
		} else if (progerss == 100) {
			builder.getMessageTv().setText( getResources().getString( R.string.dowanloadend_install ) );
			builder.getProgressBar().setProgress( progerss );
			ApkUtils.installApp( apkName, this );
			if (alertDialog != null) {
				alertDialog.dismiss();
			}
		}
	}

	@Override
	protected void onDestroy() {
		ListenerManager.unRegisterUpdateListener( this );
		LocalBroadcastManager.getInstance( this ).unregisterReceiver( toDrivingReceiver );
		ImmersionBar.with( this ).destroy();
		super.onDestroy();
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		switch (position) {
			case 0:
				SystemBarUtils.compat( this, R.color.white );
				break;
			case 1:
				SystemBarUtils.compat( this, R.color.white );
				break;
			case 2:
				SystemBarUtils.compat( this, R.color.white );
				break;
			case 3:
				
				ListenerManager.sendOnMainViewPagerChangeListener( 3 );
				break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	private void showUpdate(String upUrl) {
		Log.i( "MainActivity", "url is " + upUrl );
		path = upUrl;
		if (!TextUtils.isEmpty( path )
				&& !SpUtil.isUpdateing()) {
			showDialog();
		}
	}

	private void showNotice(String title, String content) {
		AnnouncementDialogBuilder builder = new AnnouncementDialogBuilder(
				mContext );
		builder.setTitle( title );
		builder.setContent( content );
		builder.showDialog();
	}

	@Override
	public void OnToDriving() {
		toDriving();
	}

	private class DoLoginThread extends Thread {

		private void login() {
			String userName = SpUtil.getAccount();
			String pwd = SpUtil.getPwd();
			if (!TextUtils.isEmpty( pwd )) {
				HttpManager.login( 1, mContext, new HttpResultListener() {
					@Override
					public void onSuccess(int what, ResultItem resultItem) {
						int status = resultItem.getIntValue( "status" );
						jrtt( 1 == status );
						if (1 == status) {
							ResultItem item = resultItem.getItem( "data" );
							if (null != item) {
								String uId = item.getString( "id" );
								String header = item.getString( "icon_url" );
								String coin = item.getString( "coin" );
								String platform = item.getString( "platform_money" );
								String bonus = item.getString( "recom_bonus" );
								String phone = item.getString( "mobile" );
								String nickName = item.getString( "nick_name" );
								Constant.setIsVip( item.getBooleanValue( "is_vip", 1 ) );
								TrackingUtils.setLoginSuccessBusiness( uId );
								SpUtil.setUserId( uId );
								SpUtil.setHeaderUrl( header );
								SpUtil.setPhone( phone );
								SpUtil.setNick( nickName );
								MyApplication.getInstance().setCoin( coin );
								MyApplication.getInstance().setPlatform( platform );
								MyApplication.getInstance().setRecom_bonus( bonus );
							}
						} else {
							SpUtil.setUserId( "-1" );
							SpUtil.setHeaderUrl( "" );
							SpUtil.setPhone( "" );
							SpUtil.setNick( "" );
							MyApplication.getInstance().setCoin( "0" );
							MyApplication.getInstance().setPlatform( "0" );
							MyApplication.getInstance().setRecom_bonus( "0" );
						}
					}

					@Override
					public void onError(int what, String error) {
						Log.e( "dologin", error );
					}
				}, userName, pwd );
			}
		}

		private void jrtt(boolean b) {
			if (Build.VERSION.SDK_INT > 21 && ContextCompat.checkSelfPermission( mContext,
					Manifest.permission.READ_PHONE_STATE )
					!= PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions( (Activity) mContext,
						new String[]{Manifest.permission.READ_PHONE_STATE,}, 22236 );
			} else {
				JrttUtils.jrttReportData( JrttUtils.ACCOUNTLOGIN, b );
			}
		}

		@Override
		public void run() {
			login();
			super.run();
		}
	}

	private void toDriving() {
		currentButton = 2;
		viewPager.setCurrentItem( 2 );
		mRadioGroup.check( R.id.radio_button3 );
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult( requestCode, permissions, grantResults );
		if (requestCode == 10036) {
			GDTActionUtils.reportData( mContext, 0 );
		} else if (requestCode == 22236) {
			JrttUtils.jrttReportData( JrttUtils.ACCOUNTLOGIN );
		}
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

	private class RandomThread extends Thread {
		@Override
		public void run() {
			File file = getRandomFile();
			String str = readRandom( file );
			String random = String.valueOf( System.currentTimeMillis() );
			Log.i( "GameBox", "defulat random is " + random );
			if (!TextUtils.isEmpty( str )) {
				String[] array = str.split( "-" );
				if (array != null && array.length > 1) {
					String channel = array[0];
					if (!MyApplication.getConfigModle().getChannelID().equals( channel )) {
						saveRandom( file, random );
					} else {
						random = array[1];
					}
					Log.i( "GameBox", "get random is " + random );
				}
			} else {
				saveRandom( file, random );
			}
			Constant.setRandom( random );
			super.run();
		}

		@NonNull
		private File getRandomFile() {
			File file = new File( FileUtils.getFileDirectory( mContext ).getAbsolutePath() + "/random.ini" );
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return file;
		}

		@NonNull
		private String readRandom(File file) {
			try {
				char[] buffer;
				FileInputStream fis = new FileInputStream( file );
				InputStreamReader reader = new InputStreamReader( fis, "utf-8" );
				buffer = new char[fis.available()];
				reader.read( buffer );
				return new String( buffer ).trim();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "";
		}

		private void saveRandom(File file, String random) {
			try {
				FileOutputStream fos = new FileOutputStream( file );
				OutputStreamWriter osw = new OutputStreamWriter( fos, "utf-8" );
				osw.write( MyApplication.getConfigModle().getChannelID() + "-" + random );
				osw.flush();
				fos.flush();
				osw.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
