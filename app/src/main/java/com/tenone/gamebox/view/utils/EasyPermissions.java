package com.tenone.gamebox.view.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.tenone.gamebox.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;

public class EasyPermissions {
	public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
	public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
	public static final String PERMISSION_INTERNET = Manifest.permission.INTERNET;
	public static final String PERMISSION_WIFI = Manifest.permission.ACCESS_WIFI_STATE;
	public static final String PERMISSION_BLUETOOTH = Manifest.permission.BLUETOOTH;
	public static final String PERMISSION_RECEIVE_BOOT_COMPLETED = Manifest.permission.RECEIVE_BOOT_COMPLETED;

	public static final String PERMISSION_RESTART_PACKAGES = Manifest.permission.RESTART_PACKAGES;

	public static final String PERMISSION_ACCESS_NETWORK_STATE = Manifest.permission.ACCESS_NETWORK_STATE;

	public static final String PERMISSION_INSTALL_SHORTCUT = Manifest.permission.INSTALL_SHORTCUT;
	public static final String PERMISSION_UNMOUNT_FILESYSTEMS = Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS;
	public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
	public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
	public static final String INSTALL_PACKAGES = Manifest.permission.INSTALL_PACKAGES;

	public static void initEasyPermissionsHintMap() {
		hintMap.put( PERMISSION_READ_PHONE_STATE, "û���ֻ���ϢȨ��,Ϊ��������ʹ�ú���,��������->Ȩ�޹���->���κ��Ӵ��ֻ���ϢȨ��" );
		hintMap.put( PERMISSION_READ_EXTERNAL_STORAGE, "û�д洢Ȩ��,Ϊ��������ʹ�ú���,��������->Ȩ�޹���->���κ��Ӵ򿪴洢Ȩ��" );
		hintMap.put( PERMISSION_WRITE_EXTERNAL_STORAGE, "û�д洢Ȩ��,Ϊ��������ʹ�ú���,��������->Ȩ�޹���->���κ��Ӵ򿪴洢Ȩ��" );
		hintMap.put( PERMISSION_CAMERA, "û��ʹ�������豸Ȩ��,Ϊ��������ʹ�ú���,��������->Ȩ�޹���->���κ��Ӵ�ʹ�������豸Ȩ��" );
		hintMap.put( PERMISSION_INTERNET, "û�з�������Ȩ��,Ϊ��������ʹ�ú���,��������->Ȩ�޹���->���κ��Ӵ򿪷�������Ȩ��" );
		hintMap.put( PERMISSION_WIFI, "�������ı�Wi-Fi����״̬" );
		hintMap.put( PERMISSION_BLUETOOTH, "�������������Թ��������豸" );
		hintMap.put( PERMISSION_RECEIVE_BOOT_COMPLETED, "��ȡ�豸��" );
		hintMap.put( PERMISSION_RESTART_PACKAGES, "�����������������������" );
		hintMap.put( PERMISSION_ACCESS_NETWORK_STATE, "��ȡ������Ϣ״̬" );
		hintMap.put( INSTALL_PACKAGES, "û���������װӦ��Ȩ��,Ϊ��������ʹ�ú���,��������->Ȩ�޹���->���κ��Ӵ��������װӦ��" );
	}

	public static HashMap<String, String> hintMap = new HashMap<String, String>();


	public static final int SETTINGS_REQ_CODE = 16061;

	private static final String TAG = "EasyPermissions";


	public interface PermissionCallbacks extends
			ActivityCompat.OnRequestPermissionsResultCallback {

		void onPermissionsGranted(int requestCode, List<String> perms);

		void onPermissionsDenied(int requestCode, List<String> perms);

	}

