package com.kong.tvlaunchre_index;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Kong on 2017/4/11.
 */

public class Utils {

    // ----- WIFI -----
    public static boolean wifiIsOpen(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wm.getWifiState() == WifiManager.WIFI_STATE_ENABLED ? true : false;
    }

    // 拿到wifi扫描列表
    public static List<ScanResult> getWiFiSignalSourceList(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> results = wm.getScanResults();
        return results;
    }

    public static boolean getWiFiIsConn(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo.State state = networkInfo.getState();
        if (state == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }

    public static String getWiFiSSID(Context context) {
        WifiManager my_wifiManager = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE));
        WifiInfo wifiInfo = my_wifiManager.getConnectionInfo();
        return getWiFiIsConn(context) ? wifiInfo.getSSID() : "开启";
    }

//    private static String intToIp(int paramInt) {
//        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
//                + (0xFF & paramInt >> 24);
//    }

    // ----- Ethernet -----
    public static boolean getEthernetIsConn(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        NetworkInfo.State state = networkInfo.getState();
        if (state == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }

    public static String getEthernetIPAddress(boolean useIPv4) {
        try {
            for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); ) {
                NetworkInterface ni = nis.nextElement();
                if (!ni.isUp()) continue;
                for (Enumeration<InetAddress> addresses = ni.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        boolean isIPv4 = hostAddress.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4) return hostAddress;
                        } else {
                            if (!isIPv4) {
                                int index = hostAddress.indexOf('%');
                                return index < 0 ? hostAddress.toUpperCase() : hostAddress.substring(0, index).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 从系统文件中获取以太网MAC地址
    public static String getEthernetMac() {
        try {
            StringBuffer fileData = new StringBuffer(1000);
            BufferedReader reader = new BufferedReader(new FileReader("/sys/class/net/eth0/address"));
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
            }
            reader.close();
            return fileData.toString().toUpperCase().substring(0, 17);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ----- 设备信息 -----
//    public static String getAllInfo(Context context){
//        String phoneInfo = null;
//        phoneInfo = " -=-=-=-=- 基本信息 -=-=-=-=-";
//        phoneInfo += "\n机型: " + getModel();
//        phoneInfo += "\n固件版本: " + getRelease();
//
//        //DISPLAY Rom的名字 例如 Flyme 1.1.2（魅族rom） &nbsp;JWR66V（Android nexus系列原生4.3rom）
//        phoneInfo += "\n -=-=-=-=- Rom -=-=-=-=-";
//        phoneInfo += "\n获取设备显示的版本号: " + Build.DISPLAY;
//
//        //指纹
//        phoneInfo += "\n -=-=-=-=- 指纹 -=-=-=-=-";
//        phoneInfo += "\n设备的唯一标识。由设备的多个信息拼接合成。: " + Build.FINGERPRINT;
//
//        return phoneInfo;
//    }

    // 获取机型
    public static String getModel() {
        return Build.MODEL;
    }

    // 获取固件版本
    public static String getRelease() {
        return Build.VERSION.RELEASE;
    }

    public static String getRomInfo() {
        return Build.DISPLAY;
    }

    public static String getUUID() {
        return Build.FINGERPRINT;
    }

    // Tools
    public static Intent getAppIntent(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> applicationsList = context.getPackageManager().getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        Intent intent = null;
        for (ApplicationInfo applicationInfo : applicationsList) {
            if (applicationInfo.packageName.equals(packageName)) {
                intent = pm.getLaunchIntentForPackage(applicationInfo.packageName);
            }
        }
        applicationsList.clear();
        applicationsList = null;
        pm = null;
        return intent;
    }
    
    
}
