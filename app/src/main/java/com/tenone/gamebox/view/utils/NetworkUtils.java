/**
 * Project Name:GameBox
 * File Name:NetworkUtils.java
 * Package Name:com.tenone.gamebox.view.utils
 * Date:2017-3-14����4:44:53
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 */

package com.tenone.gamebox.view.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkUtils {
    public static String GetNetworkType(Context cxt) {
        String strNetworkType = "";
        NetworkInfo networkInfo = ((ConnectivityManager) cxt
                .getSystemService( Context.CONNECTIVITY_SERVICE ))
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: // api<9 : replace by
                    case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace
                        // by12
                    case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace
                        // by15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE: // api<11 : replace by13
                        strNetworkType = "4G";
                        break;
                    default:
                        if (_strSubTypeName.equalsIgnoreCase( "TD-SCDMA" )
                                || _strSubTypeName.equalsIgnoreCase( "WCDMA" )
                                || _strSubTypeName.equalsIgnoreCase( "CDMA2000" )) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }
                        break;
                }
            }
        }
        return strNetworkType;
    }

    public static String getLocalIpAddress() {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address
                            && !inetAddress.isLoopbackAddress()) {
                        ip = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e( "WifiPreferenceIpAddress", ex.toString() );
        }
        return ip;
    }
}