	public static boolean hasPermissions(Context context, String... perms) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			Log.w( TAG, "hasPermissions: API version < M, returning true by default" );
			return true;
		}

		for (String perm : perms) {
			boolean hasPerm = (ContextCompat.checkSelfPermission( context, perm ) ==
					PackageManager.PERMISSION_GRANTED);
			if (!hasPerm) {
				return false;
			}
		}

		return true;
	}

	public static void requestPermissions(final Object object, String rationale,
																				final int requestCode, final String... perms) {
		requestPermissions( object, rationale,
				android.R.string.ok,
				android.R.string.cancel,
				requestCode, perms );
	}


	public static void requestPermissions(final Object object, final int requestCode, final String... perms) {
		requestPermissions( object, "\b\b\b\bΪ��������ʹ��" + getActivity( object ).getString( R.string.app_name ) + "����Ҫ��ͬ��Ӧ�û�ȡ�����Ȩ��", android.R.string.ok, android.R.string.cancel, requestCode, perms );
	}

	public static void requestPermissions(final Object object, String rationale, @StringRes int positiveButton, @StringRes int negativeButton,
																				final int requestCode, final String... perms) {
		checkCallingObjectSuitability( object );
		boolean shouldShowRationale = false;
		for (String perm : perms) {
			shouldShowRationale = shouldShowRationale || shouldShowRequestPermissionRationale( object, perm );
		}
		if (shouldShowRationale) {
			Activity activity = getActivity( object );
			if (null == activity) {
				return;
			}
			DialogUtils.showRationDialog( activity,
					(dialog, which) -> {
						switch (which) {
							case DialogInterface.BUTTON_POSITIVE:
								executePermissionsRequest( object, perms, requestCode );
								break;
							case DialogInterface.BUTTON_NEGATIVE:
								if (object instanceof PermissionCallbacks) {
									((PermissionCallbacks) object).onPermissionsDenied( requestCode, Arrays.asList( perms ) );
								}
								break;
						}
					}, "Ȩ������", rationale, activity.getText( positiveButton ).toString(), activity.getText( negativeButton ).toString() );
		} else {
			executePermissionsRequest( object, perms, requestCode );
		}
	}


	public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults, Object object) {
		checkCallingObjectSuitability( object );
		ArrayList<String> granted = new ArrayList<>();
		ArrayList<String> denied = new ArrayList<>();
		for (int i = 0; i < permissions.length; i++) {
			String perm = permissions[i];
			if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
				granted.add( perm );
			} else {
				denied.add( perm );
			}
		}
		if (!granted.isEmpty()) {
			if (object instanceof PermissionCallbacks) {
				((PermissionCallbacks) object).onPermissionsGranted( requestCode, granted );
			}
		}
		if (!denied.isEmpty()) {
			if (object instanceof PermissionCallbacks) {
				((PermissionCallbacks) object).onPermissionsDenied( requestCode, denied );
			}
		}
		if (!granted.isEmpty() && denied.isEmpty()) {
			runAnnotatedMethods( object, requestCode );
		}
	}

	public static boolean checkDeniedPermissionsNeverAskAgain(final Object object, String rationale, @StringRes int positiveButton,
																														@StringRes int negativeButton, List<String> deniedPerms) {
		return checkDeniedPermissionsNeverAskAgain( object, rationale,
				positiveButton, negativeButton, null, deniedPerms );
	}

	public static boolean checkDeniedPermissionsNeverAskAgain(final Object object, String rationale, @StringRes int positiveButton, @StringRes int negativeButton, @Nullable DialogInterface.OnClickListener negativeButtonOnClickListener, List<String> deniedPerms) {
		boolean shouldShowRationale;
		for (String perm : deniedPerms) {
			shouldShowRationale = shouldShowRequestPermissionRationale( object, perm );
			if (!shouldShowRationale) {
				final Activity activity = getActivity( object );
				if (null == activity) {
					return true;
				}
				DialogUtils.showRationDialog( activity,
						(dialog1, which) -> {
							switch (which) {
								case DialogInterface.BUTTON_POSITIVE:
									Intent intent = new Intent( Settings.ACTION_APPLICATION_DETAILS_SETTINGS );
									Uri uri = Uri.fromParts( "package", activity.getPackageName(), null );
									intent.setData( uri );
									startAppSettingsScreen( object, intent );
									break;
								case DialogInterface.BUTTON_NEGATIVE:
									if (negativeButtonOnClickListener != null) {
										negativeButtonOnClickListener.onClick( dialog1, which );
									}
									break;
							}
						}, "Ȩ������", rationale, activity.getText( positiveButton ).toString(), activity.getText( negativeButton ).toString() );
				return true;
			}
		}
		return false;
	}


	@TargetApi(23)
	private static boolean shouldShowRequestPermissionRationale(Object object, String perm) {
		if (object instanceof Activity) {
			return ActivityCompat.shouldShowRequestPermissionRationale( (Activity) object, perm );
		} else if (object instanceof Fragment) {
			return ((Fragment) object).shouldShowRequestPermissionRationale( perm );
		} else if (object instanceof android.app.Fragment) {
			return ((android.app.Fragment) object).shouldShowRequestPermissionRationale( perm );
		} else {
			return false;
		}
	}

	@TargetApi(23)
	private static void executePermissionsRequest(Object object, String[] perms, int requestCode) {
		checkCallingObjectSuitability( object );
		if (object instanceof Activity) {
			ActivityCompat.requestPermissions( (Activity) object, perms, requestCode );
		} else if (object instanceof Fragment) {
			((Fragment) object).requestPermissions( perms, requestCode );
		} else if (object instanceof android.app.Fragment) {
			((android.app.Fragment) object).requestPermissions( perms, requestCode );
		}
	}

	@TargetApi(11)
	private static Activity getActivity(Object object) {
		if (object instanceof Activity) {
			return ((Activity) object);
		} else if (object instanceof Fragment) {
			return ((Fragment) object).getActivity();
		} else if (object instanceof android.app.Fragment) {
			return ((android.app.Fragment) object).getActivity();
		} else {
			return null;
		}
	}

	@TargetApi(11)
	private static void startAppSettingsScreen(Object object,
																						 Intent intent) {
		if (object instanceof Activity) {
			((Activity) object).startActivityForResult( intent, SETTINGS_REQ_CODE );
		} else if (object instanceof Fragment) {
			((Fragment) object).startActivityForResult( intent, SETTINGS_REQ_CODE );
		} else if (object instanceof android.app.Fragment) {
			((android.app.Fragment) object).startActivityForResult( intent, SETTINGS_REQ_CODE );
		}
	}

	private static void runAnnotatedMethods(Object object, int requestCode) {
		Class clazz = object.getClass();
		if (isUsingAndroidAnnotations( object )) {
			clazz = clazz.getSuperclass();
		}
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent( AfterPermissionGranted.class )) {
				// Check for annotated methods with matching request code.
				AfterPermissionGranted ann = method.getAnnotation( AfterPermissionGranted.class );
				if (ann.value() == requestCode) {
					// Method must be void so that we can invoke it
					if (method.getParameterTypes().length > 0) {
						throw new RuntimeException(
								"Cannot execute non-void method " + method.getName() );
					}
					try {
						// Make method accessible if private
						if (!method.isAccessible()) {
							method.setAccessible( true );
						}
						method.invoke( object );
					} catch (IllegalAccessException e) {
						Log.e( TAG, "runDefaultMethod:IllegalAccessException", e );
					} catch (InvocationTargetException e) {
						Log.e( TAG, "runDefaultMethod:InvocationTargetException", e );
					}
				}
			}
		}
	}

	private static void checkCallingObjectSuitability(Object object) {
		boolean isActivity = object instanceof Activity;
		boolean isSupportFragment = object instanceof Fragment;
		boolean isAppFragment = object instanceof android.app.Fragment;
		boolean isMinSdkM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
		if (!(isSupportFragment || isActivity || (isAppFragment && isMinSdkM))) {
			if (isAppFragment) {
				throw new IllegalArgumentException( "Target SDK needs to be greater than 23 if caller is android.app.Fragment" );
			} else {
				throw new IllegalArgumentException( "Caller must be an Activity or a Fragment." );
			}
		}
	}

	private static boolean isUsingAndroidAnnotations(Object object) {
		if (!object.getClass().getSimpleName().endsWith( "_" )) {
			return false;
		}
		try {
			Class clazz = Class.forName( "org.androidannotations.api.view.HasViews" );
			return clazz.isInstance( object );
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
