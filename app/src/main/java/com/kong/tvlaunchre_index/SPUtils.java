package com.kong.tvlaunchre_index;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Kong on 2017/4/12.
 */

public class SPUtils {

    private static final String TAG = "SharedPreferencesUtil";

    private static final String SP_FILE_NAME = "TV_SP_Data";
    
    public static final String ADVANCED_SETTING_URL_KEY = "SpecifyURL"; // 默认网址
    public static final String ADVANCED_SETTINGS_PASSWORD_KEY = "AdvancedSettingsPassword"; // 高级设置密码
    
    public static final String SCREEN_ZOOM = "ScreenZoom"; // 屏幕缩放
    public static final String SCREEN_ORIENTATION = "ScreenOrientation"; // 屏幕方向
    public static final String THE_BEST_DISPLAY_MODE = "TheBestDisplayMode"; // 最佳显示模式
    public static final String CUSTOM_OUTPUT_MODE = "CustomOutputMode"; // 自定义输出模式
    
    public static final String MOBILE_TELECONTROL = "MobileTelecontrol"; // 手机遥控
    public static final String GOOGLE_TV_TELECONTROL = "GoogleTVTelecontrol"; // Google TV 遥控
    public static final String AUTOMATICALLY_SWITCH_THE_DIGITAL_AUDIO_OUTPUT = "AutomaticallySwitchTheDigitalAudioOutput"; // 自动切换数字音频输出
    public static final String SOUND_SET = "SoundSet"; // 声音设置

    // 存放高级设置密码
    public static boolean putString(Context context, String key, String value) {
        SharedPreferences.Editor spEditor = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE).edit();
        spEditor.putString(key, value);
        return spEditor.commit();
    }

    // 获取高级设置密码
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    // 保存SN 状态
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences.Editor spEditor = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE).edit();
        spEditor.putBoolean(key, value);
        spEditor.commit();
    }

    // 获取SN 状态
    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }
//    // 保存下载记录
//    public static void putDownloadInfo(Context context, String key, String value) {
//        SharedPreferences.Editor spEditor = context.getSharedPreferences("DownloadData", Context.MODE_PRIVATE).edit();
//        spEditor.putString(key, value);
//        spEditor.commit();
//    }
//
//    // 获取下载记录
//    public static Map<String, String> getDownloadInfo(Context context) {
//        SharedPreferences sp = context.getSharedPreferences("DownloadData", Context.MODE_PRIVATE);
//        return (Map<String, String>) sp.getAll();
//    }
//
//    // 获取移除记录
//    public static boolean remoString(Context context,String key) {
//        SharedPreferences.Editor sp = context.getSharedPreferences("DownloadData", Context.MODE_PRIVATE).edit();
//        sp.remove(key);
//        return true;
//    }
    
}
