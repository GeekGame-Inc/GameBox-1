package com.tenone.gamebox.view.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * TODO:�ֻ���Ϣ������ ClassName: TelephoneUtl <br/>
 * date: 2017-6-14 ����4:15:37 <br/>
 *
 * @author John Lie
 * @since JDK 1.6
 */
@SuppressWarnings("deprecation")
public class TelephoneUtils {

    /**
     * TODO:sim���Ƿ���� isSimExist. <br/>
     *
     * @param ctx
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static boolean isSimExist(Context ctx) {
        TelephonyManager manager = (TelephonyManager) ctx
                .getSystemService( "phone" );
			return manager.getSimState() != 1;
		}

    /**
     * TODO:��ѯ�Ƿ��й㲥���� queryBroadcastReceiver. <br/>
     *
     * @param ctx
     * @param actionName
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static boolean queryBroadcastReceiver(Context ctx, String actionName) {
        PackageManager pm = ctx.getPackageManager();
        try {
            Intent intent = new Intent( actionName );
            List<ResolveInfo> apps = pm.queryBroadcastReceivers( intent, 0 );
					return !apps.isEmpty();
				} catch (Exception e) {
        }
        return false;
    }

    /**
     * TODO:��ȡWIFI��ַ getWifiAddress. <br/>
     *
     * @param ctx
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static String getWifiAddress(Context ctx) {
        try {
            WifiManager wifiManager = (WifiManager) ctx
                    .getSystemService( "wifi" );

            if (wifiManager.isWifiEnabled()) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();
                return intToIp( ipAddress );
            }

            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * TODO:��ȡCPU��Ϣ getCPUABI. <br/>
     *
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static String getCPUABI() {
        String abi = Build.CPU_ABI;
		/*abi = (abi == null) || (abi.trim().length() == 0) ? "" : abi;
		try {
			String cpuAbi2 = Build.class.getField("CPU_ABI2").get(null)
					.toString();
			cpuAbi2 = (cpuAbi2 == null) || (cpuAbi2.trim().length() == 0) ? null
					: cpuAbi2;
			if (cpuAbi2 != null)
				abi = abi + "," + cpuAbi2;
		} catch (Exception localException) {
		}*/
        return abi;
    }

    /**
     * TODO:תΪIP��ַ��ʽ intToIp. <br/>
     *
     * @param i
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static String intToIp(int i) {
        return (i & 0xFF) + "." + (i >> 8 & 0xFF) + "." + (i >> 16 & 0xFF)
                + "." + (i >> 24 & 0xFF);
    }

    /**
     * TODO:�Ƿ�Ϊ���� isZh. <br/>
     *
     * @param ctx
     * @return
     * @author John Lie
     * @since JDK 1.6
     */

    public static boolean isZh(Context ctx) {
        Locale lo = ctx.getResources().getConfiguration().locale;
			return lo.getLanguage().equals( "zh" );
		}

    /**
     * TODO:����Ƿ���rootȨ�� hasRootPermission. <br/>
     *
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static boolean hasRootPermission() {
        boolean rooted = true;
        try {
            File su = new File( "/system/bin/su" );
            if (!su.exists()) {
                su = new File( "/system/xbin/su" );
                if (!su.exists())
                    rooted = false;
            }
        } catch (Exception e) {
            rooted = false;
        }
        return rooted;
    }

    /**
     * TODO:��ȡϵͳĬ������ getLanguage. <br/>
     *
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * TODO:��ȡ����MAC��ַ getLocalMacAddress. <br/>
     *
     * @param ctx
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static String getLocalMacAddress(Context ctx) {
        @SuppressLint("WrongConstant")
        WifiManager wifi = (WifiManager) ctx.getSystemService( "wifi" );
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * TODO:��ȡ������������ getNetworkTypeName. <br/>
     *
     * @param ctx
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static String getNetworkTypeName(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService( "connectivity" );
        NetworkInfo info = cm.getActiveNetworkInfo();
        if ((null == info) || (null == info.getTypeName())) {
            return "unknown";
        }
        return info.getTypeName();
    }

    /**
     * TODO:��ȡ����״̬ getNetType. <br/>
     *
     * @param ctx
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static String getNetType(Context ctx) {
        String type = "0";
        ConnectivityManager cm = (ConnectivityManager) ctx
                .getSystemService( "connectivity" );
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            type = "0";
        } else if (info.getType() == 1) {
            type = "1";
        } else if (info.getType() == 0) {
            int subType = info.getSubtype();
            switch (subType) {
                case 0:
                    type = "0";
                    break;
                case 1:
                case 2:
                case 4:
                case 7:
                case 10:
                case 11:
                    type = "2";
                    break;
                case 3:
                case 5:
                case 6:
                case 8:
                case 9:
                case 12:
                case 14:
                case 15:
                    type = "3";
                    break;
                case 13:
                    type = "4";
                    break;
                default:
                    type = "0";
            }
        }

        return type;
    }

    /**
     * TODO:��ȡSIM��վ���� getNetworkOperators. <br/>
     *
     * @param context
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static String getNetworkOperators(Context context) {
        TelephonyManager telManager = (TelephonyManager) context
                .getSystemService( "phone" );
        String operator = telManager.getSimOperator();
        if (operator != null) {
            if ((operator.equals( "46000" )) || (operator.equals( "46002" ))
                    || (operator.equals( "46007" )))
                return "2";
            if (operator.equals( "46001" ))
                return "3";
            if (operator.equals( "46003" )) {
                return "1";
            }
        }
        return "0";
    }

    /**
     * TODO:��ȡIP��ַ GetInetAddress. <br/>
     *
     * @param host
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static String GetInetAddress(String host) {
        String IPAddress = "";
        InetAddress ReturnStr1 = null;
        try {
            ReturnStr1 = InetAddress.getByName( host );
            IPAddress = ReturnStr1.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return IPAddress;
        }
        return IPAddress;
    }

    /**
     * TODO:��ȡAndroid_Id getAndroidId. <br/>
     *
     * @param context
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static String getAndroidId(Context context) {
        if (context == null) {
            return null;
        }
        return Secure.getString( context.getContentResolver(),
                "android_id" );
    }

    public static boolean hasSDCard() {
        return "mounted".equals( Environment.getExternalStorageState() );
    }

    /**
     * TODO:��ȡ�ֻ�Ʒ�� getDeviceBrandName. <br/>
     *
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static String getDeviceBrandName() {
        String temp = Build.MANUFACTURER;
        return temp.replaceAll( " ", "" );
    }

    /**
     * TODO:��ȡ�豸��Ϣ getDeviceModel. <br/>
     *
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static String getDeviceModel() {
        String temp = Build.MODEL;
        if (TextUtils.isEmpty( temp.replaceAll( "��", "" ).replaceAll( "\\+", "" ) )) {
            return "δ֪";
        }
        return temp.replaceAll( " ", "" ).replaceAll( "\\+", "" );
    }

    /**
     * TODO:��ȡ�豸���� getDeviceName. <br/>
     *
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static String getDeviceName() {
        String temp = Build.BRAND;
        if (TextUtils.isEmpty( temp.replaceAll( "��", "" ).replaceAll( "\\+", "" ) )) {
            return "δ֪";
        }
        return temp.replaceAll( " ", "" ).replaceAll( "\\+", "" );
    }

    public static boolean isEmulator1(Context context) {
			return Build.CPU_ABI.equalsIgnoreCase( "x86" );
		}

    /**
     * TODO:��ȡ��Ļ�ֱ��� getDisplayMetrics. <br/>
     *
     * @param context
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static String getDisplayMetrics(Context context) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics( mDisplayMetrics );
        StringBuilder sb = new StringBuilder();
        sb.append( mDisplayMetrics.heightPixels ).append( "*" )
                .append( mDisplayMetrics.widthPixels );
        return sb.toString();
    }

    /**
     * TODO:��ȡ����SDK�İ汾 getAndroidSDKVersion. <br/>
     *
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static int getAndroidSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * TODO:wifi�Ƿ�� isWifiEnable. <br/>
     *
     * @param context
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static boolean isWifiEnable(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService( "wifi" );
        if (wifiManager != null) {
            int wifiState = wifiManager.getWifiState();
            return wifiState == 3;
        }
        return false;
    }

    /**
     * ��ȡ��ǰ�ֻ�ϵͳ���ԡ�
     *
     * @return ���ص�ǰϵͳ���ԡ����磺��ǰ���õ��ǡ�����-�й������򷵻ء�ZH-CN��
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * ��ȡ��ǰϵͳ�ϵ������б�(Locale�б�)
     *
     * @return �����б�
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * ��ȡ��ǰ�ֻ�ϵͳ�汾��
     *
     * @return ϵͳ�汾��
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * ��ȡ�ֻ��ͺ�
     *
     * @return �ֻ��ͺ�
     */
    public static String getSystemModel() {
        String model = Build.MODEL;
        model = model.replaceAll( " ", "" );
        return model;
    }

    /**
     * ��ȡ�ֻ�����
     *
     * @return �ֻ�����
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * ��ȡMAC��ַ getMac:(������һ�仰�����������������). <br/>
     *
     * @return
     * @author John Lie
     * @since JDK 1.6
     */
    public static String getMac() {
        String macSerial = "";
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address" );
            InputStreamReader ir = new InputStreamReader( pp.getInputStream() );
            LineNumberReader input = new LineNumberReader( ir );
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// ȥ�ո�
                    break;
                }
            }
        } catch (IOException ex) {
            // ����Ĭ��ֵ
            ex.printStackTrace();
        }
        return macSerial;
    }

    @SuppressLint("NewApi")
    public static String getMac_() {
        try {
            List<NetworkInterface> all = Collections.list( NetworkInterface
                    .getNetworkInterfaces() );
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase( "wlan0" ))
                    continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append( String.format( "%02X:", b ) );
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt( res1.length() - 1 );
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public static String getImei(Context context) {
        String m_szImei = SpUtil.getImei();
        if (!TextUtils.isEmpty( m_szImei )) {
            return m_szImei;
        }
        try {
            TelephonyManager TelephonyMgr = (TelephonyManager) context
                    .getSystemService( Context.TELEPHONY_SERVICE );
            m_szImei = TelephonyMgr.getDeviceId();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        String m_szDevIDShort = "35" + Build.BOARD.length() % 10
                + Build.BRAND.length() % 10 + Build.CPU_ABI.length() % 10
                + Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10
                + Build.HOST.length() % 10 + Build.ID.length() % 10
                + Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10
                + Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10
                + Build.TYPE.length() % 10 + Build.USER.length() % 10;
        String m_szAndroidID = Secure.getString( context.getContentResolver(),
                Secure.ANDROID_ID );
        WifiManager wm = (WifiManager) context
                .getSystemService( Context.WIFI_SERVICE );
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        String m_szBTMAC = "";
        try {
            BluetoothAdapter m_BluetoothAdapter = null;
            m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            m_szBTMAC = m_BluetoothAdapter.getAddress();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        String m_szLongID = m_szImei + m_szDevIDShort + m_szAndroidID
                + m_szWLANMAC + m_szBTMAC;
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance( "MD5" );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update( m_szLongID.getBytes(), 0, m_szLongID.length() );
        byte p_md5Data[] = m.digest();
        String m_szUniqueID = "";
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
            if (b <= 0xF)
                m_szUniqueID += "0";
            m_szUniqueID += (Integer.toHexString( b ) + "");
        }
        m_szUniqueID = m_szUniqueID.toUpperCase();
        SpUtil.setImei( m_szUniqueID );
        return m_szUniqueID;
    }

    public static String getszImei(Context context) {
        String m_szImei = "";
        try {
            TelephonyManager TelephonyMgr = (TelephonyManager) context
                    .getSystemService( Context.TELEPHONY_SERVICE );
            m_szImei = TelephonyMgr.getDeviceId();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return m_szImei;
    }
}