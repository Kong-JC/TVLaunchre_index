package com.kong.tvlaunchre_index;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * Created by Kong on 2017/4/11.
 */

public class Utils {

    // 拿到wifi扫描列表
    public static List<ScanResult> getWiFiSignalSourceList(Context context){
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> results = wm.getScanResults();
        return results;
    }
    
}
