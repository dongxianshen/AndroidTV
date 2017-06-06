package com.dy.androidiptv.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;
import java.security.MessageDigest;

/**
 * @author Droid
 */
public class NetWorkUtil {
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	public static final int STATE_DISCONNECT = 0;
	public static final int STATE_WIFI = 1;
	public static final int STATE_MOBILE = 2;
	// wifi 热点
	public static final int WIFI_AP_STATE_DISABLING = 10;
	public static final int WIFI_AP_STATE_DISABLED = 11;
	public static final int WIFI_AP_STATE_ENABLING = 12;
	public static final int WIFI_AP_STATE_ENABLED = 13;
	public static final int WIFI_AP_STATE_FAILED = 14;

	private static int getWifiApState(Context mContext) {
		WifiManager wifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		try {
			Method method = wifiManager.getClass().getMethod("getWifiApState");
			int i = (Integer) method.invoke(wifiManager);
			return i;
		} catch (Exception e) {
			return WIFI_AP_STATE_FAILED;
		}
	}

	private static boolean isApEnabled(Context mContext) {
		int state = getWifiApState(mContext);
		return WIFI_AP_STATE_ENABLING == state
				|| WIFI_AP_STATE_ENABLED == state;
	}

	private static boolean isWifiApEnabled(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		try {
			Method method = wifiManager.getClass().getMethod("isWifiApEnabled");
			method.setAccessible(true);
			return (Boolean) method.invoke(wifiManager);

		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String concatUrlParams() {

		return null;
	}

	public static String encodeUrl() {

		return null;
	}

	public static boolean isNetWorkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] nis = cm.getAllNetworkInfo();
		if (nis != null) {
			for (NetworkInfo ni : nis) {
				if (ni != null) {
					if (ni.isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isWifiConnected(Context context) {
		WifiManager wifiMgr = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		boolean isWifiEnable = wifiMgr.isWifiEnabled();
		return isWifiEnable;
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
		return false;
	}

	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String md5Encode(String origin) {
		String resultString = null;

		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = new String(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return resultString;
	}

	public static String md5EncodeToHexString(String origin) {
		String resultString = null;

		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return resultString;
	}

	public static int getNetworkState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// Wifi
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return STATE_WIFI;
		}

		// 3G
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return STATE_MOBILE;
		}
		return STATE_DISCONNECT;
	}
}
