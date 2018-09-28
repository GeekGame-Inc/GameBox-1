package com.tenone.gamebox.view.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.tenone.gamebox.view.base.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.os.Environment.MEDIA_MOUNTED;

@SuppressLint("SdCardPath")
public class FileUtils {

	private static final String JPEG_FILE_PREFIX = "IMG_";

	private static final String JPEG_FILE_SUFFIX = ".jpg";

	public static void initFileUtils(Context cxt) {
		getCacheDirectory( cxt, true );
	}

	/**
	 * �Ƿ���ڣ��������򴴽� checkFileExist:(������һ�仰�����������������). <br/>
	 *
	 * @param filePath
	 * @author John Lie
	 * @since JDK 1.6
	 */
	public static void checkFileExist(String filePath) {
		File file = new File( filePath );
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public static boolean isFileExist(String filePath) {
		boolean hasFile = false;
		try {
			File file = new File( filePath );
			hasFile = file.exists();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hasFile;
	}

	public static boolean delFile(File file) {
		if (file.exists()) {
			if (file.isFile()) {
				return file.delete();
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File( path );

		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith( File.separator )) {
				temp = new File( path + tempList[i] );
			} else {
				temp = new File( path + File.separator + tempList[i] );
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile( path + "/" + tempList[i] );
				flag = true;
			}
		}
		return flag;
	}

	public static boolean hasSdcard() {
		return Environment.MEDIA_MOUNTED.equals( Environment
				.getExternalStorageState() );
	}

	public static long getFileOrFilesSize(String filePath) {
		File file = new File( filePath );
		long blockSize = 0;
		try {
			if (file.isDirectory()) {
				blockSize = getFileSizes( file );
			} else {
				blockSize = getFileSize( file );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return blockSize;
	}

	private static long getFileSizes(File f) throws Exception {
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSizes( flist[i] );
			} else {
				size = size + getFileSize( flist[i] );
			}
		}
		return size;
	}

	@SuppressWarnings("resource")
	public static long getFileSize(File file) throws Exception {
		long size = 0;
		FileInputStream fileInputStream = null;
		if (file.exists()) {
			fileInputStream = new FileInputStream( file );
			size = fileInputStream.available();
		} else {
			file.createNewFile();
		}
		return size;
	}

	public static String saveBitmap(String path, Bitmap bm, String picName) {
		String str = "";
		try {
			checkFileExist( path );
			File f = new File( path, picName + ".JPEG" );
			FileOutputStream out = new FileOutputStream( f );
			bm.compress( Bitmap.CompressFormat.JPEG, 90, out );
			out.flush();
			out.close();
			str = f.getPath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static File createFile(Context context) {
		String path = "";
		path = Configuration.getCachepath();
		File mareaFile = new File( path, "imgCache" );
		if (!mareaFile.exists()) {
			mareaFile.mkdirs();
		}
		return mareaFile;
	}

	public static File createTmpFile(Context context) throws IOException {
		if (Build.VERSION.SDK_INT >= 24) {
			File dir = null;
			dir = createFile( context );
			dir = File.createTempFile( JPEG_FILE_PREFIX, JPEG_FILE_SUFFIX, dir );
			return dir;
		} else {
			File dir = null;
			if (TextUtils.equals( Environment.getExternalStorageState(),
					Environment.MEDIA_MOUNTED )) {
				dir = Environment
						.getExternalStoragePublicDirectory( Environment.DIRECTORY_DCIM );
				if (!dir.exists()) {
					dir = Environment
							.getExternalStoragePublicDirectory( Environment.DIRECTORY_DCIM
									+ "/Camera" );
					if (!dir.exists()) {
						dir = getCacheDirectory( context, true );
					}
				}
			} else {
				dir = getCacheDirectory( context, true );
			}
			return File.createTempFile( JPEG_FILE_PREFIX, JPEG_FILE_SUFFIX, dir );
		}
	}

	private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

	/**
	 * Returns application cache directory. Cache directory will be created on
	 * SD card <i>("/Android/data/[app_package_name]/cache")</i> if card is
	 * mounted and app has appropriate permission. Else - Android defines cache
	 * directory on device's file system.
	 *
	 * @param context Application context
	 * @return Cache {@link File directory}.<br />
	 * <b>NOTE:</b> Can be null in some unpredictable cases (if SD card
	 * is unmounted and {@link Context#getCacheDir()
	 * Context.getCacheDir()} returns null).
	 */
	public static File getCacheDirectory(Context context) {
		return getCacheDirectory( context, true );
	}

	public static File getFileDirectory(Context context) {
		return getFileDirectory( context, true );
	}

	/**
	 * Returns application cache directory. Cache directory will be created on
	 * SD card <i>("/Android/data/[app_package_name]/cache")</i> (if card is
	 * mounted and app has appropriate permission) or on device's file system
	 * depending incoming parameters.
	 *
	 * @param context        Application context
	 * @param preferExternal Whether prefer external location for cache
	 * @return Cache {@link File directory}.<br />
	 * <b>NOTE:</b> Can be null in some unpredictable cases (if SD card
	 * is unmounted and {@link Context#getCacheDir()
	 * Context.getCacheDir()} returns null).
	 */
	public static File getCacheDirectory(Context context, boolean preferExternal) {
		File appCacheDir = null;
		String externalStorageState;
		try {
			externalStorageState = Environment.getExternalStorageState();
		} catch (NullPointerException e) { // (sh)it happens (Issue #660)
			externalStorageState = "";
		} catch (IncompatibleClassChangeError e) {
			externalStorageState = "";
		}
		if (preferExternal && MEDIA_MOUNTED.equals( externalStorageState )
				&& hasExternalStoragePermission( context )) {
			appCacheDir = getExternalCacheDir( context );
		}
		if (appCacheDir == null) {
			appCacheDir = context.getCacheDir();
		}
		if (appCacheDir == null) {
			String cacheDirPath = "/data/data/" + context.getPackageName()
					+ "/cache/";
			appCacheDir = new File( cacheDirPath );
			if (!appCacheDir.exists()) {
				appCacheDir.mkdirs();
			}
		}
		return appCacheDir;
	}


	public static File getFileDirectory(Context context, boolean preferExternal) {
		File appFileDir = null;
		String externalStorageState;
		try {
			externalStorageState = Environment.getExternalStorageState();
		} catch (NullPointerException e) { // (sh)it happens (Issue #660)
			externalStorageState = "";
		} catch (IncompatibleClassChangeError e) {
			externalStorageState = "";
		}
		if (preferExternal && MEDIA_MOUNTED.equals( externalStorageState )
				&& hasExternalStoragePermission( context )) {
			appFileDir = getExternalFileDir( context );
		}
		if (appFileDir == null) {
			appFileDir = context.getFilesDir();
		}
		if (appFileDir == null) {
			String cacheDirPath = "/data/data/" + context.getPackageName()
					+ "/files/";
			appFileDir = new File( cacheDirPath );
			if (!appFileDir.exists()) {
				appFileDir.mkdirs();
			}
		}
		return appFileDir;
	}

	/**
	 * Returns individual application cache directory (for only image caching
	 * from ImageLoader). Cache directory will be created on SD card
	 * <i>("/Android/data/[app_package_name]/cache/uil-images")</i> if card is
	 * mounted and app has appropriate permission. Else - Android defines cache
	 * directory on device's file system.
	 *
	 * @param context  Application context
	 * @param cacheDir Cache directory path (e.g.: "AppCacheDir",
	 *                 "AppDir/cache/images")
	 * @return Cache {@link File directory}
	 */
	public static File getIndividualCacheDirectory(Context context,
																								 String cacheDir) {
		File appCacheDir = getCacheDirectory( context );
		File individualCacheDir = new File( appCacheDir, cacheDir );
		if (!individualCacheDir.exists()) {
			if (!individualCacheDir.mkdir()) {
				individualCacheDir = appCacheDir;
			}
		}
		return individualCacheDir;
	}

	private static File getExternalCacheDir(Context context) {
		File dataDir = new File( new File(
				Environment.getExternalStorageDirectory(), "Android" ), "data" );
		File appCacheDir = new File(
				new File( dataDir, context.getPackageName() ), "cache" );
		if (!appCacheDir.exists()) {
			if (!appCacheDir.mkdirs()) {
				return null;
			}
			try {
				new File( appCacheDir, ".nomedia" ).createNewFile();
			} catch (IOException e) {
			}
		}
		return appCacheDir;
	}

	private static File getExternalFileDir(Context context) {
		File dataDir = new File( new File(
				Environment.getExternalStorageDirectory(), "Android" ), "data" );
		File appFileDir = new File(
				new File( dataDir, context.getPackageName() ), "files" );
		if (!appFileDir.exists()) {
			if (!appFileDir.mkdirs()) {
				return null;
			}
			try {
				new File( appFileDir, ".nomedia" ).createNewFile();
			} catch (IOException e) {
			}
		}
		return appFileDir;
	}


	private static boolean hasExternalStoragePermission(Context context) {
		int perm = context
				.checkCallingOrSelfPermission( EXTERNAL_STORAGE_PERMISSION );
		return perm == PackageManager.PERMISSION_GRANTED;
	}

	/**
	 * �ж��ֻ��Ƿ�ӵ��RootȨ�ޡ�
	 *
	 * @return ��rootȨ�޷���true�����򷵻�false��
	 */
	public static boolean isRoot() {
		boolean bool = false;
		try {
			bool = new File( "/system/bin/su" ).exists()
					|| new File( "/system/xbin/su" ).exists();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bool;
	}

	/* �~̖����·�� */
	public static String ACCOUNTPATH = "185SY/";
	public static String ACCOUNTPATH2 = "data/Android/185SY";
	/* �~̖�����ļ��� */
	public static String ACCOUNTNAME = "185File";
	public static String ACCOUNTNAMENEW = "185NewFile";

	/**
	 * TODO:��ȡ�ڴ濨·�� getExternalStorageDirectory. <br/>
	 *
	 * @return
	 * @author John Lie
	 * @since JDK 1.6
	 */
	public static String getExternalStorageDirectory(Context context,
																									 String name, String path) {
		String dirPath = "";

		File appDir = new File( Environment.getExternalStorageDirectory(), name );

		if (hasSdcard()) {
			dirPath = appDir.getPath();
		} else {
			dirPath = path;
		}

		if (!appDir.exists()) {
			appDir.mkdir();
		}
		checkFileExist( dirPath );

		return dirPath;
	}

	/**
	 * TODO:����һ���ļ� createFile. <br/>
	 */
	public static File createFile(String dirPath, String fileName)
			throws IOException {
		checkFileExist( dirPath );
		File mareaFile = new File( dirPath, fileName );
		if (mareaFile.exists()) {
			mareaFile.delete();
		}
		mareaFile.createNewFile();
		return mareaFile;
	}

	/**
	 * �����˻�����
	 *
	 * @param context
	 * @param text
	 */
	public static void saveAccountNew(Context context, String text) {
		String path = getExternalStorageDirectory( context, ACCOUNTPATH,
				ACCOUNTPATH2 );
		// str = EncryptionUtils.encryptBASE64(str);
		try {
			File file = createFile( path, ACCOUNTNAMENEW );
			FileOutputStream fos = new FileOutputStream( file );
			OutputStreamWriter osw = new OutputStreamWriter( fos, "utf-8" );
			osw.write( text );
			osw.flush();
			fos.flush();
			osw.close();
			fos.close();
		} catch (Exception e) {
			Log.e( "SYSDK", "�����˺��ļ�������..." );
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	public static String getAccountNew(Context context) {
		String str = "";
		String path = getExternalStorageDirectory( context, ACCOUNTPATH,
				ACCOUNTPATH2 );
		try {
			checkFileExist( path );
			File mareaFile = new File( path, ACCOUNTNAMENEW );
			if (!mareaFile.exists()) {
				return null;
			}
			FileInputStream fis = new FileInputStream( mareaFile );
			InputStreamReader reader = new InputStreamReader( fis, "utf-8" );
			char[] buffer = new char[fis.available()];
			reader.read( buffer );
			str = new String( buffer ).trim();
			// str = EncryptionUtils.decryptBASE64(str);
		} catch (Exception e) {
			Log.e( "SYSDK", "��ȡ�˺��ļ�������..." );
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * �����˻�����
	 *
	 * @param context
	 * @param account
	 * @param pwd
	 * @param phone
	 */
	public static void saveAccount(Context context, String account, String pwd,
																 String phone, String userId) {
		String path = getExternalStorageDirectory( context, ACCOUNTPATH,
				ACCOUNTPATH2 );
		String str = account + ";" + pwd + ";" + userId + ";" + phone;
		// str = EncryptionUtils.encryptBASE64(str);
		try {
			File file = createFile( path, ACCOUNTNAME );
			FileOutputStream fos = new FileOutputStream( file );
			OutputStreamWriter osw = new OutputStreamWriter( fos, "utf-8" );
			osw.write( str );
			osw.flush();
			fos.flush();
			osw.close();
			fos.close();
		} catch (Exception e) {
			Log.e( "SYSDK", "�����˺��ļ�������..." );
			e.printStackTrace();
		}
	}

	@SuppressWarnings("resource")
	public static String getAccount(Context context) {
		String str = "";
		String path = getExternalStorageDirectory( context, ACCOUNTPATH,
				ACCOUNTPATH2 );
		try {
			checkFileExist( path );
			File mareaFile = new File( path, ACCOUNTNAME );
			if (!mareaFile.exists()) {
				return null;
			}
			FileInputStream fis = new FileInputStream( mareaFile );
			InputStreamReader reader = new InputStreamReader( fis, "utf-8" );
			char[] buffer = new char[fis.available()];
			reader.read( buffer );
			str = new String( buffer ).trim();
			// str = EncryptionUtils.decryptBASE64(str);
		} catch (Exception e) {
			Log.e( "SYSDK", "��ȡ�˺��ļ�������..." );
			e.printStackTrace();
		}
		return str;
	}


	/**
	 * ���Ƶ����ļ�
	 *
	 * @param oldPath String ԭ�ļ�·�� �磺c:/fqf.txt
	 * @param newPath String ���ƺ�·�� �磺f:/fqf.txt
	 * @return boolean
	 */
	@SuppressWarnings("resource")
	public static void copyFile(String oldPath, String newPath) {
		try {
			int byteread = 0;
			File oldfile = new File( oldPath );
			if (oldfile.exists()) { // �ļ�����ʱ
				InputStream inStream = new FileInputStream( oldPath ); // ����ԭ�ļ�
				FileOutputStream fs = new FileOutputStream( newPath );
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read( buffer )) != -1) {
					fs.write( buffer, 0, byteread );
				}
				inStream.close();
				Log.e( "GameBox", "����ҳ�������" );
			} else {
				Log.e( "GameBox", "����ҳ������" );
			}
		} catch (Exception e) {
			System.out.println( "���Ƶ����ļ���������" );
			e.printStackTrace();
		}
	}

	/**
	 * �����ļ�
	 *
	 * @param source �����ļ�
	 * @param target ����ļ�
	 */
	public static void copyFile(File source, File target) {
		FileInputStream fileInputStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			fileInputStream = new FileInputStream( source );
			fileOutputStream = new FileOutputStream( target );
			byte[] buffer = new byte[1024];
			while (fileInputStream.read( buffer ) > 0) {
				fileOutputStream.write( buffer );
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileInputStream.close();
				fileOutputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
