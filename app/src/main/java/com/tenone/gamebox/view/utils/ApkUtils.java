package com.tenone.gamebox.view.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.tenone.gamebox.R;
import com.tenone.gamebox.mode.mode.GameModel;
import com.tenone.gamebox.mode.mode.GamePackMode;
import com.tenone.gamebox.mode.mode.GameStatus;
import com.tenone.gamebox.view.base.Configuration;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ApkUtils {
	static String TAG = "ApkUtils";

	public static boolean isAppInstalled(Context context, String name) {
		final PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> pinfo = packageManager.getInstalledPackages( 0 );
		Log.d( TAG, "pinfo 的大小是" + pinfo.size() );
		List<String> pName = new ArrayList<String>();
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get( i ).packageName;
				pName.add( pn );
			}
		}
		return pName.contains( name );
	}

	public static void installApp(String apkName, final Context context) {
		if (Build.VERSION.SDK_INT >= 26) {
			boolean haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();
			if (!haveInstallPermission) {
				DialogUtils.showConfirmDialog( context, dialog -> {
					startInstallPermissionSettingActivity( context );
					if (dialog != null && dialog.isShowing())
						dialog.dismiss();
				}, context.getString( R.string.install_app_txt ), context.getString( R.string.no_install ), context.getString( R.string.go_open ) );
				return;
			}
		}
		AutoInstallUtils.setUrl( Configuration.getDownloadpath() + "/" + apkName );
		AutoInstallUtils.install( context, apkName );
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	private static void startInstallPermissionSettingActivity(Context context) {
		//8.0
		Intent intent = new Intent( Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES );
		context.startActivity( intent );
	}

	public static void unstallApp(String packagename, Context context) {
		Intent uninstall_intent = new Intent();
		uninstall_intent.setAction( Intent.ACTION_DELETE );
		uninstall_intent.setData( Uri.parse( "package:" + packagename ) );
		context.startActivity( uninstall_intent );
	}

	@SuppressLint("NewApi")
	public static void doStartApplicationWithPackageName(String packagename,
																											 Context context) {
		PackageInfo packageinfo = null;
		try {
			packageinfo = context.getPackageManager().getPackageInfo( packagename, 0 );
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (packageinfo == null) {
			return;
		}
		Intent resolveIntent = new Intent( Intent.ACTION_MAIN, null );
		resolveIntent.addCategory( Intent.CATEGORY_LAUNCHER );
		resolveIntent.setPackage( packageinfo.packageName );
		List<ResolveInfo> resolveinfoList = context.getPackageManager().queryIntentActivities( resolveIntent, 0 );
		ResolveInfo resolveinfo = resolveinfoList.iterator().next();
		if (resolveinfo != null) {
			String packageName = resolveinfo.activityInfo.packageName;
			@SuppressWarnings("unused")
			String className = resolveinfo.activityInfo.name;
			Intent intent = context.getPackageManager()
					.getLaunchIntentForPackage( packageName );
			context.startActivity( intent );
		}
	}

	public static List<PackageInfo> getAllApps(Context context) {
		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		PackageManager pManager = context.getPackageManager();
		List<PackageInfo> paklist = pManager.getInstalledPackages( 0 );
		for (int i = 0; i < paklist.size(); i++) {
			PackageInfo pak = paklist.get( i );
			if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
				apps.add( pak );
			}
		}
		return apps;
	}

	public static String getVersionName(Context context) {
		return getPackageInfo( context ).versionName;
	}

	public static int getVersionCode(Context context) {
		return getPackageInfo( context ).versionCode;
	}

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo pi = null;
		try {
			PackageManager pm = context.getPackageManager();
			pi = pm.getPackageInfo( context.getPackageName(), PackageManager.GET_CONFIGURATIONS );
			return pi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pi;
	}

	public static boolean install(String apkPath) {
		boolean result = false;
		DataOutputStream dataOutputStream = null;
		BufferedReader errorStream = null;
		try {
			Process process = Runtime.getRuntime().exec( "su" );
			dataOutputStream = new DataOutputStream( process.getOutputStream() );
			String command = "pm install -r " + apkPath + "\n";
			dataOutputStream.write( command.getBytes( Charset.forName( "utf-8" ) ) );
			dataOutputStream.flush();
			dataOutputStream.writeBytes( "exit\n" );
			dataOutputStream.flush();
			int what = process.waitFor();
			result = returnResult( what );
		} catch (Exception e) {
			Log.e( "TAG", e.getMessage(), e );
		} finally {
			try {
				if (dataOutputStream != null) {
					dataOutputStream.close();
				}
				if (errorStream != null) {
					errorStream.close();
				}
			} catch (IOException e) {
				Log.e( "TAG", e.getMessage(), e );
			}
		}
		return result;
	}

	public static boolean clientUninstall(String packageName) {
		PrintWriter PrintWriter = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec( "su" );
			PrintWriter = new PrintWriter( process.getOutputStream() );
			PrintWriter.println( "LD_LIBRARY_PATH=/vendor/lib:/system/lib " );
			PrintWriter.println( "pm uninstall " + packageName );
			PrintWriter.flush();
			PrintWriter.close();
			int value = process.waitFor();
			return returnResult( value );
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
		return false;
	}

	public static boolean inspectApk(Context cxt, List<GameModel> items) {
		List<PackageInfo> apps = ApkUtils.getAllApps( cxt );
		boolean isInstall = false;
		for (PackageInfo packageInfo : apps) {
			for (GameModel gameModel : items) {
				String packgeName = packageInfo.packageName;
				if (!TextUtils.isEmpty( gameModel.getPackgeName() )
						&& gameModel.getPackgeName().equals( packgeName )) {
					gameModel.setStatus( GameStatus.INSTALLCOMPLETED );
					gameModel.setProgress( 100 );
					isInstall = true;
				}
			}
		}
		return isInstall;
	}

	public static void inspectApk(Context cxt, List<GameModel> items,
																List<GamePackMode> packModes) {
		PackageManager pManager = cxt.getPackageManager();
		List<PackageInfo> apps = ApkUtils.getAllApps( cxt );
		for (PackageInfo packageInfo : apps) {
			for (GamePackMode packMode : packModes) {
				String packgeName = packageInfo.packageName;
				if (!TextUtils.isEmpty( packMode.getPackName() )
						&& packMode.getPackName().equals( packgeName )) {
					GameModel gameModel = new GameModel();
					gameModel.setGameId( packMode.getGameId() );
					gameModel.setPackgeName( packgeName );
					gameModel.setStatus( GameStatus.INSTALLCOMPLETED );
					gameModel.setProgress( 100 );
					gameModel.setName( pManager.getApplicationLabel(
							packageInfo.applicationInfo ).toString() );
					gameModel.setIconDrawable( pManager
							.getApplicationIcon( packageInfo.applicationInfo ) );
					gameModel.setVersionsName( packageInfo.versionName );
					gameModel.setVersionsCode( packageInfo.versionCode );
					items.add( gameModel );
					break;
				}
			}
		}
	}

	public static boolean inspectApk(Context cxt, GameModel mode) {
		List<PackageInfo> apps = ApkUtils.getAllApps( cxt );
		boolean isInstall = false;
		for (PackageInfo packageInfo : apps) {
			String packgeName = packageInfo.packageName;
			if (!TextUtils.isEmpty( mode.getPackgeName() )
					&& mode.getPackgeName().equals( packgeName )) {
				mode.setStatus( GameStatus.INSTALLCOMPLETED );
				mode.setProgress( 100 );
				isInstall = true;
			}
		}
		return isInstall;
	}

	private static boolean returnResult(int value) {
		if (value == 1) {
			return true;
		} else if (value == 0) {
			return false;
		} else {
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public static boolean checkAppExist(Context cxt, String packageName) {
		if (TextUtils.isEmpty( packageName )) {
			return false;
		}
		try {
			ApplicationInfo info = cxt.getPackageManager().getApplicationInfo(
					packageName, PackageManager.GET_UNINSTALLED_PACKAGES );
			return info != null;
		} catch (NameNotFoundException e) {
			return false;
		}
	}
}
