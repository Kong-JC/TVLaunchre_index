package com.kong.tvlaunchre_index;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

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

    private static final String TAG = "Utils";

//    private Context mContext;

//    private static Utils instance;
//    private Utils (Context context){
//        this.mContext = context;
//    }
//    public static synchronized Utils getInstance(Context context) {
//        if (instance == null) {
//            instance = new Utils(context);
//        }
//        return instance;
//    }

    // ----- WIFI -----
    public static boolean switchWIFI(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        } else {
            wifiManager.setWifiEnabled(true);
        }
        return wifiManager.isWifiEnabled();
    }

    public static boolean wifiIsOpen(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

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

    public static boolean connWiFi(Context context, ScanResult scanResult, String password) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "\"" + scanResult.SSID + "\"";
        config.preSharedKey = "\"" + password + "\"";
        config.hiddenSSID = true;
        config.status = WifiConfiguration.Status.ENABLED;
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        int netId = manager.addNetwork(config);
        boolean isConn = manager.enableNetwork(netId, true);
//        if (isConn && checked) {
//            SPUtils.putString(context, scanResult.SSID, new Gson().toJson(config));
//            SPUtils.putBoolean(context, scanResult.SSID + "-" + SPUtils.IS_SAVE_WIFI_KEY, true);
//        }
        return isConn;
    }

    public static String getCurrentWiFiSSID(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        int wifiState = wifiMgr.getWifiState();
        WifiInfo info = wifiMgr.getConnectionInfo();
        String wifiId = (info != null ? info.getSSID() : null);

        Log.i(TAG, " -=-=-=-=- getCurrentWiFiSSID: wifiId:" + wifiId);
        
        String[] ss = wifiId.split("\"");

        for (String s : ss) {
            Log.i(TAG, " -=-=-=-=- getCurrentWiFiSSID: s:" + s);
        }
                
        return wifiId.split("\"").length > 1 ? wifiId.split("\"")[1] : wifiId;
    }

    public static String getCurrentWiFiNetId(Context context) {
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        int wifiState = wifiMgr.getWifiState();
        WifiInfo info = wifiMgr.getConnectionInfo();
        String netId = (info != null ? info.getNetworkId() + "" : null);
        return netId;
    }

    public static void autoConnWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled() && wifiManager.startScan()) {//扫描可用的无线网络
            List<ScanResult> scanResultList = wifiManager.getScanResults();
            for (ScanResult scanResult : scanResultList) {
                String password = SPUtils.getString(context, scanResult.SSID);
                boolean isAutoConn = SPUtils.getBoolean(context, scanResult.SSID + "-" + SPUtils.IS_SAVE_WIFI_KEY);
//                Log.i(TAG, " -=-=-=-=- initView: wifiConfig是否为空:" + (wifiConfiguration == null) + " | isAutoConn:" + isAutoConn + " | 以太网是否连接:" + Utils.getEthernetIsConn(context));
//                Toast.makeText(context, "wifiConfig是否为空:" + (wifiConfiguration == null) +
//                        "\nisAutoConn:" + isAutoConn + "\n以太网是否连接:" +
//                        Utils.getEthernetIsConn(context), Toast.LENGTH_SHORT).show();
                if (password.length() != 0 && isAutoConn && !getEthernetIsConn(context)) {
                    boolean isConn = connWiFi(context, scanResult, password);
                    Toast.makeText(context, "WIFI是否连接:" + isConn, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public static boolean disconnectWifi(Context context, int netId) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.disableNetwork(netId);
        
        
        return wifiManager.disconnect();
    }
    public static boolean removeWifi(Context context, int netId) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.disableNetwork(netId);
        wifiManager.removeNetwork(netId);
        return wifiManager.disconnect();
    }

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

    public static String getModel() {
        return Build.MODEL;
    }

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
